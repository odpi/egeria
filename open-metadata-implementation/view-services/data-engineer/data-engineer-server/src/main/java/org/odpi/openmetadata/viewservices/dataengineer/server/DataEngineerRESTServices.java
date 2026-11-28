/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dataengineer.server;


import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.DataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.DataValueSourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.EmbeddedDataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.ProcessingVariableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DataValueHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataDataSetConnectorBase;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.TabularDataSetReportResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataSetReport;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The DataEngineerRESTServices provides the server-side implementation of the Data Engineer Open Metadata
 * View Service (OMVS).
 */
public class DataEngineerRESTServices extends TokenController
{
    private static final DataEngineerInstanceHandler instanceHandler = new DataEngineerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(DataEngineerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public DataEngineerRESTServices()
    {
    }


    /**
     * Returns details of a tabular data set including the data rows that match the startFromRow and maxRowCount values.
     * If maxRowCount=0 then no data records are returned.
     *
     * @param serverName     name of called server
     * @param tabularDataSetGUID     unique identifier of tabular dat set asset
     * @param startFromRow    cursor position in the data set
     * @param maxRowCount maximum number of rows to return.  0 means no rows.
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public TabularDataSetReportResponse getTabularDataSetReport(String serverName,
                                                                String tabularDataSetGUID,
                                                                int    startFromRow,
                                                                int    maxRowCount)
    {
        final String methodName = "getTabularDataSetReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TabularDataSetReportResponse response = new TabularDataSetReportResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ConnectedAssetClient connectedAssetClient = instanceHandler.getConnectedAssetClient(userId, serverName, methodName);

            Connector connector = connectedAssetClient.getConnectorForAsset(userId, tabularDataSetGUID, auditLog);

            if (connector instanceof OpenMetadataDataSetConnectorBase openMetadataDataSetConnectorBase)
            {
                openMetadataDataSetConnectorBase.setLocalEnvironment(userId, serverName, instanceHandler.getServiceName());
            }

            if (connector instanceof ReadableTabularDataSource readableTabularDataSource)
            {
                connector.start();

                TabularDataSetReport tabularDataSetReport = new TabularDataSetReport();

                tabularDataSetReport.setRecordCount(readableTabularDataSource.getRecordCount());
                tabularDataSetReport.setTableName(readableTabularDataSource.getTableName());
                tabularDataSetReport.setTableDescription(readableTabularDataSource.getTableDescription());
                tabularDataSetReport.setColumnDescriptions(readableTabularDataSource.getColumnDescriptions());

                if (maxRowCount > 0)
                {
                    Map<String, List<String>> tabularDataRows = new HashMap<>();

                    for (int i = startFromRow; ((i < startFromRow + maxRowCount) &&
                                                (i < startFromRow + tabularDataSetReport.getRecordCount())); i++)
                    {
                        List<String> dataRecord = readableTabularDataSource.readRecord(i);

                        if (dataRecord != null)
                        {
                            tabularDataRows.put(String.valueOf(i), dataRecord);
                        }
                    }

                    tabularDataSetReport.setDataRecords(tabularDataRows);
                }

                response.setTabularDataSetReport(tabularDataSetReport);

                connector.disconnect();
            }

            else

            {

                restExceptionHandler.handleInvalidCallToServer(ReadableTabularDataSource.class.getName(),

                                                               methodName,

                                                               tabularDataSetGUID,

                                                               (connector == null) ? null : connector.getClass().getName());

            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /*
     * =====================================================================================================================
     * Data values
     */

    /**
     * Create a data value.
     *
     * @param serverName  name of the server to route the request to
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the newly created element or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public GUIDResponse createDataValue(String                serverName,
                                        NewElementRequestBody requestBody)
    {
        final String methodName = "createDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataValueProperties properties)
                {
                    response.setGUID(handler.createDataValue(userId, requestBody, requestBody.getInitialClassifications(), properties, requestBody.getParentRelationshipProperties()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataValueProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a new metadata element to represent a data value using an existing metadata element as a template.  The
     * template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName  name of the server to route the request to
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public GUIDResponse createDataValueFromTemplate(String              serverName,
                                                    TemplateRequestBody requestBody)
    {
        final String methodName = "createDataValueFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createDataValueFromTemplate(userId, requestBody, requestBody.getTemplateGUID(), requestBody.getReplacementProperties(), requestBody.getReplacementClassifications(), requestBody.getPlaceholderPropertyValues(), requestBody.getParentRelationshipProperties()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a data value.
     *
     * @param serverName    name of the server to route the request to
     * @param dataValueGUID unique identifier of the data value
     * @param requestBody   properties for the element
     *
     * @return boolean - true if an update occurred or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public BooleanResponse updateDataValue(String                   serverName,
                                           String                   dataValueGUID,
                                           UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataValueProperties properties)
                {
                    response.setFlag(handler.updateDataValue(userId, dataValueGUID, requestBody, properties));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setFlag(handler.updateDataValue(userId, dataValueGUID, requestBody, null));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataValueProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Delete a data value.
     *
     * @param serverName    name of the server to route the request to
     * @param dataValueGUID unique identifier of the data value
     * @param requestBody   options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public VoidResponse deleteDataValue(String                   serverName,
                                        String                   dataValueGUID,
                                        DeleteElementRequestBody requestBody)
    {
        final String methodName = "deleteDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            handler.deleteDataValue(userId, dataValueGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Returns the list of data values with a particular name.
     *
     * @param serverName  name of the server to route the request to
     * @param requestBody name to search for and the query options
     *
     * @return a list of elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public OpenMetadataRootElementsResponse getDataValuesByName(String            serverName,
                                                                FilterRequestBody requestBody)
    {
        final String methodName = "getDataValuesByName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getDataValuesByName(userId, requestBody.getFilter(), requestBody));
            }
            else
            {
                response.setElements(handler.getDataValuesByName(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return the properties of a specific data value.
     *
     * @param serverName    name of the server to route the request to
     * @param dataValueGUID unique identifier of the data value
     * @param requestBody   options for the retrieve
     *
     * @return retrieved properties or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public OpenMetadataRootElementResponse getDataValueByGUID(String         serverName,
                                                              String         dataValueGUID,
                                                              GetRequestBody requestBody)
    {
        final String methodName = "getDataValueByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            response.setElement(handler.getDataValueByGUID(userId, dataValueGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of data value metadata elements that contain the search string.
     *
     * @param serverName  name of the server to route the request to
     * @param requestBody search string and the query options
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public OpenMetadataRootElementsResponse findDataValues(String                  serverName,
                                                           SearchStringRequestBody requestBody)
    {
        final String methodName = "findDataValues";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findDataValues(userId, requestBody.getSearchString(), requestBody));
            }
            else
            {
                response.setElements(handler.findDataValues(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach a data value to the asset that describes the digital resource where the data is stored.
     *
     * @param serverName    name of the server to route the request to
     * @param dataValueGUID unique identifier of the data value
     * @param assetGUID     unique identifier of the asset for the digital resource where the data is stored
     * @param requestBody   properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public VoidResponse linkDataValueSource(String                     serverName,
                                            String                     dataValueGUID,
                                            String                     assetGUID,
                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkDataValueSource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkDataValueSource(userId, dataValueGUID, assetGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof DataValueSourceProperties properties)
            {
                handler.linkDataValueSource(userId, dataValueGUID, assetGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkDataValueSource(userId, dataValueGUID, assetGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(DataValueSourceProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach a data value from the asset that describes the digital resource where the data is stored.
     *
     * @param serverName    name of the server to route the request to
     * @param dataValueGUID unique identifier of the data value
     * @param assetGUID     unique identifier of the asset for the digital resource where the data is stored
     * @param requestBody   options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public VoidResponse detachDataValueSource(String                        serverName,
                                              String                        dataValueGUID,
                                              String                        assetGUID,
                                              DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachDataValueSource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            handler.detachDataValueSource(userId, dataValueGUID, assetGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Attach an element (such as a process) to a data value that is used to configure its processing.
     *
     * @param serverName    name of the server to route the request to
     * @param elementGUID   unique identifier of the element that uses the data value
     * @param dataValueGUID unique identifier of the data value
     * @param requestBody   properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public VoidResponse linkProcessingVariable(String                     serverName,
                                               String                     elementGUID,
                                               String                     dataValueGUID,
                                               NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkProcessingVariable";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkProcessingVariable(userId, elementGUID, dataValueGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof ProcessingVariableProperties properties)
            {
                handler.linkProcessingVariable(userId, elementGUID, dataValueGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkProcessingVariable(userId, elementGUID, dataValueGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ProcessingVariableProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Detach an element (such as a process) from a data value that is used to configure its processing.
     *
     * @param serverName    name of the server to route the request to
     * @param elementGUID   unique identifier of the element that uses the data value
     * @param dataValueGUID unique identifier of the data value
     * @param requestBody   options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public VoidResponse detachProcessingVariable(String                        serverName,
                                                 String                        elementGUID,
                                                 String                        dataValueGUID,
                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachProcessingVariable";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            handler.detachProcessingVariable(userId, elementGUID, dataValueGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Embed a data value in another data value.
     *
     * @param serverName            name of the server to route the request to
     * @param parentDataValueGUID   unique identifier of the parent data value
     * @param embeddedDataValueGUID unique identifier of the embedded data value
     * @param requestBody           properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public VoidResponse linkEmbeddedDataValue(String                     serverName,
                                              String                     parentDataValueGUID,
                                              String                     embeddedDataValueGUID,
                                              NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkEmbeddedDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.linkEmbeddedDataValue(userId, parentDataValueGUID, embeddedDataValueGUID, null, null);
            }
            else if (requestBody.getProperties() instanceof EmbeddedDataValueProperties properties)
            {
                handler.linkEmbeddedDataValue(userId, parentDataValueGUID, embeddedDataValueGUID, requestBody, properties);
            }
            else if (requestBody.getProperties() == null)
            {
                handler.linkEmbeddedDataValue(userId, parentDataValueGUID, embeddedDataValueGUID, requestBody, null);
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(EmbeddedDataValueProperties.class.getName(), methodName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove an embedded data value from its parent data value.
     *
     * @param serverName            name of the server to route the request to
     * @param parentDataValueGUID   unique identifier of the parent data value
     * @param embeddedDataValueGUID unique identifier of the embedded data value
     * @param requestBody           options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    public VoidResponse detachEmbeddedDataValue(String                        serverName,
                                                String                        parentDataValueGUID,
                                                String                        embeddedDataValueGUID,
                                                DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "detachEmbeddedDataValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            DataValueHandler handler = instanceHandler.getDataValueHandler(userId, serverName, methodName);

            handler.detachEmbeddedDataValue(userId, parentDataValueGUID, embeddedDataValueGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
