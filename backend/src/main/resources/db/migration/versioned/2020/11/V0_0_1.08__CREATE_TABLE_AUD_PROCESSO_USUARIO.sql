CREATE TABLE AUD_PROCESSO_USUARIO (
	REV INT NOT NULL, 
  	REVTYPE INT,
	CODPROCESSO INT NOT NULL,
	CODUSUARIO INT NOT NULL,
	PRIMARY KEY (CODPROCESSO, CODUSUARIO, REV)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8