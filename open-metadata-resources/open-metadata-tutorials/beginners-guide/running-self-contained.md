<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Running Egeria in a self-contained environment

The following technologies can be used to run Egeria and its tutorials in an entirely self-contained environment.
With this approach, you do not need to download, install, configure and understand all of the underlying components
that Egeria itself uses.

Simply using Docker you can get up-and-running with Egeria (and our tutorials) without any other software downloads
or installations.

## Docker

[Docker](https://www.docker.com) is a platform for developing and running "containers", which for extreme
over-simplification can be thought of as lightweight virtual machines.

As part of the Egeria project, we make a number of pre-built Docker containers available as well as re-using
other projects' containers: these have all of the software you need (like the JRE, Jupyter, Zookeeper, Kafka, and
Egeria itself) pre-installed within them. Using these, you only need to run a single command in order to download
and run all of the pieces necessary to work through the tutorials -- the only piece of software you need to download
and install yourself is Docker.

Docker Compose is a utility within the ecosystem that for running multiple containers together in a coordinated
way (for example, all accessible to each other over the same private network).

Docker (and Compose) can be installed:

- On Windows and MacOS, by [downloading and installing Docker Desktop](https://hub.docker.com/?overlay=onboarding)
(which includes Docker Compose).
- On Linux operating systems, by following the appropriate instructions for [CentOS](https://docs.docker.com/v17.12/install/linux/docker-ce/centos/),
[Debian](https://docs.docker.com/v17.12/install/linux/docker-ce/debian/), [Fedora](https://docs.docker.com/v17.12/install/linux/docker-ce/fedora/),
or [Ubuntu](https://docs.docker.com/v17.12/install/linux/docker-ce/ubuntu/). If you do not want to use Kubernetes or
(see below), you will also need to [install Docker Compose](https://github.com/docker/compose/releases).

Running the environment (after Docker is installed and running):

1. Download all of the files (and directory structure) under
[https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/compose/tutorials](https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/compose/tutorials).

1. From within the `tutorials` directory downloaded in the first step, run the command:

    ```bash
    $ docker-compose -f ./egeria-tutorial.yaml up
    ```

1. You may need to wait while some containers are downloaded and started up. Once you see the following lines:

    ```text
    egeriacore_1  | Thu Sep 26 19:33:54 GMT 2019 OMAG server platform ready for configuration
    egeriadev_1   | Thu Sep 26 19:33:55 GMT 2019 OMAG server platform ready for configuration
    egeriadl_1    | Thu Sep 26 19:33:56 GMT 2019 OMAG server platform ready for configuration
    ```

    Your self-contained environment should be ready.  Go to http://localhost:18888 to open the Jupyter interface and
    you are ready to [start the Open Metadata Labs](../../open-metadata-labs).

1. To shutdown the environment press `CTRL-C` in the console where it was started.  To remove the containers,
    again within the `tutorials` directory run the command:

    ```bash
    $ docker-compose -f ./egeria-tutorial.yaml down
    ```

**Important note**: Be aware that each time you run the environment it will reset to a clean tutorial. So if you make
any changes within the Notebooks that you want to keep, be sure to save these to a local file on your computer before
shutting down.

## Advanced options

If you are not already familiar with these technologies, they will take more effort to utilize than Docker. However, if
you want to leverage public cloud services these may be more appropriate than using Docker directly.

After installing / configuring these components, details on deploying the labs can be found under:
[https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/charts/odpi-egeria-lab](https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/charts/odpi-egeria-lab)

### Kubernetes

[Kubernetes](https://kubernetes.io) is similar to Docker Compose -- it allows running multiple containers together in
a coordinated way -- but on a production-grade scale. Kubernetes is therefore often found in major cloud platforms like
[IBM Kubernetes Cluster (IKS)](https://cloud.ibm.com/kubernetes/catalog/cluster), [Google Kubernetes Engine (GKE)](https://cloud.google.com/kubernetes-engine/),
 [Amazon Elastic Kubernetes Service (EKS)](https://aws.amazon.com/eks/) and [Microsoft Azure Kubernetes Service (AKS)](https://azure.microsoft.com/services/kubernetes-service).

As you might imagine, "production-grade scale" means not only additional flexibility, but also complexity (compared to
Docker Compose).

For simple tutorial usage, Kubernetes is therefore not needed; and if you are not already familiar with it you will
probably want to start with just Docker Compose. However, if you are interested in exploring this option further you
can actually run it under Docker Desktop on both Windows and MacOS (or any of the various public cloud options above).

There are also [tutorials provided on Kubernetes itself](https://kubernetes.io/docs/tutorials/kubernetes-basics/).

### Helm

[Helm](https://helm.sh) is a package manager of sorts for Kubernetes. It allows defining a set of containers and other
related components in a template fashion so that they can be easily deployed into Kubernetes without a deep
understanding of the Kubernetes constructs themselves.

Like for Kubernetes itself, for simple tutorial usage Helm is not needed; however, if you are interested in exploring
this option further you can deploy Helm into the Kubernetes environment running in Docker Desktop on both Windows and
MacOS (as well, again, as public cloud options).

There is a [QuickStart Guide provided by Helm itself](https://helm.sh/docs/using_helm/#quickstart) covering
installation and basic usage.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
