/**
    Cliente UDP
    Bruno de Almeida Zampirom

    Solução: Contém duas ações permitidas: alteração no preço de um produto e alteração no
    estoque de um produto. Deve ser enviado o tipo da alteração, o código do produto e o novo
    preço ou estoque a ser atualizado. Após deve-se receber a resposta do servidor

    Jars necessários:
    * mongo-java-driver-3.12.5.jar
*/

package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author bruno
 */
public class Cliente {
    static DatagramSocket soc;
    static InetAddress endereco;

    public static void main(String[] args) throws Exception {
        DatagramPacket pacote;
        byte buffer[] = new byte[100];
        Scanner in = new Scanner(System.in);

        Random rnd = new Random();
        try {
            soc = new DatagramSocket();
            endereco = InetAddress.getLocalHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Iniciando cliente...\n");
        while (true) {  
            String operacao;

            System.out.println("Digite 1 para alterar o preco / ou 2 para estoque.");
            String entrada = in.nextLine();
            System.out.println("Digite o codigo do produto a ser alterado: ");
            int codigo = Integer.parseInt(in.nextLine());

            if (entrada.equals("1")) {
                operacao = "preco";
                System.out.println("Digite o preco do produto a ser alterado: ");
                Float novo = Float.parseFloat(in.nextLine());
                String mensagemCliente = operacao + ";" + codigo + ";" + novo;
                buffer = mensagemCliente.getBytes();
                pacote = new DatagramPacket(buffer, buffer.length, endereco, 2006);
            }
            else {
                operacao = "estoque";
                System.out.println("Digite o estoque do produto a ser alterado: ");
                int novo = Integer.parseInt(in.nextLine());
                String mensagemCliente = operacao + ";" + codigo + ";" + novo;
                buffer = mensagemCliente.getBytes();
                pacote = new DatagramPacket(buffer, buffer.length, endereco, 2006);
            }

            try {
                soc.send(pacote);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("--------------------\n");
        }
    }
}
