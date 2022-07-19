<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Designing and Implementing an Open Metadata Governance Server

When designing a Governance Server, you want to allow different technologies to plug-in.
Therefore, it is important to design the service so that it uses the
[Open Connector Framework](../../../frameworks/open-connector-framework)'s connectors.

In other words, your service needs to store or processes information, without tightly-coupling to
specific back-end technology providing that storage or processing. After all, technology preferences
will vary across organizations. One organization may prioritize speed, and another may prioritize cost.
Or one configuration may be useful for simple development and test purposes and another for large-scale
production deployment. Yet in all cases, you want to implement your service without needing to worry
about these differences. It should be up to the underlying technology to determine how best to provide
what your service requires.

**Open connectors** loosely-couple these back-end choices to the server. Implementing a service that uses
open connectors involves six steps:

1. Define the interface to which connectors need to adhere
1. Write administration services to configure the service
1. Create connector instances within your service
1. Run your service's operations on these connector instances
1. Bind your service into the Open Metadata and Governance (OMAG) platform
1. Ensure your connector implementation is in the server's classpath

Administration code should be lightweight and integrated into the OMAG administration modules of Egeria.
Your service itself, and the interfaces and base classes it relies on, should be part of their own code
module. To clarify, the modules used in the examples below illustrate this separation.

## 1. Define the interface to which connectors need to adhere

Start by defining the methods underlying technologies need to provide for your service to operate.

For example, the [Data Engine Proxy Services](../../data-engine-proxy-services) need to receive information
about the processing activities data engines carry out. Therefore, the [DataEngineInterface](../../data-engine-proxy-services/data-engine-proxy-connector/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/connectors/DataEngineInterface.java)
defines methods for retrieving changed processes, lineage mappings, ports and schema types from an underlying data
engine.

A service-specific module structure contains the base classes and interfaces: for example, the
data engine proxy services connector interface code is all under its own Maven module
`data-engine-proxy-connector` within the parent module `data-engine-proxy-services`. This is because the data engine
proxy services do not offer their own API. If they did, its interfaces and base classes might more logically be placed
in the same module as the API it offers, e.g. `data-engine-proxy-services-api` as a submodule under the parent
`data-engine-proxy-servicse` module.

## 2. Write administration services to configure the service

Next we need to be able to configure the service to use these underlying technologies. We must write an administration
service to configure this connectivity:

- Offer a REST API through the [OMAG admin services](../../../admin-services).
- Require configuration inputs, defined by a service-specific configuration object. To leverage the Open Connector
    Framework, this will be a generic [connection](../../../frameworks/open-connector-framework/docs/concepts/connection.md)
    object.

For example, the Data Engine Proxy Services define administrative REST API endpoints in [DataEngineProxyResource](../../../admin-services/admin-services-spring/src/main/java/org/odpi/openmetadata/adminservices/spring/DataEngineProxyResource.java).
There are endpoints for both storing the configuration and deleting the configuration. Also note the use of the
[DataEngineProxyConfig](../../../admin-services/admin-services-api/src/main/java/org/odpi/openmetadata/adminservices/configuration/properties/DataEngineProxyConfig.java)
object. This object defines the configuration needed for the Data Engine Proxy Services: a server name and URL to the
Data Engine OMAS as well as a generic connection object to the data engine itself.

### A generic connection object?

At this point you may be wondering why this is a generic connection object (`Connection`) and not a connection object
that is more specific to (in our example) data engines. The answer is two-fold:

- Firstly, note that this is a [connection](../../../frameworks/open-connector-framework/docs/concepts/connection.md)
    and not a [connector](../../../frameworks/open-connector-framework/docs/concepts/connector.md). A _connection_ only
    describes the properties needed to instantiate a _connector_. Such properties include the network address, protocol,
    user credentials, and any additional configuration properties needed by a _connector_ to connect to an underlying
    technology. As such, the _connection_ can be general: there is nothing _connector_-specific about such a list of
    properties.
- Secondly, by using this general mechanism we can ensure the [connector broker](../../../frameworks/open-connector-framework/docs/concepts/connector-broker.md)
    is able to understand all such connections without any dependency on the service-specific code or its
    connector-specific interfaces.

### Storing the configuration

Finally, note that all admin services REST API endpoint methods call into methods that persist the configuration
details into a server configuration document (via [OMAGServerDataEngineProxyService](../../../admin-services/admin-services-server/src/main/java/org/odpi/openmetadata/adminservices/OMAGServerDataEngineProxyService.java)
in our example). As a result, there is no need to reconfigure the services each time the server restarts.

Persisting the configuration requires a few more steps:

- The service-specific configuration object needs to be added as a subtype to [AdminServicesConfigHeader](../../../admin-services/admin-services-api/src/main/java/org/odpi/openmetadata/adminservices/configuration/properties/AdminServicesConfigHeader.java)
    (approximately line 30 for our example).
- Utility methods to save and retrieve the configuration object to the server configuration document need to be added
    to [OMAGServerConfig](../../../admin-services/admin-services-api/src/main/java/org/odpi/openmetadata/adminservices/configuration/properties/OMAGServerConfig.java)
    (approximately lines 110, 160, 600-610, 730, 750 for our example).

The OMAG admin Maven module (`admin-services`) contains all of the code for this step: the REST API definition,
configuration object code, configuration storage code, and revisions to the admin services and server config code.

## 3. Create connector instances within your service

Up to now we have defined the methods our service will use and how to configure the service with a connection to an
underlying technology. Next, we need to actually access that underlying technology and make use of it within our
service.

Define a class that implements your services' functionality. Continuing our previous example for the Data Engine Proxy
Services, the implementation is in [DataEngineProxyOperationalServices](../../data-engine-proxy-services/data-engine-proxy-services-server/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/admin/DataEngineProxyOperationalServices.java).
Typically you should implement an initialize method that:

- Receives the services-specific configuration object described in (2) above.
- Instantiates a _connector_ to connect to the underlying technology using a _connector broker_ and the connection
    object from the configuration.
    
Read through the `initialize` method code of [DataEngineProxyOperationalServices](../../data-engine-proxy-services/data-engine-proxy-services-server/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/admin/DataEngineProxyOperationalServices.java)
and you will see the creation of the:

- Data engine OMAS client, using the server name and URL from the configuration object (approximately line 130-140).
- Data engine connector, as an instance of the abstract [DataEngineConnectorBase](../../data-engine-proxy-services/data-engine-proxy-connector/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/connectors/DataEngineConnectorBase.java)
    class (approximately line 150-170). Note that a `ConnectorBroker` is used on the generic connection object (from
    the configuration object) to create the data engine connector. The _connector broker_ gives us a _connector_ to a
    real data engine based on the configuration parameters defined in the _connection_.

As with the base classes and interfaces, the service-specific Maven module contains the services implementation.
For example, the data engine proxy services code is all under the `data-engine-proxy-services-server` Maven module.
Furthermore, the server-side code is in the same parent module as the client-side connector code of step (1)
(`data-engine-proxy-services`).

## 4. Run your service's operations on those connector instances

We can implement the functionality of our service itself in the remainder of the operational services class
([DataEngineProxyOperationalServices](../../data-engine-proxy-services/data-engine-proxy-services-server/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/admin/DataEngineProxyOperationalServices.java)
and the threaded [DataEngineProxyService](../../data-engine-proxy-services/data-engine-proxy-services-server/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/processor/DataEngineProxyService.java)
it runs in our example).

Note that the implementation of the operational services (in particular within [DataEngineProxyService](../../data-engine-proxy-services/data-engine-proxy-services-server/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/processor/DataEngineProxyService.java))
is entirely defined using the general methods defined in our interface (the [DataEngineConnectorBase](../../data-engine-proxy-services/data-engine-proxy-connector/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/connectors/DataEngineConnectorBase.java)
class in our example). As a result, it is not tightly-coupled to any specific underlying technology. However, since we
obtain a real data engine connector from the connector broker, the functionality will actually execute against the data
engine itself.

Of course, as you implement the operational services class(es), you may find other methods you require: these should be
added to the interface / base class that you expect connectors to extend or implement. As long as those connectors
adhere to this interface, they can then be used to execute your service's functionality.

Again, the service-specific Maven module (`data-engine-proxy-services`) contains all of this code.

## 5. Bind your service into the OMAG platform

We then need to bind our service into the OMAG platform for it to actually run as part of Egeria. Without this step,
our operational services class(es) will never actually be called by Egeria itself.

Bind your service through the following two steps:

- Add a class member, getter and setter for your operational service to [OMAGOperationalServicesInstance](../../../admin-services/admin-services-server/src/main/java/org/odpi/openmetadata/adminservices/OMAGOperationalServicesInstance.java).
    In our example, the class member is `operationalDataEngineProxyServices`, an instance of
    [DataEngineProxyOperationalServices](../../data-engine-proxy-services/data-engine-proxy-services-server/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/admin/DataEngineProxyOperationalServices.java).
- Implement the service configuration retrieval, service instantiation, and service initialization of your operational
    service in [OMAGServerOperationalServices](../../../admin-services/admin-services-server/src/main/java/org/odpi/openmetadata/adminservices/OMAGServerOperationalServices.java).
    In our example these occur around line 160, 550, and 560 respectively for the data engine proxy services.

The OMAG admin services (`admin-services`) contain the code that needs to be enhanced. This creates a dependency on
your services' server-side implementation (e.g. `data-engine-proxy-services-server`), but this is necessary for your
service to run as part of the OMAG platform itself.

Now the OMAG platform will automatically run the data engine proxy service, against a real data engine, any time one
is configured.

## 6. Ensure any implemented connectors are in the server's classpath

Finally, the server can only load implemented connectors that are included in the server's classpath. Egeria's OMAG
platform by default uses Spring. When your connector implementation is built independently from Egeria itself, use
Spring's `loader.path` command-line argument or the `LOADER_PATH` environment variable to give the OMAG platform the
location of your compiled connector implementation at runtime.

Without this step the OMAG platform will not find your connector, and therefore it cannot be used.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.