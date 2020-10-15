<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Lineage OMAS Exceptions

The Asset Lineage OMAS makes use of standard checked exceptions

* **InvalidParameterException** - one of the parameters is null or invalid.
* **NewInstanceException** - the access service is not able to register a new instance of itself in the platform's instance map.
* **PropertyServerException** - there is a problem retrieving information from the property server.
* **UserNotAuthorizedException** - the requesting user is not authorized to issue this request.
* **OCFCheckedExceptionBase** - checked exception for reporting errors found when using OCF connectors

Also, the Asset Lineage OMAS provides a checked exception for reporting errors
* [AssetLineageException](asset-lineage-exception.md)  - the Asset Lineage OMAS service is not able to build a specific function
 
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.