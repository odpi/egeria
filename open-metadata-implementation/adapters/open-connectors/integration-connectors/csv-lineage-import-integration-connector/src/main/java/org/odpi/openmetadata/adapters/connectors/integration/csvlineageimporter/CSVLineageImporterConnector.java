/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter;

import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.ffdc.CSVLineageImporterAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.ffdc.CSVLineageImporterErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CSVLineageImporterConnector extends LineageIntegratorConnector
{
    private String fileName = null;

    private final Map<String, String> columnToTypeName = new HashMap<>();

    private OpenMetadataAccess openMetadataAccess = null;
    private final PropertyHelper propertyHelper = new PropertyHelper();



    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        loadTypeMaps();

        /*
         * Extract the configuration
         */
        EndpointProperties endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            fileName = endpoint.getAddress();
        }

        openMetadataAccess = integrationContext.getIntegrationGovernanceContext().getOpenMetadataAccess();
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        processFile();
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {

    }


    private void loadTypeMaps()
    {
        columnToTypeName.put("LIB", "Collection");
        columnToTypeName.put("CMS", "Collection");
        columnToTypeName.put("CMP", "DisplayDataSchemaType");
        columnToTypeName.put("CTN", "DeployedSoftwareComponent");
        columnToTypeName.put("TB2", "RelationalTable");
        columnToTypeName.put("ENT", "Port");
        columnToTypeName.put("IDD", "EmbeddedProcess");
        columnToTypeName.put("PGM", "DeployedSoftwareComponent");
        columnToTypeName.put("CPY", "SourceCodeFile");
        columnToTypeName.put("MIS", "Process");
        columnToTypeName.put("LIB->PGM", "CollectionMembership");
        columnToTypeName.put("CMS->CMP", "CollectionMembership");
        columnToTypeName.put("PGM->CMS", "ResourceList");
        columnToTypeName.put("PGM->CPY", "ResourceList");
        columnToTypeName.put("PGM->ENT", "ProcessPort");
        columnToTypeName.put("PGM->TB2", "DataFlow");
        columnToTypeName.put("PGM->IDD", "DataFlow");
        columnToTypeName.put("PGM->PGM", "ProcessCall");
        columnToTypeName.put("PGM->CMP", "ProcessCall");
        columnToTypeName.put("PGM->CTN", "ProcessCall");
        columnToTypeName.put("MIS->PGM", "ProcessCall");
    }

    private void processFile() throws ConnectorCheckedException
    {
        final String methodName = "processFile";
        try
        {
            CSVFileStoreConnector csvFileStoreConnector = this.getConnectorWithOCF();

            long numberOfRecords = csvFileStoreConnector.getRecordCount();

            for (int i = 0; i < numberOfRecords; i++)
            {
                List<String> columns = csvFileStoreConnector.readRecord(i);

                if (columns != null)
                {
                    System.out.println(columns);
                    processRow(columns);
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    CSVLineageImporterAuditCode.UNEXPECTED_EXC_RETRIEVING_FOLDER.getMessageDefinition(error.getClass().getName(),
                                                                                                                      connectorName,
                                                                                                                      methodName,
                                                                                                                      fileName,
                                                                                                                      fileName,
                                                                                                                      error.getMessage()));
            }

            throw new ConnectorCheckedException(
                    CSVLineageImporterErrorCode.UNEXPECTED_EXC_RETRIEVING_FOLDER.getMessageDefinition(error.getClass().getName(),
                                                                                                      connectorName,
                                                                                                      methodName,
                                                                                                      fileName,
                                                                                                      fileName,
                                                                                                      error.getMessage()),
                    error.getClass().getName(),
                    methodName,
                    error);
        }
    }

    /**
     * Process a single row from the CSV file.
     *
     * @param row row contents separated into a list of strings
     */
    private void processRow(List<String> row) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        if ((row != null) && (row.size() > 4))
        {
            String objectType = row.get(0);
            String objectInstance = row.get(1);
            String relatedType = row.get(2);
            String relatedInstance = row.get(3);
            String orientation = row.get(4);
            String mode = null;

            if (row.size() > 5)
            {
                mode = row.get(5);
            }

            String openMetadataObjectType = columnToTypeName.get(objectType.trim());
            String openMetadataRelatedType = columnToTypeName.get(relatedType.trim());
            String openMetadataRelationshipType = columnToTypeName.get(objectType.trim() + "->" + relatedType.trim());

            String openMetadataObjectTypeGUID = getOpenMetadataElement(objectType.trim(),
                                                                       objectInstance.trim(),
                                                                       openMetadataObjectType);
            String openMetadataRelatedTypeGUID = getOpenMetadataElement(relatedType.trim(),
                                                                        relatedInstance.trim(),
                                                                        openMetadataRelatedType);

            setupOpenMetadataRelationship(openMetadataObjectTypeGUID,
                                          openMetadataRelatedTypeGUID,
                                          openMetadataRelationshipType,
                                          orientation,
                                          mode);
        }
    }


    private String getOpenMetadataElement(String inputType,
                                          String inputInstanceName,
                                          String openMetadataType) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        if (openMetadataType != null)
        {
            String qualifiedName = inputType + ":" + inputInstanceName;

            OpenMetadataElement openMetadataElement = openMetadataAccess.getMetadataElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

            if (openMetadataElement == null)
            {
                ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                       qualifiedName);

                return openMetadataAccess.createMetadataElementInStore(openMetadataType,
                                                                       ElementStatus.ACTIVE,
                                                                       null,
                                                                       null,
                                                                       elementProperties);

            }
            else
            {
                return openMetadataElement.getElementGUID();
            }
        }

        return null;
    }


    private void setupOpenMetadataRelationship(String openMetadataObjectTypeGUID,
                                               String openMetadataRelatedTypeGUID,
                                               String openMetadataRelationshipType,
                                               String orientation,
                                               String mode) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        if ((openMetadataObjectTypeGUID != null) &&
                    (openMetadataRelatedTypeGUID != null) &&
                    (openMetadataRelationshipType != null))
        {
            String end1GUID = openMetadataObjectTypeGUID;
            String end2GUID = openMetadataRelatedTypeGUID;

            if ("U".equals(orientation))
            {
                end1GUID = openMetadataRelatedTypeGUID;
                end2GUID = openMetadataObjectTypeGUID;
            }


            List<RelatedMetadataElements> existingRelationships = openMetadataAccess.getMetadataElementRelationships(end1GUID,
                                                                                                                     end2GUID,
                                                                                                                     openMetadataRelationshipType,
                                                                                                                     0,
                                                                                                                     0);
            if (existingRelationships == null)
            {
                ElementProperties properties = null;
                if (mode != null)
                {
                    properties = propertyHelper.addStringProperty(null, "description", mode);
                }
                openMetadataAccess.createRelatedElementsInStore(openMetadataRelationshipType,
                                                                end1GUID,
                                                                end2GUID,
                                                                null,
                                                                null,
                                                                properties);
            }
        }
    }


    /**
     * Create the connector to the CSV file.
     *
     * @return connector to requested file
     */
    private CSVFileStoreConnector getConnectorWithOCF()
    {
        CSVFileStoreConnector connector = null;

        try
        {
            ConnectorBroker broker = new ConnectorBroker();

            connector = (CSVFileStoreConnector) broker.getConnector(getHardCodedConnection(fileName));
        }
        catch (Exception error)
        {
            System.out.println("The connector can not be created with OCF.");
        }

        return connector;
    }


    /**
     * This method creates a connection.  The connection object provides the OCF with the properties to create the
     * connector and the properties needed by the connector to access the asset.
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param fileName name of the file to connect to
     * @return connection object
     */
    private Connection getHardCodedConnection(String   fileName)
    {
        final String endpointGUID      = "8bf8f5fa-b5d8-40e1-a00e-e4a0c59fd6c0";
        final String connectorTypeGUID = "2e1556a3-908f-4303-812d-d81b48b19bab";
        final String connectionGUID    = "b9af734f-f005-4085-9975-bf46c67a099a";

        final String endpointDescription = "File name.";

        String endpointName    = "CSVFileStore.Endpoint." + fileName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(Endpoint.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(fileName);


        final String connectorTypeDescription   = "CSVFileStore connector type.";
        final String connectorTypeJavaClassName = CSVFileStoreProvider.class.getName();

        String connectorTypeName = "CSVFileStore.ConnectorType.Test";

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "CSVFileStore connection.";

        String connectionName = "CSVFileStore.Connection.Test";

        Connection connection = new Connection();

        connection.setType(Connection.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

        return connection;
    }

}
