CREATE TABLE users(
	uemail VARCHAR(100) NOT NULL PRIMARY KEY
);

CREATE TABLE users_connected (
	token VARCHAR(100) NOT NULL,
	uemail VARCHAR(100),
	is_removed BOOLEAN NOT NULL,
	PRIMARY KEY(uemail)
);

CREATE TABLE Messages(
	id BIGINT,
	text VARCHAR(256),
	is_removed BOOLEAN NOT NULL,
	PRIMARY KEY(id)
);

CREATE TABLE messages_users(
	message_id BIGINT,
	uto VARCHAR(100) NOT NULL,
	ufrom VARCHAR(100) NOT NULL,
	is_sended BOOLEAN NOT NULL,
	PRIMARY KEY(message_id),
	FOREIGN KEY (message_id) REFERENCES Messages(id),
	FOREIGN KEY (uto) REFERENCES users_connected(uemail),
	FOREIGN KEY (ufrom) REFERENCES users_connected(uemail)
);