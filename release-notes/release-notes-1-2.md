<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.2

Release 1.2 provides the ability to build an asset catalog, search for assets and also
access the data and function provided by these assets.
It is also possible to group Assets into governance zones to control the discoverability and visibility of the Assets. 

Below are the highlights:
  
* There are new access services:
   * The [Asset Catalog OMAS](../open-metadata-implementation/access-services/asset-catalog) provides a catalog search API for Assets.
   * The [Asset Consumer OMAS](../open-metadata-implementation/access-services/asset-consumer) supports the access to both the data and metadata associated with an asset.
   * The [Asset Owner OMAS](../open-metadata-implementation/access-services/asset-owner) supports the manual cataloging of new Assets.
   * The [Data Platform OMAS](../open-metadata-implementation/access-services/data-platform) supports the cataloging of Assets from data platforms such as Apache Cassandra.
   
* The first governance server is released:
   * The [Data Platform Server](../open-metadata-implementation/governance-servers/data-platform-services) supports the processing of notifications from data platforms such as Apache Cassandra in order
     to automatically catalog the Assets stored on the data platform.  It calls the Data Platform OMAS.

* The [Open Metadata Repository Services (OMRS)](../open-metadata-implementation/repository-services)
  have been enhanced to support support dynamic types and type patching.
  There is also function to load archives of metadata instances.
  
* There is a new user interface to query the Asset Catalog and preview Asset contents.
   
* There are [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs) and
  [samples](../open-metadata-resources/open-metadata-samples) demonstrating
  the new Asset cataloging capabilities.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.