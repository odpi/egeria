<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Governance Server Services

The open metadata governance server services provide the primary function for 
Egeria's [governance servers](https://egeria-project.org/concepts/governance-server).
They add services that either help to integrate third party technology, or
make use of open metadata to create new services.

More information about Governance Servers in general can be found in the [Administration Guide](https://egeria-project.org/guides/admin/servers).
However, each of the services below provide the principle service
for a particular type of governance server.

* **[data-engine-proxy-services](data-engine-proxy-services)** - bridge between data engines and the
    [Data Engine OMAS](https://egeria-project.org/services/omas/data-engine/overview).

* **[integration-daemon-services](integration-daemon-services)** - metadata exchange with third party tools.

* **[engine-host-services](engine-host-services)** - running governance engines.

* **[open-lineage-services](open-lineage-services)** - provides a historic reporting warehouse for lineage.


----
Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.