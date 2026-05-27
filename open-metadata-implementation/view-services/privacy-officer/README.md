<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Privacy Officer OMVS

The Privacy Officer OMVS is a REST API designed to support user interfaces (UIs) for supporting a Privacy Officer as they lead the data privacy governance program. This builds on the capabilities of the Governance Officer API.

## Key Features

The Privacy Officer API provides specialized capabilities for managing the descriptions and actions associated with data processing and privacy governance:

* **Data Processing Purpose Management**: Managing the definitions of purposes for which data is processed.
* **Permitted Processing Management**: Linking and detaching data processing purposes to data processing descriptions to define what processing is allowed for a given purpose.
* **Data Processing Action Management**: Managing the actions taken as part of data processing.
* **Data Processing Target Management**: Linking and detaching data processing actions to the specific metadata elements (targets) that they affect.

## Further information

* [Privacy Officer API Overview](https://egeria-project.org/services/omvs/privacy-officer/overview/)
* [Data Processing Description Concept](https://egeria-project.org/concepts/data-processing-description/)

----
Sample REST API requests can be found in [Egeria-api-privacy-officer.http](Egeria-api-privacy-officer.http).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.