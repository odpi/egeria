<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuring the Open Metadata Access Services (OMASs)

The [Open Metadata Access Services (OMASs)](../../../access-services) provide the domain-specific
APIs for metadata management and governance.
They run in a metadata server and typically offer a REST API, Java client and an event-based interface for
asynchronous interaction.

## Prerequisite configuration

The access service configuration depends on the definitions of the [event bus](configuring-event-bus.md)
and the [local server's userId](configuring-omag-server-basic-properties.md).
  
## Enable the access services

To enable all of the access services (and the [Enterprise Repository Services](../../../repository-services/docs/subsystem-descriptions/enterprise-repository-services.md)
that support them) with default configuration values use the following command.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/access-services
```

## Disable the access services


The access services can be disabled with the following command.
This also disables the enterprise repository services since they
are not being used.

```
DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/access-services
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.