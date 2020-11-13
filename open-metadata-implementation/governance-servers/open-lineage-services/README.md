<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

## Open Lineage Services

The Open Lineage Services provides a historic reporting warehouse for lineage. It listens to events that are sent out 
by the [Asset Lineage OMAS](../../access-services/asset-lineage/README.md), and stores lineage data in a graph database. 
This lineage can then be queried through the Open Lineage Services Client and by its REST API, for example by a lineage GUI. 

Open Lineage Services stores lineage data in the Open Metadata format, wile also adding some specific nodes and relationships in order to optimize 
query performance. 

## Digging Deeper

* [User Documentation](docs/user/README.md)
* [Design Documentation](docs/design/README.md)
* [Configuring OLS](docs/configuration/README.md)

----
Return to the [governance-servers](..) module.

----
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.