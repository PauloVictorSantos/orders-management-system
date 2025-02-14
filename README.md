![image](https://github.com/user-attachments/assets/ad0f38c9-2997-4f81-adc3-fd2a0f538ee2)


# Sistema de Gerenciamento de Pedidos

## Visão Geral
Este projeto é um Sistema de Gerenciamento de Pedidos 
construído com Java e Spring Boot. Ele fornece APIs RESTful 
para gerenciar pedidos, incluindo a criação, 
listagem e cancelamento de pedidos.
O sistema visa atender os seguintes requisitos com volumetria de pedidos, considerando que pode receber 150 mil a 200 mil pedidos por dia.
Lidar com a alta carga de pedidos, garantindo a disponibilidade e a confiabilidade do sistema.


## Tecnologias Utilizadas
- Spring Boot
- Maven
- Kafka
- JUnit 5
- Mockito
- Docker
- Docker Compose
- Swagger - : http://localhost:8080/swagger-ui.html
- PostGreSQL

## Estrutura do Projeto
- `src/main/java/com/moutti/orders/controller`: Contém os controladores REST.
- `src/main/java/com/moutti/orders/service`: Contém a camada de serviço.
- `src/main/java/com/moutti/orders/repository`: Contém as interfaces de repositório.
- `src/main/java/com/moutti/orders/model`: Contém os modelos de dados.
- `src/main/java/com/moutti/orders/kafka`: Contém o produtor e consumidor Kafka.
- `src/main/java/com/moutti/orders/enums`: Contém os enums usados no projeto.
- `src/test/java/com/moutti/orders`: Contém os testes unitários.

## Começando

### Pré-requisitos
- Java 11 ou superior
- Maven
- Kafka
- Docker
- Docker Compose

### Instalação
1. Clone o repositório:
    ```sh
    git clone https://github.com/PauloVictorSantos/orders-management-system.git
    cd orders-management-system
    ```

2. Construa o projeto:
    ```sh
    mvn clean install
    ```

3. Execute a aplicação:
    ```sh
    docker-compose up --build -d
    ```
### Adicionar novos itens via swagger

1. Acesse o Swagger: http://localhost:8080/swagger-ui.html
2. http://localhost:8080/swagger-ui/index.html#/pedido-controller/criarPedido
3. Clique em "Try it out"
4. Insira o JSON do pedido no campo "body" o seguinte JSON:
    ```json
    {
    "status": "RECEBIDO",
    "itens": [
      {     
        "produto": {
          "descr": "arroz",
          "prec": 12.00
        },
        "qtd": 1,
        "vtotalItem":  12.00
      }
    ],
    "vtotalPed":  12.00
   }
    ```
Considere alterar os dados do pedido para evitar duplicação.
5. Clique em "Execute"
6. O pedido será criado e você verá a resposta no campo "Response body"

### Verificacao de duplicacao de pedido
```java 
 public void verificarDuplicidade(Pedido pedido) {
   List<Pedido> pedidosExistentes = pedidoRepository.findAll();
   List<String> itensAtuais = formatarListPedido(pedido);

   // Itera sobre os pedidos existentes para verificar duplicidade
   for (Pedido pedidoExistente : pedidosExistentes) {
      // Valida se o número de itens é igual
      if (pedidoExistente.getItens().size() != pedido.getItens().size()) {
         continue;
      }

      // Formata a lista de itens do pedido existente
      List<String> itensExistentes = formatarListPedido(pedidoExistente);

      // Verifica se todos os itens são iguais
      verificarPedidoDuplicados(itensAtuais, itensExistentes);
   }
}

private static void verificarPedidoDuplicados(List<String> itensAtuais, List<String> itensExistentes) {
   if (itensAtuais.containsAll(itensExistentes) && itensExistentes.containsAll(itensAtuais)) {
      logger.error("Pedido duplicado detectado com os seguintes itens: {}", itensAtuais);
      throw new RuntimeException("Pedido duplicado detectado. Estrutura: " + itensAtuais);
   }
}
```
Este método verifica se o pedido é duplicado comparando-o com os pedidos existentes. Se um pedido duplicado for encontrado, uma exceção é lançada.

### Consistencia e concorrencia  

```java
@Transactional // Garantia de consistência nos commits e rollbacks
public class PedidoService {...}
```

Controle transacional é usado para garantir a consistência dos dados. Se ocorrer um erro durante a execução do método, o Spring irá fazer rollback da transação.

```java
@Override
@Lock(value = LockModeType.PESSIMISTIC_WRITE)
Optional<Pedido> findById(Long aLong);
```

O bloqueio pessimista é usado para garantir a concorrência.
Se um pedido estiver sendo processado, ele será bloqueado até que o processamento seja concluído.

Também está definido que o consumidor deve ler desde o começo da partição, no entanto, por conta da verificação de duplicação, o sistema não processará pedidos duplicados.
```java
props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Busca mensagens desde o início caso não haja
```

### Volumetria de pedidos

Configuração adequada de partições e replicação! Porque ?   
- Partições: Mais partições permitem que o tópico seja dividido, o que permite que as mensagens sejam processadas em paralelo. Isso aumenta a capacidade de processamento e a escalabilidade do sistema.
- Replicação: A replicação permite que as mensagens sejam replicadas em vários nós, o que aumenta a disponibilidade e a confiabilidade do sistema.
- Implicações: Mais partições e replicação aumentam a complexidade do sistema e o consumo de recursos. É importante encontrar um equilíbrio entre a escalabilidade e a complexidade do sistema.
```java
docker exec kafka  kafka-topics --create --topic pedido-topic --partitions 3 --replication-factor 1 --bootstrap-server localhost:9092
```

```java
@KafkaListener(topics = "pedido-topic", groupId = "pedido-group", concurrency = "5")
// 5 threads paralela para consumir mensagens do tópico pedido-topic
public void consumerPedido(Pedido pedido) { }
```

O consumidor do Kafka é configurado para processar mensagens em paralelo.
Isso aumenta a capacidade de processamento e a escalabilidade do sistema.

### Banco de dados
PostGreSQL é usado para armazenar os pedidos.
Recursos do PostGreSQL para ganho de perfomance e escalabilidade:
- Índices: Índices são usados para acelerar a recuperação de dados. Eles são criados em colunas comumente usadas em consultas.
Exemplo:
```java
    CREATE INDEX idx_status ON pedido (status);
```
- Particionamento: O particionamento é usado para dividir tabelas grandes em várias partições. Isso aumenta a eficiência das consultas e reduz o tempo de resposta.
Exemplo:
```java
    CREATE TABLE pedido (
    ...
    ) PARTITION BY RANGE (data);
```



