import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static LinkedList<HistoryEntry> history = new LinkedList<>();

    public static void main(String[] args) {

        // Apenas para a entrega do challenge, a chave da API será enviada junto do projeto.
        String key;
        try {
            key = getApiKey();
        } catch (FileNotFoundException err) {
            System.out.println(
                    """
                    Arquivo apikey.txt não encontrado.
                    
                    É necessária uma chave da https://exchangerate-api.com para utilizar esse programa.
                    Crie um arquivo com o nome de apikey.txt no diretório raíz do projeto e cole sua chave dentro do arquivo.
                    
                    Consulte o arquivo README para mais informações.
                    """
            );
            return;
        }

        System.out.print("""
                =======================================
                Bem-vindo(a) ao conversor de moedas!
                =======================================
                
                Escolha uma das opções:
                """);

        String message = "";

        boolean run = true;
        int option;

        while(run) {

            showOptions();
            System.out.println(message);
            message = "";

            System.out.print("> ");

            try {
                option = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException err) {
                message = "Opção inválida!";
                scanner.nextLine();
                continue;
            }

            Exchange.Currency base;
            Exchange.Currency target;

            switch (option) {
                case 0 -> {
                    run = false;
                    continue;
                }
                case 10 -> {
                    System.out.println("\n[Histórico, do mais recente para o mais antigo]");
                    for (HistoryEntry entry : history) {
                        System.out.println(entry);
                    }
                    System.out.println("\nFim do histórico. Pressione ENTER para voltar.");
                    scanner.nextLine();
                    continue;
                }
                case 1 -> {
                    base = Exchange.Currency.BRL;
                    target = Exchange.Currency.USD;
                }
                case 2 -> {
                    base = Exchange.Currency.USD;
                    target = Exchange.Currency.BRL;
                }
                case 3 -> {
                    base = Exchange.Currency.BRL;
                    target = Exchange.Currency.EUR;
                }
                case 4 -> {
                    base = Exchange.Currency.EUR;
                    target = Exchange.Currency.BRL;
                }
                case 5 -> {
                    base = Exchange.Currency.USD;
                    target = Exchange.Currency.EUR;
                }
                case 6 -> {
                    base = Exchange.Currency.EUR;
                    target = Exchange.Currency.USD;
                }
                default -> {
                    message = "Opção inválida!";
                    continue;
                }
            }

            double value = askValue();
            Exchange exchange = new Exchange(base, key);
            double result = exchange.convert(target, value);
            message = "\nO resultado da conversão é " + Exchange.prettyNumber(result) + " " + Exchange.getCurrencyNameString(target);
            history.addFirst(new HistoryEntry(base, target, value, result));
        }
    }

    public static String getApiKey() throws FileNotFoundException {
        FileReader file = new FileReader("apikey.txt");
        Scanner scanner = new Scanner(file);
        return scanner.nextLine().trim();
    }

    public static double askValue() {
        double value;

        while(true) {
            try {
                System.out.print("Digite um valor para converter: ");
                value = scanner.nextDouble();
                break;
            } catch (InputMismatchException err) {
                System.out.println("Valor inválido!");
                scanner.nextLine();
            }
        }
        return value;
    }

    public static void showOptions() {
        System.out.print("""
                1) Real Brasileiro -> Dólar Americano
                2) Dólar Americano -> Real Brasileiro
                3) Real Brasileiro -> Euro
                4) Euro -> Real Brasileiro
                5) Dólar Americano -> Euro
                6) Euro -> Dólar Americano
                
                0) Sair.
                10) Histórico.
                """);
    }
}