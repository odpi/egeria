<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
 
![In Development](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Presentation Server

The **Presentation Server** is a multi-tenant server that serves a user interface - it issues rest calls downstream primarily to view
servers.    

The has been added to Git to facilitate code sharing and development and the ability to raise issues; it is not yet in the Maven build.
As is becomes more mature, it will be integrated into the build and fully documented.

See the [Egeria Planning guide](https://github.com/odpi/egeria/tree/master/open-metadata-publication/website/planning-guide) to familiarise yourself with 
the types of Egeria OMAG Servers; Presentation Server is one of.  


## For development
 * Add the presentation-server folder into [Visual Studio Code](https://code.visualstudio.com/). It will create a new workspace.
 * You can open a terminal in Visual Studio code to issue the npm commands
 * Set up your environment by running `npm install`
 * Build and start the server using `npm start`. Use other npm parameters as specified in [package.json](nodejs/package.json). 
## Setting up the downstream servers  
 * You need to have configured an Access Point Server and View server. The Presentation server issue omvs calls to the View server
 which issues Open Metadata Access Service (OMAS) calls to the Access Point Server.        
 * At present you need to configure a view server with server name 'viewserver' and root url 'http://localhost:8085'.
## Access UI using the browser. 
 * Enter 'https://localhost:8091/<tenant-name>/' on the UI to access the tenanted UI (<tenant-name> is the serverName used by the presentation server). The Ui will prompt for a login; some of the 
 Coco Pharmaceutical personnas have been enabled here - use user 'faith' and password 'admin'  
   

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.