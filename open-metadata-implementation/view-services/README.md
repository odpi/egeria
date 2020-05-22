<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
![InDev](../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Open Metadata View Services (OMVS)

The Open Metadata View Services (OMVS) provide task oriented, domain-specific services
for user interfaces to integrate with open metadata. 

The view services are as follows:

* **[glossary-author-view](glossary-author-view)** - develop a definition of a subject area including glossary
terms, reference data and rules. 

  The Glossary Author OMVS is for user interfaces that support subject matter experts
who are defining glossaries around data for a specific
subject area, such as "customer data".  It supports the development of a comprehensive
definition of the subject area and the standards that support it.
These definitions can then be folded into the Governance Program,
and used by Asset Owner's to improve the findability and understandability
of their assets by linking their asset's structure to relevant parts of
the subject area definition.


## Inside an OMVS

User Interfaces can connect to an OMVS through its REST API. The REST API interacts with a remote OMAG Server.
The OMVS APIs are deployed together in a single web application. 

The [administration services](../admin-services/README.md) provide the ability to configure, start and stop the view services.
An example configuration document for a view server called 'viewserver', configured to communicate with a
remote server called 'Server1' is:
```javascript
{ 
   "class":"OMAGServerConfig",
   "versionId":"V2.0",
   "localServerId":"a8e40a02-a95a-4dce-a5ba-8d4f68298ec9",
   "localServerName":"viewserver",
   "localServerType":"View Server",
   "localServerURL":"http://localhost:8085",
   "localServerUserId":"OMAGServer",
   "maxPageSize":1000,
   "viewServicesConfig":[ 
      { 
         "class":"ViewServiceConfig",
         "viewServiceId":0,
         "viewServiceFullName":"Glossary Author",
         "viewServiceAdminClass":"org.odpi.openmetadata.viewservices.glossaryauthor.admin.GlossaryAuthorViewAdmin",
         "viewServiceOperationalStatus":"ENABLED",
         "omagserverPlatformRootURL":"https://localhost:9443",
         "omagserverName":"Server1"
      }
   ],
   "repositoryServicesConfig":{ 
      "class":"RepositoryServicesConfig",
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
            "qualifiedName":"DefaultAuditLog.Connection.viewserver",
            "displayName":"DefaultAuditLog.Connection.viewserver",
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
               "qualifiedName":"DefaultAuditLog.Endpoint.viewserver.auditlog",
               "displayName":"DefaultAuditLog.Endpoint.viewserver.auditlog",
               "description":"OMRS default audit log endpoint.",
               "address":"viewserver.auditlog"
            }
         }
      ]
   }
}
```

----
Return to [open-metadata-implementation](..).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
