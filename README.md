# Plataforma de Cursos Online

Sistema de gerenciamento de cursos desenvolvido em Java Swing para a disciplina de Banco de Dados do curso de Ciência da Computação da Unifal-MG.

## Sobre o Projeto

Plataforma de cursos online desenvolvida em Java utilizando arquitetura em camadas (MVC) com interface gráfica Swing e integração com MySQL via JDBC.

## Funcionalidades

### Para Alunos
- Login no sistema
- Visualização de cursos disponíveis
- Navegação por módulos e aulas
- Consulta de matrículas

### Para Administradores/Instrutores
- CRUD completo de Cursos
- CRUD completo de Módulos
- CRUD completo de Aulas

## Tecnologias Utilizadas

- Java 21 (OpenJDK)
- Swing (Interface gráfica)
- MySQL 8 (Banco de dados)
- JDBC (Conexão com banco)
- Maven (Gerenciamento de dependências)
- Arquitetura MVC


## Banco de Dados
- Nome do banco:
platform_courses

 Antes de rodar o sistema, você deve:
  
1- Criar o banco no MySQL:
CREATE DATABASE platform_courses;

2- Selecionar o banco:
USE platform_courses;

3-Executar o script SQL completo do projeto(ele está no arquivo: database.sql dentro do repositório).


## Configurar a Conexão com o Banco

Verifique o arquivo: ConnectionFactory.java

E ajuste seus dados locais:

private static final String URL = "jdbc:mysql://localhost:3306/platform_courses";

private static final String USER = "root";

private static final String PASSWORD = "SUA_SENHA_AQUI";

## Como Rodar o Projeto 
✔ Pré-requisitos

- Java 17 ou 21 instalado

- MySQL 8 instalado

- IntelliJ IDEA (recomendado)


## Passo a passo 

Clone o repositório

git clone https://github.com/anafreiria/PlataformaCursoSwing.git


Abra o projeto no IntelliJ após rodar o script no MySQL

Configure a ConnectionFactory com sua senha MySQL

Rode a aplicação

Execute Main.java

## Credenciais Para Teste
👨‍🎓 Aluno

Email: ana.silva@example.com
Senha: senha123

🛠️ Administrador

Email: joao.pereira@example.com
Senha: senha123

## Funcionalidades Implementadas
✔ Login

✔ Menu principal com opções diferenciadas por perfil

✔ CRUD completo de: Cursos, Módulos, Aulas.

✔ Consulta de dados para o aluno: Listar cursos, listar módulos, listar aulas, ver matrículas

## Arquitetura 
- Model: Contém classes que representam entidades do banco.
- DAO: Realiza operações SQL: inserir, edita, excluir e listar.
- View: Telas swing que o usuário interage.
- Main: Inicia o sistema mostrando a TelaLogin.
