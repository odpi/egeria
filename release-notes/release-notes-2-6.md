<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.6 (February 2021)

Release 2.6 adds support for:
 * New Governance Server called [Engine Host](../open-metadata-implementation/admin-services/docs/concepts/engine-host.md)
   with associated [Open Metadata Engine Services (OMES)](../open-metadata-implementation/engine-services).
   This Governance Server replaces the Discovery Server, Stewardship Server, Virtualizer and Security Officer Server.
   
 * Extensions to Open Metadata Types for lineage, duplicate processing, governance actions,
   the software development lifecycle and analytics models.
   
The release also changes the default location of some important files in order to facilitate deployment
and seperate program files from writeable data. 

Details of these and other changes are in the sections that follow.

## Description of Changes
### Changes to data files created/used by Egeria

Up to and including release 2.5, various data files were created in the current working directory when Egeria was run. This included
configuration files, cohort information, graph repositories etc.  This made it difficult to manage Egeria in a container environment
where we want to manage persistent data explicitly - for example via a docker volume, or a kubernetes persistent volume (claim).

Because of this the default locations of a number of files have changed, so when deploying release 2.6 make sure you copy any existing
files you need to preserve over to their new locations. If this is not done server configs and repository data may not
be found, or configuration files may not be decrytable.

| usage                | old | new | variables |
| -------------------- | ------------------------- | ------------------------------------------ | -------------------------------- |
| Server configuration | omag.server.{0}.config    | data/servers/{0}/config/{0}.config         | 0 = server Name                  |
| File based audit log | omag.server.{0}.auditlog/ | data/servers/{0}/logs/auditlog/            | 0 = server Name                  |
| cohort registry      | {0}.{1}.registrystore     | data/servers/{0}/cohorts/{1}.registrystore | 0 = server Name, 1 = cohort name |
| Graph repository     | {0}-graph-repository/     | data/servers/{0}/repository/graph/         | 0 = server Name                  |
| Encrypted config key | keystore_*                | data/platform/keys/keystore_*              |                                  |

The result of this is that all the dynamic data created by egeria locally in the filesystem is restricted to the 'data' directory
so this can be mapped to a volume easily.

If you have already explicitly configured the relevant connector yourself there will be no change. this updates the defaults only.

### Removal of the Discovery Server and Stewardship Server
 
The Discovery Server, Stewardship Server, Virtualizer and Security Officer Server have been consolidated into a new
type of server called the [Engine Host OMAG Server](../open-metadata-implementation/admin-services/docs/concepts/engine-host.md). 
The Engine Host runs one-to-many [Open Metadata Engine Services (OMES)](../open-metadata-implementation/engine-services).
Each engine services hosts a specific type of governance engine. The first engine service called
[Asset Analysis OMES](../open-metadata-implementation/engine-services/asset-analysis) will be for
[discovery engines](../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-engine.md)
and others for the different types of [governance action engines](../open-metadata-implementation/frameworks/governance-action-framework/docs/governance-action-engine.md).
from the 
[Governance Action Framework (GAF)](../open-metadata-implementation/frameworks/governance-action-framework).

The reason for this change is that there is a lot of duplicated code in the original servers and this change simplifies
the [Governance Server Services](../open-metadata-implementation/governance-servers) and
[Server Administration](../open-metadata-implementation/admin-services/docs/user).
With this change it will also be easier for Egeria to host other types of governance engines such as Palisade and Gaian.

### New open metadata types for Governance Actions

The following types have been added to support the governance action engines:

* [0461 Governance Action Engines](../open-metadata-publication/website/open-metadata-types/0461-Governance-Engines.md)
* [0462 Governance Action Types](../open-metadata-publication/website/open-metadata-types/0462-Governance-Action-Types.md)
* [0463 Governance Actions](../open-metadata-publication/website/open-metadata-types/0463-Governance-Actions.md)

### Updates to open metadata types for Lineage Mapping

The [LineageMapping](../open-metadata-publication/website/open-metadata-types/0770-Lineage-Mapping.md)
open metadata relationship type has been updated to link **Referenceables** rather than **SchemaElements**.
This is to capture lineage between components at different levels of detail since the data field mappings may not
always be available. Lineage mapping is described in more detail [here](../open-metadata-publication/website/lineage).

### New open metadata types for Duplicate Processing

Since Egeria is integrating and distributing metadata from many different sources, it is inevitable that
there will be multiple metadata instances that represent the same real-world "thing".  The 
[0465 Duplicate Processing](../open-metadata-publication/website/open-metadata-types/0465-Duplicate-Processing.md)
types allow these elements to be linked together.

### Presentation Server / React UI

* The node based User Interface component known as 'Presentation Server' has now fully moved to it's own
[GitHub Repository](https://github.com/odpi/egeria-react-ui). 
* The docker image has been renamed to [egeria-react-ui](https://hub.docker.com/repository/docker/odpi/egeria-react-ui) 
* Dino - Adds display of integration servers’ integration services and engine hosts’ engine services, including display of a dependency on a partnerOMAS.
* Rex - Improved error reporting and geometry management plus more consistent handling of focus objects.
Enterprise queries are now the default, but can be over-ridden to perform a local operation.
* At this time 'Server Author' and 'Glossary Author' are still in development.

### New Helm Chart

In addition to our 'lab' helm chart to support the Coco Pharmaceuticals environment, we have now added an [additional
helm chart](../open-metadata-resources/open-metadata-deployment/egeria-base) which provides a simpler environment with just a single platform, and a single server, but configured with 
persistence and auto start. This offers an example of a simple Kubernetes deployment.

### Graph Repository
* Now implements the findEntities and fnidRelationships methods of the OMRS MetadataCollection API.
* Added detailed documentation for the graph repository

### Conformance Test Suite
* CTS now has tests for findEntities and findRelationships methods and search tests have been realigned into profiles so that all search operations are in optional profiles, with basic and advanced profiles for each of entities and relationships.

### Other changes

Release 2.6 also contains many bug fixes and minor improvements & dependency updates
### Removals and Deprecations

* Discovery Server, Stewardship Server, Virtualizer and Security Officer Server have been replaced with more extensive capability - see above.
* Information View OMAS has now been removed following earlier deprecation.

# Egeria Implementation Status at Release 2.6

![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-2.6.png#pagewidth)

Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
Open Metadata and Governance vision, strategy and content.


# Further Help and Support

As part of the Linux AI & Data Foundation, our slack channels have moved to the [LF AI & Data Slack workspace](slack.lfaidata.foundation), and our mailing lists can now be found at https://lists.lfaidata.foundation/groups

Continue to use these resources, along with GitHub to report bugs or ask questions.

----
* Return to [Release Notes](.)
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
