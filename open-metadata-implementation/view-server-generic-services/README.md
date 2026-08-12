<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# View Server Generic Services

This module contains the services that are available in all view servers.  They allow the view server URL marker of an active view service to be passed.  There is a default view service name fore each of these services.

* **[Actor Manager](actor-manager)** - manage the definitions of actors and their related properties.

  The Actor Manager OMVS provides APIs for user interfaces that manage the definitions of actors (people, teams,
  and IT systems) and their related properties, such as roles, identities, and skills.

* **[Asset Maker](asset-maker)** - create and maintain assets.

  The Asset Maker OMVS provides edit and query capabilities for tools and applications to support an asset curation function.

* **[Automated Curation](automated-curation)** - manage activation of governance actions.

  The automated Curation OMVS provides the REST APIs to manage governance actions such as surveys.

* **[Classification Explorer](classification-explorer)** - curate classifications and relationships for assets and other elements.

  The Classification Explorer OMVS provides the REST APIs to curate additional information about digital resources and related elements such as schemas. 

* **[Collection Manager](collection-manager)** - manage collections of assets and other elements.

  The Collection Manager OMVS provides the REST APIs to create collections of asset and other elements such as glossary terms or policies.  The collections can be nested (like folders in a file system) and they can be classified to describe the type of collection.  For example, one type of classification for a collection is a *DigitalProduct* that allows the collections of assets and other related material to describe data and or services for a specific consumer or purpose.

* **[Connection Maker](connection-maker)** - create and maintain connections for assets.

  The Connection Maker OMVS provides edit and query capabilities for tools and applications to support an asset curation function.

* **[External Links](external-links)** - manage the definitions of external references and external identifiers.

  The External Links OMVS enables the caller to manage the definitions of external references and external identifiers.

* **[Feedback Manager](feedback-manager)** - add comments and other feedback to elements.

  The Feedback Manager OMVS is for user interfaces supporting feedback from users.  A registered user is able to add comments, informal tags, notes, reviews and likes to elements such as assets or glossary terms.

* **[Governance Officer](governance-officer)** - maintain governance definitions used to define any governance domain.

  The Governance Officer OMVS is for user interfaces supporting the creation and editing of a new governance domain.

* **[Lineage Linker](lineage-linker)** - create and maintain lineage relationships.

  The Lineage Linker OMVS provides support for user interfaces that need to create and maintain lineage relationships.

* **[Metadata Expert](metadata-expert)** - provides generic maintenance operations for open metadata.

  The Metadata Expert OMVS is for user interfaces supporting the maintenance of open metadata.  It is an advanced API for users that understand the [Open Metadata Types](https://egeria-project.org/types/).

* **[Multi Language](multi-language)** - define and manage translations of open metadata content.

  The Multi Language OMVS enables the caller to define and manage translations of open metadata content, so that
  the same metadata elements can be presented in different natural languages.

* **[Schema Maker](schema-maker)** - create and maintain schema structures for assets.

  The Schema Maker OMVS provides edit and query capabilities for tools and applications to support an asset curation function.

* **[Time Keeper](time-keeper)** - locate and maintain context events.

  The Time Keeper OMVS is a REST API designed to support user interfaces (UIs) that support the management of context events that used to mark significant events that affect the performance of organizations.

* **[Valid Metadata](valid-metadata)** - manage valid values for metadata.

  The Valid Metadata OMVS is a REST API designed to support user interfaces (UIs) that query and/or maintain list of valid values for open metadata attributes.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.