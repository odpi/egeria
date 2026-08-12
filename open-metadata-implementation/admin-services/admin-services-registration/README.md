<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Admin Services Registration

The admin services registration module provides the base classes that let new access services and view
services register themselves with the admin services and receive their configuration at server start up:

* `AccessServiceAdmin` and `OMAGAccessServiceRegistration` are implemented and used, respectively, by an
  access service to register itself with the OMAG Server and receive its configuration.
* `ViewServiceAdmin` and `OMAGViewServiceRegistration` provide the equivalent support for a view service.
* `ViewServerGenericServiceAdmin` is implemented by the
  [view server generic services](../../view-server-generic-services) to receive their configuration.

----
Return to [Admin Services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.