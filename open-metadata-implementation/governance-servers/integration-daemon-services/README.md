<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

## Integration Daemon Services

The integration daemon services provide the implementation
of the [Integration Daemon](../../admin-services/docs/concepts/integration-daemon.md)
OMAG Server which is responsible for operating the 
[Open Metadata Integration Services (OMISs)](../../integration-services).
The integration services are responsible for running connectors that exchange metadata with third party
technology.

Each type of integration service supports particular types of connectors that are designed to exchange
metadata with a specific [Open Metadata Access Services (OMAS)](../../access-services).
For example, the Database Integrator OMIS calls the Data Manager OMAS.

![Figure 1](docs/omis-omas-pair.png)

Inside the Integration Daemon, the integration services
host the [integration connectors](docs/integration-connector.md) that manage the
exchange of metadata with third party technology. 

![Figure 2](docs/inside-integration-daemon.png)


----
* Return to the [Governance Servers](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.