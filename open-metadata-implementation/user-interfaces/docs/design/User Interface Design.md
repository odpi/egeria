<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# User Interface Design

The User interface is designed to run like the OMAG Server. There is a Platform, the UI PLatform in this case,
that run server [UI Server](../../ui-admin-services/docs/concepts/ui-server.md), which have services 
[view services](../../view-services).

The User Interface is designed to have separate modules for technology specific implementation and for 
technology agnostic code. This is so that is is always possible to replace a technology specific implementation with
an alternative. Currently the User Interface uses the Spring framework in places, those modules are named
with a suffix of '-spring'. 

All access to pluggable capabilities is done using the connector framework. 

Environment variables can be specified in the application.properties 

The [ui-chassis-spring](../../ui-chassis/ui-chassis-spring) contains the EgeriaUIPlatform which needs to be started in order to use the UI.
It also contains all the web resources including javascript.   
  

## Maven considerations
The structure of the Maven modules is [UI Maven module](UI-Maven-structure.png).

The spring component scan requires the ui-chassis-spring to depend on all the spring modules it needs to bring in;
the dependency checker in the build has been changed to not flag this as an error (normally a dependency not explicitly 
used by the code is flagged as an error - as it is redundant). 

## Login considerations

We need the login to occur on a per server view (tenant) basis. So the login screen needs to be associated with a
tenant, currently this is coded as a query parameter. In the future, the intent is to change the urls to include 
a tenant segment. 

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.