<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Egeria Developer Guide

Egeria is designed to simplify the effort necessary to integrate different
technologies so that they can actively share and consume metadata from each other.

It focuses on providing five types of integration interfaces.

* Java interfaces for small integration components called connectors
  that translate between third party APIs and open metadata APIs.
  These connectors are hosted in the Egeria servers and support the active exchange of
  metadata with these technologies. 
  [Learn more ...](what-is-a-connector.md)
  
* Java clients for applications to call the Open Metadata Access Service (OMAS) interfaces, each
  of which are crafted for particular types of technology.  These interfaces
  support both synchronous APIs, inbound event notifications and outbound
  asynchronous events.
  [Learn more ...](../../../open-metadata-implementation/access-services/docs/user)
  
* Connectors for accessing popular type of data sources that also retrieve
  open metadata about the data source.  This allows applications and tools to 
  understand the structure, meaning, profile, quality and lineage of the data
  they are using.
  [Learn more ...](what-is-a-connector.md)

* REST APIs for the Egeria Services.  These include the Access Services,
  Admin Services and Platform Services.
  [Learn more ...](../../../open-metadata-implementation/access-services)

* Kafka Topics with JSON payloads for asynchronous communication (both in and out)
  with the open metadata ecosystem.
  [Learn more ...](../../../open-metadata-implementation/access-services)
  

----
Return to [Home Page](../../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.