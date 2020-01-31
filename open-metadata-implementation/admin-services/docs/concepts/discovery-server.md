<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Discovery Server

The discovery server is an [OMAG Server](omag-server.md) that is able to host one or more
discovery engines.

A discovery engine is the execution environment for special types of connectors called
**discovery services**.

Each discovery service implements a specific type of analysis.  This analysis may be looking
at the metadata about the assets stored in the open metadata repositories, or it may look
into the content of the physical asset itself.  The result of its analysis is stored in an
open metadata repository and linked off of the asset's description.

The discovery engine defines a set of discovery services.  Its definition is stored in
an open metadata repository and maintained through the
[Discovery Engine OMAS's configuration API](../../../access-services/discovery-engine).

Discovery servers are deployed close to the physical assets they are analysing.
They connect to the Discovery Engine OMAS running in a metadata server
to provide metadata about assets and to
store the results of the discovery service's analysis.

Multiple discovery servers can run the same discovery engines.
They can also connect to the same 



## Open Discovery Framework (ODF)

The [Open Discovery Framework (ODF)](../../../frameworks/open-discovery-framework)
provides a definition of the interfaces for a discovery service.  If a discovery
service follows the specification of the open discovery framework in its implementation
then it can be run in the Egeria Discovery Server.

## Implementation details



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.