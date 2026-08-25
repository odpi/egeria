<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OCFServicesErrorCode

The OCFServicesErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with OCF Beans.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `CONNECTED-ASSET-SERVICES-` |
| **Java class** | `org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.ffdc.OCFServicesErrorCode` |
| **Module** | [open-metadata-implementation/access-services/ocf-metadata-management/ocf-metadata-api](../../open-metadata-implementation/access-services/ocf-metadata-management/ocf-metadata-api) |
| **Source** | [OCFServicesErrorCode.java](../../open-metadata-implementation/access-services/ocf-metadata-management/ocf-metadata-api/src/main/java/org/odpi/openmetadata/frameworkservices/ocf/metadatamanagement/ffdc/OCFServicesErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/ocf-metadata-management/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [CONNECTED-ASSET-SERVICES-404-001](#connected-asset-services-404-001) | 404 | The open metadata repository services are not initialized for the {0} operation |
| [CONNECTED-ASSET-SERVICES-500-001](#connected-asset-services-500-001) | 500 | The requested connector for connection named {0} has not been created.  The connection was provided by the OCF service running in OMAG Server {1} at {2} |

----

### CONNECTED-ASSET-SERVICES-404-001

> The open metadata repository services are not initialized for the {0} operation

|  |  |
|---|---|
| **Java constant** | `OCFServicesErrorCode.OMRS_NOT_INITIALIZED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The system cannot connect to an open metadata repository.

**User action**

Check that the server where the Open Connector Framework metadata services are running is initialized correctly.  Correct any errors discovered and retry the request when the open metadata services are available.


----

### CONNECTED-ASSET-SERVICES-500-001

> The requested connector for connection named {0} has not been created.  The connection was provided by the OCF service running in OMAG Server {1} at {2}

|  |  |
|---|---|
| **Java constant** | `OCFServicesErrorCode.NULL_CONNECTOR_RETURNED` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot create a connector which means some of its services will not work.

**User action**

This problem is likely to be caused by an incorrect connection object.  Check the settings on the Connectionand correct if necessary.  If the connection is correct, contact the Egeria community for help.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
