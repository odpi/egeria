<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.5 (Planned February 2020)


Release 1.5 focuses on the automatic metadata discovery and capture
using different techniques as well as end-user collaboration over assets.
  * Automated inspection of asset contents and stewardship of the results
  * Automatic cataloguing of Apache Cassandra data sources
  * IT Infrastructure cataloging maintained through DevOps pipelines
  * Support for Governance Roles, Profiles and Collaboration so that the end user is able to build up a
    profile of themselves that includes their roles and any feedback and other types of collaboration they have contributed.
    This information will be the basis of how the Egeria user interface adapts itself to the needs of each specific user.

Below are the highlights:

* There are new access services:
   * The [Community Profile OMAS](../open-metadata-implementation/access-services/community-profile) supports the personal profile and communities APIs.
   * The [Data Platform OMAS](../open-metadata-implementation/access-services/data-platform) supports the cataloging of Assets from data platforms such as Apache Cassandra.
   * The [Discovery Engine OMAS](../open-metadata-implementation/access-services/discovery-engine) supports the discovery server (below).
   * The [IT Infrastructure OMAS](../open-metadata-implementation/access-services/it-infrastructure) provides the ability to manage a catalog of IT Infrastructure.
   * The [DevOps OMAS](../open-metadata-implementation/access-services/dev-ops) supports the recording of successful steps and certifications in a DevOps pipeline.

* There are new governance servers that make use of the new access services:
   * The Organization Sync Server loads user and organization information into the open metadata ecosystem.
     It calls the Community Profile OMAS.
   * The [Data Platform Server](../open-metadata-implementation/governance-servers/data-platform-services) supports the processing of notifications from data platforms such as Apache Cassandra in order
     to automatically catalog the Assets stored on the data platform.  It calls the Data Platform OMAS.
   * The [Discovery Server](../open-metadata-implementation/governance-servers/discovery-engine-services) provides support for automated metadata discovery services that implement the ODF interfaces.
     It calls the Discovery Engine OMAS.

* The [Open Discovery Framework (ODF)](../open-metadata-implementation/frameworks/open-discovery-framework) is now defined and
  implemented to support the interfaces for automated discovery services.
  It complements the [Open Connector Framework (OCF)](../open-metadata-implementation/frameworks/open-connector-framework) delivered in release 1.0.

* The [Stewardship Action OMAS](../open-metadata-implementation/access-services/stewardship-action) 
  and [Stewardship Server](../open-metadata-implementation/governance-servers/stewardship-services)
  are enhanced to support exception triage and resolution.  
     
* There are new [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs) and
  [samples](../open-metadata-resources/open-metadata-samples) demonstrating
  the new automatic metadata discovery and capture capabilities of Egeria. 
  
  
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.