-- Inserindo autores
START TRANSACTION;

INSERT INTO categoria (nome) VALUES
('Fantasia'),
('Ficção Científica'),
('Suspense'),
('Terror'),
('Mistério'),
('Clássicos');

INSERT INTO autor (nome) VALUES
('J.K. Rowling'),
('George R.R. Martin'),
('J.R.R. Tolkien'),
('Agatha Christie'),
('Isaac Asimov'),
('Stephen King');

-- J.K. Rowling
INSERT INTO livro (autor_id, categoria_id, titulo, descricao, link_image, data_lancamento, quantidade_paginas, valor_dia_alugado, valor_dia_multa) VALUES
(1, 1, 'Harry Potter e a Pedra Filosofal', 'Primeiro livro da série Harry Potter.', 'https://link_imagem1.com', '1997-06-26', 223, 3.50, 1.00),
(1, 1, 'Harry Potter e a Câmara Secreta', 'Segundo livro da série Harry Potter.', 'https://link_imagem2.com', '1998-07-02', 251, 3.50, 1.00),
(1, 1, 'Harry Potter e o Prisioneiro de Azkaban', 'Terceiro livro da série Harry Potter.', 'https://link_imagem3.com', '1999-07-08', 317, 3.50, 1.00),
(1, 1, 'Harry Potter e o Cálice de Fogo', 'Quarto livro da série Harry Potter.', 'https://link_imagem4.com', '2000-07-08', 636, 3.50, 1.00),
(1, 1, 'Harry Potter e a Ordem da Fênix', 'Quinto livro da série Harry Potter.', 'https://link_imagem5.com', '2003-06-21', 766, 3.50, 1.00);

-- George R.R. Martin
INSERT INTO livro (autor_id, categoria_id, titulo, descricao, link_image, data_lancamento, quantidade_paginas, valor_dia_alugado, valor_dia_multa) VALUES
(2, 1, 'A Guerra dos Tronos', 'Primeiro livro das Crônicas de Gelo e Fogo.', 'https://link_imagem6.com', '1996-08-06', 694, 4.00, 1.50),
(2, 1, 'A Fúria dos Reis', 'Segundo livro das Crônicas de Gelo e Fogo.', 'https://link_imagem7.com', '1998-11-16', 768, 4.00, 1.50),
(2, 1, 'A Tormenta de Espadas', 'Terceiro livro das Crônicas de Gelo e Fogo.', 'https://link_imagem8.com', '2000-08-08', 973, 4.00, 1.50),
(2, 1, 'O Festim dos Corvos', 'Quarto livro das Crônicas de Gelo e Fogo.', 'https://link_imagem9.com', '2005-10-17', 753, 4.00, 1.50),
(2, 1, 'A Dança dos Dragões', 'Quinto livro das Crônicas de Gelo e Fogo.', 'https://link_imagem10.com', '2011-07-12', 1056, 4.00, 1.50);

-- J.R.R. Tolkien
INSERT INTO livro (autor_id, categoria_id, titulo, descricao, link_image, data_lancamento, quantidade_paginas, valor_dia_alugado, valor_dia_multa) VALUES
(3, 1, 'O Senhor dos Anéis: A Sociedade do Anel', 'Primeiro volume da trilogia O Senhor dos Anéis.', 'https://link_imagem11.com', '1954-07-29', 423, 5.00, 2.00),
(3, 1, 'O Senhor dos Anéis: As Duas Torres', 'Segundo volume da trilogia O Senhor dos Anéis.', 'https://link_imagem12.com', '1954-11-11', 352, 5.00, 2.00),
(3, 1, 'O Senhor dos Anéis: O Retorno do Rei', 'Terceiro volume da trilogia O Senhor dos Anéis.', 'https://link_imagem13.com', '1955-10-20', 416, 5.00, 2.00),
(3, 1, 'O Hobbit', 'Prequela da trilogia O Senhor dos Anéis.', 'https://link_imagem14.com', '1937-09-21', 310, 5.00, 2.00),
(3, 6, 'O Silmarillion', 'História do universo de O Senhor dos Anéis.', 'https://link_imagem15.com', '1977-09-15', 365, 5.00, 2.00);

-- Agatha Christie
INSERT INTO livro (autor_id, categoria_id, titulo, descricao, link_image, data_lancamento, quantidade_paginas, valor_dia_alugado, valor_dia_multa) VALUES
(4, 5, 'O Assassinato no Expresso do Oriente', 'Mistério clássico de Hercule Poirot.', 'https://link_imagem16.com', '1934-01-01', 256, 3.00, 1.00),
(4, 5, 'E Não Sobrou Nenhum', 'Um dos maiores clássicos de mistério.', 'https://link_imagem17.com', '1939-11-06', 272, 3.00, 1.00),
(4, 5, 'Morte no Nilo', 'Mistério passado no Egito.', 'https://link_imagem18.com', '1937-11-01', 288, 3.00, 1.00),
(4, 5, 'A Casa Torta', 'Outro clássico de mistério de Christie.', 'https://link_imagem19.com', '1949-03-19', 256, 3.00, 1.00),
(4, 5, 'Os Cinco Porquinhos', 'Investigação de um caso antigo.', 'https://link_imagem20.com', '1942-10-01', 288, 3.00, 1.00);

-- Isaac Asimov
INSERT INTO livro (autor_id, categoria_id, titulo, descricao, link_image, data_lancamento, quantidade_paginas, valor_dia_alugado, valor_dia_multa) VALUES
(5, 2, 'Fundação', 'Primeiro livro da série Fundação.', 'https://link_imagem21.com', '1951-05-01', 244, 4.50, 1.50),
(5, 2, 'Fundação e Império', 'Segundo livro da série Fundação.', 'https://link_imagem22.com', '1952-06-01', 247, 4.50, 1.50),
(5, 2, 'Segunda Fundação', 'Terceiro livro da série Fundação.', 'https://link_imagem23.com', '1953-06-01', 240, 4.50, 1.50),
(5, 2, 'Eu, Robô', 'Coletânea de contos de ficção científica.', 'https://link_imagem24.com', '1950-12-02', 253, 4.50, 1.50),
(5, 2, 'O Fim da Eternidade', 'Um dos clássicos de viagem no tempo.', 'https://link_imagem25.com', '1955-08-01', 191, 4.50, 1.50);

-- Stephen King
INSERT INTO livro (autor_id, categoria_id, titulo, descricao, link_image, data_lancamento, quantidade_paginas, valor_dia_alugado, valor_dia_multa) VALUES
(6, 4, 'It: A Coisa', 'Uma das maiores obras de King.', 'https://link_imagem26.com', '1986-09-15', 1138, 6.00, 2.50),
(6, 4, 'O Iluminado', 'Clássico de terror psicológico.', 'https://link_imagem27.com', '1977-01-28', 447, 6.00, 2.50),
(6, 4, 'Carrie, a Estranha', 'Primeiro romance de King.', 'https://link_imagem28.com', '1974-04-05', 199, 6.00, 2.50),
(6, 4, 'A Zona Morta', 'Mistura de ficção científica e terror.', 'https://link_imagem29.com', '1979-08-30', 426, 6.00, 2.50),
(6, 4, 'Doutor Sono', 'Sequência de O Iluminado.', 'https://link_imagem30.com', '2013-09-24', 531, 6.00, 2.50);
    
INSERT INTO estudante (nome, email, senha) VALUES
('Jão', 'jão@email.com', 'b7e94be513e96e8c45cd23d162275e5a12ebde9100a425c4ebcdd7fa4dcd897c');

SET @matricula = (SELECT matricula FROM estudante WHERE email = 'jão@email.com');

INSERT INTO aluguel
(livro_id, estudante_matricula, inicio_reserva, dias, fim_estimado, fim_reserva, valor_estimado, valor_total) VALUES
(1, @matricula, '2024-11-01 10:00:00', 7, '2024-11-08 10:00:00', '2024-11-08 09:00:00', 24.50, 24.50),
(2, @matricula, '2024-10-15 15:00:00', 5, '2024-10-20 15:00:00', '2024-10-22 12:00:00', 20.00, 28.00);

INSERT INTO aluguel
(livro_id, estudante_matricula, inicio_reserva, dias, fim_estimado, valor_estimado) VALUES
(3, @matricula, '2024-11-05 14:00:00', 10, '2024-11-15 14:00:00', 35.00);
COMMIT ;