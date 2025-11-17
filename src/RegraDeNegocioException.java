/**
 * Exceção customizada pra regras de negócio
 * 
 * Criei essa exceção pra tratar erros específicos do sistema,
 * como item indisponível, limite excedido, etc.
 * 
 * @author Ryan Figueredo
 */
public class RegraDeNegocioException extends Exception {
    public RegraDeNegocioException(String message) {
        super(message);
    }
}
