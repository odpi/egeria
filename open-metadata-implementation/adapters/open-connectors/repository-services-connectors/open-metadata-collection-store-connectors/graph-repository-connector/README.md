<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../../../../open-metadata-publication/website/images/egeria-content-status-released.png#pagewidth)

# Graph Repository

The Graph Repository provides a local repository that uses a graph store as its persistence store.

## Configuration
To configure an OMAG server to use the Graph Repository, set the repository-mode to 'local-graph-repository'. For example:

```
POST <serverURLRoot>/open-metadata/admin-services/users/<userName>/servers/<serverName>/local-repository/mode/local-graph-repository
```

Replace the <serverURLRoot>, <userName> and <serverName> with the values appropriate for the server you are configuring.

## Using the Graph Repository
The interface to the graph repository is the OMRS MetadataCollection API. The graph repository supports almost all of the MetadataCollection API apart from historical queries and undo.

## Using the find methods
The find methods (listed below) use regular expression (regexp) syntax. They are intended for retrieval of specific entities or relationships and the regexp is always matched to the whole of the property value or classification name.
* findEntitiesByProperty()
* findRelationshipsByProperty()
* findEntitiesByPropertyValue()
* findRelationshipsByPropertyValue()
* findEntitiesByClassification()

The first two methods accept a map of property names and values that acts as a filter.
The next pair of methods accept a string that is matched against the values of any string properties.
The last method accepts a string that is matched against the names of classifications associated with an entity.

In all cases the string value(s) provided are treated as a regular expression that is matched against the whole property value or classification name.

To find entities with property values of "coco-asset-123", "coco-asset-456", "coco-asset-789" you could specify a regexp e.g. "coco-asset-.*"

To find an entity or relationship with a property value of "coco-asset-123" you need to specify a regexp that will match the whole string value, e.g. "coco-asset-.*", a substring is not sufficient. If you were to only specify a regexp of "coco-asset", it would not match the entity or relationship.

Similarly, to find an entity with a classification with name "coco-classification-abc" you would need to to specify a regexp that will match the whole string value, e.g. "coco-classification-.*"


## Under the hood
Internally, the Graph Repository uses JanusGraph to create a graph database. The JanusGraph graph database and graph are automatically created and the graph schema and search indexes are automatically configured.

The Graph Repository configures JanusGraph to use the berkeley embedded database and Lucene search indexes.

The supported interface for using the Graph Repository is the MetadataCollection API, and access should only be performed through this interface. In contrast, the schema and indexing used internally by the Graph Repository are subject to change as a result of future improvements to Egeria and should not be relied upon directly.

The repository uses the repository helper for parameter and type validation.



----
Return to the [open-metadata-collection-store-connectors](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
