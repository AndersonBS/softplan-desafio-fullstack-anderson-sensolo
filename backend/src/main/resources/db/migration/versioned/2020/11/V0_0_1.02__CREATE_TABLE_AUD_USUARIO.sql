CREATE TABLE AUD_USUARIO (
  	REV INT NOT NULL, 
  	REVTYPE INT, 
	CODUSUARIO INT NOT NULL,
	NOME VARCHAR(255),
	LOGIN VARCHAR(63),
	SENHA VARCHAR(63),
	EMAIL VARCHAR(63),
	PERMISSAO VARCHAR(31),
	DATAINCLUSAO DATE,
	VERSAO INT,
	PRIMARY KEY(CODUSUARIO,REV)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8