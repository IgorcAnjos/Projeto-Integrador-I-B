--- modelo físico autor
CREATE TABLE IF NOT EXISTS autor(
  id   INTEGER NOT NULL AUTO_INCREMENT,
  nome VARCHAR(150) NOT NULL,
  PRIMARY KEY (id),
  INDEX(nome)
);