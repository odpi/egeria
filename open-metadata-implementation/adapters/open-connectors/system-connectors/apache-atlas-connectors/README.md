<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Apache Atlas Connectors

The Apache Atlas connectors provide a variety of functions to access, survey, catalog and govern Apache Atlas.
The Jar file *apache-atlas-connectors.jar* is included in the OMAG Server Platform.

## Apache Atlas REST Connector

Apache Atlas is a digital resource connector that has a REST API that allows external callers to query and create both
types and instances.  This connector provides a simple Java API to this REST API.
It is written without any dependencies on Apache Atlas (or its associated Hadoop components)
so it happily runs in the same version of Java as the rest of Egeria.

This connector is used by other connectors from Egeria, and may also be used
by components from outside Egeria.

The values from the connection used by this connector are:

* Connection.getUserId() and Connection.getClearPassword() for logging in to Apache Atlas.
* Connection.getDisplayName() for the connector name in messages.
* Connection.getEndpoint().getAddress() for the URL root (typically host and port name) of the Apache Atlas server.
* Connection.getConfigurationProperties.get("atlasServerName") for the name of the Apache Atlas server to use in messages.

This connector is called by the other Atlas Connectors.

## The Apache Atlas Survey Connector

The Apache Atlas Survey Connector reviews the status and content of an Apache Atlas server and documents it in a survey report.

## The Apache Atlas Integration Connector

The Apache Atlas integration connector publishes glossary terms, assets and lineage to Apache Atlas.

### Deployment and configuration

The Apache Atlas integration connector is included in the main Egeria assembly: omag-server-platform.
It runs in the [integration daemon](https://egeria-project.org/concepts/integration-daemon/).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.