## hub-delivery-correios
Essa API é responsável por baixar [aqui](https://github.com/miltonhit/miltonhit/raw/main/public-assets/cep-20190602.csv) todos os endereços Brasileiros e inputar em uma base de dados MySQL.
Ao final do processo é possível pesquisar por um endpoint GET qualquer CEP Brasileiro.

## Stack
-> Java 11+
-> MySQL
-> Spring Framework, Spring DATA
-> Docker

## Para executar
Será um Docker-Compose com o spring e MySQL.
Ainda em construção.

## Rotas
GET /status
GET zip/{zipcode}

## Exemplo
###### curl http://localhost:7000/zip/03358150
200 OK
```JSON
{
    "zipcode": "03358150",
    "street": "Rua Ituri",
    "district": "Vila Formosa",
    "state": "SP",
    "city": "São Paulo"
}
```

503 Service Unavailable
```JSON
{
    "timestamp": "2023-01-16T23:27:34.962+00:00",
    "status": 503,
    "error": "Service Unavailable",
    "message": "This service is being installed, please wait a few moments.",
    "path": "/zip/03358150"
}
```

###### curl http://localhost:7000/zip/9999999

204 No Content
```JSON
```