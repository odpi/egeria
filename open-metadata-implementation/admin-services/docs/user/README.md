<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Server Configuration User Guide

An **[Open Metadata and Governance (OMAG) Server Platform](../../../../open-metadata-publication/website/omag-server)**
hosts one or more **[OMAG servers](../concepts/omag-server.md)**, each supporting a variety of open metadata
and governance capabilities.  These capabilities are implemented as [OMAG Subsystems](../concepts/omag-subsystem.md).

The subsystems that are enabled in a specific instance of an OMAG Server
are defined in a JSON **[configuration document](../concepts/configuration-document.md)**.
When the configuration document is loaded into the OMAG server platform, the OMAG server that is describes
is started, and the subsystems defined in the configuration document are activated.

In an open metadata landscape, it is anticipated that there may be multiple
instances of the OMAG Server running in an OMAG Server Platform, each performing a different role.
The active subsystems of each of these server instances would be defined in a different
configuration document.
They could all, however, be loaded into the same OMAG Server Platform, or distributed across
different OMAG Server Platforms ([more information](../concepts/omag-server.md)).

The configuration document for a specific OMAG server is identified by the server's name.
This is passed on the URL of every admin services API request along with the user
id of the administrator.  By default, the configuration is stored in a file called:

```
omag.server.{serverName}.config
```

The administration services that set up this file all begin with a URL like this:

```
.../open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/...
```

The **serverName** specified on these calls determines which configuration
document is used, and hence which of the OMAG server's configuration it is working with.

The OMAG Server Platform starts up without any OMAG servers active.
Once it is running, it can be used to set up the configuration documents
that describe the open metadata subsystems needed for each OMAG server instance.

Once the configuration document is in place, the OMAG Server
can be activated and deactivated multiple times, across multiple
restarts of the OMAG Server Platform.

## Building a configuration document for an OMAG server

The configuration document for the OMAG Server determines which OMAG subsystems (and hence the types of open
metadata and governance services) that should be activated in the OMAG Server.
For example:

* Basic descriptive properties of the server that are used in logging and events
originating from the server.
* What type of local repository to use.
* Whether the Open Metadata Access Services (OMASs) should be started.
* Which cohorts to connect to.

Each of the configuration commands builds up sections in the configuration document.
This document is stored in the configuration file after each configuration request so
it is immediately available for use each time the open metadata services are activated
in the OMAG Server.

In the descriptions of the configuration commands the following values are used as examples:

* The OMAG server platform is running on the localhost, at port 8080 (ie **http://localhost:8080**).
* The user id of the administrator is **garygeeke**.
* The name of the OMAG server (serverName) is **cocoMDS1**.

### Common Configuration Tasks

* [Setting basic properties for an OMAG server](configuring-omag-server-basic-properties.md)
* Setting up default configuration parameters
   * [Setting up the default event bus](configuring-event-bus.md)
   * [Configuring the default local server URL root](configuring-local-server-url.md)
* [Configuring the Open Metadata Repository Services (OMRS)](configuring-the-repository-services.md)
* [Configuring the Open Metadata Access Services (OMASs)](configuring-the-access-services.md)
* [Configuring the Discovery Engine Services](configuring-the-discovery-engine-services.md)
* [Configuring the Security Sync Services](configuring-the-security-sync-services.md)
* [Configuring the Stewardship Services](configuring-the-stewardship-services.md)

### Advanced Configuration Topics

* [Migrating configuration documents](migrating-configuration-documents.md)
* [Configuring the storage mechanism to use for configuration documents](configuring-configuration-file-store.md)


## Querying the contents of a configuration document

It is possible to query the configuration document for a specific OMAG server using the following command.

```
GET http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/configuration
```

It is also possible to query the origin of the server supporting the open metadata services.
For the Egeria OMAG Server Platform, the response is "ODPi Egeria OMAG Server Platform (version 1.5-SNAPSHOT)".

```
GET http://localhost:8080/open-metadata/platform-services/users/garygeeke/servers/cocoMDS1/server-platform-origin
```

## Examples of configuration calls

The postman collection illustrates many of the configuration calls: 
[admin-services-configuration.postman_collection.json](../../admin-services-configuration.postman_collection.json)




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
