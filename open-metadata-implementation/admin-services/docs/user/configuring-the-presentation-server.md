<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuring the Presentation Server

The [Presentation Server](../../../user-interfaces/presentation-server) is a multi-tenant web application that calls 
view services to retrieve information and perform operations relating to metadata servers.

### Configuring the Presentation Server 
* The Presentation Server supports multi-tenants. A Presentation Server tenant has a serverName and associated configuration.
* The configuration for the Presentation Server is minimal, only specifying how to access a downstream server.     
* Each tenant's configuration is specified in an environment variable that contains the tenant's serverName. 
* The environment variable is called `EGERIA_PRESENTATIONSERVER_SERVER_<ui server name>`  when the <ui server name> is the tenants
 serverName.
* The value of the environment variable is a json payload with 2 named properties
    * `remoteServerName` the remote view server name.
    * `remoteURL` the platform root url for the view server. 
* An example of the environment variable you would specify for a UI tenant with server name `aaa` with a remote server name
`  cocoView1` and remote URL of `https://localhost:9443 ` is
 ` EGERIA_PRESENTATIONSERVER_SERVER_aaa={"remoteServerName":"cocoView1","remoteURL":"https://localhost:9443"}`
* To run the Presentation Server in production, use the Presentation Server assembly and specify the environment variables as required.   

### For development
 * Add the presentation-server folder into [Visual Studio Code](https://code.visualstudio.com/). It will create a new workspace.
 * You can open a terminal in Visual Studio Code to issue the npm commands
 * Set up your environment by running `npm install`
 * Developers can specify the environment variable(s) in a .env file, by copying [.env_sample](nodejs/.env_sample) to a file
 called .env in the same folder and amend the values as required.
 * Once the .env file is in place, build and start the server using `npm start`. Use other npm parameters as specified in [package.json](nodejs/package.json).  

### Access UI using the browser. 
 * Enter 'https://localhost:8091/<tenant-name>/login' on the UI to access the tenanted UI (<tenant-name> is the serverName used by the presentation server). The Ui will prompt for a login; some of the 
 Coco Pharmaceutical personnas have been enabled here - use user 'faithbroker' and password 'admin'. If there is an environment variable :
  ` EGERIA_PRESENTATIONSERVER_SERVER_aaa={"remoteServerName":"cocoView1","remoteURL":"https://localhost:9443"}`
  then the browser url to use to login is `https://localhost:8091/aaa`. All browser requests for this tenant will be issued using urls starting
  `https://localhost:8091/aaa`. 



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.