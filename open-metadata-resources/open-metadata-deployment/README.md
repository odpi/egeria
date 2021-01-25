<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Containerized deployment of ODPi Egeria

This module contains resources to assist in deployment of Egeria and related components. They are intended to
facilitate developer usage and technology demonstrations and will need further work to be suitable for production.

Currently the focus is on Helm charts to facilitate deployment to Kubernetes clusters.

Docker images are created independently from the Helm deployment, and where possible we plan to host them on Docker Hub.
(You can also build most of these images directly, and host them in your own registry, following the instructions under
the `docker` directory.)

## Kubernetes install

**Note**: before following the steps below, ideally ensure that any pre-existing `kubectl`
configuration is moved (or removed), eg. from HomeBrew, as it may conflict with the one being
configured by Docker in the steps below.
(See https://docs.docker.com/docker-for-mac/#kubernetes)

The easiest way to experiment with kubernetes is to

1. Install Docker for Mac | Windows.
1. Go to `Preferences...` -> `Kubernetes` and ensure `Enable Kubernetes` is selected.
1. Start / wait for docker and kubernetes to be started.
1. Install Helm: for macOS (with [HomeBrew](https://brew.sh)) just use `brew install kubernetes-helm`.
1. Initialize helm with `helm init`.

For installing helm on other platforms see: https://docs.helm.sh/using_helm/#installing-helm

The instructions below assume using a local kubernetes. For cloud providers the noticeable differences will be:

- Typically needing to login to the cloud service via the CLI.
- Network can differ - in particular how ports are exposed outside the cluster, and whether you should use NetworkPort,
    LoadBalancer or other options.
- Once configured, use `kubectl config use-context` to switch between operating with different clouds or local.

## Helm configuration

To make use of Helm and its charts, first run through the following steps to ensure the charts we
want to make use of will be available.

### Add chart repositories

Start by adding the chart repositories we will need with the following helm command:

```bash
$ helm repo add bitnami https://charts.bitnami.com/bitnami
"bitnami" has been added to your repositories
```

This repository is needed for the Kafka chart on which our deployment depends.

Then update your local repository index with the following command:

```bash
$ helm repo update
Hang tight while we grab the latest from your chart repositories...
...Skip local chart repository
...Successfully got an update from the "bitnami" chart repository
...Successfully got an update from the "stable" chart repository
Update Complete. * Happy Helming!*
```

## Usage

### Configuring environment-specific values

By default, the chart will deploy everything it requires into your k8s cluster, including Zookeeper, Kafka, etc. You
may already have such components available, however, that you want to re-use. You can therefore override these so that
they are not deployed as part of the cluster but simply re-use your existing resources.

To simplify various configuration scenarios, you can put each of these options into its own separate YAML file: you can
then run the helm installation step passing multiple such files to suit your specific configuration requirements for
that deployment.

#### Apache Kafka

The following properties will disable the internally-deployed Kafka (and Zookeeper) components, and instead make use of
a pre-existing Apache Kafka (with Zookeeper) environment:

```yaml
kafka:
  internal:
    enabled: false
  external:
    hostname: "my.host.somewhere.com"
    ip: "1.2.3.4"
    port: "9092"
```

When `kafka.internal.enabled` is set to `true` (as it is by default), the options under `external` are simply ignored.

----
* Return to [open-metadata-resources](..)
* Return to [Site Map](../../Content-Organization.md)
* Return to [Home Page](../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.