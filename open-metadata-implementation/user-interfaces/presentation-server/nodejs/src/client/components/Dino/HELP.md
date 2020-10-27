## Dino User Interface

&nbsp;

The Dino interface provides a way to visualize Egeria platforms, servers, the services they are running and the cohorts they have joined. The information is presented both textually (on the left side) and diagrammatically (on the right side).

&nbsp;

![Dino Interface](image1)

&nbsp;

### Using the Dino Interface
When the Dino interface first loads, it sends a request to the Dino View Service to load the configured platform and server resources. The platforms are loaded into the `Accessible Platforms` selector at the top of the page. The servers are loaded into the `Direct Server Links` selector at the top of the page. 

&nbsp;

The listed platforms are the only ones the user will be able to interrogate, but from a platform they will be able to see the platform's registered services and query the platform to show the servers that are running (or that have run) on that platform. From there they can continue to explore. 

&nbsp;

The listed servers are included for convenience so that a user who wants to see a particular server does not need to know which platform it is running on. Instead they can use the `serverInstanceName` in the list of servers.

&nbsp;

Typically a user may start by selecting a platform from the `Accessible Platforms` selector. The platform will be loaded and will be made the focus resource; it will be displayed in the details area on the left side and represented by a platform icon in the diagram on the right side. The user can expand sections in the details and use the embedded buttons to query the (active or known) servers. Alternatively they can right click on the platform icon and use the platform pop-up menu to perform these operations.

&nbsp;

Clicking the `Get Active Servers` or `Get All Servers` buttons, or using the platform pop-up menu, will load the servers into the diagram. If a server is connected to a platform by a solid line this indicates that the server is active. A dotted line is used to indicate that the server is not currently active.

&nbsp;

![Topology Diagram](image2)

&nbsp;

To make a server the focus the user can click on it, or use the right click popup-menu. This results in the server details being loaded into the details area, containing server properties and expandable sections for additional detail. If the user expands the `Cohorts` section and expands a particular cohort, that cohort will be added to the diagram. If a cohort is connected to a server by a solid line this indicates that the server is actively connected to the cohort. A dotted line is used where the server is not currently active. Similarly for `Services` - if a service is expanded it will be added to the diagram.

&nbsp;

If the user expands the server's `Configuration` section, the Dino interface will send a request to the Dino View Service to load the server's configuration. This actually loads the stored configuration and (if the server is running) also the active configuration. The radio buttons can be used to display either the `Stored Config`, the `Active Config` or see if there are any  `Differences` between them. There are sections within the configuration details that can be expanded to see the configuration for access services, common services, governance services, view services, repository services, and the event bus. There is also a section that shows the configuration audit trail.

&nbsp;

### Diagram Controls
The diagram has two layout modes - 'time-based' and 'proximity-based'. The 'time-based' layout arranges objects vertically on the diagram with the newest toward the bottom. The 'proxmity-based' layout allows the graph of objects to organize itself based on connectivity. 

&nbsp;

If the 'pin dragged objbect' checkbox is checked, when the user drags an object it will be pinned where it is dropped. It can be dragged again if needed, but it will always stay where it is dropped. To release an individual object, press shift and click on the object. To allow objects to move around independently, uncheck the 'pin dragged objbect' option.

&nbsp;

### Undo and Clear
There are buttons to undo the most recent operation (`Undo`) or clear (`Clear`) the whole graph.
