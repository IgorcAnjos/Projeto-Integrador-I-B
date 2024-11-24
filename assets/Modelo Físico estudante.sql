--- modelo físico estudante
CREATE TABLE IF NOT EXISTS estudante(
  matricula    VARCHAR(64)  UNIQUE NOT NULL DEFAULT(UUID()),
  nome         VARCHAR(150) NOT NULL,
  email        VARCHAR(100) NOT NULL,
  senha        CHAR(64)     NOT NULL,
  data_criacao  DATETIME    NOT NULL DEFAULT(NOW()),
  PRIMARY KEY (matricula),
  UNIQUE INDEX(email),
  INDEX(nome)
);