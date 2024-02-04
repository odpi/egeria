<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Governance Servers Documentation

A governance server is a specialized [OMAG Server](https://egeria-project.org/concepts/omag-server).
It is designed to host connectors that manage metadata in different technologies.

Each type of governance server is paired with an Open Metadata Access Service (OMAS) that supports
the same metadata types that the governance server is managing in the third party technology.
The governance servers each have a specific subsystem that contains its services.

The table below shows the different types of governance servers and the OMAS they are paired with.

| Governance Server        | Description | Supported by subsystem                                        | Paired with OMAS | 
|:-------------------------| :---------- |:--------------------------------------------------------------| :--------------- | 
| Integration Daemon       | Manages the capture of metadata through configured integration services. | [Integration Daemon Services](../integration-daemon-services) | Defined in each [integration service](../../integration-services) |
| Engine Host              | Manages governance engines. | [Engine Host Services](../engine-host-services)               | [Governance Engine OMAS](../../access-services/governance-engine) |
| Data Engine Proxy Server | Manages capture of metadata from a data engine. | [Data Engine Proxy Services](../data-engine-proxy-services)   | [Data Engine OMAS](../../access-services/data-engine) | 
| Lineage Warehouse Server | Provides a historic reporting warehouse for lineage. | [Lineage Warehouse Services](../lineage-warehouse-services)   | [Asset Lineage OMAS](../../access-services/asset-lineage) | 

## Further information

Each governance server has its own documentation (follow the links above).  In addition, there is an
overview design document for developing a new governance server.

* [Designing and Implementing an Open Metadata Governance Server](design)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.