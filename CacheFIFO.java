import java.util.LinkedList;

public class CacheFIFO {
    private final int maxSize;
    private LinkedList<NoAVL> cache;

    public CacheFIFO() {
        this.maxSize = 21;
        this.cache = new LinkedList<>();
    }

    public void adicionar(NoAVL os) {
        if (cache.size() >= maxSize) {
            cache.removeFirst();
        }
        cache.addLast(os);
    }

    public NoAVL buscar(int codigo) {
        for (NoAVL no : cache) {
            if (no.os.getCodigo() == codigo) { 
                return no;
            }
        }
        return null;
    }

    public void listarCache() {
        for (NoAVL no : cache) {
            System.out.println(no.os);
        }
    }

    public void remover(int codigo) {
        cache.removeIf(no -> no.os.getCodigo() == codigo);
    }

    public String gerarStringCache() {
        StringBuilder sb = new StringBuilder();
        for (NoAVL no : cache) {
            sb.append(no.os.imprimir()).append(", ");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "Cache vazia";
    }
}
