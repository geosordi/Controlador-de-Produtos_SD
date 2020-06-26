/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicoWeb;

/*
    Jars necessários:
        * JAX-RS 2.0
        * Jersey 2.5.1
        * org.json https://mvnrepository.com/artifact/org.json/json/20200518
*/

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author Geovani Sordi
 */

public class ConsumidorRestNode {
    private String baseUri = "http://localhost:3000";
    private Client client = null;
    private WebTarget target = null;
    
    public String listagem(){
        client = ClientBuilder.newClient();
        target = client.target(baseUri + "/");
        //return target.request().get(String.class);
        String str = target.request().get(String.class);
        try {
            JSONArray jsonArray = new JSONArray(str);
            System.out.println("Foram encontrados " + jsonArray.length() + " produtos: \n");
            for (int i=0; i < jsonArray.length(); i++) {
                System.out.println("codigo: " + jsonArray.getJSONObject(i).get("_id"));
                System.out.println("descricao: " + jsonArray.getJSONObject(i).get("descricao"));
                System.out.println("preco: R$ "+ String.format("%.2f", Float.parseFloat(jsonArray.getJSONObject(i).get("preco").toString())));
                System.out.println("estoque: " + jsonArray.getJSONObject(i).get("estoque"));
                System.out.println("------------");
            }
        }catch (Exception err){
            return "Ocorreu um erro";
        }
        return "Listagem finalizada";
    }
    
    public String consulta(Integer codigo){
        client = ClientBuilder.newClient();
        target = client.target(baseUri + "/" + codigo);
        try{
            String str = target.request().get(String.class);
            try {
                JSONObject jsonObject = new JSONObject(str);
                System.out.println("codigo: " + jsonObject.get("_id"));
                System.out.println("descricao: " + jsonObject.get("descricao"));
                System.out.println("preco: R$ "+ String.format("%.2f", Float.parseFloat(jsonObject.get("preco").toString())));
                System.out.println("estoque: " + jsonObject.get("estoque"));
                System.out.println();
            }catch (Exception err){
                return "Ocorreu um erro";
            }
        }catch (Exception e){
            return "Código não existente";
        }
        return "Consulta finalizada";
    }
    
    public String exclusao(Integer codigo){
        JSONObject param = writeJsonExcluir(codigo);
        String param2 = param.toString();
        client = ClientBuilder.newClient();    
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        target = client.target(baseUri + "/exclusao/" + codigo);
        try{
            return target.request().delete(String.class);
        } catch (Exception e){
            return "Código não existe";
        }
    }
    
    public String alteracao(Integer codigo, String descricao, float preco, Integer estoque){
        JSONObject param = writeJsonAlteracao(codigo, descricao, preco, estoque);
        String param2 = param.toString();
        client = ClientBuilder.newClient();
        target = client.target(baseUri + "/alteracao");
        
        try{
        String resposta = target
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.entity(param2, MediaType.APPLICATION_JSON), String.class);
        
        return resposta;
        } catch (Exception e){
            return "Código inexistente";
        }
    }
    
    public String inclusao(Integer codigo, String descricao, float preco, Integer estoque){
        JSONObject param = writeJsonInclusao(codigo, descricao, preco, estoque);
        String param2 = param.toString();
       
        client = ClientBuilder.newClient();
        target = client.target(baseUri + "/inclusao");
        try{
            String resposta = target
                    .request(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(param2, MediaType.APPLICATION_JSON), String.class);
            return resposta;
        } catch (Exception e){
            return "Código duplicado";
        }
    }
    
    public static JSONObject writeJsonAlteracao(Integer codigo, String descricao, float preco, Integer estoque){
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("codigo", codigo);
        jsonObject.put("descricao", descricao);
        jsonObject.put("preco", preco);
        jsonObject.put("estoque", estoque);
        
        return jsonObject;
    }
    
    public static JSONObject writeJsonInclusao(Integer codigo, String descricao, float preco, Integer estoque){
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("codigo", codigo);
        jsonObject.put("descricao", descricao);
        jsonObject.put("preco", preco);
        jsonObject.put("estoque", estoque);
        
        return jsonObject;
    }    
}
