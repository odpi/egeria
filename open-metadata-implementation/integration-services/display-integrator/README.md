<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Display Integrator Open Metadata Integration Service (OMIS)

The Display Integrator OMIS supports the exchange of displayed data such as reports and forms along with their related
schema and
connection information between an external system supporting the data display
and the open metadata ecosystem.

It supports a type of [integration connector](../../governance-servers/integration-daemon-services/docs/integration-connector.md)
that are able to create assets representing forms, reports and queries
along with their associated display schema .

## Forms and Reports

Figure 1 shows the types of metadata for forms and reports that integrators connectors can create with this integration service.


![Figure 1 - Form](../../access-services/data-manager/docs/form-model.png#pagewidth)
<br><br>
![Figure 1 - Report](../../access-services/data-manager/docs/report-model.png#pagewidth)
> **Figure 1:** Forms and Reports metadata supported by the Display Integrator OMIS 

Either can be represented as a set of nested **DataContainers** with specific input and output
**DataFields** use to represent the individual data items. 


## Queries

Integrators connectors can also create queries that support forms and reports with this integration service.
Figure 2 shows the types of metadata for the queries.

![Figure 2 - InformationView](../../access-services/data-manager/docs/information-view-model.png#pagewidth)
> **Figure 2:** Query metadata supported by the Display Integrator OMIS 

Queries can also be represented as a set of nested data containers and data fields.

## Open Metadata Types

Figure 3 shows the open metadata types used to represent the metadata for forms, reports and queries
created through this service.

![Figure 3 - Form](../../access-services/data-manager/docs/form-open-metadata-types.png#pagewidth)
<br><br>
![Figure 3 - Report](../../access-services/data-manager/docs/report-open-metadata-types.png#pagewidth)
<br><br>
![Figure 2 - InformationView](../../access-services/data-manager/docs/information-view-open-metadata-types.png#pagewidth)
> **Figure 3:** Form and Report open metadata types supported by the Display Integrator OMIS 

The principle asset types are
[Form](../../../open-metadata-publication/website/open-metadata-types/0239-Reports.md),
[Report](../../../open-metadata-publication/website/open-metadata-types/0239-Reports.md) and
[InformationView](../../../open-metadata-publication/website/open-metadata-types/0235-Information-View.md)
for forms, reports and queries respectively.

If a SoftwareServerCapability such as an
[Application](../../../open-metadata-publication/website/open-metadata-types/0050-Applications-and-Processes.md) or 
[ReportingEngine](../../../open-metadata-publication/website/open-metadata-types/0055-Data-Processing-Engines.md) is defined,
any asset created is automatically linked to it using the
[ServerAssetUse](../../../open-metadata-publication/website/open-metadata-types/0045-Servers-and-Assets.md)
relationship.

When a **DataContainer** is created for a report or a form, it is represented as a
[DisplayDataContainer](../../../open-metadata-publication/website/open-metadata-types/0537-Display-Schemas.md) entity.
**DataFields** are created as
[DisplayDataField](../../../open-metadata-publication/website/open-metadata-types/0537-Display-Schemas.md) entities.

The **DataContainers** and **DataFields** for a queries are represented as
[QueryDataContainer](../../../open-metadata-publication/website/open-metadata-types/0537-Display-Schemas.md) and
[QueryDataField](../../../open-metadata-publication/website/open-metadata-types/0537-Display-Schemas.md) repectively.

All of these types are subtypes of schema attributes that
the [TypeEmbeddedAttribute](../../../open-metadata-publication/website/open-metadata-types/0505-Schema-Attributes.md)
classification [method for defining the schema type](../../../open-metadata-publication/website/modelling-technology/modelling-schemas.md).

The different subtypes enable metadata searches to narrow the results to this type of display metadata.

When a data container or a data field is attached to a form or report asset, the service automatically inserts the
[AssetSchemaType](../../../open-metadata-publication/website/open-metadata-types/0503-Asset-Schema.md) relationship,
[DisplaySchemaType](../../../open-metadata-publication/website/open-metadata-types/0537-Display-Schemas.md) entity and
[AttributeForSchema](../../../open-metadata-publication/website/open-metadata-types/0505-Schema-Attributes.png) relationship
in between the asset entity and the data container/field entity if not already in place.

Similarly, the combination of
[AssetSchemaType](../../../open-metadata-publication/website/open-metadata-types/0503-Asset-Schema.md) relationship,
[QuerySchemaType](../../../open-metadata-publication/website/open-metadata-types/0537-Display-Schemas.md) entity and
[AttributeForSchema](../../../open-metadata-publication/website/open-metadata-types/0505-Schema-Attributes.png) relationship
is inserted between a query and a data container/field.

When a data field/container is nested inside another, it is 
linked to the structure using the
[NestedSchemaAttribute](../../../open-metadata-publication/website/open-metadata-types/0505-Schema-Attributes.png) relationship.


## Module Implementation

This module is not implemented yet.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.