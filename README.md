| :placard: Vitrine.Dev |     |
| -------------  | --- |
| :sparkles: Nome        | **Planner**
| :label: Tecnologias | Java, Spring Boot, Spring Boot JPA, Flyway, H2 Database (tecnologias utilizadas)

## Detalhes do projeto

Este projeto é uma Agenda de Roteiro de Viagem desenvolvida durante o curso gratuito da Rocketseat. A aplicação permite que os usuários planejem seus roteiros de viagem, adicionem destinos, datas e outras informações importantes.


| Método | Endpoint | Descrição |
| --- | --- | --- |
| GET | /trips/:id | Retorna uma viagem com o ID especificado |
| POST | /trips | Cria uma nova viagem |
| GET | /trips/:id/confirm | Confirmação de viagem |
| POST | /participants/:id/confirm | Confirmação de participante no evento |
| PUT | /trips/:id | Atualização de informações da viagem |
| POST | /trips/:id/activities | Cria uma nova atividade em viagem |
| GET | /trips/:id/activities | Retorna a listagem de atividades |
| POST | /trips/:id/invite | Registra um participante na viagem |
| GET | /trips/:id/participants | Retorna a listagem de participantes |
| POST | /trips/:id/links | Cria uma listagem de links em viagem |
| GET | /trips/:id/links | Retorna a listagem de links |
