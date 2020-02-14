<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Open Metadata View Services (OMVS)

The Open Metadata View Services (OMVS) provide task oriented, domain-specific services
for user interfaces to integrate with open metadata. 

The view services are as follows:

* **[subject-area](subject-area)** - develop a definition of a subject area including glossary
terms, reference data and rules.

  The Subject Area OMVS is for tools that support subject matter experts
who are defining glossaries, reference data and rules around data for a specific
subject area, such as "customer data".  It supports the development of a comprehensive
definition of the subject area and the standards that support it.
These definitions can then be folded into the Governance Program,
and used by Asset Owner's to improve the findability and understandability
of their assets by linking their asset's structure to relevant parts of
the subject area definition.

## Inside an OMVS

User Interfaces can connect to an OMVS through its REST API. The REST API interacts with a remote OMAG Server.
The OMVS APIs are deployed together in a single web application. 

The [administration services](../admin-services/README.md) provide
the ability to configure, start and stop the view services.

Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.