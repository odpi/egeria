<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Lineage Services

The Open Lineage Services provides a historic reporting warehouse for lineage. It listens to events that are send out 
by the Asset Lineage OMAS, and stores lineage data in a Janusgraph database. This lineage can then be queried through
the Open Lineage Services client and by its REST API, for example by a lineage GUI. While the data format of events sent
out by the Asset Lineage Omas are in the Open Metadata format, Open Lineage services store lineage data in a very basic
data format in order to optimize query performance.

In essence there are 3 kinds of graphs:
- ***buffer graphName*** -  used to store current lineage in the Open Metadata types
- ***current graphName*** - stores current lineage in graphName database in the format optimimized for lineage
- ***historic graphName*** -  stores historic lineage in graphName database in the format optimimized for lineage


The Open Lineage Services data format consists of the following nodes (node properties included):

**Glossaryterm**

- guid
- displayName
- qualifiedName
- glossary

**Table**

- guid
- displayName
- qualifiedName
- glossaryTerm
- host.displayName
- database.displayName
- schema.displayName

**Column**

- guid
- displayName
- qualifiedName
- glossaryTerm
- host.displayName
- database.displayName
- schema.displayName
- table.displayName

**Process**

- guid
- createTime
- updateTime
- formula
- displayName
- processDescriptionURI
- version
- processType

**SubProcess**

- processID
- subprocessID
- guid
- createTime
- updateTime
- formula
- displayName
- processDescriptionURI
- version
- processType




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.