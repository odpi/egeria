<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Platform Services Server

The platform services server module contains the server-side implementation of the platform services.

* `OMAGServerPlatformOperationalServices` provides support to start, manage and stop the OMAG Servers
  running on the platform.
* `OMAGServerPlatformActiveServices` allows an external caller to determine which servers are active on the
  platform and the services that are active within them.
* `OMAGServerPlatformOriginServices` supports the origin services for Egeria's OMAG Server, and is overridden
  in other server platform implementations.
* `OMAGServerPlatformSecurityServices` provides the capability to set up the
  [open metadata security connectors](https://egeria-project.org/concepts/open-metadata-security-connector)
  that validate the authorization of a user to access specific services and metadata in Egeria.

----
Return to [Platform Services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
