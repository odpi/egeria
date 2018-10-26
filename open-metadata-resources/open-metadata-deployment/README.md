<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# open-metadata-deployment

## Overview

This module contains resources to assist in deployment of Egeria & related components. They are intended to 
facilitate developer useage & technology demonstrations and will need further work to be suitable for production.

Currently the focus is on Helm charts to facilitate deployment to Kubernetes clusters.

Docker images are created independently from this project & hosted on docker hub. 

Helm charts will be reused from other helm repos when available

## Kubernetes install

The easiest way to experiment with kubernetes is to
 * Install Docker for Mac|Windows
 * Go to preferences->kubernetes and ensure 'enable kubernetes' is selected
 * Start/wait for docker and kubernetes to be started
 * For macOS, with HomeBrew, just use 'brew install helm'
 * Initialize helm with 'helm init'
 
 For installing helm on other platforms see: https://docs.helm.sh/using_helm/#installing-helm
 For information on installing HomeBrew on macOS see: https://docs.brew.sh/Installation
 
 The instructions below assume using a local kubernetes. For cloud providers the noticeable differences will be
  * Typically needing to login to the cloud service via the CLI
  * Network can differ - in particular how ports are exposed outside the cluster, and whether you should use 
  NetworkPort, LoadBalancer or other options
  * Once configured use 'kubectl config use-context' to switch between operating with different clouds or local
## Usage

I'll use an example of the 'Egeria' chart here. This is the simplest, and will only deploy the Egeria server, with no
 persistency, and just running OMAG_Server
 
 Make sure your starting directory is here - where you'll see a 'charts' subdirectory which is what helm looks for
### Installing Egeria
 ```
 11:49 $ helm install egeria
 2018/10/26 11:49:02 warning: skipped value for image: Not a table.
 NAME:   ringed-marsupial
 LAST DEPLOYED: Fri Oct 26 11:49:02 2018
 NAMESPACE: default
 STATUS: DEPLOYED
 
 RESOURCES:
 ==> v1/Pod(related)
 NAME                                                        READY  STATUS             RESTARTS  AGE
 ringed-marsupial-egeria-egeria-deployment-56c7dfbc47-shltn  0/1    ContainerCreating  0         0s
 ringed-marsupial-zookeeper-0                                0/1    ContainerCreating  0         0s
 ringed-marsupial-kafka-0                                    0/1    Pending            0         0s
 
 ==> v1/ConfigMap
 NAME                               DATA  AGE
 ringed-marsupial-egeria-configmap  1     1s
 
 ==> v1/Service
 NAME                                 TYPE       CLUSTER-IP      EXTERNAL-IP  PORT(S)                     AGE
 ringed-marsupial-zookeeper-headless  ClusterIP  None            <none>       2181/TCP,3888/TCP,2888/TCP  1s
 ringed-marsupial-zookeeper           ClusterIP  10.104.99.249   <none>       2181/TCP                    1s
 ringed-marsupial-0-external          NodePort   10.100.83.9     <none>       19092:31090/TCP             1s
 ringed-marsupial-1-external          NodePort   10.108.179.201  <none>       19092:31091/TCP             1s
 ringed-marsupial-2-external          NodePort   10.97.176.171   <none>       19092:31092/TCP             0s
 ringed-marsupial-kafka               ClusterIP  10.101.21.3     <none>       9092/TCP                    0s
 ringed-marsupial-kafka-headless      ClusterIP  None            <none>       9092/TCP                    0s
 ringed-marsupial-egeria-service      NodePort   10.97.82.13     <none>       8080:32256/TCP              0s
 
 ==> v1/Deployment
 NAME                                       DESIRED  CURRENT  UP-TO-DATE  AVAILABLE  AGE
 ringed-marsupial-egeria-egeria-deployment  1        1        1           0          0s
 
 ==> v1beta1/StatefulSet
 NAME                        DESIRED  CURRENT  AGE
 ringed-marsupial-zookeeper  3        1        0s
 ringed-marsupial-kafka      3        1        0s
 
 ==> v1beta1/PodDisruptionBudget
 NAME                        MIN AVAILABLE  MAX UNAVAILABLE  ALLOWED DISRUPTIONS  AGE
 ringed-marsupial-zookeeper  N/A            1                0                    0s
 ```
 ### Finding out the network address/IP
```$xslt
11:54 $ kubectl describe service ringed-marsupial-egeria-service
Name:                     ringed-marsupial-egeria-service
Namespace:                default
Labels:                   app.kubernetes.io/component=egeria-service
                          app.kubernetes.io/instance=ringed-marsupial
                          app.kubernetes.io/managed-by=Tiller
                          app.kubernetes.io/name=egeria
                          helm.sh/chart=egeria-0.0.1
Annotations:              <none>
Selector:                 app.kubernetes.io/component=egeria-service,app.kubernetes.io/instance=ringed-marsupial,app.kubernetes.io/name=egeria
Type:                     NodePort
IP:                       10.97.82.13
LoadBalancer Ingress:     localhost
Port:                     ringed-marsupial-egeria-ports  8080/TCP
TargetPort:               8080/TCP
NodePort:                 ringed-marsupial-egeria-ports  32256/TCP
Endpoints:                10.1.0.52:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   <none>
```
This is where kubernetes isn't entirely simple!
* The IP is the internal IP of the pod running the sgeria service - and only accessible within kubernetes. It can 
change constantly as pods are started/stopped/restarted
* TargetPort is the port exposing the service on the pod
* Endpoint is the cluster IP for service. This remains stable and will automatically redirect to the correct pod(s)
* NodePort gives me the external port now exposed - in this case 32256. The interpretation of this varies depending 
on provider - the example here is for Docker on MacOS.
* So http://localhost:32256 would be the baseURL of my egeria service :-)


You can see more detail about other resources using 
'kubectl' (not covered here) 
### Checking status of the helm chart
In the example above, the deployment is called ringed-marsupial so use this:
```
helm status ringed-marsupial
```

If you're unsure which charts are deployed....
### Finding a list of deployed charts
```$xslt
11:49 $ helm list
NAME            	REVISION	UPDATED                 	STATUS  	CHART       	NAMESPACE
ringed-marsupial	1       	Fri Oct 26 11:49:02 2018	DEPLOYED	egeria-0.0.1	default
```
### removing this chart when finished
```$xslt
11:46 $ helm delete ringed-marsupial
release "ringed-marsupial" deleted
```

 
 
 
 
 
 
 

tbd
