import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Servidor {
    private Arvore arvore;
    private CacheFIFO cache;

    public Servidor() {
        arvore = new Arvore();
        cache = new CacheFIFO();
    }

    public void mostrarCache() {
        System.out.println("Itens da Cache: ");
        cache.listarCache();
    }

    public void cadastrarOrdemServico(OrdemServico os) {
        escreverLog("");
        escreverLog("Insercao de Ordem de Servico: " + os.getCodigo());
        arvore.setRaiz(arvore.inserir(arvore.getRaiz(), os));

        NoAVL noBuscado = arvore.buscar(os.getCodigo());
        cache.adicionar(noBuscado); // colocando o no na cache

        escreverLog("Altura da arvore: " + arvore.getRaiz().altura + ", Time: " + now());
        escreverLog("Itens da cache: " + cache.gerarStringCache());

    }

    public void removerOrdemServico(OrdemServico removeOS) {

        escreverLog("");
        escreverLog("Remoção de Ordem de Serviço: " + removeOS.getCodigo());

        arvore.remover(removeOS);
        cache.remover(removeOS.getCodigo());

        
        escreverLog("Altura da Árvore: " + arvore.getRaiz().altura + ", Time: " + now());
        escreverLog("Itens da cache: " + cache.gerarStringCache());

    }

    public void atualizarOrdemServico(OrdemServico novaOS) {

        NoAVL noBuscado = buscarNaCache(novaOS.getCodigo());

        if (noBuscado != null) { // Encontrado na cache
            noBuscado.os.setNome(novaOS.getNome());
            noBuscado.os.setDescricao(novaOS.getDescricao());
            noBuscado.os.setHoraSolicitacao(novaOS.getHoraSolicitacao());
            System.out.println("Alterada na cache -> Árvore");

            escreverLog("");
            escreverLog("Alteracao na Ordem de Servido feita na cache: " + novaOS.getCodigo() + ", Time: " + now());
            escreverLog("Itens da cache: " + cache.gerarStringCache());

            return;
        }

        // Não tem na cache edita na árvore
        arvore.alterar(novaOS);

        // Reload no item da cache
        noBuscado = arvore.buscar(novaOS.getCodigo());
        cache.remover(novaOS.getCodigo());
        cache.adicionar(noBuscado); // Armazena referencia

        escreverLog("");
        escreverLog("Alteracao na Ordem de Servico: " + novaOS.getCodigo() + ", Time: " + now());
        escreverLog("Itens da cache: " + cache.gerarStringCache());

    }

    public NoAVL buscarNaCache(int codigo) {
        NoAVL no = cache.buscar(codigo);
        return no;

    }

    public OrdemServico buscarOrdemServico(int codigo) {

        // Primeiro busca na cache
        NoAVL buscado = buscarNaCache(codigo);
        if (buscado != null) {
            System.out.println("Ordem de Serviço encontrada na cache! ");
            return buscado.os;
        }

        // Não achou - busca na árvore
        buscado = arvore.buscar(codigo);

        if (buscado != null) { // Encontrou na árvore
            cache.adicionar(buscado); // Adiciona na cache
            System.out.println("Ordem de Serviço encontrada na base de dados (Árvore)! ");

            escreverLog("");
            escreverLog("Item buscado foi adicionado na Cache: " + codigo + ", Time: " + now());
            escreverLog("Itens da cache: " + cache.gerarStringCache());

            return buscado.os;
        }

        return null;
    }

    public void mostrarArvore() {
        System.out.println();
        this.arvore.verArvore();
        System.out.println();
    }

    public void mostrarOrdensServico() {
        System.out.println();
        this.arvore.listarOS();
        System.out.println();
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
