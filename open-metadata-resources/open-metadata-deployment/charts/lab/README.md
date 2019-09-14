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

- Kubernetes
- Helm

You could use the Docker-embedded Kubernetes for this on eg. Docker Desktop,
or a public cloud service that provides Kubernetes (and is Helm-compatible).

If you are using the Docker Desktop-embedded Kubernetes, just remember to install
Helm into it as well before following the installation instructions below.

In order to eliminate some challenges with security/permissions in helm2 it's recommended to install
helm3 from https://github.com/helm/helm/releases before starting.

If you can't meet these requirements, an alternative environment for running the tutorials has been implemented using docker-compose & can be found at https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/compose/tutorials .

## Installation

From one directory level above the location of this README, run the following:

```shell script
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm dep update lab
helm install lab --name labs
```

If name is not specified an auto-generated name like `slippery-lizard` will be used with helm v2
For helmv3 use 'helm install lab lab' instead

You should see output similar to the following, indicating that the various
components have been deployed and are starting up:

```text
2019/08/02 15:59:29 Warning: Building values map for chart 'cp-kafka'. Skipped value (map[]) for 'image', as it is not a table.
NAME:   labs
LAST DEPLOYED: Fri Aug  2 15:59:29 2019
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/Deployment
NAME              READY  UP-TO-DATE  AVAILABLE  AGE
labs-egeria-core  0/1    1           0          0s
labs-egeria-dev   0/1    1           0          0s
labs-egeria-lake  0/1    1           0          0s
labs-jupyter      0/1    1           0          0s

==> v1/Pod(related)
NAME                               READY  STATUS             RESTARTS  AGE
labs-cp-kafka-0                    0/1    ContainerCreating  0         0s
labs-cp-zookeeper-0                0/1    Pending            0         0s
labs-egeria-core-869995888d-4hd49  0/1    ContainerCreating  0         0s
labs-egeria-dev-594ddf775b-x7j4m   0/1    ContainerCreating  0         0s
labs-egeria-lake-84fd699864-r9q4g  0/1    ContainerCreating  0         0s
labs-jupyter-796d97f79b-mpzqw      0/1    Pending            0         0s

==> v1/Service
NAME                        TYPE       CLUSTER-IP      EXTERNAL-IP  PORT(S)            AGE
labs-cp-kafka               ClusterIP  10.99.73.44     <none>       9092/TCP           0s
labs-cp-kafka-headless      ClusterIP  None            <none>       9092/TCP           0s
labs-cp-zookeeper           ClusterIP  10.110.167.227  <none>       2181/TCP           0s
labs-cp-zookeeper-headless  ClusterIP  None            <none>       2888/TCP,3888/TCP  0s
labs-egeria-core-service    NodePort   10.109.219.6    <none>       8080:30080/TCP     0s
labs-egeria-dev-service     NodePort   10.96.214.25    <none>       8080:30082/TCP     0s
labs-egeria-lake-service    NodePort   10.110.106.201  <none>       8080:30081/TCP     0s
labs-jupyter-service        NodePort   10.106.212.44   <none>       8888:30888/TCP     0s

==> v1beta1/PodDisruptionBudget
NAME                   MIN AVAILABLE  MAX UNAVAILABLE  ALLOWED DISRUPTIONS  AGE
labs-cp-zookeeper-pdb  N/A            1                0                    0s

==> v1beta1/StatefulSet
NAME               READY  AGE
labs-cp-kafka      0/1    0s
labs-cp-zookeeper  0/1    0s
```

Note that it can take a few seconds for the various components to all spin-up. You can monitor
the readiness by running `kubectl get all` -- when ready, you should see output like the following:

```text
NAME                                    READY   STATUS    RESTARTS   AGE
pod/labs-cp-kafka-0                     1/1     Running   0          76s
pod/labs-cp-zookeeper-0                 1/1     Running   0          76s
pod/labs-egeria-core-869995888d-4hd49   1/1     Running   0          76s
pod/labs-egeria-dev-594ddf775b-x7j4m    1/1     Running   0          76s
pod/labs-egeria-lake-84fd699864-r9q4g   1/1     Running   0          76s
pod/labs-jupyter-796d97f79b-mpzqw       1/1     Running   0          76s

NAME                                 TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)             AGE
service/kubernetes                   ClusterIP   10.96.0.1        <none>        443/TCP             29h
service/labs-cp-kafka                ClusterIP   10.99.73.44      <none>        9092/TCP            76s
service/labs-cp-kafka-headless       ClusterIP   None             <none>        9092/TCP            76s
service/labs-cp-zookeeper            ClusterIP   10.110.167.227   <none>        2181/TCP            76s
service/labs-cp-zookeeper-headless   ClusterIP   None             <none>        2888/TCP,3888/TCP   76s
service/labs-egeria-core-service     NodePort    10.109.219.6     <none>        8080:30080/TCP      76s
service/labs-egeria-dev-service      NodePort    10.96.214.25     <none>        8080:30082/TCP      76s
service/labs-egeria-lake-service     NodePort    10.110.106.201   <none>        8080:30081/TCP      76s
service/labs-jupyter-service         NodePort    10.106.212.44    <none>        8888:30888/TCP      76s

NAME                               READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/labs-egeria-core   1/1     1            1           76s
deployment.apps/labs-egeria-dev    1/1     1            1           76s
deployment.apps/labs-egeria-lake   1/1     1            1           76s
deployment.apps/labs-jupyter       1/1     1            1           76s

NAME                                          DESIRED   CURRENT   READY   AGE
replicaset.apps/labs-egeria-core-869995888d   1         1         1       76s
replicaset.apps/labs-egeria-dev-594ddf775b    1         1         1       76s
replicaset.apps/labs-egeria-lake-84fd699864   1         1         1       76s
replicaset.apps/labs-jupyter-796d97f79b       1         1         1       76s

NAME                                 READY   AGE
statefulset.apps/labs-cp-kafka       1/1     76s
statefulset.apps/labs-cp-zookeeper   1/1     76s
```

(Note that all of the `pod/...` listed at the top have `Running` as their `STATUS` and `1/1` under `READY`.)

At this point you should be able to access your notebook by going to the port listed to the right of
`service/labs-egeria-labs-jupyter-service` -- by default (and in the case above) the port is `30888`:

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

To delete the deployment, simply run:

```shell script
$ helm delete labs --purge
```

Where `labs` is the name you used in your original deployment. (If you left Helm to randomly
generate one, you can see what it's called by first running `helm list` and reviewing the output.)

(Then just re-run the last command in the Installation section above to get a fresh environment.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
