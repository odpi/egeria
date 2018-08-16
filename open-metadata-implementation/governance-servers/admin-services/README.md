<!-- SPDX-License-Identifier: Apache-2.0 -->

# Administration Services

The administration services support the configuration of the open metadata server functions.
This configuration determines which of the open metadata services are active.  It also supports
querying the runtime (operational) state of the open metadata components.

There are four modules for the administration services:

* **[admin-services-api](admin-services-api)** - contains the Java interfaces for the administration services.
* **[admin-services-client](admin-services-client)** - contains a Java client implementation for calling
the administration services.
* **[admin-services-server](admin-services-server)** - contains the server side implementation of the
administration services.
* **[admin-services-spring](admin-services-spring)** - uses Spring to implement a RESTful service
for the open metadata server.

The operation of the administration services is described [here](Using-the-Admin-Services.md).