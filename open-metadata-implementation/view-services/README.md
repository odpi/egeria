<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  

# Open Metadata View Services (OMVS)

The Open Metadata View Services (OMVS) provide task oriented, domain-specific services for user interfaces to integrate with open metadata.  They provide REST APIs.

The view services are as follows:

* **[Action Author](action-author)** - define governance actions.

  The Action Author OMVS is used to create and maintain governance actions: governance action types and governance action processes.

* **[Asset Catalog](asset-catalog)** - search for assets.

  The Asset Catalog OMVS provides search and query capabilities for tools and applications to support an asset catalog function. It supports search requests for assets with specific characteristics and returns summaries of the matching assets, plus methods to allow drill-down into the details of a specific asset to related metadata.

* **[Automated Curation](automated-curation)** - run governance actions.

  The Automated Curation OMVS provides search and query capabilities for technology types and then the ability to run associated governance actions.

* **[Classification Manager](classification-manager) - manage classifications on open metadata.

  The Classification Manager OMVS enables the caller to add classifications and governance relationships to open metadata elements.

* **[Collection Manager](collection-manager)** - manage collections of assets and other elements.

  The Collection Manager OMVS provides the REST APIs to create collections of asset and other elements such as glossary terms or policies.  The collections can be nested (like folders in a file system) and they can be classified to describe the type of collection.  For example, one type of classification for a collection is a *DigitalProduct* that allows the collections of assets and other related material to describe data and or services for a specific consumer or purpose.

* **[Data Designer](data-designer)** - create schema definitions to describe the structure of data.

  The Data Designer OMVS is a REST API designed to support user interfaces (UIs) for building schemas for new data assets.

* **[Data Discovery](data-discovery)** - define and search for new data resources.

  The Data Discovery OMVS is a REST API designed to support user interfaces (UIs) for locating the data needed for a project.

* **[Data Officer](data-officer)** - manage the governance of data.

  The Data Officer OMVS is a REST API designed to support user interfaces (UIs) for supporting a Data Officer as they lead the data governance program.  This builds on the capabilities of the Governance Officer OMVS.

* **[Devops Pipeline](devops-pipeline)** - maintain the metadata about the assets managed by a devops pipeline

  The Devops Pipeline OMVS is a REST API designed to support user interfaces (UIs) for supporting a devops engineer to maintain the metadata about the changing digital resources being deployed through devops pipelines.

* **[Glossary Browser](glossary-browser)** - search and view the contents of specific glossaries and their links to governance definitions and assets.

  The Glossary Browser OMVS is for user interfaces that wish to provide search facilities within a single glossary (or glossaries) along with the ability to browse the structure of the glossary and view an element within a glossary along with connected governance classifications, governance definitions, user feedback and attached assets.

* **[Glossary Manager](glossary-manager)** - develop new glossary terms and categories in a controlled workflow process.

  The Glossary Manager OMVS is for user interfaces supporting the creation and editing of glossary content, such as glossary terms and categories, in a controlled workflow process.  This means that as terms and categories are created, updated and deleted, these changes are invisible to the general user until they are approved.

* **[Governance Officer](governance-officer)** - maintain governance definitions used to define any governance domain.

  The Governance Officer OMVS is for user interfaces supporting the creation and editing of a new governance domain.

* **[My Profile](my-profile)** - locate, retrieve and update information relating to the calling user's profile.

  The My Profile OMVS is for user interfaces supporting a personalized experience.  A registered user is able to maintain information about themselves and their network as well as set up and use personalized collections of metadata elements.

* **[Notification Manager](notification-manager)** - manages the definitions of notifications.

  The Notification Manager OMVS is for user interfaces supporting a personalized notification service.  This includes the definition of the trigger for the notification, the style of notification and the recipient.

* **[People Organizer](people-organizer)** - describe teams, roles and organizational structure.

  The People Organizer OMVS provides the REST APIs used to maintain information about an organization.  This includes the definitions of teams, roles and organization structures.

* **[Project Manager](project-manager)** - manage projects and related elements.

  The Project Manager OMVS provides the REST APIs to create projects and related elements such as project manager roles.  The projects can be nested and they can be classified to describe the type of project.  For example, one type of classification for a project is *Campaign*, another is *Task*.

* **[Privacy Officer](data-officer)** - manage the governance of privacy.

  The Privacy Officer OMVS is a REST API designed to support user interfaces (UIs) for supporting a Privacy Officer as they lead the data privacy governance program.  This builds on the capabilities of the Governance Officer OMVS.

* **[Reference Data](reference-data)** - manage reference data.

  The Reference Data OMVS is a REST API designed to support user interfaces (UIs) that maintain reference data values and their mappings.

* **[Security Officer](security-officer)** - manage the governance of security.

  The Security Officer OMVS is a REST API designed to support user interfaces (UIs) for supporting a Security Officer as they lead the security governance program.  This builds on the capabilities of the Governance Officer OMVS.

* **[Solution Architect](solution-architect)** - maintain the metadata about information supply chains and solution components.

  The Solution Architect OMVS is a REST API designed to support user interfaces (UIs) relating to the definition and display of solution blueprints and their supporting solution components along with the relevant information supply chains.

* **[Template Manager](template-manager)** - locate and maintain templates.

  The Template Manager OMVS is a REST API designed to support user interfaces (UIs) that support the management of templates that are used when creating new metadata.

* **[Time Keeper](time-keeper)** - locate and maintain context events.

  The Time Keeper OMVS is a REST API designed to support user interfaces (UIs) that support the management of context events that used to mark significant events that affect the performance of organizations.

* **[Valid Metadata](reference-data)** - manage valid values for metadata.

  The Valid Metadata OMVS is a REST API designed to support user interfaces (UIs) that query and/or maintain list of valid values for open metadata attributes.

----
Return to [open-metadata-implementation](..).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
