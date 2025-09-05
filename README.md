# Microsserviços: Pagamentos e Pedidos

Este projeto é um estudo prático de **arquitetura de microsserviços** utilizando **Spring Boot** e várias ferramentas do ecossistema Spring Cloud, com comunicação síncrona entre os microsserviços.

## Tecnologias utilizadas
- **Java 21** + **Spring Boot 3.5**
- **Spring Cloud Netflix Eureka** → Service Discovery
- **Spring Cloud Gateway** → API Gateway responsável por centralizar as requisições para os microsserviços, realizando roteamento dinâmico e balanceamento de carga.
- **Spring Cloud OpenFeign** → Comunicação síncrona entre microsserviços
- **Flyway** → Controle de versão do banco de dados, gerenciando migrações e criação automática das tabelas.
- **MySQL** → Banco de dados relacional
- **Maven** → Gerenciador de dependências

---

## Estrutura do projeto

O sistema é composto por quatro módulos principais:

- **eureka-server** → Responsável pelo registro e descoberta dos serviços.
- **gateway** → Centraliza as requisições e faz roteamento dinâmico com balanceamento de carga.
- **ms-pedidos** → Gerencia operações relacionadas a pedidos.
- **ms-pagamentos** → Gerencia operações relacionadas a pagamentos.

---

## Comunicação entre serviços
- O **Eureka Server** gerencia a descoberta dinâmica dos serviços.
- O **Gateway** faz roteamento para os microsserviços.
- O **Feign Client** é usado para comunicação entre **ms-pedidos** e **ms-pagamentos** sem precisar escrever código HTTP manual.

---

## Migrações com Flyway
Cada microsserviço tem suas migrações de banco gerenciadas pelo **Flyway**, garantindo consistência no esquema do banco de dados.

---

Todas as requisições passam pelo Gateway utilizando a URL base:
http://localhost:8082/<nomeDoProjeto>/<rota>
<img width="802" height="467" alt="Screenshot 2025-09-04 203640" src="https://github.com/user-attachments/assets/74625886-512a-4079-960e-ac008331e0cd" />

A confirmação de pagamento é realizada pelo serviço ms-pagamentos, mas passa pelo serviço de pedidos para validação e atualização do status do pedido:
POST http://localhost:8082/pagamentos-service/pagamentos/confirmar
<img width="1526" height="522" alt="confirmar-pag" src="https://github.com/user-attachments/assets/cdca7ac6-c45c-4b43-b946-e9ba13cd426f" />

A comunicação entre ms-pedidos e ms-pagamentos é feita utilizando OpenFeign, permitindo que o retorno de pagamentos seja integrado ao pedido correspondente:
<img width="1493" height="877" alt="pag-pedido" src="https://github.com/user-attachments/assets/bfefad70-92cd-43fb-9e54-5dc8256f9f79" />

Todos os serviços estão registrados no Eureka Server, permitindo que o Gateway e outros microsserviços descubram dinamicamente as instâncias disponíveis:
<img width="1354" height="615" alt="eureka" src="https://github.com/user-attachments/assets/aebf0b3f-e19a-4d50-bad5-379225f882fa" />

## Como Executar
- Clone o repositório
- Inicie o Eureka Server
- Inicie os microsserviços (pedidos, pagamentos)
- Inicie o Gateway
- Acesse as rotas pelo Gateway (localhost:8082)
