<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.7 (Planned April 2020)

Release 1.7 focuses on the automatic metadata discovery and capture
using different techniques.

Below are the highlights:

* There are new access services:
   * The [Data Platform OMAS](../open-metadata-implementation/access-services/data-platform) supports the cataloging of Assets from data platforms such as Apache Cassandra.
   * The [Stewardship Action OMAS](../open-metadata-implementation/access-services/stewardship-action) 

* There are new governance servers that make use of the new access services:
   * The Organization Sync Server loads user and organization information into the open metadata ecosystem.
     It calls the Community Profile OMAS.
   * The [Data Platform Server](../open-metadata-implementation/governance-servers/data-platform-services) supports the processing of notifications from data platforms such as Apache Cassandra in order
     to automatically catalog the Assets stored on the data platform.  It calls the Data Platform OMAS.
   * The [Stewardship Server](../open-metadata-implementation/governance-servers/stewardship-engine-services) manages the triage and resolution of request for actions.
     It calls the Stewardship Action Engine OMAS.
     
* The [Discovery Engine OMAS](../open-metadata-implementation/access-services/discovery-engine) and
  [Discovery Server](../open-metadata-implementation/governance-servers/discovery-engine-services)
  are enhanced to support data quality request for action.  
     
* There are new [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs) and
  [samples](../open-metadata-resources/open-metadata-samples) demonstrating
  the new automatic metadata discovery and capture capabilities of Egeria. 
  
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.