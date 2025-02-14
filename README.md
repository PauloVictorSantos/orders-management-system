# Sistema de Gerenciamento de Pedidos

## Visão Geral
Este projeto é um Sistema de Gerenciamento de Pedidos construído com Java e Spring Boot. Ele fornece APIs RESTful para gerenciar pedidos, incluindo a criação, listagem e cancelamento de pedidos. O sistema também integra com Kafka para processamento de mensagens.

## Tecnologias Utilizadas
- Java
- Spring Boot
- Maven
- Kafka
- JUnit 5
- Mockito

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
    mvn spring-boot:run
    ```

### Executando Testes
Para executar os testes unitários, use o seguinte comando:
```sh
mvn test