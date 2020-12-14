<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Security Officer Server

The Security Officer Server is a deprecated governance server.  The [Engine Host OMAG Server](engine-host.md) provides the
runtime for the
[Metadata Watchdog Open Metadata Engine Service (OMES)](../../../engine-services/metadata-watchdog)
that hosts the Security Officer Server function.

The [Integration Daemon OMAG Server](integration-daemon.md) provides the 
runtime for the
[Security Integrator Open Metadata Integration Service (OMIS)](../../../integration-services/security-integrator)
that listens for changes to the
[SecurityTag](../../../../open-metadata-publication/website/open-metadata-types/0423-Security-Tags.md) classifications
set up by the Security Officer Server and pushes them to a security enforcement point such as Apache Ranger.


----
* Go to [Governance Servers](governance-server-types.md).
* Go to [Admin Guide](../user).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.