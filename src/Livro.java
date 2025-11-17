/**
 * Classe que representa um Livro
 * 
 * Herda de ItemDeAcervo porque livro é um tipo de item do acervo.
 * Tem informações específicas como autor, ISBN e edição.
 * 
 * @author Ryan Figueredo
 */
public class Livro extends ItemDeAcervo {
    private String autor;
    private String isbn;
    private int edicao;

    // Construtor - uso super() pra chamar o construtor da classe pai
    public Livro(String codigo, String titulo, int anoPublicacao, String autor, String isbn, int edicao) {
        super(codigo, titulo, anoPublicacao);
        this.autor = autor;
        this.isbn = isbn;
        this.edicao = edicao;
    }

    // Getters específicos de livro
    public String getAutor() { 
        return autor; 
    }
    
    public String getIsbn() { 
        return isbn; 
    }
    
    public int getEdicao() { 
        return edicao; 
    }
    
    // toString pra exibir as informações do livro
    @Override
    public String toString() {
        String status = isEmprestado() ? "Emprestado" : "Disponível";
        return String.format("Livro - Código: %s | Título: %s | Autor: %s | ISBN: %s | Edição: %d | Ano: %d | Status: %s",
                getCodigo(), getTitulo(), autor, isbn, edicao, getAnoPublicacao(), status);
    }
}
