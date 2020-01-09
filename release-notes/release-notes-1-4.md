<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.4 (planned January 2020)

Release 1.4 introduces design lineage collection, storage and reporting
focusing on showing the ultimate source and destination of data flow.
This involves the capture of knowledge of data movement, transformation and
copying to it is possible to trace how data is flowing between the systems.

* The first two governance servers are released:
 
   * The [Discovery Server](../open-metadata-implementation/governance-servers/discovery-engine-services) supports the scanning of assets and the notification when duplicate suspects are detected.
   * The [Stewardship Server](../open-metadata-implementation/governance-servers/stewardship-services) supports the assessment of duplicate suspects and orchestrates the linking of duplicate assets.
  
  These servers are supported by: 
   * The [Discovery Engine OMAS](../open-metadata-implementation/access-services/discovery-engine) supports the detection, recording and notification of exceptions and duplicate suspects.
   * The [Stewardship Action OMAS](../open-metadata-implementation/access-services/stewardship-action) supports the resolution of exceptions and duplicate suspects and the linking of duplicate assets.

* There are new [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs) and
  [samples](../open-metadata-resources/open-metadata-samples) demonstrating
  the new archive and de-duplication features.

* As part of the lineage support are new access services:
   * The [Asset Catalog OMAS](../open-metadata-implementation/access-services/asset-catalog) provides a catalog search API for Assets.
   * The [Asset Lineage OMAS](../open-metadata-implementation/access-services/asset-lineage) supports the notification of the availability of new lineage information.
   * The [Data Engine OMAS](../open-metadata-implementation/access-services/data-engine) supports the processing of notifications from data engines such as ETL platforms in order to catalog information about the data movement, transformation and copying they are engaged in.
 
* There is an extension the the Egeria user interface to view the lineage of an asset.


  * The [Data Engine Proxy Server](../open-metadata-implementation/governance-servers/data-engine-proxy-services) supports the processing of notifications from data engines such as ETL platforms
     in order to catalog information about the data movement, transformation and copying they are engaged in.
     It calls the Data Engine OMAS. 

* New [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs),
  [samples](../open-metadata-resources/open-metadata-samples) and 
  [open metadata archives](../open-metadata-resources/open-metadata-archives) will demonstrate the new
  lineage capability.

   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.