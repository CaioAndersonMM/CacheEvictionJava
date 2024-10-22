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
                System.out.println("| 8 | - Ver Mod e Fator de Carga");
                System.out.println("| 9 | - Buscar ocorrências no log");
                System.out.println("| 0 | - Sair");

                int escolha = sc.nextInt();

                switch (escolha) {
                    case 1:
                        System.out.println("Digite o código da Ordem de Serviço:");
                        int codInserir = sc.nextInt();
                        sc.nextLine();

                        OrdemServico osInserir = server.buscarOrdemServico(codInserir, false);

                        if (osInserir != null) { // OS já existe
                            System.out.println("Já existe essa Ordem de Serviço");
                            break;
                        }

                        System.out.println("Digite o nome da Ordem de Serviço:");
                        String nome = sc.nextLine();

                        System.out.println("Digite a descrição da Ordem de Serviço:");
                        String descricao = sc.nextLine();

                        System.out.println("Digite a hora da solicitação (formato HHMM):");
                        String hora = sc.nextLine();

                        // Compressão acontece no construtor
                        Mensagem mensagem = new Mensagem(codInserir, "Cadastrar", nome, descricao, hora);
                        System.out.println("Mensagem enviada ao servidor.");
                        server.processarMensagem(mensagem);
                        break;
                    case 2:
                        Mensagem mensagemListar = new Mensagem(0, "Listar", "", "", "");
                        server.processarMensagem(mensagemListar);
                        break;
                    case 3:
                        System.out.println("Digite o código da Ordem de Serviço para alterar:");
                        int codEdit = sc.nextInt();
                        sc.nextLine();

                        OrdemServico osEdit = server.buscarOrdemServico(codEdit, false);
                        if (osEdit != null) {
                            System.out.println(osEdit);

                            System.out.println("Edite o novo nome da Ordem de Serviço:");
                            String nomeEdit = sc.nextLine();

                            System.out.println("Edite a nova descrição da Ordem de Serviço:");
                            String descricaoEdit = sc.nextLine();

                            System.out.println("Edite a nova hora da solicitação (formato HHMM):");
                            String horaEdit = sc.nextLine();

                            Mensagem mensagemAlterar = new Mensagem(codEdit, "Alterar", nomeEdit, descricaoEdit, horaEdit);
                            System.out.println("Mensagem enviada ao servidor.");
                            server.processarMensagem(mensagemAlterar);
                        } else {
                            System.out.println("Ordem de Serviço não encontrada!");
                        }
                        break;

                    case 4:
                        System.out.println("Digite o código da Ordem de Serviço para remover:");
                        int codRemove = sc.nextInt();
                        sc.nextLine();

                        Mensagem mensagemRemover = new Mensagem(codRemove, "Remover", "", "", "");

                        System.out.println("Mensagem enviada ao servidor.");
                        server.processarMensagem(mensagemRemover);

                        break;

                    case 5:
                        Mensagem mensagemQuantidade = new Mensagem(0, "Quantidade", "", "", "");
                        System.out.println("Mensagem enviada ao servidor.");
                        server.processarMensagem(mensagemQuantidade);
                        break;

                    case 6:
                        System.out.println("Qual Ordem de Serviço você quer buscar? ");
                        int codBuscar = sc.nextInt();
                        Mensagem mensagemBuscar = new Mensagem(codBuscar, "Buscar", "", "", "");
                        System.out.println("Mensagem enviada ao servidor.");
                        server.processarMensagem(mensagemBuscar);
                        break;

                    case 7:
                        Mensagem mensagemCache = new Mensagem(0, "Cache", "", "", "");
                        System.out.println("Mensagem enviada ao servidor.");
                        server.processarMensagem(mensagemCache);
                        break;

                    case 8:
                        Mensagem mensagemMod = new Mensagem(0, "Mod", "", "", "");
                        System.out.println("Mensagem enviada ao servidor.");
                        server.processarMensagem(mensagemMod);
                        break;
                    case 9:
                        System.out.println("Você quer buscar por 1-Inserções, 2-Remoções, 3-Alterações ou 4-Buscas? ");
                        int escolhaLog = sc.nextInt();
                        
                        Mensagem mensagemLog = new Mensagem(escolhaLog, "VerLog", "", "", "");
                        System.out.println("Mensagem enviada ao servidor.");
                        server.processarMensagem(mensagemLog);
                        
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

        for (int i = 0; i <= 99; i++) {
            // OrdemServico os = new OrdemServico(i, "Serviço " + i, "Descrição do serviço "
            // + i, (i * 2) % 24);
            // server.cadastrarOrdemServico(os);

            Mensagem mensagem = new Mensagem(i, "Cadastrar", "Serviço " + i, "Descrição da " + i, "" + i + ":" + i);
            server.processarMensagem(mensagem);
            System.out.println("Mensagem enviada ao servidor.");
        }
        return server;
    }
}
