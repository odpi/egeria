<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 3.1 (expected September 2021)

Release 3.0 adds:
* Requirement to build & run with Java 11 (LTS) or above

Details of these and other changes are in the sections that follow.

## Description of Changes

### Open Metadata Types

The following changes have been made to the open metadata types:

* New types for modeling data processing purposes.
  See new type descriptions in models [0485](../open-metadata-publication/website/open-metadata-types/0485-Data-Processing-Purposes.md).

* The following types have been deprecated: **BoundedSchemaType**, **BoundedSchemaElementType**,
  **ArraySchemaType** and **SetSchemaType**.
  See description in model [0507](../open-metadata-publication/website/open-metadata-types/0507-External-Schema-Type.md).

* The **ServerEndpoint** relationship can now connect to any **ITInfrastructure** elements, not just **SoftwareServers**.
  See description in model [0040](../open-metadata-publication/website/open-metadata-types/0040-Software-Servers.md).

* The **OperatingPlatform** entity can now record the patch level of the operating system.  There are also new types for describing
  the contents of an operating platform.
  See description in model [0030](../open-metadata-publication/website/open-metadata-types/0030-Hosts-and-Platforms.md).

* There are now types for distinguishing between virtual machines and virtual containers as well as bare metal hardware.
  There are also new types for specific technologies such as **HadoopCluster**, **KubernetesCluster** and **DockerContainer**
  to provide concrete examples of different types of hosts using popular technologies.
  Finally the **DeployedVirtualContainer** relationship has been deprecated in favor of a more generic **HostedHost** relationship.
  See description in model [0035](../open-metadata-publication/website/open-metadata-types/0035-Complex-Hosts.md).

* There are new types for define a storage volume that has been attached to a host.
  See description in model [0036](../open-metadata-publication/website/open-metadata-types/0036-Storage.md).

* A new subtype of software server for reusable business functions (such as microservices) has been added called **ApplicationService**.
  See description in model [0057](../open-metadata-publication/website/open-metadata-types/0057-Software-Services.md).


## Deprecation of docker-compose

The docker-compose environment for running our Coco Pharmaceuticals lab demo/tutorial is now deprecated. The configuration is still available, but will not
be further developed or tested, and will be removed in a future release.

Our Kubernetes Helm charts are now recommended to quickly setup the same lab
environment, and the [documentation](https://odpi.github.io/egeria-charts/site/index.html) for these has been improved to cover 
a Kubernetes introduction, and example based on 'microk8s' which are suited to
an end user desktop environment (and can also be run in enterprise/cloud environments)

### Known Issues

* It is recommended to use a chromium-based browser such as Google Chrome or Microsoft Edge, or alternatively Apple Safari for the Egeria React UI. Some parts of the UI experience such as Dino currently experience problems with Firefox. See [odpi/egeria-react-ui#96](https://github.com/odpi/egeria-react-ui/issues/96) .


# Further Help and Support

See the [Community Guide](../Community-Guide.md).

----
* Return to [Release Notes](.)
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
