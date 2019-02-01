<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Creating configuration documents for the OMAG Server Platform

The OMAG server platform provides a software platform
for running open metadata and governance services.
When [the OMAG Server is first started](task-starting-the-omag-server.md),
it has two sets of APIs active.

* Operations for creating configuration documents.
* Operations for starting and stopping open metadata and governance services in
the OMAG server using the configuration documents.

## What is a configuration document?

A configuration document provides the configuration properties for one or
more open metadata and governance services.  The contents of
a single configuration document describes a logical server.

The OMAG server platform can host multiple logical servers at a time.
Each logical server is isolated within the server platform and so
the OMAG server platform can be used to support
[multi-tenant operation](https://en.wikipedia.org/wiki/Multitenancy).

## Calling the Administration Services

Configuration documents are created using the OMAG Server Platform
Administration Services.  In order to experiment with these services,
this tutorial uses the [Postman](task-working-with-postman.md) test tool.
This is a tool that enables you to type in REST API calls and execute them
against the OMAG server platform.

There is also a [postman collection](./omag-server-platform-tutorial.postman_collection.json)
that can be downloaded and imported into postman
(see `Import` button top left of the Postman user interface).

Once you have Postman running and have loaded the collection
you are ready to build configuration documents.








----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.