public class OrdemServico {
    private int codigo;
    private String nome;
    private String descricao;
    private long horaSolicitacao;

    public OrdemServico(int codigo, String nome, String descricao, long horaSolicitacao) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.horaSolicitacao = horaSolicitacao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public long getHoraSolicitacao() {
        return horaSolicitacao;
    }

    public String toString() {
        return "OrdemServico{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", horaSolicitacao=" + horaSolicitacao +
                '}';
    }
}
