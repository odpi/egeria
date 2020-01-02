<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Open Metadata and Governance (OMAG) Server

An OMAG server is a software server that supports one or more Open Metadata and Governance (OMAG)
services.  The services that are activated in an OMAG server are defined in its
[Configuration Document](../../../open-metadata-implementation/admin-services/docs/concepts/configuration-document.md).

The OMAG Server runs in one or more [OMAG Server Platforms](.).  When it is started, the operational services
of the OMAG Server Platform reads the configuration document and activates the OMAG Server with the requested services.

Although an OMAG Server can be be configured with any combination of OMAG services, there are
commonly used combinations called
[OMAG Server Personalities](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server-personalities.md).
These personalities help to structure your open metadata ecosystem.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.