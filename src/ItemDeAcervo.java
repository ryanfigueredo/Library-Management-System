/**
 * Classe abstrata que representa um item do acervo
 * 
 * Criei essa classe porque tanto Livro quanto Revista têm coisas em comum,
 * então faz sentido ter uma classe base pra não repetir código.
 * 
 * @author Ryan Figueredo
 */
public abstract class ItemDeAcervo {
    private String codigo;
    private String titulo;
    private int anoPublicacao;
    private boolean isEmprestado;

    // Construtor básico
    public ItemDeAcervo(String codigo, String titulo, int anoPublicacao) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
        this.isEmprestado = false; // Todo item novo começa disponível
    }

    // Método pra marcar como emprestado
    public void emprestar() {
        this.isEmprestado = true;
    }

    // Método pra marcar como devolvido
    public void devolver() {
        this.isEmprestado = false;
    }

    // Getters básicos
    public String getCodigo() { 
        return codigo; 
    }
    
    public String getTitulo() { 
        return titulo; 
    }
    
    public int getAnoPublicacao() { 
        return anoPublicacao; 
    }
    
    public boolean isEmprestado() { 
        return isEmprestado; 
    }
}
