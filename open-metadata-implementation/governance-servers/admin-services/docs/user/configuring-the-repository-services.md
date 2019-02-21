<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the Open Metadata Repository Services (OMRS)

The [Open Metadata Repository Services](../../../../repository-services) provide support for access and exchange
of open metadata.

A OMAG server always needs the repository services, even if it is just for the audit log.

## Setting up the local repository

A local repository is optional.
The administration services can be used to enable one of the built-in
local repositories.

#### Enable the graph repository

This command is a placeholder for an Egeria graph repository.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/mode/local-graph-repository

```

#### Enable the in-memory repository

The in-memory repository is useful for demos and testing.
No metadata is kept if the open metadata services are deactivated,
or the server is shutdown.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/mode/in-memory-repository

```

#### Enable the IBM Information Governance Catalog repository proxy

The OMAG Server can act as a [repository proxy](../concepts/repository-proxy.md)
to an IBM Information Governance Catalog ("IGC") environment.
This is done by POSTing the IGC environment details:

- `igcBaseURL` specifies the https host and port on which to access the IGC REST API
- `igcAuthorization` provides a basic-encoded username and password

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/mode/ibm-igc/details
{
    "igcBaseURL": "https://infosvr.vagrant.ibm.com:9446",
    "igcAuthorization": "aXNhZG1pbjppc2FkbWlu",
}

```

The specific version of IBM Information Governance Catalog the environment is running will be detected as part of the initialization process.

#### Enable OMAG Server as a repository proxy

The OMAG Server can act as a proxy to a vendor's repository.
This is done by adding the connection
for the repository proxy as the local repository.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/proxy-details?connectorProvider={javaClassName}

```

#### Add the local repository's event mapper

Any open metadata repository that supports its own API needs an
event mapper to ensure the
Open Metadata Repository Services (OMRS) is notified when
metadata is added
to the repository without going through the open metadata APIs.

The event mapper is a connector that listens for proprietary events
from the repository and converts them into calls to the OMRS.
The OMRS then distributes this new metadata.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/event-mapper-details?connectorProvider={javaClassName}&eventSource={resourceName}

```

For example, to enable the IBM Information Governance Catalog event mapper,
POST the following (where `igc.hostname.somewhere.com` is the hostname of the
domain (services) tier of the environment, and `59092` is the port on which
its Kafka bus can be accessed):

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/event-mapper-details?connectorProvider=org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.IGCOMRSRepositoryEventMapperProvider&eventSource=igc.hostname.somewhere.com:59092

```

#### Remove the local repository

This command removes all configuration for the local repository.
This includes the local metadata collection id.  If a new local repository is
added, it will have a new local metadata collection id and will
not be able to automatically re-register with its cohort(s).

```

DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository

```

### Cohort registration

Open metadata repository cohorts are a set of metadata servers
that are sharing metadata using the open metadata services.
They use a peer-to-peer protocol coordinated through an event bus topic
(typically this is an Apache Kafka topic).

#### Enable access to a cohort

The following command registers the server with cohort called `cocoCohort`.

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/cohorts/cocoCohort

```

#### Disconnect from a cohort

This command unregisters a server from a cohort called `cocoCohort`.

```

DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/cohorts/cocoCohort

```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.