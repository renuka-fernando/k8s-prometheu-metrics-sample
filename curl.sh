#!/bin/bash

# get the first nodes external IP in k8s cluster
node_ip=$(kubectl get nodes -o=jsonpath='{$.items[0].status.addresses[?(@.type=="ExternalIP")].address}')

# handle first argument
period=${1}
if [ -z "$period" ]; then
  period=2
fi

# handle first argument
resource=${2}
if [ -z "$resource" ]; then
  resource=/hello
fi

echo "Start sending requests"
i=1
while true; do
  printf "\nREQUST: %s and SLEEP %s seconds ------------------------------------------------\n" ${i} ${period};
  i=$((i+1)) ;
  curl --request GET \
    --url "http://${node_ip}:30994/${resource}" & sleep ${period};
done
