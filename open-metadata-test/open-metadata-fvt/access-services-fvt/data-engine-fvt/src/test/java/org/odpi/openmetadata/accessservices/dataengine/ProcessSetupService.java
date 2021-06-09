/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine;

import org.odpi.openmetadata.accessservices.dataengine.client.DataEngineClient;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class is used in the DataEngineFVT in order to generate a job process containing stages, port implementations with
 * their schemas and attributes. It creates virtual assets fora CSV file with 4 columns and a database table with the same
 * number of columns. The process contains 3 stages which read from the CSV, rename the columns, then write the values into a
 * database table.
 * The class also helps the setup with creating an external data engine using a SoftwareServerCapability object.
 */
public class ProcessSetupService {
    private static final String DISPLAY_NAME_SUFFIX = "displayName";
    private static final String SEPARATOR = "_";
    private static final String ATTRIBUTE = "attribute";
    private static final String PORT_IMPLEMENTATION = "portImplementation";
    private static final String PORT_ALIAS = "portAlias";
    private static final String SCHEMA_TYPE = "schemaType";
    private static final String JOB_PROCESS = "jobProcess";
    private static final String STAGE_PROCESS = "stageProcess";

    private static final String VIRTUAL = "virtual";
    private static final String CSV = "csv";
    private static final String DATABASE = "database";

    private static final String DATA_ENGINE_DISPLAY_NAME = "Data Engine Display Name";
    private static final String DATA_ENGINE_NAME = "DataEngine";
    private static final String DATA_ENGINE_DESCRIPTION = "Data Engine Description";
    private static final String DATA_ENGINE_TYPE = "DataEngine";
    private static final String ENGINE_VERSION = "1";
    private static final String PATCH_LEVEL = "2";
    private static final String SOURCE = "source";

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

    private final Map<String, List<String>> jobProcessLineageMappingsProxies = new HashMap<>();

    public ProcessSetupService() {
        csvToDatabase.put("last", "surname");
        csvToDatabase.put("location", "locid");
        csvToDatabase.put("id", "empid");
        csvToDatabase.put("first", "fname");

        csvHeaderAttributeNames = new ArrayList<>(csvToDatabase.keySet());
        databaseTableAttributeNames = new ArrayList<>(csvToDatabase.values());

        csvHeaderAttributeNames.forEach(headerAttribute -> jobProcessLineageMappingsProxies.put(headerAttribute,
                getLineageMappingAttributesForColumn(headerAttribute)));
    }

    /** retrieves the list of qualified names for the attributes that are linked in lineage mappings, in the order they can be
     * traversed from the CSV column to the DB column. This list is retrieved for each of the columns. This is the order in
     * which the process was constructed and intended to be and is used by the FVT to check the result of all the creation calls.
     * @return a list of lineage mapping attribute names in order for each CSV column
     */
    public Map<String, List<String>> getJobProcessLineageMappingsProxiesByCsvColumn() {
        return jobProcessLineageMappingsProxies;
    }

    /**
     * Registers an external data engine source.
     *
     * @param userId               the user which creates the data engine
     * @param dataEngineOMASClient the data engine client that is used to create the external data engine
     * @return the software server capability used to create the external data source that is needed for checks inside the FVT
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public SoftwareServerCapability createExternalDataEngine(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException, ConnectorCheckedException {
        SoftwareServerCapability softwareServerCapability = new SoftwareServerCapability();
        softwareServerCapability.setName(DATA_ENGINE_DISPLAY_NAME);
        softwareServerCapability.setQualifiedName(DATA_ENGINE_NAME);
        softwareServerCapability.setDescription(DATA_ENGINE_DESCRIPTION);
        softwareServerCapability.setEngineType(DATA_ENGINE_TYPE);
        softwareServerCapability.setEngineVersion(ENGINE_VERSION);
        softwareServerCapability.setPatchLevel(PATCH_LEVEL);
        softwareServerCapability.setSource(SOURCE);
        dataEngineOMASClient.createExternalDataEngine(userId, softwareServerCapability);
        return softwareServerCapability;
    }

    /** Creates the job process containing all the stage processes, port implementations, schemas, attributes and virtual assets
     * @param userId the user which created the job process
     * @param dataEngineOMASClient the data engine client that is used to create the job process
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void createJobProcessWithContent(String userId, DataEngineClient dataEngineOMASClient) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        createVirtualAssets(userId, dataEngineOMASClient);

        createFirstStageProcess(userId, dataEngineOMASClient);
        createSecondStageProcess(userId, dataEngineOMASClient);
        createThirdStageProcess(userId, dataEngineOMASClient);

        createJobProcess(userId, dataEngineOMASClient);
    }

    private List<String> getLineageMappingAttributesForColumn(String name) {
        List<String> lineageAttributes = new ArrayList<>();
        lineageAttributes.add(getAttributeName(VIRTUAL, CSV, PortType.INOUT_PORT.getName(), name));
        lineageAttributes.add(getAttributeName(FIRST_STAGE_PROCESS_NAME, FIRST_STAGE_INPUT_PORT_NAME, PortType.INPUT_PORT.getName(), name));
        lineageAttributes.add(getAttributeName(FIRST_STAGE_PROCESS_NAME, FIRST_STAGE_OUTPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(), name));
        lineageAttributes.add(getAttributeName(SECOND_STAGE_PROCESS_NAME, SECOND_STAGE_INPUT_PORT_NAME, PortType.INPUT_PORT.getName(), name));
        String newName = csvToDatabase.get(name);
        lineageAttributes.add(getAttributeName(SECOND_STAGE_PROCESS_NAME, SECOND_STAGE_OUTPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(), newName));
        lineageAttributes.add(getAttributeName(THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_INPUT_PORT_NAME, PortType.INPUT_PORT.getName(), newName));
        lineageAttributes.add(getAttributeName(THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_OUTPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(), newName));
        lineageAttributes.add(getAttributeName(VIRTUAL, DATABASE, PortType.INOUT_PORT.getName(), newName));
        return lineageAttributes;
    }

    private void createVirtualAssets(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        List<Attribute> csvAttributes = createAttributes(csvHeaderAttributeNames, VIRTUAL, CSV, PortType.INOUT_PORT);
        SchemaType csvSchemaType = createSchemaType(VIRTUAL, CSV, csvAttributes);
        dataEngineOMASClient.createOrUpdateSchemaType(userId, csvSchemaType);

        List<Attribute> databaseAttributes = createAttributes(databaseTableAttributeNames, VIRTUAL, DATABASE, PortType.INOUT_PORT);
        SchemaType dbSchemaType = createSchemaType(VIRTUAL, DATABASE, databaseAttributes);
        dataEngineOMASClient.createOrUpdateSchemaType(userId, dbSchemaType);
    }

    private LineageMapping createLineageMapping(String sourceName, String targetName) {
        LineageMapping lineageMapping = new LineageMapping();
        lineageMapping.setSourceAttribute(sourceName);
        lineageMapping.setTargetAttribute(targetName);
        return lineageMapping;
    }

    private void createJobProcess(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        Process job = new Process();
        String name = getJobProcessName();
        job.setQualifiedName(name);
        job.setDisplayName(getDisplayName(name));

        String inputPortAliasDelegation = getPortImplementationName(FIRST_STAGE_PROCESS_NAME, FIRST_STAGE_INPUT_PORT_NAME);
        PortAlias inputPortAlias = createPortAlias(inputPortAliasDelegation, PortType.INPUT_PORT);
        String outputPortAliasDelegation = getPortImplementationName(THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_OUTPUT_PORT_NAME);
        PortAlias outputPortAlias = createPortAlias(outputPortAliasDelegation, PortType.OUTPUT_PORT);
        job.setPortAliases(Arrays.asList(inputPortAlias, outputPortAlias));

        List<LineageMapping> lineageMappings = createLineageMappings(FIRST_STAGE_PROCESS_NAME, SECOND_STAGE_PROCESS_NAME,
                FIRST_STAGE_OUTPUT_PORT_NAME, SECOND_STAGE_INPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(),
                PortType.INPUT_PORT.getName(), csvHeaderAttributeNames);
        lineageMappings.addAll(createLineageMappings(SECOND_STAGE_PROCESS_NAME, THIRD_STAGE_PROCESS_NAME,
                SECOND_STAGE_OUTPUT_PORT_NAME, THIRD_STAGE_INPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(),
                PortType.INPUT_PORT.getName(), databaseTableAttributeNames));
        job.setLineageMappings(lineageMappings);

        dataEngineOMASClient.createOrUpdateProcesses(userId, Collections.singletonList(job));
    }

    private PortAlias createPortAlias(String delegatesTo, PortType type) {
        PortAlias portAlias = new PortAlias();
        String name = getPortAliasName(delegatesTo);
        portAlias.setQualifiedName(name);
        portAlias.setDisplayName(getDisplayName(name));
        portAlias.setPortType(type);
        portAlias.setDelegatesTo(delegatesTo);
        return portAlias;
    }

    private void createFirstStageProcess(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        Process firstStageProcess = createStageProcess(FIRST_STAGE_PROCESS_NAME, FIRST_STAGE_INPUT_PORT_NAME, FIRST_STAGE_OUTPUT_PORT_NAME,
                csvHeaderAttributeNames, csvHeaderAttributeNames);

        List<LineageMapping> lineageMappings = createLineageMappings(VIRTUAL, FIRST_STAGE_PROCESS_NAME,
                CSV, FIRST_STAGE_INPUT_PORT_NAME, PortType.INOUT_PORT.getName(), PortType.INPUT_PORT.getName(), csvHeaderAttributeNames);

        lineageMappings.addAll(createLineageMappings(FIRST_STAGE_PROCESS_NAME, FIRST_STAGE_PROCESS_NAME,
                FIRST_STAGE_INPUT_PORT_NAME, FIRST_STAGE_OUTPUT_PORT_NAME, PortType.INPUT_PORT.getName(),
                PortType.OUTPUT_PORT.getName(), csvHeaderAttributeNames));

        firstStageProcess.setLineageMappings(lineageMappings);
        dataEngineOMASClient.createOrUpdateProcesses(userId, Collections.singletonList(firstStageProcess));
    }

    private void createSecondStageProcess(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        Process secondStageProcess = createStageProcess(SECOND_STAGE_PROCESS_NAME, SECOND_STAGE_INPUT_PORT_NAME, SECOND_STAGE_OUTPUT_PORT_NAME,
                csvHeaderAttributeNames, databaseTableAttributeNames);

        List<LineageMapping> lineageMappings = new ArrayList<>();
        csvToDatabase.forEach((csvColumn, databaseColumn) -> {
            String sourceName = getAttributeName(SECOND_STAGE_PROCESS_NAME, SECOND_STAGE_INPUT_PORT_NAME, PortType.INPUT_PORT.getName(), csvColumn);
            String targetName = getAttributeName(SECOND_STAGE_PROCESS_NAME, SECOND_STAGE_OUTPUT_PORT_NAME, PortType.OUTPUT_PORT.getName(), databaseColumn);
            LineageMapping lineageMapping = createLineageMapping(sourceName, targetName);
            lineageMappings.add(lineageMapping);
        });
        secondStageProcess.setLineageMappings(lineageMappings);
        dataEngineOMASClient.createOrUpdateProcesses(userId, Collections.singletonList(secondStageProcess));
    }

    private void createThirdStageProcess(String userId, DataEngineClient dataEngineOMASClient)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, ConnectorCheckedException {
        Process thirdStageProcess = createStageProcess(THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_INPUT_PORT_NAME, THIRD_STAGE_OUTPUT_PORT_NAME,
                databaseTableAttributeNames, databaseTableAttributeNames);

        List<LineageMapping> lineageMappings = createLineageMappings(THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_PROCESS_NAME, THIRD_STAGE_INPUT_PORT_NAME,
                THIRD_STAGE_OUTPUT_PORT_NAME, PortType.INPUT_PORT.getName(), PortType.OUTPUT_PORT.getName(), databaseTableAttributeNames);

        lineageMappings.addAll(createLineageMappings(THIRD_STAGE_PROCESS_NAME, VIRTUAL, THIRD_STAGE_OUTPUT_PORT_NAME,
                DATABASE, PortType.OUTPUT_PORT.getName(), PortType.INOUT_PORT.getName(), databaseTableAttributeNames));

        thirdStageProcess.setLineageMappings(lineageMappings);
        dataEngineOMASClient.createOrUpdateProcesses(userId, Collections.singletonList(thirdStageProcess));
    }

    private List<LineageMapping> createLineageMappings(String sourceProcessName, String targetProcessName, String sourcePort, String targetPort,
                                                       String sourcePortType, String targetPortType, List<String> attributeNames) {
        List<LineageMapping> lineageMappings = new ArrayList<>();
        attributeNames.forEach(csvColumn -> {
            String sourceName = getAttributeName(sourceProcessName, sourcePort, sourcePortType, csvColumn);
            String targetName = getAttributeName(targetProcessName, targetPort, targetPortType, csvColumn);
            LineageMapping lineageMapping = createLineageMapping(sourceName, targetName);
            lineageMappings.add(lineageMapping);
        } );
        return lineageMappings;
    }

    private Process createStageProcess(String processName, String inputPortName, String outputPortName, List<String> inputAttributeNames, List<String> outputAttributeNames) {
        PortImplementation inputPortImplementation = createPortImplementationWithSchema(inputAttributeNames, processName, inputPortName, PortType.INPUT_PORT);
        PortImplementation outputPortImplementation = createPortImplementationWithSchema(outputAttributeNames, processName, outputPortName, PortType.OUTPUT_PORT);
        List<PortImplementation> portImplementations = Arrays.asList(inputPortImplementation, outputPortImplementation);
        Process process = new Process();
        String name = getStageProcessName(processName);
        process.setQualifiedName(name);
        process.setDisplayName(getDisplayName(name));
        process.setPortImplementations(portImplementations);
        return process;
    }

    private PortImplementation createPortImplementationWithSchema(List<String> attributeNames, String processName, String portName, PortType portType) {
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

    private String getPortAliasName(String delegatesTo) {
        return String.join(SEPARATOR, PORT_ALIAS, JOB_NAME, delegatesTo);
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
