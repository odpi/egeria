<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Discovery Pipeline

A discovery pipeline is a specialized implementation of a [discovery service](discovery-service.md)
that runs a set of discovery services against a single asset.  The implementation of
the discovery pipeline determines the order that these discovery services are run.

The aim of the discovery pipeline is to enable a detailed picture of the properties
of an asset to be built up by the discovery services it calls.  Each discovery service
is able to access the results of the discovery services that have run before it.

The results created by a discovery pipeline are stored in [Annotations](discovery-annotation.md)..

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.