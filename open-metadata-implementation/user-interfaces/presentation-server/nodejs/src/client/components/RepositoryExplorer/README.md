## Repository Explorer User Interface

The Repository Explorer (Rex) interface provides a way to query Egeria metadata servers to retrieve and display about the metadata instances they contain. It is possible to retrieve entities and relationships, view their properties and display them as a graph of interconnected objects. The instances are presented both textually (on the left side) and diagrammatically (on the right side).

![Rex Interface](image1)
TODO - replace image


### Deployment
The Rex interface runs under the Presentation Server component of Egeria, and it uses the Repository Explorer View Service in order to query the metadata servers. For details of the Presentation Server and View Server please refer to their readme information.


### Configuration
The Repository Explorer View Service is an Integration View Service and is configured using `IntegrationViewServiceConfig`. This contains the name and status of the View Service and also contains a list of the resources that the Rex interface is permitted to access. These resources are the platforms and servers that the user will be able access. The configured servers will appear in the selectors in the Rex interface. All requests to the Repository Explorer View Service REST API are based on these configured named resources. When a user selects a server name from the selector list, the interface sends the resource name to the Repository Explorer View Service, which resolves the server name to a resource endpoint to identify the URL needed to send a request to the server. 

In the Repository Explorer View Service configuration, the view service administrator configures a list of Resource Endpoints to represent platforms and servers. Every `ResourceEndpointConfig` needs a `resourceCategory`, which is set to either `"Platform"` or `"Server"`. A platform `ResourceEndpointConfig` must have a unique `platformName` and a `platformRootURL` and can have an optional `description` property. A server `ResourceEndpointConfig` for a server must have a `serverName` and the `platformName` of a configured platform resource endpoint. A server can have an optional `description` property. In an Egeria deployment, a server may be deployed to multiple platforms - this is typically used for clustering - and a server ResourceEndpointConfig contains a property called serverInstanceName which must be unique and is used to name a specific instance of a server. For example, you could configure a pair of server resource endpoints called "Server1@PlatformA" and "Server1@PlatformB". The serverInstanceName is used to display the resource in the user interface selector lists. 

An example configuration with two platforms and two servers is shown below:

```
{
            "class":"IntegrationViewServiceConfig",
            "viewServiceAdminClass":"org.odpi.openmetadata.viewservices.rex.admin.RexViewAdmin",
            "viewServiceFullName":"RepositoryExplorer",
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


### Using the Rex Interface
When the Rex interface first loads, it sends a request to the Repository Explorer View Service to load the configured server resources. The servers are loaded into the `"Accessible Servers"` selector at the top of the page. 

The listed servers are the only ones the user will be able to interrogate directly, although they can set the Enterprise checkbox to perform Enterprise queries, which federate results from the cohorts the chosen server has joined.

Typically a user may start by selecting a server from the `"Accessible Servers"` selector. The metadata types supported by the server will be queried and will be used to populate the search filters towrd the right hand side at the top of the interface.

The user can then retrieve an instance by GUID, if they know the GUID of the instance, or by using the Search tool further to the right. The results of a search operation are displayed in a dialog allowing the user to select which instances to retrieve. 

Each retrieved instance is added to the diagram. If only one instance is retrieved it will automatically become the focus and will be highlighted in the diagram and its properties will be displayed on the left-hand side.  

![Topology Diagram](image2)
TODO - replace image

The user can explore the neighborhood around the focus entity, by clicking on the Explore button to perform a graph traversal. This will present a dialog that allows the user to refine the graph traversal according to the types of entities and relationships in the neighborhood, or by only traversing to entities that have classifications chosen by the user.

Like for entities, the user can click on a displayed relationship to show the relationship's properties.

### Diagram Controls
The diagram has two layout modes - 'time-based' and 'proximity-based'. The 'time-based' layout arranges objects vertically on the diagram with the newest toward the bottom. The 'proxmity-based' layout allows the graph of objects to organize itself based on connectivity. 

If the 'pin dragged entities' checkbox is checked, when the user drags an object it will be pinned where it is dropped. A pinned entity is indicated by a small 'pin' drawn vertically beneath the entity. It can be dragged again if needed, but it will always stay where it is dropped. To release an individual object, press shift and click on the object. To allow objects to move around independently, uncheck the 'pin dragged entities' option.

### Undo and Clear
There are buttons to undo the most recent operation (`Undo`) or clear (`Clear`) the whole graph.

### History
There is a `History` button to display a summary of the operations performed by the user since the graph was last empty.
