<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMF Metadata Management server-side implementation

`OMFOperationalServices` initializes the REST services that support the [Open Metadata Framework (OMF)](../../../frameworks/open-metadata-framework)
open metadata store calls.  The server-side support is organized as follows:

* `OpenMetadataStoreRESTServices` implements the REST API for searching, creating, updating and deleting open
  metadata elements, classifications and relationships.
* `MetadataElementHandler` provides the underlying handler for these elements, working with
  `MetadataElementBuilder` and the converter classes (`MetadataElementConverter`, `ElementHeaderConverter`,
  `OpenMetadataRelationshipConverter`, `RelatedElementConverter`, `OpenMetadataElementStubConverter`,
  `OpenMetadataStoreConverter`) to translate open metadata repository instances into the beans returned
  through the API.
* `OpenMetadataOMRSTopicListener` and `OpenMetadataOutTopicPublisher` manage the events published on the
  service's out topic to notify listeners of relevant changes to open metadata.
* `OMFServicesInstance` caches the server's runtime state for use by these handlers.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
