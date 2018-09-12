 /* SPDX-License-Identifier: Apache-2.0 */
 package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

 import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

 /**
  * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
  * The IGCOMRSRepositoryEventMapperProvider is the connector provider for the IGCOMRSRepositoryEventMapper.
  * It extends OMRSRepositoryEventMapperProviderBase which in turn extends the OCF ConnectorProviderBase.
  * ConnectorProviderBase supports the creation of connector instances.
  * <p>
  * The OMRSRESTRepositoryConnectorProvider must initialize ConnectorProviderBase with the Java class
  * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
  * Then the connector provider will work.
  */
 public class IGCOMRSRepositoryEventMapperProvider extends OMRSRepositoryConnectorProviderBase {
     /**
      * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
      * OMRS Connector implementation.
      */
     public IGCOMRSRepositoryEventMapperProvider() {
         Class connectorClass = IGCOMRSRepositoryEventMapper.class;

         super.setConnectorClassName(connectorClass.getName());
     }
 }