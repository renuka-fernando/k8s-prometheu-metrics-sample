# 1. Prometheus Adapter Installation

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
            --from-file=serving-ca.crt=certs/serving-ca.crt \
            --from-file=serving-ca.key=certs/serving-ca.key \
            -n custom-metrics
    
    Output:
    secret/cm-adapter-serving-certs created
    ```

- Install Prometheus Adapter (version 0.7.0 for this sample) in Kubernetes cluster.
    ```sh
    $ kubectl apply -f .
  
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
  [custom-metrics-config-map.yaml](prometheus-adapter/custom-metrics-config-map.yaml) contains rules defined for this
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