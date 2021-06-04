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
helm dep update odpi-egeria-lab
helm install lab odpi-egeria-lab
```

Example transcript:

```text
$ export PATH=~/bin:$PATH
$ pwd
/home/jonesn/egeria/open-metadata-resources/open-metadata-deployment/charts
$ helm version
version.BuildInfo{Version:"v3.5.3", GitCommit:"041ce5a2c17a58be0fcd5f5e16fb3e7e95fea622", GitTreeState:"dirty", GoVersion:"go1.16"}
$ helm dep update odpi-egeria-lab
Getting updates for unmanaged Helm repositories...
...Successfully got an update from the "https://charts.bitnami.com/bitnami" chart repository
Saving 1 charts
Downloading kafka from repo https://charts.bitnami.com/bitnami
Deleting outdated charts
```

Now we can actually do the deployment with

```bash
helm install lab odpi-egeria-lab
```

which will look like:

```text
$ helm install lab odpi-egeria-lab                                      [16:52:10]
NAME: lab
LAST DEPLOYED: Tue Apr  6 16:52:17 2021
NAMESPACE: egeria-release
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
ODPi Egeria lab tutorial
---

The Egeria tutorials have now been deployed to Kubernetes.
It may take a minute or so for everything to start up.

Open your web browser and go to addressofmycluster:30888 to
get started

You may need to contact your cluster admin, or read your cloud provider helptext to understand
the correct 'addressofmycluster' to use.

If you experience problems, check memory consumption on your nodes. A minimum of
a 3 node cluster, 2GB per node; or a desktop environment with 6GB dedicated is recommended.

Please provide any feeback via a github issue at https://github.com/odpi/egeria or
join us on slack via https://http://slack.lfai.foundation

- The ODPi Egeria team
```

Note that it can take a few seconds for the various components to all spin-up. You can monitor
the readiness by running `kubectl get all` -- when ready, you should see output like the following:

```text
$ kubectl get all                                                       [16:52:24]
NAME                                                    READY   STATUS    RESTARTS   AGE
pod/lab-odpi-egeria-lab-core-0                          0/1     Running   0          34s
pod/lab-odpi-egeria-lab-datalake-0                      0/1     Running   0          34s
pod/lab-odpi-egeria-lab-dev-0                           0/1     Running   0          34s
pod/lab-odpi-egeria-lab-factory-0                       0/1     Running   0          34s
pod/lab-odpi-egeria-lab-jupyter-bb7cf5f47-k6w2p         1/1     Running   0          34s
pod/lab-odpi-egeria-lab-nginx-69f7cfc67b-nptz5          1/1     Running   0          34s
pod/lab-odpi-egeria-lab-presentation-66574b9796-7t4rc   1/1     Running   0          34s
pod/lab-odpi-egeria-lab-ui-6b4874b5d-6kr74              0/1     Running   0          34s
pod/lab-odpi-egeria-lab-uistatic-6694896d7b-7qj2w       1/1     Running   0          34s
pod/lab-zookeeper-0                                     1/1     Running   0          34s

NAME                                  TYPE           CLUSTER-IP       EXTERNAL-IP                         PORT(S)                      AGE
service/lab-core                      ClusterIP      172.21.163.188   <none>                              9443/TCP                     34s
service/lab-datalake                  ClusterIP      172.21.25.50     <none>                              9443/TCP                     34s
service/lab-dev                       ClusterIP      172.21.19.97     <none>                              9443/TCP                     34s
service/lab-jupyter                   ClusterIP      172.21.200.189   <none>                              8888/TCP                     34s
service/lab-kafka                     ClusterIP      172.21.199.61    <none>                              9092/TCP                     34s
service/lab-kafka-headless            ClusterIP      None             <none>                              9092/TCP,9093/TCP            34s
service/lab-nginx                     ClusterIP      172.21.245.104   <none>                              443/TCP                      34s
service/lab-odpi-egeria-lab-factory   ClusterIP      172.21.112.28    <none>                              9443/TCP                     34s
service/lab-presentation              ClusterIP      172.21.151.140   <none>                              8091/TCP                     34s
service/lab-ui                        ClusterIP      172.21.115.64    <none>                              8443/TCP                     34s
service/lab-uistatic                  ClusterIP      172.21.247.192   <none>                              80/TCP                       34s
service/lab-zookeeper                 ClusterIP      172.21.100.132   <none>                              2181/TCP,2888/TCP,3888/TCP   34s
service/lab-zookeeper-headless        ClusterIP      None             <none>                              2181/TCP,2888/TCP,3888/TCP   34s

NAME                                               READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/lab-odpi-egeria-lab-jupyter        1/1     1            1           34s
deployment.apps/lab-odpi-egeria-lab-nginx          1/1     1            1           34s
deployment.apps/lab-odpi-egeria-lab-presentation   1/1     1            1           34s
deployment.apps/lab-odpi-egeria-lab-ui             0/1     1            0           34s
deployment.apps/lab-odpi-egeria-lab-uistatic       1/1     1            1           34s

NAME                                                          DESIRED   CURRENT   READY   AGE
replicaset.apps/lab-odpi-egeria-lab-jupyter-bb7cf5f47         1         1         1       34s
replicaset.apps/lab-odpi-egeria-lab-nginx-69f7cfc67b          1         1         1       34s
replicaset.apps/lab-odpi-egeria-lab-presentation-66574b9796   1         1         1       34s
replicaset.apps/lab-odpi-egeria-lab-ui-6b4874b5d              1         1         0       34s
replicaset.apps/lab-odpi-egeria-lab-uistatic-6694896d7b       1         1         1       34s

NAME                                            READY   AGE
statefulset.apps/lab-kafka                      0/1     34s
statefulset.apps/lab-odpi-egeria-lab-core       0/1     34s
statefulset.apps/lab-odpi-egeria-lab-datalake   0/1     34s
statefulset.apps/lab-odpi-egeria-lab-dev        0/1     34s
statefulset.apps/lab-odpi-egeria-lab-factory    0/1     34s
statefulset.apps/lab-zookeeper                  1/1     34s```

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

You may wish to expose these cluster IPs via a LoadBalancer. You can find an example of this
in the egeria-base chart README.

You will find the egeria open metadata lab notebooks already populated in the notebook server.
## Starting over

Because the environment is entirely self-contained, you can easily start over the labs simply
by deleting the deployment and running the installation again. This will wipe out all of the
metadata across the lab Egeria servers, remove all messages from the Kafka bus used in the cohort,
reset the Jupyter notebooks to their original clean state, etc.

To delete the deployment, simply run this:

```bash
$ helm delete lab
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

Refer to the existing values file for additional ports in this section that may reflect new components as added

You can then deploy using
`helm install lab odpi-egeria-lab -f lab.yaml` which will override standard defaults with your choices

## Enabling persistence

Support has been added to use persistence in these charts. See 'values.yaml' for more information on this option.
You may also wish to refer to the 'egeria-base' helm chart which is a deployment of a single, persistent, autostart server with UI.

Note however that since this will save the state of your configuration done from
the tutorial notebooks it may be confusing - as such this is disabled by default.

## Using the environment to extend notebooks or develop new ones

  - If you are using a notebook written to assume 'localhost:9443' or similar, replace with the following fragment. This will use the correct defaults for the environment (k8s or compose), or localhost if these are not yet. :
  corePlatformURL     = os.environ.get('corePlatformURL','https://localhost:9443')
  dataLakePlatformURL = os.environ.get('dataLakePlatformURL','https://localhost:9444')
  devPlatformURL      = os.environ.get('devPlatformURL','https://localhost:9445')
  factoryPlatformURL  = os.environ.get('factoryPlatformURL','https://localhost:9446')
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
