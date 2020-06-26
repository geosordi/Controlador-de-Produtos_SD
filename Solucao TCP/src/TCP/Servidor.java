/*

    Serviço TCP
    Bruno de Almeida Zampirom

    Solução: Deve aguardar solicitações de conexão e a cada pedido de conexão de cliente,
    criar uma thread específica para atender as solicitações dos clientes. Ao receber a operação,
    realizar a ação solicitada e devolver uma resposta ao cliente se a ação foi bem sucedida ou
    não. Ao receber a mensagem de final de trabalho, encerrar a conexão e a thread.

    Jars necessários:
        * org.json https://mvnrepository.com/artifact/org.json/json/20200518
        * mongo-java-driver-3.12.5.jar

*/
package TCP;

import java.net.ServerSocket;
import java.net.Socket;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import com.mongodb.MongoClientSettings;

/**
 *
 * @author bruno
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        ServerSocket server;
        int cont=0;
        System.out.println("Iniciando servidor...");
        server = new ServerSocket(2006);

        System.out.println("Conectando-se ao banco de dados...");
        ConnectionString connString = new ConnectionString(
            "mongodb+srv://brunozampirom:CheykSRTYY2nw44L@cluster0-wn3ai.mongodb.net/produtos?retryWrites=true&w=majority"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connString)
            .retryWrites(true)
            .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("produtos");
        MongoCollection<Document> produtos = database.getCollection("produtos");
        ThreadServidor.produtos = produtos;

        System.out.println("Aguardando conexões");
        while(true) {
            cont++;
            Socket soc = server.accept();
            System.out.println("Conexao estabelecida");
            ThreadServidor thread = new ThreadServidor("Thread_Servidor_" + cont, soc);
            thread.start();
        }
    }
}
