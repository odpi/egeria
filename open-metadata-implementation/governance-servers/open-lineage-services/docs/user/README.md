<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Lineage Services User Documentation

The Open Lineage Services is designed to listen on the events published by [Asset lineage OMAS](../../../../access-services/asset-lineage) 
and build the lineage information based on these events.

OLS can be used by external tools to retrieve the lineage information at **column** and **table** levels.

THe Open Lineage Services is used to retrieve **Vertical Lineage** and **Horizontal Lineage** functionality. 
- The **horizontal lineage** traces how data travels across systems, starting with the system of records to the point of usage
- The **vertical lineage** traces how data is connected from its business element to the corresponding technical elements

Most of the interaction with the Open Lineage Services will be driven by the external tools used to query for lineage.


To understand how to configure:

* [Configuring OLS](../configuration)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.