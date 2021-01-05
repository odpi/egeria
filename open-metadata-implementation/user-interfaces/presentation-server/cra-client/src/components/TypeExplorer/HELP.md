## Type Explorer User Interface

&nbsp;

The Type Explorer (Tex) interface provides a way to explore the Egeria type system. It can be used to retrieve the types supported by a chosen server. The type information is then displayed both textually (on the left side) and diagrammatically (on the right side) with a choice of diagrams. One of the diagrams shows the entity inheritance tree and the other diagram shows the types of relationships that an entity type supports.

&nbsp;

![Tex Interface](image1)

&nbsp;

### Using the Tex Interface
When the Tex interface first loads, it sends a request to the Tex View Service to load the configured server resources. The servers are loaded into the `Select Server` selector at the top of the page. 

&nbsp;

The listed servers are the only ones the user will be able to retrieve type information from.

&nbsp;

Typically a user may start by selecting a server from the `Select Server` selector. The type information for the server will be loaded. If a particular type is selected (clicked) it becomes the focus type; it will be displayed in the details area on the left side and highlighted in a diagram on the right side. The user can view the type's attributes and can click on types in the attiributes or in the diagrams to get more details about the selected type and to make that type the new focus.

&nbsp;

There are two diagrams, one that shows the entity inheritance hierarchy and another that shows the types of relationships that an entity type supports.

&nbsp;

The entity inheritance diagram is shown as a set of expandable/collapsible trees. There are multiple trees because not all entity types inherit from a common root. The entity inheritance diagram will be displayed as soon as the server's types have been retrieved; it does not require that an entity type is chosen as the focus type. The user can select an entity type to focus (by clicking in the diagram or from the type selector) to see the details of that type and highlight it in the diagram.

&nbsp;

![Inheritance Diagram](image2)

&nbsp;

The other diagram is the neighborhood diagram, which is displayed as a radial plot showing the types of relationship that the focus entity type supports. The user needs to have selected a focus entity type in order for the neighborhood diagram to be displayed. For each supported relationship type shownb in the neighborhood diagram, the entity type of the neighboring entity is shown (at the outer end of the radial plot). The user can select any of the entity or relationship types in the neighborhood diagram to change the focus to that type and see more details about it.

&nbsp;

![Neighborhood Diagram](image3)

&nbsp;

There are three checkboxes, located just beneath the help icon, that provide finer control of the types that are retrieved from the server and the way that they are displayed.

&nbsp;

The `Enterprise` checkbox allows Type Explorer to perform an enterprise scope query rather than a local query of the selected server.

&nbsp;

The `Include deprecated types` checkbox instructs Type Explorer to retrieve all types from the server, whether or not the types are deprecated. This can be useful for closer inspection of the type system. The name of a deprecated type will be displayed in square brakcets, like this `[<type name>]`. If this option is checked and a deprecated entity type is the focus (i.e. is highlighted in the diagram and its details are shown on the left hand side), then if the `Include deprecated types` is unchecked, Type Explorer will
automatically clear the focus because the deprecated entity type will no longer be present.

&nbsp;

The `Include deprecated attributes` checkbox instructs Type Explorer to display any deprecated attributes. When this option is checked, any deprecated attributes will be displayed. The name of a deprecated attribute will be displayed in square brakcets, like this `[<attribute name>]`, together with its type and the name of the attribute that replaces it.

