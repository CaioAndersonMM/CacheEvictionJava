public class Servidor {
    private Arvore arvore;
    private CacheFIFO cache;

    public Servidor() {
        arvore = new Arvore();
        cache = new CacheFIFO();
    }
    
    public void cadastrarOrdemServico(OrdemServico os) {
        arvore.setRaiz(arvore.inserir(arvore.getRaiz(), os));
        System.out.println("Ordem de Serviço cadastrada: " + os);
    }

    public OrdemServico buscarOrdemServico(int codigo) {
        OrdemServico os = cache.buscar(codigo);

        //Primeiro busca na cache
        if (os != null) {
            System.out.println("Ordem de Serviço encontrada na cache: " + os);
            return os;
        }

        //Não achou
        
        NoAVL no = arvore.buscar(arvore.getRaiz(), codigo);

        if (no != null) {
            os = no.os;
            cache.adicionar(os);
            System.out.println("Ordem de Serviço encontrada na base de dados (Árvore): " + os);
            return os;
        }

        System.out.println("Ordem de Serviço não encontrada na base de dados.");
        return null;
    }


}
