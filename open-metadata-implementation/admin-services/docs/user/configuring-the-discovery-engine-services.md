<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuring the discovery engine services

The discovery engine services provide the principle [subsystem](../concepts/omag-subsystem.md) of
a [discovery server](../concepts/discovery-server.md).

The configuration for the discovery engine services is made up of 2 parts:

* The location of the metadata server or metadata access point where the
  [Discovery Engine Open Metadata Access Service (OMAS)](../../../access-services/discovery-engine) is running.

* The list of discovery engines that should run in this server.  The definition of these discovery
  engines are retrieved from the open metadata server when the discovery server starts up.

The descriptions below describe how to configure the discovery engine services into a Discovery Server. 

## Defining the location of the metadata server or metadata access point

The location of the metadata server (or metadata access point) is configured using two properties: the server url root of the 
metadata server's OMAG Server Platform and the name of the metadata server.
 
```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{discoveryServerName}/discovery-server/client-config
{
        "class": "OMAGServerClientConfig",
        "omagserverPlatformRootURL": {MDServerURLRoot},
        "omagserverName" : "{MDServerName}"
}
```

## Configure the discovery engines

The following command sets up the list of discovery engines that are to run in the discovery service.
The list of discovery engines are sent in the request body
    
```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{discoveryServerName}/discovery-server/set-discovery-engines
{ list of unique names (qualified names) for the discovery engines }
```


# Removing the configuration for the discovery engine services

The following command removes the configuration for the discovery engine services from an
OMAG server's configuration document.  This may be used if the discovery engine services have been
added in error.  
```
DELETE {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{discoveryServerName}/discovery-server
```

----
Return to [Configuring the discovery server](../concepts/discovery-server.md#Configuring-the-Discovery-Server).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.