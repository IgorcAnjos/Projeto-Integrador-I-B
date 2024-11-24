BEGIN;
 CREATE DATABASE IF NOT EXISTS biblioteca_anjos;

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