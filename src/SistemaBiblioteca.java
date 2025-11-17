import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Classe principal que gerencia o sistema
 * 
 * Essa classe coordena tudo: cadastro, empréstimos, devoluções, etc.
 * Trabalha com as hierarquias de Usuario e ItemDeAcervo usando polimorfismo.
 * 
 * @author Ryan Figueredo
 */
public class SistemaBiblioteca {
    private List<Usuario> listaUsuarios;
    private List<ItemDeAcervo> acervo;
    private List<Emprestimo> historicoEmprestimos;

    private final String USUARIOS_FILE = "usuarios.csv";
    private final String ACERVO_FILE = "acervo.csv";

    private long proximoIdEmprestimo = 1;
    private Scanner scanner;

    // Construtor - inicializa tudo vazio
    public SistemaBiblioteca() {
        this.listaUsuarios = new ArrayList<>();
        this.acervo = new ArrayList<>();
        this.historicoEmprestimos = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // Busca um usuário pelo ID usando Stream
    private Optional<Usuario> buscarUsuario(String id) {
        return listaUsuarios.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }
    
    // Busca um item pelo código - pode ser Livro ou Revista
    private Optional<ItemDeAcervo> buscarItem(String cod) {
        return acervo.stream()
                .filter(i -> i.getCodigo().equals(cod))
                .findFirst();
    }
    
    // Busca um empréstimo pelo ID
    private Optional<Emprestimo> buscarEmprestimo(String id) {
        return historicoEmprestimos.stream()
                .filter(e -> e.getIdEmprestimo().equals(id))
                .findFirst();
    }

    // Adiciona um usuário - pode ser Aluno ou Professor
    public void adicionarUsuario(Usuario u) { 
        this.listaUsuarios.add(u); 
    }
    
    // Adiciona um item ao acervo - pode ser Livro ou Revista
    public void adicionarItem(ItemDeAcervo item) { 
        this.acervo.add(item); 
    }
    
    public List<Emprestimo> getHistoricoEmprestimos() { 
        return historicoEmprestimos; 
    }

    // Realiza um empréstimo - valida todas as regras de negócio
    // RN1: Item disponível | RN2: Limite de empréstimos | RN3: Sem multa | RN4: Sem item atrasado
    public Emprestimo realizarEmprestimo(String idUsuario, String codItem) throws RegraDeNegocioException {
        // Busca usuário e item
        Usuario usuario = buscarUsuario(idUsuario)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado."));

        ItemDeAcervo item = buscarItem(codItem)
                .orElseThrow(() -> new RegraDeNegocioException("Item de Acervo não encontrado."));

        // RN1: Item deve estar disponível
        if (item.isEmprestado()) {
            throw new RegraDeNegocioException("RN1: O item '" + item.getTitulo() + "' está indisponível para empréstimo.");
        }

        // RN2 e RN4: Verifica se pode emprestar
        // Polimorfismo: getLimiteEmprestimo() retorna 3 pra Aluno e 5 pra Professor
        if (!usuario.isAptoParaEmprestimo()) {
            String motivo = usuario.getItensEmprestados().size() >= usuario.getLimiteEmprestimo()
                    ? "RN2: Limite máximo de empréstimos excedido (" + usuario.getLimiteEmprestimo() + ")."
                    : "RN4: Usuário bloqueado (multa pendente ou item com prazo vencido).";
            throw new RegraDeNegocioException(motivo);
        }

        // Cria o empréstimo
        String novoId = String.valueOf(proximoIdEmprestimo++);
        Emprestimo novoEmprestimo = new Emprestimo(novoId, usuario, item, LocalDate.now());

        // Atualiza tudo
        item.emprestar();
        usuario.adicionarEmprestimo(novoEmprestimo);
        this.historicoEmprestimos.add(novoEmprestimo);

        return novoEmprestimo;
    }

    // Realiza uma devolução e calcula multa se tiver atraso
    public void realizarDevolucao(String idEmprestimo) throws RegraDeNegocioException {
        Emprestimo emprestimo = buscarEmprestimo(idEmprestimo)
                .orElseThrow(() -> new RegraDeNegocioException("Empréstimo não encontrado."));

        if (emprestimo.getDataDevolucaoReal() != null) {
            throw new RegraDeNegocioException("Empréstimo já foi devolvido.");
        }

        LocalDate dataDevolucaoReal = LocalDate.now();
        emprestimo.finalizarEmprestimo(dataDevolucaoReal);
        emprestimo.getItem().devolver();
        emprestimo.getUsuario().removerEmprestimo(emprestimo);
    }

    // Salva os dados em arquivos CSV
    // Uso instanceof pra saber se é Aluno/Professor ou Livro/Revista
    public void salvarDados() {
        try {
            // Salva usuários
            List<String> userLines = listaUsuarios.stream().map(u -> {
                String base = u.getClass().getSimpleName() + ";" + u.getId() + ";" + 
                             u.getNome() + ";" + u.getEndereco() + ";" + u.getStatus();

                // Verifica o tipo real do objeto
                if (u instanceof Aluno) {
                    Aluno a = (Aluno) u;
                    return base + ";" + a.getMatricula() + ";" + a.getCurso();
                } else if (u instanceof Professor) {
                    Professor p = (Professor) u;
                    return base + ";" + p.getSiape() + ";" + p.getDepartamento();
                }
                return "";
            }).collect(Collectors.toList());
            Files.write(Paths.get(USUARIOS_FILE), userLines);

            // Salva itens do acervo
            List<String> itemLines = acervo.stream().map(i -> {
                String base = i.getClass().getSimpleName() + ";" + i.getCodigo() + ";" + 
                             i.getTitulo() + ";" + i.getAnoPublicacao() + ";" + i.isEmprestado();

                // Verifica o tipo real do objeto
                if (i instanceof Livro) {
                    Livro l = (Livro) i;
                    return base + ";" + l.getAutor() + ";" + l.getIsbn() + ";" + l.getEdicao();
                } else if (i instanceof Revista) {
                    Revista r = (Revista) i;
                    return base + ";" + r.getEditora() + ";" + r.getVolume() + ";" + r.getIssn();
                }
                return "";
            }).collect(Collectors.toList());
            Files.write(Paths.get(ACERVO_FILE), itemLines);

            System.out.println("Dados salvos com sucesso em " + USUARIOS_FILE + " e " + ACERVO_FILE);

        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    // Carrega os dados dos arquivos CSV e recria os objetos
    public void carregarDados() {
        System.out.println("Carregando dados...");
        
        // Carrega usuários
        try (BufferedReader br = new BufferedReader(new FileReader(USUARIOS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String tipo = parts[0];
                
                if ("Aluno".equals(tipo)) {
                    Aluno a = new Aluno(parts[1], parts[2], parts[3], parts[5], parts[6]);
                    a.setStatus(parts[4]);
                    this.listaUsuarios.add(a);
                } else if ("Professor".equals(tipo)) {
                    Professor p = new Professor(parts[1], parts[2], parts[3], parts[5], parts[6]);
                    p.setStatus(parts[4]);
                    this.listaUsuarios.add(p);
                }
            }
            System.out.println("Usuários carregados: " + this.listaUsuarios.size());

        } catch (IOException e) {
            System.out.println("Nenhum arquivo de usuários encontrado para carregar.");
        }

        // Carrega itens do acervo
        try (BufferedReader br = new BufferedReader(new FileReader(ACERVO_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String tipo = parts[0];
                boolean isEmprestado = Boolean.parseBoolean(parts[4]);

                ItemDeAcervo item = null;
                if ("Livro".equals(tipo)) {
                    item = new Livro(parts[1], parts[2], Integer.parseInt(parts[3]), 
                                   parts[5], parts[6], Integer.parseInt(parts[7]));
                } else if ("Revista".equals(tipo)) {
                    item = new Revista(parts[1], parts[2], Integer.parseInt(parts[3]), 
                                     parts[5], Integer.parseInt(parts[6]), parts[7]);
                }

                if (item != null) {
                    if (isEmprestado) {
                        item.emprestar();
                    } else {
                        item.devolver();
                    }
                    this.acervo.add(item);
                }
            }
            System.out.println("Itens de acervo carregados: " + this.acervo.size());
        } catch (IOException | NumberFormatException | DateTimeParseException e) {
            System.out.println("Nenhum arquivo de acervo encontrado ou erro no formato para carregar.");
        }
    }

    // Menu interativo do sistema
    public void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n========================================");
            System.out.println("  SISTEMA DE GESTÃO DE BIBLIOTECA");
            System.out.println("========================================");
            System.out.println("1.  Cadastrar Aluno");
            System.out.println("2.  Cadastrar Professor");
            System.out.println("3.  Cadastrar Livro");
            System.out.println("4.  Cadastrar Revista");
            System.out.println("5.  Realizar Empréstimo");
            System.out.println("6.  Realizar Devolução");
            System.out.println("7.  Listar Usuários");
            System.out.println("8.  Listar Acervo");
            System.out.println("9.  Listar Empréstimos Ativos");
            System.out.println("10. Listar Empréstimos Atrasados");
            System.out.println("11. Salvar Dados");
            System.out.println("12. Carregar Dados");
            System.out.println("0.  Sair");
            System.out.println("========================================");
            
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpa buffer
            
            try {
                switch (opcao) {
                    case 1: cadastrarAluno(); break;
                    case 2: cadastrarProfessor(); break;
                    case 3: cadastrarLivro(); break;
                    case 4: cadastrarRevista(); break;
                    case 5: realizarEmprestimoMenu(); break;
                    case 6: realizarDevolucaoMenu(); break;
                    case 7: listarUsuarios(); break;
                    case 8: listarAcervo(); break;
                    case 9: listarEmprestimosAtivos(); break;
                    case 10: listarEmprestimosAtrasados(); break;
                    case 11: salvarDados(); break;
                    case 12: carregarDados(); break;
                    case 0: System.out.println("Saindo..."); break;
                    default: System.out.println("Opção inválida!");
                }
            } catch (RegraDeNegocioException e) {
                System.err.println("ERRO: " + e.getMessage());
            }
            
            if (opcao != 0) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
        } while (opcao != 0);
    }
    
    private void cadastrarAluno() {
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Curso: ");
        String curso = scanner.nextLine();
        
        Aluno aluno = new Aluno(id, nome, endereco, matricula, curso);
        adicionarUsuario(aluno);
        System.out.println("Aluno cadastrado com sucesso!");
    }
    
    private void cadastrarProfessor() {
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("SIAPE: ");
        String siape = scanner.nextLine();
        System.out.print("Departamento: ");
        String departamento = scanner.nextLine();
        
        Professor professor = new Professor(id, nome, endereco, siape, departamento);
        adicionarUsuario(professor);
        System.out.println("Professor cadastrado com sucesso!");
    }
    
    private void cadastrarLivro() {
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Ano: ");
        int ano = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Autor: ");
        String autor = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Edição: ");
        int edicao = scanner.nextInt();
        scanner.nextLine();
        
        Livro livro = new Livro(codigo, titulo, ano, autor, isbn, edicao);
        adicionarItem(livro);
        System.out.println("Livro cadastrado com sucesso!");
    }
    
    private void cadastrarRevista() {
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Ano: ");
        int ano = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Editora: ");
        String editora = scanner.nextLine();
        System.out.print("Volume: ");
        int volume = scanner.nextInt();
        scanner.nextLine();
        System.out.print("ISSN: ");
        String issn = scanner.nextLine();
        
        Revista revista = new Revista(codigo, titulo, ano, editora, volume, issn);
        adicionarItem(revista);
        System.out.println("Revista cadastrada com sucesso!");
    }
    
    private void realizarEmprestimoMenu() throws RegraDeNegocioException {
        System.out.print("ID do Usuário: ");
        String idUsuario = scanner.nextLine();
        System.out.print("Código do Item: ");
        String codItem = scanner.nextLine();
        
        Emprestimo emp = realizarEmprestimo(idUsuario, codItem);
        System.out.println("Empréstimo realizado com sucesso!");
        System.out.println("Prazo de devolução: " + emp.getDataDevolucaoPrevista());
    }
    
    private void realizarDevolucaoMenu() throws RegraDeNegocioException {
        System.out.print("ID do Empréstimo: ");
        String idEmp = scanner.nextLine();
        
        realizarDevolucao(idEmp);
        System.out.println("Devolução realizada com sucesso!");
    }
    
    private void listarUsuarios() {
        System.out.println("\n--- USUÁRIOS CADASTRADOS ---");
        listaUsuarios.forEach(u -> System.out.println(u));
    }
    
    private void listarAcervo() {
        System.out.println("\n--- ACERVO ---");
        acervo.forEach(i -> System.out.println(i));
    }
    
    private void listarEmprestimosAtivos() {
        System.out.println("\n--- EMPRÉSTIMOS ATIVOS ---");
        historicoEmprestimos.stream()
            .filter(e -> e.getDataDevolucaoReal() == null)
            .forEach(e -> System.out.println(e));
    }
    
    private void listarEmprestimosAtrasados() {
        System.out.println("\n--- EMPRÉSTIMOS ATRASADOS ---");
        historicoEmprestimos.stream()
            .filter(e -> e.getDataDevolucaoReal() == null)
            .filter(e -> e.getDataDevolucaoPrevista().isBefore(LocalDate.now()))
            .forEach(e -> {
                System.out.println(e);
                long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                    e.getDataDevolucaoPrevista(), LocalDate.now());
                System.out.println("  Dias de atraso: " + diasAtraso);
            });
    }
    
    // Método main pra executar o programa
    public static void main(String[] args) {
        SistemaBiblioteca sistema = new SistemaBiblioteca();
        sistema.exibirMenu();
    }
}
