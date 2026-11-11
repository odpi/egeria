<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# The OpenAPI Integration Connector

The OpenAPI integration connector catalogs APIs by extracting their OpenAPISpecification from the swagger endpoint.

## Deployment and configuration

The Open API integration connector is included in the main Egeria assembly.
It runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

The Open API monitor integration connector connects to an endpoint and extracts the open API specification through the `GET {{serverURL}}/v3/api-docs` request.  It creates one [DeployedAPI](https://egeria-project.org/types/2/0212-Deployed-APIs) asset for the specification (the API's tags are recorded in its additional properties), with an APISchemaType beneath it. A new [APIOperation](https://egeria-project.org/types/5/0536-API-Schemas) is created for each combination of path name and operation (GET, POST, PUT, DELETE).

When no endpoint is configured, the connector monitors every catalogued Endpoint whose network address starts with `http`.  It looks for these endpoints when it starts, and picks up new ones from Endpoint creation events.

### Parameters and reusable beans

Each operation has up to three APIParameterLists - *Request* (URL parameters and the request body), *Response* (one APIParameter per status code) and *Header* (header parameters and security schemes).  Each APIParameter carries a TypeEmbeddedAttribute classification describing its type.

The beans in the specification's `components/schemas` section are catalogued once per API, rather than once for every place they are used:

* Each bean (a component schema with fields of its own, or a subclass of a bean) becomes a StructSchemaType called `DeployedAPI:{{serverURL}}::Bean::{{beanName}}`, anchored to the DeployedAPI asset.  Its fields are APIParameters attached to it with the AttributeForSchema relationship.
* A parameter or field whose type is a bean, or an array of a bean, has a TypeEmbeddedAttribute with `schemaTypeName` set to `ExternalSchemaType`, and is linked to the bean's StructSchemaType with the [LinkedExternalSchemaType](https://egeria-project.org/types/5/0507-External-Schema-Type) relationship.  Arrays set `maxCardinality` to -1 on the relationship to their parent.
* Schema attributes can only have one parent, so the bean's fields belong to its StructSchemaType alone and are shared through the link.  A bean that refers to itself (such as `OMAGConnectorProperties.nestedConnectors` or `TechnologyTypeHierarchy.subTypes`) simply links back to its own schema type.
* A subclass bean (written as `allOf` its superclass's `$ref` and its own fields) holds only the fields it declares, and its StructSchemaType is linked to its superclass's StructSchemaType with LinkedExternalSchemaType.  Inherited fields are found by following that chain, so each field is catalogued once, on the bean that declares it.
* A parameter or field that may hold any one of several beans (`oneOf` or `anyOf`, used for polymorphic values such as a `Connection` or a `VirtualConnection`) is linked with LinkedExternalSchemaType to a [SchemaTypeChoice](https://egeria-project.org/types/5/0501-Schema-Elements) whose SchemaTypeOption relationships point to each alternative bean's StructSchemaType.  One choice is catalogued per distinct set of alternatives, called `DeployedAPI:{{serverURL}}::Choice::{{name}}::{{hash}}`, where the name is the nearest superclass that every alternative inherits from (or `OneOf` if they share none) and the hash identifies the full set.
* Enums, primitive types and objects with no fields are described inline by the TypeEmbeddedAttribute, and inline objects (those without a `$ref`) are catalogued as nested attributes.

Elements are matched by qualified name, so a refresh only adds what is missing; it does not update or remove elements for operations or beans that have changed (missing fields, superclass links and choice options are added).  An API catalogued by an earlier version of this connector, which copied every bean's fields under each use, keeps that structure until its DeployedAPI asset is deleted and the API is catalogued again.

![Figure 1](docs/open-api-monitor-integration-connector.png)
> **Figure 1:** Operation of the Open API monitor integration connector


## Configuration

This connector runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

This is its connection definition to use on the [administration commands that configure the integration daemon](https://egeria-project.org/guides/admin/servers/by-server-type/configuring-an-integration-daemon).

```json linenums="1" hl_lines="15"
{
   "connection" : 
                { 
                    "class" : "Connection",
                    "qualifiedName" : "APIMonitorConnection",
                    "connectorType" : 
                    {
                        "class" : "ConnectorType",
                        "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider"
                    },
                    "endpoint" :
                    {
                        "class" : "Endpoint",
                        "address" : "{{serverURL}}"
                    }
                }
}
```

- Replace `{{serverURL}}` with the network address of the process where the API is hosted (for example, `localhost:7443`).


----
* Return to [Integration Connectors module](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.