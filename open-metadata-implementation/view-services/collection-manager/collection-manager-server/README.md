<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Collection Manager Open Metadata View Service (OMVS) server-side implementation

The Collection Manager OMVS server-side support is organized in the following top level packages 

* admin -  CollectionManagerAdmin controls this OMVS's lifecycle. It is initialised here receiving the view service configuration. It is shutdown here.
* ffdc - defines the exceptions and audit log messages.
* rest - defines the payloads used on the REST API.
* server - implementation of the view service

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.