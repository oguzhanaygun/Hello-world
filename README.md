# Demo Project

![Go](https://github.com/spootrick/hello/workflows/Go/badge.svg?branch=master)

This repository contains simple web application to demonstrate automated CI pipeline and K8s deployment.

## Requirements

- Docker
- Kubernetes (Docker Desktop)
- Kubectl
- Helm3

## About Project

There is a Go based simple web application which has an automated test, build and deploy pipelines.
It will print some hello message when requested.

The workflow is as follows:

1. If there is a push or a pull request on `master` branch,
1. GitHub action on the repo will be triggered.
1. This action will perform following in order:
    1. Creates an Ubuntu container
    1. Sets up a Go environment
    1. Checks out the repository
    1. Runs the unit tests
    1. Builds the Docker container
    1. Pushes the Docker container to [DockerHub](https://hub.docker.com/repository/docker/spootrick/hello)
1. After successful push to the DockerHub, you can install web app to your K8s 
cluster with Helm.

## Local Development

In order to develop and maintain the project locally, there is a `Makefile` to perform some actions.
You can run;

1. `make test` for unit tests
1. `make build` to build Docker container
1. `make push` to push Docker container to [DockerHub](https://hub.docker.com/repository/docker/spootrick/hello)
1. `make show-resource-list` to see clusters resources

Environment variables:

| Env. variable | Default Value | Description |
|---|---|---|
| REGISTRY | registry-1.docker.io | Default DockerHub registry |
| VERSION | latest | Docker image version tag |
| KUBECONFIG | ${HOME}/.kube/config | Default Docker Desktop Kubeconfig path |

## About Dockerfile

The Dockerfile in this project is a multi-staged Dockerfile. By this way, we can
reduce the size of the resulting image. Also, final image has a non-root user to
run the produced binary for security purposes.

## Configure the Customization Points

The web application prints hello message when there is a request on specified port
in the container. Both hello message and port can be adjusted from Helm `values.yaml` file.

## Installing Helm Chart

To install the Helm chart, simply issue the following command:

```bash
cd <project-directory>
helm install --generate-name helm/
```

*Note:* Do not forget to install appropirate Nginx ingress to your cluster. You can
follow the documentation [here](https://github.com/kubernetes/ingress-nginx/blob/master/docs/deploy/index.md).

After installing the chart, open up your favorite browser and hit the 
`http://localhost/`.

*Note:* Since Docker Desktop for Mac didn't provide an IP address, I couldn't set up a DNS name for it,
so I continued with `localhost`.

## K8s Autoscale

The deployment of the web app has `HorizontalPodAutoscaler` to scale the replicas.

It has three rules to scale the deployment:

1. CPU utilization > 80%
1. Memory utilization > 80%
1. Requests per second to Ingress > 1k

If one of the rule is applied, the replica count will be between 1-50.

## Helper Script

There is a Python script called `resource_list.py` to list cluster resources. It will display each
node in the cluster with CPU and memory request and limits.

Sample usage:

```bash
# with Makefile
make show-resource-list
# it will use the following kubeconfig file: "${HOME}/.kube/config"

# without Makefile
python3 resource_list.py -k </full/path/to/kubeconfig>

python3 resource_list.py --help
# usage: resource_list.py [-h] -k KUBECONFIG [-v] [--version]
#
# List K8s resources
#
# optional arguments:
#   -h, --help            show this help message and exit
#   -k KUBECONFIG, --kubeconfig KUBECONFIG
#                         Path to Kubeconfig file
#   -v, --verbose         Verbosity (-v, -vv)
#   --version             show program's version number and exit
```

It will display:

```
2020-08-09 23:29:20 +03 [INFO] [resource_list.py:52] Collecting cluster nodes
2020-08-09 23:29:22 +03 [INFO] [resource_list.py:67] Collecting metrics for node 'xxx.eu-central-1.compute.internal'
2020-08-09 23:29:23 +03 [INFO] [resource_list.py:67] Collecting metrics for node 'xxx.eu-central-1.compute.internal'
2020-08-09 23:29:23 +03 [INFO] [resource_list.py:67] Collecting metrics for node 'xxx.eu-central-1.compute.internal'
2020-08-09 23:29:25 +03 [INFO] [resource_list.py:67] Collecting metrics for node 'xxx.eu-central-1.compute.internal'
2020-08-09 23:29:25 +03 [INFO] [resource_list.py:67] Collecting metrics for node 'xxx.eu-central-1.compute.internal'
2020-08-09 23:29:26 +03 [INFO] [resource_list.py:67] Collecting metrics for node 'xxx.eu-central-1.compute.internal'
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:93] ------------------------------------------------------------
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:94] Collected cluster metrics. Node count in cluster: 6
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:95] ------------------------------------------------------------
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:98] Node: xxx.eu-central-1.compute.internal [CPU] request 2275m (28%) limit 27335m (345%) [Memory] request 10644999808 (34%) limit 118339497344 (384%)
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:98] Node: xxx.eu-central-1.compute.internal [CPU] request 1665m (21%) limit 8505m (107%) [Memory] request 1643942784 (5%) limit 13832590208 (44%)
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:98] Node: xxx.eu-central-1.compute.internal [CPU] request 3895m (49%) limit 45745m (577%) [Memory] request 13028473472 (41%) limit 43019790720 (138%)
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:98] Node: xxx.eu-central-1.compute.internal [CPU] request 2155m (27%) limit 3875m (48%) [Memory] request 8353780608 (27%) limit 24082930560 (78%)
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:98] Node: xxx.eu-central-1.compute.internal [CPU] request 2695m (34%) limit 10045m (126%) [Memory] request 16542821760 (53%) limit 41211005824 (134%)
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:98] Node: xxx.eu-central-1.compute.internal [CPU] request 4098m (51%) limit 27820m (351%) [Memory] request 16976030592 (55%) limit 87911663872 (285%)
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:100] ------------------------------------------------------------
2020-08-09 23:29:27 +03 [INFO] [resource_list.py:101] Total limits may be over 100 percent, i.e., overcommitted
```
