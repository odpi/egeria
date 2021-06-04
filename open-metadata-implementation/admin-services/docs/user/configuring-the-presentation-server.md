<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuring the Presentation Server

The [Presentation Server](../concepts/presentation-server.md) is a multi-tenant web application that calls 
view services running in a [View Server](../concepts/view-server.md) to retrieve information and perform operations 
relating to metadata servers.

A presentation server tenant is designed to support an organization.  These may be independent organizations
or divisions/departments within an organization.  The tenant routes requests to the appropriate
view server and then onto the metadata servers behind.  Therefore each tenant sees a different collection of
metadata.

Information for configuring the presentation server is provided here: 
[https://github.com/odpi/egeria-react-ui](https://github.com/odpi/egeria-react-ui).


----
* Learn more about the [Presentation Server](../concepts/presentation-server.md).
* Return to the [Admin Guide](../user).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.