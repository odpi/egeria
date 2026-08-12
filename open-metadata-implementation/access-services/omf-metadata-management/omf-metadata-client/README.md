<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMF Metadata Management Client

OMF Metadata Management supports a REST API for requests and an event-based
interface for asynchronous integration.  This client module provides the Java client classes that make it
easier for governance servers and applications to call these interfaces.

* `EgeriaOpenMetadataStoreClient` (via `OpenMetadataClientBase`) provides a comprehensive interface for
  searching, creating, updating and deleting open metadata elements, classifications and relationships,
  subject to the user's security permissions.
* `EgeriaOpenMetadataEventClient` sets up a listener to receive inbound events from the open metadata store's
  out topic.
* `OMFRESTClient` provides the underlying REST call support used by the other clients.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
