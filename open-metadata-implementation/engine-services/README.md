<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


![In Development](../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Open Metadata Engine Services (OMES)

The engine services each provide the services that host a specific
type of governance engine.  These services are still in design.

## Background

The diagram below shows the existing hierarchy of OMAG Server Types
![OMAG Server Types](../admin-services/docs/concepts/types-of-omag-servers.png)

The Discovery Server and the Stewardship Server are classified as types of
*Engine Hosts*.  The Discovery Server hosts Discovery Engines and the
Stewardship server hosts Stewardship Engines.
There is a lot of duplicated code in these two servers that are
implemented today as separate [governance services](../governance-servers).

The plan is to create a more generic **Engine Host** governance server.
It will run one to many **engine-services**.  Each engine services will
host a specific type of engine.  The first two engine services will be
one for the discovery engines and another for the stewardship engines
(although the different types of stewardship engines may each be supported by
their own engine service.)

With this change it is easier for Egeria to host other types of governance engines
such as Palisade and Gaian.

----
Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.