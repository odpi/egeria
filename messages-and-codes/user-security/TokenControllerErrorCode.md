<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# TokenControllerErrorCode

The TokenControllerErrorCode is used to define first failure data capture (FFDC) for errors that occur when processing user security information on an incoming REST API call. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 1 |
| **Message identifiers begin** | `TOKEN-CONTROLLER-401-` |
| **Java class** | `org.odpi.openmetadata.tokencontroller.ffdc.TokenControllerErrorCode` |
| **Module** | [open-metadata-implementation/user-security/token-controller](../../open-metadata-implementation/user-security/token-controller) |
| **Source** | [TokenControllerErrorCode.java](../../open-metadata-implementation/user-security/token-controller/src/main/java/org/odpi/openmetadata/tokencontroller/ffdc/TokenControllerErrorCode.java) |
| **Further reading** | <https://egeria-project.org/features/metadata-security/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [TOKEN-CONTROLLER-401-001](#token-controller-401-001) | 400 | Call made to {0} service {1} has no logged-on user |

----

### TOKEN-CONTROLLER-401-001

> Call made to {0} service {1} has no logged-on user

|  |  |
|---|---|
| **Java constant** | `TokenControllerErrorCode.NO_USER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

An inbound REST API call has been received but there is no userId in the authorization bearer token.

**User action**

Ensure the user acquires a valid token and this token is included in the request header.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
