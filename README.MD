
<div align="center">
  <img src="https://github.com/user-attachments/assets/996821ed-eed1-4338-a19e-4cff0b632282" alt="Imagem do Projeto" width="300">
</div>
<br>

Este projeto é uma API REST desenvolvida em Java Spring, com o objetivo de auxiliar professores do Ensino Médio a lidar com dores e desconfortos relacionados a Lesões por Esforços Repetitivos (LER) e Distúrbios Osteomusculares Relacionados ao Trabalho (DORT). O sistema oferece exercícios específicos para fortalecimento muscular, alongamento e melhoria da mobilidade, baseados nas articulações e intensidade de dor relatadas pelo usuário.

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

## Estrutura do Projeto

O projeto segue a estrutura MVC (Model-View-Controller).

## Endpoints Principais

### Autenticação (`/api/auth/**`)
- **POST `/api/auth/login`**: Autenticação via Google e geração de JWT.
- **POST `/api/auth/refreshToken`**: Atualização do JWT.

### Exercícios (`/api/exercise/**`)
- **GET `/api/exercise`**: Lista de exercícios (filtros dinâmicos).
- **GET `/api/exercise/{id}`**: Detalhes de um exercício específico.
- **GET `/api/exercise/getByUser`**: Lista exercicios recomendados baseado no usuário.
- **POST `/api/exercise`**: Criação de um novo exercício (apenas `ADMIN`).
- **PUT `/api/exercise/{id}`**: Atualização de um exercício (apenas `ADMIN`).
- **DELETE `/api/exercise/{id}`**: Exclusão de um exercício (apenas `ADMIN`).

### Usuários (`/api/user/**`)
- **GET `/api/user`**: Lista de usuários .
- **GET `/api/user/info`**: Perfil do usuário autenticado.
- **PATCH `/api/user`**: Atualizar informações do usuário autenticado.

---
## [Front-end desenvolvido em Angular](https://github.com/pemaismais/Projeto_LerDort_frontend)

