<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Administration Services Server

The administration services server module contains the server-side
implementation of the administration services.

* `OMAGServerAdminServices` provides the configuration calls common to all types of OMAG Server, and
  `OMAGServerAdminStoreServices` manages the reading, writing and deployment of configuration documents.
* `OMAGServerAdminForAccessServices`, `OMAGServerAdminForEngineHostServices`,
  `OMAGServerAdminForIntegrationDaemonServices` and `OMAGServerAdminForViewServices` provide the configuration
  calls specific to servers running access services, engine services, integration services and view services
  respectively.
* `OMAGServerAdminSecurityServices` configures the security connector used to authorize access to a server.
* `OMAGConformanceSuiteConfigServices` configures a
  [conformance test server](https://egeria-project.org/guides/cts/overview).
* `OMAGServerErrorHandler` provides common parameter validation used across these services.

----
Return to [Admin Services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.