<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
 
# Open Metadata User Interface

Egeria provides a basic user interface (UI() to demonstrate the power of the open
metadata and governance capabilities.  They are fully functional as
far as the standards go, and could be used by a small company.  However,
it is likely that commercial offerings will offer a richer user experience,
particularly for a larger organization.

The user interface supports user-facing view services. A view service is a personna orientated
experience of open metadata.

Quick start on the UI: 
* [Review applications.properties](ui-chassis/ui-chassis-spring/README.md)
* Start and configure the OMAGPlatform if not already started. 
* Start the EgeriaUIIPlatform.
* [Configure the UI server](ui-admin-services/README.md)


Modules:
* [ui-chassis](ui-chassis) the user interface platform.
* [ui-admin-services](ui-admin-services) supports UI administration, used to configure and operate the UI server.
* [ui-common](ui-common) supports common capabilities  
* [ui-security](ui-security) supports security including authentication and mapping allowed urls
* [view-services](view-services) supports the view services
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.