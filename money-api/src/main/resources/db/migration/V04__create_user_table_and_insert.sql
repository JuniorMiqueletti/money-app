CREATE TABLE user (
	id_user BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	password VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permission (
	id_permission BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	description VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permission_user (
	id_user BIGINT(20) NOT NULL,
	id_permission BIGINT(20) NOT NULL,
	PRIMARY KEY (id_user, id_permission),
	FOREIGN KEY (id_user) REFERENCES user(id_user),
	FOREIGN KEY (id_permission) REFERENCES permission(id_permission)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO user (id_user, name, email, password) values (1, 'Administrator', 'admin@gmail.com', '$2a$10$WQZSJD1a1F6ulXanXDZv8OwMnCuaTB9qyr5bH8rNP/6dBMHfxxoWC');
INSERT INTO user (id_user, name, email, password) values (2, 'John Doe', 'johndoe@gmail.com', '$2a$10$9C09OgrNue79J8PItYoFwujKWaQf0rlfO313z.kPcZWUWxvaqZPo6');

INSERT INTO permission (id_permission, description) values (1, 'ROLE_CREATE_CATEGORY');
INSERT INTO permission (id_permission, description) values (2, 'ROLE_SEARCH_CATEGORY');

INSERT INTO permission (id_permission, description) values (3, 'ROLE_CREATE_PERSON');
INSERT INTO permission (id_permission, description) values (4, 'ROLE_REMOVER_PERSON');
INSERT INTO permission (id_permission, description) values (5, 'ROLE_SEARCH_PERSON');

INSERT INTO permission (id_permission, description) values (6, 'ROLE_CREATE_RELEASE');
INSERT INTO permission (id_permission, description) values (7, 'ROLE_REMOVER_RELEASE');
INSERT INTO permission (id_permission, description) values (8, 'ROLE_SEARCH_RELEASE');

-- admin
INSERT INTO permission_user (id_user, id_permission) values (1, 1);
INSERT INTO permission_user (id_user, id_permission) values (1, 2);
INSERT INTO permission_user (id_user, id_permission) values (1, 3);
INSERT INTO permission_user (id_user, id_permission) values (1, 4);
INSERT INTO permission_user (id_user, id_permission) values (1, 5);
INSERT INTO permission_user (id_user, id_permission) values (1, 6);
INSERT INTO permission_user (id_user, id_permission) values (1, 7);
INSERT INTO permission_user (id_user, id_permission) values (1, 8);

-- john
INSERT INTO permission_user (id_user, id_permission) values (2, 2);
INSERT INTO permission_user (id_user, id_permission) values (2, 5);
INSERT INTO permission_user (id_user, id_permission) values (2, 8);