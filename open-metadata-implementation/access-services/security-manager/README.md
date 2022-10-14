<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Security Manager Open Metadata Access Service (OMAS)

The Security Manager OMAS provides APIs for technologies wishing to register
new data assets, connections and related schema from data resources located
in database servers, file systems, file managers and content managers.
The caller of this interface may be the security manager itself, or an
[integration daemon](https://egeria-project.org/concepts/integraiton-daemon) if the
security manager does not support open metadata directly.

* [Documentation](https://egeria-project.org/services/omas/security-manager/overview)

# Design Information

The module structure for the Security Manager OMAS is as follows:

* [security-manager-client](security-manager-client) supports the client library.
* [security-manager-api](security-manager-api) supports the common Java classes that are used both by the client and the server.
* [security-manager-server](security-manager-server) supports in implementation of the access service and its related event management.
* [security-manager-spring](security-manager-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
* [security-manager-topic-connectors](security-manager-topic-connectors) supports the connectors used to access the InTopic and OutTopic
events from the Security Manager OMAS.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

