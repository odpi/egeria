<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

#Server author Open Metadata View Service (OMVS) server-side implementation

The Server Author OMVS server-side support is organized in the following top level packages

* admin - ServerAuthorAdmin controls this OMVS's lifecycle. It is initialised here receiving the view service configuration. It is shutdown here.
* handlers - instance handler
* server - implementation of the view service
* initialization - classes involved with registering nd setting up the instance of the view server

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.