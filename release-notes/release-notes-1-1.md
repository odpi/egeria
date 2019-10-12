<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.1

Release 1.1 represents a major update to the ODPi Egeria function.

Below are the highlights:

* A [conformance test suite](../open-metadata-conformance-suite)
  that validates implementations of open metadata repository connectors.

* A [persistent metadata repository](../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector) based on JanusGraph.

* A multi-tenant [OMAG Server Platform](../open-metadata-publication/website/omag-server)
  that is able to host one to many metadata servers and/or governance servers.
  This platform supports APIs to administer servers, start and stop servers and query their
  status.  The platform offers a connector to change the store used for configuration
  documents and to control who can issue platform API calls.
  
* There are now [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs) and
  [samples](../open-metadata-resources/open-metadata-samples) demonstrating
  the capabilities of Egeria.  The aim is to help people to get up and running
  with the Egeria technology.  In addition, there are both [docker scripts and
  Kubernetes helm charts](../open-metadata-resources/open-metadata-deployment) to deploy
  the platforms and related technology used in the tutorials, samples and labs.

* The [Open Metadata Repository Services (OMRS)](../open-metadata-implementation/repository-services) shipped in the [first release](release-notes-1-0.md)
  have been enhanced with REST APIs to query the cohorts that a server
  is connected to.  There are also REST APIs to issue federated queries across
  the cohorts that a metadata server is connected to.
  
* The [Open Discovery Framework (ODF)](../open-metadata-implementation/frameworks/open-discovery-framework) is now defined and implemented.
  It complements the [Open Connector Framework (OCF)](../open-metadata-implementation/frameworks/open-connector-framework) delivered in release 1.0.
  
* There are new access services:
   * The [Asset Catalog OMAS](../open-metadata-implementation/access-services/asset-catalog) provides a catalog search API for Assets
   * The [Asset Consumer OMAS](../open-metadata-implementation/access-services/asset-consumer) supports the access to both the data and metadata associated with an asset.
   * The [Asset Owner OMAS](../open-metadata-implementation/access-services/asset-owner) supports the manual cataloging of new Assets.
   * The [Data Engine OMAS](../open-metadata-implementation/access-services/data-engine) supports the processing of notifications from data engines such as ETL platforms in order to catalog information about the data movement, transformation and copying they are engaged in.
   * The [Data Platform OMAS](../open-metadata-implementation/access-services/data-platform) supports the processing of notifications from data platforms such as Cassandra in order to automatically catalog the Asset stored on the data platform.
   * The [Discovery Engine OMAS](../open-metadata-implementation/access-services/discovery-engine) supports the discovery server (below).
   * The [Glossary View OMAS](../open-metadata-implementation/access-services/glossary-view) provides the ability to browse glossaries and their content.
   * The [Governance Engine OMAS](../open-metadata-implementation/access-services/governance-engine) supports the security sync server (below).
   * The [Information View OMAS](../open-metadata-implementation/access-services/information-view) supports the virtualizer server (below).
   * The [Security Officer OMAS](../open-metadata-implementation/access-services/security-officer) supports the assigning of security tags to assets and their schemas.
   * The [Subject Area OMAS](../open-metadata-implementation/access-services/subject-area) supports the creation of glossaries, categories and terms.
   
* There are new governance servers:
   * The [Data Engine Proxy Server](../open-metadata-implementation/governance-servers/data-engine-proxy-services) supports the processing of notifications from data engines such as ETL platforms
     in order to catalog information about the data movement, transformation and copying they are engaged in.
     It calls the Data Engine OMAS.
   * The [Data Platform Server](../open-metadata-implementation/governance-servers/data-platform-services) supports the processing of notifications from data platforms such as Cassandra in order
     to automatically catalog the Assets stored on the data platform.  It calls the Data Platform OMAS.
   * The [Discovery Server](../open-metadata-implementation/governance-servers/discovery-engine-services) provides support for automated metadata discovery services that implement the ODF interfaces.
     It calls the Discovery Engine OMAS.
   * The [Security Sync Server](../open-metadata-implementation/governance-servers/security-sync-services) exchanges security configuration with enforcement engines such as Apache Ranger.
     It calls the Discovery Engine OMAS.
   * The [Virtualizer Server](../open-metadata-implementation/governance-servers/virtualization-services) supports the automatic configuration of a data virtualization platform based on the
     Assets that are being cataloged in open metadata.  It consumes notifications
     from the Information View OMAS and stores details of the views it is creating
     through the Data Platform OMAS.   

* The metadata server and governance servers support [metadata-centric security](../open-metadata-implementation/common-services/metadata-security)
  that is controlled by a connector.
  
* There is a new user interface to explore the open metadata types.  It is called the Type Explorer.
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.