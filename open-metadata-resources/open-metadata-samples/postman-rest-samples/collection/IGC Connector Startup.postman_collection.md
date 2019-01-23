<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# IGC Connector Startup.postman_collection.json

This script can be used to configure Egeria for use with an existing IBM Information Governance Catalog ("IGC") environment.

Prerequisites:

- an existing IBM IGC environment, running v11.5.0.1 or later
- kafka running with the queue specified below created. 

Variables:

- `baseURL` the egeria URL
- `user` the userName to pass to Egeria
- `server` the server name for Egeria
- `cohort` the name of the cohort: used as the kafka queue name for OMRS
- `kafkaep` kafka endpoint for the cohort
- `igc_host` the hostname (or IP address) of the existing IGC environment (domain / servics tier)
- `igc_port` the port number of the domain tier console of the existing IGC environment
- `igc_basic_auth` the basic-encoded authorisation details for the IGC environment (base64-encoded `username:password`)

An example environment can be found at [../environment/Egeria IGC Connector.postman_environment.json](../environment/Egeria%20IGC%20Connector.postman_environment.json).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
