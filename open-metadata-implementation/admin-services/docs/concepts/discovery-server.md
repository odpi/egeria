<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Discovery Server

The discovery server is an [OMAG Server](omag-server.md) that is able to host one or more
discovery engines.

A discovery engine is the execution environment for special types of connectors called
**discovery services**.  Each discovery service implements a specific type of analysis.  This analysis may be looking
at the metadata about the assets stored in the open metadata repositories, or it may look
into the content of the physical asset itself.  The result of its analysis is stored in an
open metadata repository and linked off of the asset's description.

The discovery engine defines a set of discovery services.  Its definition is stored in
an open metadata repository and maintained through the
[Discovery Engine OMAS's configuration API](../../../access-services/discovery-engine).

Discovery servers are deployed close to the physical assets they are analysing.
They connect to the [Discovery Engine OMAS](../../../access-services/discovery-engine)
running in a metadata server
to provide metadata about assets and to
store the results of the discovery service's analysis.

Figure 1 shows the discovery server connected to other
OMAG Servers.  The discovery server has a REST API to request
that specific types of analysis are 

![Figure 1](discovery-server.png)
> **Figure 1:** How the discovery server connects to other OMAG Servers

Multiple discovery servers can run the same discovery engines.
They can also connect to the same metadata server.


## Configuring the Discovery Server

The configuration for the discovery server is as follows:

* [Setting basic properties for an OMAG server](../user/configuring-omag-server-basic-properties.md)
* [Configuring the audit log destinations](../user/configuring-the-audit-log.md)
* [Configuring the server security connector](../user/configuring-the-server-security-connector.md)
* [Configuring the discovery engine services](../user/configuring-the-discovery-engine-services.md)

## Further information

 * The [Discovery Engine OMAS](../../../access-services/discovery-engine)
describes how to configure discovery engines and discovery services for your
discovery server.

 * The [Discovery Engine Service](../../../governance-servers/discovery-engine-services)
 supports REST APIs for requesting metadata discovery services on specific
 assets or groups of assets.

 * The [Open Discovery Framework (ODF)](../../../frameworks/open-discovery-framework)
provides a definition of the interfaces for a discovery service.  If a discovery
service follows the specification of the open discovery framework in its implementation
then it can be run in the Egeria Discovery Server.  Link to the ODF if you are
interested in writing new discovery services.

----
* Return to [Engine Host](engine-host.md)
* Return to [Admin Guide](../user/configuring-an-omag-server.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.