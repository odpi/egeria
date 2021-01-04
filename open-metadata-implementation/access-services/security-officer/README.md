<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Security Officer Open Metadata Access Service (OMAS)

The Security Officer Open Metadata Access Service (OMAS) provides access to metadata for policy enforcement frameworks
such as Apache Ranger.  This API simplifies the internal models and structures of
the open metadata type model and related structure for the consumers.

As an example, Apache Ranger needs to know how a particular entity is classified so that the
classification can be used within a policy (rule).
The [Open Metadata Types](../../../open-metadata-publication/website/open-metadata-types)
define a complex graph-oriented model,
within which classifications can be multi level - for example a column may be classified
as `employee_salary` whilst `employee_salary` itself may be classified as `SPI`.
Ranger however just needs to know that the column is SPI, not how we got there.
So we convert this complex model into something much more 
operationally-focused in the form of [SecurityTags](../../../open-metadata-publication/website/open-metadata-types/0423-Security-Tags.md)
and deliver that over the API. The implementation will follow this graph,
and build up a list of all tags that are appropriate to use. Note that in the case
of Ranger it is actually the tagsync process that will call the
Governance Engine OMAS for this classification information

Ranger can do this today, but via a large number of individual requests to retrieve
types and entities. Rather than these lower level queries to the metadata repository services,
the Security Officer OMAS offers result sets to make queries more efficient,
and more appropriate notifications.

More details on Apache Ranger's [Tag Propagation Process](docs/TagPropogation) is found in the docs directory by following the link.

## Internals

The module structure for the Security Officer OMAS is as follows:

* [security-officer-api](security-officer-api) supports the common Java classes that are used both by the client and the server.
* [security-officer-topic-connectors](security-officer-topic-connectors) supports the common Java classes that are used both by the client and the server.
* [security-officer-client](security-officer-client) supports the client library.
* [security-officer-server](security-officer-server) supports in implementation of the access service and its related event management.
* [security-officer-spring](security-officer-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

