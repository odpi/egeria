<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Software Developer Open Metadata Access Service (OMAS)

The Software Developer OMAS provides APIs and events for software developer
tools and applications that help developers make good use of the
standards and best practices defined for the data landscape.

For example, it supports search for data structure implementation snippets
based on search criteria such as glossary terms and/or language.

It also provides information about the most appropriate data sources
to use for particular situations.

The module structure for the Software Developer OMAS is as follows:

* [software-developer-client](software-developer-client) supports the client library.
* [software-developer-api](software-developer-api) supports the common Java classes that are used both by the client and the server.
* [software-developer-server](software-developer-server) supports in implementation of the access service and its related event management.
* [software-developer-spring](software-developer-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
