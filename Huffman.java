import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Classe para os nós da árvore de Huffman
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

public class Huffman {
    private NoHuffman raiz;
    private Map<Character, String> codigosHuffman = new HashMap<>(); // Para armazenar os códigos de Huffman

    // Função para contar as frequências de cada letra
    private Map<Character, Integer> contarFrequencias(String texto) {
        Map<Character, Integer> frequencias = new HashMap<>();
        for (char letra : texto.toCharArray()) {
            frequencias.put(letra, frequencias.getOrDefault(letra, 0) + 1);
        }
        return frequencias;
    }

    private NoHuffman construirArvoreHuffman(Map<Character, Integer> frequencias) {
        List<NoHuffman> nos = new ArrayList<>();
        for (Map.Entry<Character, Integer> entrada : frequencias.entrySet()) {
            nos.add(new NoHuffman(entrada.getKey(), entrada.getValue()));
        }

        while (nos.size() > 1) {
            // Encontrar os dois nós com menores frequências
            NoHuffman menor1 = removerMenor(nos);
            NoHuffman menor2 = removerMenor(nos);

            // Criar um novo nó combinando os dois menores
            NoHuffman novoNo = new NoHuffman('\0', menor1.frequencia + menor2.frequencia);
            novoNo.esquerda = menor1;
            novoNo.direita = menor2;

            nos.add(novoNo); // Adiciona o novo nó de volta à lista
        }

        return nos.get(0); // A raiz da árvore
    }
// Função para remover o nó de menor frequência da lista
private NoHuffman removerMenor(List<NoHuffman> nos) {
    int indiceMenor = 0;
    for (int i = 1; i < nos.size(); i++) {
        if (nos.get(i).frequencia < nos.get(indiceMenor).frequencia) {
            indiceMenor = i;
        }
    }
    return nos.remove(indiceMenor);
}

// Função para gerar os códigos de Huffman
private void gerarCodigos(NoHuffman no, String codigo) {
    if (no == null) {
        return;
    }
    if (no.esquerda == null && no.direita == null) {
        codigosHuffman.put(no.letra, codigo); // Mapeia a letra ao código
    } else {
        gerarCodigos(no.esquerda, codigo + '0');
        gerarCodigos(no.direita, codigo + '1');
    }
}

// Função para comprimir o texto
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

// Função para descomprimir o texto
public String descomprimir(String textoComprimido) {
    StringBuilder textoOriginal = new StringBuilder();
    NoHuffman no = raiz;

    for (char c : textoComprimido.toCharArray()) {
        no = (c == '0') ? no.esquerda : no.direita; // Desce na árvore
        // Se é um nó folha, adiciona a letra ao texto original
        if (no.esquerda == null && no.direita == null) {
            textoOriginal.append(no.letra);
            no = raiz; // Retorna à raiz
        }
    }
    return textoOriginal.toString();
}

// Método para inicializar a árvore com os textos
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
