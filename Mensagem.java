public class Mensagem {
    private int cod;
    private String nomeComprimido;
    private String descricaoComprimida;
    private String horaComprimida;
    private String operacaoComprimida;
    private Huffman huffman;

    public Mensagem(int cod, String operacao, String nome, String descricao, String hora) {
        this.cod = cod;
        this.huffman = new Huffman();
        
        // Inicializar a árvore com todos os textos
        String[] textos = {nome, descricao, hora, operacao};
        huffman.inicializar(textos);

        // Compressão
        this.nomeComprimido = huffman.comprimir(nome);
        this.descricaoComprimida = huffman.comprimir(descricao);
        this.horaComprimida = huffman.comprimir(hora);
        this.operacaoComprimida = huffman.comprimir(operacao);

        System.out.println("Nome original: " + nome);
        System.out.println("Nome comprimido: " + nomeComprimido);
        System.out.println("Descrição comprimida: " + descricaoComprimida);
        System.out.println("Hora comprimida: " + horaComprimida);
        System.out.println("Operação comprimida: " + operacaoComprimida);
    }

    public String descomprimirNome() {
        return huffman.descomprimir(nomeComprimido);
    }

    public String descomprimirDescricao() {
        return huffman.descomprimir(descricaoComprimida);
    }

    public String descomprimirHora() {
        return huffman.descomprimir(horaComprimida);
    }

    public String descomprimirOperacao() {
        return huffman.descomprimir(operacaoComprimida);
    }

    public int getCod() {
        return this.cod;
    }
}