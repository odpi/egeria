<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project, 2019. -->

# Repository services clients

This module supports the Java clients that call the Open Metadata Repository Services (OMRS) REST API in a
remote server.

* **MetadataCollectionServicesClient** - represents a remote metadata repository that supports the OMRS
  Repository REST API, translating requests one-for-one to the remote metadata collection.
  * **LocalRepositoryServicesClient** - calls to the local repository in a remote server.  This client
    is used in the OMRS REST Repository Connector to call remote servers during federated queries.
  * **EnterpriseRepositoryServicesClient** - calls to the enterprise repository services in a remote server.
* **MetadataHighwayServicesClient** - calls to the OMRS Metadata Highway REST API, to retrieve information
  about a remote server's cohort membership.
* **AuditLogServicesClient** - calls to retrieve audit logs through the OMRS Repository Services APIs.


----
* Return to [repository-services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.