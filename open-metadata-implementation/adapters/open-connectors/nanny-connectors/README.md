<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# The Nanny Connectors

The Nanny Connectors provide support for the observation, analysis and improvement of an existing metadata 
catalog deployment.  Initial tests have focused on Apache Atlas and make use of the 
[Apache Atlas Integration Connector](../integration-connectors/atlas-integration-connector) to exchange metadata
between the open metadata ecosystem and Apache Atlas.

* The **Discover Apache Atlas Connector** is an open discovery service that gathers statistics about the
  Apache Atlas server and stores it in a Discovery Analysis Report attached the the SoftwareServer entity
  that represents the Apache Atlas server.

* The **Load Observations By Egeria Connector** extracts relevant metadata published to the open metadata
ecosystem by the Apache Atlas Integration Connector and the Discover Apache Atlas Connector and writes it
to a database formatted to support a set of SuperSet reports.  It is an integration
connector running under the Catalog Integrator OMIS.  


The LoadObservationsByEgeriaConnector must be configured with metadataSourceQualifiedNAme set to null. 



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.