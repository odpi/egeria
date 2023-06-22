<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  

# Open Metadata View Services (OMVS)

The Open Metadata View Services (OMVS) provide task oriented, domain-specific services
for user interfaces to integrate with open metadata.

The view services are as follows:
The Open Metadata View Services (OMVS) provide task oriented, domain-specific services for user interfaces to integrate with open metadata.  They provide REST APIs.

The view services are as follows:

* **[My Profile](my-profile)** - locate, retrieve and update information relating to the calling user's profile.

  The My Profile OMVS is for user interfaces supporting a personalized experience.  A registered user is able to maintain information about themselves and their network as well as set up and use personalized collections of metadata elements.

* **[Glossary Author](glossary-author)** - develop new glossary terms and categories.

  The Glossary Author OMVS is for user interfaces supporting the creating and editing of glossary content, such as glossary terms and categories.  The changes made are immediately visible to all users.

* **[Glossary Browser](glossary-browser)** - search and view the contents of specific glossaries and their links to governance definitions and assets.

  The Glossary Browser OMVS is for user interfaces that wish to provide search facilities within a single glossary (or glossaries) along with the ability to browse the structure of the glossary and view an element within a glossary along with connected governance classifications, governance definitions, user feedback and attached assets.

* **[Glossary Workflow](glossary-workflow)** - develop new glossary terms and categories in a controlled workflow process.

  The Glossary Workflow OMVS is for user interfaces supporting the creation and editing of glossary content, such as glossary terms and categories, in a controlled workflow process.  This means that as terms and categories are created, updated and deleted, these changes are invisible to the general user until they are approved.

* **[Repository Explorer](rex-view)** - explorer interface to inspect instances across a cohort of repositories.

  The Repository Explorer (Rex) OMVS is for user interfaces that support enterprise architects who need to inspect, navigate or explore the instance data stored in an open metadata repository or a cohort of repositories. It enables the retrieval of instance data (entities and relationships) and exploration of the graph of instances connected to those entities. This enables the user to construct a graph (as a diagram) to visualize the details and connectivity of a group of instances of interest to the user.

* **[Type Explorer](tex-view)** - explorer interface to inspect types across a cohort of repositories.

  The Type Explorer (Tex) OMVS is for user interfaces that support enterprise architects who need to inspect, navigate or explore the open metadata types supported by a repository or a cohort of repositories. It enables the retrieval of type data (relating to entities, relationships and classifications) and exploration of the graphs of entity type inheritance and the supported combinations of entity and relationship types.

* **[Dynamic Infrastructure and Operations](dino-view)** - admin interface to inspect servers, services, cohorts and platforms.

  The Dino OMVS is for user interfaces that support Egeria operators who need to inspect, navigate or explore the open metadata servers, services, cohorts and platforms that are configured or actively running. It is intended for operations and problem-determination.

* **[Server Author](server-author-view)** - an authoring interface for servers

  The Server Author OMVS is for user interfaces that support Egeria Server authors. This user interface allows the author to create, delete or update a server configuration using an intuitive UI, rather than needing to use the low level admin rest calls and associated json payloads.



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
   "localServerURL":"https://localhost:9448",
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
