<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Catalog Open Metadata Access Service (OMAS) server-side implementation

The Asset Catalog OMAS server supports the server side implementation of the access service. This includes:

* the interaction with the [administration services](../../../admin-services) for
    registration, configuration, initialization and termination of the access service.
* the interaction with the [repository services](../../../repository-services) to work with open metadata from the
    [cohort](../../../repository-services/docs/open-metadata-repository-cohort.md).
* the OMRS Topic listener designed for receiving events from the cohorts that the local server is connected to.
* the audit log used to define the message content for the OMRS Audit Log.
* the service package that provides the server-side implementation of the Asset Catalog Open Metadata Assess Service (OMAS).
These services provide the functionality to fetch asset's header, classification, properties and relationships.
  
  
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.