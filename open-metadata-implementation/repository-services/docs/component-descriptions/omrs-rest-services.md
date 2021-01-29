<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMRS REST Services

The OMRS REST Services provide the server-side implementation of the repository services REST API
that enables repositories to make direct metadata queries to other open metadata repositories who
are members of the same cohort.

There are 4 REST APIs:

* **Local repository services API** used by other [members of a cohort](../cohort-member.md) to issue
  API calls to the server's local repository.
  
* **Enterprise repository services API** providing a federated view of all of the metadata from the
  [Cohort Members](../cohort-member.md) of the
  [Open Metadata Repository Cohorts](../open-metadata-repository-cohort.md) that the
  server is a member of.
  
* **Metadata Highway services API** providing information about the [Cohort Members](../cohort-member.md) of the
  [Open Metadata Repository Cohorts](../open-metadata-repository-cohort.md) that the
  server is a member of.
  
* **Audit Log services API** providing information about the audit log and the destinations configured
  in the server.


----
* Return to [repository services component descriptions](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.