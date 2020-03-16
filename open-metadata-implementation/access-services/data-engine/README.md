<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Data Engine Open Metadata Access Service (OMAS)

The Data Engine OMAS provides APIs and events for data movement/processing engines to record the changes made to the data landscape. 

It provides the ability to register the data engine itself along with the lineage details of the ETL transformations. 
Data Engine OMAS APIs offer support for creating the corresponding open metadata types for assets and jobs.
  

The module structure for the Data Engine OMAS is as follows:

* [data-engine-api](data-engine-api) supports the common Java classes that are used both by the client and the server. 
This includes the Java API, beans and REST API structures.
* [data-engine-client](data-engine-client) supports the Java client library that allows applications and tools to call the remote REST APIs.
* [data-engine-server](data-engine-server) supports the server side implementation of the access service.
 This includes the
  * interaction with the [administration services](../../admin-services) for
    registration, configuration, initialization and termination of the access service.
  * interaction with the [repository services](../../repository-services) to work with open metadata from the
    [cohort](../../repository-services/docs/open-metadata-repository-cohort.md).
  * support for the access service's API and its related event management.
* [data-engine-spring](data-engine-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


## Digging Deeper

* [User Documentation](docs/user)
* [Design Documentation](docs/design)


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
