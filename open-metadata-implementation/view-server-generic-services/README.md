<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# View Server Generic Services

This module contains the services that are available in all view servers.  They allow the view server URL marker of an active view service to be passed.  There is a default view service name fore each of these services.

* **[Asset Maker](asset-maker)** - create and maintain assets.

  The Asset Maker OMVS provides edit and query capabilities for tools and applications to support an asset curation function.

* **[Collection Manager](collection-manager)** - manage collections of assets and other elements.

  The Collection Manager OMVS provides the REST APIs to create collections of asset and other elements such as glossary terms or policies.  The collections can be nested (like folders in a file system) and they can be classified to describe the type of collection.  For example, one type of classification for a collection is a *DigitalProduct* that allows the collections of assets and other related material to describe data and or services for a specific consumer or purpose.

* **[Feedback Manager](feedback-manager)** - add comments and other feedback to elements.

  The Feedback Manager OMVS is for user interfaces supporting feedback from users.  A registered user is able to add comments, informal tags, notes, reviews and likes to elements such as assets or glossary terms.
* 
* **[Governance Officer](governance-officer)** - maintain governance definitions used to define any governance domain.

  The Governance Officer OMVS is for user interfaces supporting the creation and editing of a new governance domain.

* **[Metadata Explorer](metadata-explorer)** - provides generic search, query and retrieval operations for open metadata.

  The Metadata Explorer OMVS is for user interfaces supporting the search, query and retrieval of open metadata.  It is an advanced API for users that understand the [Open Metadata Types](https://egeria-project.org/types/).

* **[Metadata Expert](metadata-expert)** - provides generic maintenance operations for open metadata.

  The Metadata Expert OMVS is for user interfaces supporting the maintenance of open metadata.  It is an advanced API for users that understand the [Open Metadata Types](https://egeria-project.org/types/).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.