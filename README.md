# Challenge Library

Sistema de gerenciamento de biblioteca desenvolvido com Spring Boot e Kotlin, que gerencia livros, usuários, empréstimos e devoluções.

## Stack Tecnológica

- **Linguagem**: Kotlin
- **Framework**: Spring Boot 3.3.3
- **Java**: 21
- **Build**: Maven
- **Banco de Dados**: MongoDB
- **Cache**: Redis
- **Testes**: JUnit 5, TestContainers, SpringMockK

## Pré-requisitos

- Java 21
- Docker e Docker Compose
- Maven (ou use o wrapper incluso `./mvnw`)

## Instalação e Execução

### 1. Clone o repositório

```bash
git clone <repository-url>
cd challenge-library
```

### 2. Inicie os serviços com Docker Compose

```bash
docker-compose up -d
```

Isso iniciará:
- **MongoDB** na porta 27017 (usuário: root, senha: root)
- **Redis** na porta 6379

### 3. Build do projeto

```bash
./mvnw clean install
```

### 4. Execute a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível na porta 8080.

### 5. Execute os testes

```bash
./mvnw test
```

## Estrutura do Projeto

```
src/main/kotlin/com/challenge/library/
├── controller/          # Endpoints REST
│   ├── dto/             # Data Transfer Objects
├── service/             # Camada de negócio
├── repository/          # Acesso a dados
├── model/               # Modelos de domínio
├── mapper/              # Mapeadores DTO ↔ Model
└── exception/           # Exceções customizadas
```

## Funcionalidades

### Gerenciamento de Livros
- Listar catálogo de livros (paginado)
- Buscar livros por título, autor, ISBN ou categoria
- Cadastrar, atualizar e remover livros
- Sistema de feedback com avaliações e notas

### Gerenciamento de Usuários
- Três tipos de perfil: ADMIN, LIBRARY (funcionário) e MEMBER
- Sistema de notificações
- Estatísticas de usuários

### Gerenciamento de Empréstimos
Ciclo de vida completo do empréstimo:

```
REQUESTED_LOAN → APPROVED → CHECKED_OUT → REQUESTED_RETURN → RETURNED
```

- Solicitar empréstimo de livro
- Aprovar solicitação
- Registrar retirada do livro
- Solicitar devolução
- Aprovar devolução

### Relatórios Estatísticos
- Total de empréstimos no último mês
- Análise de devoluções em atraso vs. no prazo
- Top 3 livros mais emprestados
- Livros mais bem avaliados
- Estatísticas de usuários por tipo

## Endpoints da API

### Livros (`/book`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/book/catalog` | Lista todos os livros (paginado) |
| GET | `/book?search=<query>` | Busca livros |
| POST | `/book` | Cadastra novo livro |
| PUT | `/book` | Atualiza livro |
| DELETE | `/book/{id}` | Remove livro |
| POST | `/book/feedback-book` | Adiciona avaliação |

### Empréstimos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/list` | Lista todos os empréstimos |
| GET | `/list/{idUser}` | Lista empréstimos do usuário |
| POST | `/register` | Solicita empréstimo |
| PUT | `/approve/{idLoan}` | Aprova solicitação |
| PUT | `/grab/{idLoan}` | Registra retirada |
| PUT | `/devolution/{idLoan}` | Solicita devolução |
| PUT | `/approve-return/{idLoan}` | Aprova devolução |

### Usuários

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/createAdmin` | Cria conta admin |
| POST | `/createLibrary` | Cria conta funcionário |
| POST | `/createMember` | Cria conta membro |
| GET | `/notification/{idUser}` | Lista notificações |
| POST | `/notification-delay/{idLoan}` | Cria notificação de atraso |

### Relatórios Estatísticos (`/statistical-report`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/statistical-report/loan` | Estatísticas de empréstimos |
| GET | `/statistical-report/user` | Estatísticas de usuários |
| GET | `/statistical-report/book` | Estatísticas de livros |

## Configuração

As configurações padrão estão em `src/main/resources/application.properties`:

```properties
# MongoDB
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=library
spring.data.mongodb.username=root
spring.data.mongodb.password=root

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

## Testes

O projeto utiliza TestContainers para testes de integração, garantindo isolamento e reprodutibilidade:

- **Testes de Integração**: `BookIntegrationTest`, `LoanIntegrationTest`
- **Testes Unitários**: Services, Controllers, Repositories e Models
- **Dados de Teste**: Classes em `src/test/kotlin/.../data/`