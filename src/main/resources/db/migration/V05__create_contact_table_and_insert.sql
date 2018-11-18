CREATE TABLE contact (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	id_person BIGINT(20) NOT NULL,
	name VARCHAR(50) NOT NULL,
	email VARCHAR(100) NOT NULL,
	phone_number VARCHAR(20) NOT NULL,
    FOREIGN KEY (id_person) REFERENCES person(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into contact (id, id_person, name, email, phone_number) values (1, 1, 'Junior Contact', 'contact@gmail.com', '44 99998-6889');
