# API de Pagamento

API RESTful para processamento e gerenciamento de pagamentos, desenvolvida com Spring Boot.

## Características

- Processamento de múltiplos métodos de pagamento (Cartão de Crédito, Cartão de Débito, PIX, Boleto)
- Documentação OpenAPI/Swagger
- Cache de dados com Caffeine
- Auditoria de transações
- Paginação de resultados
- Validação de dados
- Tratamento global de exceções
- Monitoramento com Spring Actuator

## Requisitos

- Java 17
- PostgreSQL
- Maven

## Configuração

1. Clone o repositório:
```bash
git clone https://github.com/MarcossVini/API-de-pagamento.git
cd api-pagamento
```

2. Configure o banco de dados PostgreSQL em `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pagamento_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

3. Execute o projeto:
```bash
mvn spring-boot:run
```

## Endpoints da API

### Pagamentos

- `POST /api/payments` - Criar novo pagamento
- `GET /api/payments/{id}` - Buscar pagamento por ID
- `GET /api/payments` - Listar todos os pagamentos (paginado)
- `GET /api/payments/transaction/{transactionId}` - Buscar pagamento por ID da transação
- `PATCH /api/payments/{id}/status` - Atualizar status do pagamento

## Documentação da API

A documentação completa da API está disponível através do Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## Métodos de Pagamento Suportados

- `CREDIT_CARD` - Cartão de Crédito
- `DEBIT_CARD` - Cartão de Débito
- `PIX` - Pagamento Instantâneo PIX
- `BOLETO` - Boleto Bancário

## Status de Pagamento

- `PENDING` - Pagamento pendente
- `PROCESSING` - Em processamento
- `COMPLETED` - Concluído
- `FAILED` - Falha no processamento
- `REFUNDED` - Reembolsado
- `CANCELLED` - Cancelado

## Monitoramento

Endpoints do Actuator disponíveis em:
```
http://localhost:8080/actuator
```

Métricas disponíveis:
- `/health` - Status da aplicação
- `/info` - Informações da aplicação
- `/metrics` - Métricas detalhadas
- `/prometheus` - Métricas formato Prometheus

## Cache

A aplicação utiliza cache Caffeine para otimizar o desempenho das consultas:
- Tempo de expiração: 5 minutos
- Tamanho máximo: 500 entradas

## Testes

Execute os testes unitários:
```bash
mvn test
```

## Tecnologias Utilizadas

- Spring Boot 3.1.5
- Spring Data JPA
- Spring Cache
- Spring Actuator
- PostgreSQL
- Lombok
- SpringDoc OpenAPI
- Caffeine Cache
- JUnit 5
- Mockito

## Segurança e Boas Práticas

- Validação de entrada com Bean Validation
- Tratamento global de exceções
- Auditoria de alterações (created_by, updated_by)
- Controle de versão otimista
- Logging estruturado com SLF4J
- Paginação para grandes conjuntos de dados

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas alterações (`git commit -m 'Adicionando nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request
