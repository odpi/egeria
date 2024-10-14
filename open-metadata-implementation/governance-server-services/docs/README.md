<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Governance Servers Documentation

A governance server is a specialized [OMAG Server](https://egeria-project.org/concepts/omag-server).
It is designed to host connectors that manage metadata in different technologies.

Each type of governance server is paired with an Open Metadata Access Service (OMAS) that supports
the same metadata types that the governance server is managing in the third party technology.
The governance servers each have a specific subsystem that contains its services.

The table below shows the different types of governance servers and the OMAS they are paired with.

| Governance Server        | Description                                                              | Supported by subsystem                                        | Paired with OMAS                                                                                                                                                                                                                                           | 
|:-------------------------|:-------------------------------------------------------------------------|:--------------------------------------------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------| 
| Integration Daemon       | Manages the capture of metadata through configured integration services. | [Integration Daemon Services](../integration-daemon-services) | [Governance Server OMAS](../../access-services/governance-server) provides support for retrieving governance engine definitions.  The [integration services](../../integration-services) define the OMASs that support each type of integration connector. |
| Engine Host              | Manages governance engines.                                              | [Engine Host Services](../engine-host-services)               | [Governance Server OMAS](../../access-services/governance-server) provides support for retrieving governance engine definitions. The [engine services](../../engine-services) define the OMASs that support each type of governance engine.                |

## Further information

Each governance server has its own documentation (follow the links above).  In addition, there is an
overview design document for developing a new governance server.

* [Designing and Implementing an Open Metadata Governance Server](design)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.