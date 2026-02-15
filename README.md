# Sistema de Benefícios - Fullstack

Sistema completo de gerenciamento de benefícios com arquitetura em camadas (DB, EJB, Backend, Frontend).

## 🏗️ Arquitetura

```
┌─────────────────┐
│   Frontend      │  Angular 17
│   (Porta 4200)  │
└────────┬────────┘
         │ HTTP/REST
         ▼
┌─────────────────┐
│   Backend       │  Spring Boot 3
│   (Porta 8080)  │  - CRUD Completo
└────────┬────────┘  - Swagger/OpenAPI
         │
         ▼
┌─────────────────┐
│   Database      │  H2 (memória)
│                 │  - Schema automático
└─────────────────┘  - Dados iniciais
```

## 📋 Pré-requisitos

- Java 21+
- Maven 3.8+
- Node.js 18+
- npm 9+

## 🚀 Execução

### 1. Backend (Spring Boot)

```bash
cd backend-module
./mvnw spring-boot:run
```

O backend estará disponível em: http://localhost:8080

- API REST: http://localhost:8080/api/v1/beneficios
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console

### 2. Frontend (Angular)

```bash
cd frontend
npm install
npm start
```

O frontend estará disponível em: http://localhost:4200

## 📚 API Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/v1/beneficios` | Listar todos |
| GET | `/api/v1/beneficios/{id}` | Buscar por ID |
| POST | `/api/v1/beneficios` | Criar benefício |
| PUT | `/api/v1/beneficios/{id}` | Atualizar benefício |
| DELETE | `/api/v1/beneficios/{id}` | Deletar benefício |
| POST | `/api/v1/beneficios/transferir` | Transferir valor |

### Exemplo de Transferência

```json
POST /api/v1/beneficios/transferir
{
  "origemId": 1,
  "destinoId": 2,
  "valor": 100.00
}
```

## ✅ Funcionalidades

- ✔️ CRUD completo de benefícios
- ✔️ Transferência de valores entre benefícios
- ✔️ Validação de saldo insuficiente
- ✔️ Locking otimista (@Version)
- ✔️ Tratamento de concorrência
- ✔️ Swagger/OpenAPI documentação
- ✔️ Frontend Angular responsivo
- ✔️ Testes unitários

## 🧪 Testes

### Backend
```bash
cd backend-module
./mvnw test
```

### Frontend
```bash
cd frontend
npm test
```

## 📁 Estrutura

```
.
├── backend-module/          # Spring Boot API
│   ├── src/main/java/
│   │   ├── controllers/     # REST Controllers
│   │   ├── services/        # Business Logic
│   │   ├── repositories/    # JPA Repositories
│   │   ├── entities/        # JPA Entities
│   │   ├── dto/             # Data Transfer Objects
│   │   └── config/          # Configurations
│   └── src/test/            # Unit Tests
├── frontend/                # Angular App
│   └── src/app/
│       ├── components/      # UI Components
│       ├── services/        # HTTP Services
│       └── models/          # TypeScript Models
├── db/                      # Database Scripts
│   ├── schema.sql
│   └── seed.sql
└── ejb-module/             # EJB Module (WildFly)
    └── beneficio-ejb-rest/
```

## 🐞 Correção do Bug EJB

O método `transferir` foi corrigido com:
- ✔️ Validação de saldo antes da transferência
- ✔️ Locking otimista (@Version no entity)
- ✔️ Transação atômica (rollback automático)
- ✔️ Verificação de IDs de origem/destino

## 📄 Licença

Este é um projeto de desafio técnico.