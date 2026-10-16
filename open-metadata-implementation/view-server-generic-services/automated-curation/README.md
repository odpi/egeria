<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Automated Curation OMVS

The Automated Curation OMVS (Open Metadata View Service) enables the caller to manage the execution of
integration connectors and engine actions.

Key features of this service include:

* **Technology Type Management**: Browsing and searching for the technology types supported by the ecosystem's integration connectors and governance engines.
* **Governance and Engine Action Management**: Initiating, canceling, and monitoring governance actions and engine actions.
* **Governance Action Process Cancellation**: Cancelling every unfinished engine action of a running governance action process instance.
* **Templated Cataloguing**: Support for creating metadata elements using templates, ensuring consistency in the cataloguing process.
* **Client-Side Secret Management**: Saving and deleting secrets (such as credentials) used by client-side integration components.

The operations used by a governance engine to claim an engine action, update its status, report progress on its action targets and record its completion are in the [Governance Engine OMVS](../../view-services/governance-engine).  They were previously part of this service; to call them, replace the `automated-curation` URL marker with `governance-engine`.

## Further information

* [Automated Curation API Overview](https://egeria-project.org/services/omvs/automated-curation/overview/)
* [Integration Connector Concept](https://egeria-project.org/concepts/integration-connector/)
* [Governance Action Concept](https://egeria-project.org/concepts/governance-action/)
* [Integration Daemon Concept](https://egeria-project.org/concepts/integration-daemon/)

----
Sample REST API requests:
* [Egeria-api-automated-curation.http](Egeria-api-automated-curation.http)
* [Egeria-create-client-side-secrets.http](Egeria-create-client-side-secrets.http)
* [Egeria-test-catalog-processes.http](Egeria-test-catalog-processes.http)
* [Egeria-test-surveys.http](Egeria-test-surveys.http)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.