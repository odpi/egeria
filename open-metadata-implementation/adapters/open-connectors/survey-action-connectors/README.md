<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

![TechPreview](../../../../images/egeria-content-status-tech-preview.png#pagewidth)

# Survey Action Service Connectors

The survey action service connectors contain the implementation of the
[survey action services](https://egeria-project.org/concepts/survey-action-service)
provided by Egeria.

* **Sequential Survey Pipeline** - Runs nested survey action services in a sequence.
  
* **CSV Survey Service** - Extracts the column names from the first line of the file, counts up the number of records in the file
  and extracts its last modified time.

* **Apache Atlas Survey Service** is a survey action service that gathers statistics about the
  Apache Atlas server and stores it in a Survey Report attached to the SoftwareServer entity
  that represents the Apache Atlas server.


Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.