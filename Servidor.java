import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Servidor {
    private TabelaHash tabelaHash;
    private CacheHash cache;

    public Servidor() {
        tabelaHash = new TabelaHash();
        cache = new CacheHash();
    }

    public void mostrarCache() {
        System.out.println("Itens da Cache: ");
        cache.listarCache();
    }

    public void cadastrarOrdemServico(OrdemServico os) {
        escreverLog("");
        escreverLog("Insercao de Ordem de Servico: " + os.getCodigo());

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

            System.out.println("Ordem de Serviço encontrada na base de dados (Árvore)! ");

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
    //     System.out.println();
    //     this.tabelaHash.listarOS();
    //     System.out.println();
    // }

    public int quantidadeDeRegistros() {
        return this.tabelaHash.getTamanho();
    }

    public int mostrarMod(){
        return this.tabelaHash.getMod();
    }

    public float mostrarFatorDeCarga(){
        return this.tabelaHash.getFatorDeCarga();
    }
    
    private String now() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
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
