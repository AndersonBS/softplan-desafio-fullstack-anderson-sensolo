INSERT INTO USUARIO (NOME, LOGIN, SENHA, EMAIL, PERMISSAO, DATAINCLUSAO, VERSAO)
VALUES ('Administrador', 'admin', '$2a$04$aHkXWAlSAAnWb0GpeSPbVew0UfReI2yBXFVaO9kLtFSC76aEgtYP6', 'administrador@softplan.com', 'ADMINISTRADOR', CURDATE(), 0),
	('Triador', 'triad', '$2a$04$pv9quK6mjSFqd455uU5IYO5OT8xiuO8N1RFAKU7YuyN5/tMK/cFdO', 'triador@softplan.com', 'TRIADOR', CURDATE(), 0),
	('Finalizador', 'final', '$2a$04$00ViLCus29KwQmwVEpkQb.tG/Ncod9vcqsaRN5NkvmQ9kO7VBkeBG', 'finalizador@softplan.com', 'FINALIZADOR', CURDATE(), 0);