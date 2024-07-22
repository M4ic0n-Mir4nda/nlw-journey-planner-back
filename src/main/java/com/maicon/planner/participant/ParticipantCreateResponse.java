package com.maicon.planner.participant;

import java.util.UUID;

// classe usada para retornar/receber o ID do participante recem criado
public record ParticipantCreateResponse(UUID id) {
}
