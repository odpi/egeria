<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Glossary Author Open Metadata View Service (OMVS) server-side implementation

The Glossary Author OMVS server-side support is organized in the following top level packages 

* admin -  The SubjectAreaAdmin controls this OMVS's lifecycle. It is initialised here receiving the view service configuration. It is shutdown here.
* auditlog - this is a list of the audit log messages
* initialization - registration and set up for multitenancy
* services - implementations of the view service
* handlers
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.