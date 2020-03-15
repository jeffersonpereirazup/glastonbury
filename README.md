# Glastonbury
Tech Leads Training

O Objetivo deste código é observar o comportamento de serviços integrando através de eventos,
utilizando o padrão Saga. 

# Premissas

- Padrão Saga
- Apenas uma compra por solicitação é processada 

# Serviços
- Order: Contém o serviço responsável por receber a solicitação de compra de ticket e propagar a solciitação para os demais serviços, o ponto de entrada da aplicação.
- Inventory: Contém os serviços responsáveis por gerenciar a quantidade de ingresso disponíveis e a transação de reserva.
- Paymento: Contém o serviço responsavel por processar o pagamento

## Rotas
No arquivo "glastonbury.postman_collection.json" você encontra algumas rotas disponíveis.

- Create Order
- Get Order
- Create Ticket
- Get Tickets

# Docker
## Kafka e zookeeper
No arquivo "zk-single-kafka-single.yml" você encontra o docker-compose responsável por subir 
a infraestrutura básica do kafka.