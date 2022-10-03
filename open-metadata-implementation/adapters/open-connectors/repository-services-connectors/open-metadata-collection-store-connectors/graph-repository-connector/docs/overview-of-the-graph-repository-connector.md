<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2021. -->

# Overview of the Graph Repository Connector

## Abstract
The Egeria Graph Repository is one of a number of repository types within (or integrated with) Egeria. 
The graph repository provides a means to store instance metadata into a graph database, which is an 
efficient way to traverse interconnected entities, relationships and classifications. This document 
describes the graph repository both in terms of its external shape and behavior and in terms of its 
internal implementation.

## Background

### Egeria
The Egeria architecture provides the ability to create a cohort of servers, which can run access 
services, integration services and engine services. A server can optionally host a local metadata 
repository in which it can store metadata instances. 

### Cohorts
A cohort is a group of servers that cooperate with one another. They communicate requests and 
events across the cohort, making it possible for a server in the cohort to access metadata managed 
by another server in the cohort. The routing of requests and events enables the cohort to support a 
location transparent, federated view of the metadata.

### Open Metadata Repository Services (OMRS)
The management of metadata repositories and the ability to operate as a cohort are provided by the 
OMRS component of Egeria. OMRS enables a service running in the cohort to issue requests to a metadata 
server to create, find, read and manage metadata, and the federation provided by the OMRS layer means 
this can be location-transparent across the cohort.

OMRS provides the event mechanism that enables a repository server in the cohort to generate events 
relating to its local metadata, to be delivered to other servers in the cohort. By subscribing to OMRS 
events a server can stay up to date with changes to the metadata in other servers. To support this 
capability, a repository uses an OMRS Event Mapper but for many repositories this function is implemented 
by the built-in local repository support.

### Metadata Collection
The OMRS Repository Services interface defines the Metadata Collection API which is the standard API used 
to interact with a metadata repository. A repository connector implements this interface and maps the 
operations to fit the schema and interface of its chosen store. It would be possible to connect many 
different sorts of store or database to act as metadata repositories, each bringing their own advantages 
in terms of speed, persistence, scaling, historical queries, traversal of relationships, etc.

### Graph Repository
The Egeria graph repository is an implementation of the Metadata Collection interface. The graph repository 
supports the full open metadata type system and uses a graph database to store and retrieve metadata instances 
into a local metadata repository. 

The graph database used is JanusGraph. It would be possible to implement similar connectors for other graph databases.

## Overview of the Egeria Graph Repository
An Egeria repository is implemented by a repository connector. The graph-repository-connector can be found in the Egeria code 
repository, within the adapters module:
```
open-metadata-implementation 
-> adapters 
    -> open-connectors 
         -> repository-services-connectors 
              -> open-metadata-collection-store-connectors 
                   -> graph-repository-connector
```
The principal components in the graph-repository-connector module are the:
* GraphOMRSMetadataCollection – which implements the Metadata Collection interface
* GraphOMRSMetadataStore - which maps the Metadata Collection interface to the graph database

These components are described in more detail below, with commentary on the rationale and design decisions. There are additional 
classes that provide preparation of the graph schema and indexes, object mapping, error handling and audit logging. They are 
described later in this document.

###The Repository Connector
The Repository Connector allows other OMRS components to connect to the graph repository, to create, read, update or delete metadata 
types and instances. The connector is brought into existence and started by a connector provider. Both the connector and the connector 
provider are classes in the graph-repository-connector module.

###GraphOMRSMetadataCollection
The repository connector instantiates a class called the GraphOMRSMetadataCollection – which implements the OMRSMetadataCollection 
interface. 

The metadata collection interface contains 6 groups of methods relating to different aspects of management of metadata:
1.	Identification of the metadata repository 
2.	Operations on types (create, read, update, delete)
3.	Operations for retrieval of entity and relationship instances
4.	Operations to create, update or delete entity and relationship instances. This includes classification of entity instances
5.	Operations on control information – effectively an advanced section for re-identification, re-typing, re-homing of entity and 
relationship instances
6.	Methods for management of local reference copies of instances

The GraphOMRSMetadataCollection module is a straightforward implementation of the Metadata Collection API which is very similar to 
the corresponding module for the Egeria inmemory-repository-connector. The two built-in repositories share a lot of common code which 
is "pushed up" into the super-classes. 

Some of the methods in the Metadata Collection interface are mandatory - which means a repository connector must support them in order 
for the repository to be conformant with the Egeria conformance test suite. Meanwhile, there are other methods in the interface that 
are optional. The graph repository supports the whole interface apart from the ability to 'undo' and operation or to perform a 
historical query, in which the results reflect the state the database was in at a specified point in time, such as an hour ago, or a 
week ago. 

###GraphOMRSMetadataStore
The GraphOMRSMetadataStore module presents an internal interface that supports the operations implemented by the 
GraphOMRSMetadataCollection module and converts them into operations that fit the schema and API of the graph database. 

The metadata store converts from the concepts in the metadata collection interface - entities, classifications and relationships - 
into the corresponding physical manifestations within the graph database - vertices and edges, both with properties.
Each entity, relationship or classification instance has a type and properties. 

Entities and classifications are stored in the graph as vertices with properties. 
Relationships are stored as edges with properties. 
An entity vertex is connected to each of its classification vertices by an edge.

The graph schema uses the following mappings:
* Entity => A vertex with the label "Entity"
* Relationship => An edge with the label "Relationship"
* Classification => A vertex with the label "Classification"
* Association between an Entity and a Classification => An edge with the label "Classifier"

###Instance Properties
There are two broad categories of properties in metadata instances. These are the core properties and the type-defined attributes. 
Core properties are common across all instances, regardless of type. They are defined in the InstanceAuditHeader and associated classes. 
In contrast, type-defined attributes are specific to a particular type. They are optional; a type may have no type-defined attributes.

###Core Properties
The core properties include fields like GUID, typeName, createdBy, createTime, etc. as well as the id of the metadata collection that 
is the home for the instance - i.e. the repository that holds the authoritative copy of the instance.

###Type-defined attributes
The type-defined attributes are used to store any properties that are specific to a particular type. You can find them either by 
looking in the Type Explorer user interface, or by examining the open metadata types archives. I would recommend the former approach.

###Reference Copies
The Repository Connector may be called with a request to store a reference copy of an entity or relationship instance managed by 
another ("home") metadata server. The home metadata server will be identified by a metadataCollectionId – which is stored in the 
metadataCollectionId core property in the reference copy. This property is important because it is the means by which the local 
repository (or its users) can verify whether a metadata instance is homed by the local repository or is a reference copy of an 
instance that has a different home repository.

##Operations

The following examples illustrate some operations implemented by the graph repository.

###Creation of the graph database
When the server starts and loads the repository connector, it will look for the graph database and by default will use the location
'./data/servers/{serverName}/repository/graph/berkeley' for the persistent store and './data/servers/{serverName}/repository/graph/searchIndex' 
for the Lucene index. The choice of backends and locations can be configured by storage properties set in the connection and passed via 
the Connector's connectionBean to the GraphOMRSMetadataCollection constructor.

If the graph database does not already exist, the connector will create it and initialize it. The initialization includes defining 
the graph schema and creating indexes for all known core properties. When the repository starts up it will be called to verify 
support for each of the open types, and during that processing it will check that it has an index for each of the type-defined 
attributes for that type.

If the graph database already exists, the repository connector checks that the metadataCollectionId stored in the graph matches 
the metadataCollectionId of the local server's repository. If not, it will reject it as this may be an inadvertent attempt to adopt 
the graph database due to misconfiguration. If it matches the graph database will be opened and checked to ensure that it contains 
the schema and indexes for the current open types.

###Operations on Types

Validation of types during start
During start of the connector, the OMRS operational services component will load the types defined in the Egeria type archive. For 
each type in the archive, the OMRS connector will be asked to verify whether the (OM) TypeDef can be supported by the local repository. 
The OMRS method of interest here is verifyTypeDef. The graph repository supports all the open types, so the answer is always "Yes" and 
it will check it has an index for all the attributes of the type. 

Creation of types after start
Following start, it is possible for OMRS to call the addTypeDef method to add further types to the system. This behaves in a similar 
manner to the verification of a TypeDef described above.

Removal of a type
A TypeDef can be removed from the system by calling the deleteTypedef method, passing the GUID of the TypeDef.

###Operations on Entities

####Addition of an entity
It is possible to add an instance of an entity type by supplying the type and any necessary initial properties and classifications. The 
implementation of this method will delegate to the metadata store, which will create a vertex for the entity and set its properties, and 
will create a vertex for each classification, set its properties and connect it to the entity vertex via a classifier edge. The mapping 
from entity to vertex or classification to vertex is handled by the appropriate object mapper, using the GraphOMRSEntityMapper or 
GraphOMRSClassificationMapper.

####Retrieval of one or more entities
There are metadata collection methods that allow retrieval of a specific entity with a supplied GUID or that allow retrieval of entities 
that match specified features, using the 'find' methods. All these methods delegate to the metadata store which constructs graph traversals 
to fetch the corresponding vertices. These are mapped to entities using the GraphOMRSEntityMapper.

####Deletion of an entity
The OMRS method for deleting an entity requires that the caller specify the GUID of the entity to delete. Once again, this delegates to 
the metadata store, which locates the entity and marks it as deleted (for a soft-delete) or removes if (for a purge). The entity mapper 
is employed here too, because the delete methods return a copy of the entity.

####Classification of an entity
The classifyEntity() method validates the entity type and classification type are valid and that the entity type is within the list of 
valid entity definitions to which the classification can be applied. It then delegates to the metadata store to request that the 
corresponding entity is classified, which as described above means a classification vertex is created and linked to the entity vertex 
using a classifier edge.

###Operations on Relationships

The operations for relationships are largely similar to those for entities, with the exception that a relationship needs to refer to a 
pair of entities or entity proxies.

####Addition of a relationship
It is possible to add a relationship instance by specifying the relationship type, initial properties and related entities. Each entity 
is identified by its GUID. The repository connector locates the entities, optionally creating proxies if they do not exist and creates 
a relationship edge that connects the entity vertices.

####Retrieval of one or more relationships
There are metadata collection methods that allow retrieval of a specific relationship with a supplied GUID or that allow retrieval of 
relationships that match specified features, using the 'find' methods. All these methods delegate to the metadata store which constructs 
graph traversals to fetch the corresponding edges and the connected vertices. These are mapped to relationships and entities using the 
object mappers.

####Deletion of a relationship
The metadata collection method for deleting a relationship requires that the caller specify the GUID of the entity to delete. Once again, 
this delegates to the metadata store, which locates the relationship and marks it as deleted (for a soft-delete) or removes if (for a 
purge). The relationship and entity mappers are employed here too, because the delete methods return a copy of the relationship.

###Finding metadata instances

The graph-repository-connector supports all the find methods in the metadata collection interface.

The preferred approach should be to use:
* findEntities()
* findRelationships()

The methods listed above have a number of advantages. They enable you to specify multiple properties (which is similar to the methods listed 
below) but in addition they support the specification of a different operation for each property. There is also a much broader set of operations 
available to the caller; in addition to supporting EQ, you can also use NEQ, LT, LTE, GT, GTE, and more. It is also possible to perform multiple
comparison operations on the same property, which enables you to search for all instances that have (for example) an updateTime within a specified
range. You can also combine groups property conditions that have independent match criteria - for example, ALL, ANY or NONE.
The above methods are therefore more powerful than the earlier methods which include:
* findEntitiesByProperty()
* findEntitiesByPropertyValue()
* findRelationshipsByProperty() 
* findRelationshipsByPropertyValue()
* findEntitiesByClassifications()

For information on how to use the find methods, please refer to the article 
https://wiki.lfaidata.foundation/display/EG/How+to+find+entities+and+relationships

At the time of writing, there is one restriction in the findEntities(), findRelationships() methods, which is that they do not support 
the 'IN' PropertyComparisonOperator. This is a temporary restriction and may well have changed by the time you read this, particularly 
if Egeria is using JanusGraph 0.5.3 or above.

### Performance of find operations
All the find operations listed above will attempt to use the most efficient (fastest) traversal strategy possible. From release 2.7 of Egeria, 
the graph repository includes a query plan capability that analyses each query to determine the most efficient query strategy. As a caller
of any of these methods you will generally achieve faster performance by making each query as specific as possible. If possible use the type 
filtering parameters to scope the query down to a subset of the type space, and if possible try to search on properties that will provide 
maximum discrimination between types. Where this is not possible, the repository connector will still perform the query as efficiently as it
can but the overall response time is likely to be much slower than for queries where it can perform a single graph traversal.

##Conformance with the Egeria specification

###Mandatory and Optional Features

The Egeria metadata collection interface defines a set of mandatory features plus a set of optional features that do not need to be 
supported by every metadata management system. 

The Egeria Conformance Test Suite (CTS) runs a set of tests against a metadata management server and documents the test results. The 
tests are assigned to different profiles, one of which is mandatory, the others optional. The metadata management server under test 
needs to support all the features of the mandatory profile and may additionally support the optional profiles.

The graph repository connector supports the mandatory profile and most of the optional profiles. The only optional profiles it does not 
support are for 'undo' and for 'historical queries'. Neither of these are 'impossible' - but they have not yet been investigated and may 
introduce additional complexity.

Support for the ‘undo’ (retraction) of an operation, such as an update or delete operation of an entity or relationship, would require 
the ability to (successively) retrieve the previous version of an object or to restore an object that was the subject of a delete.

Support for the Egeria operations to retrieve metadata pertaining to an earlier date/time would require history information to be 
retained and for consistent historic versions of metadata objects to be retrievable from the graph database.

##Internals

###Graph Database Schema
Inside a graph database, objects are stored as a graph of vertices connected by edges. Vertices and edges can have properties and the 
graph can have indexes on those properties.

The Egeria graph repository schema is designed as follows:

* Each Entity is stored as a Vertex
* Each Relationship is stored as an Edge - connecting two entity vertices
* A Classification is stored as a Vertex - and is connected to an entity vertex by an edge known as a Classifier.

In addition to the above, there is one further type of vertex in the graph; the Control vertex stores metadata about the graph database 
such as its metadataCollectionId and when the database was created. There is only (at most) one instance of the Contol vertex.

The above scheme gives rise to the following labels: Control, Entity, Relationship, Classification and Classifier. These labels are created 
during the initialize method of the GraphOMRSGraphFactory class.

####The Control Vertex
The control vertex exists to enable the graph repository connector to persist the identity of a metadata collection into the graph database, 
so that during start it can verify that it is opening the correct graph database, and that it is not already in use. This helps to protect 
against inadvertent attempts to use the same graph database from more than one server, or inadvertent attempts to access a graph database 
owned by a different server - e.g. due to a server configuration problem. 

If you look closely at the management of the control vertex, you'll notice that despite there only being one instance of the vertex, it has 
its own index! This index is created so that the graph database does not issue a 'scan' warning when retrieving the control vertex.

####Property Keys
To support the creation of indexes, the graph schema requires a set of property keys that provide coverage and unique identification for 
the core properties and type-defined attributes of all types of Egeria instance. This leads to a large number of property keys! You can 
find the property keys in GraphOMRSConstants. 

Although it would, theoretically, be possible to use the same key for similarly named core properties of an entity, relationship and 
classification, there is benefit in keeping the indexes as small as possible (for speed of lookup) so each of the categories uses a distinct 
set of property keys - e.g. PROPERTY_KEY_ENTITY_GUID is not the same as PROPERTY_KEY_CLASSIFICATION_GUID - they are used to construct 
separate indexes so the indexes are smaller and the database doesn't need to inspect the resulting vertex to check whether it represents 
an entity or classification.

###Graph Indexes
In the GraphOMRSGraphFactory's initialize() method, you will find the creation of the graph indexes. There are two broad types of index;
"composite" and "mixed". Both types of index make use of property keys as described above.

A composite index uses one or more keys (in a fixed composition) and support equality comparisons (only). This makes them useful for 
properties whose values should be matched exactly, such as a GUID (you generally wouldn't search for all instances with GUIDs starting 
"fe07", you would want to find the single instance that has the fully matching GUID). In the Egeria graph repo composite indexes are 
always based on a single key (property). Composite indexes are used for core properties such as GUID and typeName.

A mixed index is more flexible and can use any combination of property keys and support a broader range of operators, rather than just 
equality. This makes them more appropriate for properties that contain string values or dates - you can use regular expressions and 
inequalities, which is the basis for supporting date ranges. Mixed indexes are used for core properties such as createdBy, createTime, 
metadataCollectionName, amongst others.

For specific details about indexing in JanusGraph, see https://docs.janusgraph.org/index-management/index-performance.

###Maintaining the Graph DB Schema
The graph schema is written in such a way that as new types are added to the Open Metadata Type System, you should not need to add to 
the graph constants or graph factory. A new type is introduced by the verifyTypeDef method (in the metadata collection interface) and it 
will be dynamically added to the schema, with property keys and indexes added as needed. That takes care of the types and their 
type-defined attributes.

Suppose you need to add a new core property. You should add a new entry to the list of short names in GraphOMRSConstants and add its 
Java type to the corePropertyTypes map. Below that are the lists of constants for the construction of property keys and the maps for the 
association of core properties with the different categories. A new core property will need to be added to the list and map for all three
 categories: entities, relationships and classifications.

On final consideration is that you should spend a moment deciding what type of index to create for the new core property (composite or 
mixed, described above) and add the necessary index creation call to the GraphOMRSGraphFactory where you see the existing core properties 
being indexed. If you added mixed index, then also add an entry to the corePropertyMixedIndexMappings map (also in GraphOMRSGraphFactory) 
which dictates the type of mapping that the graph database will use - the most important aspect of this is whether the value is mapped as 
a String or not, as this affects how predicates will match it. If in doubt find a similar core property, if possible, and copy the entry 
for that! Note that collections (such as mappingProperties) are stored as a serialized copy of the collection and use a Text mapping for 
whole-word matching of the elements of the collection.

You should not need to change the worker methods in the GraphOMRSGraphFactory, such as ' createMixedIndexForVertexCoreProperty()' or worry 
about reindexing. These methods will detect whether a property key is new or existing and will initiate a reindex when needed.

With the above additions to the property keys, maps and indexes, you should just need to rebuild and deploy, then restart an affected 
server. The server's existing graph database should be augmented with the new keys and indexes.

###Internals of Find operations
The graph repository supports all the find methods listed in the section on 'Finding metadata instances'. All of the methods differ in 
terms of how the caller specifies how the search should be conducted, but internally they share a lot of common implementation. 

The findEntitiesByPropertyValue and findRelationshipsByPropertyValue methods identify the string properties of the eligible types and 
add them to a MatchProperties object before delegating the query to the GraphOMRSMetadataStore. This processing uses the same query 
analysis and query plan selection as the other find methods.

All of the 'find' methods support filtering of results by type, where the results will include the specified filter type and any of its 
subtypes. The find methods all perform the type filter checking in the top level (GraphOMRSMetadataCollection) and then delegate to a 
corresponding operation in the GraphOMRSMetadataStore. 

The GraphOMRSMetadataStore layer is either presented with a single type to search against or a set of valid types. The other search parmaters 
depend on the specific method called, and consist of either a MatchProperties object or a SearchProperties object. The GraphOMRSMetadataStore 
method parses the relevant properties object to construct a graph traversal that will identify and return the matching vertices or edges 
from the graph database. These are then converted, using the appropriate entity or relationship mapper into lists of EntityDetail or 
Relationship instances.

Finally, back in the GraphOMRSMetadataCollection, the resulting instances are processed by the extremely helpful OMRSRepositoryHelper, 
which formats the results in terms of paging and sequencing.

The query analysis and strategy selection tries where possible to perform a single graph traversal that has steps for all the referenced 
properties, type filtering, etc. This is a lot faster than performing a separate traversal per type and aggregating the results.

A tightly specified query (one with type filtering that narrows the scope of the query) is quite likely to contain only unique property 
names and in this case it is possible to perform a single traversal. A more loosely specified (or in the extreme, completely wild) query 
may encounter duplicate property names. Duplicates may be vertical or horizontal - as described below.

Where there are vertical duplications, such that a super type and one/more of its subtypes define a property with the same name, this is 
benign. The vertically duplicated properties must share the same type, otherwise the subtype would not be a refinement of the supertype. 
This means that the vertically duplicated type defined attributes can only differ in terms of things like 'description' which is not of 
interest to the graph repository. (Type definitions are not stored in the repository). The graph repository generates unique property keys 
used for both storage and retrieval, which include the name of the type that defines the attribute. These are always based on the most 
superior attribute definition - i.e. the definition from the type nearest the OpenMetataRoot type - so there is therefore no ambiguity 
from a vertically duplicated attribute name.

Horizontal duplicates are more challenging. These occur when a query refers to a property for which multiple types in the scope of the 
query define attributes with the same name. This is perfectly legitimate - otherwise there would be a single property name space across 
the entire type system. These duplicated type defined attributes may have different types or index mappings and are differentiated in the 
graph schema. As mentioned above, they are disambiguated by the generation of unique property keys that include the name of the most 
superior type that introduces the attribute. 
There are three possible approaches to handling horizontal duplicates. 
* One would be to include them in a single graph traversal, using an 'or()' step that covers the range of the mapping from short 
property name to qualified property names. However, at the time of writing, JanusGraph issue 2231 
([https://github.com/JanusGraph/janusgraph/issues/2231]) prevents this for the time being. 
* The second approach would be to partition the type space admitted by the query such that each partition contains no 
duplicates. Each resulting partition would then be the subject of a separate traversal, with the results being aggregated. However, 
the partitioning of the query type space into disjoint type spaces is considered a complex solution, requiring partitioning on different 
dimensions for each of the duplicated property names. Whilst this is probably feasible, if JG issue 2231 is resolved in the near future, 
the single traversal approach might be preferred as it should be both simpler and be more efficient.
* That leaves the third approach, which is effectively an extreme case of the partitioning approach, which is to perform a separate 
traversal per type and aggregate the results. This is slower, but is simple and is the least likely approach to stress the graph db.

The GraphOMRSQueryPlan in the graph repository connector analyses each query's parameters and recommends a query strategy - which 
is either 'iterated' (per type, slower) or 'delegated' (single traversal, faster). Wherever possible the QueryPlan will recommend a 
'delegated' traversal. The QueryPlan saves the property maps it generates during its analysis so they are available for the 
traversal construction methods.

Where a caller specifies type filtering that is sufficiently tight that there are no horizontally duplicated properties in the scope 
of the query, performance is maximised. It is therefore advisable to constrain a search by using the most specific type filtering and 
most type-specific property names possible.

###Instance Mappers
There are separate mappers for entities, relationships and classifications. Look for the classes like GraphOMRSEntityMapper. There are 
similar classes for relationships and classifications.

In each Mapper there are utility methods to add, remove or retrieve properties on the vertex or edge, plus two sets of methods: 
•	One set converts from an Instance to a Vertex or Edge. 
•	The other set converts in the other direction - from a Vertex or Edge to an Instance.

In the case of the EntityMapper, each set has methods to cover EntitySummary, EntityProxy and EntityDetail instances, and the latter 
two delegate the common logic (e.g. core properties) to the EntitySummary method.

###Graph Transactions
Every time a graph traversal is constructed, a new backend transaction is started by the graph database. It's important not to leave 
transactions hanging around, so each needs to be completed. Depending on the success or failure of the traversal the metadata store has 
to decide whether to commit or rollback the current transaction. 

A traversal may be abandoned due to an error condition, or circumvented due to detection of a combination of MatchProperties and 
MatchCriteria that prohibit any useful result. In these cases, the transaction is rolled back. In the traversal code, you will see a 
pattern in which a rollback is performed as soon as a terminating condition is detected, and the commit is executed at end of the method, 
just before the resulting instances are returned.

###RAS (FFDC and Audit)
The graph repository connector supports FFDC and Audit with the GraphOMRSAuditCode and GraphOMRSErrorCode classes. These should not 
hold many surprises as the implementation is similar to the audit logging and error handling of other Egeria components.

###Debugging
If you need to see what's actually happening at the interface to the graph database, you can enable logging.

For example, when launching the platform, you could add:
```
-Dlogging.level.org.odpi.openmetadata.adapters.repositoryservices.graphrepository.repositoryconnector=DEBUG
```
and this will show the interactions between the MetadataStore and the graph database.

This can be useful for things like searches, which will show the actual traversal being requested.

For example:
```
2021-01-19 18:19:10.422 DEBUG ... GraphOMRSMetadataStore : findEntitiesByProperty traversal looks like this --> [GraphStep(vertex,[]), HasStep([~label.eq(Entity), vetypeName.eq(OpenTriageEngine), veentityIsProxy.eq(false)]), OrStep([[HasStep([veReferenceablexqualifiedName.textRegex(AssetQuality)])]])] 
```

For a more detailed investigation you also have the option of starting a gremlin console and opening the graph database. To do this make 
sure you are using a JanusGraph download that is at least the same version as the version of JanusGraph used in your Egeria release. 
Also ensure that the graph database is not already open - easiest way to do that is to stop the server that has the repository that uses it.

You can then use gremlin to inspect the content of the database and perform traversals to work out why the graph repository is getting 
the results you have seen.

For example, given the configuration properties file shown in the appendix, you can start a gremlin console and do the following (and 
lots more not shown here!):

 

If you study the gremlin console session above, you will see that the graph is opened and a traversal created. This is followed by me 
directly accessing a particular vertex (with the id 4104) and showing its property values. How do you know the vertex id? Well in the 
main you won’t, and you will need to perform a property-based search, such as:

g.V().has('veReferenceablexqualifiedName','csv-asset-discovery-service-GovernanceServiceProvider')

However, at the time of writing I cannot do that because the JanusGraph 0.5.2 assembly I am using has an older version of Lucene than 
that used in the Egeria 3.13-SNAPSHOT build, so I am unable to use the index. I would need to build a JanusGraph assembly with a later 
version of Lucene, so the above cuts a massive corner, for the sake of illustration, so that you can see what an entity looks like when 
stored as a vertex in the graph database.

Another valuable tool within the gremlin console is to open the JanusGraph management API and retrieve the schema, property keys and 
graph indexes. The property keys and index names can be found in the GraphOMRSConstants and GraphOMRSGraphFactory classes. YOu can 
retrieve a key and its index as follows:
```
mgmt = graph.openManagement()

pkey = mgmt.getPropertyKey('veReferenceablexqualifiedName')

index = mgmt.getGraphIndex('vertexIndexPropertyveReferenceablexqualifiedName')

index.getIndexStatus(pkey)
```
  

Showing the whole schema would take a lot of space here, but if you want to see the schema, try the following:
```
mgmt.printschema()
```

This will print out a (very long!) description of the whole graph schema including the labels, property keys, vertex 
indexes and edge indexes with their states.

####Config file for gremlin properties
The following file is not needed for normal operation of Egeria. It may be useful to you if you want to connect a gremlin console to 
the database. The two lines that contain the long directory paths should not have line breaks.

```
# Copyright 2019 JanusGraph Authors
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# JanusGraph configuration sample: BerkeleyDB JE & Embedded Lucene
#
# This file opens a BDB JE instance in the directory
# /path/to/this/file/../db/berkeley and an index at /path/to/this/file/../db/searchindex

# The implementation of graph factory that will be used by gremlin server
#
# Default:    org.janusgraph.core.JanusGraphFactory
# Data Type:  String
# Mutability: LOCAL
gremlin.graph=org.janusgraph.core.JanusGraphFactory

# The primary persistence provider used by JanusGraph.  This is required.
# It should be set one of JanusGraph's built-in shorthand names for its
# standard storage backends (shorthands: berkeleyje, cassandrathrift,
# cassandra, astyanax, embeddedcassandra, cql, hbase, inmemory) or to the
# full package and classname of a custom/third-party StoreManager
# implementation.
#
# Default:    (no default value)
# Data Type:  String
# Mutability: LOCAL
storage.backend=berkeleyje

# Storage directory for those storage backends that require local storage.
#
# Default:    (no default value)
# Data Type:  String
# Mutability: LOCAL
storage.directory=../../../egeria-install/egeria-omag-3.13-SNAPSHOT/data/servers/Metadata_Server/repository/graph/berkeley

# The indexing backend used to extend and optimize JanusGraph's query
# functionality. This setting is optional.  JanusGraph can use multiple
# heterogeneous index backends.  Hence, this option can appear more than
# once, so long as the user-defined name between "index" and "backend" is
# unique among appearances.Similar to the storage backend, this should be
# set to one of JanusGraph's built-in shorthand names for its standard
# index backends (shorthands: lucene, elasticsearch, es, solr) or to the
# full package and classname of a custom/third-party IndexProvider
# implementation.
#
# Default:    elasticsearch
# Data Type:  String
# Mutability: GLOBAL_OFFLINE
#
# Settings with mutability GLOBAL_OFFLINE are centrally managed in
# JanusGraph's storage backend.  After starting the database for the first
# time, this file's copy of this setting is ignored.  Use JanusGraph's
# Management System to read or modify this value after bootstrapping.
index.search.backend=lucene

# Directory to store index data locally
#
# Default:    (no default value)
# Data Type:  String
# Mutability: MASKABLE
index.search.directory=../../../egeria-install/egeria-omag-3.13-SNAPSHOT/data/servers/Metadata_Server/repository/graph/searchindex
```
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
