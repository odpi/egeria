<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Security Access Connectors

The connector in this module provides a combined implementation of the [open metadata security connectors](https://egeria-project.org/features/metadata-security/overview/)
that can be added to the OMAG Server Platform and to any OMAG server
running on the platform.  The connector makes use of externalized security information
supplied by an embedded [Secrets Store Connector](https://egeria-project.org/concepts/secrets-store-connector/).

There is a sample YAML-based secrets store called `coco-user-directory.omsecrets` included in Egeria's 
[deployment](../../../../../open-metadata-resources/open-metadata-deployment/secrets).
With this sample secrets store, it is possible to experiment with the use of 
[governance zones](https://egeria-project.org/concepts/governance-zone)
to control the visibility of elements
through the open metadata services as well as more standard authorization services base around the platform/server/service called.

The open metadata platform security connector provides authorization
services for the platform services and the admin service to create a new server.
The open metadata server security connector is specific to an OMAG server instance and
is defined in the configuration document for a server.

This connector illustrates how a security connector implementation extends the appropriate base class from 
[metadata-security](../../../../common-services/metadata-security) and uses their methods to provide
a security service.

Features of this implementation are described below.

## User security

The user security methods pass through all user account management and verification to the nested secrets store connector.
The other security methods validate that that calling user has a valid user account before performing any specific checks.

## Governance Zones

Governance zones are supported through the element security methods.
Each governance zone is modelled as a security access control.
The zone itself represents a collection of metadata.
The security access control defines which users can perform which operation.
There is a DEFAULT operation that means, all operations not explicitly defined.

An element with no zones is accessible to every user with an active account.
An element in one or more zones is accessible if any one of its zones permits the operation.
A zone with no security access control in the user directory, or whose control has neither a list for the
operation nor a DEFAULT list, permits no-one - so every governance zone in use must be defined in the user directory.
The exception is a zone named after a user, which is that user's own.


## Connection security

Finally, the connector will only permit a connector with 
to access Connection object that have security information in it such as userIds and passwords.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.