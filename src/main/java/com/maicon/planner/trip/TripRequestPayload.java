package com.maicon.planner.trip;

import java.util.List;

// Porque Record: E a forma mais facil de gerar Getters e Setter automaticamente pois não tera alteração de valores após criados
public record TripRequestPayload(String destination, String starts_at, String ends_at, List<String> emails_to_invite,
                                 String owner_email, String owner_name) {

}
