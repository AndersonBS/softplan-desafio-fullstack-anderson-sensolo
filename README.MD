# Desafio implementador fullstack

### Características da implementação

**Backend**
- Projeto Spring Boot, Maven e Java 8
- Segurança com Spring Security e JSON Web Tokens
- Banco de dados MySQL
- Interface de Banco de dados com Java Persistence API (JPA) / Hibernate
- Migrações do Banco de dados com Flyway
- Auditoria do Banco de dados com Envers
- Modelos sem boilerplate com Lombok
- Validação dos requests com Java Bean Validation
- Simulação de requisições com Postman

**Frontend**
- Projeto React
- Formulários com Formik
- Cliente HTTP com Axios
- Aparência e responsividade com Bootstrap

### Suposições do desafio
- Registros podem ser editados e removidos
- Registros só podem ser removidos após seus vínculos serem manualmente removidos também
- Existem 3 permissões de usuário: Administrador, Triador e Finalizador
- Usuários Administradores possuem acesso a tudo
- Cada usuário pode possuir apenas uma das 3 permissões
- O login e o e-mail dos usuários devem ser únicos no sistema
- Existem 3 status de processo: Novo, Aguardando Parecer e Finalizado
- Somente usuários Administradores e Triadores podem ser responsáveis por processos
- Somente usuários Administradores e Finalizadores podem ser vinculados aos processos para incluirem pareceres
- É permitido somente um parecer por usuário no processo
- Os processos são finalizados quando todos os pareceres são incluídos
- Novos usuários podem ser vinculados aos processos mesmo após finalizados
- Usuários finalizadores só podem visualizar os processos ao qual devem prestar parecer
- Usuários finalizadores só podem visualizar os seus pareceres
- Usuários são automaticamente deslogados ao acessarem a página de login

Observação: Aprendi React essa semana para fazer o desafio, pois diariamente trabalho com JSP e Thymeleaf no frontend. Portanto, não tenho certeza se a estrutura do frontend ou a forma como foi implementado segue as boas práticas de desenvolvimento. Contudo, "Everything just works" :)

### Rodando a aplicação
Requisitos:
- Servidor MySql com usuário root e senha root rodando local na porta 3306 (ou alterar no application.properties do backend)
- Spring Tool Suite 4 (STS)
- Node Package Manager (NPM)

Instruções:
- Certifique-se de que o servidor MySQL está rodando e respondendo
- Importe o projeto backend no STS como uma aplicação maven e inicie pelo Boot Dashboard
- Inicie o projeto frontend através do comando npm start

Observação: Infelizmente não tive tempo de disponibilizar com docker.

### Melhorias a serem feitas
- Adicionar busca e paginação nas listas de registros
- Adicionar página para visualização de auditorias para o usuário Administrador
- Adicionar controle de logs

---

Seja parte da jornada de transformação digital no Brasil.

Esse é o nosso desafio para a vaga de implementador fullstack na [Softplan](https://www.softplan.com.br/carreira/). Serão testadas as habilidades e qualidade de código ao transformar requisitos limitados em uma aplicação web.

Seja parte da jornada de transformação digital no Brasil.
Procuramos desenvolvedor versátil para resolver os mais diversos problemas, que impactam no negócio do cliente, utilizando conhecimento em tecnologias back-end e front-end como meio para isso. Sinta-se confortável com ambas responsabilidades e seja engajado com o que há de melhor no mercado e boas práticas para trabalhar com inovação, tecnologias modernas e emergentes na Softplan, empresa no Top 10 dos melhores lugares para se trabalhar em Santa Catarina (GPTW 2017).


### O que você traz?
- Perfil proativo, saber trabalhar em equipe , raciocínio lógico, responsabilidade e comprometimento são imprescindíveis para essa oportunidade.
- Fácil adaptação em projetos experimentais e complexos;
- Aprendizado rápido no uso de tecnologias de desenvolvimento de software
- Experiência em Desenvolvimento de software Web.


### O que oferecemos
- Oportunidade de desenvolvimento profissional, meet ups, capacitação e treinamento in-company
- Apoio no seu crescimento e aprendizado com bolsas de graduação, pós-graduação e verba para cursos e eventos de aprendizado externos
- Plano de bonificação por atingimento de metas
- Biblioteca colaborativa
- Ginástica laboral
- No dress code
- Parcerias com desconto em academias, restaurantes, salão de beleza, etc.
- Espaço para refeições, com geladeira e microondas
- Vale alimentação/refeição, auxílio transporte, assistência médica e odontológica


### Instruções para o desafio

- Faça o **Fork** desse repositório e responda o desafio em um projeto com o seguinte nome: softplan-desafio-fullstack-`nome-sobrenome`;
- Assim que concluir o seu desafio, **publique o mesmo em seu github próprio** e mande o link do projeto para o recrutador, informando que finalizou.
- Atenção: Não se esqueça de deixar o projeto como público para que possamos baixar e dar uma olhada, ok? =)


### Desafio
- Desenvolver uma aplicação web responsável por gerenciar processos.
- *Soluções parcias serão aceitas.*
- Visão de administrador
	- Incluir, excluir, atualizar e visualizar usuários,
- Visão de usuário-triador
	- Incluir e visualizar processos,
	- Atribuir um ou mais usuários a realizar um parecer sobre um processo,
- Visão de usuário-finalizador
	- Visualizar processos pendentes de parecer.
	- Incluir o parecer sobre o processo.


### Escopo do desafio
- Documentar todas suposições realizadas.
- Desenvolver os módulos de frontend e backend de forma separada.
- O desenvolvimento do backend deve ser feito em Java.
- O desenvolvimento do frontend pode utilizar JavaScript e qualquer framework ou ferramenta que suportem ou utilizem estas tecnologias.
- Preferencialmente utilizar Spring Boot 1.5+ com toda sua stack para o desenvolvimento do backend.
- Preferencialmente utilizar React para o desenvolvimento do frontend.
- É aceitável utilizar algumas respostas estáticas em determinadas porções da aplicação.
- Não é necessário submeter uma aplicação que cumpra cada um dos requisitos descritos, mas o que for submetido deve funcionar.


### O que será avaliado
- O código será avaliado seguindo os seguintes critérios: manutenabilidade, clareza e limpeza de código; resultado funcional; entre outros fatores. 
- O histórico no `git` também está avaliado.
- Não esqueça de documentar o processo necessário para rodar a aplicação.
- Se necessário explique as decisões técnicas tomadas, as escolhas por bibliotecas e ferrramentas, o uso de patterns etc.


### Diferenciais
- Adaptar a página para dispositivos móveis (torná-la responsiva).
- Liberação da aplicação utilizando Docker.
- Utilização de boas práticas de UX na solução.
- Boa documentação de código e de serviços.
- Testes do código.

---
Em caso de dúvidas, envie um email para [g_cpa_squad_produto@softplan.com.br](mailto:g_cpa_squad_produto@softplan.com.br).


**Um dos nossos pilares é a valorização das pessoas e temos orgulho de dizer que somos uma empresa que apoia a diversidade e inclusão. Sendo assim, consideramos todos os candidatos para as nossas oportunidades, independente de raça, cor, religião, gênero, identidade de gênero, nacionalidade, deficiência, ascendência ou idade.**


**Até breve**

---

### Screenshots

![Login](/screenshots/Login.png?raw=true "Login")
![Home](/screenshots/Home.png?raw=true "Home")
![Usuários](/screenshots/Usuarios.png?raw=true "Usuários")
![Usuários Form](/screenshots/UsuariosForm.png?raw=true "Usuários Form")
![Processos](/screenshots/Processos.png?raw=true "Processos")
![ProcessosForm](/screenshots/ProcessosForm.png?raw=true "Processos Form")
![Atribuições](/screenshots/Atribuicoes.png?raw=true "Atribuições")
![Atribuições Form](/screenshots/AtribuicoesForm.png?raw=true "Atribuições Form")
![Pareceres](/screenshots/Pareceres.png?raw=true "Pareceres")
![Pareceres Form](/screenshots/PareceresForm.png?raw=true "Pareceres Form")