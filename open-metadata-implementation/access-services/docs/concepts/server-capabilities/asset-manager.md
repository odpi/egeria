<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Asset Manager

An **asset manager**
is typically a service that provides a catalog of [assets](../assets).  It is responsible
for maintaining details of the assets including their characteristics,
ownership, assessments and governance requirements.

Often an asset manager is specialized for particular types of assets.
For example, you may see a **data catalog** cataloging information about data sets and data stores
for a data lake.
A Configuration Management Database (CMDB) is an asset manager of infrastructure assets.

## Open metadata support for asset managers

An asset manager may integrate with the open metadata ecosystem using one of three methods:

 * as a
[cohort member](../../../../admin-services/docs/concepts/cohort-member.md)
through the
[Open Metadata Repository Services (OMRS) connectors](../../../../repository-services/docs/component-descriptions/connectors/repository-connector.md)
or 

* using the [Catalog Integrator Open Metadata Integration Service (OMIS)](../../../../integration-services/catalog-integrator)
through an [integration connector](../../../../governance-servers/integration-daemon-services/docs/integration-connector.md) or

* Through direct calls to the [Asset Manager Open Metadata Access Service (OMAS)](../../../asset-manager).

The [Metadata Server Exchange](../../../../../open-metadata-publication/website/solutions/metadata-server-exchange)
solution provides more information on these options.

----

* Return to [Software Server Capabilities](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.