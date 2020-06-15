Create secret
```sh
cd certs
kubectl create namespace custom-metrics
kubectl -n custom-metrics create secret generic cm-adapter-serving-certs --from-file client-ca-config.json --from-file requestheader-client-ca-config.json --from-file server-ca-config.json --from-file serving-ca-config.json --from-file client-ca.crt --from-file requestheader-client-ca.crt --from-file server-ca.crt --from-file serving-ca.crt --from-file client-ca.key --from-file requestheader-client-ca.key --from-file server-ca.key --from-file serving-ca.key
```

Apply configs
```sh
kubectl apply -f .
```