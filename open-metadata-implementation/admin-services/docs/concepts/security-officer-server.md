<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Security Officer Server

The Security Officer Server is a [Governance Server](governance-server-types.md)
that manages the setting of security classifications on data.
These are then interpreted by security enforcement engines
such as Apache Ranger and Palisade.

The [security Sync Server](security-sync-server.md) provides the
integration daemon that takes the 
[SecurityTag](../../../../open-metadata-publication/website/open-metadata-types/0423-Security-Tags.md) classifications
set up by the Security Officer Server and pushes them to Apache Ranger.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.