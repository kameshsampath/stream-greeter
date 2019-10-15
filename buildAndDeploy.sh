#!/bin/bash

set -eu

set -o pipefail

_CURR_DIR="$( cd "$(dirname "$0")" ; pwd -P )"

ARTIFACT_NAME=$(mvn org.apache.maven.plugins:maven-help-plugin:3.1.1:evaluate -Dexpression=project.build.finalName -q -DforceStdout)
ARTIFACT_VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:3.1.1:evaluate -Dexpression=project.version -q -DforceStdout)

#mvn -DskipTests clean package -Pnative -Dnative-image.docker-build=true
mvn -DskipTests clean package

IMAGE=${1:-"dev.local/rhdevelopers/$ARTIFACT_NAME:v$ARTIFACT_VERSION"}

docker build -t $IMAGE  $_CURR_DIR

yq w $_CURR_DIR/kubernetes/app.yaml -d1 'spec.template.spec.containers[0].image' $IMAGE | kubectl apply -n knativetutorial -f -

