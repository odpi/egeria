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
  hostname: infosvr.vagrant.ibm.com
  webport: 9446
  auth:
    os:
      user: vagrant
      pass: something
    app:
      user: isadmin
      pass: isadmin
service:
  externalip: 192.168.1.175
```

These values capture the following:

- `igc.host`: the hostname or IP address of your pre-existing IBM InfoSphere Information Governance Catalog environment's
    domain (services) tier
- `igc.hostname`: the hostname of your pre-existing IBM InfoSphere Information Governance Catalog environment's domain
    (services) tier
- `igc.webport`: the port on which the domain (services) tier console (and API) is available
- `igc.auth.os.user`: the OS-level user through which to access your pre-existing IGC environment
- `igc.auth.os.pass`: the password for the OS-level user on the pre-existing IGC environment
- `igc.auth.app.user`: the application-level user through which to access the pre-existing IGC environment
    (typically `isadmin`)
- `igc.auth.app.pass`: the password for the application-level user on the pre-existing IGC environment
- `service.externalip`: the hostname or IP address of your kubernetes worker nodes (for NodePort services)

(The two IP addresses / hosts must be network-accessible to each other, but could be on the same private network as in
the example above.)

### Deploying the demonstration

Navigate back up to the `charts` directory, and run the following command (providing the name of the file you configured
in the step immediately above, and any unique name you like for the `--name` parameter):

```bash
$ helm install vdc -f vars.yml --name local
2019/02/08 09:57:36 Warning: Building values map for chart 'cp-kafka'. Skipped value (map[]) for 'image', as it is not a table.
NAME:   local
E0208 09:57:37.967114   82959 portforward.go:303] error copying from remote stream to local connection: readfrom tcp4 127.0.0.1:51672->127.0.0.1:51674: write tcp4 127.0.0.1:51672->127.0.0.1:51674: write: broken pipe
LAST DEPLOYED: Fri Feb  8 09:57:36 2019
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1beta1/PodDisruptionBudget
NAME                    MIN AVAILABLE  MAX UNAVAILABLE  ALLOWED DISRUPTIONS  AGE
local-cp-zookeeper-pdb  N/A            1                0                    1s

==> v1/Secret
NAME            TYPE    DATA  AGE
local-openldap  Opaque  2     1s

==> v1/ConfigMap
NAME                              DATA  AGE
local-cp-kafka-jmx-configmap      1     1s
local-cp-zookeeper-jmx-configmap  1     1s
local-openldap-env                6     1s
local-vdc-configmap               28    1s

==> v1/Service
NAME                           TYPE       CLUSTER-IP      EXTERNAL-IP  PORT(S)                                                                     AGE
local-cp-kafka-headless        ClusterIP  None            <none>       9092/TCP                                                                    1s
local-cp-kafka                 ClusterIP  10.106.142.155  <none>       9092/TCP                                                                    1s
local-cp-zookeeper-headless    ClusterIP  None            <none>       2888/TCP,3888/TCP                                                           1s
local-cp-zookeeper             ClusterIP  10.109.88.78    <none>       2181/TCP                                                                    1s
local-openldap                 ClusterIP  10.97.145.22    <none>       389/TCP,636/TCP                                                             1s
local-vdc-atlas-service        NodePort   10.107.206.23   <none>       21000:31000/TCP                                                             1s
local-vdc-egeria-service       NodePort   10.103.23.234   <none>       8080:30080/TCP                                                              1s
local-vdc-gaian-service        NodePort   10.99.183.202   <none>       6414:30414/TCP                                                              1s
local-vdc-igcproxy-service     NodePort   10.96.13.54     <none>       8080:30081/TCP                                                              1s
local-vdc-omrsmonitor-service  NodePort   10.96.170.162   <none>       58080:31080/TCP                                                             1s
local-vdc-postgresql-service   NodePort   10.103.139.227  <none>       5432:30432/TCP,22:30471/TCP                                                 1s
local-vdc-ranger-service       NodePort   10.97.32.163    <none>       6080:32080/TCP,6182:30182/TCP,6083:31682/TCP,6183:32006/TCP,3306:31734/TCP  1s
local-vdc-ui-service           NodePort   10.97.225.131   <none>       8443:30443/TCP                                                              1s

==> v1beta2/Deployment
NAME            DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
local-openldap  1        1        1           0          1s

==> v1/Deployment
NAME                                DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
local-vdc-atlas-deployment          1        1        1           0          1s
local-vdc-egeria-deployment         1        1        1           0          1s
local-vdc-gaian-deployment          1        1        1           0          1s
local-vdc-igcproxy-deployment       1        0        0           0          1s
local-vdc-kafka-monitor-deployment  1        1        1           0          1s
local-vdc-omrsmonitor-deployment    1        1        1           0          1s
local-vdc-postgresql-deployment     1        0        0           0          1s
local-vdc-rangeradmin               1        0        0           0          1s
local-vdc-ui-deployment             1        0        0           0          1s

==> v1beta1/StatefulSet
NAME                DESIRED  CURRENT  AGE
local-cp-kafka      3        1        1s
local-cp-zookeeper  3        1        1s

==> v1/Pod(related)
NAME                                                READY  STATUS             RESTARTS  AGE
local-openldap-55c8cf6cc-tncd7                      0/1    ContainerCreating  0         1s
local-vdc-atlas-deployment-6b78d7b8d4-qbxf4         0/2    ContainerCreating  0         1s
local-vdc-egeria-deployment-5cf76f8555-86fqp        0/2    ContainerCreating  0         1s
local-vdc-gaian-deployment-677d59fbb5-rtcht         0/1    ContainerCreating  0         1s
local-vdc-igcproxy-deployment-57b675dbc5-7jt2d      0/2    ContainerCreating  0         1s
local-vdc-kafka-monitor-deployment-cdfdc5975-5cf46  0/1    ContainerCreating  0         1s
local-vdc-omrsmonitor-deployment-696dd5d778-9wdx4   0/1    Pending            0         1s
local-vdc-postgresql-deployment-6969bddc64-scvhw    0/2    Pending            0         0s
local-vdc-rangeradmin-c68dc7c4b-8kw98               0/2    Pending            0         0s
local-vdc-ui-deployment-699b56b8ff-6b24d            0/1    Pending            0         0s
local-cp-kafka-0                                    0/2    Pending            0         1s
local-cp-zookeeper-0                                0/2    Pending            0         0s
```

Note that the value you provide for the `--name` parameter becomes a meaningful prefix to the various components you
are deploying.

### Finding out the network address/IP

The simplest method for determining the network address and IPs that kubernetes is exposing
for external access is its `get services` command:

```bash
$ kubectl get services
NAME                                     TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                                                                      AGE
NAME                            TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                                                                      AGE
kubernetes                      ClusterIP   10.96.0.1        <none>        443/TCP                                                                      2d
local-cp-kafka                  ClusterIP   10.106.142.155   <none>        9092/TCP                                                                     42s
local-cp-kafka-headless         ClusterIP   None             <none>        9092/TCP                                                                     42s
local-cp-zookeeper              ClusterIP   10.109.88.78     <none>        2181/TCP                                                                     42s
local-cp-zookeeper-headless     ClusterIP   None             <none>        2888/TCP,3888/TCP                                                            42s
local-openldap                  ClusterIP   10.97.145.22     <none>        389/TCP,636/TCP                                                              42s
local-vdc-atlas-service         NodePort    10.107.206.23    <none>        21000:31000/TCP                                                              42s
local-vdc-egeria-service        NodePort    10.103.23.234    <none>        8080:30080/TCP                                                               42s
local-vdc-gaian-service         NodePort    10.99.183.202    <none>        6414:30414/TCP                                                               42s
local-vdc-igcproxy-service      NodePort    10.96.13.54      <none>        8080:30081/TCP                                                               42s
local-vdc-omrsmonitor-service   NodePort    10.96.170.162    <none>        58080:31080/TCP                                                              42s
local-vdc-postgresql-service    NodePort    10.103.139.227   <none>        5432:30432/TCP,22:30471/TCP                                                  42s
local-vdc-ranger-service        NodePort    10.97.32.163     <none>        6080:32080/TCP,6182:30182/TCP,6083:31682/TCP,6183:32006/TCP,3306:31734/TCP   42s
local-vdc-ui-service            NodePort    10.97.225.131    <none>        8443:30443/TCP                                                               42s
```

From the list above you can see that the Egeria service (`local-vdc-egeria-service`) is available
externally on port `30080`.

More detail can be obtained about any particular service using the `describe service` command:

```bash
$ kubectl describe service local-vdc-egeria-service
Name:                     local-vdc-egeria-service
Namespace:                default
Labels:                   app.kubernetes.io/component=egeria-service
                          app.kubernetes.io/instance=local
                          app.kubernetes.io/managed-by=Tiller
                          app.kubernetes.io/name=vdc
                          helm.sh/chart=vdc-0.1.10
Annotations:              <none>
Selector:                 app.kubernetes.io/component=egeria-service,app.kubernetes.io/instance=local,app.kubernetes.io/name=vdc
Type:                     NodePort
IP:                       10.103.23.234
LoadBalancer Ingress:     localhost
Port:                     <unset>  8080/TCP
TargetPort:               8080/TCP
NodePort:                 <unset>  30080/TCP
Endpoints:                10.1.0.98:8080
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
NAME 	REVISION	UPDATED                 	STATUS  	CHART     	APP VERSION	NAMESPACE
local	1       	Fri Feb  8 09:57:36 2019	DEPLOYED	vdc-0.1.10	           	default
```

### Checking the status of a helm chart

Recall that in the examples above, we named our deployment `local` (as also seen when using the `list` command).
To check the status of the deployment (ie. that everything is up and running, as it may take a
few minutes for everything to startup), use this command:

```bash
$ helm status local
LAST DEPLOYED: Fri Feb  8 09:57:36 2019
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/Deployment
NAME                                DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
local-vdc-atlas-deployment          1        1        1           1          2m59s
local-vdc-egeria-deployment         1        1        1           1          2m59s
local-vdc-gaian-deployment          1        1        1           1          2m59s
local-vdc-igcproxy-deployment       1        1        1           1          2m59s
local-vdc-kafka-monitor-deployment  1        1        1           1          2m59s
local-vdc-omrsmonitor-deployment    1        1        1           1          2m59s
local-vdc-postgresql-deployment     1        1        1           1          2m59s
local-vdc-rangeradmin               1        1        1           1          2m59s
local-vdc-ui-deployment             1        1        1           1          2m59s

==> v1beta1/StatefulSet
NAME                DESIRED  CURRENT  AGE
local-cp-kafka      3        3        2m59s
local-cp-zookeeper  3        3        2m59s

==> v1/Pod(related)
NAME                                                READY  STATUS   RESTARTS  AGE
local-openldap-55c8cf6cc-tncd7                      1/1    Running  0         2m59s
local-vdc-atlas-deployment-6b78d7b8d4-qbxf4         2/2    Running  0         2m59s
local-vdc-egeria-deployment-5cf76f8555-86fqp        2/2    Running  0         2m59s
local-vdc-gaian-deployment-677d59fbb5-rtcht         1/1    Running  0         2m59s
local-vdc-igcproxy-deployment-57b675dbc5-7jt2d      2/2    Running  0         2m59s
local-vdc-kafka-monitor-deployment-cdfdc5975-5cf46  1/1    Running  0         2m59s
local-vdc-omrsmonitor-deployment-696dd5d778-9wdx4   1/1    Running  0         2m59s
local-vdc-postgresql-deployment-6969bddc64-scvhw    2/2    Running  0         2m58s
local-vdc-rangeradmin-c68dc7c4b-8kw98               2/2    Running  0         2m58s
local-vdc-ui-deployment-699b56b8ff-6b24d            1/1    Running  0         2m58s
local-cp-kafka-0                                    2/2    Running  0         2m59s
local-cp-kafka-1                                    2/2    Running  0         2m30s
local-cp-kafka-2                                    2/2    Running  0         2m10s
local-cp-zookeeper-0                                2/2    Running  0         2m58s
local-cp-zookeeper-1                                2/2    Running  0         2m31s
local-cp-zookeeper-2                                2/2    Running  0         2m4s

==> v1beta1/PodDisruptionBudget
NAME                    MIN AVAILABLE  MAX UNAVAILABLE  ALLOWED DISRUPTIONS  AGE
local-cp-zookeeper-pdb  N/A            1                1                    2m59s

==> v1/Secret
NAME            TYPE    DATA  AGE
local-openldap  Opaque  2     2m59s

==> v1/ConfigMap
NAME                              DATA  AGE
local-cp-kafka-jmx-configmap      1     2m59s
local-cp-zookeeper-jmx-configmap  1     2m59s
local-openldap-env                6     2m59s
local-vdc-configmap               28    2m59s

==> v1/Service
NAME                           TYPE       CLUSTER-IP      EXTERNAL-IP  PORT(S)                                                                     AGE
local-cp-kafka-headless        ClusterIP  None            <none>       9092/TCP                                                                    2m59s
local-cp-kafka                 ClusterIP  10.106.142.155  <none>       9092/TCP                                                                    2m59s
local-cp-zookeeper-headless    ClusterIP  None            <none>       2888/TCP,3888/TCP                                                           2m59s
local-cp-zookeeper             ClusterIP  10.109.88.78    <none>       2181/TCP                                                                    2m59s
local-openldap                 ClusterIP  10.97.145.22    <none>       389/TCP,636/TCP                                                             2m59s
local-vdc-atlas-service        NodePort   10.107.206.23   <none>       21000:31000/TCP                                                             2m59s
local-vdc-egeria-service       NodePort   10.103.23.234   <none>       8080:30080/TCP                                                              2m59s
local-vdc-gaian-service        NodePort   10.99.183.202   <none>       6414:30414/TCP                                                              2m59s
local-vdc-igcproxy-service     NodePort   10.96.13.54     <none>       8080:30081/TCP                                                              2m59s
local-vdc-omrsmonitor-service  NodePort   10.96.170.162   <none>       58080:31080/TCP                                                             2m59s
local-vdc-postgresql-service   NodePort   10.103.139.227  <none>       5432:30432/TCP,22:30471/TCP                                                 2m59s
local-vdc-ranger-service       NodePort   10.97.32.163    <none>       6080:32080/TCP,6182:30182/TCP,6083:31682/TCP,6183:32006/TCP,3306:31734/TCP  2m59s
local-vdc-ui-service           NodePort   10.97.225.131   <none>       8443:30443/TCP                                                              2m59s

==> v1beta2/Deployment
NAME            DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
local-openldap  1        1        1           1          2m59s
```

In the example above, everything is started and running (as indicated by the `1/1` and `Running` status for the pods.)

### Removing a chart when finished

To remove the chart when no longer needed, use helm's delete command:

```bash
$ helm delete local
release "local" deleted
$ helm del --purge local
release "local" deleted
```

This will ensure the pods and resources are stopped (terminated), and removed from kubernetes,
without needing to know the multiple commands this actually requires through kubernetes itself.

(Note that you need to use the `--purge` option to purge the chart if you intend to use the same name at some point in
the future.)
