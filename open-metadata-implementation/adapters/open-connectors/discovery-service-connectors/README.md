<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

![TechPreview](../../../../open-metadata-publication/website/images/egeria-content-status-tech-preview.png#pagewidth)

# Discovery Service Connectors

The discovery service connectors contain the implementation of the
[discovery services](../../../frameworks/open-discovery-framework/docs/discovery-service.md)
provided by Egeria.

* **Sequential Discovery Pipeline** - Runs nested discovery services
  in a sequence.  [More information on discovery pipelines](../../../frameworks/open-discovery-framework/docs/discovery-pipeline.md).
  
* **CSVDiscoveryService** - Extracts the column names from the first line of the file, counts up the number of records in the file
  and extracts its last modified time..

## Further information

Discovery services are responsible for analysing the content of assets and creating
descriptions of its content (called [Annotations](../../../frameworks/open-discovery-framework/docs/discovery-annotation.md)).  
The annotations from a top-level discovery service
execution are grouped together and linked from the asset as a discovery analysis report.
They run in the [Asset Analysis OMES](../../../engine-services/asset-analysis).

Once the discovery service has completed its analysis,
an event is sent by the [Governance Engine OMAS](../../../access-services/governance-engine) to any listening
[Engine Host OMAG Servers](../../../admin-services/docs/concepts/engine-host.md) running 
the [Governance Action OMES](../../../engine-services/governance-action) to process the results.

The interfaces used by a discovery service are defined in
the [Open Discovery Framework (ODF)](../../../frameworks/open-discovery-framework)
along with a guide on how to write a discovery service.

Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.