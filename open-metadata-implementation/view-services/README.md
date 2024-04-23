<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  

# Open Metadata View Services (OMVS)

The Open Metadata View Services (OMVS) provide task oriented, domain-specific services for user interfaces to integrate with open metadata.  They provide REST APIs.

The view services are as follows:

* **[Asset Catalog](asset-catalog)** - search for assets.

  The Asset Catalog OMVS provides search and query capabilities for tools and applications to support an asset catalog function. It supports search requests for assets with specific characteristics and returns summaries of the matching assets, plus methods to allow drill-down into the details of a specific asset to related metadata.

* **[Automated Curation](automated-curation)** - run governance actions.

  The Automated Curation OMVS provides search and query capabilities for technology types and then the ability to run associated governance actions.

* **[Collection Manager](collection-manager)** - manage collections of assets and other elements.

  The Collection Manager OMVS provides the REST APIs to create collections of asset and other elements such as glossary terms or policies.  The collections can be nested (like folders in a file system) and they can be classified to describe the type of collection.  For example, one type of classification for a collection is a *DigitalProduct* that allows the collections of assets and other related material to describe data and or services for a specific consumer or purpose.

* **[Data Discovery](data-discovery)** - define data specification.

  The Data Discovery OMVS is a REST API designed to support user interfaces (UIs) for documenting specifications that describe the data needed for a project.  These are known as DataSpecs and they can be used to specify schemas for data pipelines as well as saved searches.

* **[Feedback Manager](feedback-manager)** - add comments and other feedback to elements.

  The Feedback Manager OMVS is for user interfaces supporting feedback from users.  A registered user is able to add comments, informal tags, notes, reviews and likes to elements such as assets or glossary terms.
  
* **[My Profile](my-profile)** - locate, retrieve and update information relating to the calling user's profile.

  The My Profile OMVS is for user interfaces supporting a personalized experience.  A registered user is able to maintain information about themselves and their network as well as set up and use personalized collections of metadata elements.

* **[Project Manager](project-manager)** - manage projects and related elements.

  The Project Manager OMVS provides the REST APIs to create projects and related elements such as project manager roles.  The projects can be nested and they can be classified to describe the type of project.  For example, one type of classification for a project is *Campaign*, another is *Task*.

* **[Glossary Author](glossary-author)** - develop new glossary terms and categories.

  The Glossary Author OMVS is for user interfaces supporting the creating and editing of glossary content, such as glossary terms and categories.  The changes made are immediately visible to all users.

* **[Glossary Browser](glossary-browser)** - search and view the contents of specific glossaries and their links to governance definitions and assets.

  The Glossary Browser OMVS is for user interfaces that wish to provide search facilities within a single glossary (or glossaries) along with the ability to browse the structure of the glossary and view an element within a glossary along with connected governance classifications, governance definitions, user feedback and attached assets.

* **[Glossary Workflow](glossary-workflow)** - develop new glossary terms and categories in a controlled workflow process.

  The Glossary Workflow OMVS is for user interfaces supporting the creation and editing of glossary content, such as glossary terms and categories, in a controlled workflow process.  This means that as terms and categories are created, updated and deleted, these changes are invisible to the general user until they are approved.

* **[Reference Data](reference-data)** - manage reference data.

  The Reference Data OMVS is a REST API designed to support user interfaces (UIs) that maintain reference data values and their mappings.

* **[Valid Metadata](reference-data)** - manage valid values for metadata.

  The Valid Metadata OMVS is a REST API designed to support user interfaces (UIs) that query and/or maintain list of valid values for open metadata attributes.

* **[Template Manager](template-manager)** - locate and maintain templates.

  The Template Manager OMVS is a REST API designed to support user interfaces (UIs) that support the management of templates that are used when creating new metadata.

* **[Repository Explorer](rex-view)** - explorer interface to inspect instances across a cohort of repositories.

  The Repository Explorer (Rex) OMVS is for user interfaces that support enterprise architects who need to inspect, navigate or explore the instance data stored in an open metadata repository or a cohort of repositories. It enables the retrieval of instance data (entities and relationships) and exploration of the graph of instances connected to those entities. This enables the user to construct a graph (as a diagram) to visualize the details and connectivity of a group of instances of interest to the user.

* **[Type Explorer](tex-view)** - explorer interface to inspect types across a cohort of repositories.

  The Type Explorer (Tex) OMVS is for user interfaces that support enterprise architects who need to inspect, navigate or explore the open metadata types supported by a repository or a cohort of repositories. It enables the retrieval of type data (relating to entities, relationships and classifications) and exploration of the graphs of entity type inheritance and the supported combinations of entity and relationship types.

* **[Dynamic Infrastructure and Operations](dino-view)** - admin interface to inspect servers, services, cohorts and platforms.

  The Dino OMVS is for user interfaces that support Egeria operators who need to inspect, navigate or explore the open metadata servers, services, cohorts and platforms that are configured or actively running. It is intended for operations and problem-determination.

* **[Server Author](server-author-view)** - an authoring interface for servers

  The Server Author OMVS is for user interfaces that support Egeria Server authors. This user interface allows the author to create, delete or update a server configuration using an intuitive UI, rather than needing to use the low level admin rest calls and associated json payloads.


----
Return to [open-metadata-implementation](..).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
