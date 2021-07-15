<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Egeria's Connector Catalog

Egeria has a growing collection of [connectors](../developer-guide/what-is-a-connector.md) to third party technologies.
These connectors help to accelerate the rollout of your open metadata
ecosystem since they can be used to automate the extraction and
distribution of metadata to the third party technologies.

A connector is a client to a third party technology.  It supports a standard API
that Egeria calls and it then translates these calls into
requests to the third party technology.  Some connectors are also able to listen
for notifications from the third party technology.  When a notification
is received, the connector converts its content into a call to Egeria to
distribute the information to the open metadata ecosystem.

Connectors enable Egeria to operate in many environments
and with many types of third party technologies, just by managing the configuration of the OMAG servers.
The Connector Catalog list the connector implementations supplied by the Egeria community.
There are three broad categories of connectors and the connector catalog is organized accordingly:

* Connectors that support the [exchange and maintenance of metadata](exchange-connectors.md).  This includes the
integration connectors, repository connectors, discovery services and governance action services.

* Connectors that support [Egeria's runtimes](runtime-connectors.md).   This includes the event bus connectors,
cohort registry stores, configuration stores, audit log destination connectors, open metadata archive stores,
REST client connectors and the cohort member remote repository connectors.

* Connectors that provide [access to digital resources and their metadata](data-connectors.md) that is
stored in the open metadata ecosystem.




## Further Information


* Learn how to [write your own connector](../developer-guide/what-is-a-connector.md)
* Learn how to [configure connectors in an Egeria runtime](../../../open-metadata-implementation/admin-services/docs/user)



----

* Return to [Home Page](../../../index.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.