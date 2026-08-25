<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# EngineHostServicesErrorCode

The EngineHostServicesErrorCode error code is used to define first failure data capture (FFDC) for errors that occur when working with the Engine Host Services. It is used in conjunction with all exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 15 |
| **Message identifiers begin** | `ENGINE-HOST-SERVICES-400-` |
| **Java class** | `org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesErrorCode` |
| **Module** | [open-metadata-implementation/governance-server-services/engine-host-services/engine-host-services-api](../../open-metadata-implementation/governance-server-services/engine-host-services/engine-host-services-api) |
| **Source** | [EngineHostServicesErrorCode.java](../../open-metadata-implementation/governance-server-services/engine-host-services/engine-host-services-api/src/main/java/org/odpi/openmetadata/governanceservers/enginehostservices/ffdc/EngineHostServicesErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/engine-host-services/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [ENGINE-HOST-SERVICES-400-001](#engine-host-services-400-001) | 400 | Engine host {0} has been passed a null configuration document section for the engine host services |
| [ENGINE-HOST-SERVICES-400-002](#engine-host-services-400-002) | 400 | Engine host {0} is not configured with any engine services |
| [ENGINE-HOST-SERVICES-400-003](#engine-host-services-400-003) | 400 | The engine host services are unable to initialize a new instance of engine host {0}; exception {1} with message {2} |
| [ENGINE-HOST-SERVICES-400-004](#engine-host-services-400-004) | 400 | Engine service with URL marker {0} is not registered in the engine host {1} |
| [ENGINE-HOST-SERVICES-400-005](#engine-host-services-400-005) | 400 | Governance engine named {0} is not running in the engine host {1} |
| [ENGINE-HOST-SERVICES-400-006](#engine-host-services-400-006) | 400 | No governance engines are running in the engine host {0} |
| [ENGINE-HOST-SERVICES-400-007](#engine-host-services-400-007) | 400 | No governance engines are running in the engine service {0} on engine host {1} |
| [ENGINE-HOST-SERVICES-400-013](#engine-host-services-400-013) | 400 | The engine service {0} has been configured with a null admin class in engine host {1} |
| [ENGINE-HOST-SERVICES-400-014](#engine-host-services-400-014) | 400 | The engine service {0} has been configured with an admin class of {1} which can not be used by the class loader.  The {2} exception was returned with message {3} |
| [ENGINE-HOST-SERVICES-400-016](#engine-host-services-400-016) | 400 | Method {0} can not execute in the governance engine {1} hosted by engine host server {2} because the associated governance service properties are invalid: {3} |
| [ENGINE-HOST-SERVICES-400-017](#engine-host-services-400-017) | 400 | Engine host server {0} cannot pass a governance request to governance engine {1} because this governance engine has not retrieved its configuration from the metadata access server |
| [ENGINE-HOST-SERVICES-400-019](#engine-host-services-400-019) | 400 | The configuration document for engine {0} configuration property for engine service {1} in engine host {2} is null |
| [ENGINE-HOST-SERVICES-400-020](#engine-host-services-400-020) | 400 | The engine host services in engine host server {0} are unable to initialize a new instance of engine service {1}; exception {2} with message {3} |
| [ENGINE-HOST-SERVICES-400-023](#engine-host-services-400-023) | 400 | Properties for governance engine called {0} have not been returned by open metadata server {1} to engine host services in server {2} |
| [ENGINE-HOST-SERVICES-400-024](#engine-host-services-400-024) | 400 | Governance engine {0} defined in open metadata server {1} is of type {2} rather than {3}; engine host server {4} is not able to run requests for this governance engine |

----

### ENGINE-HOST-SERVICES-400-001

> Engine host {0} has been passed a null configuration document section for the engine host services

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.NO_CONFIG_DOC` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The engine host services can not retrieve its configuration values.  The hosting engine host server fails to start.

**User action**

This is an internal logic error since the admin services should not have initialized the engine host services without this section of the configuration document filled in.  Raise an issue to get this fixed.


----

### ENGINE-HOST-SERVICES-400-002

> Engine host {0} is not configured with any engine services

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.NO_ENGINE_SERVICES_CONFIGURED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The engine host, fails to start because it would be bored with nothing to do.

**User action**

Add the configuration for at least one engine service to the engine services' section of this engine host's configuration document and then restart the engine host server.


----

### ENGINE-HOST-SERVICES-400-003

> The engine host services are unable to initialize a new instance of engine host {0}; exception {1} with message {2}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.SERVICE_INSTANCE_FAILURE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The engine host services detected an error during the start up of a specific engine host instance.  No engine services are running in the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the engine host.


----

### ENGINE-HOST-SERVICES-400-004

> Engine service with URL marker {0} is not registered in the engine host {1}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.UNKNOWN_ENGINE_SERVICE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The engine service specified on a request is not known to the engine host.

**User action**

This may be a configuration error in the engine host or an error in the caller.  The supported engine services are listed in the engine host's configuration.  Check the configuration document for the server and then its start up messages to ensure the correct engine services are started.  Look for other error messages that indicate that an error occurred during start up.  If the engine host is running the correct engine services then validate that the caller has passed the correct URL marker of the engine service to the engine host.If all of this is correct then it may be a code error in the engine host services and you need to raise an issue to get it fixed.  Once the cause is resolved, retry the request.


----

### ENGINE-HOST-SERVICES-400-005

> Governance engine named {0} is not running in the engine host {1}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.UNKNOWN_ENGINE_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance engine specified on a request is not known to the engine service.

**User action**

This may be a configuration error in the engine host or an error in the caller.  The supported integration connectors are listed in the engine service's configuration.  Check the configuration document for the daemon and then its start up messages to ensure the correct engine services and connectors are started successfully.  Look for other error messages that indicate that an error occurred during start up.  If the engine host is running the correct engine services then validate that the caller has passed matching connector name and URL marker of the engine service to the engine host.If all of this is correct then it may be a code error in the engine host services and you need to raise an issue to get it fixed.  Once the cause is resolved, retry the request.


----

### ENGINE-HOST-SERVICES-400-006

> No governance engines are running in the engine host {0}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.NO_GOVERNANCE_ENGINES` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The call to the engine host fails and an exception is returned to the caller.

**User action**

This is either a configuration error or a logic error.  If this is a configuration error, theengine host will have logged detailed messages to the audit log to describe what is wrong and how to fix it.  If there are no errors in the configuration, raise an issue to get help to fix this.


----

### ENGINE-HOST-SERVICES-400-007

> No governance engines are running in the engine service {0} on engine host {1}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.NO_ENGINES_FOR_SERVICE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The call to the engine service fails and an exception is returned to the caller.

**User action**

This is either a configuration error or a logic error.  If this is a configuration error, theengine host will have logged detailed messages to the audit log when it was initializing the engine service to describe what is wrong and how to fix it.  If there are no errors in the configuration, raise an issue to get help to fix this.


----

### ENGINE-HOST-SERVICES-400-013

> The engine service {0} has been configured with a null admin class in engine host {1}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.NULL_ENGINE_SERVICE_ADMIN_CLASS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The engine service fails to start because the engine host can not initialize it.

**User action**

Each engine service registers itself using a static method call with the engine host astheir classes are loaded into the platform.  This is driven by the component scan for REST APIs implemented by the spring modules by the platform-chassis-spring module.  Ensure the engine service registers itself with the engine-host-services module and the platform-chassis-spring module has access to the engine service's spring module.


----

### ENGINE-HOST-SERVICES-400-014

> The engine service {0} has been configured with an admin class of {1} which can not be used by the class loader.  The {2} exception was returned with message {3}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.BAD_ENGINE_SERVICE_ADMIN_CLASS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The engine service fails to start.  Its governance engines, if any, are not activated.

**User action**

Check that the jar containing the engine service's admin class is visible to the OMAG Server Platform through the class path - and that the class name specified includes the full, correct package name and class name.  Once the class is correctly set up, restart the engine host.  It will be necessary to restart the OMAG Server Platform if the class path needed adjustment.


----

### ENGINE-HOST-SERVICES-400-016

> Method {0} can not execute in the governance engine {1} hosted by engine host server {2} because the associated governance service properties are invalid: {3}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.NULL_GOVERNANCE_SERVICE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The governance request is not run and an error is returned to the caller.

**User action**

This may be an error in the governance engine's logic or the Governance Engine OMAS may have returned invalid configuration.  Raise an issue to get help to fix it


----

### ENGINE-HOST-SERVICES-400-017

> Engine host server {0} cannot pass a governance request to governance engine {1} because this governance engine has not retrieved its configuration from the metadata access server

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.GOVERNANCE_ENGINE_NOT_INITIALIZED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance engine is not able to run any governance requests until it is able to retrieve its configuration.

**User action**

Use the configuration interface of the Governance Engine OMAS to create a definition of at least one governance engine.


----

### ENGINE-HOST-SERVICES-400-019

> The configuration document for engine {0} configuration property for engine service {1} in engine host {2} is null

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.NULL_SERVICE_CONFIG_VALUE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The engine service fails to start and this causes the hosting engine host to fail.

**User action**

Add a suitable value for this configuration property in the engine service configuration.


----

### ENGINE-HOST-SERVICES-400-020

> The engine host services in engine host server {0} are unable to initialize a new instance of engine service {1}; exception {2} with message {3}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The engine service detected an error during the start up of a specific governance engine instance.  Its governance services are not available.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### ENGINE-HOST-SERVICES-400-023

> Properties for governance engine called {0} have not been returned by open metadata server {1} to engine host services in server {2}

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.UNKNOWN_GOVERNANCE_ENGINE_CONFIG` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The engine host server is still not able to initialize the governance engine and so it will not be able to support governance requests targeted to this governance engine.

**User action**

This may be a configuration error or the metadata server may be down.  Look for other error messages and review the configuration of the engine host.  Once the cause is resolved, restart the server.


----

### ENGINE-HOST-SERVICES-400-024

> Governance engine {0} defined in open metadata server {1} is of type {2} rather than {3}; engine host server {4} is not able to run requests for this governance engine

|  |  |
|---|---|
| **Java constant** | `EngineHostServicesErrorCode.WRONG_TYPE_OF_GOVERNANCE_ENGINE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The governance engine has been associated with the wrong type of Open Metadata Engine Services (OMES) and so it will not de able to support governance requests targeted to this governance engine.

**User action**

This is a configuration error.  Update the configuration for the engine host service to ensure governance engines are correctly matched to the engine services.  Once the cause is resolved, restart the server.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
