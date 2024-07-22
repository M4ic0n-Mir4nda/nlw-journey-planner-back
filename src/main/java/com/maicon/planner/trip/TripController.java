package com.maicon.planner.trip;

import com.maicon.planner.activity.ActivityData;
import com.maicon.planner.activity.ActivityRequestPayload;
import com.maicon.planner.activity.ActivityResponse;
import com.maicon.planner.activity.ActivityService;
import com.maicon.planner.link.LinkData;
import com.maicon.planner.link.LinkRequestPayload;
import com.maicon.planner.link.LinkResponse;
import com.maicon.planner.link.LinkService;
import com.maicon.planner.participant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private LinkService linkService;

    // TRIPS

    // Metodo que cria uma nova viagem
    @PostMapping
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);

        this.tripRepository.save(newTrip);

        // registerParticipantsToEvent > Envio da lista todos os participantes da viagem
        this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
    }

    // Método de confirmação de viagem
    @GetMapping("/{id}/confirm") // @PathVariable > é usado para mapear o parametro do metodo e pegar o valor do
    // id
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setIsConfirmed(true);

            this.tripRepository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(id);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    // Metodo que retorna uma viagem especifica por ID
    @GetMapping("/{id}") // @PathVariable > é usado para mapear o parametro do metodo e pegar o valor do
                         // id
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        // Condicional > Se tiver informação dentro de trip é usado o method reference
        // para montar uma resposta do tipo
        // ResponseEntity com status OK e ai ele adiciona o trip dentro do body / caso
        // contrario se o trip for nulo(null) com o
        // id passado ele vai montar um ResponseEntity com status NotFound e vai
        // retornar essa resposta
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Metodo de atualização de informações da viagem
    @PutMapping("/{id}") // @PathVariable > é usado para mapear o parametro do metodo e pegar o valor do
                         // id
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setDestination(payload.destination());

            this.tripRepository.save(rawTrip);

            return ResponseEntity.ok(rawTrip);
        }

        return ResponseEntity.notFound().build();
    }

    // ACTIVITY

    // Método de cadastrar atividades
    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id,
            @RequestBody ActivityRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        // Verifica se a viagem existe
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);

            return ResponseEntity.ok(activityResponse);
        }

        return ResponseEntity.notFound().build();
    }

    // Metodo de listar atividades
    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
        List<ActivityData> activityDataList = this.activityService.getAllActivitiesFromId(id);

        return ResponseEntity.ok(activityDataList);
    }

    // PARTICIPANT

    // Metodo de convidar participantes/registrar um participante mesmo sem
    // confirmação dele
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id,
            @RequestBody ParticipantRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        // Verifica se a viagem existe
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            // Cria um novo participante para ela
            ParticipantCreateResponse participantResponse = this.participantService
                    .registerParticipantEvent(payload.email(), rawTrip);

            // Se a trip(viagem) estiver confirmada, caso esteja ela trigga/envia o email
            // para o participante
            if (rawTrip.getIsConfirmed())
                this.participantService.triggerConfirmationEmailToParticipant(payload.email());

            return ResponseEntity.ok(participantResponse);
        }

        return ResponseEntity.notFound().build();
    }

    // Metodo de listar participantes
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantData> participantsList = this.participantService.getAllParticipantsFromEvent(id);

        return ResponseEntity.ok(participantsList);
    }

    // LINKS

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            LinkResponse linkResponse = this.linkService.registerLink(payload, rawTrip);

            return ResponseEntity.ok(linkResponse);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
        List<LinkData> LinkDataList = this.linkService.getAllLinksFromTrip(id);

        return ResponseEntity.ok(LinkDataList);
    }

}
