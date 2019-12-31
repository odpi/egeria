<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Discovery Service Connectors

The discovery service connectors provide the implementation of the discovery services
provided by Egeria.

Discovery services are responsible for analysing the content of assets and creating
descriptions of its content (called Annotations).  The annotations from a top-level discovery service
execution are grouped together and linked from the asset as a discovery analysis report.
They run in the [discovery server]()

Once the discovery service has completed its analysis, an event is sent to the stewardship server to process the
results.

The interfaces used by a discovery

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.