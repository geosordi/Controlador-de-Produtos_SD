/*

    Serviço TCP
    Bruno de Almeida Zampirom

    Solução: Prevê duas operações: alteração na descrição do produto ou inclusão de um novo
    produto. Na alteração do nome, deve-se enviar o código e a nova descrição do produto. Na
    inclusão de novos produtos, deve-se enviar código, descrição, preço e estoque. Receber a
    resposta do servidor. O cliente deve poder enviar também um pedido de final de trabalho,
    encerrando a conexão com o servidor.

    Jars necessários:
        * org.json https://mvnrepository.com/artifact/org.json/json/20200518
        * mongo-java-driver-3.12.5.jar

*/
package TCP;

import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

/**
 *
 * @author bruno
 */    
public class Cliente extends Thread {
    private static Socket soc;
    private static ObjectOutputStream saida;
    private String txt;
    private int dorme;

    /**
    * @param args the command line arguments
    */

    public Cliente(String txt, int dorme) {
        this.txt = txt;
        this.dorme = dorme;
    }

    public void run() {
        System.out.println("Thread " + this.txt + " executando");
        try {
            soc = new Socket("localhost", 2006);
            saida = new ObjectOutputStream(soc.getOutputStream());
            Scanner in = new Scanner(System.in);

            System.out.println("Cliente conectado...");
            while (true) {
                System.out.println("Digite 1 para incluir um novo produto\nDigite 2 para alterar um produto\nDigite 3 para encerrar");
                String optionNumber = in.nextLine();
                if (optionNumber.equals("1")) {
                    // Leitura
                    System.out.println("** Inclusao selecionada **\nDigite o código do produto: ");
                    int productCode = Integer.parseInt(in.nextLine());
                    System.out.println("Digite a descricao: ");
                    String productDescription = in.nextLine();
                    System.out.println("Digite o preco: ");
                    Float productPrice = Float.parseFloat(in.nextLine());
                    System.out.println("Digite quantidade de estoque: ");
                    int productStock = Integer.parseInt(in.nextLine());
                    // Envio
                    JSONObject msg = writeJsonInsert(productCode, productDescription, productPrice, productStock);
                    System.out.println("Aguardando resposta do servidor...");
                    doSendAsync(soc, msg);
                    // Retorno
                    String retorno = doReciveAsync(soc);
                    System.out.println("Cliente recebeu: " + retorno);
                } else if (optionNumber.equals("2")) {
                    // Leitura
                    System.out.println("** Alteracao selecionada **\nDigite o código do produto: ");
                    int productCode = Integer.parseInt(in.nextLine());
                    System.out.println("Digite a nova descricao: ");
                    String productDescription = in.nextLine();
                    // Envio
                    JSONObject msg = writeJsonUpdate(productCode, productDescription);
                    System.out.println("Aguardando resposta do servidor...");
                    doSendAsync(soc, msg);
                    // Retorno
                    String retorno = doReciveAsync(soc);
                    System.out.println("Cliente recebeu: " + retorno);
                } else if (optionNumber.equals("3")) {
                    System.out.println("** Encerramento selecionada **");
                    // Envio
                    JSONObject msg = writeJsonClose();
                    System.out.println("Aguardando resposta do servidor...");
                    doSendAsync(soc, msg);
                    // Retorno
                    String retorno = doReciveAsync(soc);
                    System.out.println("Cliente recebeu: " + retorno);
                    // Encerramento
                    break;
                }
                System.out.println("-------------\n");
            }
            System.out.println("Conexão com localhost:2006 fechada");
            in.close();
            soc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean doSendAsync(Socket soc, JSONObject msg) {
        try {
            saida.writeObject(msg);
            saida.flush();
            System.out.println("Thread cliente enviou: " + msg);
            return true;
        } catch (IOException e) {
            System.out.println(" cliente falhou no envio, ERROR:" + e.getMessage());
            return false;
        }
    }

    private static String doReciveAsync(Socket soc) {
        try {
           ObjectInputStream retorno = new ObjectInputStream(soc.getInputStream());
            return (String) retorno.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "{ERRO NO RECEBIMENTO}";
    }

    private static JSONObject writeJsonUpdate(int codigo, String descricao) {
        JSONObject jsonObject = new JSONObject();

        //Armazena dados em um Objeto JSON
        jsonObject.put("tipoOperacao", "alteracao");
        jsonObject.put("codigo", codigo);
        jsonObject.put("descricao", descricao);

        return jsonObject;
    }

    private static JSONObject writeJsonInsert(int codigo, String descricao, float preco, int estoque) {
        JSONObject jsonObject = new JSONObject();
         
        //Armazena dados em um Objeto JSON
        jsonObject.put("tipoOperacao", "inclusao");
        jsonObject.put("codigo", codigo);
        jsonObject.put("descricao", descricao);
        jsonObject.put("preco", preco);
        jsonObject.put("estoque", estoque);

        return jsonObject;
    }

    private static JSONObject writeJsonClose() {
        JSONObject jsonObject = new JSONObject();
         
        //Armazena dados em um Objeto JSON
        jsonObject.put("tipoOperacao", "encerra");
        return jsonObject;
    }

    // Inicio do cliente
    public static void main(String[] args) throws Exception{

        Cliente cli1 = new Cliente("Cliente 1", 0);
        // Cliente cli2 = new Cliente("Cliente 2", 0);
        // Cliente cli3 = new Cliente("Cliente 3", 0);
        // Cliente cli4 = new Cliente("Cliente 4", 0);
        // Cliente cli5 = new Cliente("Cliente 5", 0);
        cli1.start();
        // cli2.start();
        // cli3.start();
        // cli4.start();
        // cli5.start();
    }
}