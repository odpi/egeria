<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
 
![In Development](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Presentation Server

The **Presentation Server** is a multi-tenant server that serves a user interface - it issues rest calls downstream primarily to view
servers.    

See the [Egeria Planning guide](https://github.com/odpi/egeria/tree/master/open-metadata-publication/website/planning-guide) to familiarise yourself with 
the types of [Egeria OMAG Servers](../../admin-services/docs/concepts/omag-server.md); Presentation Server is one of those servers.  

There are 2 types of capabilities exposed in the Presentation Server.
- Integration View Services
- Governance Solution View Services  

### Integration View Services

- Type Explorer: allowing a user to explore the Egeria open types
- Repository Explorer: allowing a user to explore Egeria instances
- Server Author: allowing a user to configure a server. This is work in progress.
- Dino: allowing a user to see and work with Egeria platforms and servers operationally.      

### Governance Solution View Services  
- Glossary Author: allowing a Glossary Author use to create, update and delete Glossary content.       

## Setting up the downstream servers  
 To run the Presentation Server, you need to have correctly [configured](../../admin-services/docs/user/configuring-an-omag-server.md) and be [operating](../../admin-services/docs/user/operating-omag-server.md) the downstream servers.
 Be aware that in addition to any existing OMAG Servers, the Presentation Server requires that you have [configured the view services](../../admin-services/docs/user/configuring-the-view-services.md) on a View Server. 

 For example for the Glossary Author capability, the Presentation Server issues Open Metadata View Server (OMVS) calls to the [View Server](../../admin-services/docs/concepts/view-server.md)
 which issues [Open Metadata Access Service (OMAS)](../../access-services/README.md) calls to the [Access Point Server](../../admin-services/docs/concepts/metadata-access-point.md).     
 
## Configuring the Presentation Server 
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

## For development
 * Add the presentation-server folder into [Visual Studio Code](https://code.visualstudio.com/). It will create a new workspace.
 * You can open a terminal in Visual Studio Code to issue the npm commands
 * Set up your environment by running `npm install`
 * Developers can specify the environment variable(s) in a .env file, by copying [.env_sample](nodejs/.env_sample) to a file
 called .env in the same folder and amend the values as required.
 * Once the .env file is in place, build and start the server using `npm start`. Use other npm parameters as specified in [package.json](nodejs/package.json).  

## Access UI using the browser. 
 * Enter 'https://localhost:8091/<tenant-name>/login' on the UI to access the tenanted UI (<tenant-name> is the serverName used by the presentation server). The Ui will prompt for a login; some of the 
 Coco Pharmaceutical personnas have been enabled here - use user 'faithbroker' and password 'admin'. If there is an environment variable :
  ` EGERIA_PRESENTATIONSERVER_SERVER_aaa={"remoteServerName":"cocoView1","remoteURL":"https://localhost:9443"}`
  then the browser url to use to login is `https://localhost:8091/aaa`. All browser requests for this tenant will be issued using urls starting
  `https://localhost:8091/aaa`. 

## There is on-going work to enhance the development experience 
 * The build described above is a production build; meaning everything is rebuilt each time a change is made.
 * There is ongoing work to allow developers to do be able to make changes and hot swap them into a running server.
 * The ongoing work is being tracked in issue [Git issue 3543](https://github.com/odpi/egeria/issues/3543)
 * The work involves using Create React App. This will be done iteratively.
 * The new work is being developed in the cra-client directory and cra-server directory, and may contain out of date Presentation Server source code. 
 * To start the new client in development mode: navigate to the `cra-client` and `cra-server` directories. Run `npm install` inside each. Then, navigate into `cra-server` and run `npm start`.
 * To start the new client in production mode: navigate into `cra-client`, then run `npm run build`. Then, navigate into `cra-server` and run `npm run prod`.
 * PLEASE NOTE: this should NOT be considered the main build yet. This is still in development.

## There is ongoing work to enable easy running of the new Presentation Server and view services in a demo environment
 * The Coco Pharmaceuticals 'lab' tutorial environment will now configure the view services, and allow a user to experiment with the Presentation Server
 * The 'egeria-server-config' notebook has code added to configure the view services - so once this & egeria-server-start is run, Presentation Server is available.
 * The Presentation Server code is built under maven (mvn clean install) & added to a new assembly (open-metadata-distribution/open-metadata-assemblies) as part of the overall build.
 * A new docker image is created as 'odpi/egeria-presentation-server' containing Presentation Server.
 * Our docker-compose based tutorial now has Presentation Server included & configured via the environment variable, and accessed at https://localhost:18091/coco/login .
 * The Kubernetes tutorial 'odpi-egeria-lab' has also been updated similarly, with the Presentation Server UI being acessible via port 30091 - ie access via https:<<address-of-k8s-node>/coco.login
 * For the lab environment it is recommended to use user 'garygeeke' and password 'admin' since this environment has security setup, and other users including faithbroker will not have access to all capabilities. 
 * This is still work in progress. For example if the session times out you will need to go to the UI URL again manually.
 * Contact us via Slack on odpi.slack.com to get additional guidance.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.