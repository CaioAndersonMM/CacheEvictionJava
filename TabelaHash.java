import java.util.LinkedList;

public class TabelaHash {
    private int m;
    private LinkedList<Entrada>[] tabela = new LinkedList[m]; // Array de linkedlists
    private int tamanho;
    private static final double alfaCarga = 0.75;

    public TabelaHash() {
        // Inicializa com a primeira potência de 2 próxima e encontrar o menor primo
        m = encontrarMenorPrimo(proximaPotenciaDe2(30));
        tabela = new LinkedList[30];
        tamanho = 0;

        // Cada indice do array é uma linkedlist pois pode haver colisões
        for (int i = 0; i < tabela.length; i++) {
            tabela[i] = new LinkedList<>();
        }
    }

    public void inserir(int codigo, OrdemServico ordem) {
        if ((double) tamanho / tabela.length >= alfaCarga) { //Acima de 75% da capacidade
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

    private int hash(int codigo) {
        return Integer.hashCode(codigo) % tabela.length;
    }

    private void redimensionar() {
        LinkedList<Entrada>[] novaTabela = new LinkedList[tabela.length * 2];
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
    }

    private int proximaPotenciaDe2(int n) {
        int potencia = 1;
        while (potencia < n) {
            potencia *= 2;
        }
        return potencia;
    }

    private int encontrarMenorPrimo(int n) {
        while (!ehPrimo(n)) {
            n++;
        }
        return n;
    }

    private boolean ehPrimo(int n) {
        if (n <= 1) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
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
