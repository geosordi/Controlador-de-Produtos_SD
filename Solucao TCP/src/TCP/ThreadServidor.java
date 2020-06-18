package TCP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.json.simple.JSONObject;

/**
 *
 * @author bruno
 */
public class ThreadServidor extends Thread {
    Socket soc;
    static int acumTotal = 0;
    static int acumSuccess = 0;
    static int acumFail = 0;
    static Boolean encerra = false;
    static MongoCollection<Document> produtos;

    public ThreadServidor(String label, Socket soc) {
        this.setName(label);
        this.soc = soc;
    }


    public void run() {
        ObjectInputStream entrada;
        JSONObject  jsonObject;
        String operacao;
        try {
            acumTotal++;
            entrada = new ObjectInputStream(soc.getInputStream());
            jsonObject = (JSONObject) entrada.readObject();
            operacao = (String) jsonObject.get("tipoOperacao");
            System.out.println(this.getName() + " operacação: " + operacao);
            if(operacao.equals("alteracao")) {
                String retornoMensagem = mongoUpdate(jsonObject);
                doSendAsync(soc, retornoMensagem);
            }
            else if(operacao.equals("inclusao")) {
                String retornoMensagem = mongoInsert(jsonObject);
                doSendAsync(soc, retornoMensagem);
            }
            else if(operacao.equals("encerra")) {
                doSendAsync(soc, "Servidor Encerrou!");
                encerra = true;
            }
            this.soc.close();
        } catch (IOException | ClassNotFoundException e) {
            doSendAsync(soc, "Erro no servico: "+ e.getMessage());
            e.printStackTrace();
        }
        System.out.println(this.getName() + " encerrou.");
    }

    private static boolean doSendAsync(Socket soc, String msg) {
        try {
            ObjectOutputStream saida = new ObjectOutputStream(soc.getOutputStream());
            saida.writeObject(msg);
            saida.close();
            acumSuccess++;
            return true;
        } catch (IOException e) {
            acumFail++;
            e.printStackTrace();;
        }
        return false;
    }

    private static String mongoUpdate(JSONObject produto) {
        int codigo = (int) produto.get("codigo");
        if(verificaProdutoExistente(codigo)) {
            Document produtoAlterado = new Document("descricao", (String) produto.get("descricao"));
            System.out.println("produtoAlterado");
            System.out.println(produtoAlterado);
            Boolean resultadoAltera = alteraProduto(produtoAlterado, codigo);
            return resultadoAltera ? "Produto alterado com sucesso!" : "Ocorreu um erro ao alterar o produto";
        }
        return "Codigo de produto não existente";
    }

    private static String mongoInsert(JSONObject produto) {
        int codigo = (int) produto.get("codigo");
        if(!verificaProdutoExistente(codigo)) {
            Document produtoInclusao = new Document("_id", (int) produto.get("codigo"))
                .append("descricao", (String) produto.get("descricao"))
                .append("preco", (Float) produto.get("preco"))
                .append("estoque", (int) produto.get("estoque"));
            System.out.println("produtoInclusao");
            System.out.println(produtoInclusao);
            Boolean resultadoInsere = insereProduto(produtoInclusao);
            return resultadoInsere ? "Produto inserido com sucesso!" : "Ocorreu um erro ao inserido o produto";
        }
        return "Codigo de produto ja existente";
    }

    private static Boolean verificaProdutoExistente(int codigo) {
        Document produtoDocument = new Document("_id", codigo);
        return produtos.countDocuments(produtoDocument) > 0 ? true : false;
    }

    private static Document retornaProduto(int codigo) {
        Document produtoDocument = new Document("_id", codigo);
        return produtos.find(produtoDocument).first();
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

    private static Boolean insereProduto(Document produto) {
        try {
            produtos.insertOne(produto);
            return true;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
