<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Creating configuration documents for the OMAG Server

The OMAG Server provides a platform (called the server chassis)
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

The OMAG Server can have services defined by multiple configuration documents
active at any one time.
However the services for each configuration document run in their own container and 
can not interact within the OMAG Server.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.