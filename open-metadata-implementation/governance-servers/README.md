<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Governance Servers

The open metadata governance servers pull combinations of the Egeria services together to
support different integration patterns.

More information about Governance Servers in general can be found under [docs](docs).

* **[data-engine-proxy-services](data-engine-proxy-services)** - bridge between data engines and the
    [Data Engine OMAS](../access-services/data-engine).

* **[data-platform-services](data-platform-services)** - bridge between data platforms and the
    [Data Platform OMAS](../access-services/data-platform).

* **[discovery-engine-services](discovery-engine-services)** - the discovery engine provides the server
    capability to run [Open Discovery Services](../frameworks/open-discovery-framework/docs/discovery-service.md)
    on demand.

* **[open-lineage-services](open-lineage-services)** - provides a historic reporting warehouse for lineage.

* **[security-officer-services](security-officer-services)** - provide operational support for the
    security policies defined by the security officer.

* **[security-sync-services](security-sync-services)** - keep security enforcement engines supplied with the
    latest metadata.

* **[stewardship-services](stewardship-services)** - manage the triage and remediation of
    **Request for Action** annotations made by the discovery services or other activity on the Assets.

* **[virtualization-services](virtualization-services)** - Propagate the Out Topic of Information View OMAS,
    build queries to data virtualization solutions and generate corresponding In Topic for IV OMAS.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.