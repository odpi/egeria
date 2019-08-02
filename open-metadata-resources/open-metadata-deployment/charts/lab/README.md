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

## Installation

From one directory level above the location of this README, run the following:

```shell script
$ helm repo add confluentinc https://confluentinc.github.io/cp-helm-charts/

"confluentinc" has been added to your repositories

$ helm repo update

Hang tight while we grab the latest from your chart repositories...
...Skip local chart repository
...Successfully got an update from the "confluentinc" chart repository
...Successfully got an update from the "stable" chart repository
Update Complete.

$ helm dependency update lab

Hang tight while we grab the latest from your chart repositories...
...Unable to get an update from the "local" chart repository (http://127.0.0.1:8879/charts):
	Get http://127.0.0.1:8879/charts/index.yaml: dial tcp 127.0.0.1:8879: connect: connection refused
...Successfully got an update from the "confluentinc" chart repository
...Successfully got an update from the "stable" chart repository
Update Complete.
Saving 1 charts
Downloading cp-helm-charts from repo https://confluentinc.github.io/cp-helm-charts/
Deleting outdated charts

$ helm install lab --name labs
```

Note: If name is not specified an auto-generated name like `slippery-lizard` will be used.

You should see output similar to the following, indicating that the various
components have been deployed and are running:

```text

```

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

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
