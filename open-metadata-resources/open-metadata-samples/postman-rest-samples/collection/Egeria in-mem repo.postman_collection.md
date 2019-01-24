<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Egeria in-mem repo.postman_collection.json

This script can be used to configure Egeria for use with an in-memory repository

Prerequisites:

- kafka running with the queue specified below created.

Variables:

- `baseURL` the egeria URL
- `user` the userName to pass to Egeria
- `server` the server name
- `cohort` the name of the cohort. Used as the kafka queue name for OMRS
- `kafkaep` kafka endpoint

An example environment can be found at [../environment/Egeria localhost.postman_environment.json](../environment/Egeria%20localhost.postman_environment.json).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
