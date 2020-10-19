<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Lineage Access Service Design

### Asset Lineage Admin
The Asset Lineage Admin class extends the abstract AccessServiceAdmin class in order to be registered and to receive its configuration. 
The initialization call provides this OMAS with resources from the Open Metadata Repository Services.
 
### Asset Lineage Audit Log

The [AuditLogFramework](/../../../frameworks/audit-log-framework/README.md) is used to log the audit exceptions and messages in the OMAS implementation.

### Asset Lineage Listeners

AssetLineageOMRSTopicListener received details of each OMRS event from the cohorts that the local server is connected to. 
 It passes [Lineage Entity events](../../../asset-lineage-api/docs/events/README.md) to the Asset Lineage publisher.
 
### Asset Lineage Handlers
The current implementations uses different types of handler based on the type of the entity that is processed.
These handlers use the Enterprise Connector to fetch entities context based on the spefic Open Metadata relationships.

### Asset Lineage Publisher
The published sends out the Lineage Event on Asset Lineage Output Topic.

### Asset Lineage REST Service
The REST interface expose an endpoint described [here](../user/README.md).

### Asset Lineage Converters
The converters are responsible for converting entities, classifications and relationships retrieved from the
open metadata repositories into Asset Lineage OMAS beans.
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.