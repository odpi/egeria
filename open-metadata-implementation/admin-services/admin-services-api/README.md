<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Admin Services API

The admin services API module provides the property structures and
REST structures used to configure the open metadata
and governance services.

* The **classifier** package classifies an OMAG server's configuration document to determine the type of
  server it defines.
* The **properties** and **rest** packages provide the Java beans used, respectively, exclusively on the API
  and to wrap the request bodies and responses of the REST API.
* The **ffdc** package defines the exceptions and audit log messages used by the admin services.
* The **store** package contains the interface definition for the
  [configuration document store connector](https://egeria-project.org/concepts/configuration-document-store-connector)
  - the connector used to read and write configuration documents.  Egeria's default implementation stores each
  configuration document in a separate file.

----
Return to [Admin Services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.