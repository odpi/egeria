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
or a public cloud service that provides Kubernetes 

In order to eliminate some challenges with security/permissions in helm2 it's recommended to install
helm3 from https://github.com/helm/helm/releases before starting and to ensure the 'helm' executable is in your PATH. The
instructions and examples that follow assume use of this version

If you can't meet these requirements, or would like to start with a simpler approach,
an alternative environment for running the tutorials has been implemented using docker-compose & can be found at https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/compose/tutorials .

## Installation

From one directory level above the location of this README, run the following:

```shell script
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
helm dep update lab
helm install lab lab
```
Example transcript:
```text
➜  export PATH=~/bin:$PATH
➜  pwd
/home/jonesn/tmp/egeria/open-metadata-resources/open-metadata-deployment/charts
➜  helm version
version.BuildInfo{Version:"v3.0.0-beta.3", GitCommit:"5cb923eecbe80d1ad76399aee234717c11931d9a", GitTreeState:"clean", GoVersion:"go1.12.9"}
➜  helm repo add bitnami https://charts.bitnami.com/bitnami
"bitnami" has been added to your repositories
➜  helm repo update
Hang tight while we grab the latest from your chart repositories...
...Successfully got an update from the "bitnami" chart repository
Update Complete. ⎈ Happy Helming!⎈
➜  helm dep update lab
Hang tight while we grab the latest from your chart repositories...
...Successfully got an update from the "bitnami" chart repository
Update Complete. ⎈Happy Helming!⎈
Saving 1 charts
Downloading kafka from repo https://charts.bitnami.com/bitnami
Deleting outdated chart
```
Now we can actually do the deployment with
```shell script
helm install lab lab
```
which will look like:
```text
➜  helm install lab lab                                                                                  <<<
NAME: lab
LAST DEPLOYED: 2019-09-14 15:12:35.663861002 +0000 UTC m=+0.131192396
NAMESPACE: egeria
STATUS: deployed
```
Note that it can take a few seconds for the various components to all spin-up. You can monitor
the readiness by running `kubectl get all` -- when ready, you should see output like the following:
```text
➜ kubectl get all
NAME                                READY   STATUS    RESTARTS   AGE
pod/lab-core-7cf97db498-jp5ql       1/1     Running   0          68s
pod/lab-datalake-78984678f4-lw77s   1/1     Running   0          68s
pod/lab-dev-7d9bbbc686-4mnhl        1/1     Running   0          68s
pod/lab-jupyter-664c8595bd-nc2rq    1/1     Running   0          68s
pod/lab-kafka-0                     1/1     Running   1          68s
pod/lab-ui-866b5796d9-98npz         1/1     Running   0          68s
pod/lab-zookeeper-0                 1/1     Running   0          68s

NAME                             TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                      AGE
service/lab-core                 NodePort    172.21.33.108    <none>        8080:30080/TCP               69s
service/lab-datalake             NodePort    172.21.200.243   <none>        8080:30081/TCP               69s
service/lab-dev                  NodePort    172.21.146.166   <none>        8080:30082/TCP               69s
service/lab-jupyter              NodePort    172.21.226.53    <none>        8888:30888/TCP               69s
service/lab-kafka                ClusterIP   172.21.248.164   <none>        9092/TCP                     69s
service/lab-kafka-headless       ClusterIP   None             <none>        9092/TCP                     69s
service/lab-ui                   NodePort    172.21.6.119     <none>        8443:30443/TCP               69s
service/lab-zookeeper            ClusterIP   172.21.249.121   <none>        2181/TCP,2888/TCP,3888/TCP   69s
service/lab-zookeeper-headless   ClusterIP   None             <none>        2181/TCP,2888/TCP,3888/TCP   69s

NAME                           READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/lab-core       1/1     1            1           69s
deployment.apps/lab-datalake   1/1     1            1           69s
deployment.apps/lab-dev        1/1     1            1           68s
deployment.apps/lab-jupyter    1/1     1            1           68s
deployment.apps/lab-ui         1/1     1            1           68s

NAME                                      DESIRED   CURRENT   READY   AGE
replicaset.apps/lab-core-7cf97db498       1         1         1       69s
replicaset.apps/lab-datalake-78984678f4   1         1         1       68s
replicaset.apps/lab-dev-7d9bbbc686        1         1         1       68s
replicaset.apps/lab-jupyter-664c8595bd    1         1         1       68s
replicaset.apps/lab-ui-866b5796d9         1         1         1       68s

NAME                             READY   AGE
statefulset.apps/lab-kafka       1/1     68s
statefulset.apps/lab-zookeeper   1/1     68s
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

```shell script
$ helm delete lab
```
Or if using Helm2:
```shell script
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
    ui: 30443
```
and then change the port numbers accordingly.
You can then deploy using
'helm install lab lab -f lab.yaml' which will override standard defaults with your choices

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
