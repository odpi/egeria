<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# open-metadata-deployment

## Overview

This module contains resources to assist in deployment of Egeria and related components. They are intended to
facilitate developer useage and technology demonstrations and will need further work to be suitable for production.

Currently the focus is on Helm charts to facilitate deployment to Kubernetes clusters.

Docker images are created independently from this project and hosted on docker hub.

Helm charts will be reused from other helm repos when available

## Kubernetes install

**Note**: before following the steps below, ideally ensure that any pre-existing `kubectl`
configuration is moved (or removed), eg. from HomeBrew, as it may conflict with the one being
configured by Docker in the steps below.
(See https://docs.docker.com/docker-for-mac/#kubernetes)

The easiest way to experiment with kubernetes is to

1. Install Docker for Mac | Windows
1. Go to `Preferences...` -> `Kubernetes` and ensure `Enable Kubernetes` is selected
1. Start / wait for docker and kubernetes to be started
1. Install Helm: for macOS (with [HomeBrew](https://brew.sh)) just use `brew install kubernetes-helm`
1. Initialize helm with `helm init`
 
For installing helm on other platforms see: https://docs.helm.sh/using_helm/#installing-helm
 
The instructions below assume using a local kubernetes. For cloud providers the noticeable differences will be

- Typically needing to login to the cloud service via the CLI
- Network can differ - in particular how ports are exposed outside the cluster, and whether you should use
  NetworkPort, LoadBalancer or other options
- Once configured, use `kubectl config use-context` to switch between operating with different clouds or local

## Helm configuration

To make use of Helm and its charts, first run through the following steps to ensure the charts we
want to make use of will be available.

### Add chart repositories

Start by adding the chart repositories we will need with the following helm command:

```bash
$ helm repo add confluent https://confluentinc.github.io/cp-helm-charts/
"confluent" has been added to your repositories
```

This repository is needed for the Kafka chart on which our deployment depends.

Then update your local repository index with the following command:

```bash
$ helm repo update
Hang tight while we grab the latest from your chart repositories...
...Skip local chart repository
...Successfully got an update from the "confluent" chart repository
...Successfully got an update from the "stable" chart repository
Update Complete. * Happy Helming!*
```

## Usage

For an interesting, multi-component configuration we'll use the `vdc` chart.

This chart deploys Apache Zookeeper, Apache Kafka, a PostgreSQL database, Apache Atlas and can configure and deploy
sample metadata into a pre-existing IBM InfoSphere Information Governance Catalog ("IGC") environment. The intention of
the chart is to setup an environment that can be used to demonstrate a Virtual Data Connector ("VDC") that makes use of
metadata across multiple repositories (Apache Atlas and IBM IGC) to control access to data within a database
(PostgreSQL), through a virtualized access layer (Gaian).

*(Be aware that this is very much still in a preview state, and not all pieces may yet be deployed or fully working.)*

**Important**: Make sure your starting directory is `open-metadata-resources/open-metadata-deployment/charts`.

### Downloading the chart's dependencies

Start by downloading the chart's dependencies. To do this, change into the `vdc` subdirectory
within the `charts` directory referenced as the starting point above and run the following:

```bash
$ helm dependency update
Hang tight while we grab the latest from your chart repositories...
...Unable to get an update from the "local" chart repository (http://127.0.0.1:8879/charts):
	Get http://127.0.0.1:8879/charts/index.yaml: dial tcp 127.0.0.1:8879: connect: connection refused
...Successfully got an update from the "confluent" chart repository
...Successfully got an update from the "stable" chart repository
Update Complete. ⎈Happy Helming!⎈
Saving 2 charts
Downloading cp-helm-charts from repo https://confluentinc.github.io/cp-helm-charts/
Downloading openldap from repo https://kubernetes-charts.storage.googleapis.com/
Deleting outdated charts
```

### Configuring environment-specific values

Create a simple YAML file (eg. `vars.yml`) specific to your environment conditions with the following values:

```yaml
igc:
  host: 192.168.1.200
service:
  externalip: 192.168.1.175
```

These values capture the following:

- `igc.host`: the hostname or IP address of your pre-existing IBM InfoSphere Information Governance Catalog environment
- `service.externalip`: the hostname or IP address of your kubernetes worker nodes (for NodePort services)

(The two IP addresses / hosts must be network-accessible to each other, but could be on the same private network as in
the example above.)

### Deploying the demonstration

Navigate back up to the `charts` directory, and run the following command (providing the name of the file you configured
in the step immediately above):

```bash
$ helm install vdc -f vars.yml
2019/02/07 15:58:35 Warning: Building values map for chart 'cp-kafka'. Skipped value (map[]) for 'image', as it is not a table.
NAME:   bulging-jaguar
LAST DEPLOYED: Thu Feb  7 15:58:35 2019
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1beta1/PodDisruptionBudget
NAME                             MIN AVAILABLE  MAX UNAVAILABLE  ALLOWED DISRUPTIONS  AGE
bulging-jaguar-cp-zookeeper-pdb  N/A            1                0                    2s

==> v1/Secret
NAME                     TYPE    DATA  AGE
bulging-jaguar-openldap  Opaque  2     2s

==> v1/ConfigMap
NAME                                       DATA  AGE
bulging-jaguar-cp-kafka-jmx-configmap      1     2s
bulging-jaguar-cp-zookeeper-jmx-configmap  1     2s
bulging-jaguar-openldap-env                6     2s
bulging-jaguar-vdc-configmap               28    2s

==> v1/Service
NAME                                    TYPE       CLUSTER-IP      EXTERNAL-IP  PORT(S)                                                                     AGE
bulging-jaguar-cp-kafka-headless        ClusterIP  None            <none>       9092/TCP                                                                    2s
bulging-jaguar-cp-kafka                 ClusterIP  10.108.188.62   <none>       9092/TCP                                                                    2s
bulging-jaguar-cp-zookeeper-headless    ClusterIP  None            <none>       2888/TCP,3888/TCP                                                           2s
bulging-jaguar-cp-zookeeper             ClusterIP  10.106.138.42   <none>       2181/TCP                                                                    2s
bulging-jaguar-openldap                 ClusterIP  10.107.69.145   <none>       389/TCP,636/TCP                                                             2s
bulging-jaguar-vdc-atlas-service        NodePort   10.108.223.125  <none>       21000:31000/TCP                                                             2s
bulging-jaguar-vdc-egeria-service       NodePort   10.98.17.216    <none>       8080:30080/TCP                                                              2s
bulging-jaguar-vdc-gaian-service        NodePort   10.108.113.168  <none>       6414:30414/TCP                                                              2s
bulging-jaguar-vdc-igcproxy-service     NodePort   10.102.86.45    <none>       8080:30081/TCP                                                              2s
bulging-jaguar-vdc-omrsmonitor-service  NodePort   10.96.30.144    <none>       58080:31080/TCP                                                             2s
bulging-jaguar-vdc-postgresql-service   NodePort   10.102.144.50   <none>       5432:30432/TCP,22:32472/TCP                                                 2s
bulging-jaguar-vdc-ranger-service       NodePort   10.109.23.244   <none>       6080:32080/TCP,6182:30182/TCP,6083:30126/TCP,6183:30004/TCP,3306:30647/TCP  2s
bulging-jaguar-vdc-ui-service           NodePort   10.101.49.46    <none>       8443:30443/TCP                                                              2s

==> v1beta2/Deployment
NAME                     DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
bulging-jaguar-openldap  1        0        0           0          2s

==> v1/Deployment
NAME                                         DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
bulging-jaguar-vdc-atlas-deployment          1        1        1           0          2s
bulging-jaguar-vdc-egeria-deployment         1        1        1           0          2s
bulging-jaguar-vdc-gaian-deployment          1        1        1           0          2s
bulging-jaguar-vdc-igcproxy-deployment       1        0        0           0          2s
bulging-jaguar-vdc-kafka-monitor-deployment  1        1        1           0          2s
bulging-jaguar-vdc-omrsmonitor-deployment    1        1        1           0          2s
bulging-jaguar-vdc-postgresql-deployment     1        1        1           0          2s
bulging-jaguar-vdc-rangeradmin               1        1        1           0          2s
bulging-jaguar-vdc-ui-deployment             1        1        1           0          2s

==> v1beta1/StatefulSet
NAME                         DESIRED  CURRENT  AGE
bulging-jaguar-cp-kafka      3        1        2s
bulging-jaguar-cp-zookeeper  3        1        1s

==> v1/Pod(related)
NAME                                                          READY  STATUS             RESTARTS  AGE
bulging-jaguar-openldap-8978d8785-nfjvg                       0/1    ContainerCreating  0         2s
bulging-jaguar-vdc-atlas-deployment-74f9dcdf6-fq697           0/2    ContainerCreating  0         1s
bulging-jaguar-vdc-egeria-deployment-7447b576c5-dj7c6         0/2    ContainerCreating  0         2s
bulging-jaguar-vdc-gaian-deployment-7b8dd86577-w4kql          0/1    ContainerCreating  0         2s
bulging-jaguar-vdc-igcproxy-deployment-598d4f875c-w44hr       0/2    ContainerCreating  0         2s
bulging-jaguar-vdc-kafka-monitor-deployment-65cb86875f-4t8b9  0/1    Pending            0         1s
bulging-jaguar-vdc-omrsmonitor-deployment-6595c7c9bd-wtqw8    0/1    Pending            0         1s
bulging-jaguar-vdc-postgresql-deployment-5965997d88-zzn7h     0/2    Pending            0         1s
bulging-jaguar-vdc-rangeradmin-b99f95f7b-fxs7z                0/2    Pending            0         1s
bulging-jaguar-vdc-ui-deployment-7944f78655-kbjhq             0/1    Pending            0         1s
bulging-jaguar-cp-kafka-0                                     0/2    Pending            0         1s
bulging-jaguar-cp-zookeeper-0                                 0/2    Pending            0         1s
```



### Finding out the network address/IP

The simplest method for determining the network address and IPs that kubernetes is exposing
for external access is its `get services` command:

```bash
$ kubectl get services
NAME                                     TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                                                                      AGE
bulging-jaguar-cp-kafka                  ClusterIP   10.108.188.62    <none>        9092/TCP                                                                     3m
bulging-jaguar-cp-kafka-headless         ClusterIP   None             <none>        9092/TCP                                                                     3m
bulging-jaguar-cp-zookeeper              ClusterIP   10.106.138.42    <none>        2181/TCP                                                                     3m
bulging-jaguar-cp-zookeeper-headless     ClusterIP   None             <none>        2888/TCP,3888/TCP                                                            3m
bulging-jaguar-openldap                  ClusterIP   10.107.69.145    <none>        389/TCP,636/TCP                                                              3m
bulging-jaguar-vdc-atlas-service         NodePort    10.108.223.125   <none>        21000:31000/TCP                                                              3m
bulging-jaguar-vdc-egeria-service        NodePort    10.98.17.216     <none>        8080:30080/TCP                                                               3m
bulging-jaguar-vdc-gaian-service         NodePort    10.108.113.168   <none>        6414:30414/TCP                                                               3m
bulging-jaguar-vdc-igcproxy-service      NodePort    10.102.86.45     <none>        8080:30081/TCP                                                               3m
bulging-jaguar-vdc-omrsmonitor-service   NodePort    10.96.30.144     <none>        58080:31080/TCP                                                              3m
bulging-jaguar-vdc-postgresql-service    NodePort    10.102.144.50    <none>        5432:30432/TCP,22:32472/TCP                                                  3m
bulging-jaguar-vdc-ranger-service        NodePort    10.109.23.244    <none>        6080:32080/TCP,6182:30182/TCP,6083:30126/TCP,6183:30004/TCP,3306:30647/TCP   3m
bulging-jaguar-vdc-ui-service            NodePort    10.101.49.46     <none>        8443:30443/TCP                                                               3m
kubernetes                               ClusterIP   10.96.0.1        <none>        443/TCP                                                                      2d
```

From the list above you can see that the Egeria service (`bulging-jaguar-vdc-egeria-service`) is available
externally on port `30080`.

More detail can be obtained about any particular service using the `describe service` command:

```bash
$ kubectl describe service bulging-jaguar-vdc-egeria-service
Name:                     bulging-jaguar-vdc-egeria-service
Namespace:                default
Labels:                   app.kubernetes.io/component=egeria-service
                          app.kubernetes.io/instance=bulging-jaguar
                          app.kubernetes.io/managed-by=Tiller
                          app.kubernetes.io/name=vdc
                          helm.sh/chart=vdc-0.1.10
Annotations:              <none>
Selector:                 app.kubernetes.io/component=egeria-service,app.kubernetes.io/instance=bulging-jaguar,app.kubernetes.io/name=vdc
Type:                     NodePort
IP:                       10.98.17.216
LoadBalancer Ingress:     localhost
Port:                     <unset>  8080/TCP
TargetPort:               8080/TCP
NodePort:                 <unset>  30080/TCP
Endpoints:                10.1.0.51:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   <none>
```

- The `IP` is the internal IP of the pod running the Egeria service - and only accessible within kubernetes. It can
    change constantly as pods are started/stopped/restarted.
- `TargetPort` is the port exposing the service on the pod (internally).
- `Endpoints` is the cluster IP for the service. This remains stable and will automatically redirect to the correct pod(s).
- `NodePort` gives me the external port now exposed - in this case `30080`. The interpretation of this varies depending
    on provider (the example here is for Docker on MacOS).
- Hence, `http://localhost:30080` would be the baseURL of the egeria service.

The `kubectl` command allows you to extensively explore the kubernetes resources, but is best explored
through [its own reference material](https://kubernetes.io/docs/reference/kubectl/overview/).

### Finding a list of deployed charts

If you're unsure which charts are deployed, use the following helm command to list them:

```bash
$ helm list
NAME          	REVISION	UPDATED                 	STATUS  	CHART     	APP VERSION	NAMESPACE
bulging-jaguar	1       	Thu Feb  7 15:58:35 2019	DEPLOYED	vdc-0.1.10	           	default
```

### Checking the status of a helm chart

In the example above, the deployment is called `bulging-jaguar`.
To check the status of the deployment (ie. that everything is up and running, as it may take a
few minutes for everything to startup), use this command:

```bash
$ helm status bulging-jaguar
LAST DEPLOYED: Thu Feb  7 15:58:35 2019
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/Deployment
NAME                                         DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
bulging-jaguar-vdc-atlas-deployment          1        1        1           1          9m23s
bulging-jaguar-vdc-egeria-deployment         1        1        1           1          9m23s
bulging-jaguar-vdc-gaian-deployment          1        1        1           1          9m23s
bulging-jaguar-vdc-igcproxy-deployment       1        1        1           1          9m23s
bulging-jaguar-vdc-kafka-monitor-deployment  1        1        1           1          9m23s
bulging-jaguar-vdc-omrsmonitor-deployment    1        1        1           1          9m23s
bulging-jaguar-vdc-postgresql-deployment     1        1        1           1          9m23s
bulging-jaguar-vdc-rangeradmin               1        1        1           1          9m23s
bulging-jaguar-vdc-ui-deployment             1        1        1           1          9m23s

==> v1beta1/StatefulSet
NAME                         DESIRED  CURRENT  AGE
bulging-jaguar-cp-kafka      3        3        9m23s
bulging-jaguar-cp-zookeeper  3        3        9m22s

==> v1/Pod(related)
NAME                                                          READY  STATUS   RESTARTS  AGE
bulging-jaguar-openldap-8978d8785-nfjvg                       1/1    Running  0         9m23s
bulging-jaguar-vdc-atlas-deployment-74f9dcdf6-fq697           2/2    Running  0         9m22s
bulging-jaguar-vdc-egeria-deployment-7447b576c5-dj7c6         2/2    Running  0         9m23s
bulging-jaguar-vdc-gaian-deployment-7b8dd86577-w4kql          1/1    Running  0         9m23s
bulging-jaguar-vdc-igcproxy-deployment-598d4f875c-w44hr       2/2    Running  0         9m23s
bulging-jaguar-vdc-kafka-monitor-deployment-65cb86875f-4t8b9  1/1    Running  0         9m22s
bulging-jaguar-vdc-omrsmonitor-deployment-6595c7c9bd-wtqw8    1/1    Running  0         9m22s
bulging-jaguar-vdc-postgresql-deployment-5965997d88-zzn7h     2/2    Running  0         9m22s
bulging-jaguar-vdc-rangeradmin-b99f95f7b-fxs7z                2/2    Running  0         9m22s
bulging-jaguar-vdc-ui-deployment-7944f78655-kbjhq             1/1    Running  0         9m22s
bulging-jaguar-cp-kafka-0                                     2/2    Running  0         9m22s
bulging-jaguar-cp-kafka-1                                     2/2    Running  0         9m5s
bulging-jaguar-cp-kafka-2                                     2/2    Running  0         9m1s
bulging-jaguar-cp-zookeeper-0                                 2/2    Running  0         9m22s
bulging-jaguar-cp-zookeeper-1                                 2/2    Running  0         9m4s
bulging-jaguar-cp-zookeeper-2                                 2/2    Running  0         8m59s

==> v1beta1/PodDisruptionBudget
NAME                             MIN AVAILABLE  MAX UNAVAILABLE  ALLOWED DISRUPTIONS  AGE
bulging-jaguar-cp-zookeeper-pdb  N/A            1                1                    9m23s

==> v1/Secret
NAME                     TYPE    DATA  AGE
bulging-jaguar-openldap  Opaque  2     9m23s

==> v1/ConfigMap
NAME                                       DATA  AGE
bulging-jaguar-cp-kafka-jmx-configmap      1     9m23s
bulging-jaguar-cp-zookeeper-jmx-configmap  1     9m23s
bulging-jaguar-openldap-env                6     9m23s
bulging-jaguar-vdc-configmap               28    9m23s

==> v1/Service
NAME                                    TYPE       CLUSTER-IP      EXTERNAL-IP  PORT(S)                                                                     AGE
bulging-jaguar-cp-kafka-headless        ClusterIP  None            <none>       9092/TCP                                                                    9m23s
bulging-jaguar-cp-kafka                 ClusterIP  10.108.188.62   <none>       9092/TCP                                                                    9m23s
bulging-jaguar-cp-zookeeper-headless    ClusterIP  None            <none>       2888/TCP,3888/TCP                                                           9m23s
bulging-jaguar-cp-zookeeper             ClusterIP  10.106.138.42   <none>       2181/TCP                                                                    9m23s
bulging-jaguar-openldap                 ClusterIP  10.107.69.145   <none>       389/TCP,636/TCP                                                             9m23s
bulging-jaguar-vdc-atlas-service        NodePort   10.108.223.125  <none>       21000:31000/TCP                                                             9m23s
bulging-jaguar-vdc-egeria-service       NodePort   10.98.17.216    <none>       8080:30080/TCP                                                              9m23s
bulging-jaguar-vdc-gaian-service        NodePort   10.108.113.168  <none>       6414:30414/TCP                                                              9m23s
bulging-jaguar-vdc-igcproxy-service     NodePort   10.102.86.45    <none>       8080:30081/TCP                                                              9m23s
bulging-jaguar-vdc-omrsmonitor-service  NodePort   10.96.30.144    <none>       58080:31080/TCP                                                             9m23s
bulging-jaguar-vdc-postgresql-service   NodePort   10.102.144.50   <none>       5432:30432/TCP,22:32472/TCP                                                 9m23s
bulging-jaguar-vdc-ranger-service       NodePort   10.109.23.244   <none>       6080:32080/TCP,6182:30182/TCP,6083:30126/TCP,6183:30004/TCP,3306:30647/TCP  9m23s
bulging-jaguar-vdc-ui-service           NodePort   10.101.49.46    <none>       8443:30443/TCP                                                              9m23s

==> v1beta2/Deployment
NAME                     DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
bulging-jaguar-openldap  1        1        1           1          9m23s
```

In the example above, everything is started and running (as indicated by the `1/1` and `Running` status for the pods.)

### Removing a chart when finished

To remove the chart when no longer needed, use helm's delete command:

```bash
$ helm delete bulging-jaguar
release "bulging-jaguar" deleted
```

This will ensure the pods and resources are stopped (terminated), and removed from kubernetes,
without needing to know the multiple commands this actually requires through kubernetes itself.
