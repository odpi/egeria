<!-- SPDX-License-Identifier: Apache-2.0 -->
This script can be used to configure Egeria for use with an in-memory repository

Prerequisites:

kafka running with the queue specified below created. 
 
Variables:
 baseURL - The egeria URL
 user    - the userName to pass to Egeria
 server  - the server name
 cohort  - the name of the cohort. Used as the kafka queue name for OMRS
 kafkaep - kafka endpoint

An example environment can be found at ../environment/Egeria localhost.postman_environment.json
