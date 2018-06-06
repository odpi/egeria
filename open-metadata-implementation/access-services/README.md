<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Open Metadata Access Services (OMAS)

The Open Metadata Access Services (OMRS) provide domain-specific services
for data tools, engines and platforms to integrate with open metadata.

The access services are as follows:

* **asset-catalog** - search for assets.
* **asset-consumer** - create connectors to access assets.
* **asset-owner** - manage metadata and feedback for owned assets.
* **community-profile** - manage personal profiles and communities.
* **connected-asset** - provide metadata about assets for the connectors.
* **data-architecture** - support the definition of data standards and models.
* **data-infrastructure** - manage metadata about deployed infrastructure.
* **data-platform** - exchange metadata with a data platform.
* **data-privacy** - support a data privacy officer.
* **data-process** - exchange metadata with a data processing engine.
* **data-protection** - set up rules to protect data.
* **data-science** - manage metadata for analytics.
* **dev-ops** - manage metadata for a devOps pipeline.
* **discovery-engine** - manage metadata for metadata discovery services.
* **governance-engine** - manage metadata for an operational governance engine.
* **governance-program** - set up and manage a governance program.
* **information-view** - configure and manage metadata for data tools that 
create virtual views over data - such as business intelligence tools and
data virtualization platforms.
* **project-management** - manage definitions of projects for metadata
management and governance.
* **software-developer** - deliver useful metadata to software developers.
* **stewardship-action** - manage metadata as part of a data steward's work to
improve the data landscape.
* **subject-area** - develop a definition of a subject area including glossary
terms, reference data and rules.

Each OMAS supports a client and a server-side implementation.  The clients
are combined into a single JAR file by the open-metadata-distribution module
for use by web applications (such as the access-services-user-interface) and
data tools, engines and platforms that integrate with open metadata.

The server-side components run in the open-metadata/governance-servers or
they may be embedded in an external technology - such as a vendor's
commercial metadata repository offering.