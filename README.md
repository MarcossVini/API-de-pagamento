# API de Pagamento

API RESTful para processamento e gerenciamento de pagamentos, desenvolvida com Spring Boot.
## Sumário
- Características
- Arquitetura
- Banco de Dados (modelo e esquema)
- Perfis de execução (PostgreSQL e H2 local)
- Como executar (VS Code e Maven Wrapper)
- Endpoints e exemplos
- Observabilidade (Actuator)
- Cache e Retry
- Testes
- Estrutura do projeto
- Tecnologias

## Características
- Múltiplos métodos de pagamento (CREDIT_CARD, DEBIT_CARD, PIX, BOLETO)
- Documentação OpenAPI/Swagger
- Cache com Caffeine
- Auditoria de entidades (datas e usuários)
- Paginação nas listagens
- Validação com Bean Validation
- Tratamento global de exceções
- Monitoramento com Spring Actuator

## Arquitetura
Arquitetura em camadas (clean/simple layered):
- Controller (REST): expõe endpoints HTTP e orquestra a chamada de serviços.
- Service (domínio): regras de negócio, cache e retry.
- Repository (dados): acesso ao banco via Spring Data JPA.
- Model/Entity: mapeamento JPA da entidade Payment.

Cross-cutting:
- Validação: anotações Jakarta Validation nos DTOs.
- Cache: @Cacheable com Caffeine em consultas por id e transactionId.
- Retry: @Retryable em operações de criação/atualização para lidar com locking otimista.
- Auditoria: @EnableJpaAuditing com campos createdAt/updatedAt/createdBy/updatedBy.
- OpenAPI: springdoc para documentação viva.
- Exceções: handler global padroniza respostas de erro.

## Banco de Dados (modelo e esquema)
Entidade principal: Payment (tabela payments)

Campos:
- id (PK, bigint, auto-increment)
- payment_method (varchar, not null)
- amount (numeric(10,2)+, not null)

- currency (char(3), not null)
- status (varchar, not null; enum PaymentStatus)
- transaction_id (varchar, unique)

- customer_id (varchar, not null)
- description (varchar(500), nullable)
- version (bigint) – controle de concorrência otimista (@Version)

- created_at (timestamp, not null)
- updated_at (timestamp, nullable)
- created_by (varchar, nullable)

- updated_by (varchar, nullable)

Restrições e comportamentos:
- Unique: transaction_id
- Default de status: PENDING (definido em @PrePersist)
- Auditoria ativada (@EnableJpaAuditing)

- ddl-auto=update (criação/alteração automática em dev)

Observação: Em produção, recomenda-se migrações versionadas (ex.: Flyway) em vez de ddl-auto.

## Perfis de execução
- default (PostgreSQL): definido em `application.properties`
	- jdbc: `jdbc:postgresql://localhost:5432/pagamento_db`
	- usuário/senha configuráveis
- local (H2 em memória): definido em `application-local.properties`
	- jdbc: `jdbc:h2:mem:pagamento_db`
	- Console H2: `/h2-console` (JDBC URL: `jdbc:h2:mem:pagamento_db`, user: `sa`, sem senha)

## Como executar
Pré-requisitos: Java 17. O repositório inclui Maven Wrapper, não é preciso instalar Maven.

VS Code (recomendado):
1) Abra o projeto e rode os comandos:
	- “Java: Update Project Configuration”
	- “Java: Clean Java Language Server Workspace” (Reload)
2) Run and Debug > "Spring Boot: ApiPagamentoApplication (local)" (usa perfil H2)

Terminal (PowerShell no Windows):
```powershell
# Perfil local (H2)
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# Perfil default (PostgreSQL)
./mvnw spring-boot:run
```

Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

## Endpoints e exemplos
Base path: `/api/payments`

- POST `/` – Criar pagamento
	- Request (JSON):
		{
			"paymentMethod": "PIX",
			"amount": 100.50,
			"currency": "BRL",
			"customerId": "CUST123",
			"description": "Pedido #123"
		}
		```
	- Response 201 (JSON): corpo do PaymentResponse com `id`, `transactionId`, `status` etc.

- GET `/{id}` – Buscar por ID
- GET `/transaction/{transactionId}` – Buscar por transactionId
- GET `/` – Listar paginado (parâmetros `page`, `size`, `sort`)
- PATCH `/{id}/status?status=COMPLETED` – Atualizar status

Status possíveis:
`PENDING`, `PROCESSING`, `COMPLETED`, `FAILED`, `REFUNDED`, `CANCELLED`

## Observabilidade (Actuator)
Base: `http://localhost:8080/actuator`
- `/health`, `/info`, `/metrics`, `/prometheus`

## Cache e Retry
- Cache: Caffeine (config em `application.properties`)
	- spec: `maximumSize=500,expireAfterWrite=300s`
	- Chaves: id e transactionId nas consultas
- Retry: `@Retryable(maxAttempts = 3, value = OptimisticLockingFailureException.class)`
	- Ativado por `@EnableRetry` na classe principal

## Testes
Executar testes:
```powershell
./mvnw test
```

## Estrutura do projeto
```
src/
	main/
		java/com/empresa/apipagamento/
			ApiPagamentoApplication.java           # Entry point (@EnableRetry)
			controller/PaymentController.java      # Endpoints REST
			service/PaymentService*.java           # Regras de negócio, cache, retry
			repository/PaymentRepository.java      # Spring Data JPA
			model/Payment*.java                    # Entidade e enum de status
			config/CacheConfig.java                # Caffeine
			config/JpaConfig.java                  # @EnableJpaAuditing
			config/OpenApiConfig.java              # Swagger/OpenAPI
			exception/GlobalExceptionHandler.java  # Erros padronizados
		resources/
			application.properties                 # Perfil default (PostgreSQL)
			application-local.properties           # Perfil local (H2)
	test/
		java/.../service/PaymentServiceImplTest.java

.mvn/wrapper/*, mvnw, mvnw.cmd               # Maven Wrapper
.vscode/launch.json                           # Execução com perfil local
```

## Tecnologias
- Java 17, Spring Boot 3.1.5
- Spring Web, Spring Data JPA, Validation
- PostgreSQL (prod/dev), H2 (local)
- Spring Cache (Caffeine), Spring Retry + AOP
- SpringDoc OpenAPI, Spring Actuator
- Lombok, JUnit 5, Mockito

## Notas e boas práticas
- Produção: preferir migrações (Flyway) em vez de `ddl-auto=update`.
- Validar credenciais e conexões do Postgres ao usar o perfil default.
- Status de auditoria usam auditor "system" (substituir por usuário autenticado no futuro).

## Contribuição
1. Fork
2. Branch (`feature/nova-feature`)
3. Commit/push
4. Pull Request

## Licença
MIT
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
