<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuring the Open Metadata Integration Services (OMIS)

The [integration services](../../../integration-services) (or Open Metadata Integration Services (OMISs) to give them
their full name)
run in an [Integration Daemon](../concepts/integration-daemon.md).

Each integration service hosts one or more [integration connectors](../../../governance-servers/integration-daemon-services/docs/integration-connector.md).  An integration connector
is responsible for the exchange of metadata with a specific deployment of a third party technology.
For example, the [Database Integrator](../../../integration-services/database-integrator) integration service
supports integration connectors that work with relational databases (RDBMS).
A deployment of this integration service in an integration daemon may host, say,
two integration connectors each loading metadata from their own relational database server.

It is possible to get a description of each of the registered
integration services using the following command:

```
GET {platformURLRoot}/open-metadata/platform-services/users/{userId}/server-platform/registered-services/integration-services
```
Note the `integrationServiceURLMarker` for the integration service that you want to configure.

Figure 1 shows the structure of the configuration for an individual integration service.

![Figure 1](../concepts/integration-service-config.png#pagewidth)
> **Figure 1:** The configuration document contents for an integration service

The descriptive information and operational status are filled out automatically by the
administration services based on the `integrationServiceURLMarker` value that you supply.
The other values are supplied on the configuration call.

Each integration service is configured with the network location of the
[Metadata Access Point](../concepts/metadata-access-point.md) /
[Metadata Server](../concepts/metadata-server.md)
running the appropriate [Open Metadata Access Service](../../../access-services).
There are a set of options that the integration service supports
along with the list of configuration properties for the integration connectors that will be run in the
integration service.
The integration connector's configuration properties defines which connector implementation
to use and how it should be operated.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/integration-services/{integrationServiceURLMarker}
{
        "class": "IntegrationServiceRequestBody",
        "omagserverPlatformRootURL": "{MDServerURLRoot}",
        "omagserverName" : "{MDServerName}",
        "integrationConnectorConfigs" : [ 
        {
             "class": "IntegrationConnectorConfig",
             "connectorName" : " ... ",             
             "connectorUserId" : " ... ",           
             "connection" : 
                          { 
                              "class" : "Connection",
                              "connectorType" : 
                              {
                                  "class" : "ConnectorType",
                                  "connectorProviderClassName" : "{connector provider class name}"
                              },
                              "endpoint" :
                              {
                                  "class" : "Endpoint",
                                  "address" : "..."
                              }
                          },               
             "metadataSourceQualifiedName" : " ... ",
             "refreshTimeInterval" : "60", 
             "usesBlockingCalls" : "false",
             "permittedSynchronization" : " ... "
        } ]      
}
```
Where:
* **connectorName** - Set up the name of the connector.  This name is used for routing refresh calls to the connector as well        
  as being used for diagnostics.  Ideally it should be unique amongst the connectors for the integration service.
* **connectorUserId** - Set up the user id for the connector - if this is null, the integration daemon's userId is used
  on requests to the Open Metadata Access Service (OMAS). 
* **connection** - Set up the connection for the integration connector.              
* **metadataSourceQualifiedName** - Set up the qualified name of the metadata source for this integration connector.  This is the qualified name
  of an appropriate software server capability stored in open metadata.  This software server capability      
  is accessed via the partner OMAS.                                                                           
* **refreshTimeInterval** - Set up the number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh                  
  is only called at server start up and whenever the refresh REST API request is made to the integration daemon.                     
  If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.        
* **usesBlockingCalls** - Set up whether the connector should be started in its own thread to allow it is block on a listening call.
* **permittedSynchronization** - An optional property that defines the permitted directions of metadata flow
  between the third party technology and open metadata.  If the integration connector
  attempts to flow metadata in a direction that is not permitted, it receives the `UserNotAuthorizedException`.
  The default for this value is set up automatically in the integration service's descriptive information so this
  value only needs to be set if it is necessary to restrict the behavior of the
  connector.  These are the different values for this property and their effect.
  
  *    "TO_THIRD_PARTY" - The third party technology is logically downstream of open metadata.  This means the open metadata
                        ecosystem is the originator and owner of the metadata being synchronized. Any updates detected
                        in the third technology are overridden by the latest open metadata values.
  *    "FROM_THIRD_PARTY" - The third party technology is logically upstream (the originator and owner of the metadata). 
                        Any updates made in open metadata are not passed to the third party technology and the
                        third party technology is requested to refresh the open metadata version.
  *    "BOTH_DIRECTIONS" - Metadata exchange is permitted in both directions.  Synchronization is halted on a specific
                        element if potentially clashing updates have occurred both in the third party technology and
                        open metadata.  Such conflicts are logged on the audit log and resolved through manual stewardship.




----
* Return to [the Integration Daemon](../concepts/integration-daemon.md)
* Return to [configuration document structure](../concepts/configuration-document.md)
* Return to [Configuring an OMAG Server](configuring-an-omag-server.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.