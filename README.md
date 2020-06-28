# Sample for Horizontal Pod Autoscaling with custom metrics - Prometheus Server

## Steps to follow the tutorial

- [Install Metrics Server](metrics-server)
- [Install Prometheus Monitoring System](prometheus-server-configs)
- [Install Prometheus Adapter](prometheus-adapter-configs)
- [Deploy Sample Products Backend Service](sample-apps/prometheus-metrics-app-java/k8s-configs)

## Test Sample
- Lets make `IP` as the external IP of the LB service and `PERIOD` as waiting period in seconds to send requests
periodically.
    ```sh
    $ IP=<EXTERNAL_IP_OF_LB_SERVICE>
    PERIOD=5
    ```
    Send requests periodically.
    ```sh
    $ echo "Start sending requests"
    i=1
    while true; do
      printf "\nREQUST: %s and SLEEP %s seconds ------------------------------------------------\n" ${i} ${PERIOD};
      i=$((i+1)) ;
      curl -X GET "https://${IP}:9095/prodapi/v1/products" -k & sleep ${PERIOD};
    done
    ```
    Wait for 2-3 minutes and open a new terminal and execute following to get HPA details.
    ```sh
    $ apictl get hpa -w;
    
    Output:
    NAME                  REFERENCE                        TARGETS              MINPODS   MAXPODS   REPLICAS   AGE
    products              Deployment/products              200m/200m, 18%/50%   1         5         1          6m52s
    products-privatejet   Deployment/products-privatejet   166m/100m, 5%/50%    1         6         2          8m29s
    ```
    **NOTE:** Wait for fem minutes if the metrics values is `<unknown>`.

- Decrease and increase the `PERIOD` value and do the previous step to see the effect of HPA.