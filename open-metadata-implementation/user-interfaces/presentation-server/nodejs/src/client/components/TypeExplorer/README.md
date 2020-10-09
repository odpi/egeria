## Type Explorer User Interface

The Type Explorer (Tex) interface provides a way to explore the Egeria type system. It can be used to retrieve the types supported by a chosen server. The type information is then displayed both textually (on the left side) and diagrammatically (on the right side) with a choice of diagrams. One of the diagrams shows the entity inheritance tree and the other diagram shows the types of relationships that an entity type supports.

![Tex Interface](image1)

### Deployment
The Tex interface runs under the Presentation Server component of Egeria, and it uses the Type Explorer (Tex) View Service in order to retrieve the information you ask for. For details of the Presentation Server and View Services please refer to their readme information, located here [Presentation Server](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/user-interfaces/presentation-server/README.md) and [View Services](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/view-services/README.md).


### Configuration
The Tex View Service is an Integration View Service and is configured using the admin-services API to post the configuration to `open-metadata/admin-services/users/{userName}/servers/{serverName}/view-services/{vewServiceName}`. The configuration is supplied in `IntegrationViewServiceConfig`, described below or in the View Services README.md (see above). This contains the name and operational status of the View Service and also contains a list of the resource endpoints that the Tex interface is permitted to access. This list is a list of `ResourceEndpointConfig` objects, as in the example below, that represent platforms and servers. 

The list of resource endpoints configured here determines the list of servers that will appear in the selectors in the Tex interface. All requests to the Tex View Service REST API are based on these configured resources, so these are the servers that the user will be able to access.

Every `ResourceEndpointConfig` needs a `resourceCategory`, which is set to either `"Platform"` or `"Server"`. 

A platform `ResourceEndpointConfig` identifies an Egeria platform. It must have a unique `platformName` and a `platformRootURL` and can have an optional `description` property. 

A server `ResourceEndpointConfig` identifies an instance of a service running on a particular Egeria platform. In an Egeria deployment, a server may be deployed to multiple platforms - this is typically used for clustering. For example, you could configure a pair of server resource endpoints for "Server1@PlatformA" and "Server1@PlatformB". A server  `ResourceEndpointConfig` is uniquely identified by its `serverInstanceName`, which refers a specific instance of the server - i.e. the combination of server and platform. The `serverInstanceName` must be unique; it is used to identify the resource in the user interface server selector list. A server `ResourceEndpointConfig` must have a unique `serverInstanceName`; it is used to identify the resource in the user interface server selector list. It must also contain the `serverName` of the Egeria server it refers to and must contain the `platformName` of the `ResourceEndpointConfig` for the platform that runs this instance of the server. It can also have an optional `description` property. 

When a user selects a server from the selector lists, the user interface sends the `serverName` and `platformName` to the Tex View Service. The Tex View Service uses the `platformName` to identify the matching platform resource endpoint so it can resolve the URL needed to send a request to the server.

An example configuration with two platforms and two servers is shown below. You would need to replace the <hostname> and <port> variables with your own values:

```
{
            "class":"IntegrationViewServiceConfig",
            "viewServiceAdminClass":"org.odpi.openmetadata.viewservices.tex.admin.TexViewAdmin",
            "viewServiceFullName":"Tex",
            "viewServiceOperationalStatus":"ENABLED",            
            "resourceEndpoints" : [
                {
                    "class"              : "ResourceEndpointConfig",
                    "resourceCategory"   : "Platform",
                    "platformName"       : "Platform2",
                    "platformRootURL"    : "https://<hostname>:<port>",
                    "description"        : "This platform is running in the development cloud"
                },
                {
                    "class"              : "ResourceEndpointConfig",
                    "resourceCategory"   : "Platform",
                    "platformName"       : "Platform1",
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


### Using the Tex Interface
When the Tex interface first loads, it sends a request to the Tex View Service to load the configured server resources. The servers are loaded into the `Select Server` selector at the top of the page. 

The listed servers are the only ones the user will be able to retrieve type information from.

Typically a user may start by selecting a server from the `Select Server` selector. The type information for the server will be loaded. If a particular type is selected (clicked) it becomes the focus type; it will be displayed in the details area on the left side and highlighted in a diagram on the right side. The user can view the type's attributes and can click on types in the attiributes or in the diagrams to get more details about the selected type and to make that type the new focus.

There are two diagrams, one that shows the entity inheritance hierarchy and another that shows the types of relationships that an entity type supports.

The entity inheritance diagram is shown as a set of expandable/collapsible trees. There are multiple trees because not all entity types inherit from a common root. The entity inheritance diagram will be displayed as soon as the server's types have been retrieved; it does not require that an entity type is chosen as the focus type. The user can select an entity type to focus (by clicking in the diagram or from the type selector) to see the details of that type and highlight it in the diagram.

![Inheritance Diagram](image2)

The other diagram is the neighborhood diagram, which is displayed as a radial plot showing the types of relationship that the focus entity type supports. The user needs to have selected a focus entity type in order for the neighborhood diagram to be displayed. For each supported relationship type shownb in the neighborhood diagram, the entity type of the neighboring entity is shown (at the outer end of the radial plot). The user can select any of the entity or relationship types in the neighborhood diagram to change the focus to that type and see more details about it.

![Neighborhood Diagram](image3)

