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

        try {
            soc = new DatagramSocket();
            endereco = InetAddress.getLocalHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("Iniciando cliente...\n");
        String operacao;
        String mensagemCliente;

        System.out.println("Digite 1 para alterar o preco / ou 2 para estoque.");
        String entrada = in.nextLine();
        System.out.println("Digite o codigo do produto a ser alterado: ");
        int codigo = Integer.parseInt(in.nextLine());

        if (entrada.equals("1")) {
            operacao = "preco";
            System.out.println("Digite o preco do produto a ser alterado: ");
            Float novo = Float.parseFloat(in.nextLine());
            mensagemCliente = operacao + ";" + codigo + ";" + novo;
            buffer = mensagemCliente.getBytes();
            pacote = new DatagramPacket(buffer, buffer.length, endereco, 2006);
        }
        else {
            operacao = "estoque";
            System.out.println("Digite o estoque do produto a ser alterado: ");
            int novo = Integer.parseInt(in.nextLine());
            mensagemCliente = operacao + ";" + codigo + ";" + novo;
            buffer = mensagemCliente.getBytes();
            pacote = new DatagramPacket(buffer, buffer.length, endereco, 2006);
        }
        try {
            // Enviando pacote
            soc.send(pacote);
            // Resposta recebida
            soc.receive(pacote);
            String strResponseServer = new String(pacote.getData(), 0, pacote.getLength());
            System.out.println(strResponseServer);
            switch(strResponseServer){
                case "0":
                    System.out.println("Produto alterado com sucesso!");
                    break;
                case "1":
                    System.out.println("Ocorreu um erro no servidor.");
                    break;
                case "2":
                    System.out.println("Codigo de produto não encontrado.");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("----------Cliente Finalizado----------\n");

    }
}
