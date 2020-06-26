# Controlador de Produtos
Trabalho de implementação de Aplicações Distribuídas

## Motivação
A motivação do trabalho é em entender sobre as tecnologias de comunicação utilizadas em sistemas ditribuídos.

## Definição
O trabalho consiste na implementação de 3 aplicaçações para interagir com uma base de dados, uma utilizando o protocolo UDP, outra implementando o protocolo TCP e a outra utilizando a comunicação pelo modelo REST.

## UDP
A implementação de comunicação através do protocolo UDP consiste em duas aplicações em Java, sendo elas: um cliente e um servidor.

#### Cliente
Contém duas ações permitidas: alteração no preço de um produto e alteração no estoque de um produto. Deve ser enviado o tipo da alteração, o código do produto e o novo preço ou estoque a ser atualizado. Após deve-se receber a resposta do servidor.

#### Servidor
Deve aguardar solicitações de clientes e efetuar a realização da ação solicitada: alterar preço ou alterar estoque de um produto. Enviar uma resposta ao cliente, informando se ação foi bem sucedida ou se não pode ser realizada.

## TCP
A implementação de comunicação através do protocolo TCP também consiste em aplicações em Java, sendo elas: um cliente e um servidor.

#### Cliente
Prevê duas operações: alteração na descrição do produto ou inclusão de um novo produto. Na alteração do nome, deve-se enviar o código e a nova descrição do produto. Na inclusão de novos produtos, deve-se enviar código, descrição, preço e estoque. Receber a resposta do servidor. O cliente deve poder enviar também um pedido de final de trabalho, encerrando a conexão com o servidor.

#### Servidor
Deve aguardar solicitações de clientes e efetuar a realização da ação solicitada: alterar preço ou alterar estoque de um produto. Enviar uma resposta ao cliente, informando se ação foi bem sucedida ou se não pode ser realizada.

## Serviço REST
Para a parte de implementação da comunicação através de serviços REST, foi optado pelo desenvolvimento do serviço em Node.js e a aplicação consumidor em Java.

#### Serviço
Disponibilizar métodos para inclusão, alteração, exclusão, consulta ou listagem de produtos.

#### Consumidor
Implementar métodos para acessar as funcionalidades do serviço.

## Banco de dados
Como tecnologia utilizada no banco de dados foi optado por utilizar o Mongo pela produtividade que o mesmo oferece. A base de dados está hospedado na nuvem, não tendo necessidade de confira-lo localmente.

Para visualizar os dados que estão sendo armazenados pode ser baixado o MongoDB Compass e acessar com: mongodb+srv://brunozampirom:CheykSRTYY2nw44L@cluster0-wn3ai.mongodb.net/Cluster0?authSource=admin&replicaSet=Cluster0-shard-0&readPreference=primary&appname=MongoDB%20Compass&ssl=true

OBS: Mongo arredonda os números em ponto flutante.

## Jars necessários
- [json](https://mvnrepository.com/artifact/org.json/json/20200518) versão 20200518
- [mongo](https://jar-download.com/artifacts/org.mongodb/mongo-java-driver) versão 3.12.5
- [JAX-RS](https://download.oracle.com/otndocs/jcp/jaxrs-2_0-fr-spec/index.html) versão 2.0
- [Jersey](https://jar-download.com/artifacts/org.glassfish.jersey.core/jersey-client/2.5.1/source-code) versão 2.5.1

## Rodando serviço REST
```
npm i
npm run dev
```

## Desenvolvedores
|name|email|course|
| -------- | -------- | -------- |
|Bruno Zampirom|158788@upf.br|Ciência da Computação - UPF|
|Geovani Sordi|141974@upf.br|Ciência da Computação - UPF|
