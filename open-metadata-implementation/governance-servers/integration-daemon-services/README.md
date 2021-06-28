<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![TechPreview](../../../open-metadata-publication/website/images/egeria-content-status-tech-preview.png#pagewidth)

## Integration Daemon Services

The integration daemon services provide the implementation
of the [Integration Daemon](../../admin-services/docs/concepts/integration-daemon.md)
OMAG Server which is responsible for operating the 
[Open Metadata Integration Services (OMISs)](../../integration-services).
The integration services are responsible for running connectors that exchange metadata with third party
technology.


![Figure 1](docs/integration-daemon-in-action.png)
> **Figure 1:** Integration daemon exchanging metadata with many types of third party technologies


Each type of integration service supports a particular type of connector interface that is designed to exchange
metadata with a specific [Open Metadata Access Services (OMAS)](../../access-services).
For example, the [Database Integrator OMIS](../../integration-services/database-integrator) calls the
[Data Manager OMAS](../../access-services/data-manager).  

![Figure 2](docs/omis-omas-pair.png)
> **Figure 2:** The pairing of the integration services with the access services

Inside the Integration Daemon, the integration services
host the [integration connectors](docs/integration-connector.md) that manage the
exchange of metadata with third party technology. 

![Figure 3](docs/inside-integration-daemon.png)
> **Figure 3:** Inside the integration daemon showing the context object that provides the specialist API to the integration connectors


## Further information

* [Trouble shooting issues](../../../open-metadata-publication/website/diagnostic-guide/integration-daemon-diagnostic-guide.md) with the integration daemon
* [Configuring](../../admin-services/docs/concepts/integration-daemon.md) the integration daemon
* Learning more about [integration connectors](docs/integration-connector.md)
* [Solutions](../../../open-metadata-publication/website/solutions/data-manager-integration) using the integration daemon

----
* Return to the [Governance Servers](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.