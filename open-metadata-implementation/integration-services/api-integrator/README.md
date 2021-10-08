<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# API Integrator Open Metadata Integration Service (OMIS)

The API Integrator OMIS supports the exchange of data assets and their related schema and
connection information between an external API manager
and the open metadata ecosystem.

It supports a type of [integration connector](../../governance-servers/integration-daemon-services/docs/integration-connector.md)
that are able to create [DeployedAPI](../../../open-metadata-publication/website/open-metadata-types/0212-Deployed-APIs.md)
assets along with its associated API specification ([APISchemaType](../../../open-metadata-publication/website/open-metadata-types/0536-API-Schemas.md)).
Optionally, this metadata can be tied to an [APIManager](../../../open-metadata-publication/website/open-metadata-types/0050-Applications-and-Processes.md).

Figure 1 shows the types of metadata that integrators connectors can create with this integration service.

![Figure 1](../../access-services/data-manager/docs/api-model.png#pagewidth)
> **Figure 1:** API metadata supported by the API Integrator OMIS 

The structure of an API specification consist of a number of 
[APIOperation](../../../open-metadata-publication/website/open-metadata-types/0536-API-Schemas.md)s
which in turn have optional
[APIParameterList](../../../open-metadata-publication/website/open-metadata-types/0536-API-Schemas.md)s
for the header, request and response.
The details of these payloads are described using
[SchemaAttributes and SchemaTypes](../../../open-metadata-publication/website/modelling-technology/modelling-schemas.md).

Figure 2 shows the open metadata types used to represent the metadata created through this service.

![Figure 2](../../access-services/data-manager/docs/api-open-metadata-types.png#pagewidth)
> **Figure 2:** API open metadata types supported by the API Integrator OMIS 

If an APIManager is defined, any API created is automatically linked to it using the
[ServerAssetUse](../../../open-metadata-publication/website/open-metadata-types/0045-Servers-and-Assets.md)
relationship.
When an APIOperation is created for the API,
the service automatically inserts the
[AssetSchemaType](../../../open-metadata-publication/website/open-metadata-types/0503-Asset-Schema.md) relationship,
[APISchemaType](../../../open-metadata-publication/website/open-metadata-types/0536-API-Schemas.md) entity and
[APIOperations](../../../open-metadata-publication/website/open-metadata-types/0536-API-Schemas.md) relationship
in between the DeployedAPI entity and the APIOperation entity.

Any schema attributes created through this interface will use the
[APIParameter](../../../open-metadata-publication/website/open-metadata-types/0536-API-Schemas.md) subtype
to allow for find requests that only return schema information for APIs.  The schema attributes also use
the [TypeEmbeddedAttribute](../../../open-metadata-publication/website/open-metadata-types/0505-Schema-Attributes.md)
classification [method for defining the schema type](../../../open-metadata-publication/website/modelling-technology/modelling-schemas.md).


## Module Implementation

The modules are as follows:

* [api-integrator-api](api-integrator-api) - defines the interface for an integration
connector that is supported by the API Manager Integrator OMIS.  This includes the implementation
of the context that wraps the Data Manager OMAS's clients.

* [api-integrator-server](api-integrator-server) - implements the context manager for
the API Integrator OMIS.

* [api-integrator-spring](api-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [api-integrator-client](api-integrator-client) - implements a Java client for the REST API.


This integration service is paired with the [Data Manager](../../access-services/data-manager)
Open Metadata Access Service (OMAS).

----

* Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.