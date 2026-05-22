<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Asset Maker OMVS

The Asset Maker Open Metadata View Service (OMVS) provides APIs for user interfaces
that support the creation and maintenance of digital assets and their related
properties, such as software capabilities, deployment relationships, and governance actions.

## Key Features

- **Asset Management**: Create, update, and delete different types of assets. Support for creating assets from templates.
- **Software Capability Management**: Define and maintain software capabilities, representing software components, services, or engines.
- **IT Asset Deployment**: Manage the deployment of software capabilities and assets to infrastructure, including linking and detaching implementation details.
- **Data Asset Management**: Organize data assets into data sets and manage their content relationships.
- **Governance Action Management**: Create and track governance actions, assign them to actors, and manage their targets.
- **Catalog Target Management**: Manage the association between integration connectors and the metadata elements they are responsible for cataloguing.
- **Search and Exploration**: Find assets, processes, and software capabilities using search strings and filters.

## Further information

- [Asset Maker OMVS Overview](https://egeria-project.org/services/omvs/asset-maker/overview/)
- [Asset Concept](https://egeria-project.org/concepts/asset/)
- [Software Capability Concept](https://egeria-project.org/concepts/software-capability/)
- [Governance Action Concept](https://egeria-project.org/concepts/governance-action/)

----
Sample REST API requests:
- [Egeria-api-asset-maker.http](Egeria-api-asset-maker.http)
- [Egeria-jacquard-actions.http](Egeria-jacquard-actions.http)
- [Egeria-metadata-collections.http](Egeria-metadata-collections.http)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.