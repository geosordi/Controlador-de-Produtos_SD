/*
    Serviço REST
    Bruno de Almeida Zampirom

    Solução: Disponibiliza métodos para inclusão, alteração, exclusão, consulta ou listagem de
    produtos.

    Rodar servidor:
        npm i
        npm run dev

*/
const express = require('express');
const app = express();
const MongoClient = require('mongodb').MongoClient;
const bodyParser = require('body-parser');

const uri = "mongodb+srv://brunozampirom:CheykSRTYY2nw44L@cluster0-wn3ai.mongodb.net/produtos?retryWrites=true&w=majority";

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(bodyParser.raw());

MongoClient.connect(uri, (err, client) => {
    if (err) return console.log(err);
    db = client.db('produtos');

    app.listen(3000, function() {
        console.log('server running on port 3000');
        console.log('Inclusão: POST http://localhost:3000/inclusao');
        console.log('Alteração: PUT http://localhost:3000/alteracao');
        console.log('Exclusão: DELETE http://localhost:3000/exclusao');
        console.log('Consulta: GET http://localhost:3000/:id');
        console.log('Listagem: GET http://localhost:3000/');
    });
})

app.get('/', (req, res) => { // Lista todos os produtos
    db.collection("produtos").find().toArray()
        .then(collection => {
            console.log(collection);
            res.send(collection); 
        }).catch(err => {
            res.status(500).send(err);
        })
})

app.get('/:id', (req, res) => { // Consulta um produto
    const _id = parseInt(req.params.id, 10);
    db.collection("produtos").findOne({ _id })
        .then(product => {
            if(product) {
                console.log(product);
                res.send(product); 
            } else {
                res.status(409).send("Error: Nem um produto existente com o codigo informado");
            }
        }).catch(err => {
            res.status(500).send("Error: " + err);
        })
})

app.delete('/exclusao', (req, res) => { // Exclui um produto
    const body = req.body;
    if(!body || !body.codigo || !Number.isInteger(body.codigo)) res.status(400).send('Error: O body está incorreto!');

    db.collection('produtos').findOneAndDelete({_id: body.codigo})
      .then(deletedDocument => {
        if(deletedDocument.value) {
            res.send(`Success: Produto deletado com sucesso!`);
        } else {
            res.status(409).send("Error: Nem um produto existente com o codigo informado");
        }
    })
    .catch(err => res.status(500).send(`Error: Falha ao deletar ou remover o produto: ${err}`));
})

app.post('/inclusao', (req, res) => { // Inclui um novo produto
    const body = req.body;
    if(!body) res.status(400).send('Error: Os valores de inserção devem ser passados no body!');
    else {
        if(!body.codigo || !body.descricao || !body.preco || !body.estoque) res.status(400).send('Error: Nem todos valores foram informados corretamente!');
        else {
            if(Number.isInteger(body.codigo) && typeof body.descricao === 'string' && typeof body.preco === 'number' && Number.isInteger(body.estoque)) {
                console.log(body);
                db.collection('produtos').insertOne( { _id: body.codigo, "descricao": body.descricao, "preco": body.preco, "estoque": body.estoque })
                    .then(result => res.send("Success: Valor inserido com sucesso!"))
                    .catch(err => {
                        console.log(err);
                        err.code === 11000 ? res.status(409).send("Error: O codigo não pode ser duplicado") : res.status(400).send(err);
                    });
            }
            else res.status(400).send('Error: Os valores informados não estão no tipo correto!');
        }
    }
})

app.put('/alteracao', (req, res) => { // Atualiza um recurso ou inlcui caso não exista o id
    const body = req.body;
    if(!body) res.status(400).send('Error: Os valores de inserção devem ser passados no body!');
    else {
        if(!body.codigo || !body.descricao || !body.preco || !body.estoque) res.status(400).send('Error: Nem todos valores foram informados corretamente!');
        else {
            if(Number.isInteger(body.codigo) && typeof body.descricao === 'string' && typeof body.preco === 'number' && Number.isInteger(body.estoque)) {
                console.log(body);
                db.collection('produtos').save( { _id: body.codigo, "descricao": body.descricao, "preco": body.preco, "estoque": body.estoque })
                    .then(result => res.send("Valor atualizado com sucesso!"))
                    .catch(err => {
                        err.code === 11000 ? res.status(409).send("O codigo já foi inserido!") : res.status(400).send(err);
                    });
            }
            else res.status(400).send('Error: Os valores informados não estão no tipo correto!');
        }
    }
})