<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Event Management Services

The event management services are responsible for receiving and sending events to and
from the cohort, and between the Cohort Services, Local Repository Services and
Enterprise Repository Services within the server.  It includes the following
components:

* **[OMRS Repository Event Manager](../component-descriptions/event-manager.md)** - Manages the distribution of repository events
(TypeDef and Instance Events) within the local server's OMRS components.
* **[OMRS Event Listener](../component-descriptions/event-listener.md)** - Receives Registry and Repository (TypeDef and Instance)
events from the OMRS Topic for a cohort.
* **[OMRS Event Publisher](../component-descriptions/event-publisher.md)** - Sends Registry and Repository (TypeDef and Instance)
events to the OMRS Topic.
This may be the OMRS Topic for a cohort, or the OMRS Topic used by the Open Metadata Access Services (OMAS).




----
* Return to [repository services subsystem descriptions](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


