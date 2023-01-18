## hub-correios-api
API de fácil instalação para buscar qualquer cep Brasileiro :)<br>

## Conceito
Basicamente, ao subir o container, ela baixa esse [csv](https://github.com/miltonhit/miltonhit/raw/main/public-assets/cep-20190602.csv) com 900k de endereços e salva na tabela 'correio.address' dentro do MySQL.<br>
Depois do setup é possível pesquisar de forma fácil, via API REST, qualquer cep Brasileiro.

## Stack
-> Java 11+<br>
-> MySQL<br>
-> Spring Family<br>
-> Docker Compose<br>

## Por que essa stack?
Utilizei essa Stack apenas para demonstrar o meu conhecimento em spring data, mysql e docker-compose.
Uma stack com Lambda e DynamoDB cairia muito bem também :)

## Cobertura de testes
100% coberto por testes de integrações, utilizando:<br>
-> org.mock-server<br>
-> com.h2database

## Para compilar e testar
Execute no terminal: docker-compose up<br><br>
**Importante:** Ao buildar e subir a API, ela pode demora de 3 a 5 minutos para baixar todos os CEPs e inserir no MySQL.<br>
Enquanto esse **setup** não termina, você vai receber o erro *503*, conforme exemplo abaixo.

## Exemplos para testar
###### curl http://localhost:6868/zip/03358150
*200 OK*
```JSON
{
    "zipcode": "03358150",
    "street": "Rua Ituri",
    "district": "Vila Formosa",
    "state": "SP",
    "city": "São Paulo"
}
```

*503 Service Unavailable*
```JSON
{
    "timestamp": "2023-01-16T23:27:34.962+00:00",
    "status": 503,
    "error": "Service Unavailable",
    "message": "This service is being installed, please wait a few moments.",
    "path": "/zip/03358150"
}
```

###### curl http://localhost:6868/zip/9999999
*204 No-Content*
