# Agenda de ContatosVLS em JavaFX

## 📖 Sobre

**Agenda de ContatosVLS** é uma aplicação de desktop simples e moderna para gerenciar seus contatos pessoais. Ela permite adicionar, editar, excluir e visualizar informações de contato como nome, telefone, e-mail, data de nascimento e endereço.

O objetivo principal deste projeto é demonstrar a construção de uma interface gráfica moderna com JavaFX, utilizando boas práticas como separação de responsabilidades (MVC adaptado), validação de formulário, feedback visual ao usuário (notificações) e abstração da camada de dados para permitir diferentes fontes de armazenamento.

## ✨ Funcionalidades

* Listagem de contatos em uma tabela clara.
* Adição, edição e exclusão de contatos através de um formulário intuitivo.
* Validação de dados no formulário (campos obrigatórios, formato de telefone e e-mail, telefone único).
* Formatação automática do campo de telefone no padrão brasileiro `(DD) XXXXX-XXXX`.
* Notificações visuais ("toasts") para feedback de sucesso ou erro.
* Armazenamento de dados local (padrão) usando **SQLite**, sem necessidade de instalação de banco de dados externo.
* **Opcional:** Configuração para conexão com um banco de dados **MySQL** externo, permitindo flexibilidade no armazenamento.
* Interface gráfica moderna inspirada em designs atuais, com tema escuro.

## 🚀 Tecnologias Utilizadas

Este projeto foi construído utilizando as seguintes tecnologias e frameworks:

* **Linguagem:** **Java 9+** - Utilizando recursos como o sistema de módulos (JPMS).
* **Framework de UI:** **JavaFX 21+** - A plataforma principal para a construção da interface gráfica de usuário (GUI). O layout é definido usando arquivos **FXML** e estilizado com **CSS**.
* **Gerenciador de Dependências e Build:** **Apache Maven** - Utilizado para gerenciar as bibliotecas externas e compilar o projeto em um arquivo JAR executável.
* **Banco de Dados:**
    * **SQLite (Padrão)**: Banco de dados embarcado que armazena os dados localmente em um único arquivo (`agenda_contatos.db`), tornando a aplicação portátil.
    * **MySQL (Opcional)**: Suporte para conexão com um servidor MySQL externo através da tela de configurações.
    * **JDBC (Java Database Connectivity)**: Usado para a comunicação com ambos os bancos de dados (SQLite e MySQL).
* **Bibliotecas Adicionais:**
    * **ControlsFX**: Utilizada para as notificações "toast" (`Notifications`).
    * **ValidatorFX**: Empregada para implementar a validação dos campos do formulário de forma declarativa e visual.
    * **Ikonli (FontAwesome 5 Pack)**: Biblioteca para adicionar ícones vetoriais (usando FontAwesome) aos botões e outros elementos da interface.
    * **SQLite JDBC Driver**: Driver necessário para a comunicação com o banco de dados SQLite.
    * **MySQL Connector/J**: Driver para a comunicação com o banco de dados MySQL.
    * **SLF4J Simple Logger**: Implementação simples de log usada internamente pelo driver SQLite para evitar avisos no console.
* **Configuração:** Utiliza um arquivo `config.properties` para gerenciar as configurações de armazenamento (SQLite ou MySQL) e as credenciais do MySQL.

## ⚙️ Como Rodar o Projeto na IDE

1.  **Pré-requisitos:**
    * JDK 9 ou superior instalado e configurado (variável `JAVA_HOME`).
    * Apache Maven instalado (opcional, se for compilar).
2.  **Executando o JAR:**
    * Baixe o arquivo `.jar` da última [Release](https://github.com/victorluansilva/vls-agenda-de-contatos-java) (substitua com o link real).
    * Execute o JAR através do terminal:
        ```bash
        java --module-path /caminho/para/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,org.controlsfx.controls,org.kordamp.ikonli.javafx,org.kordamp.ikonli.fontawesome5.pack,net.synedra.validatorfx -jar vls-agenda-de-contatos-1.0-SNAPSHOT.jar 
        ```
        * **Observação:** Você precisará ter o JavaFX SDK baixado e apontar o `--module-path` para a pasta `lib` dele. A execução de JARs modulares pode ser complexa. A forma mais fácil é executar pela IDE.
3.  **Executando pela IDE (Recomendado):**
    * Clone o repositório: `git clone https://github.com/victorluansilva/vls-agenda-de-contatos-java.git`
    * Abra o projeto em sua IDE Java (IntelliJ IDEA, Eclipse, etc.).
    * A IDE deve reconhecer o projeto Maven (`pom.xml`) e baixar as dependências.
    * Localize a classe `agenda_de_contatos.Launcher` e execute-a como uma aplicação Java. A configuração de execução (`Launcher.xml`) já inclui os parâmetros de VM necessários (`--enable-native-access`).
