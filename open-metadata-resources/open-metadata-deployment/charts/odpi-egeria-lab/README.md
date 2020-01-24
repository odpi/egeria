<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Chart for experimenting with the Egeria labs

This Helm chart will deploy the following components, all in a self-contained environment,
for use in the Egeria hands-on labs -- allowing you to explore Egeria and its concepts safely
and repeatably:

- Multiple Egeria servers
- Apache Kafka (and its Zookeeper dependency)
- Jupyter Notebooks

## Prerequisites

In order to use the labs, you'll first need to have the following installed:

- Kubernetes 1.15 or above
- Helm 3.0 or above

The minimum tested configurations for Kubernetes are
 - Cloud/remote service - 3 nodes, 2GB ram per node
 - Local docker for mac/windows - 1 node, 6Gb ram dedicated to docker

You could use the Docker-embedded Kubernetes for this on eg. Docker Desktop,
or a public cloud service that provides Kubernetes 

If you need to install helm3, please obtain from https://github.com/helm/helm/releases before starting and 
ensure the 'helm' executable is in your PATH. The
instructions and examples that follow assume use of this version

If you can't meet these requirements, or would like to start with a simpler approach,
an alternative environment for running the tutorials has been implemented using docker-compose & can be found at https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/compose/tutorials .

## Installation

From one directory level above the location of this README, run the following:

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm dep update odpi-egeria-lab
helm install lab odpi-egeria-lab
```

Example transcript:

```text
$ export PATH=~/bin:$PATH
$ pwd
/home/jonesn/tmp/egeria/open-metadata-resources/open-metadata-deployment/charts
$ helm version
version.BuildInfo{Version:"v3.0.0-beta.3", GitCommit:"5cb923eecbe80d1ad76399aee234717c11931d9a", GitTreeState:"clean", GoVersion:"go1.12.9"}
$ helm repo add bitnami https://charts.bitnami.com/bitnami
"bitnami" has been added to your repositories
$ helm repo update
Hang tight while we grab the latest from your chart repositories...
...Successfully got an update from the "bitnami" chart repository
Update Complete. ⎈ Happy Helming!⎈
$ helm dep update odpi-egeria-lab
Hang tight while we grab the latest from your chart repositories...
...Successfully got an update from the "bitnami" chart repository
Update Complete. ⎈Happy Helming!⎈
Saving 1 charts
Downloading kafka from repo https://charts.bitnami.com/bitnami
Deleting outdated chart
```

Now we can actually do the deployment with

```bash
helm install lab odpi-egeria-lab
```

which will look like:

```text
$ helm install lab odpi-egeria-lab
NAME: lab
LAST DEPLOYED: Mon Nov 11 18:43:57 2019
NAMESPACE: egeria
STATUS: deployed
REVISION: 1
TEST SUITE: None
```

Note that it can take a few seconds for the various components to all spin-up. You can monitor
the readiness by running `kubectl get all` -- when ready, you should see output like the following:

```text
$ kubectl get all
NAME                                                READY   STATUS    RESTARTS   AGE
pod/lab-kafka-0                                     1/1     Running   1          83s
pod/lab-odpi-egeria-lab-core-76c755dc67-ssg4h       1/1     Running   0          82s
pod/lab-odpi-egeria-lab-datalake-5457897c6c-tj4bm   1/1     Running   0          82s
pod/lab-odpi-egeria-lab-dev-75f57db499-jtlnh        1/1     Running   0          82s
pod/lab-odpi-egeria-lab-factory-c6dcf59c8-ln7wc     1/1     Running   0          82s
pod/lab-odpi-egeria-lab-jupyter-64599d9bf8-6vmw4    1/1     Running   0          82s
pod/lab-odpi-egeria-lab-ui-6768fdf5df-5lr7b         1/1     Running   0          82s
pod/lab-zookeeper-0                                 1/1     Running   0          83s

NAME                                   TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
service/lab-kafka                      ClusterIP   172.21.169.174   <none>        9092/TCP                     83s
service/lab-kafka-headless             ClusterIP   None             <none>        9092/TCP                     83s
service/lab-odpi-egeria-lab-core       NodePort    172.21.194.126   <none>        8080:30080/TCP               83s
service/lab-odpi-egeria-lab-datalake   NodePort    172.21.71.217    <none>        8080:30081/TCP               83s
service/lab-odpi-egeria-lab-dev        NodePort    172.21.233.139   <none>        8080:30082/TCP               83s
service/lab-odpi-egeria-lab-factory    NodePort    172.21.1.162     <none>        8080:30083/TCP               83s
service/lab-odpi-egeria-lab-jupyter    NodePort    172.21.35.112    <none>        8888:30888/TCP               83s
service/lab-odpi-egeria-lab-ui         NodePort    172.21.43.217    <none>        8443:30443/TCP               83s
service/lab-zookeeper                  ClusterIP   172.21.242.102   <none>        2181/TCP,2888/TCP,3888/TCP   83s
service/lab-zookeeper-headless         ClusterIP   None             <none>        2181/TCP,2888/TCP,3888/TCP   83s

NAME                                           READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/lab-odpi-egeria-lab-core       1/1     1            1           84s
deployment.apps/lab-odpi-egeria-lab-datalake   1/1     1            1           84s
deployment.apps/lab-odpi-egeria-lab-dev        1/1     1            1           84s
deployment.apps/lab-odpi-egeria-lab-factory    1/1     1            1           84s
deployment.apps/lab-odpi-egeria-lab-jupyter    1/1     1            1           84s
deployment.apps/lab-odpi-egeria-lab-ui         1/1     1            1           84s

NAME                                                      DESIRED   CURRENT   READY   AGE
replicaset.apps/lab-odpi-egeria-lab-core-76c755dc67       1         1         1       84s
replicaset.apps/lab-odpi-egeria-lab-datalake-5457897c6c   1         1         1       84s
replicaset.apps/lab-odpi-egeria-lab-dev-75f57db499        1         1         1       84s
replicaset.apps/lab-odpi-egeria-lab-factory-c6dcf59c8     1         1         1       84s
replicaset.apps/lab-odpi-egeria-lab-jupyter-64599d9bf8    1         1         1       84s
replicaset.apps/lab-odpi-egeria-lab-ui-6768fdf5df         1         1         1       83s

NAME                             READY   AGE
statefulset.apps/lab-kafka       1/1     84s
statefulset.apps/lab-zookeeper   1/1     84s

NAME                                                       READY   REASON   AGE
clusterchannelprovisioner.eventing.knative.dev/in-memory   True             49d
```

(Note that all of the `pod/...` listed at the top have `Running` as their `STATUS` and `1/1` under `READY`.)

At this point you should be able to access your notebook by going to the port listed to the right of
`service/lab-jupyter` -- by default (and in the case above) the port is `30888`:

For example, if running Kubernetes locally on your machine, you should be able to get to the notebook
with:

```text
http://localhost:30888
```

If you are using a cloud service, you will need to know what external ip address or name is exposed. This may be called 'Ingress Domain' or similar, but will typically not be seen in the output above. For example:

```
http://mycluster.mydomain.mycloud.com:30888
```

You will find the egeria open metadata lab notebooks already populated in the notebook server.
## Starting over

Because the environment is entirely self-contained, you can easily start over the labs simply
by deleting the deployment and running the installation again. This will wipe out all of the
metadata across the lab Egeria servers, remove all messages from the Kafka bus used in the cohort,
reset the Jupyter notebooks to their original clean state, etc.

To delete the deployment, simply run this for Helm3:

```bash
$ helm delete lab
```

Or if using Helm2:

```bash
helm delete lab --purge
```

Where `lab` is the name you used in your original deployment. (You can see what it's called by first running `helm list` and reviewing the output.)

(Then just re-run the last command in the Installation section above to get a fresh environment.)
## Port Clashes
The chart is configured to use a fixed set of ports and expose them using a 'NodePort' service as described above.

You may find you clash with other services setup in your cluster. If so you can override the ports by creating a file such as `lab.yaml` with the following contents:

```
service:
  type: NodePort
  nodeport:
    jupyter: 30888
    core: 30080
    datalake: 30081
    dev: 30082
    factory: 30083
    ui: 30443
```

and then change the port numbers accordingly.
You can then deploy using
`helm install lab odpi-egeria-lab -f lab.yaml` which will override standard defaults with your choices

## Using the environment to extend notebooks or develop new ones

  - If you are using a notebook written to assume 'localhost:8080' or similar, replace with the following fragment. This will use the correct defaults for the environment (k8s or compose), or localhost if these are not yet. :
  corePlatformURL     = os.environ.get('corePlatformURL','http://localhost:8080')
  dataLakePlatformURL = os.environ.get('dataLakePlatformURL','http://localhost:8081')
  devPlatformURL      = os.environ.get('devPlatformURL','http://localhost:8082')
  factoryPlatformURL  = os.environ.get('factoryPlatformURL','http://localhost:8083')
 - The notebooks downloaded from git are refreshed on each start. Ensure any modifications to notebooks are saved elsewhere
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
