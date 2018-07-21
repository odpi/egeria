<!-- SPDX-License-Identifier: Apache-2.0 -->

# Governance Engine Open Metadata Access Service (OMAS)

The Governance Engine Open Metadata Access Service (OMAS) provides access to metadata for policy enforcement frameworks
such as Apache Ranger.  This API simplifies the internal models and structures of
the open metadata type model and related structure for the consumers.

As an example, Ranger needs to know how a particular entity is classified so that the
classification can be used within a policy (rule). Atlas has a complex graph oriented model,
within which classifications can be multi level - for example a column may be classified
as "employee_salary" whilst employee_salary may be SPI.
Ranger however just needs to know that employee_salary is SPI, not how we got there.
So we convert this complex model into something much more operationally
focused and deliver that over the API. The implementation will follow this graph,
and build up a list of all tags that are appropriate to use. Note that in the case
of Ranger it is actually the tagsync process that will call the
Governance Engine OMAS for this classification information

Ranger can do this today, but via a large number of individual requests to retrieve
types and entities. Rather than these lower level queries to the metadata repository services,
the Governance Engine OMAS offers result sets to make queries more efficient,
and more appropriate notifications.