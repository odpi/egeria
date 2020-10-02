## Type Explorer User Interface

The Type Explorer (Tex) interface provides a way to explore the Egeria type system. It can be used to retrieve the types supported by a chosen server. The type information is then displayed both textually (on the left side) and diagrammatically (on the right side) with a choice of diagrams. One of the diagrams shows the entity inheritance tree and the other diagram shows the types of relationships that an entity type supports.

![Tex Interface](image1)

### Deployment
The Tex interface runs under the Presentation Server component of Egeria, and it uses the Type Explorer (Tex) View Service in order to retrieve the information you ask for. For details of the Presentation Server and View Server please refer to their readme information.


### Configuration
The Tex View Service is an Integration View Service and is configured using `IntegrationViewServiceConfig`. This contains the name and status of the View Service and also contains a list of the resources that the Tex interface is permitted to access. These resources are the platforms and servers that the user will be able access. The configured servers will appear in the selectors in the Tex interface. All requests to the Tex View Service REST API are based on these configured named resources. When a user selects a server name from the selector lists, the interface sends the resource name to the Tex View Service, which resolves the server name to a resource endpoint to identify the URL needed to send a request to the server. 

In the Tex View Service configuration, the view service administrator configures a list of Resource Endpoints to represent platforms and servers. Every `ResourceEndpointConfig` needs a `resourceCategory`, which is set to either `"Platform"` or `"Server"`. A platform `ResourceEndpointConfig` must have a unique `platformName` and a `platformRootURL` and can have an optional `description` property. A server `ResourceEndpointConfig` for a server must have a `serverName` and the `platformName` of a configured platform resource endpoint. A server can have an optional `description` property. In an Egeria deployment, a server may be deployed to multiple platforms - this is typically used for clustering - and a server ResourceEndpointConfig contains a property called serverInstanceName which must be unique and is used to name a specific instance of a server. For example, you could configure a pair of server resource endpoints called "Server1@PlatformA" and "Server1@PlatformB". The serverInstanceName is used to display the resource in the user interface selector lists. 

An example configuration with two platforms and two servers is shown below:

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
                    "description"        : "This platform is running in the cloud",
                    "platformName"       : "Platform2",
                    "platformRootURL"    : "https://localhost:9443"
                },
                {
                    "class"              : "ResourceEndpointConfig",
                    "resourceCategory"   : "Platform",
                    "description"        : "This platform is running on a Raspberry Pi",
                    "platformName"       : "Platform1",
                    "platformRootURL"    : "https://localhost:8082"
                },
                {
                    "class"              : "ResourceEndpointConfig",
                    "resourceCategory"   : "Server",
                    "serverInstanceName" : "Central Metadata Server",
                    "description"        : "Metadata server used as home for central artefacts",
                    "platformName"       : "Platform1",
                    "serverName"         : "Metadata_Server"
                },
                {
                    "class"              : "ResourceEndpointConfig",
                    "resourceCategory"   : "Server",
                    "serverInstanceName" : "Departmental Metadata Server",
                    "description"        : "Metadata server used as home for departmental artefacts",
                    "platformName"       : "Platform2",
                    "serverName"         : "Metadata_Server2"
                }
            ]
        }
```


### Using the Tex Interface
When the Tex interface first loads, it sends a request to the Tex View Service to load the configured server resources. The servers are loaded into the `"Server Links"` selector at the top of the page. 

The listed servers are the only ones the user will be able to retrieve type information from.

Typically a user may start by selecting a server from the `"Server Links"` selector. The type information for the server will be loaded. If a particular type is selected (clicked) it becomes the focus type; it will be displayed in the details area on the left side and highlighted in a diagram on the right side. The user can view the type's attributes and can click on types in the attiributes or in the diagrams to get more details about the selected type and to make that type the new focus.

There are two diagrams, one that shows the entity inheritance hierarchy and another that shows the types of relationships that an entity type supports 

The entity inheritance diagram is shown as a set of expandable/collapsible trees. There are multiple trees because not all entity types inherit from a common root. The entity inheritance diagram will be displayed as soon as the server's types have been retrieved, as it does not require that an entity type is chosen as the focus type. The user can select an entity type to focus (by clicking in the diagram or from the type selector) to see the details of that type and highlight it in the diagram.

![Inheritance Diagram](image2)

The other diagram is the neighborhood diagram, which is displayed as a radial plot showing the types of relationship that the focus entity type supports. The user needs to have selected a focus entity type in order for the neighborhood diagram to be displayed. For each supported relationship type shownb in the neighborhood diagram, the entity type of the neighboring entity is shown (at the outer end of the radial plot). The user can select any of the entity or relationship types in the neighborhood diagram to change the focus to that type see more details about it.

![Neighborhood Diagram](image3)

