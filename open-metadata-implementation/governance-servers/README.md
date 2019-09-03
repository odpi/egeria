<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Governance Servers

The open metadata governance servers pull combinations of the Egeria services together to
support different integration patterns.

* **[server-chassis](server-chassis)** - the server chassis provides the webserver and basic security services
to support the [Open Metadata and Governance (OMAG) Server Platform](admin-services/docs/concepts/omag-server-platform.md).

* **[admin-services](admin-services)** - the admin services support:
  * the maintenance of [configuration documents](admin-services/docs/concepts/configuration-document.md) used to
  define the behavior of [OMAG servers](admin-services/docs/concepts/omag-server.md) running on the OMAG Server Platform.
  * the operation of OMAG servers within an OMAG server platform.
  * the [server platform origin service](admin-services/docs/concepts/server-platform-origin-service.md).

* **[discovery-engine](discovery-engine-services)** - the discovery engine provides the server
capability to run [Open Discovery Services](../frameworks/open-discovery-framework/docs/discovery-service.md)
on demand.

* **[security-sync-services](security-sync-services)** - keep security enforcement engines supplied with the
latest metadata.

* **[stewardship-services](stewardship-services)** - manage the triage and remediation of Request for
Action annotations.  Each Request for Action describes a potential issue in the data
landscape.  The stewardship services can initiate automatic remediation or
let stewards decide which Request for Actions should be actioned.
When a Request for Action is actioned, the stewardship services initiate the
requested remediation.

* **[virtualization-services](virtualization-services)** - Propagate the Out Topic of Information View OMAS, build queries to data virtualization solutions and generate corresponding In Topic for IV OMAS.


* **[open-lineage-services](open-lineage-services)** - provides a historic reporting warehouse for lineage.

  The Open Lineage Services provides a historic reporting warehouse for lineage. It listens to events that are send out 
by the Asset Lineage OMAS, and stores lineage data in a Janusgraph database. This lineage can then be queried through
the Open Lineage Services client and by its REST API, for example by a lineage GUI. While the data format of events sent
out by the Asset Lineage Omas are in the Open Metadata format, Open Lineage services store lineage data in a very basic
data format in order to optimize query performance.
In essence there are 3 kinds of graphs:
  * ***buffer graphName*** -  used to store current lineage in the Open Metadata types
  * ***current graphName*** - stores current lineage in graphName database in the format optimimized for lineage
  * ***historic graphName*** -  stores historic lineage in graphName database in the format optimimized for lineage

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.