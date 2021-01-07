<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Metadata Discovery

Metadata discovery is an automated process that extracts metadata about an
[asset](../../../open-metadata-implementation/access-services/docs/concepts/assets).
This metadata may be:
* embedded within the asset 
  * (for example a digital photograph has embedded metadata), or
* managed by the platform that is hosting the asset
  * (for example, a relational database platform maintains
schema information about the data store in its databases) or
* determined by analysing the content of the asset
  * (for example a metadata
discovery tool may analyse the data content to determine the types and range of values it contains and,
maybe determine a quality score for the data).

Some metadata discovery may occur when the asset is first cataloged.
For example, the schema of an asset may be stored through the
[Data Manager OMAS](../../../open-metadata-implementation/access-services/data-manager) API.
This schema may have been automatically extracted by a
[metadata extractor connector](../../../open-metadata-implementation/adapters/open-connectors/governance-daemon-connectors/data-platform-connectors)
hosted in Egeria's [Data Platform Server](../../../open-metadata-implementation/governance-servers/data-platform-services) which
then calls the Data Manager OMAS to store the metadata.

Egeria also supports more advanced metadata discovery.

The [Open Discovery Framework (ODF)](../../../open-metadata-implementation/frameworks/open-discovery-framework)
has open APIs that define how automated discovery services
can be managed and interact with an open metadata repository.
The metadata repository interfaces provide the metadata discovery service
with:
 * the ability to manage discovery configuration,
 * a search function to locate assets in the metadata repository,
 * access all of the metadata known about each asset including its connector and
 * the ability to record the results of its analysis.

This metadata repository interface for metadata discovery tools is
implemented by the
[Discovery Engine OMAS](../../../open-metadata-implementation/access-services/discovery-engine).

Egeria is also able to host automated metadata discovery services
that implement the ODF interfaces in its
[Asset Analysis OMES](../../../open-metadata-implementation/engine-services/asset-analysis).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.