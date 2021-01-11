<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Simple helm chart for Kubernetes deployment of Egeria

This is a simple deployment of Egeria which will just deploy

  - A single egeria platform
  - A single egeria metadata server with a persistent graph store
  - Apache Kafka (and its Zookeeper dependency)

## Prerequisites

In order to use the labs, you'll first need to have the following installed:

  - A Kubernetes cluster at 1.15 or above
  - the `kubectl` tool in your path
  - [Helm](https://github.com/helm/helm/releases) 3.0 or above



## Installation

From one directory level above the location of this README, run the following:

```bash

helm dep update odpi-egeria-base
helm install egeria odpi-egeria-base
```

### Additional Install Configuration

### Example transcript

## Accessing Egeria 

If you are using a cloud service, you will need to know what external ip address or name is exposed. This may be called 'Ingress Domain' or similar, but will typically not be seen in the output above. For example:

```
http://mycluster.mydomain.mycloud.com:30888
```

## Cleaning up / removing Egeria

To delete the deployment, simply run this for Helm3:

```bash
$ helm delete lab
```


License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
