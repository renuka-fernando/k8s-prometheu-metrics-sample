# Sample for Horizontal Pod Autoscaling with custom metrics - Prometheus Server

## Steps to follow the tutorial

1. [Install Metrics Server](metrics-server)

    If you are running Minikube, run the following command to enable metrics-server.
    ```sh
    $ minikube addons enable metrics-server
    ```
    OR else
    **NOTE:** This installation only required in local setup, if you using GKE, EKS cluster you do not need to install
    following.
    ```sh
    $ kubectl apply -f metrics-server/metrics-server-components-0.3.6.yaml
  
    Output:
    clusterrole.rbac.authorization.k8s.io/system:aggregated-metrics-reader created
    clusterrolebinding.rbac.authorization.k8s.io/metrics-server:system:auth-delegator created
    rolebinding.rbac.authorization.k8s.io/metrics-server-auth-reader created
    apiservice.apiregistration.k8s.io/v1beta1.metrics.k8s.io created
    serviceaccount/metrics-server created
    deployment.apps/metrics-server created
    service/metrics-server created
    clusterrole.rbac.authorization.k8s.io/system:metrics-server created
    clusterrolebinding.rbac.authorization.k8s.io/system:metrics-server created
    ```

1. [Install Prometheus Monitoring System](prometheus-server-configs)

    - Install Prometheus Operator (version 0.39 for this sample) in Kubernetes cluster.
    
        ```sh
        $ kubectl apply -f https://raw.githubusercontent.com/coreos/prometheus-operator/v0.39.0/bundle.yaml
      
        Output:
        customresourcedefinition.apiextensions.k8s.io/alertmanagers.monitoring.coreos.com created
        customresourcedefinition.apiextensions.k8s.io/podmonitors.monitoring.coreos.com created
        customresourcedefinition.apiextensions.k8s.io/prometheuses.monitoring.coreos.com created
        customresourcedefinition.apiextensions.k8s.io/prometheusrules.monitoring.coreos.com created
        customresourcedefinition.apiextensions.k8s.io/servicemonitors.monitoring.coreos.com created
        customresourcedefinition.apiextensions.k8s.io/thanosrulers.monitoring.coreos.com created
        clusterrolebinding.rbac.authorization.k8s.io/prometheus-operator created
        clusterrole.rbac.authorization.k8s.io/prometheus-operator created
        deployment.apps/prometheus-operator created
        serviceaccount/prometheus-operator created
        service/prometheus-operator created
        ```
    
    - Create a Prometheus instance in Kubernetes cluster. The directory `prometheus/` contains related configurations.
        ```sh
        $ kubectl apply -f prometheus-server-configs/
      
        Output:
        prometheus.monitoring.coreos.com/prometheus created
        serviceaccount/prometheus created
        clusterrole.rbac.authorization.k8s.io/prometheus created
        clusterrolebinding.rbac.authorization.k8s.io/prometheus created
        servicemonitor.monitoring.coreos.com/products-backend created
        service/prometheus created
        ```
1. [Install Prometheus Adapter](prometheus-adapter-configs)

    - Create namespace `custom-metrics`.
        ```sh
        $ kubectl create namespace custom-metrics
      
        Output:
        namespace/custom-metrics created
        ```
    - Create service certificate. Follow [Serving Certificates, Authentication, and Authorization](https://github.com/kubernetes-sigs/apiserver-builder-alpha/blob/v1.18.0/docs/concepts/auth.md)
    to create serving certificate. For this sample we can use certs in the directory `prometheus-adapter/certs`. Create secret `cm-adapter-serving-certs` as follows.
        ```sh
        $ kubectl create secret generic cm-adapter-serving-certs \
                --from-file=serving-ca.crt=prometheus-adapter-configs/certs/serving-ca.crt \
                --from-file=serving-ca.key=cprometheus-adapter-configs/erts/serving-ca.key \
                -n custom-metrics
        
        Output:
        secret/cm-adapter-serving-certs created
        ```
    
    - Install Prometheus Adapter (version 0.7.0 for this sample) in Kubernetes cluster.
        ```sh
        $ kubectl apply -f prometheus-adapter-configs/
      
        Output:
        clusterrolebinding.rbac.authorization.k8s.io/custom-metrics:system:auth-delegator created
        rolebinding.rbac.authorization.k8s.io/custom-metrics-auth-reader created
        deployment.apps/custom-metrics-apiserver created
        clusterrolebinding.rbac.authorization.k8s.io/custom-metrics-resource-reader created
        serviceaccount/custom-metrics-apiserver created
        service/custom-metrics-apiserver created
        apiservice.apiregistration.k8s.io/v1beta1.custom.metrics.k8s.io created
        clusterrole.rbac.authorization.k8s.io/custom-metrics-server-resources created
        configmap/adapter-config created
        clusterrole.rbac.authorization.k8s.io/custom-metrics-resource-reader created
        clusterrolebinding.rbac.authorization.k8s.io/hpa-controller-custom-metrics created
        ```
      In the directory `prometheus-adapter` we have specified configurations for Prometheus Adapter.
      [custom-metrics-config-map.yaml](prometheus-adapter-configs/custom-metrics-config-map.yaml) contains rules defined for this
      sample.
        ```yaml
        # rule for products backend service
        - seriesQuery: '{__name__=~"^.*_http_requests_total"}'
          resources:
            overrides:
              namespace: {resource: "namespace"}
              pod: {resource: "pod"}
          name:
            matches: "^(.*)_http_requests_total"
            as: "${1}_http_requests_total_per_second"
          metricsQuery: 'sum(rate(<<.Series>>{<<.LabelMatchers>>,http_url!=""}[1m])) by (<<.GroupBy>>)'
        
    - Test the Prometheus Adapter deployment executing follows.
        ```sh
        $ kubectl get --raw /apis/custom.metrics.k8s.io/v1beta1
      
        Output:
        {"kind":"APIResourceList","apiVersion":"v1","groupVersion":"custom.metrics.k8s.io/v1beta1","resources":[]}
        ```
  
1. [Deploy Sample Products Backend Service](sample-apps/prometheus-metrics-app-java/k8s-configs)

    ```sh
    $ kubectl apply -f sample-apps/prometheus-metrics-app-java/k8s-configs/
    
    Output:
    deployment.apps/products-backend created
    horizontalpodautoscaler.autoscaling/products created
    service/products-backend created
    ```

## Test Sample

- Lets make `IP` as the node IP and `PERIOD` as waiting period in seconds to send requests
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
      curl -X GET "http://${IP}:30100/products" & sleep ${PERIOD};
    done
    ```
    Wait for 2-3 minutes and open a new terminal and execute following to get HPA details.
    ```sh
    $ kubectl get hpa -w;
    
    Output:
    NAME       REFERENCE                     TARGETS                         MINPODS   MAXPODS   REPLICAS   AGE
    products   Deployment/products-backend   <unknown>/100m, <unknown>/50%   1         10        0          8s
    products   Deployment/products-backend   <unknown>/100m, <unknown>/50%   1         10        1          15s
    products   Deployment/products-backend   <unknown>/100m, 1%/50%          1         10        1          92s
    products   Deployment/products-backend   <unknown>/100m, 1%/50%          1         10        1          2m33s
    products   Deployment/products-backend   173m/100m, 1%/50%               1         10        1          2m49s
    products   Deployment/products-backend   200m/100m, 1%/50%               1         10        2          3m4s
    products   Deployment/products-backend   99m/100m, 1%/50%                1         10        2          3m19s
    products   Deployment/products-backend   99m/100m, 2%/50%                1         10        2          3m34s
    products   Deployment/products-backend   106m/100m, 2%/50%               1         10        2          3m50s
    products   Deployment/products-backend   116m/100m, 2%/50%               1         10        2          4m5s
    ```
    **NOTE:** Wait for fem minutes if the metrics values is `<unknown>`.

- Decrease and increase the `PERIOD` value and do the previous step to see the effect of HPA.