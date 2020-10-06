## Dino User Interface

The Dino interface provides a way to visualize Egeria platforms, servers, the services they are running and the cohorts they have joined. The information is presented both textually (on the left side) and diagrammatically (on the right side).

![Dino Interface](image1)


### Deployment
The Dino interface runs under the Presentation Server component of Egeria, and it uses the Dino View Service in order to retrieve the information you ask for. For details of the Presentation Server and View Services please refer to their readme information, located here [Presentation Server](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/user-interfaces/presentation-server/README.md) and [View Services](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/view-services/README.md).


### Configuration
The Dino View Service is an Integration View Service and is configured using the admin-services API to post the configuration to `open-metadata/admin-services/users/{userName}/servers/{serverName}/view-services/{vewServiceName}`. The configuration is supplied in `IntegrationViewServiceConfig`, described below or in the View Services README.md (see above). This contains the name and operational status of the View Service and also contains a list of the resource endpoints that the Dino interface is permitted to access. This list is a list of `ResourceEndpointConfig` objects, as in the example below, that represent platforms and servers.

The list of resource endpoints configured here determines the content of the lists of platforms and servers that will appear in the selectors in the Dino interface. All requests to the Dino View Service REST API are based on these configured resources, so these are the platforms and servers that the user will be able to access.

  Every `ResourceEndpointConfig` needs a `resourceCategory`, which is set to either `"Platform"` or `"Server"`. 

  A platform `ResourceEndpointConfig` identifies an Egeria platform. It must have a unique `platformName` and a `platformRootURL` and can have an optional `description` property. 

  A server `ResourceEndpointConfig` identifies an instance of a service running on a particular Egeria platform. In an Egeria deployment, a server may be deployed to multiple platforms - this is typically used for clustering. For example, you could configure a pair of server resource endpoints for "Server1@PlatformA" and "Server1@PlatformB". A server  `ResourceEndpointConfig` is uniquely identified by its `serverInstanceName`, which refers a specific instance of the server - i.e. the combination of server and platform. The `serverInstanceName` must be unique; it is used to identify the resource in the user interface server selector list. A server `ResourceEndpointConfig` must have a unique `serverInstanceName`; it is used to identify the resource in the user interface server selector list. It must also contain the `serverName` of the Egeria server it refers to and must contain the `platformName` of the `ResourceEndpointConfig` for the platform that runs this instance of the server. It can also have an optional `description` property. 

  When a user selects a platform from the selector lists, the user interface sends the `platformName` to the Dino View Service. 
  When a user selects a server from the selector lists, the user interface sends the `serverName` and `platformName` to the Dino View Service. The Dino View Service uses the `platformName` to identify the matching platform resource endpoint so it can resolve the URL needed to send a request to the platform or server.

  An example configuration with two platforms and two servers is shown below. You would need to replace the <hostname> and <port> variables with your own values:


```
{
            "class":"IntegrationViewServiceConfig",
            "viewServiceAdminClass":"org.odpi.openmetadata.viewservices.dino.admin.DinoViewAdmin",
            "viewServiceFullName":"Dino",
            "viewServiceOperationalStatus":"ENABLED",
            "resourceEndpoints" : [
                 {
                     "class"              : "ResourceEndpointConfig",
                     "resourceCategory"   : "Platform",
                     "platformName"       : "Platform1",
                     "platformRootURL"    : "https://<hostname>:<port>",
                     "description"        : "This platform is running in the development cloud"
                 },
                 {
                     "class"              : "ResourceEndpointConfig",
                     "resourceCategory"   : "Platform",
                     "platformName"       : "Platform2",
                     "platformRootURL"    : "https://<hostname>:<port>",
                     "description"        : "This platform is running in the departmental test cluster"               
                 },
                 {
                     "class"              : "ResourceEndpointConfig",
                     "resourceCategory"   : "Server",
                     "serverInstanceName" : "Central Metadata Server",
                     "serverName"         : "Metadata_Server",
                     "platformName"       : "Platform1",
                     "description"        : "Metadata server with home reopsitory for schema artefacts"
                 },
                 {
                     "class"              : "ResourceEndpointConfig",
                     "resourceCategory"   : "Server",
                     "serverInstanceName" : "Supplementary Metadata Server",
                     "serverName"         : "Metadata_Server2",
                     "platformName"       : "Platform2",
                     "description"        : "Metadata server with home repository for review artefacts"
                 }
             ]
        }
```


### Using the Dino Interface
When the Dino interface first loads, it sends a request to the Dino View Service to load the configured platform and server resources. The platforms are loaded into the `Accessible Platforms` selector at the top of the page. The servers are loaded into the `Direct Server Links` selector at the top of the page. 

The listed platforms are the only ones the user will be able to interrogate, but from a platform they will be able to see the platform's registered services and query the platform to show the servers that are running (or that have run) on that platform. From there they can continue to explore. 

The listed servers are included for convenience so that a user who wants to see a particular server does not need to know which platform it is running on. Instead they can use the `serverInstanceName` in the list of servers.

Typically a user may start by selecting a platform from the `Accessible Platforms` selector. The platform will be loaded and will be made the focus resource; it will be displayed in the details area on the left side and represented by a platform icon in the diagram on the right side. The user can expand sections in the details and use the embedded buttons to query the (active or known) servers. Alternatively they can right click on the platform icon and use the platform pop-up menu to perform these operations.

Clicking the `Get Active Servers` or `Get All Servers` buttons, or using the platform pop-up menu, will load the servers into the diagram. If a server is connected to a platform by a solid line this indicates that the server is active. A dotted line is used to indicate that the server is not currently active.

![Topology Diagram](image2)

To make a server the focus the user can click on it, or use the right click popup-menu. This results in the server details being loaded into the details area, containing server properties and expandable sections for additional detail. If the user expands the `Cohorts` section and expands a particular cohort, that cohort will be added to the diagram. If a cohort is connected to a server by a solid line this indicates that the server is actively connected to the cohort. A dotted line is used where the server is not currently active. Similarly for `Services` - if a service is expanded it will be added to the diagram. 

If the user expands the server's `Configuration` section, the Dino interface will send a request to the Dino View Service to load the server's configuration. This actually loads the stored configuration and (if the server is running) also the active configuration. The radio buttons can be used to display either the `Stored Config`, the `Active Config` or see if there are any  `Differences` between them. There are sections within the configuration details that can be expanded to see the configuration for access services, common services, governance services, view services, repository services, and the event bus. There is also a section that shows the configuration audit trail.

### Diagram Controls
The diagram has two layout modes - 'time-based' and 'proximity-based'. The 'time-based' layout arranges objects vertically on the diagram with the newest toward the bottom. The 'proxmity-based' layout allows the graph of objects to organize itself based on connectivity. 

If the 'pin dragged objbect' checkbox is checked, when the user drags an object it will be pinned where it is dropped. It can be dragged again if needed, but it will always stay where it is dropped. To release an individual object, press shift and click on the object. To allow objects to move around independently, uncheck the 'pin dragged objbect' option.

### Undo and Clear
There are buttons to undo the most recent operation (`Undo`) or clear (`Clear`) the whole graph.
