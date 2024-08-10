public class Servidor {
    private Arvore arvore;
    private CacheFIFO cache;

    public Servidor() {
        arvore = new Arvore();
        cache = new CacheFIFO();
    }
    
    public void cadastrarOrdemServico(OrdemServico os) {
        arvore.setRaiz(arvore.inserir(arvore.getRaiz(), os));
        System.out.println("Ordem de Serviço cadastrada: ");
        System.out.println(os);

        OrdemServico osBuscada = arvore.buscar(os.getCodigo()).os;
        cache.adicionar(osBuscada); //colocando a referencia na cache

    }

    public void removerOrdemServico(int codigo) {



    }

    public void atualizarOrdemServico(OrdemServico novaOS) {
        arvore.alterar(novaOS);

        //Reload no item da cache
        cache.remover(novaOS.getCodigo());
        cache.adicionar(novaOS);
    }

    public OrdemServico buscarOrdemServico(int codigo) {
        OrdemServico os = cache.buscar(codigo);

        //Primeiro busca na cache
        if (os != null) {
            System.out.println("Ordem de Serviço encontrada na cache: ");
            return os;
        }

        //Não achou
        
        os = arvore.buscar(codigo).os;

        if (os != null) { //Encontrou na árvore
            cache.adicionar(os); // Adiciona na cache
            System.out.println("Ordem de Serviço encontrada na base de dados (Árvore): " + os);
            return os;
        }

        System.out.println("Ordem de Serviço não encontrada na base de dados.");
        return null;
    }

    public void mostrarArvore(){
        System.out.println();
        this.arvore.verArvore();
        System.out.println();
    }
    
    public void mostrarOrdensServico(){
        System.out.println();
        this.arvore.listarOS();
        System.out.println();
    }
}
