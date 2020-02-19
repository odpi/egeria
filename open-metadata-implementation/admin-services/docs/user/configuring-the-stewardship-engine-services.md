<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the stewardship engine services

The stewardship engine services provide the principle [subsystem](../concepts/omag-subsystem.md) of
a [stewardship server](../concepts/stewardship-server.md).

The configuration for the stewardship engine services is made up of 2 parts:

* The location of the metadata server or metadata access point where the
  [Stewardship Action Open Metadata Access Service (OMAS)](../../../access-services/stewardship-action) is running.

* The list of stewardship engines that should run in this server.  The definition of these stewardship
  engines are retrieved from the open metadata server when the stewardship server starts up.

The descriptions below describe how to configure the stewardship engine services into a Stewardship Server. 

## Defining the location of the metadata server or metadata access point

The location of the metadata server (or metadata access point) is configured using two properties: the server url root of the 
metadata server's OMAG Server Platform and the name of the metadata server.
 
```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{stewardshipServerName}/stewardship-server/client-config
{
        "class": "OMAGServerClientConfig",
        "omagserverPlatformRootURL": {MDServerURLRoot},
        "omagserverName" : "{MDServerName}"
}
```

## Configure the stewardship engines

The following command sets up the list of stewardship engines that are to run in the stewardship service.
The list of stewardship engines are sent in the request body
    
```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{stewardshipServerName}/stewardship-server/set-stewardship-engines
{ list of unique names (qualified names) for the stewardship engines }
```


# Removing the configuration for the stewardship engine services

The following command removes the configuration for the stewardship engine services from an
OMAG server's configuration document.  This may be used if the stewardship engine services have been
added in error.  
```
DELETE {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{stewardshipServerName}/stewardship-server
```

----
Return to [Configuring the stewardship server](../concepts/stewardship-server.md#Configuring-the-Stewardship-Server).





----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.