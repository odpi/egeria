<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


## Asset Consumer OMAS User Guide

The Asset Consumer OMAS is designed for use by an application that is accessing data sources and services through
[connectors](../../../../frameworks/open-connector-framework/docs/concepts/connector.md).
These data sources and services are called [Assets](../../../docs/concepts/assets),
hence the name of this OMAS is **Asset Consumer**.

Typically the first action to take is to
[create the connector](../scenarios/creating-a-connector.md) to 
get [access to the asset content and its properties](../scenarios/working-with-connectors.md).
Connectors are created from
[Connection](../../../../frameworks/open-connector-framework/docs/concepts/connection.md)
objects.
Connection objects can be created by the calling application, or stored
in one of the open metadata repositories that are accessible to the Asset Consumer OMAS.

Alternatively, if the consumer only needs access to the asset's properties, they can use the
Asset Consumer OMAS to
[locate the identifier of the asset](../scenarios/locating-the-connected-asset.md)
and then [retrieve the asset properties](../scenarios/retrieving-asset-properties.md).

Within the asset properties are links to glossary terms.
It is possible to
[look up the full description of a term](../scenarios/looking-up-meanings-of-terms.md)
to further understand the asset.

There are also capabilities to 
[log messages about the asset](../scenarios/logging-messages-about-an-asset.md),
[add feedback to the asset](../scenarios/adding-feedback-to-an-asset.md)
in terms of likes, star ratings, reviews and comments,
and [add tags to the asset](../scenarios/tagging-an-asset.md).

## Interface choices

The Asset Consumer OMAS offers the following types of interface:

* [Java client](../../asset-consumer-client/docs/user/java-client), 
* [REST API](../../asset-consumer-server/docs/user) and 
* [Out Topic Events](../../asset-consumer-client/docs/user/java-events) for receiving events about assets.

Connectors are only available through the Java client.

## Configuration

Details of how to configure the Asset Consumer OMAS can be found [here](../../asset-consumer-server/docs/configuration)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.