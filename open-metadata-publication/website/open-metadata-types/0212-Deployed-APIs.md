<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0212 Deployed APIs

Deployed APIs are [Assets](0010-Base-Model.md) that provide remote access to [DeployedSoftwareComponents](0215-Software-Components.md).
The **APIEndPoint** identifies the network address used to call the API
(defined in [Endpoint](0026-Endpoints.md)).  

![UML](0212-Deployed-APIs.png#pagewidth)

The classifications **RequestResponseInterface**, **ListenerInterface** and **PublisherInterface** can be used to
describe the style of the API.  They can appear in combination on a single API.
The structure of the API's interface is described in the [APISchema](0536-API-Schemas.md).

The definition of the API's operation, parameters and responses are supported by the [API Schema Types](0536-API-Schemas.md).

----

* Return to [Area 2](Area-2-models.md).
* Return to [Overview](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.