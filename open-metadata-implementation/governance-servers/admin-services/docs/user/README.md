<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Server Platform Administration Services User Guide

An **[Open Metadata and Governance (OMAG) Server Platform](../../../../../open-metadata-publication/website/omag-server)**
hosts one or more logical **[OMAG servers](../concepts/logical-omag-server.md)**, each supporting a variety of open metadata
and governance capabilities.

The capabilities that are enabled in a specific instance of a logical OMAG Server
are defined in a JSON **[configuration document](../concepts/configuration-document.md)**.
When the configuration document is loaded to the OMAG server platform, the logical OMAG server
is started, and the capabilities defined in the configuration document are activated.

In an open metadata landscape, it is anticipated that there may be multiple
instances of the logical OMAG Server running, each performing a different role.
The capabilities of each of these instances would be defined in a different configuration document.
They could all, however, be loaded into the same OMAG server platform, or distributed across
different OMAG server platforms.

The configuration document for a specific logical OMAG server is identified by the server's name.
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
document is used, and hence which of the logical OMAG server's configuration it is working with.

The OMAG server platform starts up without any open metadata capabilities enabled.
Once it is running, it can be used to set up the configuration documents
that describe the open metadata capabilities needed for each server instance.

Once the configuration document is in place, the open metadata services
can be activated and deactivated multiple times, across multiple
restarts of the server platform.

## Building a configuration document for a server

The configuration document for the logical OMAG Server determines the types of open
metadata and governance services that should be activated in the logical OMAG server.
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
* The name of the logical OMAG server (serverName) is **cocoMDS1**.

### Common Configuration Tasks

* [Setting basic properties for a logical OMAG server](configuring-omag-server-basic-properties.md)
* Setting up default configuration parameters
   * [Setting up the default event bus](configuring-event-bus.md)
   * [Configuring the default local server URL root](configuring-local-server-url.md)
* [Configuring the Open Metadata Repository Services (OMRS)](configuring-the-repository-services.md)
* [Configuring the Open Metadata Access Services (OMASs)](configuring-the-access-services.md)
* [Configuring the Discovery Engine Services](configuring-the-discovery-engine-services.md)
* [Configuring the Security Sync Services](configuring-the-security-sync-services.md)
* [Configuring the Stewardship Services](configuring-the-stewardship-services.md)

### Advanced Configuration Topics

* [Configuring the storage mechanism to use for configuration documents](configuring-configuration-file-store.md)


## Querying the contents of a configuration document

It is possible to query the configuration document for a specific OMAG server using the following command.

```

GET http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/configuration

```

It is also possible to query the origin of the server supporting the open metadata services.  For the Egeria OMAG Server Platform, the response is "ODPI Egeria OMAG Server Platform".

```

GET http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/server-origin

```

## Examples of configuration calls

The postman collection illustrates many of the configuration calls: 
[admin-services-configuration.postman_collection.json](../../admin-services-configuration.postman_collection.json)




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.