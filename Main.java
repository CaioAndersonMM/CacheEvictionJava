import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Servidor server = new Servidor();

        gerarOrdensFicticias(server);

        boolean option = true;

        while (option) {
            try {
                System.out.println("| 1 | - Cadastrar");
                System.out.println("| 2 | - Listar");
                System.out.println("| 3 |- Alterar");
                System.out.println("| 4 |- Remover");
                System.out.println("| 5 | - Acessar Quantidade de Registros");
                System.out.println("| 6 | - Buscar Ordem de Serviço");
                System.out.println("| 7 | - Ver Cache");
                System.out.println("| 8 | - Ver Arvore (Níveis)");
                System.out.println("| 0 | - Sair");

                int escolha = sc.nextInt();

                switch (escolha) {
                    case 1:
                        System.out.println("Digite o código da Ordem de Serviço:");
                        int codInserir = sc.nextInt();
                        sc.nextLine();

                        OrdemServico osInserir = server.buscarOrdemServico(codInserir);

                        if (osInserir != null) { // OS já existe
                            System.out.println("Já existe essa Ordem de Serviço");
                            break;
                        }

                        System.out.println("Digite o nome da Ordem de Serviço:");
                        String nome = sc.nextLine();

                        System.out.println("Digite a descrição da Ordem de Serviço:");
                        String descricao = sc.nextLine();

                        System.out.println("Digite a hora da solicitação (formato HHMM):");
                        int hora = sc.nextInt();
                        sc.nextLine();

                        OrdemServico novaOS = new OrdemServico(codInserir, nome, descricao, hora);
                        server.cadastrarOrdemServico(novaOS);
                        System.out.println("Ordem de Serviço cadastrada: ");
                        System.out.println(novaOS);
                        break;
                    case 2:
                        server.mostrarOrdensServico();
                        break;
                    case 3:
                        System.out.println("Digite o código da Ordem de Serviço para alterar:");
                        int codEdit = sc.nextInt();
                        sc.nextLine();

                        OrdemServico osEdit = server.buscarOrdemServico(codEdit);
                        if (osEdit != null) {
                            System.out.println(osEdit);

                            System.out.println("Edite o novo nome da Ordem de Serviço:");
                            osEdit.setNome(sc.nextLine());

                            System.out.println("Edite a nova descrição da Ordem de Serviço:");
                            osEdit.setDescricao(sc.nextLine());

                            System.out.println("Edite a nova hora da solicitação (formato HHMM):");
                            osEdit.setHoraSolicitacao(sc.nextInt());
                            sc.nextLine();

                            server.atualizarOrdemServico(osEdit);
                            System.out.println("Ordem de Serviço alterada com sucesso!");
                        } else {
                            System.out.println("Ordem de Serviço não encontrada!");
                        }
                        break;

                    case 4:
                        System.out.println("Digite o código da Ordem de Serviço para remover:");
                        int codRemove = sc.nextInt();
                        sc.nextLine();

                        OrdemServico osRemove = server.buscarOrdemServico(codRemove);
                        if (osRemove != null) {
                            server.removerOrdemServico(osRemove);
                        } else {
                            System.out.println("Ordem de Serviço não encontrada.");
                        }
                        break;

                    case 5:
                        int qtdRegistros = server.quantidadeDeRegistros();
                        System.out.println(qtdRegistros);
                        break;

                    case 6:
                        System.out.println("Qual Ordem de Serviço você quer buscar? ");
                        int codBuscar = sc.nextInt();
                        OrdemServico osBuscada = server.buscarOrdemServico(codBuscar);
                        if (osBuscada != null) {
                            System.out.println(osBuscada);
                        } else {
                            System.out.println("Ordem de Serviço não encontrada.");
                        }

                        break;

                    case 7:
                        server.mostrarCache();
                        break;

                    case 8:
                        server.mostrarArvore();
                        break;
                    case 0:
                        option = false;
                    default:
                        break;
                }

            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                sc.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Erro ao ler entrada");
                break;
            }
        }

        sc.close();

    }

    public static Servidor gerarOrdensFicticias(Servidor server) {

        for (int i = 1; i <= 60; i++) {
            OrdemServico os = new OrdemServico(i, "Serviço " + i, "Descrição do serviço " + i, (i * 2) % 24);
            server.cadastrarOrdemServico(os);
        }

        return server;

    }
}
