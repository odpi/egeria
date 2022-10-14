<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Security Officer Open Metadata Access Service (OMAS)

The Security Officer Open Metadata Access Service (OMAS) provides support for a security officer to set upp and audit the security settings managed by the open metadata ecosystem.

* [Documentation](https://egeria-project.org/services/omas/security-officer/overview)

## Design Information

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

