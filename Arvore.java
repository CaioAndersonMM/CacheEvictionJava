public class Arvore {
    private NoAVL raiz;

    public NoAVL getRaiz(){
        return this.raiz;
    }

    public void setRaiz(NoAVL raiz){
        this.raiz = raiz;
    }

    private int altura(NoAVL no) {
        if (no == null)
            return -1;

        return no.altura;
    }

    private int maior(int a, int b){
        return (a > b) ? a : b;
    }

    public NoAVL inserir(NoAVL node, OrdemServico os) {    
        if (node == null)
            return new NoAVL(os);
            
        if (os.getCodigo() < node.os.getCodigo())
            node.esq = inserir(node.esq, os);
        else if (os.getCodigo() > node.os.getCodigo())
            node.dir = inserir(node.dir, os);
        else
            return node;

        //Atualizar alturas do ancestral

        node.altura = 1 + maior(altura(node.esq), altura(node.dir));

        // 3. Obtenha o fator de balanceamento deste nó ancestral

        int fb = obterFB(node);
        int fbSubArvEsq = obterFB(node.esq);
        int fbSubArvDir = obterFB(node.dir);

        //4 possibilidades:

        if (fb > 1 && fbSubArvEsq >= 0){
            return rotacaoDireitaSimples(node);
        }

        if (fb < -1 && fbSubArvDir <= 0 ) {
            return rotacaoEsquerdaSimples(node);
        }

        // Rotação dupla direita
        if (fb > 1 && fbSubArvEsq < 0) {
            node.esq = rotacaoEsquerdaSimples(node.esq);
            return rotacaoDireitaSimples(node);
        }
            // Rotação dupla esquerda
        if (fb < -1 && fbSubArvDir > 0) {
            node.dir = rotacaoDireitaSimples(node.dir);
            return rotacaoEsquerdaSimples(node);
        }

        return node;

    }

    public int obterFB(NoAVL node){
        if (node == null)
            return 0;

        return altura(node.esq) - altura(node.dir);
    }

    private NoAVL rotacaoEsquerdaSimples(NoAVL x){
        NoAVL y = x.dir; // guarda subarvore direita de x em y (arvore guardada)
        NoAVL z = y.esq; // subarvore esquerda da arvore guardada, y, é guardada.

        // Rotação
        y.esq = x; // árvore original na subárvore esquerda da árvore guardada
        x.dir = z; /* subárvore direita da árvore original recebe subárvore
        esquerda da árvore guardada */

        // Atualiza alturas dos nós
        x.altura = maior(altura(x.esq), altura(x.dir)) + 1;
        y.altura = maior(altura(y.esq), altura(y.dir)) + 1;

        // Retorna a nova raiz
        return y;
    }

    private NoAVL rotacaoDireitaSimples(NoAVL y){
        NoAVL x = y.esq; // guarda a subarvore esquerda da árvore original
        NoAVL z = x.dir; // subarvore direita da subarvore guardada
        // Executa rotação

        x.dir = y;
        y.esq = z;

        // Atualiza as alturas
        y.altura = maior(altura(y.esq), altura(y.dir)) + 1;
        x.altura = maior(altura(x.esq), altura(x.dir)) + 1;

        // Retorna a nova raiz
        return x;
    }

    NoAVL remover(NoAVL node, OrdemServico os){
        if (node == null)
            return node;

        if (os.getCodigo() < node.os.getCodigo())
            node.esq = remover(node.esq, os);
        else if (os.getCodigo() > node.os.getCodigo())
            node.dir = remover(node.dir, os);
        else{
            //Nó sem filhos
            if (node.esq == null && node.dir == null)
                node = null;

            //Nó só com filho a direita
            else if (node.esq == null){
                NoAVL temp = node;
                node = temp.dir;
                temp = null;
            }

            //Só filho a esquerda
            else if (node.dir == null){
                NoAVL temp = node;
                node = temp.esq;
                temp = null;
            }

            else{
                /*Nó com 2 filhos: pegue o sucessor do percurso em ordem
                * Menor chave da subárvore direita do nó */
                NoAVL temp = noMenorChave(node.dir);
                node.os = temp.os;
                node.dir = remover(node.dir, temp.os);
            }
        }

        if (node == null)
            return node;

        /* Atualiza altura do (pai) do Nó inserido */
        node.altura = Math.max(altura(node.esq), altura(node.dir)) + 1;

        int fb = obterFB(node);
        int fbSubArvEsq = obterFB(node.esq);
        int fbSubArvDir = obterFB(node.dir);

        //4 possibilidades:
        if (fb > 1 && fbSubArvEsq >= 0){
            return rotacaoDireitaSimples(node);
        }

        if (fb < -1 && fbSubArvDir <= 0 ) {
            return rotacaoEsquerdaSimples(node);
        }

        // Rotação dupla direita
        if (fb > 1 && fbSubArvEsq < 0) {
            node.esq = rotacaoEsquerdaSimples(node.esq);
            return rotacaoDireitaSimples(node);
        }
            // Rotação dupla esquerda
        if (fb < -1 && fbSubArvDir > 0) {
            node.dir = rotacaoDireitaSimples(node.dir);
            return rotacaoEsquerdaSimples(node);
        }

        return node;
    }

    private NoAVL noMenorChave(NoAVL node) {
        NoAVL atual = node;
        while (atual.esq != null)
            atual = atual.esq;

        return atual;
    }


    public NoAVL buscar(int codigo) {
        return buscar(raiz, codigo);
    }

    private NoAVL buscar(NoAVL node, int codigo) {
        if (node == null) {
            return null;
        }

        if (codigo == node.os.getCodigo()) {
            return node;
        } else if (codigo < node.os.getCodigo()) {
            return buscar(node.esq, codigo);
        } else {
            return buscar(node.dir, codigo);
        }
    }

    public void verArvore(){
        verArvore(this.raiz, 0);
    }

    private void verArvore(NoAVL no, int nivel){
        System.out.print(no.os.getCodigo() + " - "); System.out.print(no.os.getNome()); System.out.print(" - Nível " + nivel);
        System.out.println();

        if (no.esq != null) {
            verArvore(no.esq, nivel+1);
        }
        if (no.dir != null) {
            verArvore(no.dir, nivel+1);
        }
    }

    public void listarOS() {
       listarOS(this.raiz);
    }

    private void listarOS(NoAVL no) {
        if (no != null) {
            listarOS(no.esq);
            System.out.println("-----------------------------");
            System.out.println("Código: " + no.os.getCodigo());
            System.out.println("Nome: " + no.os.getNome());
            System.out.println("Descrição: " + no.os.getDescricao());
            System.out.println("Hora da Solicitação: " + no.os.getHoraSolicitacao());
            System.out.println("-----------------------------");
            listarOS(no.dir);
        }
    }

    public void alterar(OrdemServico novaOS) {
        
        NoAVL node = buscar(novaOS.getCodigo());
        
        if (node != null) {
            node.os.setNome(novaOS.getNome());
            node.os.setDescricao(novaOS.getDescricao());
            node.os.setHoraSolicitacao(novaOS.getHoraSolicitacao());
        } else {
            System.out.println("Ordem de Serviço não encontrada.");
        }
    }
}
