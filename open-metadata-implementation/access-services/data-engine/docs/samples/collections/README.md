<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Samples

### [DataEngine-asset-endpoints.postman_collection.json](DataEngine-asset_endpoints.postman_collection.json)
This sample Postman collection illustrates DataEngine OMAS endpoints for creating/updating/deleting assets

### [DataEngine-process-endpoints.postman_collection.json](DataEngine-process_endpoints.postman_collection.json)
This sample Postman collection illustrates DataEngine OMAS endpoints for creating/updating/deleting processes

### [DataEngineOMAS-local-graph-integration.postman_collection.json](Data Engine-minimal_flows_granular_level.postman_collection)
This sample Postman collection illustrates DataEngine OMAS endpoints used for creating column level lineage. It showcases the minimal flows example, composed of three lineage flows tht are connected.

Prerequisites:

- [local-integration-tests.postman_environment.json](local-integration-tests.postman_environment.json) - the environment used for running the tests locally

- Egeria by default uses https:// requests with a self-signed certificate. Any PostMan users therefore will need to
 go into settings->general and turn off 'SSL certificate verification' or requests will fail.
 
Each step is sequentially numbered so that they can be executed in-order as part of a Postman "Runner", if desired.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
