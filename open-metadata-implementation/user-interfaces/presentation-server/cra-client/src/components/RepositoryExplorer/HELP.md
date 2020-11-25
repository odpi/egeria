## Repository Explorer User Interface

&nbsp;

The Repository Explorer (Rex) interface provides a way to query Egeria metadata servers to retrieve and display about the metadata instances they contain. It is possible to retrieve entities and relationships, view their properties and display them as a graph of interconnected objects. The instances are presented both textually (on the left side) and diagrammatically (on the right side).

&nbsp;

![Rex Interface](image1)

&nbsp;

### Using the Rex Interface
When the Rex interface first loads, it sends a request to the Repository Explorer View Service to load the configured server resources. The servers are loaded into the `Select Server` selector at the top of the page. The listed servers are the only ones you can interrogate directly, although you can set the Enterprise checkbox to perform Enterprise queries, which federate results from the cohorts the chosen server has joined.

&nbsp;

You should start by selecting a server from the `Select Server` selector. The metadata types supported by the server will be queried and will be used to populate the search filters in the top right hand area of the interface.

&nbsp;

You can now start accessing metadata instances (entities and relationships) known to the server you selected. 

&nbsp;

If you know the GUID of the metadata instance you wish to retrieve, enter the GUID into the `Instance GUID` field and press the `Get instance by GUID` button. 

&nbsp;

If you don't know the GUID you can use the Search tool further to the right. Enter search text in the `Search text` field. The search text should be a string value that will match any string property of the instance you are looking for. The Egeria-provided repositories support general regular expression syntax, so you could specify a search string like `".*egeria.*"` which would match any property value containing the string `"egeria"`. You can literalize special characters; the search string `".*egeria\.repository.*"` would match values containining the string `"egeria.repository"`. Other repositories might not support regular expressions, or might support a constrained regexp syntax. In the latter case you might need to literalize the search expression by using the `"\Q...\E"` escape sequence, for example `"\Qother.repository\E"`, in which all characters between the `\Q` and `\E` are interpreted as literals.

&nbsp;

You can optionally narrow a search by setting the type filters just beneath the `Search text` field. Then press the `Search for instances` button. This will initiate a search for metadata instances matching the criteria you specified. The results of the search operation will be displayed in a dialog, from which you can select which instances to retrieve. These instances will be added to the graph.

&nbsp;

![Search Dialog](image2)

&nbsp;

Each retrieved instance is added to the diagram. If only one instance is retrieved it will automatically become the focus, which means it will be highlighted in the diagram and its properties will be displayed on the left-hand side. You can set the focus to a specific instance at any time by clicking on that instance. For either entities or relationships, clicking on an instance will show the entity or relationship properties.

&nbsp;

You can explore the neighborhood around the focus entity by clicking on the Explore button to perform a graph traversal. This will present a dialog that allows you to refine the graph traversal by filtering the types of entities and relationships that will be retrieved, or by only retrieving entities that have particular types of classification. When the traversal is complete, the entities and relationships are added to the graph.

&nbsp;

### Diagram Controls
The diagram has two layout modes - 'time-based' and 'proximity-based'. The 'time-based' layout arranges objects vertically on the diagram with the newest toward the bottom. The 'proxmity-based' layout allows the graph of objects to organize itself based on connectivity. 

&nbsp;

If the 'pin dragged entities' checkbox is checked, when the user drags an object it will be pinned where it is dropped. A pinned entity is indicated by a small 'pin' drawn vertically beneath the entity. It can be dragged again if needed, but it will always stay where it is dropped. To release an individual object, press shift and click on the object. To allow objects to move around independently, uncheck the 'pin dragged entities' option.

&nbsp;

### Undo and Clear
There are buttons to undo the most recent operation (`Undo`) or clear (`Clear`) the whole graph.

&nbsp;

### History
There is a `History` button to display a summary of the operations performed by the user since the graph was last empty.
