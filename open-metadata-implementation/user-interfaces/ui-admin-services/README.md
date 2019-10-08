<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
 
# User Interface Administration Services

The administration services support the configuration of the open metadata and governance user interface services
within the [OMAG UI Platform](docs/concepts/omag-ui-platform.md)
This configuration determines how to connect to the OMAG Server platform services.

It also supports the starting and stopping of logical [OMAG UI Servers](docs/concepts/logical-ui-server.md)
on the OMAG UI platform and querying the runtime (operational) state of these ui servers.


An example rest call to configure the UI is a post call to https://localhost:8443/open-metadata/ui-admin-services/users/<userid>/servers/<server>/configuration
with body:

{
   "class":"org.odpi.openmetadata.userinterface.adminservices.configuration.properties.UIServerConfig",
   "localServerName":"aaa",
   "localServerUserId":"UIServer",
   "localServerPassword":"bbb",
   "metadataServerName":"Server1",
   "metadataServerURL":"http://localhost:8080",
   "governanceServerEndpoints":[
      {
         "serverName":"lin1",
         "serverRootURL":"http://localhost:8081",
         "governanceServiceName":"open-lineage"
      }
   ]
}


Note that Git hub issue #1658 has been raised to ensure this config file is encrypted.

## Further reading

* TODO User Guide
* TODO Tutorials
* TODO Internal Design Documentation

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.