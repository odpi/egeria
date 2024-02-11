<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

## Lineage Warehouse Services

The Lineage Warehouse Services provides a historic reporting warehouse for lineage. It listens to events that are sent out 
by the [Asset Lineage OMAS](../../access-services/asset-lineage), and stores lineage data in a graph database. 
This lineage can then be queried through the Lineage Warehouse Services Client and by its REST API, for example by a lineage GUI. 

Lineage Warehouse Services stores lineage data in the Open Metadata format, wile also adding some specific nodes and relationships in order to optimize 
query performance. 

* [Documentation](https://egeria-project.org/services/lineage-warehouse-services)

## Digging Deeper

* [User Documentation](docs/user)
* [Design Documentation](docs/design)
* [Configuring OLS](docs/configuration)

----
Return to the [governance-servers](..) module.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.