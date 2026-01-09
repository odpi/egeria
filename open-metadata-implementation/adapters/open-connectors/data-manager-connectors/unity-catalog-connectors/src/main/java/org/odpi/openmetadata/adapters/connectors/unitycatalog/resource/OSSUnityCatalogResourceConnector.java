/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.resource;

import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.factory.RESTClientFactory;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCAuditCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.*;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

public class OSSUnityCatalogResourceConnector extends ConnectorBase implements AuditLoggingComponent
{
    private AuditLog auditLog      = null;
    private String   connectorName = "Unity Catalog Connector";

    private String targetRootURL  = null;
    private String ucInstanceName = "Unity Catalog";

    private RESTClientConnector clientConnector = null;



    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Set up a new instance name (must be called before start()).
     *
     * @param ucInstanceName new instance name
     */
    public void setUCInstanceName(String ucInstanceName)
    {
        this.ucInstanceName = ucInstanceName;
    }



    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException connector disconnected
     */
    @Override
    public void start() throws ConnectorCheckedException,
                               UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        if (connectionBean.getDisplayName() != null)
        {
            connectorName = connectionBean.getDisplayName();
        }

        /*
         * Retrieve the configuration
         */
        Endpoint endpoint = connectionBean.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getNetworkAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(UCErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        try
        {
            /*
             * Create the client that calls Unity Catalog.
             */
            RESTClientFactory factory = new RESTClientFactory(ucInstanceName,
                                                              targetRootURL,
                                                              secretsStoreConnectorMap,
                                                              auditLog);

            this.clientConnector = factory.getClientConnector();

            /*
             * Test the connection.
             */
            this.listCatalogs();
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      UCAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                         error.getClass().getName(),
                                                                                         methodName,
                                                                                         error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(UCErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * The full name uses dot separators.  Return the name after the final dot.
     *
     * @param fullName  dotted name
     * @return short name
     */
    public String getNameFromFullName(String fullName)
    {
        String[] nameParts = fullName.split("\\.");

        return nameParts[nameParts.length - 1];
    }


    /**
     * The schema name is in the middle segment of the three part name.
     *
     * @param threePartName dotted name
     * @return schemaName
     */
    public String getSchemaNameFromThreePartName(String   threePartName)
    {
        String[] nameParts = threePartName.split("\\.");

        if (nameParts.length > 1)
        {
            return nameParts[1];
        }

        return null;
    }


    /**
     * The catalog name is in the first segment of the three part name.
     *
     * @param threePartName dotted name
     * @return catalogName
     */
    public String getCatalogNameFromThreePartName(String   threePartName)
    {
        String[] nameParts = threePartName.split("\\.");

        if (nameParts.length > 0)
        {
            return nameParts[0];
        }

        return null;
    }


    /*
     *===========================================================================
     * Specialized methods - Catalogs
     */

    /**
     * Create catalog.
     *
     * @param name name of the catalog
     * @param comment description of the catalog
     * @param properties additional properties
     * @return resulting catalog info
     * @throws PropertyServerException problem with the call
     */
    public CatalogInfo createCatalog(String              name,
                                     String              comment,
                                     Map<String, String> properties) throws PropertyServerException
    {
        final String methodName = "createCatalog";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/catalogs";

        CatalogProperties catalogProperties = new CatalogProperties();

        catalogProperties.setName(name);
        catalogProperties.setComment(comment);
        //catalogProperties.setProperties(properties);

        return callPostRESTCallNoParams(methodName,
                                        CatalogInfo.class,
                                        urlTemplate,
                                        catalogProperties);
    }


    /**
     * Delete catalog.
     *
     * @param name name of the catalog
     * @param force force the deletion
     * @throws PropertyServerException problem with the call
     */
    public void deleteCatalog(String  name,
                              boolean force) throws PropertyServerException
    {
        final String methodName = "deleteCatalog";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/catalogs/{0}?force={1}";

        callDeleteRESTCall(methodName,
                           Object.class,
                           urlTemplate,
                           name,
                           Boolean.toString(force));
    }


    /**
     * Get catalog.
     *
     * @param name name of the catalog
     * @return resulting catalog info
     * @throws PropertyServerException problem with the call
     */
    public CatalogInfo getCatalog(String name) throws PropertyServerException
    {
        final String methodName = "getCatalog";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/catalogs/{0}";

        return callGetRESTCall(methodName,
                               CatalogInfo.class,
                               urlTemplate,
                               name);
    }


    /**
     * Get all catalog.
     *
     * @return resulting catalog info
     * @throws PropertyServerException problem with the call
     */
    public List<CatalogInfo> listCatalogs() throws PropertyServerException
    {
        final String methodName = "listCatalogs";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/catalogs";

        ListCatalogsResponse response =  callGetRESTCallNoParams(methodName,
                                                                 ListCatalogsResponse.class,
                                                                 urlTemplate);

        if (response != null)
        {
            return response.getCatalogs();
        }

        return null;
    }


    /**
     * Update catalog.  This call overrides all existing properties.
     *
     * @param name name of the catalog
     * @param newName optional new name of the catalog
     * @param comment description of the catalog
     * @param properties additional properties
     * @return resulting catalog info
     * @throws PropertyServerException problem with the call
     */
    public CatalogInfo updateCatalog(String              name,
                                     String              newName,
                                     String              comment,
                                     Map<String, String> properties) throws PropertyServerException
    {
        final String methodName = "updateCatalog";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/catalogs/{0}";

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setNew_name(newName);
        requestBody.setComment(comment);

        if ((newName == null) || (newName.equals(name)))
        {
            /*
             * new_name must not be null.
             */
            requestBody.setNew_name(name + "TemporaryWorkaroundName");
            callPatchRESTCall(methodName,
                              CatalogInfo.class,
                              urlTemplate,
                              requestBody,
                              name);

            requestBody.setNew_name(name);
            return callPatchRESTCall(methodName,
                                     CatalogInfo.class,
                                     urlTemplate,
                                     requestBody,
                                     name);
        }
        else
        {
            return callPatchRESTCall(methodName,
                                     CatalogInfo.class,
                                     urlTemplate,
                                     requestBody,
                                     name);
        }
    }


    /*
     *===========================================================================
     * Specialized methods - Schemas
     */

    /**
     * Create schema.
     *
     * @param name name of the schema
     * @param catalogName name of catalog
     * @param comment description of the schema
     * @param properties additional properties
     * @return resulting schema info
     * @throws PropertyServerException problem with the call
     */
    public SchemaInfo createSchema(String              name,
                                   String              catalogName,
                                   String              comment,
                                   Map<String, String> properties) throws PropertyServerException
    {
        final String methodName = "createSchema";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/schemas";

        SchemaProperties schemaProperties = new SchemaProperties();

        schemaProperties.setName(name);
        schemaProperties.setCatalog_name(catalogName);
        schemaProperties.setComment(comment);
        //schemaProperties.setProperties(properties);

        return callPostRESTCallNoParams(methodName,
                                        SchemaInfo.class,
                                        urlTemplate,
                                        schemaProperties);
    }


    /**
     * Delete schema.
     *
     * @param fullName fullName of the schema
     * @param force force the deletion
     * @throws PropertyServerException problem with the call
     */
    public void deleteSchema(String  fullName,
                             boolean force) throws PropertyServerException
    {
        final String methodName = "deleteSchema";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/schemas/{0}?force={1}";

        callDeleteRESTCall(methodName,
                           Object.class,
                           urlTemplate,
                           fullName,
                           Boolean.toString(force));
    }


    /**
     * Get schema.
     *
     * @param fullName fullName of the schema
     * @return resulting schema info
     * @throws PropertyServerException problem with the call
     */
    public SchemaInfo getSchema(String fullName) throws PropertyServerException
    {
        final String methodName = "getSchema";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/schemas/{0}";

        return callGetRESTCall(methodName,
                               SchemaInfo.class,
                               urlTemplate,
                               fullName);
    }


    /**
     * Get all schema.
     *
     * @return resulting schema info
     * @throws PropertyServerException problem with the call
     */
    public List<SchemaInfo> listSchemas(String catalogName) throws PropertyServerException
    {
        final String methodName = "listSchemas";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/schemas?catalog_name={1}";

        ListSchemasResponse response = callGetRESTCall(methodName,
                                                       ListSchemasResponse.class,
                                                       urlTemplate,
                                                       catalogName);

        if (response != null)
        {
            return response.getSchemas();
        }

        return null;
    }


    /**
     * Update schema.
     *
     * @param fullName name of the schema
     * @param newName optional new name of the schema
     * @param comment description of the schema
     * @param properties additional properties
     * @return resulting schema info
     * @throws PropertyServerException problem with the call
     */
    public SchemaInfo updateSchema(String              fullName,
                                   String              newName,
                                   String              comment,
                                   Map<String, String> properties) throws PropertyServerException
    {
        final String methodName  = "updateSchema";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/schemas/{0}";

        String oldName = this.getNameFromFullName(fullName);

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setNew_name(newName);
        requestBody.setComment(comment);

        if ((newName == null) || (newName.equals(oldName)))
        {
            /*
             * Workaround since not allowed to use the same name twice.
             */
            requestBody.setNew_name(oldName + "TemporaryWorkaroundName");
            callPatchRESTCall(methodName,
                              SchemaInfo.class,
                              urlTemplate,
                              requestBody,
                              fullName);

            requestBody.setNew_name(oldName);
            return callPatchRESTCall(methodName,
                                     SchemaInfo.class,
                                     urlTemplate,
                                     requestBody,
                                     fullName);
        }
        else
        {
            requestBody.setNew_name(newName);
            return callPatchRESTCall(methodName,
                                     SchemaInfo.class,
                                     urlTemplate,
                                     requestBody,
                                     fullName);
        }

    }


    /*
     *===========================================================================
     * Specialized methods - Volumes
     */

    /**
     * Create volume.
     *
     * @param name name of the volume
     * @param catalogName name of catalog
     * @param schemaName name of the schema
     * @param comment description of the volume
     * @param volumeType MANAGED, or EXTERNAL
     * @param storageLocation directory name eg file:///Users/me/Code/uc/unitycatalog/etc/data/external/unity/default/volumes/json_files/
     * @return resulting volume info
     * @throws PropertyServerException problem with the call
     */
    public VolumeInfo createVolume(String name,
                                   String catalogName,
                                   String schemaName,
                                   String comment,
                                   String volumeType,
                                   String storageLocation) throws PropertyServerException
    {
        VolumeProperties volumeProperties = new VolumeProperties();

        volumeProperties.setName(name);
        volumeProperties.setCatalog_name(catalogName);
        volumeProperties.setSchema_name(schemaName);
        volumeProperties.setComment(comment);
        volumeProperties.setVolume_type(volumeType);
        volumeProperties.setStorage_location(storageLocation);

        return createVolume(volumeProperties);
    }


    /**
     * Create volume.
     *
     * @param volumeProperties properties
     *
     * @return resulting volume info
     * @throws PropertyServerException problem with the call
     */
    public VolumeInfo createVolume(VolumeProperties volumeProperties) throws PropertyServerException
    {
        final String methodName = "createVolume";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/volumes";

        return callPostRESTCallNoParams(methodName,
                                        VolumeInfo.class,
                                        urlTemplate,
                                        volumeProperties);
    }


    /**
     * Delete volume.
     *
     * @param fullName fullName of the volume
     * @throws PropertyServerException problem with the call
     */
    public void deleteVolume(String  fullName) throws PropertyServerException
    {
        final String methodName = "deleteVolume";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/volumes/{0}";

        callDeleteRESTCall(methodName,
                           Object.class,
                           urlTemplate,
                           fullName);
    }


    /**
     * Get volume.
     *
     * @param fullName fullName of the volume
     * @return resulting volume info
     * @throws PropertyServerException problem with the call
     */
    public VolumeInfo getVolume(String fullName) throws PropertyServerException
    {
        final String methodName = "getVolume";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/volumes/{0}";

        return callGetRESTCall(methodName,
                               VolumeInfo.class,
                               urlTemplate,
                               fullName);
    }


    /**
     * Get all volumes.
     *
     * @param catalogName name of the catalog
     * @param schemaName name of the schema
     * @return resulting volume info
     * @throws PropertyServerException problem with the call
     */
    public List<VolumeInfo> listVolumes(String catalogName,
                                        String schemaName) throws PropertyServerException
    {
        final String methodName = "listVolumes";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/volumes?catalog_name={1}&schema_name={2}";

        ListVolumesResponse response = callGetRESTCall(methodName,
                                                       ListVolumesResponse.class,
                                                       urlTemplate,
                                                       catalogName,
                                                       schemaName);

        if (response != null)
        {
            return response.getVolumes();
        }

        return null;
    }


    /**
     * Update volume.
     *
     * @param fullName name of the volume
     * @param newName optional new name of the volume
     * @param comment description of the volume
     * @return resulting volume info
     * @throws PropertyServerException problem with the call
     */
    public VolumeInfo updateVolume(String              fullName,
                                   String              newName,
                                   String              comment) throws PropertyServerException
    {
        final String methodName  = "updateVolume";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/volumes/{0}";

        String oldName = this.getNameFromFullName(fullName);

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setComment(comment);

        if ((newName == null) || (newName.equals(oldName)))
        {
            /*
             * new_name must not be null.
             */
            requestBody.setNew_name(oldName + "TemporaryWorkaroundName");
            callPatchRESTCall(methodName,
                              VolumeInfo.class,
                              urlTemplate,
                              requestBody,
                              fullName);

            requestBody.setNew_name(oldName);
            return callPatchRESTCall(methodName,
                                     VolumeInfo.class,
                                     urlTemplate,
                                     requestBody,
                                     fullName);
        }
        else
        {
            requestBody.setNew_name(newName);
            return callPatchRESTCall(methodName,
                                     VolumeInfo.class,
                                     urlTemplate,
                                     requestBody,
                                     fullName);
        }
    }



    /*
     *===========================================================================
     * Specialized methods - Tables
     */

    /**
     * Create table.
     *
     * @param name name of the table
     * @param catalogName name of catalog
     * @param schemaName name of the schema
     * @param comment description of the table
     * @param tableType MANAGED, or EXTERNAL
     * @param dataSourceFormat format
     * @param columns list of columns in the table
     * @param storageLocation directory name eg file:///Users/me/Code/uc/unitycatalog/etc/data/external/unity/default/tables/json_files/
     * @return resulting table info
     * @throws PropertyServerException problem with the call
     */
    public TableInfo createTable(String              name,
                                 String              catalogName,
                                 String              schemaName,
                                 String              comment,
                                 String              tableType,
                                 String              dataSourceFormat,
                                 List<ColumnInfo>    columns,
                                 String              storageLocation) throws PropertyServerException
    {
        TableProperties tableProperties = new TableProperties();

        tableProperties.setName(name);
        tableProperties.setCatalog_name(catalogName);
        tableProperties.setSchema_name(schemaName);
        tableProperties.setComment(comment);
        tableProperties.setTable_type(tableType);
        tableProperties.setData_source_format(dataSourceFormat);
        tableProperties.setColumns(columns);
        tableProperties.setStorage_location(storageLocation);

        return createTable(tableProperties);
    }


    /**
     * Create table.
     *
     * @param tableProperties list of properties for the table
     *
     * @return resulting table info
     * @throws PropertyServerException problem with the call
     */
    public TableInfo createTable(TableProperties tableProperties) throws PropertyServerException
    {
        final String methodName = "createTable";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/tables";

        return callPostRESTCallNoParams(methodName,
                                        TableInfo.class,
                                        urlTemplate,
                                        tableProperties);
    }



    /**
     * Delete table.
     *
     * @param fullName fullName of the table
     * @throws PropertyServerException problem with the call
     */
    public void deleteTable(String  fullName) throws PropertyServerException
    {
        final String methodName = "deleteTable";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/tables/{0}";

        callDeleteRESTCall(methodName,
                           Object.class,
                           urlTemplate,
                           fullName);
    }


    /**
     * Get table.
     *
     * @param fullName fullName of the table
     * @return resulting table info
     * @throws PropertyServerException problem with the call
     */
    public TableInfo getTable(String fullName) throws PropertyServerException
    {
        final String methodName = "getTable";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/tables/{0}";

        return callGetRESTCall(methodName,
                               TableInfo.class,
                               urlTemplate,
                               fullName);
    }


    /**
     * Get all tables.
     *
     * @param catalogName name of the catalog
     * @param schemaName name of the schema
     * @return resulting table info
     * @throws PropertyServerException problem with the call
     */
    public List<TableInfo> listTables(String catalogName,
                                      String schemaName) throws PropertyServerException
    {
        final String methodName = "listTables";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/tables?catalog_name={1}&schema_name={2}";

        ListTablesResponse response = callGetRESTCall(methodName,
                                                      ListTablesResponse.class,
                                                      urlTemplate,
                                                      catalogName,
                                                      schemaName);

        if (response != null)
        {
            return response.getTables();
        }

        return null;
    }


    /*
     *===========================================================================
     * Specialized methods - Functions
     */


    /**
     * Create function.
     *
     * @param functionProperties list of properties for the function
     *
     * @return resulting function info
     * @throws PropertyServerException problem with the call
     */
    public FunctionInfo createFunction(FunctionProperties functionProperties) throws PropertyServerException
    {
        final String methodName = "createFunction";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/functions";

        return callPostRESTCallNoParams(methodName,
                                        FunctionInfo.class,
                                        urlTemplate,
                                        functionProperties);
    }


    /**
     * Delete function.
     *
     * @param fullName fullName of the function
     * @throws PropertyServerException problem with the call
     */
    public void deleteFunction(String  fullName) throws PropertyServerException
    {
        final String methodName = "deleteFunction";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/functions/{0}";

        callDeleteRESTCall(methodName,
                           Object.class,
                           urlTemplate,
                           fullName);
    }


    /**
     * Get function.
     *
     * @param fullName fullName of the function
     * @return resulting function info
     * @throws PropertyServerException problem with the call
     */
    public FunctionInfo getFunction(String fullName) throws PropertyServerException
    {
        final String methodName = "getFunction";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/functions/{0}";

        return callGetRESTCall(methodName,
                               FunctionInfo.class,
                               urlTemplate,
                               fullName);
    }


    /**
     * Get all functions.
     *
     * @param catalogName name of the catalog
     * @param schemaName name of the schema
     * @return resulting function info
     * @throws PropertyServerException problem with the call
     */
    public List<FunctionInfo> listFunctions(String catalogName,
                                            String schemaName) throws PropertyServerException
    {
        final String methodName = "listFunction";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/functions?catalog_name={1}&schema_name={2}";

        ListFunctionsResponse response = callGetRESTCall(methodName,
                                                         ListFunctionsResponse.class,
                                                         urlTemplate,
                                                         catalogName,
                                                         schemaName);

        if (response != null)
        {
            return response.getFunctions();
        }

        return null;
    }


    /*
     *===========================================================================
     * Specialized methods - Registered Models and versions
     */


    /**
     * Create model.
     *
     * @param name name of the model
     * @param catalogName name of catalog
     * @param schemaName name of the schema
     * @param comment description of the model
     * @param storageLocation directory name eg file:///Users/me/Code/uc/unitycatalog/etc/data/external/unity/default/models/
     * @return resulting model info
     * @throws PropertyServerException problem with the call
     */
    public RegisteredModelInfo createRegisteredModel(String name,
                                                     String catalogName,
                                                     String schemaName,
                                                     String comment,
                                                     String storageLocation) throws PropertyServerException
    {
        RegisteredModelProperties properties = new RegisteredModelProperties();

        properties.setName(name);
        properties.setCatalog_name(catalogName);
        properties.setSchema_name(schemaName);
        properties.setComment(comment);
        properties.setStorage_location(storageLocation);

        return createRegisteredModel(properties);
    }


    /**
     * Create registered model.
     *
     * @param registeredModelProperties properties
     *
     * @return resulting registered info
     * @throws PropertyServerException problem with the call
     */
    public RegisteredModelInfo createRegisteredModel(RegisteredModelProperties registeredModelProperties) throws PropertyServerException
    {
        final String methodName = "createRegisteredModel";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/models";

        return callPostRESTCallNoParams(methodName,
                                        RegisteredModelInfo.class,
                                        urlTemplate,
                                        registeredModelProperties);
    }


    /**
     * Delete model.
     *
     * @param fullName fullName of the model
     * @throws PropertyServerException problem with the call
     */
    public void deleteRegisteredModel(String  fullName) throws PropertyServerException
    {
        final String methodName = "deleteRegisteredModel";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/models/{0}";

        callDeleteRESTCall(methodName,
                           Object.class,
                           urlTemplate,
                           fullName);
    }


    /**
     * Get registered model.
     *
     * @param fullName fullName of the model
     * @return resulting model info
     * @throws PropertyServerException problem with the call
     */
    public RegisteredModelInfo getRegisteredModel(String fullName) throws PropertyServerException
    {
        final String methodName = "getVolume";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/models/{0}";

        return callGetRESTCall(methodName,
                               RegisteredModelInfo.class,
                               urlTemplate,
                               fullName);
    }


    /**
     * Get all registered models.
     *
     * @param catalogName name of the catalog
     * @param schemaName name of the schema
     * @return resulting model info
     * @throws PropertyServerException problem with the call
     */
    public List<RegisteredModelInfo> listRegisteredModels(String catalogName,
                                                          String schemaName) throws PropertyServerException
    {
        final String methodName = "listRegisteredModel";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/models?catalog_name={1}&schema_name={2}";

        ListRegisteredModelsResponse response = callGetRESTCall(methodName,
                                                                ListRegisteredModelsResponse.class,
                                                                urlTemplate,
                                                                catalogName,
                                                                schemaName);

        if (response != null)
        {
            return response.getRegistered_models();
        }

        return null;
    }


    /**
     * Update registered model.
     *
     * @param fullName name of the model
     * @param newName optional new name of the model
     * @param comment description of the model
     * @return resulting model info
     * @throws PropertyServerException problem with the call
     */
    public RegisteredModelInfo updateRegisteredModel(String              fullName,
                                                     String              newName,
                                                     String              comment) throws PropertyServerException
    {
        final String methodName  = "updateRegisteredModel";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/models/{0}";

        String oldName = this.getNameFromFullName(fullName);

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setComment(comment);

        if ((newName == null) || (newName.equals(oldName)))
        {
            /*
             * new_name must not be null.
             */
            requestBody.setNew_name(oldName + "TemporaryWorkaroundName");
            callPatchRESTCall(methodName,
                              RegisteredModelInfo.class,
                              urlTemplate,
                              requestBody,
                              fullName);

            requestBody.setNew_name(oldName);
            return callPatchRESTCall(methodName,
                                     RegisteredModelInfo.class,
                                     urlTemplate,
                                     requestBody,
                                     fullName);
        }
        else
        {
            requestBody.setNew_name(newName);
            return callPatchRESTCall(methodName,
                                     RegisteredModelInfo.class,
                                     urlTemplate,
                                     requestBody,
                                     fullName);
        }
    }



    /**
     * Create registered model version.
     *
     * @param modelVersionProperties properties
     *
     * @return resulting registered info
     * @throws PropertyServerException problem with the call
     */
    public ModelVersionInfo createModelVersion(ModelVersionProperties modelVersionProperties) throws PropertyServerException
    {
        final String methodName = "createModelVersion";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/models/versions";

        return callPostRESTCallNoParams(methodName,
                                        ModelVersionInfo.class,
                                        urlTemplate,
                                        modelVersionProperties);
    }


    /**
     * Get registered model version.
     *
     * @param fullName fullName of the model
     * @param version version to work with
     * @return resulting model info
     * @throws PropertyServerException problem with the call
     */
    public ModelVersionInfo getModelVersion(String fullName,
                                            long   version) throws PropertyServerException
    {
        final String methodName = "getVolume";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/models/{0}/versions/{1}";

        return callGetRESTCall(methodName,
                               ModelVersionInfo.class,
                               urlTemplate,
                               fullName,
                               version);
    }


    /**
     * Get all versions of model.
     *
     * @param fullName name of the model
     * @return resulting model info
     * @throws PropertyServerException problem with the call
     */
    public List<ModelVersionInfo> listModelVersions(String fullName) throws PropertyServerException
    {
        final String methodName = "listModelVersions";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/models/{0}/versions";

        ListModelVersionsResponse response = callGetRESTCall(methodName,
                                                             ListModelVersionsResponse.class,
                                                             urlTemplate,
                                                             fullName);

        if (response != null)
        {
            return response.getModel_versions();
        }

        return null;
    }


    /**
     * Update registered model version.
     *
     * @param fullName name of the model
     * @param version version to work with
     * @param comment description of the model
     * @return resulting model info
     * @throws PropertyServerException problem with the call
     */
    public ModelVersionInfo updateModelVersion(String              fullName,
                                               long                version,
                                               String              comment) throws PropertyServerException
    {
        final String methodName  = "updateModelVersion";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/models/{0}/versions/{1}";

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setComment(comment);

        return callPatchRESTCall(methodName,
                                 ModelVersionInfo.class,
                                 urlTemplate,
                                 requestBody,
                                 fullName,
                                 version);
    }


    /**
     * Delete model version.
     *
     * @param fullName fullName of the model
     * @param version version to work with
     * @throws PropertyServerException problem with the call
     */
    public void deleteModelVersion(String  fullName,
                                   long    version) throws PropertyServerException
    {
        final String methodName = "deleteModelVersion";
        final String urlTemplate = targetRootURL + "/api/2.1/unity-catalog/models/{0}/versions/{1}";

        callDeleteRESTCall(methodName,
                           Object.class,
                           urlTemplate,
                           fullName,
                           version);
    }



    /*
     *===========================================================================
     * REST Calls
     */

    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private   <T> T callGetRESTCallNoParams(String    methodName,
                                            Class<T>  returnClass,
                                            String    urlTemplate) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Issue a GET REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callGetRESTCall(String    methodName,
                                     Class<T>  returnClass,
                                     String    urlTemplate,
                                     Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callGetRESTCall(methodName, returnClass, urlTemplate, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Issue a POST REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private   <T> T callPostRESTCallNoParams(String    methodName,
                                             Class<T>  returnClass,
                                             String    urlTemplate,
                                             Object    requestBody) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPostRESTCallNoParams(methodName, returnClass, urlTemplate, requestBody);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Issue a PATCH REST call that returns a response object.  This is typically a create, update, or find with
     * complex parameters.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters.
     * @param requestBody request body for the request.
     * @param params  a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callPatchRESTCall(String    methodName,
                                       Class<T>  returnClass,
                                       String    urlTemplate,
                                       Object    requestBody,
                                       Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callPatchRESTCall(methodName, returnClass, urlTemplate, requestBody, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Issue a Delete REST call that returns a response object.
     *
     * @param <T> return type
     * @param methodName  name of the method being called.
     * @param returnClass class of the response object.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    private    <T> T callDeleteRESTCall(String    methodName,
                                        Class<T>  returnClass,
                                        String    urlTemplate,
                                        Object... params) throws PropertyServerException
    {
        try
        {
            return clientConnector.callDeleteRESTCall(methodName, returnClass, urlTemplate, null, params);
        }
        catch (Exception error)
        {
            logRESTCallException(methodName, true, error);
        }

        return null;
    }


    /**
     * Provide detailed logging for exceptions.
     *
     * @param methodName calling method
     * @param logMessage should log message
     * @param error resulting exception
     * @throws PropertyServerException wrapping exception
     */
    private void logRESTCallException(String    methodName,
                                      boolean   logMessage,
                                      Exception error) throws PropertyServerException
    {
        if ((auditLog != null) && (logMessage))
        {
            auditLog.logMessage(methodName,
                                UCAuditCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                            ucInstanceName,
                                                                                            targetRootURL,
                                                                                            error.getMessage()));
        }

        throw new PropertyServerException(UCErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                      ucInstanceName,
                                                                                                      targetRootURL,
                                                                                                      error.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          error);
    }
}
