<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Data Discovery OMVS

The Data Discovery Open Metadata View Service (OMVS) provides a REST API to support user interfaces (UIs)
relating to the discovery and analysis of digital resources (assets) and their data.
It enables external survey and analysis engines (such as data quality analysers) to load their findings 
into open metadata.

The Data Discovery OMVS supports the following key concepts:

* **Survey Reports**: Capture the analysis of IT resources and data from a specific run of a survey engine.
* **Annotations**: Individual findings within a survey report. These can be linked to the elements they describe, 
  associated with previous runs for trend analysis, or used to trigger governance actions.

## Further information

* [Data Discovery OMVS Overview](https://egeria-project.org/services/omvs/data-discovery/overview/)
* [Survey Report Concept](https://egeria-project.org/concepts/survey-report/)
* [Annotation Concept](https://egeria-project.org/concepts/annotation/)

Sample requests for the REST API can be found in `Egeria-api-data-discovery.http`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.