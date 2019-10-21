# Stream Greeter

A simple reactive messaging example that pumps messages to Kafka topic in a defined interval

## Pre-requisites

* [Kafka](https://stirmzi.io)
* [Tekton Pipelines](https://tekton.dev)

## Download sources

git clone this repository `git clone https://github.com/kameshsampath/stream-greeter`. Lets call this cloned repository directory as `$PROJECT_HOME`

## Build and Deploy

For this demo we will use a kubernetes namespace called `knativetutorial`

```shell
kubectl create ns knativetutorial
```

## minikube

```shell
./buildAndDeploy.sh
```

## Build with Tekton Pipelines

Create the pipeline service account

```shell
kubectl apply -f https://raw.githubusercontent.com/redhat-developer-demos/knative-tutorial/master/06-pipelines/pipeline-sa-role.yaml
```

```shell
kubectl apply -f $PROJECT_HOME/kubernetes/build-resources.yaml
kubectl apply -f $PROJECT_HOME/kubernetes/yq-task.yaml
kubectl apply -f https://raw.githubusercontent.com/tektoncd/catalog/master/openshift-client/openshift-client-task.yaml
kubectl apply -f $PROJECT_HOME/kubernetes/build-app-task.yaml
kubectl apply -f $PROJECT_HOME/kubernetes/build-and-deploy.yaml
```

Run the pipeline using the command

```shell
tkn pipeline start kn-svc-deploy \
 --param="mavenMirrorUrl=http://nexus.rhd-workshop-infra:8081/nexus/content/groups/public"  \
 --resource="appSource=stream-git-source" \
 --resource="appImage=stream-greeter-image" \
 --serviceaccount='pipeline'
```
