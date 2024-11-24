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