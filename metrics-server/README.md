# 1. Kubernetes Metrics Server

[Metrics Server](https://github.com/kubernetes-sigs/metrics-server) collects resource metrics from Kubelets and exposes
them in Kubernetes apiserver through [Metrics API](https://github.com/kubernetes/metrics)
for use by Horizontal Pod Autoscaler and Vertical Pod Autoscaler

- Install Metrics Server

    **NOTE:** This installation only required in local setup, if you using GKE, EKS cluster you do not need to install
    following.
    ```sh
    $ apictl apply -f metrics-server-components-0.3.6.yaml
  
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