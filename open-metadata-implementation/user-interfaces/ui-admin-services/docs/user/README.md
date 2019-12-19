<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Server Platform Administration Services User Guide

An **[Open Metadata and Governance (OMAG) Server Platform](../../../../open-metadata-publication/website/omag-server)**
hosts one or more logical **[OMAG servers](../concepts/ui-server.md)**, each supporting a variety of open metadata
and governance capabilities.

The capabilities that are enabled in a specific instance of a logical UI Server
are defined in a JSON **[configuration document](../concepts/configuration-document.md)**.
When the configuration document is loaded to the UI server platform, the logical UI server
is started, and the capabilities defined in the configuration document are activated.

In an open metadata landscape, it is anticipated that there may be multiple
instances of the logical UI Server running, each performing a different role.
The capabilities of each of these instances would be defined in a different configuration document.
They could all, however, be loaded into the same UI server platform, or distributed across
different UI server platforms.

The configuration document for a specific logical UI server is identified by the server's name.
This is passed on the URL of every admin services API request along with the user
id of the administrator.  By default, the configuration is stored in a file called:

```
ui.server.{serverName}.config
```

The administration services that set up this file all begin with a URL like this:

```
.../open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/...
```

The **serverName** specified on these calls determines which configuration
document is used, and hence which of the logical UI server's configuration it is working with.

The UI server platform starts up without any open metadata capabilities enabled.
Once it is running, it can be used to set up the configuration documents
that describe the open metadata capabilities needed for each server instance.

Once the configuration document is in place, the open metadata services
can be activated and deactivated multiple times, across multiple
restarts of the server platform.

## Building a configuration document for a server

The configuration document for the logical UI Server determines the types of open
metadata and governance services that should be activated in the logical UI server.
For example:

* Basic descriptive properties of the server that are used in logging and events
originating from the server.
* What type of local repository to use.
* Whether the Open Metadata Access Services (OMASs) should be started.
* Which cohorts to connect to.

Each of the configuration commands builds up sections in the configuration document.
This document is stored in the configuration file after each configuration request so
it is immediately available for use each time the open metadata services are activated
in the UI Server.

In the descriptions of the configuration commands the following values are used as examples:

* The UI server platform is running on the localhost, at port 8443 (ie **https://localhost:8443**).
* The user id of the administrator is **garygeeke**.
* The name of the logical UI server (serverName) is **cocoUIS1**.

### Common Configuration Tasks

* [Setting basic properties for a logical UI server](configuring-ui-server-basic-properties.md)
* Setting up default configuration parameters
   * [Setting up the default event bus](configuring-event-bus.md)
   * [Configuring the default local server URL root](configuring-local-server-url.md)
* [Configuring the Open Metadata Repository Services (OMRS)](configuring-the-repository-services.md)
* [Configuring the Open Metadata View Services (OMVSs)](configuring-the-view-services.md)


### Advanced Configuration Topics

* [Migrating configuration documents](migrating-configuration-documents.md)
* [Configuring the storage mechanism to use for configuration documents](configuring-configuration-file-store.md)


## Querying the contents of a configuration document

It is possible to query the configuration document for a specific UI server using the following command.

```
GET https://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/configuration
```

It is also possible to query the origin of the server supporting the open metadata services.
For the Egeria UI Server Platform, the response is "ODPI Egeria UI Server Platform (version 1.3-SNAPSHOT)".

```
GET https://localhost:8443/open-metadata/platform-services/users/garygeeke/servers/cocoMDS1/server-platform-origin
```

## Examples of configuration calls

The postman collection illustrates many of the configuration calls: 
[admin-services-configuration.postman_collection.json](../../admin-services-configuration.postman_collection.json)




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
