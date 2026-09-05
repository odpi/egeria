<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# IntegrationDaemonServicesAuditCode

The IntegrationDaemonServicesAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 36 |
| **Message identifiers begin** | `INTEGRATION-DAEMON-SERVICES-` |
| **Java class** | `org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode` |
| **Module** | [open-metadata-implementation/governance-server-services/integration-daemon-services/integration-daemon-services-api](../../open-metadata-implementation/governance-server-services/integration-daemon-services/integration-daemon-services-api) |
| **Source** | [IntegrationDaemonServicesAuditCode.java](../../open-metadata-implementation/governance-server-services/integration-daemon-services/integration-daemon-services-api/src/main/java/org/odpi/openmetadata/governanceservers/integrationdaemonservices/ffdc/IntegrationDaemonServicesAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/integration-daemon-services/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [INTEGRATION-DAEMON-SERVICES-0001](#integration-daemon-services-0001) | STARTUP | The integration daemon services are initializing in server {0} |
| [INTEGRATION-DAEMON-SERVICES-0003](#integration-daemon-services-0003) | ERROR | Integration daemon {0} is not configured with the platform URL root for its partner OMAS {1} |
| [INTEGRATION-DAEMON-SERVICES-0004](#integration-daemon-services-0004) | ERROR | Integration daemon {0} is not configured with the name for the server running its partner OMAS {1} |
| [INTEGRATION-DAEMON-SERVICES-0008](#integration-daemon-services-0008) | STARTUP | A new integration connector named {0} is initializing in integration daemon {1}, permitted synchronization is: {2} |
| [INTEGRATION-DAEMON-SERVICES-0009](#integration-daemon-services-0009) | STARTUP | A new integration connector named {0} failed to initialize in integration daemon {1}.  The exception returned was {2} with a message of {3} |
| [INTEGRATION-DAEMON-SERVICES-0010](#integration-daemon-services-0010) | STARTUP | The connection for integration connector named {0} created a connector of class {1} which does not implement the correct {2} interface |
| [INTEGRATION-DAEMON-SERVICES-0012](#integration-daemon-services-0012) | ERROR | The integration daemon services are unable to initialize a new instance of integration daemon {0}; error message is {1} |
| [INTEGRATION-DAEMON-SERVICES-0013](#integration-daemon-services-0013) | STARTUP | The integration daemon {0} has initialized |
| [INTEGRATION-DAEMON-SERVICES-0015](#integration-daemon-services-0015) | INFO | User {0} has updated the following configuration properties for the integration connector {1} in integration daemon {2}: {3} |
| [INTEGRATION-DAEMON-SERVICES-0016](#integration-daemon-services-0016) | INFO | User {0} has cleared all the configuration properties for the integration connector {1} in integration daemon {2} |
| [INTEGRATION-DAEMON-SERVICES-0019](#integration-daemon-services-0019) | STARTUP | The integration daemon services has registered the configuration listener for server {0}.  It will receive configuration updates from metadata server {1} |
| [INTEGRATION-DAEMON-SERVICES-0020](#integration-daemon-services-0020) | SHUTDOWN | The integration daemon {0} is shutting down |
| [INTEGRATION-DAEMON-SERVICES-0026](#integration-daemon-services-0026) | SHUTDOWN | The integration daemon {0} has completed shutdown |
| [INTEGRATION-DAEMON-SERVICES-0027](#integration-daemon-services-0027) | EXCEPTION | The integration daemon services are unable to retrieve the connection for the configuration listener for server {0} from metadata server {1}. Exception returned was {2} with error message {3} |
| [INTEGRATION-DAEMON-SERVICES-0028](#integration-daemon-services-0028) | INFO | Unable to refresh configuration for integration group {0}.  The exception was {1} with an error message {2} |
| [INTEGRATION-DAEMON-SERVICES-0030](#integration-daemon-services-0030) | STARTUP | The dedicated thread for integration connector {0} has started in integration daemon {1} |
| [INTEGRATION-DAEMON-SERVICES-0031](#integration-daemon-services-0031) | EXCEPTION | The integration connector {0} method {1} has returned with a {2} exception containing message {3} |
| [INTEGRATION-DAEMON-SERVICES-0034](#integration-daemon-services-0034) | INFO | The integration connector {0} has returned from the engage() method in integration daemon {1} |
| [INTEGRATION-DAEMON-SERVICES-0035](#integration-daemon-services-0035) | SHUTDOWN | The dedicated thread for integration connector {0} is terminating in integration daemon {1} |
| [INTEGRATION-DAEMON-SERVICES-0036](#integration-daemon-services-0036) | ERROR | The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to connect to the associated metadata access server |
| [INTEGRATION-DAEMON-SERVICES-0037](#integration-daemon-services-0037) | ERROR | The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to create and initialize a connector |
| [INTEGRATION-DAEMON-SERVICES-0041](#integration-daemon-services-0041) | INFO | Integration connector {0} is refreshing for the first time in the {1} integration daemon |
| [INTEGRATION-DAEMON-SERVICES-0042](#integration-daemon-services-0042) | INFO | Integration connector {0} is refreshing again in {1} integration daemon |
| [INTEGRATION-DAEMON-SERVICES-0043](#integration-daemon-services-0043) | INFO | The integration connector {0} in integration daemon {1} has completed refresh processing in {2} millisecond(s) |
| [INTEGRATION-DAEMON-SERVICES-0050](#integration-daemon-services-0050) | SECURITY | Integration service {0} is not authorized to call its partner OMAS running in integration daemon {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4} |
| [INTEGRATION-DAEMON-SERVICES-0051](#integration-daemon-services-0051) | INFO | All integration connector configuration is being refreshed for integration group {0} |
| [INTEGRATION-DAEMON-SERVICES-0053](#integration-daemon-services-0053) | EXCEPTION | Failed to process a change to integration group {0}.  The exception was {1} with error message {2} |
| [INTEGRATION-DAEMON-SERVICES-0054](#integration-daemon-services-0054) | EXCEPTION | Failed to process a change to integration connector {0}.  The exception was {1} with error message {2} |
| [INTEGRATION-DAEMON-SERVICES-0056](#integration-daemon-services-0056) | INFO | Refresh of all integration connector configuration has completed for integration group {0} |
| [INTEGRATION-DAEMON-SERVICES-0057](#integration-daemon-services-0057) | INFO | User {0} has updated the endpoint network address for the integration connector {1} in integration daemon {2} to: {3} |
| [INTEGRATION-DAEMON-SERVICES-0058](#integration-daemon-services-0058) | ERROR | User {0} has attempted to update the endpoint network address for the integration connector {1} in integration daemon {2} to {3} but this connector does not have an endpoint defined |
| [INTEGRATION-DAEMON-SERVICES-0060](#integration-daemon-services-0060) | STARTUP | The integration connector refresh thread for integration connector {0} has started |
| [INTEGRATION-DAEMON-SERVICES-0064](#integration-daemon-services-0064) | SHUTDOWN | The integration connector refresh thread for integration connector {0} is shutting down |
| [INTEGRATION-DAEMON-SERVICES-0066](#integration-daemon-services-0066) | INFO | The registration of integration connector {0} in integration daemon {1} has changed ({2}); the connector is being reinitialized |
| [INTEGRATION-DAEMON-SERVICES-0067](#integration-daemon-services-0067) | INFO | The registration of integration connector {0} in integration daemon {1} has changed ({2}) while the connector is refreshing; it will be reinitialized when the refresh completes |
| [INTEGRATION-DAEMON-SERVICES-0065](#integration-daemon-services-0065) | EXCEPTION | The integration connector refresh thread for integration connector {0} caught a {1} exception containing message {2} |

----

### INTEGRATION-DAEMON-SERVICES-0001

> The integration daemon services are initializing in server {0}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.SERVER_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

A new OMAG server has been started that is configured to run as an integration daemon.  Within the integration daemon are one or more dynamic integration groups that host integration connectors to exchange metadata with third party technologies.

**User action**

Verify that the start up sequence goes on to initialize the configured integration services.


----

### INTEGRATION-DAEMON-SERVICES-0003

> Integration daemon {0} is not configured with the platform URL root for its partner OMAS {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.NO_OMAS_SERVER_URL` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The service is not able to connect to the open metadata ecosystem.  It fails to start.

**User action**

Add the platform URL root of the OMAG server where the partner OMAS is running to this integration service's configuration.


----

### INTEGRATION-DAEMON-SERVICES-0004

> Integration daemon {0} is not configured with the name for the server running its partner OMAS {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.NO_OMAS_SERVER_NAME` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The service does not know which server to call for its partner OMAS.  It fails to start.

**User action**

Add the server name of the OMAG server where the partner OMAS is running to this integration service's configuration.


----

### INTEGRATION-DAEMON-SERVICES-0008

> A new integration connector named {0} is initializing in integration daemon {1}, permitted synchronization is: {2}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.INTEGRATION_CONNECTOR_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration daemon is initializing an integration connector using the information in the configured connection.

**User action**

Verify that this connector is successfully initialized.


----

### INTEGRATION-DAEMON-SERVICES-0009

> A new integration connector named {0} failed to initialize in integration daemon {1}.  The exception returned was {2} with a message of {3}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.BAD_INTEGRATION_CONNECTION` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The integration service fails to initialize.  This, in turn causes the integration daemon to fail to start.

**User action**

Correct the connection for this integration connector in the integration service's section of this integration daemon's configuration document and then restart the integration daemon.


----

### INTEGRATION-DAEMON-SERVICES-0010

> The connection for integration connector named {0} created a connector of class {1} which does not implement the correct {2} interface

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.NOT_INTEGRATION_CONNECTOR` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration service, and hence the hosting integration daemon, fails to start.

**User action**

Change the connection in the integration service's section of this integration daemon's configuration document to a valid integration connector and then restart the integration daemon.


----

### INTEGRATION-DAEMON-SERVICES-0012

> The integration daemon services are unable to initialize a new instance of integration daemon {0}; error message is {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.SERVICE_INSTANCE_FAILURE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration daemon services detected an error during the start up of a specific integration daemon instance.  Its integration services are not available.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the integration daemon.


----

### INTEGRATION-DAEMON-SERVICES-0013

> The integration daemon {0} has initialized

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.SERVER_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The integration daemon services has completed initialization.

**User action**

Verify that all the configured integration services, and their connectors within have successfully started andare able to connect both to their third party technology and their partner OMAS.


----

### INTEGRATION-DAEMON-SERVICES-0015

> User {0} has updated the following configuration properties for the integration connector {1} in integration daemon {2}: {3}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_CONFIG_PROPS_UPDATE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will be restarted once the new properties are in place.

**User action**

Ensure that the connector does not report any errors during the restart processing as it operates using its new properties.


----

### INTEGRATION-DAEMON-SERVICES-0016

> User {0} has cleared all the configuration properties for the integration connector {1} in integration daemon {2}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_CONFIG_PROPS_CLEARED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will be restarted once the properties are cleared.

**User action**

Ensure that the connector does not report any errors during the restart processing as it operated on its default properties.


----

### INTEGRATION-DAEMON-SERVICES-0019

> The integration daemon services has registered the configuration listener for server {0}.  It will receive configuration updates from metadata server {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.CONFIGURATION_LISTENER_REGISTERED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration daemon continues to run.  The integration daemon services will start up the integration groups and they will operate with whatever configuration that they can retrieve.  Periodically the integration daemon services willretry the request to retrieve the integration connector configuration associated with the group and activate/deactivate the requested integration connectors as requested.

**User action**

Ensure the configuration for the integration connectors is attached to the integration group(s) configured for this integration daemon.


----

### INTEGRATION-DAEMON-SERVICES-0020

> The integration daemon {0} is shutting down

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.SERVER_SHUTTING_DOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this integration daemon server.

**User action**

Verify that this server is no longer needed and the shutdown is expected.


----

### INTEGRATION-DAEMON-SERVICES-0026

> The integration daemon {0} has completed shutdown

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.SERVER_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local administrator has requested shut down of this integration daemon server and the operation has completed.

**User action**

Verify that all integration connectors that support the metadata exchange have shut down successfully.


----

### INTEGRATION-DAEMON-SERVICES-0027

> The integration daemon services are unable to retrieve the connection for the configuration listener for server {0} from metadata server {1}. Exception returned was {2} with error message {3}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.NO_CONFIGURATION_LISTENER` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server continues to run.  The engine host services will start up the integration services and they will operate with whatever configuration that they can retrieve.  Periodically the integration daemon services willretry the request to retrieve the connection information.  Without the connection, the integration daemon services will not be notified of changes to the integration groups' configuration

**User action**

This problem may be caused because the integration daemon services has been configured with the wrong location for the metadata server, or the metadata server is not running the Governance Engine OMAS service or the metadata server is not running at all.  Investigate the status of the metadata server to ensure it is running and correctly configured.  Once it is ready, either restart the server, or issue the refresh-config command or wait for the engine host services to retry the configuration request.


----

### INTEGRATION-DAEMON-SERVICES-0028

> Unable to refresh configuration for integration group {0}.  The exception was {1} with an error message {2}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.INTEGRATION_GROUP_NO_CONFIG` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration group cannot process any integration connector requests until its configuration can be retrieved.

**User action**

Review the error messages and resolve the cause of the problem.  Either wait for the integration daemon services to refresh the configuration, or issue the refreshConfig call to request that the integration group calls the Governance Engine OMAS to refresh the configuration for the integration group.


----

### INTEGRATION-DAEMON-SERVICES-0030

> The dedicated thread for integration connector {0} has started in integration daemon {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.CONNECTOR_THREAD_STARTING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server will call the integration connector's engage() method to indicate that it can issue blocking calls.

**User action**

Ensure that the connector is running successfully.


----

### INTEGRATION-DAEMON-SERVICES-0031

> The integration connector {0} method {1} has returned with a {2} exception containing message {3}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.CONNECTOR_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server will change the integration connector's status to failed.

**User action**

Use the message from the exception and knowledge of the integration connector's behavior to track down and resolve the cause of the error and then restart the connector.


----

### INTEGRATION-DAEMON-SERVICES-0034

> The integration connector {0} has returned from the engage() method in integration daemon {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.ENGAGE_RETURNED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration daemon created a separate thread for this connector to enable it to issue blocking calls.  It called the engage() method on this thread.  The engage() method has returned which means the connector has finished its processing of a single blocking call.  The integration daemon will wait one minute and then call engage() again unless the server is shutting down.

**User action**

Verify that the connector is not reporting errors which have caused it to terminate prematurely.


----

### INTEGRATION-DAEMON-SERVICES-0035

> The dedicated thread for integration connector {0} is terminating in integration daemon {1}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.CONNECTOR_THREAD_TERMINATING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The integration daemon created a separate thread for this connector to enable it to issue blocking calls.  The integration daemon is shutting down and has requests that the dedicated thread for this connector terminates.

**User action**

Verify that there are no errors as the thread terminates.  In particular, if the thread detectsshutdown after the integration daemon has completed, there should still be an orderly shutdown of the connector.


----

### INTEGRATION-DAEMON-SERVICES-0036

> The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to connect to the associated metadata access server

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.INITIALIZE_ERROR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server will change the integration connector's status to Initialize Failed.  It will retry the call to the metadata server during each refresh() call until the metadata server is contacted.

**User action**

Check the status of the associated metadata server - it may need restarting.  Alternatively, the integration connector may be configured with the wrong metadata server, in which case the integration connector's configuration needs updating and the integration daemon will need restarting.  If neither of these are the cause of the problem, use the message from the exception and knowledge of the open metadata landscape to track down and resolve the cause of the error and then restart the connector.


----

### INTEGRATION-DAEMON-SERVICES-0037

> The integration service {0} method {1} has returned with a {2} exception containing message {3} when attempting to create and initialize a connector

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.CONFIG_ERROR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server will change the integration connector's status to Configuration Failed.  It will ignore the connector during each refresh() call until the connector is restarted with workable configuration.

**User action**

Check the configuration of the connector.


----

### INTEGRATION-DAEMON-SERVICES-0041

> Integration connector {0} is refreshing for the first time in the {1} integration daemon

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_FIRST_REFRESH` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The thread is about to call refresh() on the integration connector hosted in this daemon for the first time.

**User action**

Ensure that the connector does not report any errors during the refresh processing.


----

### INTEGRATION-DAEMON-SERVICES-0042

> Integration connector {0} is refreshing again in {1} integration daemon

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_REFRESH` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The thread is about to call refresh() on the integration connector hosted in this daemon.

**User action**

No action is required, but the interval between these messages shows how often this connector is refreshing.


----

### INTEGRATION-DAEMON-SERVICES-0043

> The integration connector {0} in integration daemon {1} has completed refresh processing in {2} millisecond(s)

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_REFRESH_COMPLETE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The to call refresh() has returned.

**User action**

Verify that the time between refresh calls is appropriate for the connector.


----

### INTEGRATION-DAEMON-SERVICES-0050

> Integration service {0} is not authorized to call its partner OMAS running in integration daemon {1} on OMAG Server Platform {2} with userId {3}.  The error message was: {4}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.SERVER_NOT_AUTHORIZED` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

Some, or all the metadata from the connected third party technologies can not be exchanged with the openmetadata ecosystem.

**User action**

The userId comes from the integration daemon's configuration document.  It is stored as the localServerUserId.  The authorization failure may be limited to a single operation, or extend to all requests to a specific partner OMAS, specific metadata elements or an entire metadata access point or metadata server.  Diagnose the extent of the authorization failure.  Then either turn off the integration services that are not permitted or ensure the integration's userId has sufficient access.  If one of the integration connectors needs unusually permissive access, you could consider isolating it in its own integration daemon that has a more powerful userId, leaving the rest of the integration connectors working with the current userId.


----

### INTEGRATION-DAEMON-SERVICES-0051

> All integration connector configuration is being refreshed for integration group {0}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.CLEARING_ALL_INTEGRATION_CONNECTOR_CONFIG` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The integration daemon services will call the Governance Engine OMAS in the metadata server to retrieve details of all the integration connectors configured for this integration group.During this process, some refresh requests may fail if the associated integrationconnector is only partially configured.

**User action**

Monitor the integration daemon services to ensure all the integration connectors are retrieved. Then it is ready to process new refresh requests.


----

### INTEGRATION-DAEMON-SERVICES-0053

> Failed to process a change to integration group {0}.  The exception was {1} with error message {2}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.GROUP_CHANGE_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration daemon cannot process the change to a governance group.  The exception explains the reason.

**User action**

Review the error messages and resolve the cause of the problem.  Once resolved, it is possible to refresh the configuration of the integration group by calling the integration daemon's refreshConfig service.


----

### INTEGRATION-DAEMON-SERVICES-0054

> Failed to process a change to integration connector {0}.  The exception was {1} with error message {2}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.CONNECTOR_CHANGE_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration daemon cannot process the change to a integration connector.  The exception explains the reason.

**User action**

Review the error messages and resolve the cause of the problem.  Once resolved, it is possible to refresh the configuration of this integration connector by calling the integration daemon's refreshConfig service.


----

### INTEGRATION-DAEMON-SERVICES-0056

> Refresh of all integration connector configuration has completed for integration group {0}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.FINISHED_ALL_INTEGRATION_CONNECTOR_CONFIG` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The integration connectors for this integration group are running with the latest configuration.

**User action**

No action is required as long as all the expected integration connectors are started.If there are any errors reported by the integration connectors then validate the configuration of the integration connector and its associated integration group in the metadata server.


----

### INTEGRATION-DAEMON-SERVICES-0057

> User {0} has updated the endpoint network address for the integration connector {1} in integration daemon {2} to: {3}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_ENDPOINT_UPDATE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will be restarted once the new endpoint network address is in place.

**User action**

Ensure that the connector does not report any errors during the restart processing as it connects to the new endpoint.


----

### INTEGRATION-DAEMON-SERVICES-0058

> User {0} has attempted to update the endpoint network address for the integration connector {1} in integration daemon {2} to {3} but this connector does not have an endpoint defined

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.DAEMON_CONNECTOR_NO_ENDPOINT_TO_UPDATE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector continues to operate as before.

**User action**

If the connector should have an endpoint then update the whole connection for the connector.


----

### INTEGRATION-DAEMON-SERVICES-0060

> The integration connector refresh thread for integration connector {0} has started

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.REFRESH_THREAD_STARTING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The thread will periodically call refresh() on the integration connector.  The time between each refresh is set up in the configuration for the integration connector.

**User action**

Ensure that the integration connector is running successfully.


----

### INTEGRATION-DAEMON-SERVICES-0064

> The integration connector refresh thread for integration connector {0} is shutting down

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.REFRESH_THREAD_TERMINATING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The thread will stop calling refresh() on the integration connectors hosted in this daemon and stop running.

**User action**

Ensure that the thread terminates without errors.


----

### INTEGRATION-DAEMON-SERVICES-0066

> The registration of integration connector {0} in integration daemon {1} has changed ({2}); the connector is being reinitialized

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.CONNECTOR_REGISTRATION_CHANGED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration daemon re-read the connector's registration in response to a change to the connector or its group and found it different from the one the running connector was built from.  The running connector is disconnected and a new one built from the new registration.

**User action**

No action is required if the registration was changed deliberately.  If it was not, compare the two connections recorded with this message to see what differed.


----

### INTEGRATION-DAEMON-SERVICES-0067

> The registration of integration connector {0} in integration daemon {1} has changed ({2}) while the connector is refreshing; it will be reinitialized when the refresh completes

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.CONNECTOR_REINITIALIZE_DEFERRED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

Reinitializing a connector disconnects it and starts a new instance.  Doing that while the current instance is part way through a refresh would leave two instances of the connector working at once, so the change is held until the refresh returns.

**User action**

No action is required.  The connector is reinitialized from the new registration as soon as its current refresh completes.


----

### INTEGRATION-DAEMON-SERVICES-0065

> The integration connector refresh thread for integration connector {0} caught a {1} exception containing message {2}

|  |  |
|---|---|
| **Java constant** | `IntegrationDaemonServicesAuditCode.REFRESH_THREAD_CONNECTOR_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration connector thread will revisit this connector at the next refresh time.

**User action**

Use the message from the exception and knowledge of the integration connector's behavior to track down and resolve the cause of the error and then restart the connector.  The integration connector refresh thread will then continue to call the connector.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
