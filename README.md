# Sistema de Gestão de Biblioteca Universitária

## Descrição do Projeto

Este projeto implementa um sistema completo de gestão de biblioteca universitária usando Programação Orientada a Objetos (POO) em Java. O sistema permite gerenciar livros, usuários e empréstimos de forma organizada e eficiente.

## Estrutura do Projeto

O projeto é composto pelas seguintes classes:

### 1. **Livro.java**

- **Propósito**: Representa um livro do acervo da biblioteca
- **Conceitos de POO utilizados**:
  - **Encapsulamento**: Atributos privados com getters e setters
  - **Construtor**: Para inicializar objetos com valores específicos
  - **Método toString()**: Para facilitar a exibição dos dados

**Por que fiz assim?**

- Usei encapsulamento para proteger os dados e garantir que só sejam modificados através de métodos controlados
- O construtor facilita a criação de objetos com todos os dados necessários de uma vez
- O toString() permite exibir informações do livro de forma padronizada

### 2. **Usuario.java**

- **Propósito**: Representa um usuário da biblioteca (aluno, professor, funcionário)
- **Conceitos de POO utilizados**:
  - **Composição**: Usa ArrayList para armazenar livros emprestados
  - **Encapsulamento**: Atributos privados
  - **Métodos de controle**: Para gerenciar a lista de livros emprestados

**Por que fiz assim?**

- Usei ArrayList porque preciso de uma lista dinâmica que pode crescer conforme o usuário pega mais livros
- Os métodos adicionarLivroEmprestado() e removerLivroEmprestado() facilitam o controle dos empréstimos
- O método getQuantidadeLivrosEmprestados() ajuda a verificar limites de empréstimo

### 3. **Emprestimo.java**

- **Propósito**: Registra cada empréstimo realizado na biblioteca
- **Conceitos de POO utilizados**:
  - **Associação**: Relaciona um Livro com um Usuario
  - **LocalDate**: Para trabalhar com datas e calcular prazos
  - **Métodos de validação**: Para verificar se está atrasado

**Por que fiz assim?**

- Usei LocalDate porque preciso calcular datas de devolução e verificar atrasos
- O método estaAtrasado() permite identificar empréstimos que passaram do prazo
- O método getDiasAtraso() pode ser usado para calcular multas

### 4. **Biblioteca.java**

- **Propósito**: Classe principal que gerencia todas as operações do sistema
- **Conceitos de POO utilizados**:
  - **Agregação**: Gerencia listas de Livros, Usuarios e Emprestimos
  - **Métodos de negócio**: Coordena todas as operações (cadastro, empréstimo, devolução)
  - **Validações**: Verifica regras de negócio antes de executar operações

**Por que fiz assim?**

- Esta classe centraliza toda a lógica de negócio, facilitando manutenção
- Usei ArrayList para armazenar os dados porque preciso de listas dinâmicas
- Os métodos de validação garantem que as regras do negócio sejam respeitadas (ex: limite de 3 livros por usuário)
- Usei Stream API em alguns métodos para facilitar filtragens e buscas

### 5. **SistemaBiblioteca.java**

- **Propósito**: Classe principal com método main e interface de usuário
- **Conceitos utilizados**:
  - **Menu interativo**: Loop do-while para manter o sistema funcionando
  - **Scanner**: Para ler dados do usuário
  - **Switch-case**: Para organizar as opções do menu
  - **Métodos auxiliares**: Para evitar repetição de código

**Por que fiz assim?**

- Usei do-while para garantir que o menu apareça pelo menos uma vez
- Criei métodos auxiliares (lerString, lerInteiro) para evitar repetição de código
- O switch-case organiza melhor as opções do menu
- Tratamento de exceções no lerInteiro() evita que o programa quebre com entrada inválida

## Funcionalidades Implementadas

1. **Cadastro de Livros**: Adiciona novos livros ao acervo
2. **Cadastro de Usuários**: Registra novos usuários (alunos, professores, funcionários)
3. **Realizar Empréstimo**: Empresta livros para usuários (com validações)
4. **Realizar Devolução**: Registra a devolução de livros
5. **Listar Livros**: Mostra todos os livros cadastrados
6. **Listar Usuários**: Mostra todos os usuários cadastrados
7. **Buscar Livro**: Busca por ISBN ou título
8. **Listar Empréstimos Ativos**: Mostra empréstimos em aberto
9. **Listar Empréstimos Atrasados**: Identifica empréstimos fora do prazo
10. **Consultar Empréstimos de Usuário**: Mostra histórico de empréstimos de um usuário

## Regras de Negócio Implementadas

- **Limite de Empréstimos**: Cada usuário pode ter no máximo 3 livros emprestados simultaneamente
- **Prazo de Devolução**: 15 dias a partir da data do empréstimo
- **Validação de Disponibilidade**: Só é possível emprestar livros disponíveis
- **Validação de Existência**: Verifica se livro e usuário existem antes de operações

## Conceitos de POO Aplicados

1. **Encapsulamento**: Todos os atributos são privados, acessados apenas por métodos
2. **Abstração**: Cada classe representa um conceito do mundo real
3. **Composição**: Usuario contém uma lista de Livros
4. **Associação**: Emprestimo associa Livro e Usuario
5. **Agregação**: Biblioteca agrega listas de Livros, Usuarios e Emprestimos

## Como Compilar e Executar

1. Certifique-se de ter o Java JDK instalado (versão 8 ou superior)
2. Abra o terminal na pasta do projeto
3. Compile os arquivos:
   ```bash
   javac *.java
   ```
4. Execute o programa:
   ```bash
   java SistemaBiblioteca
   ```

## Explicações para Defesa

### Por que usei ArrayList?

- Preciso de listas dinâmicas que podem crescer conforme adiciono mais itens
- ArrayList é mais eficiente para operações de busca e iteração
- Facilita a implementação de funcionalidades como filtragem

### Por que usei LocalDate?

- Preciso trabalhar com datas para controlar prazos de devolução
- LocalDate oferece métodos úteis para cálculos de datas
- Facilita verificar se um empréstimo está atrasado

### Por que usei Stream API?

- Facilita filtragem e transformação de listas
- Código mais limpo e legível
- Melhor performance em operações com grandes volumes de dados

### Por que separei em várias classes?

- **Separação de responsabilidades**: Cada classe tem uma função específica
- **Reutilização**: Posso usar as classes em outros contextos
- **Manutenção**: Mais fácil de corrigir bugs e adicionar funcionalidades
- **Testes**: Mais fácil testar cada parte separadamente

### Por que usei métodos privados?

- **Encapsulamento**: Protege os dados internos da classe
- **Controle**: Garante que os dados só sejam modificados de forma segura
- **Manutenção**: Facilita mudanças futuras sem quebrar o código que usa a classe

## Melhorias Futuras Possíveis

1. Persistência de dados (salvar em arquivo ou banco de dados)
2. Interface gráfica (GUI) usando Java Swing ou JavaFX
3. Sistema de multas para empréstimos atrasados
4. Relatórios em PDF
5. Sistema de reservas de livros
6. Histórico completo de empréstimos

## Autor

Ryan Figueredo
Curso: Bacharel em Sistemas de Informação
