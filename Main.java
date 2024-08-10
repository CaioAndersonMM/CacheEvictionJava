import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Servidor server = new Servidor();

        gerarOrdensFicticias(server);

        server.mostrarArvore();

        while (true) {
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Alterar");
            System.out.println("4 - Remover");
            System.out.println("5 - Acessar Quantidade de Registros");
            System.out.println("6 - Ver Cache");

            int escolha = sc.nextInt();

            switch (escolha) {
                case 1:
                    System.out.println("Digite o código da Ordem de Serviço:");
                    int codigo = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Digite o nome da Ordem de Serviço:");
                    String nome = sc.nextLine();

                    System.out.println("Digite a descrição da Ordem de Serviço:");
                    String descricao = sc.nextLine();

                    System.out.println("Digite a hora da solicitação (formato HHMM):");
                    int hora = sc.nextInt();
                    sc.nextLine(); 

                    OrdemServico novaOS = new OrdemServico(codigo, nome, descricao, hora);
                    server.cadastrarOrdemServico(novaOS);
                    break;
                case 2:
                    // System.out.println("Árvore AVL de Ordens de Serviço:");
                    // server.mostrarArvore();
                    server.mostrarOrdensServico();
                    break;
                case 3:
                    System.out.println("Digite o código da Ordem de Serviço para alterar:");
                    int codEdit = sc.nextInt();
                    sc.nextLine(); 

                    OrdemServico os = server.buscarOrdemServico(codEdit);
                    if (os != null) {
                        System.out.println(os);

                        System.out.println("Edite o novo nome da Ordem de Serviço:");
                        os.setNome(sc.nextLine());

                        System.out.println("Edite a nova descrição da Ordem de Serviço:");
                        os.setDescricao(sc.nextLine());

                        System.out.println("Edite a nova hora da solicitação (formato HHMM):");
                        os.setHoraSolicitacao(sc.nextInt());
                        sc.nextLine();

                        server.atualizarOrdemServico(os);
                        System.out.println("Ordem de Serviço alterada com sucesso!");
                    } else {
                        System.out.println("Ordem de Serviço não encontrada.");
                    }
                    break;

                case 4:
                    System.out.println("Digite o código da Ordem de Serviço para remover:");
                    int codRemove = sc.nextInt();
                    sc.nextLine(); 

                    OrdemServico osRemove = server.buscarOrdemServico(codRemove);
                    if (osRemove != null) {
                        server.removerOrdemServico(codRemove);
                    } else {
                        System.out.println("Ordem de Serviço não encontrada.");
                    }
                    break;

                default:
                    break;

            }
        }
    }

    public static Servidor gerarOrdensFicticias(Servidor server) {

        OrdemServico os = new OrdemServico(1, "Ajeitar projetor", "LoremLoremLorem", 2);
        OrdemServico os2 = new OrdemServico(2, "Consertar arcondicionado", "LoremLoremLorem", 14);
        OrdemServico os3 = new OrdemServico(3, "Porta quebrada", "LoremLoremLorem", 9);
        OrdemServico os4 = new OrdemServico(4, "Ajeitar projetor", "LoremLoremLorem", 2);
        OrdemServico os5 = new OrdemServico(5, "Goteira no teto", "LoremLoremLorem", 22);

        server.cadastrarOrdemServico(os);
        server.cadastrarOrdemServico(os2);
        server.cadastrarOrdemServico(os3);
        server.cadastrarOrdemServico(os4);
        server.cadastrarOrdemServico(os5);

        return server;

    }
}
