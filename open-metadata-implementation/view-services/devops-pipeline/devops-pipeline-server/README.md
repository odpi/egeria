<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Devops Pipeline Open Metadata View Service (OMVS) server-side implementation

The Devops Pipeline OMVS server-side support is organized in the following top level packages 

* admin -  DevopsPipelineAdmin controls this OMVS's lifecycle. It is initialised here receiving the view service configuration. It is shutdown here.
* ffdc - defines the exceptions and audit log messages.
* server - implementation of the view service

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.