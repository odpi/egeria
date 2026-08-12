<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# OMF Metadata Management

**OMF Metadata Management** provides the generic open metadata store services used by the
[Open Metadata Framework (OMF)](../../frameworks/open-metadata-framework) - creating, searching for, updating and
deleting open metadata elements, classifications and relationships, along with the associated out topic events
that notify listeners of metadata changes.

* [Documentation](https://egeria-project.org/services/omf-metadata-management)


## Design Information

The module structure for OMF Metadata Management is as follows:

* [omf-metadata-client](omf-metadata-client) supports the client library.
* [omf-metadata-api](omf-metadata-api) supports the common Java classes that are used both by the client and the server.
* [omf-metadata-server](omf-metadata-server) supports in implementation of the access service and its related event management.
* [omf-metadata-spring](omf-metadata-spring) supports the REST API using the [Spring](https://egeria-project.org/guides/contributor/runtime/#spring) libraries.
* [omf-topic-connectors](omf-topic-connectors) supports the topic connectors for open metadata events.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

