<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.1

Release 1.1 focuses on establishing a secure, multi-tenant platform for
metadata servers and governance servers.

Below are the highlights:

* A [conformance test suite](../open-metadata-conformance-suite)
  that validates implementations of open metadata repository connectors.

* A [persistent metadata repository](../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector) based on JanusGraph.

* A multi-tenant [OMAG Server Platform](../open-metadata-publication/website/omag-server)
  that is able to host one to many metadata servers and/or governance servers.
  This platform supports APIs to administer servers, start and stop servers and query their
  status.  The platform offers a connector to change the store used for configuration
  documents and to control who can issue platform API calls.
  
* There are now [tutorials](../open-metadata-resources/open-metadata-tutorials) and
  [hands-on labs](../open-metadata-resources/open-metadata-labs) demonstrating
  the configuring and start up of servers on the OMAG Server Platform.
  The aim is to help people to get up and running
  with the Egeria technology.  In addition, there are both [docker scripts and
  Kubernetes helm charts](../open-metadata-resources/open-metadata-deployment) to deploy
  the platforms and related technology used in the tutorials, samples and labs.

* The [Open Metadata Repository Services (OMRS)](../open-metadata-implementation/repository-services) shipped in the [first release](release-notes-1-0.md)
  have been enhanced with REST APIs to query the cohorts that a server
  is connected to.  There are also REST APIs to issue federated queries across
  the cohorts that a metadata server is connected to.
  
* The [Open Discovery Framework (ODF)](../open-metadata-implementation/frameworks/open-discovery-framework) is now defined and implemented.
  It complements the [Open Connector Framework (OCF)](../open-metadata-implementation/frameworks/open-connector-framework) delivered in release 1.0.
  This framework provides the APIs that automated 

* The metadata servers and future governance servers running on the OMAG Server Platform
  support [metadata-centric security](../open-metadata-implementation/common-services/metadata-security)
  that is controlled by a connector.
  
* There is a new user interface to explore the open metadata types.  It is called the **Type Explorer**.
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.