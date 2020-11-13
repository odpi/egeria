<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Subject Area Open Metadata Access Service (OMAS) server-side implementation

The Subject Area OMAS server-side support is organized in the following top level packages 

* admin -  The SubjectAreaAdmin controls this OMAS's lifecycle. It is initialised here receiving the access service configuration. It is shutdown here.
* auditlog - this is a list of the audit log messages
* initialization 
* internal response
* listener
* outtopic 
* publisher - messaging publishing
* server - server services
* utilities - utilities - including helper methods
* validators - methods to do validation

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.