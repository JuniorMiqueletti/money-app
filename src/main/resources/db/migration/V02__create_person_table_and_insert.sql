CREATE TABLE person (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	public_place VARCHAR(30),
	number VARCHAR(30),
	complement VARCHAR(30),
	neighborhood VARCHAR(30),
	zip_code VARCHAR(30),
	city VARCHAR(30),
	state VARCHAR(30),
	active BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO person (name, public_place, number, complement, neighborhood, zip_code, city, state, active) values ('Joao Silva', 'Rua do Abacaxi', '10', null, 'Brasil', '38.400-12', 'Uberlandia', 'MG', true);
INSERT INTO person (name, public_place, number, complement, neighborhood, zip_code, city, state, active) values ('Maria Rita', 'Rua do Sabia', '110', 'Apto 101', 'Colina', '11.400-12', 'Ribeirao Preto', 'SP', true);
INSERT INTO person (name, public_place, number, complement, neighborhood, zip_code, city, state, active) values ('Pedro Santos', 'Rua da Bateria', '23', null, 'Morumbi', '54.212-12', 'Goiania', 'GO', true);
INSERT INTO person (name, public_place, number, complement, neighborhood, zip_code, city, state, active) values ('Ricardo Pereira', 'Rua do Motorista', '123', 'Apto 302', 'Aparecida', '38.400-12', 'Salvador', 'BA', true);
INSERT INTO person (name, public_place, number, complement, neighborhood, zip_code, city, state, active) values ('Josue Mariano', 'Av Rio Branco', '321', null, 'Jardins', '56.400-12', 'Natal', 'RN', true);
INSERT INTO person (name, public_place, number, complement, neighborhood, zip_code, city, state, active) values ('Pedro Barbosa', 'Av Brasil', '100', null, 'Tubalina', '77.400-12', 'Porto Alegre', 'RS', true);
INSERT INTO person (name, public_place, number, complement, neighborhood, zip_code, city, state, active) values ('Henrique Medeiros', 'Rua do Sapo', '1120', 'Apto 201', 'Centro', '12.400-12', 'Rio de Janeiro', 'RJ', true);
INSERT INTO person (name, public_place, number, complement, neighborhood, zip_code, city, state, active) values ('Carlos Santana', 'Rua da Manga', '433', null, 'Centro', '31.400-12', 'Belo Horizonte', 'MG', true);
INSERT INTO person (name, public_place, number, complement, neighborhood, zip_code, city, state, active) values ('Leonardo Oliveira', 'Rua do Musico', '566', null, 'Segismundo Pereira', '38.400-00', 'Uberlandia', 'MG', true);
INSERT INTO person (name, public_place, number, complement, neighborhood, zip_code, city, state, active) values ('Isabela Martins', 'Rua da Terra', '1233', 'Apto 10', 'Vigilato', '99.400-12', 'Manaus', 'AM', true);