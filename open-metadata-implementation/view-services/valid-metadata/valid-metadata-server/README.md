<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Valid Metadata Open Metadata View Service (OMVS) server-side implementation

The Valid Metadata OMVS server-side support is organized in the following top level packages 

* admin -  MyProfileAdmin controls this OMVS's lifecycle. It is initialised here receiving the view service configuration. It is shutdown here.
* ffdc - defines the exceptions and audit log messages.
* rest - defines the payloads used on the REST API.
* server - implementation of the view service

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.