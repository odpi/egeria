<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the Engine Host Services

The Engine Host Services provide the base implementation of
the [Engine Host](../concepts/engine-host.md) OMAG Server.

There are two parts to configuring the Engine Host Services:

* Specifying the server location of a
  [Governance Engine Open Metadata Access Service (OMAS)](../../../access-services/governance-engine)
  that will supply the definitions of the governance engines that will run in the engine services.
  
* Specify each of the engine services that will run in the
  engine host.  Each of the engine services will define the
  [governance engines](../../../access-services/governance-engine/docs/concepts/governance-service.md)
  that they will control.  



## Defining the location of the governance engine definitions

The location of the [metadata server](../concepts/metadata-server.md) (or [metadata access point](../concepts/metadata-access-point.md))
running the Governance Engine OMAS is configured using two properties: the server url root of the 
metadata server's OMAG Server Platform and the name of the metadata server.
 
```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{engineHostServerName}/engine-definitions/client-config
{
        "class": "OMAGServerClientConfig",
        "omagserverPlatformRootURL": {MDServerURLRoot},
        "omagserverName" : "{MDServerName}"
}
```

## Configure the engines services

The configuration for the engine services is covered in a
[separate article](configuring-the-engine-services.md).

## Removing the configuration for the engine host services

The following command removes the configuration for the engine host services from an
OMAG server's configuration document.  This may be used if the engine host services have been
added in error.  
```
DELETE {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{engineHostServerName}/engine-host-services
```

## Further Information

The definition of the governance engines is describe in the [Governance Engine OMAS's User Guide](../../../access-services/governance-engine/docs/user).

----
* Return to [the Engine Host Server](../concepts/engine-host.md)
* Return to [configuration document structure](../concepts/configuration-document.md)
* Return to [Configuring an OMAG Server](configuring-an-omag-server.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.