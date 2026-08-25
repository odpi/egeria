<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# WatchdogActionErrorCode

The WatchdogActionErrorCode error code is used to define first failure data capture (FFDC) for errors that occur when working with the Discovery Engine Services. It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 3 |
| **Message identifiers begin** | `OMES-WATCHDOG-ACTION-400-` |
| **Java class** | `org.odpi.openmetadata.engineservices.watchdogaction.ffdc.WatchdogActionErrorCode` |
| **Module** | [open-metadata-implementation/engine-services/watchdog-action/watchdog-action-api](../../open-metadata-implementation/engine-services/watchdog-action/watchdog-action-api) |
| **Source** | [WatchdogActionErrorCode.java](../../open-metadata-implementation/engine-services/watchdog-action/watchdog-action-api/src/main/java/org/odpi/openmetadata/engineservices/watchdogaction/ffdc/WatchdogActionErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/omes/watchdog-action/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMES-WATCHDOG-ACTION-400-001](#omes-watchdog-action-400-001) | 400 | The Watchdog Action OMES are unable to initialize a new instance in server {0}; error message is {1} |
| [OMES-WATCHDOG-ACTION-400-002](#omes-watchdog-action-400-002) | 400 | The watchdog action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3} |
| [OMES-WATCHDOG-ACTION-400-004](#omes-watchdog-action-400-004) | 400 | The watchdog engine action {0} can not be started because there is no governance service context |

----

### OMES-WATCHDOG-ACTION-400-001

> The Watchdog Action OMES are unable to initialize a new instance in server {0}; error message is {1}

|  |  |
|---|---|
| **Java constant** | `WatchdogActionErrorCode.SERVICE_INSTANCE_FAILURE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The Watchdog Action OMES detected an error during the start up of a specific server instance.  No watchdog action services are available in the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### OMES-WATCHDOG-ACTION-400-002

> The watchdog action service {0} linked to request type {1} can not be started.  The {2} exception was returned with message {3}

|  |  |
|---|---|
| **Java constant** | `WatchdogActionErrorCode.INVALID_WATCHDOG_SERVICE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The monitoring request is not run and an error is returned to the caller.

**User action**

This may be an error in the watchdog action service's logic or the watchdog action service may not be properly deployed or there is a configuration error related to the watchdog action engine.  The configuration that defines the request type in the watchdog action engine and links it to the watchdog action service is maintained in the metadata server by the open governance configuration API.Verify that this configuration is correct.  If it is then validate that the jar file containing the watchdog action service's implementation has been deployed so the Watchdog Action OMES can load it.  If all this is true this it is likely to be a code error in the watchdog action service in which case, raise an issue with the author of the watchdog action service to get it fixed.  Once the cause is resolved, retry the monitoring request.


----

### OMES-WATCHDOG-ACTION-400-004

> The watchdog engine action {0} can not be started because there is no governance service context

|  |  |
|---|---|
| **Java constant** | `WatchdogActionErrorCode.NULL_REQUEST` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The monitoring request is not run and an error is recorded in the engine action because the governance service is not set up property.

**User action**

This is an unexpected error, you may need to trace through the code to find out what has happened.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
