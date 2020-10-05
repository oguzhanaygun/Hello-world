# Demo Project

http://hb-task.oguzhanaygun.com/hello

This repository contains simple web application to demonstrate automated CI pipeline and K8s deployment.

## Requirements

- Java
- Maven
- Docker
- Kubernetes (Docker Desktop)
- Kubectl
- kustomize

## About Project

There is a  simple spring boot application which has an automated test, build and deploy pipelines.
It will print some hello message when requested.

The workflow is as follows:

1. If there is a change on `master` branch,
1. GitHub action on the repo will be triggered.
1. This action will perform following in order:
    1. Builds application
    1. Runs unit tests
    1. Builds the a docker image
    1. Pushes created image to private container registry
    1. pulls pushed image from registry
    1. Creates a k8s deployment on gcm
    2. Creates a k8s service on gcm

## About Dockerfile

The Dockerfile in this project is a multi-staged Dockerfile. By this way, we can
reduce the size of the resulting image. .

## About App

The web application runs on port 11130 and  prints hello message when there is a request on "/hello" endpoint. 

## K8s Autoscale

The deployment of the web app has `HorizontalPodAutoscaler` to scale the replicas.

It has three rules to scale the deployment:

1. CPU utilization > 50%

If one of the rule is applied, the replica count will be between 1-10.

## RESOURCES
- https://spring.io/
- https://www.docker.com/
- https://cloud.google.com/container-registry/docs/quickstart
- https://kubernetes.io/docs/tutorials/kubernetes-basics/
- https://cloud.google.com/kubernetes-engine/docs/tutorials
- https://kustomize.io/
- https://github.com/GoogleCloudPlatform/github-actions/tree/master/example-workflows/gke
