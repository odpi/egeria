<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Governance Server Strategy

[Governance Servers](../../../open-metadata-implementation/admin-services/docs/concepts/governance-server-types.md)
provide the runtime for connectors that automate metadata exchange and governance.  Our initial approach
was to build a specialized governance server type for each type of integration technology or governance
capability we were working with.  Figure 1 shows the types of governance servers in the
[2.0 release](../../../release-notes/release-notes-2-0.md).

![Figure 1](types-of-omag-servers-2-0.png#pagewidth)
> **Figure 1:** Governance servers in Release 2.0

Looking at the use cases in our roadmap we are heading for at least one governance server for each
[Open Metadata Access Service (OMAS)](../../../open-metadata-implementation/access-services).

![Figure 2](types-of-omag-servers-trajectory.png#pagewidth)
> **Figure 2:** Likely growth in the number of governance servers

Each governance server requires an extension to the OMAG Server's 
[Configuration Document](../../../open-metadata-implementation/admin-services/docs/concepts/configuration-document.md)
and administration services.  In addition, there is a lot if similarity in the code for each type of
governance server.  The potential growth in the types of governance servers suggests that the
current approach is not optimal.  So we have embarked on a restructuring effort to create two new types of governance
server that support pluggable services.  This means we can add new governance services without needing
to add a new type of server.

Figure 3 shows the first new governance server, called the
[Integration Daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md).
It is responsible for hosting 
[integration connectors](../../../open-metadata-implementation/governance-servers/integration-daemon-services/docs/integration-connector.md)
that are exchanging metadata with third party technology.
The integration daemon supports a set of [Open Metadata Integration Services (OMIS)](../../../open-metadata-implementation/integration-services)
that offer appropriate interfaces to the integration connectors.

The integration daemon was added in [release 2.3](../../../release-notes/release-notes-2-3.md) and new
integration services are in progress.  This server will replace the **Data Platform Server** and **Security Sync Server**
as well as support many of the new use cases.

![Figure 3](types-of-omag-servers-2-3.png#pagewidth)
> **Figure 3:** Stage one restructuring

Once the integration daemon is in place, then it is the turn of the
[Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md) OMAG Server.
The engine host will run governance engines that address specific aspects of governance of the open metadata ecosystem
and the digital landscape it describes.  
There is an [Open Metadata Engine Service (OMES)](../../../open-metadata-implementation/engine-services)
to support each type of engine.


![Figure 4](types-of-omag-servers-2-6.png#pagewidth)
> **Figure 4:** Resulting governance servers

The engine host server was added in [Release 2.6](../../../release-notes/release-notes-2-6.md)
and the Discovery Server function was moved to the
[Asset Analysis OMES](../../../open-metadata-implementation/engine-services).
The next step is to build out the
[Governance Action Framework (GAF)](../../../open-metadata-implementation/frameworks/governance-action-framework)
and create engine services to support the different types of engine it specifies.

----
* Return to [Developer Platform Functional Detail](developer-platform-functional-detail.md)
* Return to [Egeria Roadmap](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.