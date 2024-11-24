--- Modelo Físico alugel
CREATE TABLE IF NOT EXISTS aluguel(
  id                INTEGER        NOT NULL AUTO_INCREMENT,
  livro_id          INTEGER        NOT NULL,
  estudante_matricula VARCHAR (64) NOT NULL,
  inicio_reserva    DATETIME       NOT NULL,
  dias              INT            NOT NULL,
  fim_estimado      DATETIME       NOT NULL,
  fim_reserva       DATETIME       NULL,
  valor_estimado    FLOAT          NOT NULL,
  valor_total       FLOAT          NULL,   
  PRIMARY KEY (id),
  FOREIGN KEY (livro_id) REFERENCES livro(id),
  FOREIGN KEY (estudante_matricula) REFERENCES estudante(matricula),
  INDEX(livro_id),
  INDEX(estudante_matricula)
);