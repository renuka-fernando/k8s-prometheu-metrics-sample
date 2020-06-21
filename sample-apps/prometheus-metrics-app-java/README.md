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

## 2. Build Docker Image

Rename `IMAGE_NAME` and `VERSION` in the script `build.sh` and execute the script.
```sh
$ ./build.sh
```