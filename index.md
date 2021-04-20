---
layout: homepage
css: style-sub.css
---
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Egeria - Open Metadata and Governance

Every week we hear of new tools, data platforms and opportunities for organizations to embrace advanced
digital technologies such as artificial intelligence.
Yet despite investment and the focus of smart people,
few organizations succeed in making wide and systematic use of their data.

Today's IT is at the heart of the problem.
Many tools and data platforms recognize the value of metadata,
but manage it in a siloed, proprietary way that assumes
they are the only technology employed by the organization.
The result is that knowledge is not shared between people that use different tool sets.

Egeria is an open source project dedicated to making metadata open and automatically exchanged between tools
and platforms, no matter which vendor they come from.

We believe in the **open metadata and governance manifesto**:

 * The maintenance of metadata must be automated to scale to the sheer volumes and variety of data involved in modern business.  Similarly the use of metadata should be used to drive the governance of data and create a business friendly logical interface to the data landscape.
 * The availability of metadata management must become ubiquitous in cloud platforms and large data platforms, such as Apache Hadoop so that the processing engines on these platforms can rely on its availability and build capability around it.
 * Metadata access must become open and remotely accessible so that tools from different vendors can work with metadata located on different platforms.  This implies unique identifiers for metadata elements, some level of standardization in the types and formats for metadata and standard interfaces for manipulating metadata.
 * Wherever possible, discovery and maintenance of metadata has to be an integral part of all tools that access, change and move information.

We also believe that code talks. 
Egeria provides an Apache 2.0 licensed platform to support vendors that sign up to the open metadata and governance
manifesto.

Figure 1 shows Egeria in action.   Today's organizations have their tools and technologies distributed across
multiple data centres and cloud providers.  These are shown as the green
clouds.  The Egeria Open Metadata and Governance (OMAG) Server Platform (blue rounded box) is deployed in
each location.   Each platform is then configured to run different
types of integration services that are tailored to support specific types
of tools (see orange circles).  The Egeria services also manage the exchange of
metadata between the platforms.

![Figure 1](open-metadata-publication/website/images/egeria-distributed-operation.png#pagewidth)

The Egeria open metadata and governance technology provides an open metadata
type system, frameworks, connectors, APIs, event payloads and interchange protocols to enable tools,
engines and platforms to exchange metadata in order to get the best
value from data and tools for a wide range of use cases. 

## Eager to Get Started Now

The [Egeria Solutions](open-metadata-publication/website/solutions) describe
different integration solutions that you can create with Egeria.

If you are interested in getting hands-on with the Egeria technology, try our [tutorials](open-metadata-resources/open-metadata-tutorials).
They show people with different roles in a fictitious organization called
[Coco Pharmaceuticals](https://opengovernance.odpi.org/coco-pharmaceuticals/) making use of different capabilities
from Egeria.

Alternatively read on for more information.

## The Guides

The guides provide step-by-step guidance on a specific aspect of working with Egeria.

* [Community Guide](Community-Guide.md) - describes how to join the Egeria community to participate
  in discussions and other activities.
  
* [Operations Guide](Egeria-Operations.md) - describes the governance processes used by the Egeria community.

* [Building an Asset Catalog](open-metadata-publication/website/cataloging-assets) - provides
  guidance on the different approaches offered by Egeria to maintain a catalog service.
  A catalog service enables
  individuals and automated processes to to search for, select and retrieve information
  about relevant data, systems, applications, software components, processes, ... to help them
  achieve their specific task.
 
  This guide covers what can be cataloged, the incremental value of each
  type of information that can be added to the catalog along with the different options
  to automate the maintenance of the catalog entries.

* [Egeria Planning Guide](open-metadata-publication/website/planning-guide) - provides
  guidance on how to deploy Egeria in your organization.  It will help you decide on which components of Egeria
  to use, where they should be located and how they need to be configured.

* [Egeria Administration Guide](open-metadata-implementation/admin-services/docs/user) - describes how to
  configure and operate Egeria
  to support the integration of different types of technologies. 

* [Egeria Developer Guide](open-metadata-publication/website/developer-guide) -
  describes how to use Java classes called connectors to extend Egeria to exchange metadata with
  new types of technology and add new capabilities.  It also covers the APIs and Event Topics
  used by applications to save and retrieve metadata from Egeria.

* [Egeria Diagnostic Guide](open-metadata-publication/website/diagnostic-guide) -
  describes the different types of diagnostics produced by Egeria and how to use them.

* [Open Metadata Conformance Test Suite Guide](open-metadata-conformance-suite) - describes
  how to run the Open Metadata Conformance Test Suite to validate that a technology is
  compliant with the open metadata specifications.

## The Lists

If you would like to browse to see what is available then these pages may be of interest.

* [Egeria Open Metadata Types](open-metadata-publication/website/open-metadata-types) - describes
  the scope and usage of metadata supported by Egeria "out of the box".
* [Egeria Glossary](open-metadata-publication/website/open-metadata-glossary.md) - provides definitions to terms 
  used in the Egeria project.
* [Egeria Connector Catalog](open-metadata-publication/website/connector-catalog) - provides descriptions of the
  connectors supporting the use of third party technology in the open metadata ecosystem.
* [Egeria Status and Road Map](open-metadata-publication/website/roadmap) - describes the various
  components of Egeria, how they fit together and their current status.
* [Egeria Module Organization](Content-Organization.md) - defines the hierarchical structure of the modules in the
  Egeria project.

## Your Questions

The following articles may answer additional questions that you have.

* [Where is the code?](https://github.com/odpi/egeria) - link to GitHub.
* [Who is contributing?](https://lfanalytics.io/projects/odpi%2Fegeria/dashboard) - see the project activity by logging
  in with your GitHub Account.
* [How do I join the mailing list?](https://lists.lfaidata.foundation/g/egeria-technical-discuss/topics) - go to the sign up page.
* [What is the LF AI & Data Foundation?](https://lfaidata.foundation/) - visit their website.
* [Why is the project called Egeria?](open-metadata-publication/website/why-egeria) - understand the background of the project.
* [What are the goals of Egeria?](open-metadata-publication/website) - understand the motivation behind this project and its key components.
* [How do I run Egeria?](open-metadata-resources/open-metadata-tutorials) - try our getting started tutorials and labs.
* [Where is the design documentation?](open-metadata-implementation) - dig deeper into the code.
* [Where are the open metadata specifications?](open-metadata-publication/website/open-metadata-specifications) - learn how Egeria is both supporting and providing standards.
* [How do I raise a bug or feature request?](https://github.com/odpi/egeria/issues) - create a GitHub Issue to get help.
* [How do I integrate my metadata repository into the open metadata ecosystem?](open-metadata-publication/website/open-metadata-integration-patterns) - understand the options.
* [How do I test that a technology is conformant with the open metadata specifications?](open-metadata-conformance-suite) - learn how to run the conformance suite.
* [How is the egeria project content organized?](Content-Organization.md) - see the site structure.
* [How do I find out more about Data Governance?](https://odpi.github.io/data-governance/) - link to our guidance on governance.


## ODPi Egeria project governance

This project is maintained by the Egeria community.
To understand how to join and contribute see the
[Community Guide](Community-Guide.md).

We aim to operate in a transparent, accessible way for the benefit
of the Egeria community. All participation in this project is therefore open and not
bound to any corporate affiliation nor membership of the [Linux Foundation](https://www.linuxfoundation.org/) or 
[LF AI & Data Foundation](https://lfaidata.foundation/).
Participants are only required to follow Egeria's [Code of Conduct](https://github.com/odpi/specs/wiki/ODPi-Code-of-Conduct).

The governance of the project is described in more detail in the
[Egeria Operations](Egeria-Operations.md).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
