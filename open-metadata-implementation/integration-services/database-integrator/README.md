<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![TechPreview](../../../open-metadata-publication/website/images/egeria-content-status-tech-preview.png#pagewidth)

# Database Integrator Open Metadata Integration Service (OMIS)

The Database Integrator OMIS supports the exchange of data assets and their related schema and
connection information between an external relational database manager
and the open metadata ecosystem.

It supports a type of [integration connector](../../governance-servers/integration-daemon-services/docs/integration-connector.md)
that are able to create [Database](../../../open-metadata-publication/website/open-metadata-types/0224-Databases.md)
and [DeployedDatabaseSchema](../../../open-metadata-publication/website/open-metadata-types/0224-Databases.md)
assets along with is associated [tables and columns](../../../open-metadata-publication/website/open-metadata-types/0534-Relational-Schemas.md).
Optionally, this metadata can be tied to a [Database Manager](../../../open-metadata-publication/website/open-metadata-types/0224-Databases.md)
also known as a Database Management System (DBMS).

Figure 1 shows the types of metadata that integrators connectors can create with this integration service.

![Figure 1](../../access-services/data-manager/docs/relational-database-model.png#pagewidth)
> **Figure 1:** Database metadata supported by the Database Integrator OMIS 

The structure of a database schema consist of a number of database tables and database views.
The difference is that the database table has its values stored in the database whereas the
database view is a set of values that are derived from accessing other
database tables using a query.

The structure of both the database tables and database views are describes by database columns.

Database columns have a types that represent a single value.  This can be stored, or derived using a query.
If the column presents the unique for a row within the table, it can be decorated with the
[PrimaryKey](../../../open-metadata-publication/website/open-metadata-types/0534-Relational-Schemas.md) classification.
If a column contains values that are the primary key of another table then the two columns can be linked using the
[ForeignKey](../../../open-metadata-publication/website/open-metadata-types/0534-Relational-Schemas.md) relationship.

Figure 2 shows the open metadata types used to represent the metadata created through this service.

![Figure 2](../../access-services/data-manager/docs/relational-database-open-metadata-types.png#pagewidth)
> **Figure 2:** Database open metadata types supported by the Database Integrator OMIS 

A database schema is represented using the
[DeployedDatabaseSchema](../../../open-metadata-publication/website/open-metadata-types/0224-Databases.md) entity,
the database table is represented using the 
[RelationalTable](../../../open-metadata-publication/website/open-metadata-types/0534-Relational-Schemas.md) entity
and the database column is represented using the 
[RelationalColumn](../../../open-metadata-publication/website/open-metadata-types/0534-Relational-Schemas.md) entity.
A database view is a 
[RelationalTable](../../../open-metadata-publication/website/open-metadata-types/0534-Relational-Schemas.md) entity
with the [Calculated Value](../../../open-metadata-publication/website/open-metadata-types/0512-Derived-Schema-Elements.md)
classification attached.

If a DatabaseManager is defined, any Database created is automatically linked to it using the
[ServerAssetUse](../../../open-metadata-publication/website/open-metadata-types/0045-Servers-and-Assets.md)
relationship.
When a DeployedDatabaseSchema is created for the Database,
the service automatically inserts the
[DataContentForDataSet](../../../open-metadata-publication/website/open-metadata-types/0503-Asset-Schema.md) relationship.
(The database schema is a DataSet over the database which is a type of DataStore).

When a RelationalTable is created for the DeployedDatabaseSchema,
the service automatically inserts the
[AssetSchemaType](../../../open-metadata-publication/website/open-metadata-types/0503-Asset-Schema.md) relationship,
[RelationalDBSchemaType](../../../open-metadata-publication/website/open-metadata-types/0534-Relational-Schemas.md) entity and
[AttributeForSchema](../../../open-metadata-publication/website/open-metadata-types/0505-Schema-Attributes.md) relationship
in between the DeployedAPI entity and the RelationalTable entity.

The SchemaType for a RelationalTable is [RelationalTableType](../../../open-metadata-publication/website/open-metadata-types/0534-Relational-Schemas.md).
The schema attributes for RelationalTable and RelationalColumn also use
the [TypeEmbeddedAttribute](../../../open-metadata-publication/website/open-metadata-types/0505-Schema-Attributes.md)
classification [method for defining the schema type](../../../open-metadata-publication/website/modelling-technology/modelling-schemas.md).


## Module Implementation

The modules are as follows:

* [database-integrator-api](database-integrator-api) - defines the interface for an integration
connector that is supported by the Database Integrator OMIS.  This includes the implementation
of the context that wraps the Data Manager OMAS's clients.

* [database-integrator-server](database-integrator-server) - implements the context manager for
the Database Integrator OMIS.

* [database-integrator-spring](database-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [database-integrator-client](database-integrator-client) - implements a Java client for the REST API.

This integration service is paired with the [Data Manager](../../access-services/data-manager)
Open Metadata Access Service (OMAS).

----

* Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.