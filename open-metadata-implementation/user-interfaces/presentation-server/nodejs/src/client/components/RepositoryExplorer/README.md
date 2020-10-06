## Repository Explorer User Interface

The Repository Explorer (Rex) interface provides a way to query Egeria metadata servers to retrieve and display about the metadata instances they contain. It is possible to retrieve entities and relationships, view their properties and display them as a graph of interconnected objects. The instances are presented both textually (on the left side) and diagrammatically (on the right side).

![Rex Interface](image1)
TODO - replace image


### Deployment
The Repository Explorer (Rex) interface runs under the Presentation Server component of Egeria, and it uses the Repository Explorer (Rex) View Service in order to retrieve the information you ask for. For details of the Presentation Server and View Services please refer to their readme information, located here [Presentation Server](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/user-interfaces/presentation-server/README.md) and [View Services](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/view-services/README.md).


### Configuration
The Rex View Service is an Integration View Service and is configured using the admin-services API to post the configuration to `open-metadata/admin-services/users/{userName}/servers/{serverName}/view-services/{vewServiceName}`. The configuration is supplied in `IntegrationViewServiceConfig`, described below or in the View Services README.md (see above). This contains the name and operational status of the View Service and also contains a list of the resource endpoints that the Rex interface is permitted to access. This list is a list of `ResourceEndpointConfig` objects, as in the example below, that represent platforms and servers.

The list of resource endpoints configured here determines the content of the lists of platforms and servers that will appear in the selectors in the Rex interface. All requests to the Rex View Service REST API are based on these configured resources, so these are the platforms and servers that the user will be able to access.

Every `ResourceEndpointConfig` needs a `resourceCategory`, which is set to either `"Platform"` or `"Server"`. 

A platform `ResourceEndpointConfig` identifies an Egeria platform. It must have a unique `platformName` and a `platformRootURL` and can have an optional `description` property. 

A server `ResourceEndpointConfig` identifies an instance of a service running on a particular Egeria platform. In an Egeria deployment, a server may be deployed to multiple platforms - this is typically used for clustering. For example, you could configure a pair of server resource endpoints for "Server1@PlatformA" and "Server1@PlatformB". A server  `ResourceEndpointConfig` is uniquely identified by its `serverInstanceName`, which refers a specific instance of the server - i.e. the combination of server and platform. The `serverInstanceName` must be unique; it is used to identify the resource in the user interface server selector list. A server `ResourceEndpointConfig` must have a unique `serverInstanceName`; it is used to identify the resource in the user interface server selector list. It must also contain the `serverName` of the Egeria server it refers to and must contain the `platformName` of the `ResourceEndpointConfig` for the platform that runs this instance of the server. It can also have an optional `description` property. 

When a user selects a platform from the selector lists, the user interface sends the `platformName` to the Rex View Service. 
When a user selects a server from the selector lists, the user interface sends the `serverName` and `platformName` to the Rex View Service. The Rex View Service uses the `platformName` to identify the matching platform resource endpoint so it can resolve the URL needed to send a request to the platform or server.

An example configuration with two platforms and two servers is shown below. You would need to replace the <hostname> and <port> variables with your own values:

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


### Using the Rex Interface
When the Rex interface first loads, it sends a request to the Repository Explorer View Service to load the configured server resources. The servers are loaded into the `Accessible Servers` selector at the top of the page. 

The listed servers are the only ones the user will be able to interrogate directly, although they can set the Enterprise checkbox to perform Enterprise queries, which federate results from the cohorts the chosen server has joined.

Typically a user may start by selecting a server from the `Accessible Servers` selector. The metadata types supported by the server will be queried and will be used to populate the search filters towrd the right hand side at the top of the interface.

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
