<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Governance Servers Documentation

A governance server is a specialised [OMAG Server](../../../open-metadata-publication/website/omag-server/omag-server.md).
It is designed to host connectors that manage metadata in different technologies.

Each type of governance server is paired with an Open Metadata Access Service (OMAS) that supports
the same metadata types that the governance server is managing in the third party technology.
The governance servers each have a specific subsystem that contains the services

The table below shows the different types of governance servers and the OMAS they are paired with.
Some of the governance servers are daemons - that means they have no external API and are driven
entirely by changes in metadata and the IT landscape.

| Governance Server | Description | Supported by subsystem | Paired with OMAS | Daemon |
|:----------------- | :---------- | :--------------------- | :--------------- | :-----:|
| Data Engine Proxy Server | Manages capture of metadata from a data engine. | [Data Engine Proxy Services](../data-engine-proxy-services) | [Data Engine OMAS](../../access-services/data-engine) | Yes |
| Data Platform Server | Manages the capture of metadata from a data platform. | [Data Platform Services](../data-platform-services) | [Data Platform OMAS](../../access-services/data-platform) | Yes |
| Discovery Server | Executes [Open Discovery Services](../../frameworks/open-discovery-framework/docs/discovery-service.md) on demand. | [Discovery Engine Services](../discovery-engine-services) | [Discovery Engine OMAS](../../access-services/discovery-engine) | No |
| Open Lineage Server | Provides a historic reporting warehouse for lineage. | [Open Lineage Services](../open-lineage-services) | [Asset Lineage OMAS](../../access-services/asset-lineage) | No |
| Security Officer Server | Provide operational support for the security policies defined by the security officer. | [Security Officer Services](../security-officer-services) | [Security Officer OMAS](../../access-services/security-officer) | No |
| Security Synchronization Server | Keep security enforcement engines supplied with the latest metadata. | [Security Sync Services](../security-sync-services) | [Governance Engine OMAS](../../access-services/governance-engine) | Yes |
| Stewardship Server | Manage the triage and remediation of **Request for Action** events raised through automated governance activity.| [Stewardship EngineServices](../stewardship-engine-services) | [Stewardship Action OMAS](../../access-services/stewardship-action) | No |
| Virtualizer | Automatically configure a data virtualization platform based on available data sources. | [Virtualization Services](../virtualization-services) | [Information View OMAS](../../access-services/information-view) | Yes |

## Further information

Each governance server has its own documentation (follow the links above).  In addition, there is an
overview design document for developing a new governance server.

* [Designing and Implementing an Open Metadata Governance Server](design)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.