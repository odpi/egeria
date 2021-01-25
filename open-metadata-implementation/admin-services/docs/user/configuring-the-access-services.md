<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuring the Open Metadata Access Services (OMASs)

The [Open Metadata Access Services (OMASs)](../../../access-services) provide the domain-specific
APIs for metadata management and governance.
They run in a [metadata server](../concepts/metadata-server.md) or 
[metadata access point](../concepts/metadata-access-point.md) and typically offer a
REST API, Java client and an event-based interface for
asynchronous interaction.

## Prerequisite configuration

The access service configuration depends on the definitions of the [event bus](configuring-event-bus.md)
and the [local server's userId](configuring-omag-server-basic-properties.md).

## List the different types of access services

It is possible to list all of the available access services
that are registered with the [OMAG Server Platform](../concepts/omag-server-platform.md)
using the following command.

```
GET {platformURLRoot}/open-metadata/platform-services/users/{adminUserId}/server-platform/registered-services/access-services
```
The result looks something like this:

```json
{
    "relatedHTTPCode": 200,
    "services": [
        {
            "serviceName": "Asset Owner",
            "serviceURLMarker": "asset-owner",
            "serviceDescription": "Manage an asset",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-owner/"
        },
        {
            "serviceName": "Stewardship Action",
            "serviceURLMarker": "stewardship-action",
            "serviceDescription": "Manage exceptions and actions from open governance",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/stewardship-action/"
        },
        {
            "serviceName": "Subject Area",
            "serviceURLMarker": "subject-area",
            "serviceDescription": "Document knowledge about a subject area",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/subject-area/"
        },
        {
            "serviceName": "Governance Program",
            "serviceURLMarker": "governance-program",
            "serviceDescription": "Manage the governance program",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/governance-program/"
        },
        {
            "serviceName": "Asset Lineage",
            "serviceURLMarker": "asset-lineage",
            "serviceDescription": "Store asset lineage",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-lineage/"
        },
        {
            "serviceName": "Design Model",
            "serviceURLMarker": "design-model",
            "serviceDescription": "Exchange design model content with tools and standard packages",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/design-model/"
        },
        {
            "serviceName": "Glossary View",
            "serviceURLMarker": "glossary-view",
            "serviceDescription": "Support glossary terms visualization",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/glossary-view/"
        },
        {
            "serviceName": "Security Officer",
            "serviceURLMarker": "security-officer",
            "serviceDescription": "Set up rules to protect data",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/security-officer/"
        },
        {
            "serviceName": "Asset Consumer",
            "serviceURLMarker": "asset-consumer",
            "serviceDescription": "Access assets through connectors",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-consumer/"
        },
        {
            "serviceName": "IT Infrastructure",
            "serviceURLMarker": "it-infrastructure",
            "serviceDescription": "Manage information about the deployed IT infrastructure",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/it-infrastructure/"
        },
        {
            "serviceName": "Asset Catalog",
            "serviceURLMarker": "asset-catalog",
            "serviceDescription": "Search and understand your assets",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/asset-catalog/"
        },
        {
            "serviceName": "Data Science",
            "serviceURLMarker": "data-science",
            "serviceDescription": "Create and manage data science definitions and models",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/data-science/"
        },
        {
            "serviceName": "Community Profile",
            "serviceURLMarker": "community-profile",
            "serviceDescription": "Define personal profile and collaborate",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/community-profile/"
        },
        {
            "serviceName": "DevOps",
            "serviceURLMarker": "devops",
            "serviceDescription": "Manage a DevOps pipeline",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/dev-ops/"
        },
        {
            "serviceName": "Software Developer",
            "serviceURLMarker": "software-developer",
            "serviceDescription": "Interact with software development tools",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/software-developer/"
        },
        {
            "serviceName": "Discovery Engine",
            "serviceURLMarker": "discovery-engine",
            "serviceDescription": "Support for automated metadata discovery engines",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/discovery-engine/"
        },
        {
            "serviceName": "Data Engine",
            "serviceURLMarker": "data-engine",
            "serviceDescription": "Exchange process models and lineage with a data engine",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/data-engine/"
        },
        {
            "serviceName": "Project Management",
            "serviceURLMarker": "project-management",
            "serviceDescription": "Manage data projects",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/project-management/"
        },
        {
            "serviceName": "Governance Engine",
            "serviceURLMarker": "governance-engine",
            "serviceDescription": "Set up an operational governance engine",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/governance-engine/"
        },
        {
            "serviceName": "Information View",
            "serviceURLMarker": "information-view",
            "serviceDescription": "Support information virtualization and data set definitions",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/information-view/"
        },
        {
            "serviceName": "Digital Architecture",
            "serviceURLMarker": "digital-architecture",
            "serviceDescription": "Design of the digital services for an organization",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/digital-architecture/"
        },
        {
            "serviceName": "Data Privacy",
            "serviceURLMarker": "data-privacy",
            "serviceDescription": "Manage governance of privacy",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/data-privacy/"
        },
        {
            "serviceName": "Data Manager",
            "serviceURLMarker": "data-manager",
            "serviceDescription": "Capture changes to the data stores and data set managed by a technology managing collections of data",
            "serviceWiki": "https://egeria.odpi.org/open-metadata-implementation/access-services/data-manager/"
        }
    ]
}
```
These access services are available to configure either all together or individually.


## Configure all access services

To enable all of the access services (and the [Enterprise Repository Services](../../../repository-services/docs/subsystem-descriptions/enterprise-repository-services.md)
that support them) with default configuration values use the following command.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/access-services
```

## Configure an individual access service

Alternatively, each service can be configured individually with the following command:

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/access-services/{serviceURLMarker}
```
The service URL marker for each service is shown in the list above.

In both cases, it is possible to pass a list of properties to the access service
that controls the behavior of each access service.
These are sent in the request body.
More details of which properties are supported
are documented with each access service.

## Disable the access services


The access services can be disabled with the following command.
This also disables the enterprise repository services since they
are not being used.

```
DELETE {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/access-services
```

## Retrieve the current configuration for the access services

It is possible to retrieve the 

```
GET {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/access-services/configuration
```

it is then possible to make changes to the configuration and 
save it back:

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/access-services/configuration
```

----
* Return to [Configuring an OMAG Server](configuring-an-omag-server.md)
* Return to [Configuring a Metadata Access Point](../concepts/metadata-access-point.md#Configuring-a-Metadata-Access-Point)
* Return to [Configuring a Metadata Server](../concepts/metadata-server.md#Configuring-a-Metadata-Server)
* Return to [configuration document structure](../concepts/configuration-document.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.