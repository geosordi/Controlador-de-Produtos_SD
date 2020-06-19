/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicoWeb;

/**
 *
 * @author Geovani Sordi
 */
public class Consumidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ConsumidorRestNode consumer = new ConsumidorRestNode();
        System.out.println(consumer.inclusao(3, "HD", (float) 250.80, 7));
        //System.out.println(consumer.alteracao(3, "HD", (float) 252.75, 3));
        //System.out.println(consumer.exclusao(3));
        //System.out.println(consumer.consulta(2));
        //System.out.println(consumer.listagem());
    }
    
}
