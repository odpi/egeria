<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Managing external identifiers

## Motivation

Every open metadata instance has a unique identifier called the
[Global Unique Identifier or GUID for short](../basic-concepts/guid.md).
This provides a means to locate and retrieve the instance from the metadata repository.
However, often the GUID is not known in the systems and tools that integrate
with open metadata.  Metadata instances that inherit from the
[Referenceable](../open-metadata-types/0010-Base-Model.md) type
have a property called **qualifiedName**.  This is a unique name for
the referenceable instance.
When such an instance is created, the qualified name can
be set up to a unique identifier that is a natural unique name for the
resource that it represents (such as the full path name of a file)
or a unique identifier from an external tool.  The element
can then be retrieved using the qualifiedName.

Now consider the situation where each external tool that uses the instance has
a different identifier for the instance.  There is only one qualified name property
in the instance which will not be able to cover all of the identifiers from
the external systems/tools.

## Using External Identifiers

In this situation it is possible to set up multiple **External Identifiers**
for an open metadata instance.
Each external identifier is linked to the open metadata instance it represents
and the software server capability of the external system/tool that uses it.
You can think of this link to the software server capability as providing a scope
in which the external identifier is valid.

The external identifiers can support both one-to-many, many-to-one and many-to-many
between metadata elements from external systems/tools and open metadata instances.

Figure 1 shows a situation where an external tool
call myCatalog uses two metadata elements:
one of type **BusinessTerm** and the other of type **Example**, to represent all of the
properties that are stored in one open metadata
[GlossaryTerm](../open-metadata-types/0330-Terms.md).
To represent this in open metadata, the unique identifiers for the business term and
example metadata elements
(gt1 and ex6 respectively) are each stored in their own external identifier
that is linked to both the myCatalog's software server capability and
the corresponding open metadata glossary term.  This means the
glossary term can be located in an open metadata repository
either using the identifier gt1 or ex6.
Similarly, it is possible to locate a glossary term's properties in myCatalog
by looking up both the gt1 and ex6 elements.


![Figure 1](external-identifiers-many-to-one-mapping.png#pagewidth)
> **Figure 1:** Many external metadata elements mapping to one open metadata instance


Figure 2 shows the opposite situation where it takes multiple open metadata
instances to represent a single metadata element in an external system/tool.
In this example the external system/tool directly links its **Database**
elements to its **Table** elements.  Whereas in the open metadata types,
there is a [SchemaType](../open-metadata-types/0501-Schema-Elements.md)
(specifically
[RelationalDBSchemaType](../open-metadata-types/0534-Relational-Schemas.md)) between a
[DeployedDatabaseSchema](../open-metadata-types/0534-Relational-Schemas.md)
instance and the
[RelationalTable](../open-metadata-types/0534-Relational-Schemas.md) instance.

Again an external identifier is created for each of the external metadata elements
and this is linked to the software server capability for myCatalog.
Each external identifier is then linked to each of the open metadata instances that have properties that map to its
equivalent metadata element in the external system/tool.

![Figure 2](external-identifiers-one-to-many-mapping.png#pagewidth)
> **Figure 2:** One external metadata element mapping to many open metadata instances

The use of external identifiers is particularly important to the
[integration connectors](../../../open-metadata-implementation/governance-servers/integration-daemon-services/docs/integration-connector.md) running in the
[Open Metadata Integration Services (OMIS)](../../../open-metadata-implementation/integration-services)
where the ability to maintain consistent metadata stores in both open metadata and third party systems
and tools is important.

## Open metadata types for 

The open metadata types for external identifier are in 
model [0017](../open-metadata-types/0017-External-Identifiers.md).
The **ExternalIdLink** relationship
is between the external identifier and the open metadata instance it represents.
The **ExternalIdScope** is the relationship between the external identifier and the
software server capability that represents the external system/tool.

## Implementations of external identifiers

The [Asset Manager OMAS](../../../open-metadata-implementation/access-services/asset-manager)
provides support for external identifier mapping on its APIs.
This capability is visible through the 
[Catalog Integrator OMIS](../../../open-metadata-implementation/integration-services/catalog-integrator)
and the [Lineage Integrator OMIS](../../../open-metadata-implementation/integration-services/lineage-integrator)
that are based on the Asset Manager OMAS client.

The [Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework)
provides the ability to query the external identifiers attached to an asset
through the 
[Connected Asset Properties](../../../open-metadata-implementation/frameworks/open-connector-framework/docs/concepts/connected-asset-properties.md).
This is also visible through the **AssetUniverse** interfaces of the:
[Asset Consumer OMAS](../../../open-metadata-implementation/access-services/asset-consumer).
[Asset Owner OMAS](../../../open-metadata-implementation/access-services/asset-owner) and
[Discovery Engine OMAS](../../../open-metadata-implementation/access-services/discovery-engine) interfaces.

 

----
* Return to [Glossary](../open-metadata-glossary.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.