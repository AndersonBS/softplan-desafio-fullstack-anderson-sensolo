CREATE TABLE PARECER (
	CODPARECER INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	DESCRICAO VARCHAR(255) NOT NULL,
	DATA DATE NOT NULL,
	CODPROCESSO INT NOT NULL,
	CODUSUARIO INT NOT NULL,
	VERSAO INT,
	FOREIGN KEY (CODPROCESSO) REFERENCES PROCESSO(CODPROCESSO),
	FOREIGN KEY (CODUSUARIO) REFERENCES USUARIO(CODUSUARIO)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8