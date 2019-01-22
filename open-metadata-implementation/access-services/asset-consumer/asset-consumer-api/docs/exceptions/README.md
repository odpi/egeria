<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer OMAS Exceptions

## Checked Exceptions

* **AmbiguousConnectionNameException** - there is more than one connection defined for the supplied connection name.
* **InvalidParameterException** - one of the parameters is null or invalid.
* **NewInstanceException** - the access service is not able to register a new instance of itself in its instance map.
* **NoConnectedAssetException** - there is no asset associated with this connection.
* **PropertyServerException** - there is a problem retrieving information from the property server.
* **UnrecognizedConnectionGUIDException** - the supplied unique identifier (guid) for a connection is not recognized by the property server(s).
* **UnrecognizedConnectionNameException** - there is no connection defined for the supplied name.
* **UnrecognizedGUIDException** - the supplied unique identifier (guid) is not recognized by the property server(s).
* **UserNotAuthorizedException** - the requesting user is not authorized to issue this request.

## Unchecked exceptions

* **AssetConsumerRuntimeException** - unexpected logic error.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.