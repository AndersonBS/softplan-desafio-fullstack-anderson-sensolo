CREATE TABLE AUD_PARECER (
	REV INT NOT NULL, 
  	REVTYPE INT, 
	CODPARECER INT NOT NULL,
	DESCRICAO VARCHAR(255),
	DATA DATE,
	CODPROCESSO INT,
	CODUSUARIO INT,
	VERSAO INT,
	PRIMARY KEY(CODPARECER,REV)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8