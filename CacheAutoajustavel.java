import java.io.FileWriter;
import java.io.IOException;

public class CacheAutoajustavel {
    private final int maxSize = 30;
    private OrdemServico[] cache;
    private int tamanho;

    public CacheAutoajustavel() {
        this.cache = new OrdemServico[maxSize];
        this.tamanho = 0;
    }

    public void adicionar(OrdemServico os) {
        if (tamanho >= maxSize) {
            removerUltimo();
        }

        for (int i = tamanho; i > 0; i--) {
            cache[i] = cache[i - 1];
        }
        // Adiciona no início
        cache[0] = os;
        tamanho++;
    }

    private void removerUltimo() {
        if (tamanho > 0) {
            OrdemServico removido = cache[tamanho - 1];
            cache[tamanho - 1] = null; // Limpa a referência
            tamanho--;
            escreverLog("Item removido da cache: " + removido.imprimir());
        }
    }

    public OrdemServico buscar(int codigo) {
        for (int i = 0; i < tamanho; i++) {
            OrdemServico os = cache[i];
            if (os.getCodigo() == codigo) {

                // Move o item para o início do array
                for (int j = i; j > 0; j--) {
                    cache[j] = cache[j - 1];
                }
                cache[0] = os;
                return os;
            }
        }
        return null;
    }
    public int contarElementos() {
        return tamanho;
    }

    public void listarCache() {
        System.out.println("Total de elementos na cache: " + contarElementos());
        for (int i = 0; i < tamanho; i++) {
            System.out.print("Índice " + i + ": " + cache[i].imprimir() + "\n");
        }
    }

    public void remover(int codigo) {
        for (int i = 0; i < tamanho; i++) {
            if (cache[i].getCodigo() == codigo) {
                OrdemServico removido = cache[i];
                // Move todos os itens para preencher o espaço
                for (int j = i; j < tamanho - 1; j++) {
                    cache[j] = cache[j + 1];
                }
                cache[tamanho - 1] = null; // Limpa a referência
                tamanho--;
                escreverLog("Item removido da cache: " + removido.imprimir());
                return;
            }
        }
    }

    private void escreverLog(String msg) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write(msg + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String gerarStringCache() {
        StringBuilder sb = new StringBuilder();
        boolean cacheVazia = true;

        for (int i = 0; i < tamanho; i++) {
            sb.append(cache[i].imprimir());
            cacheVazia = false;
        }

        return cacheVazia ? "Cache vazia" : sb.toString();
    }
}
