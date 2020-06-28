# 1. Prometheus Monitoring System

- Install Prometheus Operator (version 0.39 for this sample) in Kubernetes cluster.

    ```sh
    $ apictl apply -f https://raw.githubusercontent.com/coreos/prometheus-operator/v0.39.0/bundle.yaml
  
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
    $ apictl apply -f .
  
    Output:
    prometheus.monitoring.coreos.com/prometheus created
    serviceaccount/prometheus created
    clusterrole.rbac.authorization.k8s.io/prometheus created
    clusterrolebinding.rbac.authorization.k8s.io/prometheus created
    servicemonitor.monitoring.coreos.com/products-backend created
    service/prometheus created
    ```