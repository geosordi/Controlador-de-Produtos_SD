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
        * json-simple-1.1.1.jar
*/

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientProperties;
import org.json.simple.JSONObject;

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
        String[] s = str.split("},");
        for (String item : s) {
            if(! item.contains("}]"))
                System.out.println(item + "},\n");
            else
                System.out.println(item);
        }
        return "Listagem finalizada";
    }
    
    public String consulta(Integer codigo){
        client = ClientBuilder.newClient();
        target = client.target(baseUri + "/" + codigo);
        try{
            return target.request().get(String.class);
        }catch (Exception e){
            return "Código não existente";
        }
    }
    
    public String exclusao(Integer codigo){
        JSONObject param = writeJsonExcluir(codigo);
        String param2 = param.toJSONString();
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
        String param2 = param.toJSONString();
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
        String param2 = param.toJSONString();
       
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
    
    public static JSONObject writeJsonExcluir(Integer codigo){
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("codigo", codigo);
        
        return  jsonObject;
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
