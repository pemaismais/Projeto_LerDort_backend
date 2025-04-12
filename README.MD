
<div align="center">
  <img src="https://github.com/user-attachments/assets/996821ed-eed1-4338-a19e-4cff0b632282" alt="Imagem do Projeto" width="300">
</div>
<br>
O PROJETO LER & DORT tem como objetivo investigar as dores e desconfortos presentes entre professores do Ensino Médio, com foco em Lesões por Esforços Repetitivos (LER) e Distúrbios Osteomusculares Relacionados ao Trabalho (DORT). A solução proposta visa melhorar a saúde e o bem-estar dos professores por meio de recomendações personalizadas de exercícios para aliviar as dores.

## Tecnologias Utilizadas

- **Java Spring**:
  - Spring Boot
  - Spring Security
  - Spring Data JPA
  - Spring Validation

- **Banco de Dados**:
  - MySQL (banco de dados relacional)

- **Autenticação**:
  - Google OAuth API Client
  - JWT (JSON Web Token)

- **Testes**:
  - Testes automatizados com JUnit e Mockito

- **Documentação**:
  - Swagger para API documentation

- **Monitoramento e Logs** :
  - Logs configurados para acompanhamento de requisições e erros

## Estrutura do Projeto

O projeto segue a estrutura MVC (Model-View-Controller):
```
.
├── compose.yaml
├── Dockerfile
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.MD
└── src
    └── main
        └── java
            └── app
                └── pi_fisio
                    ├── auth
                    ├── config
                    ├── controller
                    ├── dto
                    ├── entity
                    ├── helper
                    ├── infra
                    ├── queryfilters
                    ├── repository
                    ├── service
                    └── specifications
```

## Funcionalidades Principais

- **Autenticação e Autorização**:
    - Login social utilizando a API do Google.
    - Geração de JWT (JSON Web Token) para autenticação stateless.
    - Rotas protegidas por roles (`ADMIN`).

- **Gestão de Usuários**:
    - Criação de conta via login social do Google.
    - Relato de articulações com dor e intensidade.

- **Gestão de Exercícios**:
    - CRUD de exercícios (apenas para usuários com role `ADMIN`).
    - Recomendação de exercícios específicos com base nas articulações e intensidade de dor.

- **Filtros e Consultas**:
    - Uso de Specifications para filtros dinâmicos em consultas.

- **Tratamento de Exceções**:
    - Exception handler global para tratamento de erros.

## Configuração do Ambiente
Antes de iniciar a aplicação, configure as variáveis de ambiente no arquivo .env:
```
GOOGLE_SECRET_ID="your_google_secret_id"
GOOGLE_CLIENT_ID="your_google_client_id"
DATABASE_USER="your_database_user"
DATABASE_PASSWORD="your_database_password"
JWT_SECRET="your_jwt_secret"
JWT_REFRESH_TOKEN_EXPIRATION=24
JWT_TOKEN_EXPIRATION=2
```  
## Instalação e Execução
### 1. Clonar o Repositório
```
git clone https://github.com/pemaismais/PI_fisio_backend
cd PI_fisio_backend
```
### 2. Construir e Rodar a Aplicação com Docker
```
docker-compose up --build
```
### 3. Documentação da API (Swagger)
A documentação da API pode ser acessada em:
```
http://localhost:8081/swagger-ui.html
```
## Contato
Contribuições são bem-vindas! Se você tiver sugestões de melhorias ou correções, fique à vontade para abrir uma issue ou enviar um pull request.

## [Front-end desenvolvido em Angular](https://github.com/pemaismais/Projeto_LerDort_frontend)
---
