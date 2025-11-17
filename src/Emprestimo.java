import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Classe que representa um Empréstimo
 * 
 * Relaciona um usuário com um item do acervo. O item pode ser
 * Livro ou Revista (polimorfismo), e o usuário pode ser Aluno ou
 * Professor (também polimorfismo).
 * 
 * @author Ryan Figueredo
 */
public class Emprestimo {
    private String idEmprestimo;
    private Usuario usuario;
    private ItemDeAcervo item; // Pode ser Livro ou Revista
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private double multaCobrada;

    // Construtor - calcula a data de devolução usando o método do usuário
    // Isso é polimorfismo: se for Aluno retorna +7 dias, se for Professor +15 dias
    public Emprestimo(String idEmprestimo, Usuario usuario, ItemDeAcervo item, LocalDate dataEmprestimo) {
        this.idEmprestimo = idEmprestimo;
        this.usuario = usuario;
        this.item = item;
        this.dataEmprestimo = dataEmprestimo;
        // Polimorfismo: o método se comporta diferente dependendo do tipo de usuário
        this.dataDevolucaoPrevista = usuario.calculaPrazoDevolucao(dataEmprestimo);
        this.multaCobrada = 0.0;
    }

    // Calcula a multa se tiver atraso
    // R$ 1,00 por dia de atraso
    public double calcularMulta(LocalDate dataDevolucaoReal) {
        if (dataDevolucaoReal.isAfter(this.dataDevolucaoPrevista)) {
            long diasAtraso = ChronoUnit.DAYS.between(this.dataDevolucaoPrevista, dataDevolucaoReal);
            return diasAtraso * 1.0;
        }
        return 0.0;
    }

    // Finaliza o empréstimo e calcula a multa
    public void finalizarEmprestimo(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
        this.multaCobrada = calcularMulta(dataDevolucaoReal);
    }

    // Getters
    public String getIdEmprestimo() { 
        return idEmprestimo; 
    }
    
    public Usuario getUsuario() { 
        return usuario; 
    }
    
    public ItemDeAcervo getItem() { 
        return item; 
    }
    
    public LocalDate getDataEmprestimo() { 
        return dataEmprestimo; 
    }
    
    public LocalDate getDataDevolucaoPrevista() { 
        return dataDevolucaoPrevista; 
    }
    
    public LocalDate getDataDevolucaoReal() { 
        return dataDevolucaoReal; 
    }
    
    public double getMultaCobrada() { 
        return multaCobrada; 
    }
    
    // toString pra exibir as informações do empréstimo
    @Override
    public String toString() {
        String status = dataDevolucaoReal == null ? "Em aberto" : "Devolvido";
        return String.format("Empréstimo #%s | Usuário: %s | Item: %s | Empréstimo: %s | Previsto: %s | Real: %s | Multa: R$ %.2f | Status: %s",
                idEmprestimo, usuario.getNome(), item.getTitulo(),
                dataEmprestimo, dataDevolucaoPrevista,
                dataDevolucaoReal != null ? dataDevolucaoReal.toString() : "Não devolvido",
                multaCobrada, status);
    }
}
