<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Endpoint - part of the [Open Connector Framework (OCF)](../..)

The endpoint is a set of properties that defines the 
network address and how to connect to it for a resource deployed in the digital landscape.
Its properties are:

* **guid** -   Globally unique identifier for the endpoint.
* **url** -   External link address for the endpoint properties in the metadata repository.
This URL can be stored as a property in another entity to create an explicit link to this endpoint.
* **qualifiedName** -   The official (unique) name for the endpoint. This is often defined by the IT systems management
organization and should be used (when available) on audit logs and error messages.
* **displayName** - A consumable name for the endpoint.   Often a shortened form of the qualifiedName for use
on user interfaces and messages.  The displayName should be only be used for audit logs and error messages
if the qualifiedName is not set.
* **description** - A description for the endpoint.
* **address** - The location of the asset.  For network connected resources, this is typically the
URL and port number (if needed) for the server where the asset is located
(or at least accessible by the connector).  For file-based resources, this is typically the name of the file.
* **protocol** - The communication protocol that the connection should use to connect to the server.
* **encryptionMethod** - Describes the encryption method to use (if any).  This is an open value allowing
information needed by the connector user to retrieve all of the information they need to work with
the endpoint.
* **additionalProperties** - Any additional properties that the connector need to know in order to
access the Asset.


## Further information

 * The open metadata type for an endpoint is defined in 
   [model 0040](../../../../../open-metadata-publication/website/open-metadata-types/0040-Software-Servers.md).


----
* [Return to OCF Overview](../..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.