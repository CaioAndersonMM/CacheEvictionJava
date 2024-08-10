public class NoAVL {
    OrdemServico os;
    NoAVL esq, dir;
    int altura;

    public NoAVL(OrdemServico os) {
        this.os = os;
        altura = 0;
    }
}
