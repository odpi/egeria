<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Open Metadata Repository Test Workbench

The open metadata repository test workbench is responsible for testing the compliance of the
Open Metadata Repository Services (OMRS) REST API that is used to support federated queries.

The workbench uses the [OMRS REST Repository Connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector/README.md)
as a client to test the server.  This connector is also used within the enterprise repository
services as part of the federated query support.  Therefore, the calls to the
server from this test suite are representative of the calls from other open metadata
repositories in an open metadata repository cohort.

## Test cases

* **[repository-metadata-collection-id-test-case](repository-metadata-collection-id-test-case.md)**


  