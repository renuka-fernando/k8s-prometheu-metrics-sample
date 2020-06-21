#!/bin/bash
IMAGE_NAME=renukafernando/k8s-prometheu-metrics-sampl
VERSION=v1.0.0

mvn clean package;
mkdir -p target/dependency && (cd target/dependency || exit; jar -xf ../*.jar)
docker build -t $IMAGE_NAME:$VERSION .