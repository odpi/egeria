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

* The **ServerEndpoint** relationship can now connector to any **ITInfrastructure** elements, not just **SoftwareServers**.
  See description in model [0040](../open-metadata-publication/website/open-metadata-types/0040-Software-Servers.md).


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
