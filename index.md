---
layout: homepage
css: style-sub.css
---
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

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

ODPi Egeria is an open source project dedicated to making metadata open and automatically exchanged between tools
and data platforms, no matter which vendor they come from.

We believe in the **open metadata and governance manifesto**:

 * The maintenance of metadata must be automated to scale to the sheer volumes and variety of data involved in modern business.  Similarly the use of metadata should be used to drive the governance of data and create a business friendly logical interface to the data landscape.
 * The availability of metadata management must become ubiquitous in cloud platforms and large data platforms, such as Apache Hadoop so that the processing engines on these platforms can rely on its availability and build capability around it.
 * Metadata access must become open and remotely accessible so that tools from different vendors can work with metadata located on different platforms.  This implies unique identifiers for metadata elements, some level of standardization in the types and formats for metadata and standard interfaces for manipulating metadata.
 * Wherever possible, discovery and maintenance of metadata has to an integral part of all tools that access, change and move information.

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

If you are interested in getting started with Egeria, try our [tutorials](open-metadata-resources/open-metadata-tutorials).
They show people with different roles in a fictitious organization called
[Coco Pharmaceuticals](https://opengovernance.odpi.org/coco-pharmaceuticals/) making use of different capabilities
from Egeria.

Alternatively read on for more information.

## The Guides

The guides provide step-by-step guidance on a specific aspect of working with Egeria.

* [Egeria Planning Guide - How to deploy Egeria into your organization](open-metadata-publication/website/planning-guide) - provides
guidance on how to deploy Egeria in your organization.

* [OMAG Server Configuration Guide](open-metadata-implementation/admin-services/docs/user) - describes how to
configure Open Metadata and Governance (OMAG) servers (shown as an orange circle in **Figure 1**)
to support the integration of different types of technologies.

* [Egeria Developer Guide - Extending Egeria using connectors](open-metadata-publication/website/developer-guide)
describes how to use Java classes called connectors to extend Egeria to exchange metadata with
new types of technology and add new capabilities.

* [Egeria Diagnostic Guide](open-metadata-publication/website/diagnostic-guide)
describes the different types of diagnostics produced by Egeria and how to use them.

* [Open Metadata Conformance Test Suite Guide](open-metadata-conformance-suite) describes
how to run the Open Metadata Conformance Test Suite to validate that a technology is
compliant with the open metadata specifications.

## The Lists

The Egeria glossary provides definitions to terms used in the Egeria project.
The module organization defines the hierarchical structure of the modules in the
Egeria project.

* [Egeria Glossary](open-metadata-publication/website/open-metadata-glossary.md)
* [Egeria Module Organization](Content-Organization.md)

## Your Questions ...

The following articles may answer additional questions that you have.

* [Where is the code?](https://github.com/odpi/egeria)
* [Who is contributing?](https://lfanalytics.io/projects/5b554807-b041-4f98-a6ba-3306f688e05e/dashboard)
* [How do I join the mailing list?](https://lists.odpi.org/g/odpi-project-egeria)
* [What is the ODPi?](https://www.odpi.org/)
* [Why is the project called Egeria?](open-metadata-publication/website/why-egeria)
* [What are the goals of Egeria?](open-metadata-publication/website)
* [Why is open metadata important?](https://www.redbooks.ibm.com/redpapers/pdfs/redp5486.pdf)
* [How do I run Egeria?](open-metadata-resources/open-metadata-tutorials)
* [Where is the design documentation?](open-metadata-implementation)
* [Where are the open metadata specifications?](open-metadata-publication/website/open-metadata-specifications)
* [How do I raise a bug or feature request?](https://github.com/odpi/egeria/issues)
* [How do I enhance my product to support open metadata?](open-metadata-publication/website/open-metadata-integration-patterns)
* [How do I test that a technology is conformant with the open metadata specifications?](open-metadata-conformance-suite)
* [How is the egeria project content organized?](Content-Organization.md)
* [How do I find out more about Data Governance?](https://odpi.github.io/data-governance/)


## ODPi Egeria project governance

This project is maintained by the ODPi Egeria community.
To understand how to join and contribute see the
[Community Guide](Community-Guide.md).

We aim to operate in a transparent, accessible way for the benefit
of the Egeria community.

All participation in this project is therefore open and not
bound to any corporate affiliation nor membership of the ODPi.
Participants are only required to follow the ODPi's [Code of Conduct](https://github.com/odpi/specs/wiki/ODPi-Code-of-Conduct).

The governance of the project is described in more detail in the
[Egeria Operations](Egeria-Operations.md).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
