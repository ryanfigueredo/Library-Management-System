import java.time.LocalDate;

/**
 * Classe que representa um Professor
 * 
 * Herda de Usuario e implementa os métodos abstratos com valores
 * específicos pra professor: limite de 5 itens e prazo de 15 dias.
 * 
 * @author Ryan Figueredo
 */
public class Professor extends Usuario {
    private String siape;
    private String departamento;
    private final int limiteEmprestimo = 5; // Professor pode pegar até 5 itens
    private final int prazoDevolucaoDias = 15; // Professor tem 15 dias pra devolver

    // Construtor - uso super() pra chamar o construtor da classe pai
    public Professor(String id, String nome, String endereco, String siape, String departamento) {
        super(id, nome, endereco);
        this.siape = siape;
        this.departamento = departamento;
    }

    // Getters específicos de professor
    public String getSiape() { 
        return siape; 
    }
    
    public String getDepartamento() { 
        return departamento; 
    }

    // Implementa o método abstrato - professor tem limite de 5
    @Override
    public int getLimiteEmprestimo() {
        return limiteEmprestimo;
    }

    // Implementa o método abstrato - professor tem 15 dias pra devolver
    @Override
    public LocalDate calculaPrazoDevolucao(LocalDate dataEmprestimo) {
        return dataEmprestimo.plusDays(prazoDevolucaoDias);
    }
}
