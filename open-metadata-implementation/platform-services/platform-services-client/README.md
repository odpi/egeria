<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project, 2019. -->

# Platform services client

This module supports `PlatformServicesClient`, the client for issuing queries to the
[OMAG Server Platform](https://egeria-project.org/concepts/omag-server-platform) platform-services interface.
It provides calls to:

* retrieve the platform's start time, build properties, public properties and origin;
* set up, retrieve and clear the platform's security connection, along with managing the user accounts and
  security access controls used by the
  [open metadata security connectors](https://egeria-project.org/concepts/open-metadata-security-connector);
* retrieve the connector type for a given connector provider class name; and
* list the access services, engine services, view services, governance services and common services
  registered with the platform.

----
Return to [Platform Services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.