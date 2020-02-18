<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Discovery Service Connectors

The discovery service connectors contain the implementation of the
[discovery services](../../../frameworks/open-discovery-framework/docs/discovery-service.md)
provided by Egeria.

* **Sequential Discovery Pipeline** - Runs nested discovery services
  in a sequence.  [More information on discovery pipelines](../../../frameworks/open-discovery-framework/docs/discovery-pipeline.md).
  
* **DuplicateSuspectDiscoveryService** - Builds a list of all of the assets that
  appear to be duplicates of the requested asset.

## Further information

Discovery services are responsible for analysing the content of assets and creating
descriptions of its content (called [Annotations](../../../frameworks/open-discovery-framework/docs/discovery-annotation.md)).  The annotations from a top-level discovery service
execution are grouped together and linked from the asset as a discovery analysis report.
They run in the [discovery server](../../../frameworks/open-discovery-framework/docs/discovery-server.md).

Once the discovery service has completed its analysis,
an event is sent to the [stewardship server](../../../frameworks/governance-action-framework/docs/stewardship-server.md)
to process the results.

The interfaces used by a discovery service are defined in
the [Open Discovery Framework (ODF)](../../../frameworks/open-discovery-framework)
along with a guide on how to write a discovery service.

Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.