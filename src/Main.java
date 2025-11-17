import java.time.LocalDate;

/**
 * Classe Main pra testar o sistema
 * 
 * Testa as funcionalidades e mostra como funciona o polimorfismo
 * e as regras de negócio.
 * 
 * @author Ryan Figueredo
 */
public class Main {
    public static void main(String[] args) {
        SistemaBiblioteca sistema = new SistemaBiblioteca();

        // Cria alguns usuários e itens pra testar
        Aluno aluno1 = new Aluno("A100", "Ryan Lopes", "Rua A", "2023001", "Eng. Comp.");
        Professor prof1 = new Professor("P200", "Jhonatan G.", "Rua B", "0554", "TI");
        
        Livro livro1 = new Livro("L001", "POO com Java", 2022, "Autor X", "12345", 3);
        Revista revista1 = new Revista("R001", "Java Magazine", 2023, "Editora Y", 50, "98765");

        sistema.adicionarUsuario(aluno1);
        sistema.adicionarUsuario(prof1);
        sistema.adicionarItem(livro1);
        sistema.adicionarItem(revista1);

        // Testa polimorfismo - mesmo método retorna valores diferentes
        System.out.println("=== Teste de Polimorfismo (Prazo de Devolução) ===");
        System.out.println("Prazo Aluno (7 dias): " + aluno1.calculaPrazoDevolucao(LocalDate.now()));
        System.out.println("Prazo Professor (15 dias): " + prof1.calculaPrazoDevolucao(LocalDate.now()));

        try {
            System.out.println("\n=== Teste 1: Empréstimo BEM-SUCEDIDO ===");
            Emprestimo emp1 = sistema.realizarEmprestimo(aluno1.getId(), livro1.getCodigo());
            System.out.println("Empréstimo #1 (Aluno) realizado. Previsto: " + emp1.getDataDevolucaoPrevista());
            System.out.println("Livro L001 está emprestado: " + livro1.isEmprestado());

            System.out.println("\n=== Teste 2: Empréstimo FALHA (RN1: Item Indisponível) ===");
            sistema.realizarEmprestimo(prof1.getId(), livro1.getCodigo());

        } catch (RegraDeNegocioException e) {
            System.err.println("ERRO: " + e.getMessage());
        }

        try {
            System.out.println("\n=== Teste 3: Devolução SEM MULTA ===");
            sistema.realizarDevolucao("1");
            Emprestimo emp1Devolvido = sistema.getHistoricoEmprestimos().get(0);
            System.out.println("Devolução concluída. Multa Cobrada: R$ " + emp1Devolvido.getMultaCobrada());
            System.out.println("Livro L001 está emprestado: " + livro1.isEmprestado());

            System.out.println("\n=== Teste 4: Empréstimo com Multa (RN3) ===");
            Emprestimo empAtraso = new Emprestimo("3", prof1, revista1, LocalDate.now().minusDays(20));
            empAtraso.getItem().emprestar();
            prof1.adicionarEmprestimo(empAtraso);

            System.out.println("Professor vai devolver item. Previsto: " + empAtraso.getDataDevolucaoPrevista());
            empAtraso.finalizarEmprestimo(LocalDate.now());
            System.out.println("Professor devolveu item. Multa Cobrada: R$ " + empAtraso.getMultaCobrada());

            Livro livro2 = new Livro("L002", "Design Patterns", 2020, "Autor Z", "67890", 1);
            sistema.adicionarItem(livro2);

            System.out.println("\n=== Teste 5: Empréstimo FALHA (RN4: Usuário com Multa) ===");
            sistema.realizarEmprestimo(prof1.getId(), livro2.getCodigo());

        } catch (RegraDeNegocioException e) {
            System.err.println("ERRO: " + e.getMessage());
        }

        System.out.println("\n=== Teste 6: Persistência ===");
        sistema.salvarDados();
        sistema.carregarDados();
    }
}

