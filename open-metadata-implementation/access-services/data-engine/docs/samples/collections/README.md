<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Samples

This sample Postman collection illustrates configuring and using the DataEngine OMAS with the Egeria graph repository.

### [DataEngine-technical-assets.postman_collection.json](DataEngine-technical-assets.postman_collection.json) 
 
### [DataEngineOMAS-local-graph-integration.postman_collection.json](DataEngineOMAS-local-graph-integration.postman_collection.json)

This script can be used to configure Egeria with Data Engine OMAS and the local graph repository.  
It can be used to run through a number of different tests of the REST endpoints that Data Engine OMAS exposes. 

Prerequisites:

- [local-integration-tests.postman_environment.json](local-integration-tests.postman_environment.json) - the environment used for running the tests locally

- Egeria by default uses https:// requests with a self-signed certificate. Any PostMan users therefore will need to
 go into settings->general and turn off 'SSL certificate verification' or requests will fail.
 
Each step is sequentially numbered so that they can be executed in-order as part of a Postman "Runner", if desired.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
