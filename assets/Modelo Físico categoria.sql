--- modelo físico categoria
CREATE TABLE IF NOT EXISTS categoria(
  id   INTEGER NOT NULL AUTO_INCREMENT,
  nome VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  INDEX(nome)
);