<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Lineage Services

The Open Lineage Services provides a historic reporting warehouse for lineage. It listens to events that are send out 
by the Asset Lineage OMAS, and stores lineage data in a database. This lineage can then be queried through
the Open Lineage Services client and by its REST API, for example by a lineage GUI. While the data format of events sent
out by the Asset Lineage Omas are in the Open Metadata format, Open Lineage services store lineage data in a very basic
data format in order to optimize query performance.


- ***Buffer graph*** -  used to store current lineage in the Open Metadata types
- ***Main graph*** - stores current lineage in graph database in the format optimimized for lineage


The Open Lineage Services data format consists of the following nodes (node properties included):

**Glossaryterm**

- Guid
- nodeID
- displayName
- qualifiedName
- glossary.guid
- glossary.name
- category

**Table**

- guid
- nodeID
- displayName
- qualifiedName
- glossaryTerm.displayName
- glossaryTerm.guid
- host.displayName
- host.guid
- database.displayName
- database.guid
- schema.displayName
- schema.guid
- Zones
- System
- Organization
- Geographical Location



**Column**

- Guid
- nodeID
- displayName
- qualifiedName
- glossaryTerm.displayName
- glossaryTerm.guid
- host.displayName
- host.guid
- database.displayName
- database.guid
- schema.displayName
- schema.guid
- table.displayName
- table.guid

**Process**

- guid
- nodeID
- createTime
- updateTime
- formula
- displayName
- processDescriptionURI
- version
- processType

**SubProcess**

- guid
- nodeID
- process.guid
- createTime
- updateTime
- formula
- displayName
- processDescriptionURI
- version
- processType
