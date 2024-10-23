import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private TabelaHash tabelaHash;
    private CacheAutoajustavel cache;
    private Huffman huffman;
    private String logs;

    public Servidor() {
        tabelaHash = new TabelaHash();
        cache = new CacheAutoajustavel();
        this.logs = lerLog();
    }

    public void mostrarCache() {
        System.out.println("Itens da Cache: ");
        cache.listarCache();
    }

    public void processarMensagem(Mensagem mensagem) {
        // Descomprimir os dados
        int cod = mensagem.getCod();
        String nomeDescomprimido = mensagem.descomprimirNome();
        String descricaoDescomprimida = mensagem.descomprimirDescricao();
        String horaDescomprimida = mensagem.descomprimirHora();
        String operacao = mensagem.descomprimirOperacao();

        switch (operacao) {
            case "Cadastrar":
                OrdemServico osNova = new OrdemServico(cod, nomeDescomprimido, descricaoDescomprimida,
                        horaDescomprimida);
                cadastrarOrdemServico(osNova);
                break;

            case "Alterar":
                // O próprio método editar já faz a busca e pega a referência
                OrdemServico OsEdit = new OrdemServico(cod, nomeDescomprimido, descricaoDescomprimida,
                        horaDescomprimida);
                atualizarOrdemServico(OsEdit);
                break;

            case "Remover":
                // Como usamos a referência, temos que buscar a OS para remover
                OrdemServico osRemover = buscarOrdemServico(cod, true);
                if (osRemover != null) {
                    removerOrdemServico(osRemover);
                } else {
                    System.out.println("Ordem de Serviço não encontrada.");
                }
                removerOrdemServico(osRemover);
                break;
            case "Listar":
                System.out.println("Listando Ordem de Serviço: ");
                mostrarTabelaHash();
                break;
            case "Quantidade":
                System.out.println("Quantidade de Registros: " + quantidadeDeRegistros());
                break;
            case "Buscar":
                OrdemServico osBuscada = buscarOrdemServico(cod, false);
                if (osBuscada != null) {
                    System.out.println(osBuscada);
                } else {
                    System.out.println("Ordem de Serviço não encontrada.");
                }
                break;
            case "Cache":
                mostrarCache();
                break;
            case "Mod":
                System.out.println(
                        "Mod: " + mostrarMod() + " Carga: " + String.format("%.2f", mostrarFatorDeCarga()) + "%");
                break;
            case "VerLog":
                mostrarOcorrencias(cod);
                break;
            default:
                System.out.println("Operação não reconhecida: " + operacao);
        }
    }

    public void cadastrarOrdemServico(OrdemServico os) {
        escreverLog("");
        escreverLog("Insercao de Ordem de Servico: " + os.getCodigo());
        System.out.println("Inserção de Ordem de Serviço: " + os.getCodigo());

        tabelaHash.inserir(os.getCodigo(), os);

        OrdemServico osBuscada = tabelaHash.buscar(os.getCodigo());

        cache.adicionar(osBuscada); // colocando os na cache (referência)
        escreverLog("Itens da cache: " + cache.gerarStringCache());
        escreverLog("Estado da Tabela Hash: " + tabelaHash.gerarString());
    }

    public void removerOrdemServico(OrdemServico removeOS) {

        escreverLog("");
        escreverLog("Remoção de Ordem de Serviço: " + removeOS.getCodigo());

        cache.remover(removeOS.getCodigo());
        tabelaHash.remover(removeOS.getCodigo());

        escreverLog("Itens da cache: " + cache.gerarStringCache());
        escreverLog("Estado da Tabela Hash: " + tabelaHash.gerarString());

    }

    public void atualizarOrdemServico(OrdemServico novaOS) {

        OrdemServico osBuscada = buscarNaCache(novaOS.getCodigo());

        if (osBuscada != null) { // Encontrado na cache
            osBuscada.setNome(novaOS.getNome());
            osBuscada.setDescricao(novaOS.getDescricao());
            osBuscada.setHoraSolicitacao(novaOS.getHoraSolicitacao());
            System.out.println("Alterada na cache -> Árvore");

            escreverLog("");
            escreverLog("Alteracao na Ordem de Servido feita na cache: " + novaOS.getCodigo() + ", Time: " + now());
            escreverLog("Itens da cache: " + cache.gerarStringCache());
            escreverLog("Estado da Tabela Hash: " + tabelaHash.gerarString());
            return;
        }

        // Não tem na cache edita na árvore
        tabelaHash.alterarOrdemServico(novaOS);

        // Reload no item da cache
        osBuscada = tabelaHash.buscar(novaOS.getCodigo());
        cache.remover(novaOS.getCodigo());
        cache.adicionar(osBuscada); // Armazena referencia

        escreverLog("");
        escreverLog("Alteracao na Ordem de Servico: " + novaOS.getCodigo() + ", Time: " + now());
        escreverLog("Itens da cache: " + cache.gerarStringCache());
        escreverLog("Estado da Tabela Hash: " + tabelaHash.gerarString());

    }

    public OrdemServico buscarNaCache(int codigo) {
        OrdemServico os = cache.buscar(codigo);
        return os;
    }

    public OrdemServico buscarOrdemServico(int codigo, boolean isRemove) {

        // Primeiro busca na cache
        OrdemServico buscado = buscarNaCache(codigo);
        if (buscado != null) {
            System.out.println("Ordem de Serviço encontrada na cache! ");
            return buscado;
        }

        // Não achou - busca na árvore
        buscado = tabelaHash.buscar(codigo);

        if (buscado != null) { // Encontrou na árvore

            System.out.println("Ordem de Serviço encontrada na base de dados! ");

            if (!isRemove) {
                cache.adicionar(buscado); // Adiciona na cache APENAS se não for buscar de remoção
                escreverLog("Item buscado foi adicionado na Cache: " + codigo + ", Time: " + now());
            }

            escreverLog("");
            escreverLog("Itens da cache: " + cache.gerarStringCache());

            return buscado;
        }

        return null;
    }

    public void mostrarTabelaHash() {
        System.out.println();
        this.tabelaHash.imprimirTabela();
        System.out.println();
    }

    // public void mostrarOrdensServico() {
    // System.out.println();
    // this.tabelaHash.listarOS();
    // System.out.println();
    // }

    public int quantidadeDeRegistros() {
        return this.tabelaHash.getTamanho();
    }

    public int mostrarMod() {
        return this.tabelaHash.getMod();
    }

    public float mostrarFatorDeCarga() {
        return this.tabelaHash.getFatorDeCarga();
    }

    private String now() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public void mostrarOcorrencias(int operacao) {
        logs = lerLog(); // Lê o log de novo
        switch (operacao) {
            case 1:
                List<Integer> insercaoOcorrencias = buscarOcorrencias("Insercao de Ordem de Servico");
                System.out.println("Ocorrências de Inserção: " + insercaoOcorrencias.size());
                break;
            case 2:
                List<Integer> remocaoOcorrencias = buscarOcorrencias("Remoção de Ordem de Serviço");
                System.out.println("Ocorrências de Remoção: " + remocaoOcorrencias.size());
                break;
            case 3:
                List<Integer> alteracaoOcorrencias = buscarOcorrencias("Alteracao na Ordem de Servido");
                System.out.println("Ocorrências de Alteração: " + alteracaoOcorrencias.size());
                break;
            case 4:
                List<Integer> buscaOcorrencias = buscarOcorrencias("Item buscado");
                System.out.println("Ocorrências de Busca: " + buscaOcorrencias.size());
                break;
            default:
                break;
        }
    }

    private int[] construirLPS(String operacao) {
        int[] lps = new int[operacao.length()];
        int length = 0; // prefixo anterior
        int i = 1;

        while (i < operacao.length()) {
            if (operacao.charAt(i) == operacao.charAt(length)) {
                length++;
                lps[i] = length;
                i++;
            } else {
                if (length != 0) {
                    length = lps[length - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    public List<Integer> buscarOcorrencias(String operacao) {
        List<Integer> ocorrencias = new ArrayList<>();
        int[] lps = construirLPS(operacao);
        int i = 0; // logs
        int j = 0; // operacao

        while (i < logs.length()) {
            // Se os caracteres são iguais, incrementa ambos
            if (operacao.charAt(j) == logs.charAt(i)) {
                i++;
                j++;
            }

            if (j == operacao.length()) {
                ocorrencias.add(i - j); //Indice da ocorrencia
                j = lps[j - 1]; // Ajusta j
            } else if (i < logs.length() && operacao.charAt(j) != logs.charAt(i)) {
                // Descontinuidade encontrada
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        escreverLog("Buscando ocorrências da operação: " + operacao.substring(0, 8) + ". Total encontrado: " + ocorrencias.size());
        System.out.println("Índices encontrados: " + ocorrencias);
        return ocorrencias;
    }

    private String lerLog() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("log.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            return "";
        }
        return sb.toString();
    }

    private void escreverLog(String msg) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("log.txt", true), "UTF-8"))) {
            writer.write(msg + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
