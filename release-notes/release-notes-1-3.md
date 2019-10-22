<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.3

Release 1.3 focuses on the support for heterogeneous lineage.
This involves the capture of knowledge of data movement, transformation and
copying to it is possible to trace how data is flowing between the systems.

Below are the highlights:

* There are new access services:
   * The [Asset Lineage OMAS](../open-metadata-implementation/access-services/asset-lineage) supports the notification of the availability of new lineage information.
   * The [Data Engine OMAS](../open-metadata-implementation/access-services/data-engine) supports the processing of notifications from data engines such as ETL platforms in order to catalog information about the data movement, transformation and copying they are engaged in.
   * The [Stewardship Action OMAS](../open-metadata-implementation/access-services/stewardship-action) supports the asset scan API, the recording of exceptions and duplicate suspects and the linking of duplicate assets.
 
* There are new governance servers:
   * The [Data Engine Proxy Server](../open-metadata-implementation/governance-servers/data-engine-proxy-services) supports the processing of notifications from data engines such as ETL platforms
     in order to catalog information about the data movement, transformation and copying they are engaged in.
     It calls the Data Engine OMAS.
   * The [Stewardship Server](../open-metadata-implementation/governance-servers/stewardship-services) supports the scanning of assets and the notification when duplicate suspects are detected.  It can also orchestrate the linking of duplicate assets.
     It calls the Stewardship Action OMAS.

* There is an extension the the Egeria user interface to view the lineage of an asset.

* There are new [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs) and
  [samples](../open-metadata-resources/open-metadata-samples) demonstrating
  the new lineage and de-duplication features.

  
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.