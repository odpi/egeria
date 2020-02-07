<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.6 (Planned March 2020)

Release 1.6 adds support for:
    * Governance Roles, Profiles and Collaboration
    * Automatic cataloguing of Apache Cassandra data sources
    * Glossaries, reference data and model support
    
Below are the highlights:

* There are new access services:
   * The [Subject Area OMAS](../open-metadata-implementation/access-services/subject-area) provides support for defining, reviewing and refining glossary content, reference data and rules for a subject area.
   * The [Glossary View OMAS](../open-metadata-implementation/access-services/glossary-view) supports the browsing of glossary content.
   * The [Design Model OMAS](../open-metadata-implementation/access-services/design-model) supports the management of data models.
   * The [Community Profile OMAS](../open-metadata-implementation/access-services/community-profile) supports the personal profile and communities APIs.
   * The [Data Platform OMAS](../open-metadata-implementation/access-services/data-platform) supports the cataloging of Assets from data platforms such as Apache Cassandra.

* There are new governance servers that make use of the new access services:
   * The Organization Sync Server loads user and organization information into the open metadata ecosystem.
     It calls the Community Profile OMAS.
   * The [Data Platform Server](../open-metadata-implementation/governance-servers/data-platform-services) supports the processing of notifications from data platforms such as Apache Cassandra in order
     to automatically catalog the Assets stored on the data platform.  It calls the Data Platform OMAS.

* The Egeria User Interface supports the definitions of subject areas.

* There is an extension to the Swagger tooling that integrates
  the Egeria code API generation.

* There are new [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs),
  [samples](../open-metadata-resources/open-metadata-samples) and
  [open metadata archives](../open-metadata-resources/open-metadata-archives) demonstrating
  these new capabilities of Egeria.
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.