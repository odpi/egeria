<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Metadata Discovery

Metadata discovery is an automated process that extracts metadata about an
[asset](../../../open-metadata-implementation/access-services/docs/concepts/assets).
This metadata may be embedded within the asset, or managed by the platform that is hosting it,
or determined by analysing the content of the asset.

Some metadata discovery may occur when the asset is first catalogued.
For example, the schema of an asset may be captured through the
[Data Platform OMAS](../../../open-metadata-implementation/access-services/data-platform) API.
This may be automatically extracted by the metadata extractor connector hosted in the 
data platform server which then calls the Data Platform OMAS to store the metadata.

Egeria also supports more advanced metadata discovery.

It supports an [Open Discovery Framework (ODF)](../../../open-metadata-implementation/frameworks/open-discovery-framework)
that defines APIs that automated discovery services implement.  These interfaces provide these
services with the existing information that is known about the asset, a connector to
access the content of the asset, and the means to record the results of its analysis.

The discovery services run in a discovery engine



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.