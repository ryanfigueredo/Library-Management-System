import java.time.LocalDate;

/**
 * Classe que representa um Aluno
 * 
 * Herda de Usuario e implementa os métodos abstratos com valores
 * específicos pra aluno: limite de 3 itens e prazo de 7 dias.
 * 
 * @author Ryan Figueredo
 */
public class Aluno extends Usuario {
    private String matricula;
    private String curso;
    private final int limiteEmprestimo = 3; // Aluno pode pegar até 3 itens
    private final int prazoDevolucaoDias = 7; // Aluno tem 7 dias pra devolver

    // Construtor - uso super() pra chamar o construtor da classe pai
    public Aluno(String id, String nome, String endereco, String matricula, String curso) {
        super(id, nome, endereco);
        this.matricula = matricula;
        this.curso = curso;
    }

    // Getters específicos de aluno
    public String getMatricula() { 
        return matricula; 
    }
    
    public String getCurso() { 
        return curso; 
    }

    // Implementa o método abstrato - aluno tem limite de 3
    @Override
    public int getLimiteEmprestimo() {
        return limiteEmprestimo;
    }

    // Implementa o método abstrato - aluno tem 7 dias pra devolver
    @Override
    public LocalDate calculaPrazoDevolucao(LocalDate dataEmprestimo) {
        return dataEmprestimo.plusDays(prazoDevolucaoDias);
    }
}
