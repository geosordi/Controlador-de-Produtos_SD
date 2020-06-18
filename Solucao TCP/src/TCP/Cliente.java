/*

    Serviço TCP
    Bruno de Almeida Zampirom

    Solução: Prevê duas operações: alteração na descrição do produto ou inclusão de um novo
    produto. Na alteração do nome, deve-se enviar o código e a nova descrição do produto. Na
    inclusão de novos produtos, deve-se enviar código, descrição, preço e estoque. Receber a
    resposta do servidor. O cliente deve poder enviar também um pedido de final de trabalho,
    encerrando a conexão com o servidor.

    Jars necessários:
        * json-simple-1.1.1.jar
        * mongo-java-driver-3.12.5.jar

*/
package TCP;

import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.Socket;
 
import org.json.simple.JSONObject;

/**
 *
 * @author bruno
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        Socket soc;
        Scanner in = new Scanner(System.in);
        while(true) {
            soc = new Socket("localhost", 2006);
            System.out.println("Cliente conectado...");
            System.out.println("Digite 1 para incluir um novo produto\nDigite 2 para alterar um produto\nDigite 3 para encerrar");
            String optionNumber = in.nextLine();
            if(optionNumber.equals("1")) {
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
                doSendAsync(soc, msg);
                System.out.println("Aguardando resposta do servidor...");
                // Retorno
                String retorno = doReciveAsync(soc);
                System.out.println("Cliente recebeu: " + retorno);
                soc.close();
            }
            else if(optionNumber.equals("2")) {
                // Leitura
                System.out.println("** Alteracao selecionada **\nDigite o código do produto: ");
                int productCode = Integer.parseInt(in.nextLine());
                System.out.println("Digite a nova descricao: ");
                String productDescription = in.nextLine();
                // Envio
                JSONObject msg = writeJsonUpdate(productCode, productDescription);
                doSendAsync(soc, msg);
                System.out.println("Aguardando resposta do servidor...");
                // Retorno
                String retorno = doReciveAsync(soc);
                System.out.println("Cliente recebeu: " + retorno);
                soc.close();
            }
            else if(optionNumber.equals("3")) {
                System.out.println("** Encerramento selecionada **");
                // Envio
                JSONObject msg = writeJsonClose();
                doSendAsync(soc, msg);
                System.out.println("Aguardando resposta do servidor...");
                // Retorno
                String retorno = doReciveAsync(soc);
                System.out.println("Cliente recebeu: " + retorno);
                soc.close();
                // Encerramento
                break;
            }
            System.out.println("-------------\n");
        }
        in.close();
        soc.close();
        System.out.println("Conexão com localhost:2006 fechada");
    }

    private static boolean doSendAsync(Socket soc, JSONObject msg) {
        try {
            ObjectOutputStream saida = new ObjectOutputStream(soc.getOutputStream());
            saida.writeObject(msg);
            System.out.println(" thread cliente enviou: " + msg);
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
}
