<!-- SPDX-License-Identifier: Apache-2.0 -->

# Open Metadata Implementation

The open metadata and governance standards provide a Java implementation broken
down into embeddable packages to make it easier for different technologies to adopt
the open metadata standards.

The packages are as follows:

* **[frameworks](frameworks)** - the frameworks define the interfaces for pluggable components.  These
components provide much of the customization offered by the open metadata and governance
implementation.

* **[repository-services](repository-services)** - the repository services provides the events, interfaces and
implementation of the metadata exchange and federation capabilities for a metadata
repository that supports the open metadata standards.

* **[access-services](access-services)** - the access services provide domain-specific services for data tools, engines
and platforms that manage the function and support the work of the people engaged with the
organization.

* **[governance-servers](governance-servers)** - the governance servers pull combinations of the services together to
support different integration patterns.

* **[adapters](adapters)** - the adapters provide the pre-written pluggable components to the frameworks.

* **[user-interfaces](user-interfaces)** - basic user interfaces to demonstrate the power of the open
metadata and governance capabilities.