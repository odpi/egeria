<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
 
![InDev](../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Open Metadata User Interface

Egeria provides a basic user interface (UI) to demonstrate the power of the open
metadata and governance capabilities. They are fully functional as
far as the standards go, and could be used by a small company. However,
it is likely that commercial offerings will offer a richer user experience,
particularly for a larger organization.

There are 2 User Interfaces :

* The **[Presentation Server](../admin-services/docs/concepts/presentation-server.md)** is a multi-tenant server that serves a user interface - it issues rest calls downstream primarily to view
  servers. All the code for the Presentation Server has been moved to [Egeria React UI Git repository](https://github.com/odpi/egeria-react-ui)

*  [UI Application](ui-chassis) the user interface platform. The Server side of the user interface is maintained in the [ui-chassis](ui-chassis) module.
The client side has been moved to [Egeria UI Git repository](https://github.com/odpi/egeria-ui)


----
Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.