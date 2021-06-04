<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuration Documents

A configuration document provides the configuration details for a single
[OMAG Server](omag-server.md).  It defines which subsystems are activated
in the server and which connector implementations it should use.

## Configuration document structure

An OMAG Server's configuration document is structured into elements that each describe the
configuration properties for each of its desired capabilities.

Figure 1 provides more details.

![Figure 1](configuration-document-structure.png#pagewidth)
> **Figure 1:** Structure of the configuration document


The sections are as follows:

* [Default values](#Default-Values) to use when creating other configuration elements.  These values need to be set up first
* [Basic properties](#Basic-Properties-for-any-OMAG-Server) of any OMAG server.
* [Configuration for specific subsystems](#Services-for-specific-types-of-OMAG-Server) that provide the
  key capabilities for different types of OMAG Servers.
* [Audit trail](#Audit-Trail) that documents the changes that have been made to the configuration document.

It is possible to retrieve the configuration document for a server using the following command.
```
GET {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/configuration
```

When the server is running, the following command returns the configuration document that was used to start it
(since it may have changed in the configuration document store since the server was started.)

```
GET {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/instance/configuration
```

### Default Values

At the top of the configuration document are **Local Server URL Root** and **Event Bus Config**.
 * The local server URL root defines the root of the network address for the
[OMAG Server Platform](omag-server-platform.md) where the OMAG Server will run.
 * The event bus configuration provides the configuration of the event bus (Apache Kafka or similar)
 where all of the event topics that the server will use are located.

Both of these elements provide default values for other configuration
elements.

If they are changed, their new values
do not affect existing definitions in the configuration document.

* [Configuring the Local Server URL Root](../user/configuring-local-server-url.md)
* [Configuring the Default Event Bus](../user/configuring-event-bus.md)

### Basic Properties for any OMAG Server

* [Configuring the Basic Properties](../user/configuring-omag-server-basic-properties.md)
* [Configuring the Audit Log](../user/configuring-the-audit-log.md)
* [Configuring the Server Security Connector](../user/configuring-the-server-security-connector.md)


### Services for specific types of OMAG Server

  * [Configuring the local repository](../user/configuring-the-local-repository.md) - Metadata Server only
  * [Configuring registration to a cohort](../user/configuring-registration-to-a-cohort.md) - Metadata Access Point, Metadata Server, Repository Proxy and Conformance Test Server only
  * [Configuring the open metadata archives loaded at server start](../user/configuring-the-startup-archives.md) - Metadata Access Point, Metadata Server and Repository Proxy only
  * [Configuring the repository proxy connectors](../user/configuring-the-repository-proxy-connector.md) - Repository Proxy only
  * [Configuring the Open Metadata Access Services (OMASs)](../user/configuring-the-access-services.md) - Metadata Access Point and Metadata Server only
  * [Configuring the Open Metadata Engine Services (OMES)](../user/configuring-the-engine-services.md) - Engine Host only
  * [Configuring the Open Metadata Integration Services (OMIS)](../user/configuring-the-integration-services.md) - Integration Daemon only
  * [Configuring the Open Metadata View Services (OMVSs)](../user/configuring-the-view-services.md) - View Server only

### Audit Trail

The audit trail allows you to keep track of changes to the configuration document.
This is helpful what any recent changes might have been - particularly if a working server
suddenly stops working - the first question is always, "what has changed recently?".

It also acts as a nice summary of how the server has been configured.

Below is an example of an audit trail:

```
{
 "auditTrail" : [
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for local server's URL root to https://localhost:9444.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for maximum page size to 100.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for local server type name to Open Metadata Server.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for local server's owning organization's name to Coco Pharmaceuticals.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for local server's userId to cocoMDS1npa.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for local server's password to cocoMDS1passw0rd.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke added configuration for an Open Metadata Server Security Connector",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for default event bus.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for the local repository.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for the local repository.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke preserving local metadata collection id bfdfdc61-01bb-4564-9c29-6b81c0fb79f8.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for cohort cocoCohort.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:12 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for access services.",
    "Thu Jan 30 22:37:13 GMT 2020 garygeeke updated configuration for enterprise repository services (used by access services).",
    "Thu Jan 30 22:44:10 GMT 2020 garygeeke deployed configuration for server."
 ]
}
```

## Storage of the configuration document

By default the configuration document are stored as JSON in a file in the default directory
for the [OMAG Server Platform](omag-server-platform.md) that creates them.

These files may contain security certificates and passwords and so should be treated as sensitive.
It is possible to change the storage location of configuration documents - or even the type of store.
See [Configuration document Store Connector](configuration-document-store-connector.md)
for more information.

## Further reading

* [OMAG Server](omag-server.md) - different types of OMAG servers and what they do.
* [Open Connector Framework (OCF)](../../../frameworks/open-connector-framework)
to understand more abut open connectors and connections since many of the sections in the
configuration document take connection objects for connectors.
* [Configuring an OMAG Server](../user/configuring-an-omag-server.md) provides more detail on
the process of creating a configuration document.

----
Return to [Administration Services Concepts](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
