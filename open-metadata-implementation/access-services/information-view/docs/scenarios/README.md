<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Using the Information View OMAS(IV OMAS)

Below is the list of tasks supported by Information View OMAS.

## External Tool registration
In order for an external tool to submit reports or views to IV OMAS, first it needs to register as a software capability through IV OMAS. 
The result will be a new entity of type SoftwareServerCapability. The guid of this entity will be further used by the external entity when submitting new assets.
Sample of the request can be found here: [register-external-tool](../../information-view-server/docs/user/register-tool.md)
## External Tool lookup
In order to retrieve the guid of the entity created at above step, a lookup based on qualifiedName can be performed
Sample of the request can be found here: [lookup-external-tool](../../information-view-server/docs/user/lookup-registration.md)
## Report submit
The external tool can submit the metadata for a report. The submitted payload must also contain either the guid or the qualified name of the software server entity mentioned above.
An example can be found here: [submit-report](../../information-view-server/docs/user/submit-report.md)
## Report lookup
The report representation can be retrieved by using the reportId where reportId is the internal id coming from external tool.
## Data View submit
The external tool can submit the metadata for a view.
An example can be found here: [submit-view](../../information-view-server/docs/user/submit-data-view.md)
## Data View lookup 
The view representation can be retrieved by using the dataViewId where dataViewId is the internal id coming from external tool.
An example can be found here: [lookup-data-view](../../information-view-server/docs/user/lookup-data-view.md)
## Retrieve databases
This endpoint will return a list of all databases in the zones supported by IV OMAS. These zones are part of the configuration of IV OMAS 
An example can be found here: [retrieve-databases](../../information-view-server/docs/user/retrieve-databases.md)
## Retrieve tables for a database
This endpoint will return a list of all tables based on the database guid provided by previous endpoint
An example can be found here: [retrieve-tables](../../information-view-server/docs/user/retrieve-database-tables.md)
## Retrieve context for a table
This endpoint will return the table context(columns, schema, database, endpoint details) based on the table guid provided by previous endpoint
An example can be found here: [retrieve-table-context](../../information-view-server/docs/user/retrieve-table-context.md)
## Retrieve columns for a table
This endpoint will return the columns of the table based on the table guid 
An example can be found here: [retrieve-table-columns](../../information-view-server/docs/user/retrieve-table-columns.md)
## Publish Column Context Event on OUT topic

When a NEW_RELATIONSHIP_EVENT event of type SemanticAssignment between a RelationalColumn and a GlossaryTern is published on cohort topic, the full context of the RelationalColumn is retrieved and published on Information View Out topic



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.