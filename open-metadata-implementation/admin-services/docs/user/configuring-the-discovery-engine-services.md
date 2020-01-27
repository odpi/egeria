<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuring the discovery engine services

The discovery engine services provide the principle [subsystem](../concepts/omag-subsystem.md) of
a [discovery server](../../../governance-servers/discovery-engine-services/docs/discovery-server.md).

The configuration for the discovery engine services is made up of 2 parts:

* The location of the open metadata server where the
  [Discovery Engine Open Metadata Access Service (OMAS)](../../../access-services/discovery-engine) is running.

* The list of discovery engines that should run in this server.  The definition of these discovery
  engines are retrieved from the open metadata server when the discovery server starts up.

The descriptions below describe how to configure the discovery engine services into an OMAG Server.
In the examples below, the OMAG Server being configured is called `finditDL01`.  It should call
an open metadata server called `cocoMDS1`.  Both `finditDL01` and `cocoMDS1` happen to be hosted on the same
OMAG Server platform at `http://localhost:8080`.  

## Defining the location of the open metadata server

The location of the open metadata server is configured using two properties: the server url root of the
open metadata server's OMAG Server Platform and the name of the server.

This first command sets up the url root of `http://localhost:8080`.
```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/finditDL01/discovery-server/access-service-root-url
{ "http://localhost:8080" }
```

This next command sets up the name of the open metadata server.  In this example
the open metadata server is called `cocoMDS1`.   
```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/finditDL01/discovery-server/access-service-server-name
{ "cocoMDS1" }
```

## Configure the discovery engines

The following command sets up the list of discovery engines that are to run in the discovery service.
In this example, the discovery engines are being configured into
OMAG Server `finditDL01` running on the OMAG Server Platform at URL `http://localhost:8080`
    
```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/finditDL01/discovery-server/set-discovery-engines
{ list of unique names (qualified names) for the discovery engines }
```


## Removing the configuration for the discovery engine services

The following command removes the configuration for the discovery engine services from an
OMAG server's configuration document.  This may be used if the discovery engine services have been
added in error.  In this example, the discovery engine services are removed from
OMAG Server `finditDL01` running on the OMAG Server Platform at URL `http://localhost:8080`.

```
DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/finditDL01/discovery-server
```


## Related topics

In addition to the discovery engine services,
a discovery server needs to configure at least one [audit log destination](configuring-the-repository-services.md) through the repository services.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.