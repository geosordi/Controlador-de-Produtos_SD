/**
    Serviço UDP
    Bruno de Almeida Zampirom

    Solução: Deve aguardar solicitações de clientes e efetuar a realização da ação solicitada:
    alterar preço ou alterar estoque de um produto. Enviar uma resposta ao cliente, informando
    se ação foi bem sucedida ou se não pode ser realizada.

    Jars necessários:
    * mongo-java-driver-3.12.5.jar
*/

package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

/**
 *
 * @author bruno
 */
public class Servidor {
    static MongoCollection<Document> produtos;

    public static void main(String[] args) throws Exception {

        int porta = 2006;
        byte vetByte[] = new byte[100];
        DatagramSocket soc;
        DatagramPacket pacote;

        soc = new DatagramSocket(porta);
        pacote = new DatagramPacket(vetByte, vetByte.length);
        System.out.println("Receptor escutando a porta: " + porta);

        System.out.println("Conectando-se ao banco de dados...");
        ConnectionString connString = new ConnectionString(
                "mongodb+srv://brunozampirom:CheykSRTYY2nw44L@cluster0-wn3ai.mongodb.net/produtos?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString).retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("produtos");

        produtos = database.getCollection("produtos");

        while(true) {
            soc.receive(pacote);
            String str = new String(pacote.getData(), pacote.getOffset(), pacote.getLength()); // verifica comprimento do DatagramPacket

            String operacao = str.split(";")[0];
            int codigo = Integer.parseInt(str.split(";")[1]);

            String retornoMensagem;
            if (operacao.equals("preco")) {
                float novo = Float.parseFloat(str.split(";")[2]);
                retornoMensagem = mongoUpdate(codigo, novo);
            }
            else {
                int novo = Integer.parseInt(str.split(";")[2]);
                retornoMensagem = mongoUpdate(codigo, novo);
            }
            System.out.println(retornoMensagem);
            System.out.println("--------------------\n");
        }
    }

    private static String mongoUpdate(int codigo, float novo) {
        if(verificaProdutoExistente(codigo)) {
            Document produtoAlterado = new Document("preco", novo);
            System.out.println(produtoAlterado);
            Boolean resultadoAltera = alteraProduto(produtoAlterado, codigo);
            return resultadoAltera ? "Produto alterado com sucesso!" : "Ocorreu um erro ao alterar o produto";
        }
        return "Codigo de produto não existente";
    }

    private static String mongoUpdate(int codigo, int novo) {
        if(verificaProdutoExistente(codigo)) {
            Document produtoAlterado = new Document("preco", novo);
            System.out.println(produtoAlterado);
            Boolean resultadoAltera = alteraProduto(produtoAlterado, codigo);
            return resultadoAltera ? "Produto alterado com sucesso!" : "Ocorreu um erro ao alterar o produto";
        }
        return "Codigo de produto não existente";
    }

    private static Boolean verificaProdutoExistente(int codigo) {
        Document produtoDocument = new Document("_id", codigo);
        return produtos.countDocuments(produtoDocument) > 0 ? true : false;
    }

    private static Boolean alteraProduto(Document produto, int codigo) {
        try {
            produtos.updateOne(Filters.eq("_id", codigo), new Document("$set", produto));
            return true;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}