<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Digital Service Open Metadata Access Service (OMAS)

The Digital Service OMAS supports the business owners as they track the development and use of
digital services that support the operation of the organization.

Digital services are business services that are implemented in software.  An example of a digital service
is one that allows a customer to buy a product from the organization.

Digital services are implemented using a variety of software components.  These components are defined and deployed
using the [Software Developer OMAS](../software-developer) and [DevOps OMAS](../dev-ops) respectively.

The Digital Service OMAS is responsible for:

* Recording the key business capabilities of the organization - with particular focus on those business
capabilities involved in the business transformation.
* Assigning the owner of each business capability.
* Linking the business capability to the governance definitions defined by the [governance program](../governance-program).
* For each business capability, defining the digital services that support it.
* Assigning ownership to each digital service.
* Supporting the owner of a digital service throughout the digital service's lifecycle.
* Supporting the business capability owners with the ability to review the status of the digital services within
their business capability.

* [Documentation](https://egeria-project.org/services/omas/digital-service/overview)

## Design

The module structure for the Digital Service OMAS is as follows:

* [digital-service-api](digital-service-api) supports the common Java classes that are used both by the client and the server.
* [digital-service-client](digital-service-client) supports the client library.
* [digital-service-server](digital-service-server) supports in implementation of the access service and its related event management.
* [digital-service-spring](digital-service-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

