<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Graph Repository

The Graph Repository provides a local repository that uses a graph store as its persistence store.

## Configuration
To configure an OMAG server to use the Graph Repository, set the repository-mode to 'local-graph-repository'. For example:
```
HTTP POST <serverURLRoot>/open-metadata/admin-services/users/<userName>/servers/<serverName>/local-repository/mode/local-graph-repository
```
Replace the <serverURLRoot>, <userName> and <serverName> with the values appropriate for the server you are configuring.

## Using the Graph Repository
The interface to the graph repository is the OMRS MetadataCollection API. The graph repository supports almost all of the MetadataCollection API apart from historical queries and undo.

## Using the find methods
The find methods (listed below) use regular expression (regexp) syntax and can be divided into two groups:

Three of the methods are intended for retrieval of specific entities or relationships. In these methods the regexp is matched to the whole value of the property or the whole classification name.
* findEntitiesByProperty()
* findEntitiesByClassification()
* findRelationshipsByProperty()
For example, to find an entity with a property value of "coco-asset-123" you would need to specify a regexp that will match the whole string value, e.g. "coco-asset-.*"
If you only specify a regexp of "coco-asset", it will not match the entity.

The other methods are for broader search. They operate over all string properties of an entity or relationship and will return all entities or relationships for which the regexp matches a substring of the property value.
* findEntitiesByPropertyValue()
* findRelationshipsByPropertyValue()
For example, to find entities with property values of "coco-asset-123", "coco-asset-456", "coco-asset-789" you could specify a regexp e.g. "-asset-"
The regexp only needs to specify a possible substring of the value to match.



## Under the hood
Internally, the Graph Repository uses JanusGraph to create a graph database. The JanusGraph graph database and graph are automatically created and the graph schema and search indexes are automatically configured.

The Graph Repository configures JanusGraph to use the berkeley embedded database and Lucene search indexes.

The supported interface for using the Graph Repository is the MetadataCollection API, and access should only be performed through this interface. In contrast, the schema and indexing used internally by the Graph Repository are subject to change as a result of future improvements to Egeria and should not be relied upon directly.

The repository uses the repository helper for parameter and type validation.