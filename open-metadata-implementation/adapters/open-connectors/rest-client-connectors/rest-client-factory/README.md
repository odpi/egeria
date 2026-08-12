<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# REST Client Factory

`RESTClientFactory` builds the [`RESTClientConnector`](../rest-client-connectors-api) that an Egeria
client uses to call an OMAG Server's REST API. It hides two things from the caller:

1. **Authentication setup** - depending on which constructor is used, the factory wires up a bearer
   token secrets store, a basic-authentication secrets store, or an already-built map of secrets store
   connectors, so the connector can authenticate its calls without the caller needing to know how.
2. **Which REST client implementation to use** - the factory builds a `Connection` whose
   `connectorProviderClassName` points at
   [`JDKRESTClientConnectorProvider`](../jdk-rest-client-connector), and hands that `Connection` to a
   `ConnectorBroker` to instantiate. This is the **one place** in the codebase that decides which REST
   client implementation Egeria uses - see the [rest-client-connectors](..) README for why the design is
   deliberately centralised here.

```java
RESTClientFactory factory = new RESTClientFactory(serverName, serverPlatformURLRoot, secretsStoreConnectorMap, auditLog);
RESTClientConnector clientConnector = factory.getClientConnector();
```

To use the [Spring-based connector](../spring-rest-client-connector) instead, change the provider class
referenced in `RESTClientFactory` to `SpringRESTClientConnectorProvider` - no other code needs to change,
since every caller only ever depends on the `RESTClientConnector`/`RESTClientCalls` abstraction.

Return to [rest-client-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
