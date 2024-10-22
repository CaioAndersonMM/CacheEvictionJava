public class OrdemServico {
    private int codigo;
    private String nome;
    private String descricao;
    private String horaSolicitacao;

    public OrdemServico(int codigo, String nome, String descricao, String horaSolicitacao) {
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

    public String getHoraSolicitacao() {
        return horaSolicitacao;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setHoraSolicitacao(String horaSolicitacao) {
        this.horaSolicitacao = horaSolicitacao;
    }

    public String toString() {
        return String.format(
            "+=============================+%n" +
            "||       Ordem de Serviço      %n" +
            "===============================%n" +
            "|| Código:      %4d           %n" +
            "|| Nome:        %-15s %n" +
            "|| Descrição:   %-15s %n" +
            "|| Hora:        %s         %n" +
            "+=============================+%n",
            codigo, nome, descricao, horaSolicitacao
        );
    }

    public String imprimir() {
        return String.format("{cod = %d | %s} - ", codigo, nome);
    }
}
