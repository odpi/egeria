<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# 0205 Connection Linkage

The purpose of a connector is to access assets.
In order for the connector to provide metadata about the asset it
is accessing, there is a relationship between the connection and
the asset.
Notice the connection can only be associated with one asset - however assets may host smaller assets within them.
In addition, some connectors are virtual connectors - by that we mean they implement an abstract to a business level asset
and internally use one of more technical connectors as part of their implementation.
The metadata repository can reflect these connection relationships using a **VirtualConnection**.

![UML](0205-Connection-Linkage.png)