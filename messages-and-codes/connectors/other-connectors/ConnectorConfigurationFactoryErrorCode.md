<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ConnectorConfigurationFactoryErrorCode

The ConnectorConfigurationFactoryErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the ConnectorConfigurationFactory. It is used in conjunction with all Exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `CONNECTOR-CONFIGURATION-FACTORY-400-` |
| **Java class** | `org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactoryErrorCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/connector-configuration-factory](../../../open-metadata-implementation/adapters/open-connectors/connector-configuration-factory) |
| **Source** | [ConnectorConfigurationFactoryErrorCode.java](../../../open-metadata-implementation/adapters/open-connectors/connector-configuration-factory/src/main/java/org/odpi/openmetadata/adapters/repositoryservices/ConnectorConfigurationFactoryErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/connection/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [CONNECTOR-CONFIGURATION-FACTORY-400-001](#connector-configuration-factory-400-001) | 400 | Connector Provider class name {0} (or a dependent library class) is not available to this runtime. Check that the appropriate jar files are included in the runtime classpath (often specified via the loader.path option); also check that the loader.path value is correct. Class loader exception was {1} with message {2} |
| [CONNECTOR-CONFIGURATION-FACTORY-400-002](#connector-configuration-factory-400-002) | 400 | Connector Provider class name {0} does not inherit from 'org.odpi.openmetadata.frameworks.connectors.ConnectorProvider' |

----

### CONNECTOR-CONFIGURATION-FACTORY-400-001

> Connector Provider class name {0} (or a dependent library class) is not available to this runtime. Check that the appropriate jar files are included in the runtime classpath (often specified via the loader.path option); also check that the loader.path value is correct. Class loader exception was {1} with message {2}

|  |  |
|---|---|
| **Java constant** | `ConnectorConfigurationFactoryErrorCode.UNKNOWN_CONNECTOR_PROVIDER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot create the requested connector type because the Connector Provider's class is failing to initialize in the JVM.  This has resulted in an exception in the class loader.

**User action**

Update the configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request.


----

### CONNECTOR-CONFIGURATION-FACTORY-400-002

> Connector Provider class name {0} does not inherit from 'org.odpi.openmetadata.frameworks.connectors.ConnectorProvider'

|  |  |
|---|---|
| **Java constant** | `ConnectorConfigurationFactoryErrorCode.INVALID_CONNECTOR_PROVIDER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot create the requested connector type because the supplied Connector Provider class is not implemented correctly.

**User action**

Update the configuration to include a valid connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
