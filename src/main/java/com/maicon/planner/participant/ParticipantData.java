package com.maicon.planner.participant;

import java.util.UUID;

// Faz uma filtragem de informações que é da entidade Participant, além de tirar a relação direta de Participant com Trip, ou
// seja esta sendo transferido de um escopo para o outro um objeto de transferencia, usado somente para transferencia de dados.
// Aonde faz com que o retorno sejá apenas as informações a baixo.

// OBS: É recomendado pois usar diretamente a entidade Participant pode causar algum risco futuro caso sejá feita qualquer
// alteração no objeto
public record ParticipantData(UUID id, String name, String email, Boolean isConfirmed) {
}
