<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Egeria Design Documentation

ODPi Egeria implements a set of open APIs, metadata type definitions,
frameworks, connectors and interchange protocols to allow all types of tools and
metadata repositories to share and exchange metadata.

It is essentially a comprehensive toolkit for integrating and
distributing metadata between different tools and technologies.
In addition, it has a multi-tenant platform
that supports horizontal scale-out in Kubernetes and yet is light enough to run
as an edge server on a Raspberry Pi.  This platform is
used to host the integration and metadata exchange capabilities
along with open governance,
discovery and access services to automate the collection, management and
use of metadata across an enterprise.  

The result is an enterprise catalog of
data and IT resources that are transparently assessed, governed and consumed 
through many types of tools and technologies in order to
deliver maximum value to the enterprise.

The **open-metadata-implementation** module provides the implementation of the Egeria
platform and clients.

The packages are as follows:

* **[frameworks](frameworks)** - the frameworks define the interfaces for pluggable components such
as connectors, discovery services and governance actions.  These
components provide much of the customization offered by the open metadata and governance
implementation.

* **[common-services](common-services)** - a variety of common services from First Failure Data Capture (FFDC),
multi-tenancy (for the platform) along with metadata security and management.  Some of these services are
client-side and other server-side.

* **[platform-services](platform-services)** - the platform services provides the APIs for
configuring the Open Metadata and Governance (OMAG) Server Platform and discovering information about the
OMAG Servers that it is hosting.

* **[admin-services](admin-services)** - the admin services provides the APIs for configuring
and operating Open Metadata and Governance (OMAG) Servers that run on the OMAG Server Platform.

* **[repository-services](repository-services)** - the repository services provides the events, interfaces and
implementation of the metadata exchange and federation capabilities for a metadata
repository that supports the open metadata standards.

* **[access-services](access-services)** - the access services provide domain-specific services for data tools, engines
and platforms that manage the function and support the work of the people engaged with the
organization.

* **[adapters](adapters)** - the adapters provide the pre-written pluggable components to the frameworks.

* **[governance-services](governance-servers)** - the governance services pull combinations
of the services together to
support different integration patterns with third party technology.

* **[user-interfaces](user-interfaces)** - basic user interfaces to demonstrate the power of the open
metadata and governance capabilities.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.