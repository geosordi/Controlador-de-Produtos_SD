/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicoWeb;

import java.util.Scanner;

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
        Scanner in = new Scanner(System.in);
        ConsumidorRestNode consumer = new ConsumidorRestNode();
        
        while(true){
            System.out.println("Digite 1 para Incluir\n"
                    + "Digite 2 para Alterar\n"
                    + "Digite 3 para Excluir\n"
                    + "Digite 4 para Consultar\n"
                    + "Digite 5 para Listar\n"
                    + "Digite 6 para Sair");
            String optionNumber = in.nextLine();
            if(optionNumber.equals("1")) {
                //Inclusão
                System.out.println("** Inclusao selecionada **\nDigite o código do produto: ");
                int codigo = Integer.parseInt(in.nextLine());
                System.out.println("Digite a descricao: ");
                String descricao = in.nextLine();
                System.out.println("Digite o preco: ");
                Float preco = Float.parseFloat(in.nextLine());
                System.out.println("Digite quantidade de estoque: ");
                int estoque = Integer.parseInt(in.nextLine());
                System.out.println(consumer.inclusao(codigo, descricao, preco, estoque));
            } else if(optionNumber.equals("2")){
                //Alteração
                System.out.println("** Alteração selecionada **\nDigite o código do produto: ");
                int codigo = Integer.parseInt(in.nextLine());
                System.out.println("Digite a descricao: ");
                String descricao = in.nextLine();
                System.out.println("Digite o preco: ");
                Float preco = Float.parseFloat(in.nextLine());
                System.out.println("Digite quantidade de estoque: ");
                int estoque = Integer.parseInt(in.nextLine());
                System.out.println(consumer.alteracao(codigo, descricao, preco, estoque));
            } else if(optionNumber.equals("3")){
                //Exclusão
                System.out.println("** Exclusão selecionada **\nDigite o código do produto: ");
                int codigo = Integer.parseInt(in.nextLine());
                System.out.println(consumer.exclusao(codigo));
            } else if(optionNumber.equals("4")){
                //Consultar
                System.out.println("** Consulta selecionada **\nDigite o código do produto: ");
                int codigo = Integer.parseInt(in.nextLine());
                System.out.println(consumer.consulta(codigo));
            } else if(optionNumber.equals("5")){
                //Listagem
                System.out.println("** Listagem selecionada **\n");
                System.out.println(consumer.listagem());
            } else if(optionNumber.equals("6")){
                System.out.println("** Sair selecionado **\n");
                System.exit(0);
            }
        }
    }   
}
