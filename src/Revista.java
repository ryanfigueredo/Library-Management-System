/**
 * Classe que representa uma Revista
 * 
 * Também herda de ItemDeAcervo, mas tem informações diferentes do livro,
 * como editora, volume e ISSN.
 * 
 * @author Ryan Figueredo
 */
public class Revista extends ItemDeAcervo {
    private String editora;
    private int volume;
    private String issn;

    // Construtor - uso super() pra chamar o construtor da classe pai
    public Revista(String codigo, String titulo, int anoPublicacao, String editora, int volume, String issn) {
        super(codigo, titulo, anoPublicacao);
        this.editora = editora;
        this.volume = volume;
        this.issn = issn;
    }

    // Getters específicos de revista
    public String getEditora() { 
        return editora; 
    }
    
    public int getVolume() { 
        return volume; 
    }
    
    public String getIssn() { 
        return issn; 
    }
    
    // toString pra exibir as informações da revista
    @Override
    public String toString() {
        String status = isEmprestado() ? "Emprestado" : "Disponível";
        return String.format("Revista - Código: %s | Título: %s | Editora: %s | Volume: %d | ISSN: %s | Ano: %d | Status: %s",
                getCodigo(), getTitulo(), editora, volume, issn, getAnoPublicacao(), status);
    }
}
