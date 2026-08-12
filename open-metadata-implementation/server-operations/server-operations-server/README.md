<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Server Operations Server

The server operations server module contains the server-side implementation of the server operations.

* `OMAGServerOperationalServices` provides support to start, manage and stop services in the OMAG Server.
* `OMAGOperationalServicesInstance` provides the references to the active services for an instance of an
  OMAG Server, and `OMAGServerOperationalInstanceHandler` retrieves information from the (thread-safe)
  instance map for an OMAG server service instance.
* `OpenMetadataArchiveWrapper` provides an
  [open metadata archive store connector](https://egeria-project.org/concepts/open-metadata-archive-store-connector)
  implementation used when an archive's content is supplied directly, rather than read from a file.

----
Return to [Server Operations](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
