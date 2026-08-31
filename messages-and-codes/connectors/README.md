<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Connector Messages

Connectors are the pluggable components of Egeria.  They run inside the OMAG Server Platform and call out to the technologies that Egeria is integrating with, so their messages are the ones most often seen when a third party technology misbehaves.

Return to the [messages and codes index](../README.md).

| Area | Message sets | Messages | Description |
|---|---|---|---|
| [Data Manager Connectors](data-manager-connectors) | 12 | 72 | These connectors catalog and survey the contents of database servers and other data managers. |
| [Data Store Connectors](data-store-connectors) | 5 | 27 | These connectors provide access to the contents of files, folders and databases. |
| [Integration Connectors](integration-connectors) | 12 | 66 | Integration connectors run in an integration daemon.  They keep the open metadata ecosystem synchronized with the third party technologies that they monitor. |
| [System Connectors](system-connectors) | 13 | 73 | These connectors call the APIs of third party systems such as Apache Atlas, Apache Kafka and the Egeria runtime itself. |
| [Repository Services Connectors](repository-services-connectors) | 8 | 32 | These connectors provide the pluggable implementations used by the repository services - the metadata repositories, the audit log destinations, the cohort registry stores and the open metadata archive stores. |
| [Event Bus Connectors](event-bus-connectors) | 2 | 22 | These connectors send and receive events over the event bus - typically Apache Kafka. |
| [Governance Action Connectors](governance-action-connectors) | 2 | 43 | These governance services run in an engine host to make changes to the open metadata ecosystem and the resources it describes. |
| [File Survey Connectors](file-survey-connectors) | 1 | 5 | These survey action services analyse the content of files and folders and record what they find in a survey report. |
| [Nanny Connectors](nanny-connectors) | 16 | 81 | The nanny connectors harvest observability data from the open metadata ecosystem into a database so that the operation of Egeria itself can be analysed. |
| [Lovelace Insights](lovelace-insights) | 2 | 7 | These connectors analyse the harvested observability data and turn it into insight reports. |
| [Report Generating Connectors](report-generating-connectors) | 1 | 2 | These connectors turn the contents of the open metadata ecosystem into human-readable documents. |
| [Secrets Store Connectors](secrets-store-connectors) | 2 | 5 | These connectors supply the credentials that other connectors need when they call a third party technology. |
| [Metadata Security Connectors](metadata-security-connectors) | 1 | 1 | These connectors implement an organization's authorization rules for the OMAG Server Platform and its servers. |
| [Configuration Store Connectors](configuration-store-connectors) | 1 | 2 | These connectors store and retrieve the configuration documents of the servers running on an OMAG Server Platform. |
| [REST Client Connectors](rest-client-connectors) | 1 | 2 | These connectors issue the REST API calls that Egeria's clients make to a remote OMAG Server Platform. |
| [Other Connectors](other-connectors) | 1 | 2 | The remaining connectors shipped with Egeria. |


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
