#!/bin/bash
IMAGE_NAME=renukafernando/k8s-prometheu-metrics-order-sample
VERSION=v1.0.1

mvn clean package;
mkdir -p target/dependency && (cd target/dependency || exit; jar -xf ../*.jar)
docker build -t $IMAGE_NAME:$VERSION .