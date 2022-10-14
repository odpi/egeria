<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


![Released](../../../../../../images/egeria-content-status-in-development.png#pagewidth)

# OMRS caching repository connector

The OMRS caching Repository Connector provides a simple repository
implementation that caches metadata in an embedded repository connector. This cached metadata can be retieved
using the standard retrieval OMRS APIs.
It is used as part of a Repository proxy pattern, where the event mapper polls for 
3rd party technology content and writes it to this connector using OMRS. The OMRS calls are delegated
to the embedded Egeria repository. 


----
Return to the [open-metadata-collection-store-connectors](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
