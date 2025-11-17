import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstrata que representa um Usuário da biblioteca
 * 
 * Criei como abstrata porque Aluno e Professor são tipos de usuário,
 * mas cada um tem limites e prazos diferentes. Então faz sentido
 * ter métodos abstratos que cada um implementa do seu jeito.
 * 
 * @author Ryan Figueredo
 */
public abstract class Usuario {
    private String id;
    private String nome;
    private String endereco;
    private String status;
    private List<Emprestimo> itensEmprestados;
    
    // Construtor básico
    public Usuario(String id, String nome, String endereco) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.status = "Ativo";
        this.itensEmprestados = new ArrayList<>(); // Lista vazia no começo
    }

    // Método abstrato - cada tipo de usuário tem limite diferente
    // Aluno tem 3, Professor tem 5
    public abstract int getLimiteEmprestimo();
    
    // Método abstrato - cada tipo calcula o prazo diferente
    // Aluno tem 7 dias, Professor tem 15 dias
    public abstract LocalDate calculaPrazoDevolucao(LocalDate dataEmprestimo);

    // Verifica se o usuário pode pegar mais livros
    // Checa limite, multa pendente, item atrasado e se está bloqueado
    public boolean isAptoParaEmprestimo() {
        // Verifica se já atingiu o limite
        if (itensEmprestados.size() >= getLimiteEmprestimo()) {
            return false;
        }

        // Verifica se tem multa ou item atrasado
        for (Emprestimo e : itensEmprestados) {
            if (e.getMultaCobrada() > 0) {
                return false; // Tem multa pendente
            }
            if (e.getDataDevolucaoReal() == null && 
                e.getDataDevolucaoPrevista().isBefore(LocalDate.now())) {
                return false; // Tem item atrasado
            }
        }

        // Verifica se está bloqueado
        if ("Bloqueado".equalsIgnoreCase(status)) {
            return false;
        }
        
        return true;
    }

    // Adiciona um empréstimo na lista do usuário
    public void adicionarEmprestimo(Emprestimo emprestimo) {
        this.itensEmprestados.add(emprestimo);
    }

    // Remove um empréstimo da lista do usuário
    public void removerEmprestimo(Emprestimo emprestimo) {
        this.itensEmprestados.remove(emprestimo);
    }

    // Getters e Setters
    public String getId() { 
        return id; 
    }
    
    public String getNome() { 
        return nome; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public String getEndereco() { 
        return endereco; 
    }
    
    public List<Emprestimo> getItensEmprestados() { 
        return itensEmprestados; 
    }
    
    // toString pra exibir as informações do usuário
    @Override
    public String toString() {
        return String.format("ID: %s | Nome: %s | Tipo: %s | Status: %s | Empréstimos: %d/%d",
                id, nome, this.getClass().getSimpleName(), status, 
                itensEmprestados.size(), getLimiteEmprestimo());
    }
}
