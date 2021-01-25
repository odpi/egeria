<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# View Server

A **View Server** is an [OMAG Server](omag-server.md)
that hosts the REST API services to support a UI.  
These REST API services are called 
[Open Metadata View Services (OMVS)](../../../view-services) 
and they are designed to support specific display paradigms needed
by different types of UIs.

The view services are are typically
called by the [Presentation Server](presentation-server.md), but may also be used by third parties.
The presentation server hosts the JavaScript that controls the browser display.


![Figure 1](view-server.png)
> **Figure 1:** A View Server in the open metadata ecosystem

## Configuring the View Server

The view server is an [OMAG Server](omag-server.md) that runs on
the [OMAG Server Platform](omag-server-platform.md).
It is properties are defined in a [Configuration Document](configuration-document.md)
as shown in Figure 2:

![Figure 2](view-server-config.png#pagewidth)
> **Figure 2:** The configuration document contents for a view server

The links below take you to the sections that describe the commands for each part of the configuration document:

* [Setting basic properties for the View Server](../user/configuring-omag-server-basic-properties.md)
* [Configuring the audit log destinations for log records from the View Server](../user/configuring-the-audit-log.md)
* [Configuring the server security connector for the View Server](../user/configuring-the-server-security-connector.md)
* [Configuring the View Services that run in the View Server](../user/configuring-the-view-services.md)

Once it is configured, the view server can be started using the
[Starting an OMAG Server](../user/starting-and-stopping-omag-server.md).

Instructions for setting up the related [presentation server](../../../user-interfaces/presentation-server)
can be found [here](../user/configuring-the-presentation-server.md).

----
* Return to the [OMAG Server](omag-server.md) types.
* Return to the [Admin Guide](../user).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.