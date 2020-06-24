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
*/

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.ClientProperties;

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
        return target.request().get(String.class);
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
        String param = "{\"codigo\":" + codigo + "}";
        client = ClientBuilder.newClient();    
        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        target = client.target(baseUri + "/exclusao");
        try{
            String resposta = target
                    .request(MediaType.APPLICATION_JSON)
                    .build("DELETE", Entity.entity(param, MediaType.APPLICATION_JSON))
                    .invoke(String.class);

            return resposta;
        } catch (Exception e){
            return "Código não existe";
        }
    }
    
    public String alteracao(Integer codigo, String descricao, float preco, Integer estoque){
        String param = "{\"codigo\":" + codigo + ", \"descricao\":\"" + descricao + "\", \"preco\":" + preco + ", \"estoque\":" + estoque + "}";
        client = ClientBuilder.newClient();
        target = client.target(baseUri + "/alteracao");
        
        try{
        String resposta = target
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.entity(param, MediaType.APPLICATION_JSON), String.class);
        
        return resposta;
        } catch (Exception e){
            return "Código inexistente";
        }
    }
    
    public String inclusao(Integer codigo, String descricao, float preco, Integer estoque){
        String param = "{\"codigo\":" + codigo + ", \"descricao\":\"" + descricao + "\", \"preco\":" + preco + ", \"estoque\":" + estoque + "}";
        
        client = ClientBuilder.newClient();
        target = client.target(baseUri + "/inclusao");
        try{
            String resposta = target
                    .request(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(param, MediaType.APPLICATION_JSON), String.class);
            return resposta;
        } catch (Exception e){
            return "Código duplicado";
        }
    }
}
