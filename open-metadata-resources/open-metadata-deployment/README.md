<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Containerized deployment of ODPi Egeria

This module contains resources to assist in deployment of Egeria and related components. They are intended to
facilitate developer usage and technology demonstrations and will need further work to be suitable for production.

The long term goal is for this area to focus on the egeria core only with samples & specific deployments moving to their own 
repositories. 

## Docker Images

Docker images used by demos, deployments & most specifically a docker image for egeria itself which is automatically published as
part of our build process, shared to Docker Hub, and is used by both our Kubernetes and Docker-compose samples.

## Kubernetes

Helm charts to deploy egeria along with supporting infrastructure can now be found at https://github.com/odpi/egeria-charts .
A K8s is being developed at https://github.com/odpi/egeria-k8s-operator .

## Docker Compose

Currently the focus is on Helm charts to facilitate deployment to Kubernetes clusters.

The docker-compose environment is now deprecated & it is recommended to switch to using our Kubernetes examples instead.

----
* Return to [open-metadata-resources](..)
* Return to [Site Map](../../Content-Organization.md)
* Return to [Home Page](../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
