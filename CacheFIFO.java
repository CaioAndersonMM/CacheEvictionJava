import java.util.LinkedList;

public class CacheFIFO {
    private final int maxSize;
    private LinkedList<OrdemServico> cache;

    public CacheFIFO() {
        this.maxSize = 10;
        this.cache = new LinkedList<>();
    }

    public void adicionar(OrdemServico os) {
        if (cache.size() >= maxSize) {
            cache.removeFirst();
        }
        cache.addLast(os);
    }

    public OrdemServico buscar(int codigo) {
        for (OrdemServico os : cache) {
            if (os.getCodigo() == codigo) {
                return os;
            }
        }
        return null;
    }

    public void listarCache() {
        for (OrdemServico os : cache) {
            System.out.println(os);
        }
    }

    public void remover(int codigo) {
        cache.removeIf(os -> os.getCodigo() == codigo);
    }
}
