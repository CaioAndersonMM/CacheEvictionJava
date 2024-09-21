import java.util.LinkedList;

public class TabelaHash {
    private int m;
    private LinkedList<Entrada>[] tabela; // Array de linkedlists
    private int tamanho;
    private static final double alfaCarga = 0.75;

    public TabelaHash() {
        // Inicializa com a primeira potência de 2 próxima e encontrar o menor primo apartir do 30
        m = encontrarMaiorPrimoAbaixo(proximaPotenciaDe2(30));
        tabela = new LinkedList[m];
        tamanho = 0;

        // Cada indice do array é uma linkedlist pois pode haver colisões (Encadeamento Aberto)
        for (int i = 0; i < tabela.length; i++) {
            tabela[i] = new LinkedList<>();
        }
    }

    public void inserir(int codigo, OrdemServico ordem) {
        if ((double) tamanho / tabela.length >= alfaCarga) { // Acima de 75% da capacidade
            redimensionar();
        }

        int indice = hash(codigo);
        LinkedList<Entrada> lista = tabela[indice];

        for (Entrada entrada : lista) {
            if (entrada.codigo == codigo) {
                entrada.ordem = ordem;
                return;
            }
        }

        lista.add(new Entrada(codigo, ordem));
        tamanho++;
    }

    public void remover(int codigo) {
        int indice = hash(codigo);
        LinkedList<Entrada> lista = tabela[indice];

        for (Entrada entrada : lista) {
            if (entrada.codigo == codigo) {
                lista.remove(entrada);
                tamanho--;

                if ((double) tamanho / tabela.length < 0.25) {
                    redimensionarParaMenor();
                }

                return;
            }
        }
    }

    public OrdemServico buscar(int codigo) {
        int indice = hash(codigo);
        LinkedList<Entrada> lista = tabela[indice];

        for (Entrada entrada : lista) {
            if (entrada.codigo == codigo) {
                return entrada.ordem;
            }
        }
        return null;
    }

    public void alterarOrdemServico(OrdemServico novaOrdem) {
        int indice = hash(novaOrdem.getCodigo());
        LinkedList<Entrada> lista = tabela[indice];
    
        for (Entrada entrada : lista) {
            if (entrada.codigo == novaOrdem.getCodigo()) {
                entrada.ordem = novaOrdem;
                System.out.println("Ordem de serviço " + novaOrdem.getCodigo() + " foi alterada.");
                return;
            }
        }
    
        System.out.println("Ordem de serviço com código " + novaOrdem.getCodigo() + " não encontrada.");
    }

    private int hash(int codigo) {
        return Integer.hashCode(codigo) % tabela.length;
    }

    private void redimensionar() {
        int novoTamanho = encontrarMaiorPrimoAbaixo(proximaPotenciaDe2(tabela.length * 2));
        LinkedList<Entrada>[] novaTabela = new LinkedList[novoTamanho];
        for (int i = 0; i < novaTabela.length; i++) {
            novaTabela[i] = new LinkedList<>();
        }

        for (LinkedList<Entrada> lista : tabela) {
            for (Entrada entrada : lista) {
                int indice = Integer.hashCode(entrada.codigo) % novaTabela.length;
                novaTabela[indice].add(entrada);
            }
        }

        tabela = novaTabela;
        m = novoTamanho;
    }

    private void redimensionarParaMenor() {
        int novoTamanho = encontrarMaiorPrimoAbaixo(proximaPotenciaDe2(tabela.length / 2));    
        LinkedList<Entrada>[] novaTabela = new LinkedList[novoTamanho];
        for (int i = 0; i < novaTabela.length; i++) {
            novaTabela[i] = new LinkedList<>();
        }
    
        for (LinkedList<Entrada> lista : tabela) {
            for (Entrada entrada : lista) {
                int indice = Integer.hashCode(entrada.codigo) % novaTabela.length;
                novaTabela[indice].add(entrada);
            }
        }
    
        tabela = novaTabela;
        m = novoTamanho;
    }


    public void imprimirTabela() {
        for (int i = 0; i < tabela.length; i++) {
            System.out.print(i + " -> ");
            LinkedList<Entrada> lista = tabela[i];
            if (lista.isEmpty()) {
                System.out.println();
            } else {
                for (Entrada entrada : lista) {
                    System.out.print(entrada.codigo + " | ");
                }
                System.out.println();
            }
        }
    }

    public int getTamanho() {
        return this.tamanho;
    }

    public int getMod(){
        return this.m;
    }

    public float getFatorDeCarga(){
        return (float) tamanho / tabela.length;
    }

    private int proximaPotenciaDe2(int n) {
        int potencia = 1;
        while (potencia < n) {
            potencia *= 2;
        }
        return potencia;
    }

    private int encontrarMaiorPrimoAbaixo(int n) {
        n--;  // Começa com o número imediatamente abaixo da potência de 2
        while (!ehPrimo(n)) {
            n--;
        }
        return n;
    }

    private boolean ehPrimo(int n) {
        if (n <= 1)
            return false;
        if (n == 2 || n == 3)
            return true;
        if (n % 2 == 0 || n % 3 == 0)
            return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0)
                return false;
        }
        return true;
    }

    private static class Entrada {
        int codigo;
        OrdemServico ordem;

        Entrada(int codigo, OrdemServico ordem) {
            this.codigo = codigo;
            this.ordem = ordem;
        }
    }
}
