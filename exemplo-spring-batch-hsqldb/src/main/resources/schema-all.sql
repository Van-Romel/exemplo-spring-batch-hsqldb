DROP TABLE pessoa_table IF EXISTS;

CREATE TABLE pessoa_table
(
    id        BIGINT IDENTITY NOT NULL PRIMARY KEY,
    nome      VARCHAR(20),
    sobrenome VARCHAR(20),
    telefone  VARCHAR(20)
);