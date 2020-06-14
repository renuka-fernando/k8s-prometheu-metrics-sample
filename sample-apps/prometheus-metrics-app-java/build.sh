#!/bin/bash
mvn clean package;
mkdir -p target/dependency && (cd target/dependency || exit; jar -xf ../*.jar)
docker build -t renukafernando/k8s-prometheu-metrics-sample:v1.0.0 .