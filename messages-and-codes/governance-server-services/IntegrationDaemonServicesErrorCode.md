<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# IntegrationDaemonServicesErrorCode

The IntegrationDaemonServicesErrorCode error code is used to define first failure data capture (FFDC) for errors that occur when working with the Integration Daemon Services. It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 8 |
| **Message identifiers begin** | `INTEGRATION-DAEMON-SERVICES-400-` |
| **Java class** | `org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode` |
| **Module** | [open-metadata-implementation/governance-server-services/integration-daemon-services/integration-daemon-services-api](../../open-metadata-implementation/governance-server-services/integration-daemon-services/integration-daemon-services-api) |
| **Source** | [IntegrationDaemonServicesErrorCode.java](../../open-metadata-implementation/governance-server-services/integration-daemon-services/integration-daemon-services-api/src/main/java/org/odpi/openmetadata/governanceservers/integrationdaemonservices/ffdc/IntegrationDaemonServicesErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/integration-daemon-services/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [INTEGRATION-DAEMON-SERVICES-400-001](#integration-daemon-services-400-001) | 400 | Integration daemon {0} has been passed a null configuration document section for the integration daemon services |
| [INTEGRATION-DAEMON-SERVICES-400-003](#integration-daemon-services-400-003) | 400 | Integration service {0} is not configured with the platform URL root for the {1} |
| [INTEGRATION-DAEMON-SERVICES-400-004](#integration-daemon-services-400-004) | 400 | Integration service {0} is not configured with the name for the server running the {1} |
| [INTEGRATION-DAEMON-SERVICES-400-009](#integration-daemon-services-400-009) | 400 | The integration daemon services are unable to initialize a new instance of integration daemon {0}; error message is {1} |
| [INTEGRATION-DAEMON-SERVICES-400-031](#integration-daemon-services-400-031) | 400 | Integration connector named {0} is not running in the integration daemon {1} |
| [INTEGRATION-DAEMON-SERVICES-400-032](#integration-daemon-services-400-032) | 400 | Properties for integration group called {0} have not been returned by open metadata server {1} to the integration daemon services in server {2} |
| [INTEGRATION-DAEMON-SERVICES-400-034](#integration-daemon-services-400-034) | 400 | Integration group named {0} is not running in the integration daemon {1} |
| [INTEGRATION-DAEMON-SERVICES-400-035](#integration-daemon-services-400-035) | 400 | No integration groups are running in the integration daemon {0} |

----

### INTEGRATION-DAEMON-SERVICES-400-001

> Integration daemon {0} has been passed a null configuration document section for the integration daemon services

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesErrorCode.NO_CONFIG_DOC` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The integration daemon services can not retrieve any configuration values.  The integration daemon fails to start because it would be bored with nothing to do.

**User action**

Add the configuration for at least one integration service or integration group to this integration daemon's configuration document and then restart the integration daemon.


----

### INTEGRATION-DAEMON-SERVICES-400-003

> Integration service {0} is not configured with the platform URL root for the {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesErrorCode.NO_OMAS_SERVER_URL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration service is not able to locate the server where its partner OMAS is running in order to exchange metadata.  The integration daemon server fails to start.

**User action**

To be successful each integration service needs both the platform URL root and the name of the server there the OMAS is running as well as the list of connections for the connectors it is to manage. Add this configuration to the integration daemon's configuration document and check that the other required configuration properties are in place. Then restart the integration daemon server.


----

### INTEGRATION-DAEMON-SERVICES-400-004

> Integration service {0} is not configured with the name for the server running the {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesErrorCode.NO_OMAS_SERVER_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration service is not able to locate the metadata server where its partner OMAS is running in order to exchange metadata.  The integration daemon fails to start.

**User action**

Add the configuration for the server name for this integration service to the integration daemon's configuration document.  Ensure that the platform URL root points to the platform where the metadata server is running and thatthere is at least one connection for an integration connector listed.  Once the configuration document is set up correctly, restart the integration daemon.


----

### INTEGRATION-DAEMON-SERVICES-400-009

> The integration daemon services are unable to initialize a new instance of integration daemon {0}; error message is {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesErrorCode.SERVICE_INSTANCE_FAILURE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration daemon services detected an error during the start up of a specific integration daemon instance.  No integration services are running in the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the integration daemon.


----

### INTEGRATION-DAEMON-SERVICES-400-031

> Integration connector named {0} is not running in the integration daemon {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesErrorCode.UNKNOWN_CONNECTOR_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration connector specified on a request is not known to the integration daemon.

**User action**

This may be a configuration error in the integration daemon or an error in the caller.  The supported integration connectors may be listed in the integration service's configurationor be part of one of the configured integration groups.  Check the configuration document for the daemon and then its start up messages to ensure the correct integration services and connectors are started successfully.  Look for other error messages that indicate that an error occurred during start up.  If the integration daemon is running the correct integration services then validate that the caller has passed matching connector name and URL marker of the integration service to the integration daemon.If all of this is correct then it may be a code error in the integration daemon services and you need to raise an issue to get it fixed.  Once the cause is resolved, retry the request.


----

### INTEGRATION-DAEMON-SERVICES-400-032

> Properties for integration group called {0} have not been returned by open metadata server {1} to the integration daemon services in server {2}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesErrorCode.UNKNOWN_INTEGRATION_GROUP_CONFIG` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration daemon is still not able to initialize the integration group and so it will not be able to support any integration connectors for this group.

**User action**

This may be a configuration error or the metadata server may be down.  Look for other error messages and review the configuration of the integration daemon.  If the name of the group needs to change, restart the server.


----

### INTEGRATION-DAEMON-SERVICES-400-034

> Integration group named {0} is not running in the integration daemon {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesErrorCode.UNKNOWN_GROUP_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration group specified on a request is not known to the integration daemon.

**User action**

This may be a configuration error in the integration daemon or an error in the caller.  The supported integration groups are listed in the integration daemon's configuration.  Check the configuration document for the daemon and then its start up messages to ensure the correct integration groups are started successfully.  Look for other error messages that indicate that an error occurred during start up.  If the integration daemon is running the correct integration groups then validate that the caller has passed the correct name.If all of this is correct then it may be a code error in the integration daemon services and you need to raise an issue to get it fixed.  Once the cause is resolved, retry the request.


----

### INTEGRATION-DAEMON-SERVICES-400-035

> No integration groups are running in the integration daemon {0}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesErrorCode.NO_INTEGRATION_GROUPS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The call to the integration daemon fails and an exception is returned to the caller.

**User action**

This is either a configuration error or a logic error.  If this is a configuration error, add the required integration groups to the configuration document.  If there are no errors in the configuration, raise an issue to get help to fix this.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
