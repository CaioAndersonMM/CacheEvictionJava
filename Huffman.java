import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NoHuffman {
    char letra;
    int frequencia;
    NoHuffman esquerda;
    NoHuffman direita;

    public NoHuffman(char letra, int frequencia) {
        this.letra = letra;
        this.frequencia = frequencia;
        this.esquerda = null;
        this.direita = null;
    }
}

class ListaDePrioridade { // Tá aos savesso
    private List<NoHuffman> lista;

    public ListaDePrioridade() {
        this.lista = new ArrayList<>();
    }

    // Adiciona nó na lista ordenada por frequência
    public void adicionar(NoHuffman no) {
        int i = 0;
        while (i < lista.size() && lista.get(i).frequencia <= no.frequencia) {
            i++;
        }
        lista.add(i, no);
    }

    // Remove e retorna o nó com a menor frequência (primeiros)
    public NoHuffman removerMenor() {
        if (!lista.isEmpty()) {
            return lista.remove(0);
        }
        return null;
    }

    public int tamanho() {
        return lista.size();
    }
}

public class Huffman {
    private NoHuffman raiz;
    private Map<Character, String> codigosHuffman = new HashMap<>(); // Para armazenar os códigos de Huffman

    // Contar as frequências de cada letra - ED I
    private Map<Character, Integer> contarFrequencias(String texto) {
        Map<Character, Integer> frequencias = new HashMap<>();
        for (char letra : texto.toCharArray()) {
            frequencias.put(letra, frequencias.getOrDefault(letra, 0) + 1);
        }
        return frequencias;
    }

    private NoHuffman construirArvoreHuffman(Map<Character, Integer> frequencias) {
        ListaDePrioridade listaPrioridade = new ListaDePrioridade();

        // Pegando as frequências e adicionando como NÓ HUFFMAN na lista de prioridade
        for (Map.Entry<Character, Integer> entrada : frequencias.entrySet()) {
            NoHuffman novoNo = new NoHuffman(entrada.getKey(), entrada.getValue());
            listaPrioridade.adicionar(novoNo);
        }

        while (listaPrioridade.tamanho() > 1) {

            // Pega os dois de menor frequência
            NoHuffman menor1 = listaPrioridade.removerMenor();
            NoHuffman menor2 = listaPrioridade.removerMenor();

            // Cria um novo nó combinando os dois
            NoHuffman novoNo = new NoHuffman('\0', menor1.frequencia + menor2.frequencia);
            novoNo.esquerda = menor1;
            novoNo.direita = menor2;

            listaPrioridade.adicionar(novoNo);
        }

        // Quando terminar as combinações, o último será a raiz
        return listaPrioridade.removerMenor();
    }

    private void gerarCodigos(NoHuffman no, String codigo) {
        if (no == null) {
            return;
        }
        if (no.esquerda == null && no.direita == null) { //Somente as folhas são letras!
            codigosHuffman.put(no.letra, codigo); // Mapeia a letra ao código
        } else {
            gerarCodigos(no.esquerda, codigo + '0');
            gerarCodigos(no.direita, codigo + '1');
        }
    }

    public String comprimir(String texto) {
        StringBuilder textoComprimido = new StringBuilder();
        for (char letra : texto.toCharArray()) {
            String codigo = codigosHuffman.get(letra);
            if (codigo != null) {
                textoComprimido.append(codigo);
            }
        }
        return textoComprimido.toString();
    }

    public String descomprimir(String textoComprimido) {
        StringBuilder textoOriginal = new StringBuilder();
        NoHuffman no = raiz;

        // Percorre bit por bit até achar a folha e pegar a letra
        for (char c : textoComprimido.toCharArray()) {
            no = (c == '0') ? no.esquerda : no.direita; // Vai para esquerda se '0', direita se '1'
            if (no.esquerda == null && no.direita == null) { //Se é folha é letra, encontramos
                textoOriginal.append(no.letra);
                no = raiz; // Retorna à raiz para continuar a busca
            }
        }
        return textoOriginal.toString();
    }

    // Precisei adicionar esse método para inicializar a árvore de Huffman para um
    // texto específico, já que picotando o texto em várias partes não dá para
    // garantir que a árvore será a mesma e a descompressão não funcionará direito
    public void inicializar(String[] textos) {
        Map<Character, Integer> frequencias = new HashMap<>();
        for (String texto : textos) {
            Map<Character, Integer> freqTexto = contarFrequencias(texto);
            for (Map.Entry<Character, Integer> entrada : freqTexto.entrySet()) {
                frequencias.put(entrada.getKey(), frequencias.getOrDefault(entrada.getKey(), 0) + entrada.getValue());
            }
        }
        raiz = construirArvoreHuffman(frequencias);
        gerarCodigos(raiz, "");
    }
}
