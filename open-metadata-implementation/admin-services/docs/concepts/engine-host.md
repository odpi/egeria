<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Engine Host

An **engine host** is an [OMAG Server](omag-server.md) that hosts one or more of a particular type of governance engine.
These governance engines are using metadata to drive particular processing to aid in the governance of metadata
and the assets it describes.

The governance engine is paired with a specific access service and they may well interact with third party technology.
They typically have an external API to control and query the work of the engine
and may well be called by a view server as part of the  support for a user interface.

![Engine Host](engine-host.png)

In Egeria today there are two types of engine hosts:
* [Discovery Server](discovery-server.md) that is running automatic metadata discovery requests.
* [Stewardship Server](stewardship-server.md) that is managing the resolution of issues reported in the open
metadata ecosystem or the assets it supports.

----
Return to the [Governance Server Types](governance-server-types.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.