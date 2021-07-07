<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.11 (July 2021)

Release 2.11 adds:
* New option to have multiple topics for a cohort.
* New support for automatic extraction of metadata from event brokers and API managers.

Details of these and other changes are in the sections that follow.

## Description of Changes

### New option to have multiple topics for a cohort

An [open metadata repository cohort](../open-metadata-implementation/admin-services/docs/concepts/cohort-member.md)
uses three types of events to enable peer-to-peer sharing of metadata.
In previous releases, these events have all been exchanged through a single
event topic.  Release 2.11 provides the option for a cohort to
use a dedicated topic for each type of event.
This improves the time to register a new member in the cohort
and allows multiple instances of a [Metadata Server](../open-metadata-implementation/admin-services/docs/concepts/metadata-server.md)
or a [Metadata Access Point](../open-metadata-implementation/admin-services/docs/concepts/metadata-access-point.md)
to access the cohort.

Details of these new options can be found in the
[Administration Guide](../open-metadata-implementation/admin-services/docs/user/configuring-registration-to-a-cohort.md).

### Extensions to the support for Data Managers

The [Data Manager OMAS](../open-metadata-implementation/access-services/data-manager)
has been extended to support the capture of metadata from
Event Brokers such as Apache Kafka and API Managers
such as an API Gateway.

There are two new associated
Open Metadata Integration Services (OMISs)
to support integration connectors 
that extract metadata from these types of data managers:

* [Topic Integrator OMIS](../open-metadata-implementation/integration-services/topic-integrator)
supports integration connectors extracting metadata
from Event Brokers.

* [API Integrator OMIS](../open-metadata-implementation/integration-services/api-integrator)
supports integration connectors extracting API information from API managers.

For more information on the use of the Data Manager OMAS with
these integration services, see the 
[Data Manager Integration](../open-metadata-publication/website/solutions/data-manager-integration)
solution.

### Open Metadata Types

The following changes have been made to the open metadata types:

* Deprecation of properties in **Asset**

  A number of properties that where originally defined in Asset were moved to
  classifications to allow them to be managed independently of the original asset.
  This occurred before the TypeDefPatch support was in place and so these properties
  were not marked as deprecated at that time.  In 2.11, this deprecation has now been
  officially recorded in the Asset TypeDef.  The properties are:
  
   * **owner** - now captured in the [Ownership](../open-metadata-publication/website/open-metadata-types/0445-Governance-Roles.md) classification
   * **ownerType** - also captured in the Ownership classification
   * **zoneMembership** - now captured in the [AssetZoneMembership](../open-metadata-publication/website/open-metadata-types/0424-Governance-Zones.md) classification
   * **latestChange** - now captures]d in the [LatestChange](../open-metadata-publication/website/open-metadata-types/0011-Managing-Referenceables.md) classification
  
  See new type description in model [0010](../open-metadata-publication/website/open-metadata-types/0010-Base-Model.md).

* New types for **APIManager** and **EventBroker**.
  These types inherit from [SoftwareServerCapability](../open-metadata-publication/website/open-metadata-types/0042-Software-Server-Capabilities.md).
  These are used in the new Data Manager OMAS APIs.
  
  See new type descriptions in model [0050](../open-metadata-publication/website/open-metadata-types/0050-Applications-and-Processes.md).

* New type for a **Threat** governance driver and a relationship to connect
  a [GovernanceDefinition](../open-metadata-publication/website/open-metadata-types/0401-Governance-Definitions.md)
  with a metadata element that defines the scope where it is applicable.

  See new type descriptions in models [0401](../open-metadata-publication/website/open-metadata-types/0401-Governance-Definitions.md) and
  [0405](../open-metadata-publication/website/open-metadata-types/0405-Governance-Drivers.md).

* Deprecated type called **TabularColumnType** because it restricts tabular columns to primitive types when it could be a literal for example.
  Also a new subtype for **TabularColumn** called **TabularFileColumn** is added to be able to distinguish between tabular columns from
  files and **RelationalColumn** (which also inherits from **TabularColumn**).

  See type descriptions in model [0530](../open-metadata-publication/website/open-metadata-types/0530-Tabular-Schemas.md).

* Deprecated types called **SimpleDocumentType**, **StructDocumentType** and **MapDocumentType** because they
  offer little value since the type is typically stored in the **TypeEmbeddedAttribute** classification.
  This change makes the document schemas consistent with other types of schema.

  See type descriptions in model [0531](../open-metadata-publication/website/open-metadata-types/0531-Document-Schemas.md).

* Updated supertype of **RelationalTableType** to inherit from **ComplexSchemaType** rather than **TabularColumnType**
  since TabularColumnType is now deprecated.

  See type descriptions in model [0534](../open-metadata-publication/website/open-metadata-types/0534-Relational-Schemas.md).

* New type called **EventTypeList** to allow a list of event types to be associated with a topic
  and a specific subtype of [SchemaAttribute](../open-metadata-publication/website/open-metadata-types/0505-Schema-Attributes.md)
  for an attribute in an event type to make it easier to search for
  data fields that are exclusively found in events.
  
  See new type descriptions in model [0535](../open-metadata-publication/website/open-metadata-types/0535-Event-Schemas.md).
  
* New types for **APIParameter** and **APIParameter** to allow the capture of properties
  related to the API's treatment of the parameters.  There are also
  properties for **APIOperation**.
  
  See new type descriptions in model [0536](../open-metadata-publication/website/open-metadata-types/0536-API-Schemas.md).

* New types for **DisplayDataSchemaType**, **DisplayDataContainer**, **DisplayDataField**, **QuerySchemaType**, 
  **QueryDataContainer** and **QueryDataField** to allow the capture of properties related to the display of data to end users.
  
  See new type descriptions in model [0537](../open-metadata-publication/website/open-metadata-types/0537-Display-Schemas.md).

* Deprecated type called **TermISATypeOFRelationship** because the ends are defined the wrong way round. When visualising 
end1 should point to end2. This relationship incorrectly has the super type pointing to the subtype. Use **IsATypeOfRelationship**
instead.  

* New type **IsATypeOfRelationship** to represent an is-a-type-of relationship between two spine objects. 


### Bug fixes and other updates

* Additional Bug Fixes
* Dependency Updates

For details on both see the commit history in GitHub.

## Removed Functionality

The following capabilities have now been removed (they were previously deprecated and/or in not in 'Released' status):
* Information View OMAS.
* Virtualization Services & associated connectors.
* Security Officer Services, Security sync services & associated connectors including for Apache Ranger.
* Gaian database connector & additional authentication/impersonation support.

Much of the above capability can be implemented via [Integration Services](https://egeria.odpi.org/open-metadata-implementation/integration-services/) .
Hadoop specifics may be developed in the future within the [Egeria Hadoop](https://github.com/odpi/egeria-connector-hadoop-ecosystem) GitHub repository.

For more details of this change see [#5314](https://github.com/odpi/egeria/pull/5314).

Similarly, the following services have been deleted:

* Data Platform Services and Data Platform OMAS. For more details see [#5344](https://github.com/odpi/egeria/pull/5344) .
* Data Platform capabilities are already available in [Database Integrator](https://egeria.odpi.org/open-metadata-implementation/integration-services/database-integrator/) and [Files Integrator](https://egeria.odpi.org/open-metadata-implementation/integration-services/files-integrator/) already part of Integration Services. 
* Cassandra connectors: cassandra-data-store-connector and cassandra-metadata-extractor-connector will be introduced back
in the [Data Connectors](https://github.com/odpi/egeria-data-connectors) GitHub repository. For more information see [#2671](https://github.com/odpi/egeria/issues/2671) .

## Known Issues

* It is recommended to use a chromium-based browser such as Google Chrome or Microsoft Edge, or Apple Safari for the Egeria React UI. Some parts of the UI experience such as Dino currently experience problems with Firefox. See [odpi/egeria-react-ui#96](https://github.com/odpi/egeria-react-ui/issues/96) .

## Special note on Java Support

* This is expected to be the last release supporting Java 8. The next release will commence the 3.x series of releases and require Java 11.

# Egeria Implementation Status at Release 2.11

![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-2.11.png#pagewidth)

Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
Open Metadata and Governance vision, strategy and content.


# Further Help and Support

See the [Community Guide](../Community-Guide.md).

----
* Return to [Release Notes](.)
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
