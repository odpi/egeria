<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Egeria Design Documentation

Egeria implements a set of open APIs, metadata type definitions,
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

* **[access-services](access-services)** - the access services provide domain-specific services for data tools, engines
and platforms that for maintaining and retrieving metadata.  The access services run in either the metadata access
point server or metadata server on the OMAG Server Platform.  They call the repository services
and the common services.

* **[adapters](adapters)** - the adapters provide the pre-written pluggable components that fit into the frameworks
(see below).  These components allow calls to third party technology to be made from the Egeria
OMAS Server Platform.  Some of these components are to support the operation of Egeria and others are to enable
Egeria to connect to third party technology to exchange metadata or govern its assets.

* **[admin-services](admin-services)** - the admin services provides the APIs for configuring
and operating Open Metadata and Governance (OMAG) Servers that run on the OMAG Server Platform.

* **[common-services](common-services)** - a variety of common services from First Failure Data Capture (FFDC),
multi-tenancy (for the platform) along with metadata security and management.  Some of these services are
client-side and other server-side.

* **[engine-services](engine-services)** - the engine services support the hosting of different types of
governance engines that can be hosted in the engine host governance server on the OMAG Server Platform.

* **[frameworks](frameworks)** - the frameworks define the interfaces for pluggable components such
as connectors, discovery services and governance actions.  These
components provide much of the customization offered by the open metadata and governance
implementation.

* **[framework-services](framework-services)** - the framework services provide REST APIs to support the interfaces
defined in the frameworks.  they are incorporated into the [access services](access-services).

* **[governance-servers](governance-servers)** - the governance server services provide the 
server frameworks that support the different types of governance servers that can run in the OMAG Server Platform.

* **[integration-services](integration-services)** - the integration services support the hosting of
integration connectors to drive the exchange of metadata with third party technologies.
These services run in the integration daemon governance server on top of the OMAG Server Platform.

* **[platform-services](platform-services)** - the platform services provides the APIs for
configuring the Open Metadata and Governance (OMAG) Server Platform and discovering information about the
OMAG Servers that it is hosting.

* **[repository-services](repository-services)** - the repository services provides the events, interfaces and
implementation of the metadata exchange and federation capabilities for a metadata
repository that supports the open metadata standards.

* **[platform-chassis](platform-chassis)** - the platform chassis is the base component for the OMAG Server Platform.
It includes the web server that receives the REST API requests for both the OMAG Server Platform
and the servers that run on it.

* **[user-interfaces](user-interfaces)** - basic user interfaces to demonstrate the power of the open
metadata and governance capabilities.

* **[user-security](user-security)** - modules to enable token-based authentication/authorization for the 
OMAG Server Platform and OMAG Server runtimes.

* **[view-services](view-services)** - the view services support the REST API interfaces
called by the user interfaces.  These services run in a view server on the OMAG Server Platform.
 
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
