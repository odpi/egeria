/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFlow;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessContainmentType;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates lineage test data, and triggers requests via client for types involved in lineage
 */
public class LineageSetupService {

    private static final String DISPLAY_NAME_SUFFIX = "displayName";
    private static final String SEPARATOR = "_";
    private static final String ATTRIBUTE = "attribute";
    private static final String PORT_IMPLEMENTATION = "portImplementation";
    private static final String SCHEMA_TYPE = "schemaType";
    private static final String JOB_PROCESS = "jobProcess";
    private static final String STAGE_PROCESS = "stageProcess";

    private static final String VIRTUAL = "virtual";
    private static final String CSV = "csv";
    private static final String DATABASE = "database";

    private static final String JOB_NAME = "flow1";

    private static final String FIRST_STAGE_PROCESS_NAME = "fileInput";
    private static final String FIRST_STAGE_INPUT_PORT_NAME = "names";
    private static final String FIRST_STAGE_OUTPUT_PORT_NAME = "readFile";

    private static final String SECOND_STAGE_PROCESS_NAME = "rename";
    private static final String SECOND_STAGE_INPUT_PORT_NAME = "readFile";
    private static final String SECOND_STAGE_OUTPUT_PORT_NAME = "writeTable";

    private static final String THIRD_STAGE_PROCESS_NAME = "databaseOutput";
    private static final String THIRD_STAGE_INPUT_PORT_NAME = "writeTable";
    private static final String THIRD_STAGE_OUTPUT_PORT_NAME = "employeeName";

    private final List<String> csvHeaderAttributeNames;
    private final List<String> databaseTableAttributeNames;

    private final Map<String, String> csvToDatabase = new HashMap<>();

    private final Map<String, List<String>> jobProcessDataFlowsProxies = new HashMap<>();

    public LineageSetupService() {
        csvToDatabase.put("last", "surname");
        csvToDatabase.put("location", "locid");
        csvToDatabase.put("id", "empid");
        csvToDatabase.put("first", "fname");

        csvHeaderAttributeNames = new ArrayList<>(csvToDatabase.keySet());
        databaseTableAttributeNames = new ArrayList<>(csvToDatabase.values());

        csvHeaderAttributeNames.forEach(headerAttribute -> jobProcessDataFlowsProxies.put(headerAttribute,
                getDataFlowAttributesForColumn(headerAttribute)));
    }

    /**
     * retrieves the list of qualified names for the attributes that are linked in data flows, in the order they can be
     * traversed from the CSV column to the DB column. This list is retrieved for each of the columns. This is the order in
     * which the process was constructed and intended to be and is used by the FVT to check the result of all the creation calls.
     *
     * @return a map of data flow attribute names in order for each CSV column
     */
    public Map<String, List<String>> getJobProcessDataFlowsProxiesByCsvColumn() {
        return jobProcessDataFlowsProxies;
    }

    /**
     * Creates the job process containing all the stage processes, port implementations, schemas, attributes and virtual assets
     *
     * @param userId               the user which created the job process
     * @param dataEngineOMASClient the data engine client that is used to create the job process
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws ConnectorCheckedException  there are errors in the initialization of the connector.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<Process> createJobProcessWithContent(String userId, DataEngineClient dataEngineOMASClient) throws InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  ConnectorCheckedException {
        createVirtualAssets(userId, dataEngineOMASClient);

        List<Process> processes = new ArrayList<>();
        processes.add(createFirstStageProcess(userId, dataEngineOMASClient));
        processes.add(createSecondStageProcess(userId, dataEngineOMASClient));
        processes.add(createThirdStageProcess(userId, dataEngineOMASClient));
        processes.add(createJobProcess(userId, dataEngineOMASClient));
        return processes;
    }

    private List<String> getDataFlowAttributesForColumn(String name) {
        List<String> attributes = new ArrayList<>();
        attributes.add(getAttributeName(VIRTUAL, CSV, PortType.INOUT_PORT.getName(), name));
        attributes.add(getAttributeName(FIRST_STAGE_PROCESS_NAME, FIRST_STAGE_INPUT_PORT_NAME, PortType.INPUT_PORT.getName(), name));
        attributes.add(getAttributeName(FIRST_STAGE_PROCESS_NAME, FIRST_STAGE_OUTPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(), name));
        attributes.add(getAttributeName(SECOND_STAGE_PROCESS_NAME, SECOND_STAGE_INPUT_PORT_NAME, PortType.INPUT_PORT.getName(), name));
        String newName = csvToDatabase.get(name);
        attributes.add(getAttributeName(SECOND_STAGE_PROCESS_NAME, SECOND_STAGE_OUTPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(), newName));
        attributes.add(getAttributeName(THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_INPUT_PORT_NAME, PortType.INPUT_PORT.getName(), newName));
        attributes.add(getAttributeName(THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_OUTPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(), newName));
        attributes.add(getAttributeName(VIRTUAL, DATABASE, PortType.INOUT_PORT.getName(), newName));
        return attributes;
    }

    public void createVirtualAssets(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        List<Attribute> csvAttributes = createAttributes(csvHeaderAttributeNames, VIRTUAL, CSV, PortType.INOUT_PORT);
        SchemaType csvSchemaType = createSchemaType(VIRTUAL, CSV, csvAttributes);
        dataEngineOMASClient.createOrUpdateSchemaType(userId, csvSchemaType);

        List<Attribute> databaseAttributes = createAttributes(databaseTableAttributeNames, VIRTUAL, DATABASE, PortType.INOUT_PORT);
        SchemaType dbSchemaType = createSchemaType(VIRTUAL, DATABASE, databaseAttributes);
        dataEngineOMASClient.createOrUpdateSchemaType(userId, dbSchemaType);
    }

    public DataFlow createDataFlow(String dataSupplier, String dataConsumer) {
        DataFlow dataFlow = new DataFlow();
        dataFlow.setDataSupplier(dataSupplier);
        dataFlow.setDataConsumer(dataConsumer);
        return dataFlow;
    }

    private Process createJobProcess(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        Process job = new Process();
        String name = getJobProcessName();
        job.setQualifiedName(name);
        job.setDisplayName(getDisplayName(name));

        List<DataFlow> dataFlows = createDataFlows(FIRST_STAGE_PROCESS_NAME, SECOND_STAGE_PROCESS_NAME,
                FIRST_STAGE_OUTPUT_PORT_NAME, SECOND_STAGE_INPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(),
                PortType.INPUT_PORT.getName(), csvHeaderAttributeNames);
        dataFlows.addAll(createDataFlows(SECOND_STAGE_PROCESS_NAME, THIRD_STAGE_PROCESS_NAME,
                SECOND_STAGE_OUTPUT_PORT_NAME, THIRD_STAGE_INPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(),
                PortType.INPUT_PORT.getName(), databaseTableAttributeNames));

        dataEngineOMASClient.createOrUpdateProcess(userId, job);
        dataEngineOMASClient.addDataFlows(userId, dataFlows);

        List<ProcessHierarchy> processHierarchies = new ArrayList<>();
        processHierarchies.add(getProcessHierarchy(getStageProcessName(FIRST_STAGE_PROCESS_NAME), name));
        processHierarchies.add(getProcessHierarchy(getStageProcessName(SECOND_STAGE_PROCESS_NAME), name));
        processHierarchies.add(getProcessHierarchy(getStageProcessName(THIRD_STAGE_PROCESS_NAME), name));
        for(ProcessHierarchy processHierarchy : processHierarchies) {
            dataEngineOMASClient.addProcessHierarchy(userId, processHierarchy);
        }

        return job;
    }

    private ProcessHierarchy getProcessHierarchy(String childQualifiedName, String parentQualifiedName) {
        ProcessHierarchy processHierarchy = new ProcessHierarchy();
        processHierarchy.setChildProcess(childQualifiedName);
        processHierarchy.setParentProcess(parentQualifiedName);
        processHierarchy.setProcessContainmentType(ProcessContainmentType.OWNED);

        return processHierarchy;
    }

    private Process createFirstStageProcess(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        Process firstStageProcess = createStageProcess(FIRST_STAGE_PROCESS_NAME, FIRST_STAGE_INPUT_PORT_NAME, FIRST_STAGE_OUTPUT_PORT_NAME,
                csvHeaderAttributeNames, csvHeaderAttributeNames);

        List<DataFlow> dataFlows = createDataFlows(VIRTUAL, FIRST_STAGE_PROCESS_NAME,
                CSV, FIRST_STAGE_INPUT_PORT_NAME, PortType.INOUT_PORT.getName(), PortType.INPUT_PORT.getName(), csvHeaderAttributeNames);

        dataFlows.addAll(createDataFlows(FIRST_STAGE_PROCESS_NAME, FIRST_STAGE_PROCESS_NAME,
                FIRST_STAGE_INPUT_PORT_NAME, FIRST_STAGE_OUTPUT_PORT_NAME, PortType.INPUT_PORT.getName(),
                PortType.OUTPUT_PORT.getName(), csvHeaderAttributeNames));

        dataEngineOMASClient.createOrUpdateProcess(userId, firstStageProcess);
        dataEngineOMASClient.addDataFlows(userId, dataFlows);
        return firstStageProcess;
    }

    private Process createSecondStageProcess(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        Process secondStageProcess = createStageProcess(SECOND_STAGE_PROCESS_NAME, SECOND_STAGE_INPUT_PORT_NAME, SECOND_STAGE_OUTPUT_PORT_NAME,
                csvHeaderAttributeNames, databaseTableAttributeNames);

        List<DataFlow> dataFlows = new ArrayList<>();
        csvToDatabase.forEach((csvColumn, databaseColumn) -> {
            String sourceName = getAttributeName(SECOND_STAGE_PROCESS_NAME, SECOND_STAGE_INPUT_PORT_NAME, PortType.INPUT_PORT.getName(), csvColumn);
            String targetName =
                    getAttributeName(SECOND_STAGE_PROCESS_NAME, SECOND_STAGE_OUTPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(), databaseColumn);
            DataFlow dataFlow = createDataFlow(sourceName, targetName);
            dataFlows.add(dataFlow);
        });

        dataEngineOMASClient.createOrUpdateProcess(userId, secondStageProcess);
        dataEngineOMASClient.addDataFlows(userId, dataFlows);
        return secondStageProcess;
    }

    private Process createThirdStageProcess(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        Process thirdStageProcess = createStageProcess(THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_INPUT_PORT_NAME, THIRD_STAGE_OUTPUT_PORT_NAME,
                databaseTableAttributeNames, databaseTableAttributeNames);

        List<DataFlow> dataFlows = createDataFlows(THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_INPUT_PORT_NAME,
                THIRD_STAGE_OUTPUT_PORT_NAME, PortType.INPUT_PORT.getName(), PortType.OUTPUT_PORT.getName(), databaseTableAttributeNames);

        dataFlows.addAll(createDataFlows(THIRD_STAGE_PROCESS_NAME, VIRTUAL, THIRD_STAGE_OUTPUT_PORT_NAME,
                DATABASE, PortType.OUTPUT_PORT.getName(), PortType.INOUT_PORT.getName(), databaseTableAttributeNames));

        dataEngineOMASClient.createOrUpdateProcess(userId, thirdStageProcess);
        dataEngineOMASClient.addDataFlows(userId, dataFlows);
        return thirdStageProcess;
    }

    private List<DataFlow> createDataFlows(String sourceProcessName, String targetProcessName, String sourcePort, String targetPort,
                                           String sourcePortType, String targetPortType, List<String> attributeNames) {
        List<DataFlow> dataFlows = new ArrayList<>();
        attributeNames.forEach(csvColumn -> {
            String sourceName = getAttributeName(sourceProcessName, sourcePort, sourcePortType, csvColumn);
            String targetName = getAttributeName(targetProcessName, targetPort, targetPortType, csvColumn);
            DataFlow dataFlow = createDataFlow(sourceName, targetName);
            dataFlows.add(dataFlow);
        });
        return dataFlows;
    }

    private Process createStageProcess(String processName, String inputPortName, String outputPortName, List<String> inputAttributeNames,
                                       List<String> outputAttributeNames) {
        PortImplementation inputPortImplementation =
                createPortImplementationWithSchema(inputAttributeNames, processName, inputPortName, PortType.INPUT_PORT);
        PortImplementation outputPortImplementation =
                createPortImplementationWithSchema(outputAttributeNames, processName, outputPortName, PortType.OUTPUT_PORT);
        List<PortImplementation> portImplementations = Arrays.asList(inputPortImplementation, outputPortImplementation);
        Process process = new Process();
        String name = getStageProcessName(processName);
        process.setQualifiedName(name);
        process.setDisplayName(getDisplayName(name));
        process.setPortImplementations(portImplementations);
        return process;
    }

    private PortImplementation createPortImplementationWithSchema(List<String> attributeNames, String processName, String portName,
                                                                  PortType portType) {
        List<Attribute> attributes = createAttributes(attributeNames, processName, portName, portType);
        SchemaType schemaType = createSchemaType(processName, portName, attributes);
        return createPortImplementation(processName, portName, schemaType, portType);
    }

    private PortImplementation createPortImplementation(String processName, String portName, SchemaType schemaType, PortType portType) {
        PortImplementation portImplementation = new PortImplementation();
        portImplementation.setSchemaType(schemaType);
        String name = getPortImplementationName(processName, portName);
        portImplementation.setQualifiedName(name);
        portImplementation.setDisplayName(name + SEPARATOR);
        portImplementation.setPortType(portType);
        return portImplementation;
    }

    private SchemaType createSchemaType(String processName, String portName, List<Attribute> attributes) {
        SchemaType schemaType = new SchemaType();
        String name = getSchemaTypeName(processName, portName);
        schemaType.setQualifiedName(name);
        schemaType.setDisplayName(getDisplayName(name));
        schemaType.setAttributeList(attributes);
        return schemaType;
    }

    private List<Attribute> createAttributes(List<String> attributeNames, String processName, String portName, PortType portType) {
        List<Attribute> attributes = new ArrayList<>();
        attributeNames.forEach(attributeName -> {
            Attribute attribute = new Attribute();
            String name = getAttributeName(processName, portName, portType.getName(), attributeName);
            attribute.setQualifiedName(name);
            attribute.setDisplayName(getDisplayName(name));
            attributes.add(attribute);
        });
        return attributes;
    }

    private String getJobProcessName() {
        return JOB_PROCESS + SEPARATOR + JOB_NAME;
    }

    private String getStageProcessName(String processName) {
        return STAGE_PROCESS + SEPARATOR + processName;
    }

    private String getPortImplementationName(String processName, String portName) {
        return String.join(SEPARATOR, PORT_IMPLEMENTATION, processName, portName);
    }

    private String getSchemaTypeName(String processName, String portName) {
        return String.join(SEPARATOR, SCHEMA_TYPE, processName, portName);
    }

    private String getAttributeName(String processName, String portName, String portType, String attributeName) {
        return String.join(SEPARATOR, ATTRIBUTE, processName, portName, portType, attributeName);
    }

    private String getDisplayName(String name) {
        return name + SEPARATOR + DISPLAY_NAME_SUFFIX;
    }

}
