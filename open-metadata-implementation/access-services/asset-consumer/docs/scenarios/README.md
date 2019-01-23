<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer OMAS Scenarios

The Asset Consumer OMAS is designed for use by an application that is accessing data sources and services through
[connectors](../../../../frameworks/open-connector-framework/docs/concepts/connector.md).
These data sources and services are called [Assets](../../../docs/concepts/assets),
hence the name of this OMAS is **Asset Consumer**.

Typically the first action to take is to
[create the connector](creating-a-connector.md) to 
get access to the asset and its properties.
Connectors are created from
[connection](../../../../frameworks/open-connector-framework/docs/concepts/connection.md)
objects.
Connection objects can be created by the calling application, or stored
in the metadata repository.

If the consumer only needs access to the asset's properties, they can use the
Asset Consumer OMAS to
[locate the identifier of the asset](locating-the-connected-asset.md)
and then [retrieve the asset properties](retrieving-asset-properties.md).

Within the asset properties are links to glossary terms.
It is possible to
[look up the full description of a term](looking-up-meanings-of-terms.md)
to further understand the asset.

There are also capabilities to 
[log messages about the asset](logging-messages-about-an-asset.md),
[add feedback to the asset](adding-feedback-to-an-asset.md)
in terms of likes, star ratings, reviews and comments,
and [add tags to the asset](tagging-an-asset.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.