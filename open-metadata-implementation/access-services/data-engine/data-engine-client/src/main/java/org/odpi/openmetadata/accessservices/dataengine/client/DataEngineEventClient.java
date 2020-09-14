package org.odpi.openmetadata.accessservices.dataengine.client;

import org.odpi.openmetadata.accessservices.dataengine.connectors.intopic.DataEngineInTopicClientConnector;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineEventType;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineRegistrationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessesEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.SchemaTypeEvent;
import org.odpi.openmetadata.accessservices.dataengine.model.*;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/***
 * DataEngineEventClient implements Data Engine OMAS client side events interface using provided OMAS server details and topic connector
 */
public class DataEngineEventClient extends OCFRESTClient implements DataEngineClient {

    private static final Logger log = LoggerFactory.getLogger(DataEngineEventClient.class);
    private DataEngineInTopicClientConnector dataEngineInTopicClientConnector;
    private static String externalSource;


    /**
     * Constructor to create DataEngineEventClient with unauthenticated access to the server
     *
     * @param serverName            name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param dataEngineInTopicClientConnector topic connector used to publish to InTopic
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public DataEngineEventClient(String serverName, String serverPlatformURLRoot, String source, DataEngineInTopicClientConnector dataEngineInTopicClientConnector) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot);
        this.externalSource = source;

    }

    /***
     * Constructor to create DataEngineEventClient with authenticated user access to the server
     * @param serverName                name of the OMAG Server to call
     * @param serverPlatformURLRoot     URL root of the server platform where the OMAG Server is running
     * @param userName                  name of the user accessing the server
     * @param userPassword              password for the user accessing the server
     * @param dataEngineInTopicClientConnector topic connector used to publish to InTopic
     * @throws InvalidParameterException
     */
    public DataEngineEventClient(String serverName, String serverPlatformURLRoot, String userName, String userPassword, String source, DataEngineInTopicClientConnector dataEngineInTopicClientConnector) throws InvalidParameterException {
        super(serverName, serverPlatformURLRoot, userName, userPassword);
        this.dataEngineInTopicClientConnector = dataEngineInTopicClientConnector;
        this.externalSource = source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrUpdateProcess(String userId, Process process) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> createOrUpdateProcesses(String userId, List<Process> processes) throws InvalidParameterException, ConnectorCheckedException {

        ProcessesEvent event = new ProcessesEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.PROCESSES_EVENT);
        event.setProcesses(processes);

        try {
            dataEngineInTopicClientConnector.sendEvent(event);
        }  catch (ConnectorCheckedException e) {
            log.error(e.getMessage(),e);
            throw e;
        }
        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createExternalDataEngine(String userId, SoftwareServerCapability softwareServerCapability) throws InvalidParameterException, ConnectorCheckedException {

        DataEngineRegistrationEvent event = new DataEngineRegistrationEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.DATA_ENGINE_REGISTRATION_EVENT);
        event.setSoftwareServerCapability(softwareServerCapability);

        try {
            dataEngineInTopicClientConnector.sendEvent(event);
        } catch (ConnectorCheckedException e) {
            log.error(e.getMessage(),e);
            throw e;
        }

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrUpdateSchemaType(String userId, SchemaType schemaType) throws InvalidParameterException, ConnectorCheckedException  {

        SchemaTypeEvent event = new SchemaTypeEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.SCHEMA_TYPE_EVENT);
        event.setSchemaType(schemaType);

        try {
            dataEngineInTopicClientConnector.sendEvent(event);
        } catch (ConnectorCheckedException e) {
            log.error(e.getMessage(),e);
            throw e;
        }

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrUpdatePortImplementation(String userId, PortImplementation portImplementation) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrUpdatePortAlias(String userId, PortAlias portAlias) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addLineageMappings(String userId, List<LineageMapping> lineageMappings) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPortsToProcess(String userId, List<String> portGUIDs, String processGUID) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {

    }
}
