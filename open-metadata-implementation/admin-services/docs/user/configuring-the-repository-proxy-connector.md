<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the Repository Proxy Connectors

The mapping between a third party metadata repository and the open metadata protocols
in a [Repository Proxy](../concepts/repository-proxy.md) is implemented in two connectors:

* An OMRS Repository Connector
* An OMRS Event Mapper Connector

They are configured as follows.

## Add the connection to the repository connector

The OMAG Server can act as a proxy to a vendor's repository.
This is done by adding the connection
for the repository proxy as the local repository.

```
POST {{platformURLRoot}}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/local-repository/proxy-details?connectorProvider={fullyQualifiedJavaClassName}
```

### Add the connection to the repository's event mapper

Any open metadata repository that supports its own API needs an
event mapper to ensure the
Open Metadata Repository Services (OMRS) is notified when
metadata is added
to the repository without going through the open metadata APIs.

The event mapper is a connector that listens for proprietary events
from the repository and converts them into calls to the OMRS.
The OMRS then distributes this new metadata.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName/local-repository/event-mapper-details?connectorProvider={fullyQualifiedJavaClassName}&eventSource={resourceName}
```

For example, to enable the IBM Information Governance Catalog event mapper,
POST the following (where `igc.hostname.somewhere.com` is the hostname of the
domain (services) tier of the environment, and `59092` is the port on which
its Kafka bus can be accessed):

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/local-repository/event-mapper-details?connectorProvider=org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.IGCOMRSRepositoryEventMapperProvider&eventSource=igc.hostname.somewhere.com:59092
```

----
* Return to [Configuring an OMAG Server](configuring-an-omag-server.md)
* Return to [Configuring a Repository Proxy](../concepts/repository-proxy.md#Configuring-a-Repository-Proxy)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.