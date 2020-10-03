# Prometheus Metrics Sample Application - Spring Boot

Docker Image: [renukafernando/k8s-prometheu-metrics-sample](https://hub.docker.com/repository/docker/renukafernando/k8s-prometheu-metrics-sample)

## 1. Test Application in Local

### 1.1 Run Application
Execute following to run the application
```sh
$ mvn spring-boot:run
```

### 1.2. Sample `Order` Payload
Sample payload of **Order** for methods POST and PUT methods.
```json
{
    "productId": 101,
    "quantity": 1.0,
    "shipDate": "2020-10-03T11:35:10.492+00:00",
    "status": "placed",
    "completed": false
}
```

### 1.3. Sample Requests
Sample requests for testing products service.
- Search and get products
    ```sh
    $ curl -X GET "http://localhost:8080/orders"
  
    $ curl -X GET "http://localhost:8080/orders?productId=101&completed=false"
  
    $ curl -X GET "http://localhost:8080/orders/13"
    ```
- Add product
    ```sh
    $ curl -X POST -H "Content-Type: application/json" \
        -d '{
                "productId":102,
                "quantity":5.0,
                "shipDate":"2020-10-03T08:47:09.373+00:00",
                "status":"delivered",
                "completed":true
            }' \
        "http://localhost:8080/orders"
    ```

- Update product
    ```sh
    $ curl -X PUT -H "Content-Type: application/json" \
        -d '{
                "productId":102,
                "quantity":2.0,
                "shipDate":"2020-10-03T08:47:09.373+00:00",
                "status":"delivered",
                "completed":false
            }' \
        "http://localhost:8080/products/106"
    ```

- Delete product
    ```sh
    $ curl -X DELETE "http://localhost:8080/orders/106"
    ```

### 1.4. Get Prometheus Metrics
Get metrics by calling the resource `/metrics` after making some [sample requests](#13-sample-requests).
```sh
$ curl -X GET http://localhost:8080/metrics
```

Sample Response
```log
# HELP products_http_requests_total Products: Total http requests
# TYPE products_http_requests_total counter
products_http_requests_total{http_method="GET",http_url="/products",priority="HIGH",} 19.0
products_http_requests_total{http_method="PUT",http_url="/products/$id",priority="LOW",} 3.0
products_http_requests_total{http_method="GET",http_url="/products/$id",priority="HIGH",} 2.0
products_http_requests_total{http_method="DELETE",http_url="/products/$id",priority="LOW",} 1.0
products_http_requests_total{http_method="POST",http_url="/products",priority="HIGH",} 4.0
products_http_requests_total{http_method="",http_url="",priority="",} 0.0
# HELP products_requests_latency_seconds Products: Request latency in seconds.
# TYPE products_requests_latency_seconds summary
products_requests_latency_seconds_count{http_method="GET",http_url="/products",priority="HIGH",} 19.0
products_requests_latency_seconds_sum{http_method="GET",http_url="/products",priority="HIGH",} 0.007540193000000001
products_requests_latency_seconds_count{http_method="",http_url="",priority="",} 1.0
products_requests_latency_seconds_sum{http_method="",http_url="",priority="",} 0.0
```

### 1.5. Sample Response
Sample response of products list.
```json
[
    {
        "orderId": 11,
        "productId": 101,
        "quantity": 2.0,
        "shipDate": "2020-10-03T11:33:49.373+00:00",
        "status": "placed",
        "completed": true
    },
    {
        "orderId": 12,
        "productId": 101,
        "quantity": 1.0,
        "shipDate": "2020-10-03T11:35:10.492+00:00",
        "status": "placed",
        "completed": false
    },
    {
        "orderId": 13,
        "productId": 102,
        "quantity": 5.0,
        "shipDate": "2020-10-03T08:47:09.373+00:00",
        "status": "delivered",
        "completed": true
    },
    {
        "orderId": 14,
        "productId": 102,
        "quantity": 1.0,
        "shipDate": "2020-09-30T00:13:49.373+00:00",
        "status": "delivered",
        "completed": false
    },
    {
        "orderId": 15,
        "productId": 102,
        "quantity": 2.0,
        "shipDate": "2020-10-04T01:27:09.373+00:00",
        "status": "placed",
        "completed": true
    }
]
```

## 2. Build Docker Image

Rename `IMAGE_NAME` and `VERSION` in the script `build.sh` and execute the script.
```sh
$ ./build.sh
```

## 3. Deploy in Kubernetes

Follow