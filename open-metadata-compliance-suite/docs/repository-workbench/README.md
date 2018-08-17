<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the Egeria project. -->

  
# Open Metadata Repository Test Workbench

The open metadata repository test workbench is responsible for testing the compliance of the
Open Metadata Repository Services (OMRS) REST API that is used to support federated queries.

The workbench uses the [OMRS REST Repository Connector](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector/README.md)
as a client to test the server.  This connector is also used within the enterprise repository
services as part of the federated query support.  Therefore, the calls to the
server from this test suite are representative of the calls from other open metadata
repositories in an open metadata repository cohort.

This workbench works as a pipeline processor, accumulating information from
one test and using it to seed subsequent tests.  A failure early on in the
pipeline will cause later tests to fail.


In addition this workbench dynamically generates tests based on the types returned
by the repository.  So for example,
the **[Repository TypeDef Test Case](repository-typedef-test-case.md)**
runs for each TypeDef returned by the repository.   Again, a failure in the early set up
test cases will prevent the repository workbench from generating the full suite of
test cases for the repository under test.

## Test cases

* **[Metadata Collection Id Test Case](repository-metadata-collection-id-test-case.md)**
* **[Repository Get TypeDef Gallery Test Case](repository-get-typedef-gallery-test-case.md)**
* **[Repository TypeDef Test Case](repository-typedef-test-case.md)**
* **[Repository Attribute TypeDef Test Case](repository-attribute-typedef-test-case.md)**


  