<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Connector - part of the [Open Connector Framework (OCF)](../README.md)

A connector is a java client object that provides applications with access to
an [asset](../../../../open-metadata-publication/website/assets/README.md),
along with its related metadata.

The OCF connector provides two APIs.
* **Asset Content**: the first API provides access to the Asset's contents.
This API is crafted to provide the most natural interface to the Asset's contents. 
Therefore the Asset Content API is typically different for each type of connector.
* **Asset Metadata**: the second API is the same for all connectors.
It has a single method called getConnectedAssetProperties().  This method is abstract
and is implemented by specific metadata providers.
The [Connected Asset Open Metadata Access Service (OMAS)](../../../access-services/connected-asset/README.md)
provides the implementation of getConnectedAssetProperties() for open metadata repositories.

The Asset Metadata API provides applications and tools with a simple mechanism to make use of metadata as they process a data resource.  This is particularly useful for data science tools where the metadata can help guide the end user in the use of the data resource.


OCF connectors are not limited to representing Assets as they are physically implemented.
An OCF connector can represent a simplified logical (virtual) asset, such as a data set, that is designed for the needs of a specific application or tool.
This type of connector delegates the requests it receives to one or more physical data resources.  It is called a virtual connector.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.



