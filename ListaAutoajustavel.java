import java.util.ArrayList;
import java.util.List;

public class ListaAutoajustavel {
    private List<OrdemServico> ordens;

    public ListaAutoajustavel() {
        this.ordens = new ArrayList<>();
    }

    public void adicionar(OrdemServico ordem) {
        ordens.add(ordem);
    }

    public OrdemServico buscar(int codigo) {
        for (OrdemServico ordem : ordens) {
            if (ordem.getCodigo() == codigo) {
                ordens.remove(ordem);
                ordens.add(0, ordem);
                return ordem;
            }
        }
        return null;
    }

    public void remover(int codigo) {
        ordens.removeIf(ordem -> ordem.getCodigo() == codigo);
    }

    public int contarElementos() {
        return ordens.size();
    }

    public OrdemServico buscarPorIndice(int index) {
        if (index < 0 || index >= ordens.size()) {
            return null;
        }
        return ordens.get(index);
    }

    public void listarOrdens() {
        for (OrdemServico ordem : ordens) {
            System.out.println(ordem.imprimir());
        }
    }
}
