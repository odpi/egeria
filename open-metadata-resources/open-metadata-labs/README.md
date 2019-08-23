<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Open Metadata Labs

The open metadata labs contains python notebooks
that allow you to try different aspects of the
ODPi Egeria capability.

Each notebook describes a scenario from the
[Coco Pharmaceuticals](https://github.com/odpi/data-governance/tree/master/docs/coco-pharmaceuticals)
case study, describing
a challenge that one or more of the personas face and
how they approached the solution.

The calls to the Egeria APIs necessary
to complete the challenge are encoded in
the notebook so you can experiment with the
APIs.

The open metadata labs can be used for individual study,
as part of a class and/or as e basis of a workbook for
using Egeria within a specific organization.

## Running the Labs

There are three main ways to run these labs:

### Using docker-compose

See https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/compose/tutorials

This does require docker, and is typically ran locally within the container environment, but is fairly simple and lightweight

### Using Kubernetes

See https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/charts/lab

Kubernetes is useful for many kinds of deployments, and is highly flexible, so other scenarios beyond simple labs are best addressed in this kind of environment. It is also a good approach for running on a cloud provider and for learning more about kubernetes and general egeria deployment.

### Locally / self setup

If you have egeria running locally, you can run the tutorials without any dependency on container infrastructure. You will however need run and manage your own egeria, kafka, jupyter etc. Unlike the options above you will also need to load in the tutorial notebook files to your environment.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
