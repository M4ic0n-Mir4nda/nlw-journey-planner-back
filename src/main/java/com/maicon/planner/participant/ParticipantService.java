package com.maicon.planner.participant;

import com.maicon.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip){
                                                            // Passa por cada participante e adiciona/cria um novo Participante
                                                            // na lista com email e a viagem(trip), e como por padrão e retornado
                                                            // um Objeto do tipo String com stream().map(), por fim o toList()
                                                            // transforma tudo isso num objeto do tipo List
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();

        // Salva a lista de todos os participantes
        this.participantRepository.saveAll(participants);

        System.out.println(participants.get(0).getId());
    }

    // Metodo que registra um participante novo no evento e salva na base de dados, após isso retorna o id para a class record
    // ParticipantCreateResponse
    public ParticipantCreateResponse registerParticipantEvent(String email, Trip trip) {
        Participant newParticipant = new Participant(email, trip);
        this.participantRepository.save(newParticipant);

        // Retorna exatamente o id do novo participante
        return new ParticipantCreateResponse(newParticipant.getId());
    }

    // Responsavel por recuperar todos os participantes de uma viagem e disparar os e-mails
    public void triggerConfirmationEmailToParticipants(UUID tripId){}

    // Responsavel por recuperar um participantes especifico de uma viagem e disparar o e-mails
    public void triggerConfirmationEmailToParticipant(String email){}

    public List<ParticipantData> getAllParticipantsFromEvent(UUID tripId) {
                                                            // Pega cada um dos participantes encontrados, cria novos
                                                            // ParticipantsData aonde é passado somente os argumentos que a
                                                            // classe record precisa(id, name, email e isConfirmed) e por fim
                                                            // transforma num objeto do tipo List(toList())
        return this.participantRepository.findByTripId(tripId).stream().map(participant -> new ParticipantData(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.getIsConfirmed())).toList();
    }
}
