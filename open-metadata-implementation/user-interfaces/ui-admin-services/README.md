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
   "localServerName":"ccc",
   "localServerUserId":"UIServer",
   "localServerPassword":"bbb",
   "metadataServerName":"Server1",
   "metadataServerURL":"http://localhost:8080",
   "viewServiceConfigs":[
   	 {
         "class":"org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig",
         "viewServiceAdminClass":"org.odpi.openmetadata.userinterface.uichassis.springboot.admin.AssetSearchViewAdmin",
          "viewServiceId":1010
      },
       {
         "class":"org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig",
         "viewServiceAdminClass":"org.odpi.openmetadata.userinterface.uichassis.springboot.admin.OpenLineageViewAdmin",
         "viewServiceId":1020,
         "viewServiceOptions":{
            "openLineageServerName":"lin1",
            "openLineageServerURL":"http://localhost:8081"
         }
      },
      {
         "class":"org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig",
         "viewServiceAdminClass":"org.odpi.openmetadata.userinterface.uichassis.springboot.admin.SubjectAreaViewAdmin",
         "viewServiceId":1030
      },

      {
         "class":"org.odpi.openmetadata.userinterface.adminservices.configuration.properties.ViewServiceConfig",
         "viewServiceAdminClass":"org.odpi.openmetadata.userinterface.uichassis.springboot.admin.TypeExplorerViewAdmin",
         "viewServiceId":1040
      }

   ],
   "auditLogConnections":[
         {
            "class":"Connection",
            "type":{
               "class":"ElementType",
               "elementTypeId":"114e9f8f-5ff3-4c32-bd37-a7eb42712253",
               "elementTypeName":"Connection",
               "elementTypeVersion":1,
               "elementTypeDescription":"A set of properties to identify and configure a connector instance.",
               "elementOrigin":"CONFIGURATION"
            },
            "guid":"5390bf3e-6b38-4eda-b34a-de55ac4252a7",
            "qualifiedName":"DefaultAuditLog.Connection.Server1",
            "displayName":"DefaultAuditLog.Connection.Server1",
            "description":"OMRS default audit log connection.",
            "connectorType":{
               "class":"ConnectorType",
               "type":{
                  "class":"ElementType",
                  "elementTypeId":"954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                  "elementTypeName":"ConnectorType",
                  "elementTypeVersion":1,
                  "elementTypeDescription":"A set of properties describing a type of connector.",
                  "elementOrigin":"LOCAL_COHORT"
               },
               "guid":"4afac741-3dcc-4c60-a4ca-a6dede994e3f",
               "qualifiedName":"Console Audit Log Store Connector",
               "displayName":"Console Audit Log Store Connector",
               "description":"Connector supports logging of audit log messages to stdout.",
               "connectorProviderClassName":"org.odpi.openmetadata.adapters.repositoryservices.auditlogstore.console.ConsoleAuditLogStoreProvider"
            },
            "endpoint":{
               "class":"Endpoint",
               "type":{
                  "class":"ElementType",
                  "elementTypeId":"dbc20663-d705-4ff0-8424-80c262c6b8e7",
                  "elementTypeName":"Endpoint",
                  "elementTypeVersion":1,
                  "elementTypeDescription":"Description of the network address and related information needed to call a software service.",
                  "elementOrigin":"CONFIGURATION"
               },
               "guid":"836efeae-ab34-4425-89f0-6adf2faa1f2e",
               "qualifiedName":"DefaultAuditLog.Endpoint.Server1.auditlog",
               "displayName":"DefaultAuditLog.Endpoint.Server1.auditlog",
               "description":"OMRS default audit log endpoint.",
               "address":"Server1.auditlog"
            }
         }
      ]
}

in addition to configuration, you can start and stop the UI services. In the future, this is functionality will be
utilized at runtime.


## Further reading

* TODO User Guide
* TODO Tutorials
* TODO Internal Design Documentation

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.