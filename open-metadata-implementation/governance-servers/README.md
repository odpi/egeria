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


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.