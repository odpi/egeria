<!-- SPDX-License-Identifier: Apache-2.0 -->

# Administration Services

The administration services support the configuration of the open metadata server chassis.
This configuration determines which of the open metadata services are active.  It also supports
the querying the runtime (operational) state of the open metadata components.

There are four modules for the administration services:

* **admin-services-api** - contains the Java interfaces for the administration services.
* **admin-services-client** - contains a Java client implementation for calling
the administration services.
* **admin-services-server** - contains the server side implementation of the
administration services.
* **admin-services-spring** - uses Spring to implement a RESTful service
for the administration services server.