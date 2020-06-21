# Prometheus Metrics Sample Application - Spring Boot

## 1. Test Application in Local

### 1.1 Run Application
Execute following to run the application
```sh
$ mvn spring-boot:run
```

### 1.2. Sample `Product` Payload
Sample payload of **Product** for methods POST and PUT methods.
```json
{
	"name": "XYZ Smart Phone",
	"category": "Electronics",
	"price": "$199.99"
}
```

### 1.3. Sample Requests
Sample requests for testing products service.
```sh
$ curl -X GET http://localhost:8080/products
$ curl -X GET http://localhost:8080/products/101
$ curl -X POST -H "Content-Type: application/json" \
    -d '{"name":"XYZ Smart Phone", "category":"Electronics", "price": "$199.99"}' \
    http://localhost:8080/products
$ curl -X PUT -H "Content-Type: application/json" \
    -d '{"name":"XYZ Smart Phone 20Plus", "category":"Electronics", "price": "$199.99"}' \
    http://localhost:8080/products/106
$ curl -X DELETE http://localhost:8080/products/106
```

### 1.4. Sample Response
Sample response of products list.
```json
[
    {
        "productId": 101,
        "name": "Apples",
        "category": "Food",
        "price": "$1.49"
    },
    {
        "productId": 102,
        "name": "Macaroni & Cheese",
        "category": "Food",
        "price": "$7.69"
    },
    {
        "productId": 102,
        "name": "ABC Smart TV",
        "category": "Electronics",
        "price": "$399.99"
    },
    {
        "productId": 104,
        "name": "Motor Oil",
        "category": "Automobile",
        "price": "$22.88"
    },
    {
        "productId": 105,
        "name": "Floral Sleeveless Blouse",
        "category": "Clothing",
        "price": "$21.50"
    }
]
```

## 2. Build Docker Image

Rename `IMAGE_NAME` and `VERSION` in the script `build.sh` and execute the script.
```sh
$ ./build.sh
```