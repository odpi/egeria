<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Lineage Services Exceptions

The Open Lineage Services make use of standard checked exceptions

* **InvalidParameterException** - one of the parameters is null or invalid.
* **PropertyServerException** - there is a problem retrieving information from the property server.
* **UserNotAuthorizedException** - the requesting user is not authorized to issue this request.
* **OCFCheckedExceptionBase** - checked exception for reporting errors found when using OCF connectors

Also, the OLS provides a checked exception for reporting errors
* [OpenLineageException](lineage-warehouse-exception.md)  - the OLS service is not able to build a specific function
 
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.