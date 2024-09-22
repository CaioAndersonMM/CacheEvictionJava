import java.util.LinkedList;
import java.util.Random;

public class CacheHash {
    private final int maxSize;
    private LinkedList<OrdemServico>[] tabela;
    private Random random;
    private int tamanho;

    public CacheHash() {
        this.maxSize = 20;
        this.tabela = new LinkedList[31]; //Ajuste no tamanho/mod para evitar muitas colisões
        this.random = new Random();
        this.tamanho = 0;

        for (int i = 0; i < tabela.length; i++) {
            tabela[i] = new LinkedList<>();
        }
    }

    public void adicionar(OrdemServico os) {
        if (tamanho >= maxSize) {
            removerAleatorio();
        }

        int indice = hash(os.getCodigo());
        tabela[indice].add(os);
        tamanho++;
    }

    private void removerAleatorio() {
        if (tamanho == 0)
            return;

        int indiceAleatorio;
        do {
            indiceAleatorio = random.nextInt(tabela.length); //Indice aleatório
        } while (tabela[indiceAleatorio].isEmpty()); // Deve ter OS

        // OS aleatória do índice aleatório
        LinkedList<OrdemServico> lista = tabela[indiceAleatorio];
        int osRandom = random.nextInt(lista.size());
        lista.remove(osRandom);
        tamanho--;
    }

    public OrdemServico buscar(int codigo) {
        int indice = hash(codigo);
        for (OrdemServico os : tabela[indice]) {
            if (os.getCodigo() == codigo) {
                return os;
            }
        }
        return null;
    }

    public int contarElementos() {
        int contagem = 0;
        for (int i = 0; i < tabela.length; i++) {
            contagem += tabela[i].size();
        }
        return contagem;
    }

    public void listarCache() {

        System.out.println("Total de elementos na cache: " + contarElementos());

        for (int i = 0; i < tabela.length; i++) {
            System.out.print("Índice " + i + ": ");
            if (tabela[i].isEmpty()) {
                System.out.println("vazio");
            } else {
                for (OrdemServico os : tabela[i]) {
                    System.out.print(os.imprimir() + ", ");
                }
                System.out.println();
            }
        }
    }

    public void remover(int codigo) {
        int indice = hash(codigo);
        if (tabela[indice].removeIf(os -> os.getCodigo() == codigo)) {
            tamanho--;
        }
    }

    private int hash(int codigo) {
        return Integer.hashCode(codigo) % tabela.length;
    }

    public String gerarStringCache() {
        StringBuilder sb = new StringBuilder();
        boolean cacheVazia = true; // Flag

        for (int i = 0; i < tabela.length; i++) {
            if (!tabela[i].isEmpty()) {
                sb.append("Índice ").append(i).append(": ");
                for (OrdemServico os : tabela[i]) {
                    sb.append(os.imprimir()).append(", ");
                }
                sb.setLength(sb.length() - 2);
                sb.append("\n");
                cacheVazia = false;
            }
        }

        return cacheVazia ? "Cache vazia" : sb.toString();
    }
}
