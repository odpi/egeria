<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
 
![In Development](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Presentation Server

The **Presentation Server** is a multi-tenant server that serves a user interface - it issues rest calls downstream primarily to view
servers.    

See the [Egeria Planning guide](https://github.com/odpi/egeria/tree/master/open-metadata-publication/website/planning-guide) to familiarise yourself with 
the types of [Egeria OMAG Servers](../../admin-services/docs/concepts/omag-server.md); Presentation Server is one of those servers.  


## Setting up the downstream servers  
 To run the Presentation server, you need to have correctly [configured](../../admin-services/docs/user/configuring-an-omag-server.md) and be [operating](../../admin-services/docs/user/operating-omag-server.md) the downstream servers.
 For example for the Glossary Author capability, the Presentation server issues omvs calls to the [View server](../../admin-services/docs/concepts/view-server.md)
 which issues [Open Metadata Access Service (OMAS)](../../access-services/README.md) calls to the [Access Point Server](../../admin-services/docs/concepts/metadata-access-point.md).     
 
## Configuring the presentation server 
* The presentation server supports multi-tenants. A presentation server tenant has a serverName and associated configuration.
* The configuration for the presentation server is minimal, only specifying how to access a downstream server.     
* The each tenant's configuration is specified in an environment variable that contains the tenants serverName. 
* The environment variable is called `EGERIA_PRESENTATIONSERVER_SERVER_<ui server name>`  when the <ui server name> is the tenants
 serverName.
* The value of the environment variable is a json payload with 2 named properties
    * `remoteServerName` the remote server name
    * `remoteURL` the remote url. 
* An example of the environment variable you would specify for a UI tenant with server name `aaa` with a remote server name
`  cocoMDSV1` and remote URL of `https://localhost:9443 ` is
 ` EGERIA_PRESENTATIONSERVER_SERVER_aaa={"remoteServerName":"cocoMDSV1","remoteURL":"https://localhost:9443"}`
* To run the presentation server in production, use the presentation server assembly and specify the environment variables as required.   

## For development
 * Add the presentation-server folder into [Visual Studio Code](https://code.visualstudio.com/). It will create a new workspace.
 * You can open a terminal in Visual Studio code to issue the npm commands
 * Set up your environment by running `npm install`
 * Developers can specify the environment variable(s) in a .env file, by copying [.env_sample](nodejs/.env_sample) to a file
 called .env in the same folder and amend the values as required.
 * Once the .env file is in place, build and start the server using `npm start`. Use other npm parameters as specified in [package.json](nodejs/package.json).  
 
## Access UI using the browser. 
 * Enter 'https://localhost:8091/<tenant-name>/' on the UI to access the tenanted UI (<tenant-name> is the serverName used by the presentation server). The Ui will prompt for a login; some of the 
 Coco Pharmaceutical personnas have been enabled here - use user 'faith' and password 'admin'. If there is an environment variable :
  ` EGERIA_PRESENTATIONSERVER_SERVER_aaa={"remoteServerName":"cocoMDSV1","remoteURL":"https://localhost:9443"}`
  then the browser url to use to login is `https://localhost:8091/aaa`. All browser requests for this tenant will be issued using urls starting
  `https://localhost:8091/aaa`. 
    
  

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.