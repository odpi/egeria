<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
 
![Released](../../images/egeria-content-status-released.png#pagewidth)

# Administration Services

The administration services support the configuration of the open metadata and governance services
within the [OMAG Server Platform](https://egeria-project.org/concepts/omag-server-platform)
This configuration determines which of the open metadata and governance services are active.

It also supports the starting and stopping of logical [OMAG Servers](https://egeria-project.org/concepts/omag-server)
on the OMAG server platform and querying the runtime (operational) state of these servers.

* [Documentation](https://egeria-project.org/services/admin-services/overview)

# Administration Services Internal Design Documentation

There are five modules for the administration services:

* **[admin-services-api](admin-services-api)** - contains the Java interfaces for the administration services.
* **[admin-services-client](admin-services-client)** - contains a Java client implementation for calling
  the administration services.
* **[admin-services-registration](admin-services-registration)** - enables new access services to register with the admin services.
* **[admin-services-server](admin-services-server)** - contains the server side implementation of the
  administration services.
* **[admin-services-spring](admin-services-spring)** - uses Spring to implement a RESTful service
  for the open metadata server.


## Further reading

* [User Guide for the Administration Services](https://egeria-project.org/guides/admin/)

Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.