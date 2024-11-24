<div align="center">

# Projeto Integrador PI - IB

## PUC GO

CRUD em Java para um sistema de biblioteca.
</div>

## Execução do projeto

Para execução do projeto basta clonar o repositório e abrir com a IDE Intellij Community e realizar a execução.

Irá executar a **API - REST** em um servidor local na porta `8080`

acesse: [localhost:8080]("localhost:8080")

## Bibliotecas

- ### Javalin

Javalin é um framework leve para criar APIs RESTful e servidores web. Foi usado para gerenciar rotas HTTP e endpoints no
projeto.

- ### Jackson Databind

Jackson Databind realiza serialização e desserialização de objetos Java em JSON e vice-versa. É usado para manipular
dados JSON nas requisições e respostas.

- ### Gson

Gson é outra biblioteca para converter objetos Java em JSON e JSON em objetos Java. Foi usada para facilitar o
processamento de JSON.

- ### SLF4J Simple

SLF4J Simple fornece uma implementação básica do SLF4J (Simple Logging Facade for Java). É usado para gerenciar logs no
projeto de forma simples e centralizada.

- ### MySQL Connector

Driver JDBC para conectar aplicativos Java a bancos de dados MySQL. Permite executar consultas e manipular dados no
banco de dados MySQL utilizado pelo projeto.

# Banco de dados

## Modelo Lógico

![PNG - Modelo Lógico.png](assets/PNG%20-%20Modelo%20L%C3%B3gico.png)

## Modelo físico

~~~text
BEGIN;
CREATE DATABASE IF NOT EXISTS biblioteca;

--- modelo físico estudante
CREATE TABLE IF NOT EXISTS estudante(
    matricula    INTEGER      AUTO_INCREMENT UNIQUE NOT NULL,
    nome         VARCHAR(150) NOT NULL,
    email        VARCHAR(100) NOT NULL,
    senha        CHAR(64)     NOT NULL,
    data_criacao  DATETIME    NOT NULL DEFAULT(NOW()),
    PRIMARY KEY (matricula),
    UNIQUE INDEX(email),
    INDEX(nome)
);

--- modelo físico autor
CREATE TABLE IF NOT EXISTS autor(
    id   INTEGER NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    PRIMARY KEY (id),
    INDEX(nome)
);

--- modelo físico categoria
CREATE TABLE IF NOT EXISTS categoria(
    id   INTEGER NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    PRIMARY KEY (id),
    INDEX(nome)
);

--- modelo físico livro
CREATE TABLE IF NOT EXISTS livro(
    id                 INTEGER                 NOT NULL AUTO_INCREMENT,
    autor_id           INTEGER                 NOT NULL,
    categoria_id       INTEGER                 NOT NULL,
    titulo             VARCHAR (100)           NOT NULL,
    descricao          VARCHAR (500)           NULL,
    link_image         VARCHAR (500)           NULL,
    data_lancamento    DATE                    NULL,
    data_criacao       DATETIME DEFAULT(NOW()) NOT NULL,
    quantidade_paginas INTEGER                 NULL,
    valor_dia_alugado  FLOAT                   NOT NULL,
    valor_dia_multa    FLOAT                   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (autor_id) REFERENCES autor(id),
    FOREIGN KEY (categoria_id) REFERENCES categoria(id),
    INDEX(titulo),
    INDEX(autor_id)
);

--- Modelo Físico alugel
CREATE TABLE IF NOT EXISTS aluguel (
    id INTEGER NOT NULL AUTO_INCREMENT,
    livro_id INTEGER NOT NULL,
    estudante_matricula INTEGER NOT NULL,
    inicio_reserva DATETIME NOT NULL,
    dias INT NOT NULL,
    fim_estimado DATETIME NOT NULL,
    fim_reserva DATETIME NULL,
    valor_estimado FLOAT NOT NULL,
    valor_total FLOAT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (livro_id)
    REFERENCES livro (id),
    FOREIGN KEY (estudante_matricula)
    REFERENCES estudante (matricula),
    INDEX (livro_id),
    INDEX (estudante_matricula)
);

COMMIT;
~~~

# Arquitetura MVC e Estruturas de dados utilizadas

~~~text
src/main/java
└── org/example
    ├── model
    │   └── Aluguel.java
    │   └── Autor.java
    │   └── Categoria.java
    │   └── Estudante.java
    │   └── Livro.java
    ├── controller
    │   └── AluguelController.java
    │   └── AutorController.java
    │   └── CategoriaController.java
    │   └── EstudanteController.java
    │   └── LivroController.java
    ├── service
    │   └── AluguelService.java
    │   └── AutorService.java
    │   └── CategoriaService.java
    │   └── EstudanteService.java
    │   └── LivroService.java
    ├── repository
    │   └── AluguelRepository.java
    │   └── AutorRepository.java
    │   └── CategoriaRepository.java
    │   └── EstudanteRepository.java
    │   └── LivroRepository.java
    ├── db
    │   └── DatabaseConnection.java
    │   └── Migration.java
    └── Main.java

~~~

- #### Class
- #### Record
- #### List
- #### Argument

# Postman

## Documetação de endpoints

Para acessar os endpoint com seus payload já configurados basta importar o arquivo JSON nas collections do seu **Postman
**

[PI I-B.postman_collection.json](PI%20I-B.postman_collection.json)

# Regras de negócio

> ###### Não há regras de negócio para endpoints de Leitura (SELECT)

## Categoria

- ### Create
    1. Verifica se já existe um registro de categoria com o mesmo nome
    2. Registra a categoria

- ### Update
    1. Verifica se existe registro de categoria
    2. Verifica se já existe um registro de categoria com o nome novo
    3. Atualiza a categoria

- ### Delete
    1. Verifica se existe um registro de categoria
    2. Verifica se há registro de livros na categoria
    3. Deleta a categoria

## Autor

- ### Create
    1. Verifica se já existe um registro de autor(a) com o mesmo nome
    2. Registra o autor(a)

- ### Update
    1. Verifica se o autor existe
    2. Verifica se já existe um registro de autor(a) com o nome novo
    3. Atualiza o autor(a)

- ### Delete
    1. Verifica se o autor existe
    2. Verifica se há registro de livros para o autor(a)
    3. Deleta o autor(a)

## Livro

- ### Create
    1. Verifica se o livro existe
    2. Verifica se a categoria existe
    3. Registra o livro

- ### Update
    1. Verifica se o livro existe
    2. Verifica se o autor existe
    3. Verifica se a categoria existe
    4. Atualiza o livro

- ### Delete
    1. Verifica se o livro existe
    2. Verifica se há registro de alugueis para o livro
    3. Deleta o livro

## Estudante

- ### Create
    1. Verifica se já existe um registro de estudante com o mesmo email
    2. Registra Estudante

- ### Update
    1. Verifica se o registro de estudante existe
    2. Verifica se já existe um registro de estudante com o email novo
    3. Atualiza o registro Estudante

- ### Delete
    1. Verifica se o registro de estudante existe
    2. Verifica se há registro de alugueis para o estudante
    3. Deleta o estudante

## Aluguel

- ### Create
    1. Verifica se o estudante existe.
    2. Verifica se o livro existe
    3. Verifica se as datas estão corretamente assinalada (inicio < fim)
    4. Registra o aluguel

- ### Update
    1. Verifica se o registro de aluguel existe
    2. Mantem os identificadores de Estudante e Livro sem aletrações
    3. Verifica se as datas estão corretamente assinalada (inicio < fim)
    4. Atualiza o registro do Aluguel

- ### Delete
    1. Verifica se o registro de aluguel existe
    2. Deleta o estudante