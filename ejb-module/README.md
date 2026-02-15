# EJB Module - Benefício EJB REST

Este módulo contém a implementação EJB do sistema de benefícios utilizando Jakarta EE 10.

## 🏗️ Tecnologias

- **Jakarta EE 10**
- **EJB 4.0**
- **JAX-RS (REST)**
- **JPA 3.1**
- **CDI 4.0**
- **WildFly 27.0.1.Final**

## 📋 Pré-requisitos

- Java 17+
- Maven 3.8+
- WildFly 27.0.1.Final (ou superior compatível com Jakarta EE 10)

## 🚀 Como Subir a Aplicação

### 1. Download do WildFly

Baixe o WildFly 27.0.1.Final em: https://www.wildfly.org/downloads/

Extraia em um diretório de sua preferência (ex: `/opt/wildfly` ou `C:\wildfly`)

### 2. Configurar Variáveis de Ambiente (opcional)

```bash
export WILDFLY_HOME=/caminho/para/wildfly
export PATH=$PATH:$WILDFLY_HOME/bin
```

### 3. Build do Projeto

```bash
cd ejb-module/beneficio-ejb-rest
mvn clean package
```

Isso irá gerar o arquivo `target/beneficio-ejb-rest.war`

### 4. Deploy no WildFly

#### Opção 1: Deploy Manual (Recomendado)

1. Inicie o WildFly:
```bash
$WILDFLY_HOME/bin/standalone.sh        # Linux/Mac
$WILDFLY_HOME\bin\standalone.bat      # Windows
```

2. Copie o WAR gerado para o diretório de deployments:
```bash
cp target/beneficio-ejb-rest.war $WILDFLY_HOME/standalone/deployments/
```

#### Opção 2: Deploy via Maven Plugin

Certifique-se de que o WildFly está rodando, então execute:

```bash
mvn wildfly:deploy
```

Credenciais configuradas no `pom.xml`:
- Host: localhost:9990
- Username: admin
- Password: admin

> **Nota:** Você precisa criar o usuário admin no WildFly antes:
> ```bash
> $WILDFLY_HOME/bin/add-user.sh -u admin -p admin
> ```

### 5. Acessar a Aplicação

- **Aplicação:** http://localhost:8080/beneficio/
- **Console de Administração:** http://localhost:9990/console

## 📡 Endpoints REST

Base URL: `http://localhost:8080/beneficio/api/beneficios`

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/` | Listar todos os benefícios |
| GET | `/{id}` | Buscar benefício por ID |
| POST | `/` | Criar novo benefício |
| PUT | `/{id}` | Atualizar benefício |
| DELETE | `/{id}` | Remover benefício |
| POST | `/transferir` | Transferir valor entre benefícios |

### Exemplos de Requisições

#### Criar Benefício
```bash
curl -X POST http://localhost:8080/beneficio/api/beneficios \
  -H "Content-Type: application/json" \
  -d '{"descricao": "Vale Alimentação", "valor": 500.00}'
```

#### Transferir Valor
```bash
curl -X POST "http://localhost:8080/beneficio/api/beneficios/transferir?origem=1&destino=2&valor=100.00"
```

## 🔧 Configuração do Banco de Dados

O projeto utiliza um DataSource configurado no WildFly. Verifique o arquivo:
`src/main/java/resources/META-INF/persistence.xml`

### Configurar PostgreSQL no WildFly (Produção)

1. Adicione o driver PostgreSQL ao WildFly
2. Configure o DataSource no arquivo `standalone.xml`:

```xml
<datasource jndi-name="java:jboss/datasources/BeneficioDS" pool-name="BeneficioDS">
    <connection-url>jdbc:postgresql://localhost:5432/beneficio</connection-url>
    <driver>postgresql</driver>
    <security>
        <user-name>usuario</user-name>
        <password>senha</password>
    </security>
</datasource>
```

3. Atualize o `persistence.xml` para usar o novo DataSource.

## 🐞 Correções Implementadas

O método `transferir` foi corrigido para:
- ✔️ Validar saldo antes da transferência
- ✔️ Usar locking otimista (`LockModeType.OPTIMISTIC`)
- ✔️ Transação atômica com rollback automático
- ✔️ Verificar IDs de origem e destino
- ✔️ Validar valor maior que zero

## 📁 Estrutura do Projeto

```
ejb-module/beneficio-ejb-rest/
├── src/
│   └── main/
│       ├── java/com/example/
│       │   ├── ejb/BeneficioEjbService.java    # Lógica de negócio
│       │   ├── entity/Beneficio.java           # Entidade JPA
│       │   ├── rest/BeneficioRest.java         # Endpoints REST
│       │   └── config/                         # Configurações
│       ├── resources/
│       │   └── META-INF/persistence.xml        # Configuração JPA
│       └── webapp/
│           └── WEB-INF/
│               ├── web.xml                     # Configuração web
│               └── jboss-web.xml               # Context root
└── pom.xml                                     # Dependências Maven
```

## 🧪 Testes

Para executar os testes unitários:

```bash
mvn test
```

## 📝 Notas Importantes

- Este módro é independente do backend Spring Boot
- Ambos implementam a mesma funcionalidade em tecnologias diferentes
- O EJB usa Jakarta EE 10 (namespace `jakarta.*`)
- Não há integração automática entre EJB e Spring Boot - eles são alternativas

## 🔗 Links Úteis

- [WildFly Documentation](https://docs.wildfly.org/)
- [Jakarta EE 10 Specification](https://jakarta.ee/specifications/)
