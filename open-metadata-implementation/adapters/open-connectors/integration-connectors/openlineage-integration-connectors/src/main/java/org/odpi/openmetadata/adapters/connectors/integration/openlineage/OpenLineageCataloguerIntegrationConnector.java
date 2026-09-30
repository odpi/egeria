/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openlineage;

import org.odpi.openmetadata.adapters.connectors.controls.*;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataFeedProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DocumentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.VirtualRelationalTableProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.controls.OpenLineageCataloguerConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.integration.openlineage.ffdc.OpenLineageIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.openlineage.*;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ActorProfileClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AnnotationClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.LineageClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaAttributeClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.SchemaTypeClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ActivityStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ProcessContainmentType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.PartOfRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.DeployedSoftwareComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.RunMetricsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.SurveyReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics.TopicProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.OwnershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDuplicateLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.ControlFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.DataFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.DataMappingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.LineageRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.AttributeForSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.NestedSchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularColumnProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.tabular.TabularSchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.QualityAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.ResourceProfileAnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;


/**
 * OpenLineageCataloguerIntegrationConnector receives OpenLineage events published to the integration daemon and
 * catalogues what they describe in open metadata:
 * <ul>
 *     <li>Each OpenLineage <b>job</b> is matched to an existing process by namespace and resource name, or becomes a
 *     new process (DeployedSoftwareComponent).  Its documentation, sql,
 *     sourceCode, sourceCodeLocation, jobType, ownership and tags facets populate the process's properties.</li>
 *     <li>The <b>parent</b> and <b>root</b> jobs from the parent run facet become processes that own the job's process
 *     (OWNED ProcessHierarchy relationships, with the parent as the anchor of a newly created child).  Job dependencies (jobDependencies run facet and the JOB entries of the
 *     lineage job facet) become ControlFlow relationships between processes.</li>
 *     <li>Each <b>input and output dataset</b> is matched to an existing data asset by namespace and resource name,
 *     or becomes a new data asset whose open metadata type is chosen from the dataset's namespace and datasetType
 *     facet (ambiguous or wrong-typed matches are linked to it as peer duplicates).  Its documentation, dataSource, storage, catalog, version, symlinks,
 *     hierarchy, ownership and tags facets populate the asset.  The schema facet creates a tabular schema for the asset
 *     (optional).</li>
 *     <li>Inputs are linked to the process, and the process to the outputs, with <b>DataFlow</b> relationships.
 *     The columnLineage and lineage dataset facets create <b>LineageMapping</b> relationships between columns.</li>
 *     <li>Run counts, timings and data volumes are always maintained in the RunMetrics classification of the job's process.  Each <b>run</b> can
 *     optionally also become a TransientEmbeddedProcess owned by the job's process, recording the run's status,
 *     timing and run facets.</li>
 *     <li>The inputStatistics, outputStatistics, dataQualityMetrics, dataQualityAssertions and test facets are optionally
 *     captured as annotations in a SurveyReport created for the run, and the <b>DataScope</b> classification of the
 *     output data assets is optionally maintained from the times and statistics of the writes.</li>
 * </ul>
 * The optional behaviours are controlled by the configuration properties defined in
 * {@link OpenLineageCataloguerConfigurationProperty}.
 */
public class OpenLineageCataloguerIntegrationConnector extends IntegrationConnectorBase implements OpenLineageEventListener
{
    /*
     * Qualified name prefixes for the elements created by this connector.
     */
    private static final String LEGACY_JOB_QUALIFIED_NAME_PREFIX = "OpenLineageJob:";
    private static final String RUN_QUALIFIED_NAME_PREFIX        = "OpenLineageRun::";
    private static final String REPORT_QUALIFIED_NAME_PREFIX     = "OpenLineageRunReport::";
    private static final String QUALIFIED_NAME_SEPARATOR         = "::";
    private static final String UNKNOWN_NAMESPACE                = "default";

    /*
     * Names of additional properties maintained in the DataScope classification.
     */
    private static final String WRITE_COUNT_PROPERTY          = "writeCount";
    private static final String READ_COUNT_PROPERTY           = "readCount";
    private static final String LAST_WRITTEN_BY_PROPERTY      = "lastWrittenByRunId";
    private static final String LAST_WRITTEN_AT_PROPERTY      = "lastWrittenAt";
    private static final String LAST_READ_BY_PROPERTY         = "lastReadByRunId";
    private static final String LAST_READ_AT_PROPERTY         = "lastReadAt";
    private static final String LAST_ROW_COUNT_PROPERTY       = "lastRowCount";
    private static final String LAST_SIZE_PROPERTY            = "lastSize";
    private static final String LAST_FILE_COUNT_PROPERTY      = "lastFileCount";
    private static final String LAST_PARTITION_PROPERTY       = "lastSubsetWritten";
    private static final String LAST_LIFECYCLE_CHANGE_PROPERTY = "lastLifecycleStateChange";
    private static final String DATASET_VERSION_PROPERTY      = "lastDatasetVersion";

    /*
     * Names of additional properties recorded in the Ownership classification.
     */
    private static final String OPEN_LINEAGE_OWNER_NAME_PROPERTY = "openLineageOwnerName";
    private static final String OPEN_LINEAGE_OWNER_TYPE_PROPERTY = "openLineageOwnerType";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected String             destinationName = "Unknown";
    protected IntegrationContext myContext       = null;

    private boolean catalogRuns          = false;
    private boolean catalogSchemas       = true;
    private boolean captureStatistics    = true;
    private boolean captureDataQuality   = true;
    private boolean updateDataScope      = true;

    /*
     * Relationships that this connector instance has confirmed exist (key = end1GUID|end2GUID|typeName).
     * This avoids a repository query for every input/output of every event for long-running jobs.
     */
    private final Set<String> knownRelationships = Collections.synchronizedSet(new HashSet<>());

    /**
     * Finds the elements in open metadata that describe the datasets.
     */
    private OpenLineageDataSetResolver resolver = null;

    /**
     * Finds the governance action process, step and instance behind a run from the governance action publisher.
     */
    private OpenLineageGovernanceActionResolver governanceActionResolver = null;

    /**
     * Which catalog templates are loaded (template GUID to flag).
     */
    private final Map<String, Boolean> loadedTemplates = new ConcurrentHashMap<>();


    /**
     * Details of a process, data asset or schema attribute (such as a relational table) that is catalogued for an
     * OpenLineage element.
     *
     * @param guid unique identifier of the element
     * @param qualifiedName qualified name of the element
     * @param element full element (null if it has just been created, or is a schema attribute)
     * @param schemaAttribute the element is a schema attribute (for example a RelationalTable) rather than an asset
     * @param parentAsset for a schema attribute, the asset it belongs to (for example its DeployedDatabaseSchema);
     *                    lineage is recorded at this asset level as well as on the schema attribute
     */
    private record CataloguedElement(String                  guid,
                                     String                  qualifiedName,
                                     OpenMetadataRootElement element,
                                     boolean                 schemaAttribute,
                                     CataloguedElement       parentAsset)
    {
        /**
         * Constructor for an asset or process.
         *
         * @param guid unique identifier of the element
         * @param qualifiedName qualified name of the element
         * @param element full element (null if it has just been created)
         */
        CataloguedElement(String                  guid,
                          String                  qualifiedName,
                          OpenMetadataRootElement element)
        {
            this(guid, qualifiedName, element, false, null);
        }
    }


    /**
     * Default constructor
     */
    public OpenLineageCataloguerIntegrationConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public synchronized void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        if (connectionBean != null)
        {
            if (connectionBean.getDisplayName() != null)
            {
                destinationName = connectionBean.getDisplayName();
            }
            else if (connectionBean.getConnectorType() != null)
            {
                if (connectionBean.getConnectorType().getDisplayName() != null)
                {
                    destinationName = connectionBean.getConnectorType().getDisplayName();
                }
            }

            Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

            catalogRuns          = getBooleanConfigurationProperty(OpenLineageCataloguerConfigurationProperty.CATALOG_RUNS.getName(), configurationProperties, false);
            catalogSchemas       = getBooleanConfigurationProperty(OpenLineageCataloguerConfigurationProperty.CATALOG_SCHEMAS.getName(), configurationProperties, true);
            captureStatistics    = getBooleanConfigurationProperty(OpenLineageCataloguerConfigurationProperty.CAPTURE_STATISTICS.getName(), configurationProperties, true);
            captureDataQuality   = getBooleanConfigurationProperty(OpenLineageCataloguerConfigurationProperty.CAPTURE_DATA_QUALITY.getName(), configurationProperties, true);
            updateDataScope      = getBooleanConfigurationProperty(OpenLineageCataloguerConfigurationProperty.UPDATE_DATA_SCOPE.getName(), configurationProperties, true);
        }

        myContext = integrationContext;

        if (myContext != null)
        {
            resolver                 = new OpenLineageDataSetResolver(myContext, connectorName);
            governanceActionResolver = new OpenLineageGovernanceActionResolver(myContext, connectorName);

            myContext.registerOpenLineageListener(this);
        }
    }


    /**
     * Retrieve a boolean configuration property, using the default if it is not set.
     *
     * @param propertyName name of property
     * @param configurationProperties configuration properties from the connection
     * @param defaultValue value to use if the property is not set
     * @return boolean
     */
    private boolean getBooleanConfigurationProperty(String              propertyName,
                                                    Map<String, Object> configurationProperties,
                                                    boolean             defaultValue)
    {
        if ((configurationProperties != null) && (configurationProperties.get(propertyName) != null))
        {
            return Boolean.parseBoolean(configurationProperties.get(propertyName).toString());
        }

        return defaultValue;
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * This connector is event driven so there is nothing to do.
     */
    public void refresh()
    {
        // nothing to do
    }


    /* =======================================================================================
     * Event entry points
     */

    /**
     * Called each time an open lineage run event is published to the integration daemon.
     *
     * @param event bean representation of the run event
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     */
    @Override
    public void processOpenLineageRunEvent(OpenLineageRunEvent event,
                                           String              rawEvent)
    {
        final String methodName = "processOpenLineageRunEvent";

        if ((event == null) || (myContext == null))
        {
            return;
        }

        String eventDescription = getEventDescription(event);

        try
        {
            /*
             * The job's process is the hub of the lineage - nothing else can be catalogued without it.
             */
            CataloguedElement jobProcess;
            CataloguedElement metricsElement;
            CataloguedElement runProcess = null;

            OpenLineageGovernanceActionResolver.GovernanceActionElements governanceAction = resolveGovernanceAction(event.getRun(), eventDescription);

            if (governanceAction != null)
            {
                /*
                 * A run of a governance action process: the lineage is attached to the governance action process,
                 * the run metrics are kept on the process step (each step runs separately), and the process
                 * instance already represents this run.
                 */
                jobProcess = new CataloguedElement(governanceAction.processGUID(), governanceAction.processQualifiedName(), null);
                runProcess = new CataloguedElement(governanceAction.processInstanceGUID(), governanceAction.processInstanceQualifiedName(), null);

                if (governanceAction.processStepGUID() == null)
                {
                    metricsElement = null;
                }
                else
                {
                    metricsElement = new CataloguedElement(governanceAction.processStepGUID(), governanceAction.processStepQualifiedName(), null);
                }
            }
            else
            {
                CataloguedElement parentProcess = syncParentJobs(event.getRun(), event.getJob(), eventDescription);

                jobProcess     = syncJob(event.getJob(), parentProcess, eventDescription);
                metricsElement = jobProcess;
            }

            if (jobProcess == null)
            {
                return;
            }

            String iscQualifiedName = getISCQualifiedName(event.getRun());

            List<CataloguedElement> inputAssets  = syncInputDataSets(event.getInputs(), jobProcess, iscQualifiedName, eventDescription);
            List<CataloguedElement> outputAssets = syncOutputDataSets(event.getOutputs(), jobProcess, iscQualifiedName, eventDescription);

            syncJobDependencies(event.getRun(), jobProcess, iscQualifiedName, eventDescription);
            syncExplicitLineage(event.getJob(), jobProcess, iscQualifiedName, eventDescription);
            syncColumnLineage(event.getOutputs(), outputAssets, iscQualifiedName, eventDescription);

            /*
             * The remaining metadata is optional and does not affect the lineage so a failure in one part
             * should not stop the others.
             */
            if ((catalogRuns) && (governanceAction == null))
            {
                runProcess = syncRun(event, jobProcess, eventDescription);
            }

            if (metricsElement != null)
            {
                updateProcessMetrics(event, metricsElement, eventDescription);
            }

            if ((captureStatistics) || (captureDataQuality))
            {
                captureRunObservations(event, jobProcess, runProcess, inputAssets, outputAssets, eventDescription);
            }

            if (updateDataScope)
            {
                updateDataScope(event, jobProcess, inputAssets, outputAssets, eventDescription);
            }
        }
        catch (Exception error)
        {
            logUnexpectedException(methodName, error, rawEvent, event);
        }
    }


    /**
     * Called each time an open lineage job event is published to the integration daemon.  Job events describe a job
     * and its inputs/outputs independently of a run - typically when the job is deployed.
     *
     * @param event bean representation of the job event
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     */
    @Override
    public void processOpenLineageJobEvent(OpenLineageJobEvent event,
                                           String              rawEvent)
    {
        final String methodName = "processOpenLineageJobEvent";

        if ((event == null) || (myContext == null))
        {
            return;
        }

        String eventDescription = "job event at " + event.getEventTime();

        try
        {
            CataloguedElement jobProcess = syncJob(event.getJob(), null, eventDescription);

            if (jobProcess != null)
            {
                List<CataloguedElement> outputAssets = syncOutputDataSets(event.getOutputs(), jobProcess, null, eventDescription);

                syncInputDataSets(event.getInputs(), jobProcess, null, eventDescription);
                syncExplicitLineage(event.getJob(), jobProcess, null, eventDescription);
                syncColumnLineage(event.getOutputs(), outputAssets, null, eventDescription);
            }
        }
        catch (Exception error)
        {
            logUnexpectedException(methodName, error, rawEvent, event);
        }
    }


    /**
     * Called each time an open lineage dataset event is published to the integration daemon.  Dataset events describe
     * a dataset independently of a run - typically when it is created or its schema changes.
     *
     * @param event bean representation of the dataset event
     * @param rawEvent event in Json form from the originator - may have facets that are not known to Egeria
     */
    @Override
    public void processOpenLineageDataSetEvent(OpenLineageDataSetEvent event,
                                               String                  rawEvent)
    {
        final String methodName = "processOpenLineageDataSetEvent";

        if ((event == null) || (myContext == null) || (event.getDataset() == null))
        {
            return;
        }

        String eventDescription = "dataset event at " + event.getEventTime();

        try
        {
            syncDataSet(event.getDataset().getNamespace(), event.getDataset().getName(), event.getDataset().getFacets(), null, eventDescription);
        }
        catch (Exception error)
        {
            logUnexpectedException(methodName, error, rawEvent, event);
        }
    }


    /* =======================================================================================
     * Jobs and runs
     */

    /**
     * Find or create the process that represents an OpenLineage job.
     *
     * @param job job from the event
     * @param parentProcess process that owns this job (from the parent run facet) or null
     * @param eventDescription description of the event for logging
     * @return catalogued process or null if the job is not identifiable (or is ambiguous)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private CataloguedElement syncJob(OpenLineageJob    job,
                                      CataloguedElement parentProcess,
                                      String            eventDescription) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        if (job == null)
        {
            return null;
        }

        return syncJob(job.getNamespace(), job.getName(), job.getFacets(), parentProcess, eventDescription);
    }


    /**
     * Find or create the process that represents an OpenLineage job.
     *
     * @param namespace job namespace
     * @param name job name
     * @param facets job facets (may be null)
     * @param parentProcess process that owns this job (from the parent run facet) or null.  A new process is anchored
     *                      to its parent and linked with an OWNED ProcessHierarchy relationship; an existing process
     *                      keeps its anchor and is linked with the same relationship.
     * @param eventDescription description of the event for logging
     * @return catalogued process or null if the job is not identifiable (or is ambiguous)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private CataloguedElement syncJob(String               namespace,
                                      String               name,
                                      OpenLineageJobFacets facets,
                                      CataloguedElement    parentProcess,
                                      String               eventDescription) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        return syncJob(namespace, name, facets, getJobTemplate(facets), parentProcess, eventDescription);
    }


    /**
     * Find or create the process that represents an OpenLineage job.
     *
     * @param namespace job namespace
     * @param name job name
     * @param facets job facets (may be null)
     * @param template catalog template for a new process (null for a generic DeployedSoftwareComponent)
     * @param parentProcess process that owns this job (from the parent run facet) or null.  A new process is anchored
     *                      to its parent and linked with an OWNED ProcessHierarchy relationship; an existing process
     *                      keeps its anchor and is linked with the same relationship.
     * @param eventDescription description of the event for logging
     * @return catalogued process or null if the job is not identifiable (or is ambiguous)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private CataloguedElement syncJob(String               namespace,
                                      String               name,
                                      OpenLineageJobFacets facets,
                                      TemplateDefinition   template,
                                      CataloguedElement    parentProcess,
                                      String               eventDescription) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {

        if (name == null)
        {
            logEventIgnored(eventDescription, "the job has no name");
            return null;
        }

        AssetClient processClient = myContext.getAssetClient(OpenMetadataType.PROCESS.typeName);

        String qualifiedName = getJobQualifiedName(namespace, name);

        /*
         * Look for the process this connector would have created (current and legacy naming), then for processes
         * created by other connectors that match on namespace and resource name - the same rules as for datasets.
         */
        OpenMetadataRootElement existingProcess = processClient.getAssetByUniqueName(qualifiedName,
                                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                     processClient.getGetOptions());

        if (existingProcess == null)
        {
            existingProcess = processClient.getAssetByUniqueName(LEGACY_JOB_QUALIFIED_NAME_PREFIX + name,
                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                 processClient.getGetOptions());
        }

        List<OpenMetadataRootElement> matchingProcesses = findMatchingAssets(namespace, name);
        List<OpenMetadataRootElement> peerDuplicates    = new ArrayList<>();

        if (existingProcess == null)
        {
            OpenMetadataRootElement compatibleMatch   = null;
            int                     compatibleMatches = 0;

            for (OpenMetadataRootElement matchingProcess : matchingProcesses)
            {
                if (propertyHelper.isTypeOf(matchingProcess.getElementHeader(), OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName))
                {
                    compatibleMatch = matchingProcess;
                    compatibleMatches++;
                }
            }

            if ((compatibleMatches == 1) && (matchingProcesses.size() == 1))
            {
                existingProcess = compatibleMatch;
            }
            else
            {
                peerDuplicates.addAll(matchingProcesses);
            }
        }
        else
        {
            for (OpenMetadataRootElement matchingProcess : matchingProcesses)
            {
                if (! existingProcess.getElementHeader().getGUID().equals(matchingProcess.getElementHeader().getGUID()))
                {
                    peerDuplicates.add(matchingProcess);
                }
            }
        }

        CataloguedElement process;

        if (existingProcess == null)
        {
            DeployedSoftwareComponentProperties processProperties = new DeployedSoftwareComponentProperties();

            processProperties.setTypeName(OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName);
            processProperties.setQualifiedName(qualifiedName);
            processProperties.setDisplayName(name);
            processProperties.setResourceName(name);
            processProperties.setNamespacePath(namespace);
            processProperties.setDeployedImplementationType(DeployedImplementationType.PROCESS.getDeployedImplementationType());

            fillJobProperties(processProperties, facets, new HashMap<>());

            if (template != null)
            {
                processProperties.setDeployedImplementationType(template.getDeployedImplementationType().getDeployedImplementationType());
            }

            TemplateOptions            newElementOptions   = new TemplateOptions(processClient.getMetadataSourceOptions());
            ProcessHierarchyProperties hierarchyProperties = null;

            if (parentProcess != null)
            {
                /*
                 * The parent owns the new process: it becomes the anchor and the ProcessHierarchy relationship
                 * is created along with the element.
                 */
                newElementOptions.setAnchorGUID(parentProcess.guid());
                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setParentGUID(parentProcess.guid());
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName);
                newElementOptions.setParentAtEnd1(true);

                hierarchyProperties = new ProcessHierarchyProperties();
                hierarchyProperties.setContainmentType(ProcessContainmentType.OWNED);
            }
            else
            {
                newElementOptions.setIsOwnAnchor(true);
            }

            String processGUID = createAsset(processClient,
                                             newElementOptions,
                                             template,
                                             processProperties,
                                             getPlaceholderProperties(template, namespace, name, processProperties.getDescription()),
                                             hierarchyProperties);

            processClient.publishElement(processGUID);

            addOwnership(processGUID, (facets == null) ? null : facets.getOwnership(), null);

            logElementCatalogued(OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName, qualifiedName, processGUID, "job", name);

            process = new CataloguedElement(processGUID, qualifiedName, null);

            if (parentProcess != null)
            {
                rememberRelationship(parentProcess.guid(), processGUID, OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName);
                logLineageCatalogued(OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName, parentProcess.qualifiedName(), qualifiedName);
            }
        }
        else
        {
            String processGUID = existingProcess.getElementHeader().getGUID();

            process = new CataloguedElement(processGUID, getQualifiedName(existingProcess), existingProcess);

            /*
             * Fill in any properties that are missing from the existing process.  Existing values are not
             * overwritten because they may have been set by a steward.
             */
            if ((facets != null) && (existingProcess.getProperties() instanceof ProcessProperties existingProperties))
            {
                DeployedSoftwareComponentProperties updateProperties = new DeployedSoftwareComponentProperties();
                Map<String, String> additionalProperties = new HashMap<>();

                if (existingProperties.getAdditionalProperties() != null)
                {
                    additionalProperties.putAll(existingProperties.getAdditionalProperties());
                }

                fillJobProperties(updateProperties, facets, additionalProperties);

                boolean changed = false;

                if ((existingProperties.getDescription() == null) && (updateProperties.getDescription() != null))
                {
                    changed = true;
                }
                else
                {
                    updateProperties.setDescription(null);
                }

                if ((existingProperties.getFormula() == null) && (updateProperties.getFormula() != null))
                {
                    changed = true;
                }
                else
                {
                    updateProperties.setFormula(null);
                    updateProperties.setFormulaType(null);
                }

                if ((existingProperties.getDeployedImplementationType() == null) && (updateProperties.getDeployedImplementationType() != null))
                {
                    changed = true;
                }
                else
                {
                    updateProperties.setDeployedImplementationType(null);
                }

                updateProperties.setImplementationLanguage(null);

                if (! additionalProperties.equals(existingProperties.getAdditionalProperties()))
                {
                    updateProperties.setAdditionalProperties(additionalProperties);
                    changed = true;
                }
                else
                {
                    updateProperties.setAdditionalProperties(null);
                }

                if (changed)
                {
                    processClient.updateAsset(processGUID, processClient.getUpdateOptions(true), updateProperties);
                }

                if (existingProcess.getElementHeader().getOwnership() == null)
                {
                    addOwnership(processGUID, facets.getOwnership(), null);
                }
            }
        }

        if ((parentProcess != null) && (! parentProcess.guid().equals(process.guid())))
        {
            ensureProcessHierarchy(parentProcess, process);
        }

        linkPeerDuplicates(process, peerDuplicates, namespace, name, eventDescription);

        return process;
    }


    /**
     * Extract the qualified name of an existing element.
     *
     * @param element element
     * @return qualified name
     */
    private String getQualifiedName(OpenMetadataRootElement element)
    {
        if (element.getProperties() instanceof org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties referenceableProperties)
        {
            return referenceableProperties.getQualifiedName();
        }

        return null;
    }


    /**
     * Copy the values from the job facets into the process properties.
     *
     * @param processProperties properties to fill
     * @param facets job facets (may be null)
     * @param additionalProperties additional properties map to fill (facets that have no natural home in the process properties)
     */
    private void fillJobProperties(DeployedSoftwareComponentProperties processProperties,
                                   OpenLineageJobFacets                facets,
                                   Map<String, String>                 additionalProperties)
    {
        if (facets == null)
        {
            return;
        }

        if ((facets.getDocumentation() != null) && (facets.getDocumentation().getDescription() != null))
        {
            processProperties.setDescription(facets.getDocumentation().getDescription());
        }

        if ((facets.getSql() != null) && (facets.getSql().getQuery() != null))
        {
            processProperties.setFormula(facets.getSql().getQuery());

            if (facets.getSql().getDialect() != null)
            {
                processProperties.setFormulaType("SQL:" + facets.getSql().getDialect());
            }
            else
            {
                processProperties.setFormulaType("SQL");
            }
        }

        if (facets.getSourceCode() != null)
        {
            processProperties.setImplementationLanguage(facets.getSourceCode().getLanguage());
        }

        OpenLineageJobTypeJobFacet jobType = facets.getJobType();

        if (jobType != null)
        {
            if ((jobType.getIntegration() != null) && (jobType.getJobType() != null))
            {
                processProperties.setDeployedImplementationType(jobType.getIntegration() + " " + jobType.getJobType());
            }
            else if (jobType.getIntegration() != null)
            {
                processProperties.setDeployedImplementationType(jobType.getIntegration());
            }

            putIfNotNull(additionalProperties, "jobType.processingType", jobType.getProcessingType());
            putIfNotNull(additionalProperties, "jobType.integration", jobType.getIntegration());
            putIfNotNull(additionalProperties, "jobType.jobType", jobType.getJobType());
        }

        OpenLineageSourceCodeLocationJobFacet sourceCodeLocation = facets.getSourceCodeLocation();

        if (sourceCodeLocation != null)
        {
            putIfNotNull(additionalProperties, "sourceCodeLocation.type", sourceCodeLocation.getType());
            putIfNotNull(additionalProperties, "sourceCodeLocation.url", sourceCodeLocation.getUrl());
            putIfNotNull(additionalProperties, "sourceCodeLocation.repoUrl", sourceCodeLocation.getRepoUrl());
            putIfNotNull(additionalProperties, "sourceCodeLocation.path", sourceCodeLocation.getPath());
            putIfNotNull(additionalProperties, "sourceCodeLocation.version", sourceCodeLocation.getVersion());
            putIfNotNull(additionalProperties, "sourceCodeLocation.tag", sourceCodeLocation.getTag());
            putIfNotNull(additionalProperties, "sourceCodeLocation.branch", sourceCodeLocation.getBranch());
            putIfNotNull(additionalProperties, "sourceCodeLocation.pullRequestNumber", sourceCodeLocation.getPullRequestNumber());
        }

        if ((facets.getTags() != null) && (facets.getTags().getTags() != null))
        {
            for (OpenLineageTagsJobFacetTag tag : facets.getTags().getTags())
            {
                if ((tag != null) && (tag.getKey() != null))
                {
                    putIfNotNull(additionalProperties, "tag:" + tag.getKey(), tag.getValue());
                }
            }
        }

        if (! additionalProperties.isEmpty())
        {
            processProperties.setAdditionalProperties(additionalProperties);
        }
    }


    /**
     * Create the processes for the root job and parent job (if any) from the parent run facet.  The root job owns
     * the parent job and the parent job owns the event's job, so the processes are created from the top down and
     * the immediate owner of the event's job is returned so that its process can be anchored to it.
     *
     * @param run run from the event
     * @param eventDescription description of the event for logging
     * @return process that owns the event's job (the parent, or the root if there is no separate parent), or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private CataloguedElement syncParentJobs(OpenLineageRun run,
                                             OpenLineageJob job,
                                             String         eventDescription) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        if ((run == null) || (run.getFacets() == null) || (run.getFacets().getParent() == null))
        {
            return null;
        }

        OpenLineageParentRunFacet parent = run.getFacets().getParent();

        /*
         * The parent of an Apache Airflow task is its DAG.
         */
        TemplateDefinition parentTemplate = null;

        if ((job != null) && (getJobTemplate(job.getFacets()) == DataPipelineTemplateType.APACHE_AIRFLOW_TASK_TEMPLATE))
        {
            parentTemplate = DataPipelineTemplateType.APACHE_AIRFLOW_DAG_TEMPLATE;
        }

        CataloguedElement rootProcess   = null;
        CataloguedElement parentProcess = null;

        if ((parent.getRoot() != null) && (parent.getRoot().getJob() != null) && (parent.getRoot().getJob().getName() != null))
        {
            rootProcess = syncJob(parent.getRoot().getJob().getNamespace(), parent.getRoot().getJob().getName(), null, parentTemplate, null, eventDescription);
        }

        if ((parent.getJob() != null) && (parent.getJob().getName() != null))
        {
            if ((rootProcess != null) &&
                (parent.getJob().getName().equals(parent.getRoot().getJob().getName())) &&
                (java.util.Objects.equals(parent.getJob().getNamespace(), parent.getRoot().getJob().getNamespace())))
            {
                parentProcess = rootProcess;
            }
            else
            {
                parentProcess = syncJob(parent.getJob().getNamespace(), parent.getJob().getName(), null, parentTemplate, rootProcess, eventDescription);
            }
        }

        if (parentProcess != null)
        {
            return parentProcess;
        }

        return rootProcess;
    }


    /**
     * Create an OWNED ProcessHierarchy relationship between two processes if it does not already exist.  This is
     * used when the child process already existed (so its anchor is left alone).
     *
     * @param parentProcess parent
     * @param childProcess child
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void ensureProcessHierarchy(CataloguedElement parentProcess,
                                        CataloguedElement childProcess) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        if (! isRelationshipKnown(parentProcess.guid(), childProcess.guid(), OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName))
        {
            AssetClient processClient = myContext.getAssetClient(OpenMetadataType.PROCESS.typeName);

            ProcessHierarchyProperties properties = new ProcessHierarchyProperties();
            properties.setContainmentType(ProcessContainmentType.OWNED);

            processClient.linkProcessHierarchy(parentProcess.guid(),
                                               childProcess.guid(),
                                               new MakeAnchorOptions(processClient.getMetadataSourceOptions()),
                                               properties);

            rememberRelationship(parentProcess.guid(), childProcess.guid(), OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName);

            logLineageCatalogued(OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName, parentProcess.qualifiedName(), childProcess.qualifiedName());
        }
    }


    /**
     * Create ControlFlow relationships between the job's process and the processes of its upstream/downstream jobs
     * described in the jobDependencies run facet.
     *
     * @param run run from the event
     * @param jobProcess catalogued process for the job
     * @param iscQualifiedName qualified name of the information supply chain for the lineage (may be null)
     * @param eventDescription description of the event for logging
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void syncJobDependencies(OpenLineageRun    run,
                                     CataloguedElement jobProcess,
                                     String            iscQualifiedName,
                                     String            eventDescription) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        if ((run == null) || (run.getFacets() == null) || (run.getFacets().getJobDependencies() == null))
        {
            return;
        }

        OpenLineageJobDependenciesRunFacet jobDependencies = run.getFacets().getJobDependencies();

        if (jobDependencies.getUpstream() != null)
        {
            for (OpenLineageJobDependenciesRunFacetDependency dependency : jobDependencies.getUpstream())
            {
                if ((dependency != null) && (dependency.getJob() != null))
                {
                    CataloguedElement upstreamProcess = syncJob(dependency.getJob().getNamespace(), dependency.getJob().getName(), null, null, eventDescription);

                    if (upstreamProcess != null)
                    {
                        ensureControlFlow(upstreamProcess, jobProcess, dependency, iscQualifiedName);
                    }
                }
            }
        }

        if (jobDependencies.getDownstream() != null)
        {
            for (OpenLineageJobDependenciesRunFacetDependency dependency : jobDependencies.getDownstream())
            {
                if ((dependency != null) && (dependency.getJob() != null))
                {
                    CataloguedElement downstreamProcess = syncJob(dependency.getJob().getNamespace(), dependency.getJob().getName(), null, null, eventDescription);

                    if (downstreamProcess != null)
                    {
                        ensureControlFlow(jobProcess, downstreamProcess, dependency, iscQualifiedName);
                    }
                }
            }
        }
    }


    /**
     * Create a ControlFlow relationship between two processes if it does not already exist.
     *
     * @param fromProcess upstream process
     * @param toProcess downstream process
     * @param dependency details of the dependency (may be null)
     * @param iscQualifiedName qualified name of the information supply chain for the lineage (may be null)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void ensureControlFlow(CataloguedElement                           fromProcess,
                                   CataloguedElement                           toProcess,
                                   OpenLineageJobDependenciesRunFacetDependency dependency,
                                   String                                      iscQualifiedName) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        ControlFlowProperties properties = new ControlFlowProperties();

        if (dependency != null)
        {
            properties.setLabel(dependency.getDependencyType());
            properties.setGuard(dependency.getStatusTriggerRule());
            properties.setDescription(dependency.getSequenceTriggerRule());
        }

        ensureLineage(fromProcess, toProcess, OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName, properties, iscQualifiedName);
    }


    /**
     * Create a run element (TransientEmbeddedProcess) for the run, owned by the job's process, or update it with the
     * latest status if it already exists.  The run facets are recorded in the run's additional properties.
     *
     * @param event run event
     * @param jobProcess catalogued process for the job
     * @param eventDescription description of the event for logging
     * @return catalogued run element (null if the run could not be catalogued)
     */
    private CataloguedElement syncRun(OpenLineageRunEvent event,
                                      CataloguedElement   jobProcess,
                                      String              eventDescription)
    {
        final String methodName = "syncRun";

        try
        {
            if ((event.getRun() == null) || (event.getRun().getRunId() == null))
            {
                return null;
            }

            OpenLineageRun run           = event.getRun();
            String         runId         = run.getRunId().toString();
            String         qualifiedName = getRunQualifiedName(event.getJob(), runId);

            AssetClient processClient = myContext.getAssetClient(OpenMetadataType.PROCESS.typeName);

            OpenMetadataRootElement existingRun = processClient.getAssetByUniqueName(qualifiedName,
                                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                     processClient.getGetOptions());

            ProcessProperties runProperties = new ProcessProperties();
            Map<String, String> additionalProperties = new HashMap<>();

            if ((existingRun != null) && (existingRun.getProperties() instanceof ProcessProperties existingProperties) && (existingProperties.getAdditionalProperties() != null))
            {
                additionalProperties.putAll(existingProperties.getAdditionalProperties());
            }

            runProperties.setTypeName(OpenMetadataType.TRANSIENT_EMBEDDED_PROCESS.typeName);
            runProperties.setQualifiedName(qualifiedName);
            runProperties.setIdentifier(runId);
            runProperties.setDisplayName(event.getJob().getName() + " run " + runId);
            runProperties.setResourceName(runId);
            runProperties.setNamespacePath(event.getJob().getNamespace());
            runProperties.setDeployedImplementationType(DeployedImplementationType.PROCESS.getDeployedImplementationType());
            runProperties.setActivityStatus(getActivityStatus(event.getEventType()));

            Date eventTime = parseTimestamp(event.getEventTime());

            if ("START".equals(event.getEventType()))
            {
                runProperties.setStartTime(eventTime);
            }
            else if (("COMPLETE".equals(event.getEventType())) || ("FAIL".equals(event.getEventType())) || ("ABORT".equals(event.getEventType())))
            {
                runProperties.setCompletionTime(eventTime);
            }

            putIfNotNull(additionalProperties, "lastEventType", event.getEventType());
            putIfNotNull(additionalProperties, "lastEventTime", event.getEventTime());
            putIfNotNull(additionalProperties, "producer", event.getProducer());

            OpenLineageRunFacets facets = run.getFacets();

            if (facets != null)
            {
                if (facets.getNominalTime() != null)
                {
                    runProperties.setRequestedStartTime(parseTimestamp(facets.getNominalTime().getNominalStartTime()));
                    putIfNotNull(additionalProperties, "nominalStartTime", facets.getNominalTime().getNominalStartTime());
                    putIfNotNull(additionalProperties, "nominalEndTime", facets.getNominalTime().getNominalEndTime());
                }

                if (facets.getParent() != null)
                {
                    if (facets.getParent().getRun() != null)
                    {
                        putIfNotNull(additionalProperties, "parentRunId", facets.getParent().getRun().getRunId());
                    }
                    if ((facets.getParent().getRoot() != null) && (facets.getParent().getRoot().getRun() != null))
                    {
                        putIfNotNull(additionalProperties, "rootRunId", facets.getParent().getRoot().getRun().getRunId());
                    }
                }

                if (facets.getProcessingEngine() != null)
                {
                    putIfNotNull(additionalProperties, "processingEngine.name", facets.getProcessingEngine().getName());
                    putIfNotNull(additionalProperties, "processingEngine.version", facets.getProcessingEngine().getVersion());
                    putIfNotNull(additionalProperties, "processingEngine.openlineageAdapterVersion", facets.getProcessingEngine().getOpenlineageAdapterVersion());
                }

                if (facets.getErrorMessage() != null)
                {
                    putIfNotNull(additionalProperties, "errorMessage", facets.getErrorMessage().getMessage());
                    putIfNotNull(additionalProperties, "errorMessage.programmingLanguage", facets.getErrorMessage().getProgrammingLanguage());
                }

                if (facets.getExternalQuery() != null)
                {
                    putIfNotNull(additionalProperties, "externalQuery.id", facets.getExternalQuery().getExternalQueryId());
                    putIfNotNull(additionalProperties, "externalQuery.source", facets.getExternalQuery().getSource());
                }

                if (facets.getExtractionError() != null)
                {
                    putIfNotNull(additionalProperties, "extractionError.totalTasks", facets.getExtractionError().getTotalTasks());
                    putIfNotNull(additionalProperties, "extractionError.failedTasks", facets.getExtractionError().getFailedTasks());
                }

                if ((facets.getExecutionParameters() != null) && (facets.getExecutionParameters().getParameters() != null))
                {
                    for (OpenLineageExecutionParametersRunFacetParameter parameter : facets.getExecutionParameters().getParameters())
                    {
                        if ((parameter != null) && (parameter.getKey() != null))
                        {
                            putIfNotNull(additionalProperties, "parameter:" + parameter.getKey(), parameter.getValue());
                        }
                    }
                }

                if ((facets.getTags() != null) && (facets.getTags().getTags() != null))
                {
                    for (OpenLineageTagsRunFacetTag tag : facets.getTags().getTags())
                    {
                        if ((tag != null) && (tag.getKey() != null))
                        {
                            putIfNotNull(additionalProperties, "tag:" + tag.getKey(), tag.getValue());
                        }
                    }
                }
            }

            runProperties.setAdditionalProperties(additionalProperties);

            if (existingRun == null)
            {
                /*
                 * The run is anchored to the job's process and linked to it with a ProcessHierarchy relationship.
                 */
                NewElementOptions newElementOptions = new NewElementOptions(processClient.getMetadataSourceOptions());

                newElementOptions.setAnchorGUID(jobProcess.guid());
                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setParentGUID(jobProcess.guid());
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.PROCESS_HIERARCHY_RELATIONSHIP.typeName);
                newElementOptions.setParentAtEnd1(true);

                ProcessHierarchyProperties hierarchyProperties = new ProcessHierarchyProperties();
                hierarchyProperties.setContainmentType(ProcessContainmentType.OWNED);

                String runGUID = processClient.createAsset(newElementOptions, null, runProperties, hierarchyProperties);

                logElementCatalogued(OpenMetadataType.TRANSIENT_EMBEDDED_PROCESS.typeName, qualifiedName, runGUID, "run", runId);

                return new CataloguedElement(runGUID, qualifiedName, null);
            }
            else
            {
                String runGUID = existingRun.getElementHeader().getGUID();

                processClient.updateAsset(runGUID, processClient.getUpdateOptions(true), runProperties);

                return new CataloguedElement(runGUID, qualifiedName, existingRun);
            }
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "run details", eventDescription, error);
        }

        return null;
    }


    /**
     * Maintain the RunMetrics classification on the job's process (or, for a governance action process, on the
     * process step).  This is always maintained (whether or not runs are catalogued as elements) so that the process
     * records how often it runs and how much data it handles.
     *
     * @param event run event
     * @param jobProcess catalogued process (or process step) that holds the run metrics
     * @param eventDescription description of the event for logging
     */
    private void updateProcessMetrics(OpenLineageRunEvent event,
                                      CataloguedElement   jobProcess,
                                      String              eventDescription)
    {
        final String methodName = "updateProcessMetrics";

        try
        {
            ClassificationExplorerClient classificationClient = myContext.getClassificationExplorerClient();

            /*
             * Always re-read the process so that the metrics are built on the latest classification.  A process step
             * is not an asset, so the element is retrieved generically.
             */
            OpenMetadataRootElement process = classificationClient.getRootElementByGUID(jobProcess.guid(), classificationClient.getGetOptions());

            RunMetricsProperties runMetrics        = null;
            boolean              classified        = false;

            if ((process != null) && (process.getElementHeader() != null) && (process.getElementHeader().getRunMetrics() != null))
            {
                classified = true;

                if (process.getElementHeader().getRunMetrics().getClassificationProperties() instanceof RunMetricsProperties existingMetrics)
                {
                    runMetrics = new RunMetricsProperties(existingMetrics);
                }
            }

            if (runMetrics == null)
            {
                runMetrics = new RunMetricsProperties();
            }

            String runId     = getRunId(event);
            String eventType = event.getEventType();
            Date   eventTime = parseTimestamp(event.getEventTime());

            if ((runId != null) && (! runId.equals(runMetrics.getLastRunId())))
            {
                /*
                 * First event seen for this run.
                 */
                runMetrics.setRunCount(runMetrics.getRunCount() + 1);
                runMetrics.setLastRunId(runId);
                runMetrics.setLastRunStartTime(eventTime);
                runMetrics.setLastRunEndTime(null);
                runMetrics.setLastRunDuration(0L);

                if (runMetrics.getFirstRunStartTime() == null)
                {
                    runMetrics.setFirstRunStartTime(eventTime);
                }
            }

            if (("START".equals(eventType)) && (eventTime != null))
            {
                runMetrics.setLastRunStartTime(eventTime);

                if ((runMetrics.getFirstRunStartTime() == null) || (eventTime.before(runMetrics.getFirstRunStartTime())))
                {
                    runMetrics.setFirstRunStartTime(eventTime);
                }
            }

            if (eventType != null)
            {
                runMetrics.setLastRunStatus(eventType);
            }

            if (("COMPLETE".equals(eventType)) || ("FAIL".equals(eventType)) || ("ABORT".equals(eventType)))
            {
                runMetrics.setLastRunEndTime(eventTime);

                if ((runMetrics.getLastRunStartTime() != null) && (eventTime != null))
                {
                    long duration = eventTime.getTime() - runMetrics.getLastRunStartTime().getTime();

                    runMetrics.setLastRunDuration(duration);
                    runMetrics.setTotalRunDuration(runMetrics.getTotalRunDuration() + duration);
                }

                if ("FAIL".equals(eventType))
                {
                    runMetrics.setFailedRunCount(runMetrics.getFailedRunCount() + 1);
                }
            }

            long    rowsRead          = 0;
            long    bytesRead         = 0;
            long    rowsWritten       = 0;
            long    bytesWritten      = 0;
            boolean statisticsPresent = false;

            if (event.getInputs() != null)
            {
                for (OpenLineageInputDataSet input : event.getInputs())
                {
                    if ((input != null) && (input.getInputFacets() != null))
                    {
                        OpenLineageInputStatisticsInputDataSetFacet    inputStatistics = input.getInputFacets().getInputStatistics();
                        OpenLineageDataQualityMetricsInputDataSetFacet inputMetrics    = input.getInputFacets().getDataQualityMetrics();

                        if (inputStatistics != null)
                        {
                            if (inputStatistics.getRowCount() != null)
                            {
                                rowsRead += inputStatistics.getRowCount();
                                statisticsPresent = true;
                            }
                            if (inputStatistics.getSize() != null)
                            {
                                bytesRead += inputStatistics.getSize();
                                statisticsPresent = true;
                            }
                        }
                        else if (inputMetrics != null)
                        {
                            if (inputMetrics.getRowCount() != null)
                            {
                                rowsRead += inputMetrics.getRowCount();
                                statisticsPresent = true;
                            }
                            if (inputMetrics.getBytes() != null)
                            {
                                bytesRead += inputMetrics.getBytes();
                                statisticsPresent = true;
                            }
                        }
                    }
                }
            }

            if (event.getOutputs() != null)
            {
                for (OpenLineageOutputDataSet output : event.getOutputs())
                {
                    if ((output != null) && (output.getOutputFacets() != null) && (output.getOutputFacets().getOutputStatistics() != null))
                    {
                        OpenLineageOutputStatisticsOutputDataSetFacet outputStatistics = output.getOutputFacets().getOutputStatistics();

                        if (outputStatistics.getRowCount() != null)
                        {
                            rowsWritten += outputStatistics.getRowCount();
                            statisticsPresent = true;
                        }
                        if (outputStatistics.getSize() != null)
                        {
                            bytesWritten += outputStatistics.getSize();
                            statisticsPresent = true;
                        }
                    }
                }
            }

            if (statisticsPresent)
            {
                runMetrics.setLastRunRowsRead(rowsRead);
                runMetrics.setLastRunBytesRead(bytesRead);
                runMetrics.setLastRunRowsWritten(rowsWritten);
                runMetrics.setLastRunBytesWritten(bytesWritten);
                runMetrics.setTotalRowsRead(runMetrics.getTotalRowsRead() + rowsRead);
                runMetrics.setTotalBytesRead(runMetrics.getTotalBytesRead() + bytesRead);
                runMetrics.setTotalRowsWritten(runMetrics.getTotalRowsWritten() + rowsWritten);
                runMetrics.setTotalBytesWritten(runMetrics.getTotalBytesWritten() + bytesWritten);
            }

            if (classified)
            {
                classificationClient.updateRunMetricsClassification(jobProcess.guid(), runMetrics, classificationClient.getUpdateOptions(false));
            }
            else
            {
                classificationClient.addRunMetricsClassification(jobProcess.guid(), runMetrics, classificationClient.getMetadataSourceOptions());
            }
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "process run metrics", eventDescription, error);
        }
    }


    /**
     * Return the elements behind a run from the governance action publisher - the governance action process, the
     * process step and the process instance - using the process instance and process step named in the run's
     * egeria_governanceAction facet (see OpenLineageGovernanceActionResolver).  A failure is logged and treated as
     * a run that is not from a governance action process, so the run is still catalogued as a job.
     *
     * @param run run from the event
     * @param eventDescription description of the event for logging
     * @return governance action elements, or null if the run is not from a governance action process
     */
    private OpenLineageGovernanceActionResolver.GovernanceActionElements resolveGovernanceAction(OpenLineageRun run,
                                                                                                 String         eventDescription)
    {
        final String methodName = "resolveGovernanceAction";

        if ((run == null) || (run.getFacets() == null) || (run.getFacets().getEgeriaGovernanceAction() == null))
        {
            return null;
        }

        try
        {
            return governanceActionResolver.resolve(run.getFacets().getEgeriaGovernanceAction());
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "governance action process", eventDescription, error);

            return null;
        }
    }


    /**
     * Return the information supply chain that the run is part of.  It is named by Egeria's
     * egeria_informationSupplyChain run facet (which any producer, such as an Apache Airflow DAG, can add), or else by
     * the egeria_governanceAction facet that the governance action publisher adds.  The lineage relationships
     * created from the event are tagged with it.
     *
     * @param run run from the event
     * @return qualified name of the information supply chain or null
     */
    private String getISCQualifiedName(OpenLineageRun run)
    {
        if ((run == null) || (run.getFacets() == null))
        {
            return null;
        }

        if ((run.getFacets().getEgeriaInformationSupplyChain() != null) &&
            (run.getFacets().getEgeriaInformationSupplyChain().getIscQualifiedName() != null))
        {
            return run.getFacets().getEgeriaInformationSupplyChain().getIscQualifiedName();
        }

        if (run.getFacets().getEgeriaGovernanceAction() != null)
        {
            return run.getFacets().getEgeriaGovernanceAction().getIscQualifiedName();
        }

        return null;
    }


    /* =======================================================================================
     * Datasets
     */

    /**
     * Catalogue the input datasets and link them to the job's process with DataFlow relationships.
     *
     * @param inputs input datasets from the event
     * @param jobProcess catalogued process for the job
     * @param iscQualifiedName qualified name of the information supply chain for the lineage (may be null)
     * @param eventDescription description of the event for logging
     * @return catalogued assets in the same order as the inputs (null entries for inputs that could not be catalogued)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private List<CataloguedElement> syncInputDataSets(List<OpenLineageInputDataSet> inputs,
                                                      CataloguedElement             jobProcess,
                                                      String                        iscQualifiedName,
                                                      String                        eventDescription) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        List<CataloguedElement> assets = new ArrayList<>();

        if (inputs != null)
        {
            for (OpenLineageInputDataSet input : inputs)
            {
                CataloguedElement asset = null;

                if (input != null)
                {
                    asset = syncDataSet(input.getNamespace(), input.getName(), input.getFacets(), iscQualifiedName, eventDescription);

                    ensureDataFlow(asset, jobProcess, iscQualifiedName);
                }

                assets.add(asset);
            }
        }

        return assets;
    }


    /**
     * Catalogue the output datasets and link the job's process to them with DataFlow relationships.
     *
     * @param outputs output datasets from the event
     * @param jobProcess catalogued process for the job
     * @param iscQualifiedName qualified name of the information supply chain for the lineage (may be null)
     * @param eventDescription description of the event for logging
     * @return catalogued assets in the same order as the outputs (null entries for outputs that could not be catalogued)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private List<CataloguedElement> syncOutputDataSets(List<OpenLineageOutputDataSet> outputs,
                                                       CataloguedElement              jobProcess,
                                                       String                         iscQualifiedName,
                                                       String                         eventDescription) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        List<CataloguedElement> assets = new ArrayList<>();

        if (outputs != null)
        {
            for (OpenLineageOutputDataSet output : outputs)
            {
                CataloguedElement asset = null;

                if (output != null)
                {
                    asset = syncDataSet(output.getNamespace(), output.getName(), output.getFacets(), iscQualifiedName, eventDescription);

                    ensureDataFlow(jobProcess, asset, iscQualifiedName);
                }

                assets.add(asset);
            }
        }

        return assets;
    }


    /**
     * Find or create the element that represents an OpenLineage dataset.  The lineage from OpenLineage events is
     * attached to the elements that describe the physical landscape, and the abstractions over them (tabular data
     * sets and tabular data set collections, such as the data sets of digital products) are linked above them with
     * DataSetContent.  See OpenLineageDataSetResolver for how the matching elements are found.
     * <ul>
     *     <li>The first of the matching physical elements is used for the dataset.  Elements catalogued by technology
     *     connectors come before elements created from OpenLineage events, and within each group the oldest comes
     *     first.  The other matching physical elements are linked to it with DISCOVERED PeerDuplicateLink
     *     relationships so that the duplicate management process can resolve them.</li>
     *     <li>A relational table is a schema attribute rather than an asset.  The lineage is attached to the table and
     *     to the asset that the table belongs to (see CataloguedElement.parentAsset).</li>
     *     <li>If there is no physical element but the dataset is itself an abstraction (for example a tabular data set
     *     collection named in the egeria namespace), the abstraction is used rather than creating a new asset.</li>
     *     <li>Otherwise a new asset is created with the qualified name {typeName}::{namespace}::{name}, from the
     *     catalog template for the technology in the namespace (see getDataSetTemplate), or as a generic DataStore
     *     if the technology is not known.</li>
     *     <li>Each abstraction is linked to the element it is a view over with a DataSetContent relationship for the
     *     event's information supply chain, if there is not one already.</li>
     * </ul>
     * The dataset facets update the element only when this connector owns it (it created it from OpenLineage events);
     * elements catalogued by technology connectors only gain an ownership classification (if they have none) and the
     * OpenLineage identity (resourceName/namespacePath) if they have none.
     *
     * @param namespace dataset namespace
     * @param name dataset name
     * @param facets dataset facets (may be null)
     * @param iscQualifiedName qualified name of the information supply chain for the relationships (may be null)
     * @param eventDescription description of the event for logging
     * @return catalogued element or null if the dataset is not identifiable
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private CataloguedElement syncDataSet(String                   namespace,
                                          String                   name,
                                          OpenLineageDataSetFacets facets,
                                          String                   iscQualifiedName,
                                          String                   eventDescription) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        if (name == null)
        {
            logEventIgnored(eventDescription, "a dataset has no name");
            return null;
        }

        AssetClient         assetClient      = myContext.getAssetClient();
        TemplateDefinition  template         = getDataSetTemplate(namespace, name);
        DataAssetProperties assetProperties  = getDataSetProperties(template, namespace, name, facets);
        String              expectedTypeName = assetProperties.getTypeName();
        String              qualifiedName    = assetProperties.getQualifiedName();

        /*
         * A renamed dataset is identified by its previous name - rename the asset created for it from OpenLineage
         * events before looking it up under its new identity.  Elements catalogued by technology connectors are
         * renamed by those connectors.
         */
        OpenLineageLifecycleStateChangeDataSetFacet lifecycleStateChange = (facets == null) ? null : facets.getLifecycleStateChange();

        if ((lifecycleStateChange != null) && ("RENAME".equalsIgnoreCase(lifecycleStateChange.getLifecycleStateChange())) &&
            (lifecycleStateChange.getPreviousIdentifier() != null) && (lifecycleStateChange.getPreviousIdentifier().getName() != null))
        {
            renameDataSet(lifecycleStateChange.getPreviousIdentifier().getNamespace(),
                          lifecycleStateChange.getPreviousIdentifier().getName(),
                          namespace,
                          name,
                          expectedTypeName,
                          qualifiedName,
                          eventDescription);

            /*
             * The asset has moved from the previous identity to the new one, so neither cached resolution is valid.
             */
            resolver.forget(lifecycleStateChange.getPreviousIdentifier().getNamespace(), lifecycleStateChange.getPreviousIdentifier().getName());
            resolver.forget(namespace, name);
        }

        OpenLineageDataSetResolver.ResolvedDataSet resolvedDataSet = resolveDataSet(namespace, name, eventDescription);
        OpenLineageDataSetResolver.MatchedElement  primary         = resolvedDataSet.primary();

        /*
         * With no physical element, an abstraction that is the dataset itself is used rather than a new asset.
         */
        if (primary == null)
        {
            for (OpenLineageDataSetResolver.MatchedElement abstraction : resolvedDataSet.abstractions())
            {
                if (! abstraction.coversParent())
                {
                    primary = abstraction;
                    break;
                }
            }
        }

        CataloguedElement asset;
        boolean           ownedByThisConnector;

        if (primary == null)
        {
            /*
             * A new asset: from the catalog template for the dataset's technology, or a generic DataStore (that can
             * be retyped later) if the technology is not known.
             */
            TemplateOptions templateOptions = new TemplateOptions(assetClient.getMetadataSourceOptions());
            templateOptions.setIsOwnAnchor(true);

            String assetGUID = createAsset(assetClient,
                                           templateOptions,
                                           template,
                                           assetProperties,
                                           getPlaceholderProperties(template, namespace, name, assetProperties.getDescription()),
                                           null);

            assetClient.publishElement(assetGUID);

            resolver.forget(namespace, name);

            addOwnership(assetGUID, null, (facets == null) ? null : facets.getOwnership());

            logElementCatalogued(expectedTypeName, qualifiedName, assetGUID, "dataset", namespace + "/" + name);

            asset                = new CataloguedElement(assetGUID, qualifiedName, null);
            ownedByThisConnector = true;

            if ((catalogSchemas) && (facets != null) && (facets.getSchema() != null))
            {
                createSchema(assetGUID, qualifiedName, facets.getSchema(), eventDescription);
            }
        }
        else if (primary.schemaAttribute())
        {
            CataloguedElement parentAsset = null;

            if (primary.parentAssetGUID() != null)
            {
                parentAsset = new CataloguedElement(primary.parentAssetGUID(), primary.parentAssetQualifiedName(), null);
            }

            SchemaAttributeClient   schemaAttributeClient = myContext.getSchemaAttributeClient();
            OpenMetadataRootElement table                 = schemaAttributeClient.getSchemaAttributeByGUID(primary.guid(), schemaAttributeClient.getGetOptions());

            asset                = new CataloguedElement(primary.guid(), primary.qualifiedName(), table, true, parentAsset);
            ownedByThisConnector = false;

            if ((facets != null) && (table != null) && (table.getElementHeader().getOwnership() == null))
            {
                addOwnership(primary.guid(), null, facets.getOwnership());
            }
        }
        else
        {
            OpenMetadataRootElement existingAsset = assetClient.getAssetByGUID(primary.guid(), assetClient.getGetOptions());

            asset                = new CataloguedElement(primary.guid(), primary.qualifiedName(), existingAsset);
            ownedByThisConnector = primary.createdFromOpenLineage();

            if ((facets != null) && (existingAsset != null))
            {
                if (ownedByThisConnector)
                {
                    updateDataSetProperties(existingAsset, facets, eventDescription);
                }

                if (existingAsset.getElementHeader().getOwnership() == null)
                {
                    addOwnership(primary.guid(), null, facets.getOwnership());
                }
            }

            if (existingAsset != null)
            {
                recordOpenLineageIdentity(existingAsset, namespace, name);
            }
        }

        linkPeerDuplicateElements(asset, resolvedDataSet.physical(), namespace, name, eventDescription);
        linkAbstractions(asset, resolvedDataSet.abstractions(), iscQualifiedName, eventDescription);

        /*
         * A dropped dataset is archived or deleted according to the connector's delete method, and takes no
         * further part in the lineage of this event.  Only elements this connector owns are dropped - elements
         * catalogued by technology connectors are removed by those connectors.
         */
        if ((lifecycleStateChange != null) && ("DROP".equalsIgnoreCase(lifecycleStateChange.getLifecycleStateChange())))
        {
            if (ownedByThisConnector)
            {
                dropDataSet(asset, eventDescription);
                resolver.forget(namespace, name);
            }

            return null;
        }

        return asset;
    }


    /**
     * Update an asset that this connector created from OpenLineage events with the latest dataset facets.  The
     * description is only set if the asset has none (stewards' edits are preserved).
     *
     * @param existingAsset asset
     * @param facets dataset facets
     * @param eventDescription description of the event for logging
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void updateDataSetProperties(OpenMetadataRootElement  existingAsset,
                                         OpenLineageDataSetFacets facets,
                                         String                   eventDescription) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        if (! (existingAsset.getProperties() instanceof AssetProperties existingProperties))
        {
            return;
        }

        AssetClient         assetClient          = myContext.getAssetClient();
        String              assetGUID            = existingAsset.getElementHeader().getGUID();
        DataAssetProperties updateProperties     = new DataAssetProperties();
        Map<String, String> additionalProperties = new HashMap<>();

        if (existingProperties.getAdditionalProperties() != null)
        {
            additionalProperties.putAll(existingProperties.getAdditionalProperties());
        }

        fillDataSetProperties(updateProperties, facets, additionalProperties);

        boolean changed = false;

        if ((existingProperties.getDescription() == null) && (updateProperties.getDescription() != null))
        {
            changed = true;
        }
        else
        {
            updateProperties.setDescription(null);
        }

        if ((updateProperties.getVersionIdentifier() != null) && (! updateProperties.getVersionIdentifier().equals(existingProperties.getVersionIdentifier())))
        {
            changed = true;
        }
        else
        {
            updateProperties.setVersionIdentifier(null);
        }

        if (! additionalProperties.equals(existingProperties.getAdditionalProperties()))
        {
            updateProperties.setAdditionalProperties(additionalProperties);
            changed = true;
        }
        else
        {
            updateProperties.setAdditionalProperties(null);
        }

        if (changed)
        {
            assetClient.updateAsset(assetGUID, assetClient.getUpdateOptions(true), updateProperties);
        }

        if ((catalogSchemas) && (facets.getSchema() != null))
        {
            SchemaTypeClient schemaTypeClient = myContext.getSchemaTypeClient();

            if (schemaTypeClient.getSchemaTypeForAsset(assetGUID, schemaTypeClient.getGetOptions()) == null)
            {
                createSchema(assetGUID, getQualifiedName(existingAsset), facets.getSchema(), eventDescription);
            }
        }
    }


    /**
     * Apply a dataset rename to the existing asset.  The asset is located under its previous identity (the qualified
     * name this connector would have used, or a single type-compatible match on namespace and resource name) and,
     * provided nothing is already catalogued under the new identity, its names are updated in place.  The repository
     * is bi-temporal so the previous names remain visible at their point in time.  The qualified name is only changed
     * when it followed this connector's convention for the previous name, so that assets named by other connectors
     * keep the qualified name those connectors expect.
     *
     * @param previousNamespace namespace before the rename
     * @param previousName name before the rename
     * @param namespace namespace after the rename
     * @param name name after the rename
     * @param expectedTypeName open metadata type expected for the dataset
     * @param qualifiedName qualified name this connector uses for the new identity
     * @param eventDescription description of the event for logging
     */
    private void renameDataSet(String previousNamespace,
                               String previousName,
                               String namespace,
                               String name,
                               String expectedTypeName,
                               String qualifiedName,
                               String eventDescription)
    {
        final String methodName = "renameDataSet";

        try
        {
            AssetClient assetClient = myContext.getAssetClient();

            if (assetClient.getAssetByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, assetClient.getGetOptions()) != null)
            {
                logEventIgnored(eventDescription, "the rename of " + previousNamespace + "/" + previousName + " cannot be applied because an asset already exists for " + namespace + "/" + name);
                return;
            }

            String previousQualifiedName = getDataSetQualifiedName(expectedTypeName, previousNamespace, previousName);

            OpenMetadataRootElement previousAsset = assetClient.getAssetByUniqueName(previousQualifiedName,
                                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                     assetClient.getGetOptions());

            if (previousAsset == null)
            {
                List<OpenMetadataRootElement> matchingAssets = findMatchingAssets(previousNamespace, previousName);

                if ((matchingAssets.size() == 1) && (propertyHelper.isTypeOf(matchingAssets.get(0).getElementHeader(), expectedTypeName)))
                {
                    previousAsset = matchingAssets.get(0);
                }
            }

            if (previousAsset == null)
            {
                return;
            }

            DataAssetProperties updateProperties = new DataAssetProperties();

            updateProperties.setDisplayName(name);
            updateProperties.setResourceName(name);
            updateProperties.setNamespacePath(namespace);

            if (previousAsset.getProperties() instanceof AssetProperties previousProperties)
            {
                if (previousQualifiedName.equals(previousProperties.getQualifiedName()))
                {
                    updateProperties.setQualifiedName(qualifiedName);
                }

                if (previousProperties instanceof DataStoreProperties)
                {
                    DataStoreProperties dataStoreProperties = new DataStoreProperties();

                    dataStoreProperties.setDisplayName(name);
                    dataStoreProperties.setResourceName(name);
                    dataStoreProperties.setNamespacePath(namespace);
                    dataStoreProperties.setQualifiedName(updateProperties.getQualifiedName());
                    dataStoreProperties.setPathName(name);

                    updateProperties = dataStoreProperties;
                }
            }

            String assetGUID = previousAsset.getElementHeader().getGUID();

            assetClient.updateAsset(assetGUID, assetClient.getUpdateOptions(true), updateProperties);

            logRecord(methodName, OpenLineageIntegrationConnectorAuditCode.DATASET_RENAMED.getMessageDefinition(connectorName,
                                                                                                                 assetGUID,
                                                                                                                 previousNamespace + "/" + previousName,
                                                                                                                 namespace + "/" + name,
                                                                                                                 eventDescription));
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "rename of " + previousNamespace + "/" + previousName, eventDescription, error);
        }
    }


    /**
     * Apply a dataset drop to the asset.  The asset is archived (Memento classification) or deleted according to
     * the delete method configured for the connector, which is carried in the delete options from the context.
     * The archive details record the event that reported the drop.
     *
     * @param asset catalogued asset
     * @param eventDescription description of the event for logging
     */
    private void dropDataSet(CataloguedElement asset,
                             String            eventDescription)
    {
        final String methodName = "dropDataSet";

        try
        {
            AssetClient assetClient = myContext.getAssetClient();

            DeleteOptions deleteOptions = assetClient.getDeleteOptions(false);

            deleteOptions.setArchiveDate(new Date());
            deleteOptions.setArchiveProcess(connectorName);

            Map<String, String> archiveProperties = new HashMap<>();

            archiveProperties.put("reason", "OpenLineage lifecycleStateChange DROP");
            archiveProperties.put("event", eventDescription);
            deleteOptions.setArchiveProperties(archiveProperties);

            assetClient.deleteAsset(asset.guid(), deleteOptions);

            logRecord(methodName, OpenLineageIntegrationConnectorAuditCode.DATASET_DROPPED.getMessageDefinition(connectorName,
                                                                                                                 asset.qualifiedName(),
                                                                                                                 asset.guid(),
                                                                                                                 (deleteOptions.getDeleteMethod() == null) ? "default" : deleteOptions.getDeleteMethod().name(),
                                                                                                                 eventDescription));
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "drop of " + asset.qualifiedName(), eventDescription, error);
        }
    }


    /**
     * Find the elements in open metadata that describe a dataset.  A failure to resolve is logged and treated as no
     * match so that the lineage is still catalogued.
     *
     * @param namespace dataset namespace
     * @param name dataset name
     * @param eventDescription description of the event for logging
     * @return resolved dataset (the lists are empty if nothing matches)
     */
    private OpenLineageDataSetResolver.ResolvedDataSet resolveDataSet(String namespace,
                                                                      String name,
                                                                      String eventDescription)
    {
        final String methodName = "resolveDataSet";

        try
        {
            return resolver.resolve(namespace, name);
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "resolution of dataset " + namespace + "/" + name, eventDescription, error);

            return new OpenLineageDataSetResolver.ResolvedDataSet(new ArrayList<>(), new ArrayList<>());
        }
    }


    /**
     * Record the OpenLineage namespace and name of a dataset in an existing asset that has neither, so that the
     * asset is recognized by its OpenLineage identity from now on (for example, by the governance action publisher
     * when the asset is an action target).
     *
     * @param existingAsset asset used for the dataset
     * @param namespace dataset namespace
     * @param name dataset name
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void recordOpenLineageIdentity(OpenMetadataRootElement existingAsset,
                                           String                  namespace,
                                           String                  name) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        if ((namespace == null) || (GovernanceActionOpenLineageIntegrationConnector.EGERIA_NAMESPACE.equals(namespace)))
        {
            return;
        }

        if ((existingAsset.getProperties() instanceof AssetProperties existingProperties) &&
            (existingProperties.getResourceName() == null) && (existingProperties.getNamespacePath() == null))
        {
            AssetClient     assetClient      = myContext.getAssetClient();
            AssetProperties updateProperties = new AssetProperties();

            updateProperties.setTypeName(existingProperties.getTypeName());
            updateProperties.setResourceName(name);
            updateProperties.setNamespacePath(namespace);

            assetClient.updateAsset(existingAsset.getElementHeader().getGUID(), assetClient.getUpdateOptions(true), updateProperties);
        }
    }


    /**
     * Link each abstraction over a dataset (a tabular data set or tabular data set collection) to the element it is a
     * view over with a DataSetContent relationship - the data set is at end 1 and its content at end 2.  An
     * abstraction over the dataset's parent (for example a collection over the schema that holds a table) is linked
     * to the parent.  DataSetContent is multi-link: there is one relationship for each information supply chain.
     *
     * @param asset element used for the dataset
     * @param abstractions abstractions over the dataset
     * @param iscQualifiedName qualified name of the information supply chain (may be null)
     * @param eventDescription description of the event for logging
     */
    private void linkAbstractions(CataloguedElement                               asset,
                                  List<OpenLineageDataSetResolver.MatchedElement> abstractions,
                                  String                                          iscQualifiedName,
                                  String                                          eventDescription)
    {
        final String methodName = "linkAbstractions";

        for (OpenLineageDataSetResolver.MatchedElement abstraction : abstractions)
        {
            CataloguedElement content = asset;

            if ((abstraction.coversParent()) && (asset.parentAsset() != null))
            {
                content = asset.parentAsset();
            }

            if (abstraction.guid().equals(content.guid()))
            {
                continue;
            }

            try
            {
                if (! isRelationshipKnown(abstraction.guid(), content.guid(), OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, iscQualifiedName))
                {
                    AssetClient              assetClient = myContext.getAssetClient();
                    DataSetContentProperties properties  = new DataSetContentProperties();

                    properties.setISCQualifiedName(iscQualifiedName);

                    assetClient.linkDataSetContent(abstraction.guid(),
                                                   content.guid(),
                                                   new MakeAnchorOptions(assetClient.getMetadataSourceOptions()),
                                                   properties);

                    rememberRelationship(abstraction.guid(), content.guid(), OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, iscQualifiedName);

                    logLineageCatalogued(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, abstraction.qualifiedName(), content.qualifiedName());
                }
            }
            catch (Exception error)
            {
                logOptionalMetadataFailed(methodName, "link from " + abstraction.qualifiedName() + " to " + content.qualifiedName(), eventDescription, error);
            }
        }
    }


    /**
     * Find the assets whose resourceName and namespacePath match an OpenLineage dataset's name and namespace.
     * These are the values extracted from the technology by whichever connector catalogued the asset, so they
     * are the most reliable way to recognise the same resource.  Other naming conventions used by technologies
     * that publish OpenLineage can be added here as they are discovered.
     *
     * @param namespace dataset namespace
     * @param name dataset name
     * @return list of matching assets (empty if none)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private List<OpenMetadataRootElement> findMatchingAssets(String namespace,
                                                             String name) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        List<OpenMetadataRootElement> matchingAssets = new ArrayList<>();

        AssetClient assetClient = myContext.getAssetClient();
        int         pageSize    = myContext.getMaxPageSize();
        int         startFrom   = 0;

        List<OpenMetadataRootElement> candidates = assetClient.getAssetsByName(name, assetClient.getQueryOptions(startFrom, pageSize));

        while ((candidates != null) && (! candidates.isEmpty()))
        {
            for (OpenMetadataRootElement candidate : candidates)
            {
                if ((candidate != null) && (candidate.getElementHeader() != null) && (candidate.getProperties() instanceof AssetProperties candidateProperties))
                {
                    if ((name.equals(candidateProperties.getResourceName())) && (java.util.Objects.equals(namespace, candidateProperties.getNamespacePath())))
                    {
                        matchingAssets.add(candidate);
                    }
                }
            }

            if (candidates.size() < pageSize)
            {
                break;
            }

            startFrom  = startFrom + pageSize;
            candidates = assetClient.getAssetsByName(name, assetClient.getQueryOptions(startFrom, pageSize));
        }

        return matchingAssets;
    }


    /**
     * Link the assets that match an OpenLineage job or dataset, but could not be used directly, to the element that
     * is being used with DISCOVERED PeerDuplicateLink relationships.  The duplicate management process (stewards, or
     * the automated duplicate manager) decides whether they are the same resource.
     *
     * @param asset element used for the job or dataset
     * @param peerDuplicates matching assets
     * @param namespace OpenLineage namespace
     * @param name OpenLineage name
     * @param eventDescription description of the event for logging
     */
    private void linkPeerDuplicates(CataloguedElement             asset,
                                    List<OpenMetadataRootElement> peerDuplicates,
                                    String                        namespace,
                                    String                        name,
                                    String                        eventDescription)
    {
        for (OpenMetadataRootElement peerDuplicate : peerDuplicates)
        {
            linkPeerDuplicate(asset,
                              peerDuplicate.getElementHeader().getGUID(),
                              getQualifiedName(peerDuplicate),
                              "Both assets have resourceName " + name + " and namespacePath " + namespace +
                                      ", which is the identity of a dataset or job in OpenLineage events (" + eventDescription + ").",
                              eventDescription);
        }
    }


    /**
     * Link the other physical elements that describe an OpenLineage dataset to the element that is being used for
     * it with DISCOVERED PeerDuplicateLink relationships.
     *
     * @param asset element used for the dataset
     * @param physicalElements all the physical elements that describe the dataset
     * @param namespace OpenLineage namespace
     * @param name OpenLineage name
     * @param eventDescription description of the event for logging
     */
    private void linkPeerDuplicateElements(CataloguedElement                               asset,
                                           List<OpenLineageDataSetResolver.MatchedElement> physicalElements,
                                           String                                          namespace,
                                           String                                          name,
                                           String                                          eventDescription)
    {
        for (OpenLineageDataSetResolver.MatchedElement physicalElement : physicalElements)
        {
            if (! physicalElement.guid().equals(asset.guid()))
            {
                linkPeerDuplicate(asset,
                                  physicalElement.guid(),
                                  physicalElement.qualifiedName(),
                                  "Both elements describe the OpenLineage dataset " + namespace + "/" + name + " (" + eventDescription + ").",
                                  eventDescription);
            }
        }
    }


    /**
     * Link a peer duplicate to the element being used, unless they are already linked.
     *
     * @param asset element being used
     * @param peerGUID unique identifier of the peer duplicate
     * @param peerQualifiedName qualified name of the peer duplicate
     * @param notes why they are thought to be duplicates
     * @param eventDescription description of the event for logging
     */
    private void linkPeerDuplicate(CataloguedElement asset,
                                   String            peerGUID,
                                   String            peerQualifiedName,
                                   String            notes,
                                   String            eventDescription)
    {
        final String methodName = "linkPeerDuplicate";

        try
        {
            if ((! isRelationshipKnown(asset.guid(), peerGUID, OpenMetadataType.PEER_DUPLICATE_LINK.typeName)) &&
                (! isRelationshipKnown(peerGUID, asset.guid(), OpenMetadataType.PEER_DUPLICATE_LINK.typeName)))
            {
                ClassificationExplorerClient classificationClient = myContext.getClassificationExplorerClient();

                PeerDuplicateLinkProperties properties = new PeerDuplicateLinkProperties();

                properties.setStatusIdentifier(StatusIdentifier.DISCOVERED.getOrdinal());
                properties.setSource(connectorName);
                properties.setNotes(notes);

                classificationClient.linkElementsAsPeerDuplicates(asset.guid(),
                                                                  peerGUID,
                                                                  properties,
                                                                  new MakeAnchorOptions(classificationClient.getMetadataSourceOptions()));

                rememberRelationship(asset.guid(), peerGUID, OpenMetadataType.PEER_DUPLICATE_LINK.typeName);

                logLineageCatalogued(OpenMetadataType.PEER_DUPLICATE_LINK.typeName, asset.qualifiedName(), peerQualifiedName);
            }
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "peer duplicate link for " + asset.qualifiedName(), eventDescription, error);
        }
    }


    /**
     * Create an asset from its catalog template, or directly from its properties if there is no template or the
     * template is not loaded (the templates for some technologies are in optional content packs - for example the
     * PostgreSQL Table template is in the Postgres content pack).  The properties set the asset's type, names,
     * technology type and OpenLineage identity either way.
     *
     * @param assetClient client
     * @param options anchor and parent options
     * @param template catalog template (may be null)
     * @param properties properties of the new asset
     * @param placeholderProperties values for the template's placeholders
     * @param parentRelationshipProperties properties of the relationship to the parent (may be null)
     * @return unique identifier of the new asset
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private String createAsset(AssetClient                   assetClient,
                               TemplateOptions               options,
                               TemplateDefinition            template,
                               AssetProperties               properties,
                               Map<String, String>           placeholderProperties,
                               ProcessHierarchyProperties    parentRelationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        if ((template != null) && (isTemplateLoaded(assetClient, template)))
        {
            return assetClient.createAssetFromTemplate(options,
                                                       template.getTemplateGUID(),
                                                       properties,
                                                       null,
                                                       placeholderProperties,
                                                       parentRelationshipProperties);
        }

        return assetClient.createAsset(options, null, properties, parentRelationshipProperties);
    }


    /**
     * Is the template loaded into the open metadata repositories?  The answer is remembered, since content packs are
     * not loaded while the connector is running.
     *
     * @param assetClient client
     * @param template catalog template
     * @return boolean
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private boolean isTemplateLoaded(AssetClient        assetClient,
                                     TemplateDefinition template) throws PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        Boolean loaded = loadedTemplates.get(template.getTemplateGUID());

        if (loaded == null)
        {
            try
            {
                loaded = (assetClient.getAssetByGUID(template.getTemplateGUID(), assetClient.getGetOptions()) != null);
            }
            catch (InvalidParameterException unknownTemplate)
            {
                loaded = false;
            }

            loadedTemplates.put(template.getTemplateGUID(), loaded);
        }

        return loaded;
    }


    /**
     * Choose the catalog template for a job that is not catalogued yet, from its jobType facet (the integration
     * that ran it and the type of job).
     *
     * @param facets job facets (may be null)
     * @return template, or null for a generic DeployedSoftwareComponent
     */
    private TemplateDefinition getJobTemplate(OpenLineageJobFacets facets)
    {
        if ((facets == null) || (facets.getJobType() == null) || (facets.getJobType().getIntegration() == null))
        {
            return null;
        }

        String integration = facets.getJobType().getIntegration().toUpperCase();
        String jobType     = (facets.getJobType().getJobType() == null) ? "" : facets.getJobType().getJobType().toUpperCase();

        switch (integration)
        {
            case "AIRFLOW" -> { return ("DAG".equals(jobType)) ? DataPipelineTemplateType.APACHE_AIRFLOW_DAG_TEMPLATE : DataPipelineTemplateType.APACHE_AIRFLOW_TASK_TEMPLATE; }
            case "SPARK" -> { return DataPipelineTemplateType.APACHE_SPARK_JOB_TEMPLATE; }
            case "DEBEZIUM" -> { return DataPipelineTemplateType.DEBEZIUM_TASK_TEMPLATE; }
            case "SQL" -> { return DataPipelineTemplateType.SQL_JOB_TEMPLATE; }
            default -> { return ("QUERY".equals(jobType)) ? DataPipelineTemplateType.SQL_JOB_TEMPLATE : null; }
        }
    }


    /**
     * Choose the catalog template for a dataset that is not catalogued yet, from the technology in its namespace's
     * URI scheme (see https://openlineage.io/docs/spec/naming).  The templates create just the asset (see the
     * DatabaseTable, NoSQL, FileStore, DocumentManagement, EventStream and DataPipeline template types), apart from
     * the Apache Kafka topic template, which also creates the topic's connection.
     *
     * @param namespace dataset namespace
     * @param name dataset name
     * @return template, or null if the technology is not known (the dataset becomes a generic DataStore)
     */
    private TemplateDefinition getDataSetTemplate(String namespace,
                                                  String name)
    {
        String  rawScheme = getRawNamespaceScheme(namespace);
        String  scheme    = getNamespaceScheme(namespace);
        boolean isFolder  = name.endsWith("/");

        if (scheme == null)
        {
            return null;
        }

        switch (scheme)
        {
            case "postgres" -> { return DatabaseTableTemplateType.POSTGRESQL_TABLE_TEMPLATE; }
            case "mssql" -> { return ("sqlserver".equals(rawScheme)) ? DatabaseTableTemplateType.AZURE_SYNAPSE_TABLE_TEMPLATE : DatabaseTableTemplateType.MSSQL_TABLE_TEMPLATE; }
            case "oracle" -> { return DatabaseTableTemplateType.ORACLE_TABLE_TEMPLATE; }
            case "db2" -> { return DatabaseTableTemplateType.DB2LUW_TABLE_TEMPLATE; }
            case "awsathena" -> { return DatabaseTableTemplateType.AMAZON_ATHENA_TABLE_TEMPLATE; }
            case "arn" -> { return DatabaseTableTemplateType.AWS_GLUE_TABLE_TEMPLATE; }
            case "azurekusto" -> { return DatabaseTableTemplateType.AZURE_DATA_EXPLORER_TABLE_TEMPLATE; }
            case "bigquery" -> { return DatabaseTableTemplateType.GOOGLE_BIGQUERY_TABLE_TEMPLATE; }
            case "fabric-warehouse" -> { return DatabaseTableTemplateType.MICROSOFT_FABRIC_WAREHOUSE_TABLE_TEMPLATE; }
            case "mysql" -> { return DatabaseTableTemplateType.MYSQL_TABLE_TEMPLATE; }
            case "crate" -> { return DatabaseTableTemplateType.CRATEDB_TABLE_TEMPLATE; }
            case "hive" -> { return DatabaseTableTemplateType.APACHE_HIVE_TABLE_TEMPLATE; }
            case "oceanbase" -> { return DatabaseTableTemplateType.OCEANBASE_TABLE_TEMPLATE; }
            case "teradata" -> { return DatabaseTableTemplateType.TERADATA_TABLE_TEMPLATE; }
            case "redshift" -> { return DatabaseTableTemplateType.AMAZON_REDSHIFT_TABLE_TEMPLATE; }
            case "snowflake" -> { return DatabaseTableTemplateType.SNOWFLAKE_TABLE_TEMPLATE; }
            case "spanner" -> { return DatabaseTableTemplateType.GOOGLE_SPANNER_TABLE_TEMPLATE; }
            case "trino" -> { return DatabaseTableTemplateType.TRINO_TABLE_TEMPLATE; }
            case "unitycatalog" -> { return DatabaseTableTemplateType.UNITY_CATALOG_TABLE_TEMPLATE; }
            case "cassandra" -> { return NoSQLTemplateType.APACHE_CASSANDRA_TABLE_TEMPLATE; }
            case "azurecosmos" -> { return NoSQLTemplateType.AZURE_COSMOS_DB_COLLECTION_TEMPLATE; }
            case "milvus" -> { return NoSQLTemplateType.MILVUS_COLLECTION_TEMPLATE; }
            case "s3" -> { return isFolder ? FileStoreTemplateType.AMAZON_S3_FOLDER_TEMPLATE : FileStoreTemplateType.AMAZON_S3_OBJECT_TEMPLATE; }
            case "gs" -> { return isFolder ? FileStoreTemplateType.GOOGLE_CLOUD_STORAGE_FOLDER_TEMPLATE : FileStoreTemplateType.GOOGLE_CLOUD_STORAGE_OBJECT_TEMPLATE; }
            case "wasb", "wasbs" -> { return isFolder ? FileStoreTemplateType.AZURE_BLOB_STORAGE_FOLDER_TEMPLATE : FileStoreTemplateType.AZURE_BLOB_STORAGE_OBJECT_TEMPLATE; }
            case "abfs", "abfss" -> { return isFolder ? FileStoreTemplateType.AZURE_DATA_LAKE_STORAGE_DIRECTORY_TEMPLATE : FileStoreTemplateType.AZURE_DATA_LAKE_STORAGE_FILE_TEMPLATE; }
            case "hdfs" -> { return isFolder ? FileStoreTemplateType.HDFS_DIRECTORY_TEMPLATE : FileStoreTemplateType.HDFS_FILE_TEMPLATE; }
            case "dbfs" -> { return isFolder ? FileStoreTemplateType.DATABRICKS_FILE_SYSTEM_DIRECTORY_TEMPLATE : FileStoreTemplateType.DATABRICKS_FILE_SYSTEM_FILE_TEMPLATE; }
            case "file" -> { return isFolder ? FileStoreTemplateType.LOCAL_FOLDER_TEMPLATE : FileStoreTemplateType.LOCAL_FILE_TEMPLATE; }
            case "box" -> { return DocumentManagementTemplateType.BOX_FILE_TEMPLATE; }
            case "filenet" -> { return DocumentManagementTemplateType.IBM_FILENET_DOCUMENT_TEMPLATE; }
            case "mssharepoint" -> { return DocumentManagementTemplateType.MICROSOFT_SHAREPOINT_DOCUMENT_TEMPLATE; }
            case "pubsub" -> { return (name.startsWith("subscription:")) ? EventStreamTemplateType.GOOGLE_PUBSUB_SUBSCRIPTION_TEMPLATE : EventStreamTemplateType.GOOGLE_PUBSUB_TOPIC_TEMPLATE; }
            case "kafka" -> { return KafkaTemplateType.KAFKA_TOPIC_TEMPLATE; }
            case "inmemory" -> { return DataPipelineTemplateType.IN_MEMORY_DATA_SET_TEMPLATE; }
            default -> { return null; }
        }
    }


    /**
     * Return the properties for a new dataset asset: a bean of the template's open metadata type (or DataStore if
     * there is no template), named with this connector's convention and carrying the OpenLineage identity.  The
     * properties replace the template's values.
     *
     * @param template catalog template (may be null)
     * @param namespace dataset namespace
     * @param name dataset name
     * @param facets dataset facets (may be null)
     * @return properties bean
     */
    private DataAssetProperties getDataSetProperties(TemplateDefinition       template,
                                                     String                   namespace,
                                                     String                   name,
                                                     OpenLineageDataSetFacets facets)
    {
        String              typeName   = (template == null) ? OpenMetadataType.DATA_STORE.typeName : template.getDeployedImplementationType().getAssociatedTypeName();
        DataAssetProperties properties;

        if (OpenMetadataType.DATA_FOLDER.typeName.equals(typeName))
        {
            DataFolderProperties folderProperties = new DataFolderProperties();

            folderProperties.setPathName(name);
            properties = folderProperties;
        }
        else if (OpenMetadataType.DOCUMENT.typeName.equals(typeName))
        {
            DocumentProperties documentProperties = new DocumentProperties();

            documentProperties.setPathName(name);
            properties = documentProperties;
        }
        else if (OpenMetadataType.DATA_FILE.typeName.equals(typeName))
        {
            DataFileProperties fileProperties = new DataFileProperties();

            fileProperties.setPathName(name);
            properties = fileProperties;
        }
        else if (OpenMetadataType.TOPIC.typeName.equals(typeName))
        {
            properties = new TopicProperties();
        }
        else if (OpenMetadataType.DATA_FEED.typeName.equals(typeName))
        {
            properties = new DataFeedProperties();
        }
        else if (OpenMetadataType.VIRTUAL_RELATIONAL_TABLE.typeName.equals(typeName))
        {
            properties = new VirtualRelationalTableProperties();
        }
        else if (OpenMetadataType.DATA_SET.typeName.equals(typeName))
        {
            properties = new DataSetProperties();
        }
        else
        {
            DataStoreProperties storeProperties = new DataStoreProperties();

            storeProperties.setPathName(name);
            properties = storeProperties;
        }

        properties.setTypeName(typeName);
        properties.setQualifiedName(getDataSetQualifiedName(typeName, namespace, name));
        properties.setDisplayName(name);
        properties.setResourceName(name);
        properties.setNamespacePath(namespace);

        if (template != null)
        {
            properties.setDeployedImplementationType(template.getDeployedImplementationType().getDeployedImplementationType());

            /*
             * A local file's format says more than the generic data file technology type.
             */
            if (template == FileStoreTemplateType.LOCAL_FILE_TEMPLATE)
            {
                String fileFormat = ((facets != null) && (facets.getStorage() != null)) ? facets.getStorage().getFileFormat() : null;

                properties.setDeployedImplementationType(getFileDeployedImplementationType(fileFormat, name));
            }
        }

        fillDataSetProperties(properties, facets, new HashMap<>());

        return properties;
    }


    /**
     * Return the values for the placeholders in a template.  The properties that the connector sets replace most
     * of the template's values; the placeholders fill the rest (for example the connection and endpoint that the
     * Apache Kafka topic template creates).
     *
     * @param template catalog template
     * @param namespace dataset or job namespace
     * @param name dataset or job name
     * @param description description (may be null)
     * @return map of placeholder name to value
     */
    private Map<String, String> getPlaceholderProperties(TemplateDefinition template,
                                                         String             namespace,
                                                         String             name,
                                                         String             description)
    {
        Map<String, String> placeholderProperties = new HashMap<>();

        placeholderProperties.put(PlaceholderProperty.DISPLAY_NAME.getName(), name);
        placeholderProperties.put(PlaceholderProperty.RESOURCE_NAME.getName(), name);
        placeholderProperties.put(PlaceholderProperty.NAMESPACE_PATH.getName(), namespace);
        placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), description);
        placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);
        placeholderProperties.put(PlaceholderProperty.FILE_PATH_NAME.getName(), name);
        placeholderProperties.put(PlaceholderProperty.DIRECTORY_PATH_NAME.getName(), name);

        if (template == KafkaTemplateType.KAFKA_TOPIC_TEMPLATE)
        {
            OpenLineageNamespace location = OpenLineageNamespace.fromNamespace(namespace);

            placeholderProperties.put(PlaceholderProperty.HOST_IDENTIFIER.getName(), location.host());
            placeholderProperties.put(PlaceholderProperty.PORT_NUMBER.getName(), location.port());
            placeholderProperties.put(PlaceholderProperty.SERVER_NAME.getName(), (location.port() == null) ? location.host() : location.host() + ":" + location.port());
            placeholderProperties.put(KafkaPlaceholderProperty.FULL_TOPIC_NAME.getName(), name);
            placeholderProperties.put(KafkaPlaceholderProperty.SHORT_TOPIC_NAME.getName(), name);
            placeholderProperties.put(KafkaPlaceholderProperty.EVENT_DIRECTION.getName(), "inOut");
        }

        return placeholderProperties;
    }


    /**
     * Choose a deployed implementation type for a file based on its format or extension.
     *
     * @param fileFormat file format from the storage facet (may be null)
     * @param name dataset name (path)
     * @return deployed implementation type
     */
    private String getFileDeployedImplementationType(String fileFormat,
                                                     String name)
    {
        String format = fileFormat;

        if ((format == null) && (name.lastIndexOf('.') > name.lastIndexOf('/')))
        {
            format = name.substring(name.lastIndexOf('.') + 1);
        }

        if (format != null)
        {
            switch (format.toLowerCase())
            {
                case "csv" -> { return DeployedImplementationType.CSV_FILE.getDeployedImplementationType(); }
                case "json" -> { return DeployedImplementationType.JSON_FILE.getDeployedImplementationType(); }
                case "avro" -> { return DeployedImplementationType.AVRO_FILE.getDeployedImplementationType(); }
                case "parquet" -> { return DeployedImplementationType.PARQUET_FILE.getDeployedImplementationType(); }
                case "xml" -> { return DeployedImplementationType.XML_FILE.getDeployedImplementationType(); }
                case "yaml", "yml" -> { return DeployedImplementationType.YAML_FILE.getDeployedImplementationType(); }
                default -> { return DeployedImplementationType.DATA_FILE.getDeployedImplementationType(); }
            }
        }

        return DeployedImplementationType.DATA_FILE.getDeployedImplementationType();
    }


    /**
     * Copy the values from the dataset facets into the asset properties.
     *
     * @param assetProperties properties to fill
     * @param facets dataset facets (may be null)
     * @param additionalProperties additional properties map to fill (facets that have no natural home in the asset properties)
     */
    private void fillDataSetProperties(AssetProperties          assetProperties,
                                       OpenLineageDataSetFacets facets,
                                       Map<String, String>      additionalProperties)
    {
        if (facets == null)
        {
            return;
        }

        if ((facets.getDocumentation() != null) && (facets.getDocumentation().getDescription() != null))
        {
            assetProperties.setDescription(facets.getDocumentation().getDescription());
        }

        if ((facets.getVersion() != null) && (facets.getVersion().getDatasetVersion() != null))
        {
            assetProperties.setVersionIdentifier(facets.getVersion().getDatasetVersion());
        }

        if (facets.getDataSource() != null)
        {
            putIfNotNull(additionalProperties, "dataSource.name", facets.getDataSource().getName());
            putIfNotNull(additionalProperties, "dataSource.uri", facets.getDataSource().getUri());
        }

        if (facets.getDatasetType() != null)
        {
            putIfNotNull(additionalProperties, "datasetType", facets.getDatasetType().getDatasetType());
            putIfNotNull(additionalProperties, "datasetType.subType", facets.getDatasetType().getSubType());
        }

        if (facets.getStorage() != null)
        {
            putIfNotNull(additionalProperties, "storage.storageLayer", facets.getStorage().getStorageLayer());
            putIfNotNull(additionalProperties, "storage.fileFormat", facets.getStorage().getFileFormat());
        }

        if (facets.getCatalog() != null)
        {
            putIfNotNull(additionalProperties, "catalog.framework", facets.getCatalog().getFramework());
            putIfNotNull(additionalProperties, "catalog.type", facets.getCatalog().getType());
            putIfNotNull(additionalProperties, "catalog.name", facets.getCatalog().getName());
            putIfNotNull(additionalProperties, "catalog.metadataUri", facets.getCatalog().getMetadataUri());
            putIfNotNull(additionalProperties, "catalog.warehouseUri", facets.getCatalog().getWarehouseUri());
            putIfNotNull(additionalProperties, "catalog.source", facets.getCatalog().getSource());
        }

        if ((facets.getSymlinks() != null) && (facets.getSymlinks().getIdentifiers() != null))
        {
            for (OpenLineageSymlinksDataSetFacetIdentifier identifier : facets.getSymlinks().getIdentifiers())
            {
                if ((identifier != null) && (identifier.getName() != null))
                {
                    String key = (identifier.getType() != null) ? "symlink:" + identifier.getType() : "symlink";

                    additionalProperties.put(key, identifier.getNamespace() + "/" + identifier.getName());
                }
            }
        }

        if ((facets.getHierarchy() != null) && (facets.getHierarchy().getHierarchy() != null))
        {
            for (OpenLineageHierarchyDataSetFacetLevel level : facets.getHierarchy().getHierarchy())
            {
                if ((level != null) && (level.getType() != null))
                {
                    putIfNotNull(additionalProperties, "hierarchy:" + level.getType(), level.getName());
                }
            }
        }

        if ((facets.getTags() != null) && (facets.getTags().getTags() != null))
        {
            for (OpenLineageTagsDataSetFacetTag tag : facets.getTags().getTags())
            {
                if ((tag != null) && (tag.getKey() != null))
                {
                    if (tag.getField() != null)
                    {
                        putIfNotNull(additionalProperties, "tag:" + tag.getField() + ":" + tag.getKey(), tag.getValue());
                    }
                    else
                    {
                        putIfNotNull(additionalProperties, "tag:" + tag.getKey(), tag.getValue());
                    }
                }
            }
        }

        if (facets.getLifecycleStateChange() != null)
        {
            putIfNotNull(additionalProperties, LAST_LIFECYCLE_CHANGE_PROPERTY, facets.getLifecycleStateChange().getLifecycleStateChange());

            if (facets.getLifecycleStateChange().getPreviousIdentifier() != null)
            {
                putIfNotNull(additionalProperties, "previousIdentifier", facets.getLifecycleStateChange().getPreviousIdentifier().getNamespace() + "/" +
                        facets.getLifecycleStateChange().getPreviousIdentifier().getName());
            }
        }

        if (! additionalProperties.isEmpty())
        {
            assetProperties.setAdditionalProperties(additionalProperties);
        }
    }


    /**
     * Create a tabular schema for a data asset from the schema dataset facet.  Nested fields become nested schema attributes.
     *
     * @param assetGUID unique identifier of the asset
     * @param assetQualifiedName qualified name of the asset (prefix for the schema element qualified names)
     * @param schemaFacet schema facet
     * @param eventDescription description of the event for logging
     */
    private void createSchema(String                       assetGUID,
                              String                       assetQualifiedName,
                              OpenLineageSchemaDataSetFacet schemaFacet,
                              String                        eventDescription)
    {
        final String methodName = "createSchema";

        try
        {
            if ((schemaFacet.getFields() == null) || (schemaFacet.getFields().isEmpty()))
            {
                return;
            }

            SchemaTypeClient schemaTypeClient = myContext.getSchemaTypeClient();

            TabularSchemaTypeProperties schemaTypeProperties = new TabularSchemaTypeProperties();

            schemaTypeProperties.setTypeName(OpenMetadataType.TABULAR_SCHEMA_TYPE.typeName);
            schemaTypeProperties.setQualifiedName(assetQualifiedName + QUALIFIED_NAME_SEPARATOR + "schema");
            schemaTypeProperties.setDisplayName("Schema from OpenLineage");

            NewElementOptions newElementOptions = new NewElementOptions(schemaTypeClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(assetGUID);
            newElementOptions.setIsOwnAnchor(false);

            String schemaTypeGUID = schemaTypeClient.createSchemaType(newElementOptions, null, schemaTypeProperties, null);

            schemaTypeClient.linkSchema(assetGUID, schemaTypeGUID, new MakeAnchorOptions(schemaTypeClient.getMetadataSourceOptions()), null);

            createColumns(assetGUID, assetQualifiedName, schemaTypeGUID, OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName, schemaFacet.getFields());
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "schema for " + assetQualifiedName, eventDescription, error);
        }
    }


    /**
     * Create the columns (schema attributes) for a list of schema facet fields.
     *
     * @param assetGUID unique identifier of the asset (anchor)
     * @param parentQualifiedName qualified name of the asset or parent attribute
     * @param parentGUID unique identifier of the schema type or parent attribute
     * @param parentRelationshipTypeName relationship type to link the new attribute to its parent
     * @param fields fields to create
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void createColumns(String                                assetGUID,
                               String                                parentQualifiedName,
                               String                                parentGUID,
                               String                                parentRelationshipTypeName,
                               List<OpenLineageSchemaDataSetFacetField> fields) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        SchemaAttributeClient schemaAttributeClient = myContext.getSchemaAttributeClient();

        int position = 0;

        for (OpenLineageSchemaDataSetFacetField field : fields)
        {
            position++;

            if ((field != null) && (field.getName() != null))
            {
                TabularColumnProperties columnProperties = new TabularColumnProperties();

                columnProperties.setTypeName(OpenMetadataType.TABULAR_COLUMN.typeName);
                columnProperties.setQualifiedName(parentQualifiedName + QUALIFIED_NAME_SEPARATOR + field.getName());
                columnProperties.setDisplayName(field.getName());
                columnProperties.setDescription(field.getDescription());

                /*
                 * The position of the column is a property of the relationship to its parent.
                 */
                PartOfRelationshipProperties parentRelationshipProperties;

                if (OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName.equals(parentRelationshipTypeName))
                {
                    parentRelationshipProperties = new NestedSchemaAttributeProperties();
                }
                else
                {
                    parentRelationshipProperties = new AttributeForSchemaProperties();
                }

                if (field.getOrdinalPosition() != null)
                {
                    parentRelationshipProperties.setPosition(field.getOrdinalPosition().intValue());
                }
                else
                {
                    parentRelationshipProperties.setPosition(position);
                }

                Map<String, ClassificationProperties> initialClassifications = null;

                if (field.getType() != null)
                {
                    TypeEmbeddedAttributeProperties typeEmbeddedAttributeProperties = new TypeEmbeddedAttributeProperties();

                    typeEmbeddedAttributeProperties.setSchemaTypeName(OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName);
                    typeEmbeddedAttributeProperties.setDataType(field.getType());

                    initialClassifications = new HashMap<>();
                    initialClassifications.put(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName, typeEmbeddedAttributeProperties);
                }

                NewElementOptions newElementOptions = new NewElementOptions(schemaAttributeClient.getMetadataSourceOptions());

                newElementOptions.setAnchorGUID(assetGUID);
                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setParentGUID(parentGUID);
                newElementOptions.setParentRelationshipTypeName(parentRelationshipTypeName);
                newElementOptions.setParentAtEnd1(true);

                String columnGUID = schemaAttributeClient.createSchemaAttribute(newElementOptions, initialClassifications, columnProperties, parentRelationshipProperties);

                if ((field.getFields() != null) && (! field.getFields().isEmpty()))
                {
                    createColumns(assetGUID,
                                  columnProperties.getQualifiedName(),
                                  columnGUID,
                                  OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                  field.getFields());
                }
            }
        }
    }


    /* =======================================================================================
     * Lineage relationships
     */

    /**
     * Create the relationships described by the lineage job facet (OpenLineage 1.53+).  Each entry names a target
     * dataset or job and the datasets/jobs that feed it.
     *
     * @param job job from the event
     * @param jobProcess catalogued process for the job
     * @param iscQualifiedName qualified name of the information supply chain for the lineage (may be null)
     * @param eventDescription description of the event for logging
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void syncExplicitLineage(OpenLineageJob    job,
                                     CataloguedElement jobProcess,
                                     String            iscQualifiedName,
                                     String            eventDescription) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        if ((job == null) || (job.getFacets() == null) || (job.getFacets().getLineage() == null) || (job.getFacets().getLineage().getEntries() == null))
        {
            return;
        }

        for (OpenLineageLineageEntry entry : job.getFacets().getLineage().getEntries())
        {
            if (entry == null)
            {
                continue;
            }

            CataloguedElement target;

            if ("DATASET".equalsIgnoreCase(entry.getType()))
            {
                target = syncDataSet(entry.getNamespace(), entry.getName(), null, iscQualifiedName, eventDescription);

                /*
                 * The event's job writes the target dataset unless the inputs say otherwise.
                 */
                ensureDataFlow(jobProcess, target, iscQualifiedName);
            }
            else if (entry.getName() == null)
            {
                target = jobProcess;
            }
            else
            {
                target = syncJob(entry.getNamespace(), entry.getName(), null, null, eventDescription);
            }

            if (target == null)
            {
                continue;
            }

            if (entry.getInputs() != null)
            {
                for (OpenLineageLineageInput input : entry.getInputs())
                {
                    CataloguedElement source = syncLineageInput(input, jobProcess, iscQualifiedName, eventDescription);

                    if (source != null)
                    {
                        if (("JOB".equalsIgnoreCase(input.getType())) && ("JOB".equalsIgnoreCase(entry.getType())))
                        {
                            ensureLineage(source, target, OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName, getLineageProperties(new ControlFlowProperties(), input.getTransformations()), iscQualifiedName);
                        }
                        else
                        {
                            ensureLineage(source, target, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, getLineageProperties(new DataFlowProperties(), input.getTransformations()), iscQualifiedName);
                            ensureAssetLevelDataFlow(source, target, iscQualifiedName);
                            ensureTableMapping(source, target, iscQualifiedName);
                        }
                    }
                }
            }

            if (("DATASET".equalsIgnoreCase(entry.getType())) && (entry.getFields() != null))
            {
                syncFieldLineage(target, entry.getFields(), iscQualifiedName, eventDescription);
            }
        }
    }


    /**
     * Find or create the element for a source in the lineage facets.
     *
     * @param input source
     * @param jobProcess the event's job process (used when the source is the event's own job)
     * @param iscQualifiedName qualified name of the information supply chain for the relationships (may be null)
     * @param eventDescription description of the event for logging
     * @return catalogued element or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private CataloguedElement syncLineageInput(OpenLineageLineageInput input,
                                               CataloguedElement       jobProcess,
                                               String                  iscQualifiedName,
                                               String                  eventDescription) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        if (input == null)
        {
            return null;
        }

        if ("JOB".equalsIgnoreCase(input.getType()))
        {
            if (input.getName() == null)
            {
                return jobProcess;
            }

            return syncJob(input.getNamespace(), input.getName(), null, null, eventDescription);
        }

        return syncDataSet(input.getNamespace(), input.getName(), null, iscQualifiedName, eventDescription);
    }


    /**
     * Create LineageMapping relationships between columns from the field-level entries of the lineage facets.
     *
     * @param targetAsset asset that owns the target fields
     * @param fields map of target field name to its source inputs
     * @param iscQualifiedName qualified name of the information supply chain for the lineage (may be null)
     * @param eventDescription description of the event for logging
     */
    private void syncFieldLineage(CataloguedElement                        targetAsset,
                                  Map<String, OpenLineageLineageFieldEntry> fields,
                                  String                                    iscQualifiedName,
                                  String                                    eventDescription)
    {
        final String methodName = "syncFieldLineage";

        try
        {
            for (String targetFieldName : fields.keySet())
            {
                OpenLineageLineageFieldEntry fieldEntry = fields.get(targetFieldName);

                if ((fieldEntry == null) || (fieldEntry.getInputs() == null))
                {
                    continue;
                }

                CataloguedElement targetColumn = findColumn(targetAsset, targetFieldName);

                if (targetColumn == null)
                {
                    continue;
                }

                for (OpenLineageLineageInput input : fieldEntry.getInputs())
                {
                    if ((input != null) && ("DATASET".equalsIgnoreCase(input.getType())) && (input.getField() != null))
                    {
                        CataloguedElement sourceAsset  = syncDataSet(input.getNamespace(), input.getName(), null, iscQualifiedName, eventDescription);
                        CataloguedElement sourceColumn = findColumn(sourceAsset, input.getField());

                        if (sourceColumn != null)
                        {
                            ensureLineage(sourceColumn, targetColumn, OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, getLineageProperties(new DataMappingProperties(), input.getTransformations()), iscQualifiedName);
                            ensureTableMapping(sourceAsset, targetAsset, iscQualifiedName);
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "column lineage for " + targetAsset.qualifiedName(), eventDescription, error);
        }
    }


    /**
     * Create LineageMapping relationships between columns from the columnLineage facets of the output datasets.
     *
     * @param outputs output datasets from the event
     * @param outputAssets catalogued assets for the outputs (same order)
     * @param iscQualifiedName qualified name of the information supply chain for the lineage (may be null)
     * @param eventDescription description of the event for logging
     */
    private void syncColumnLineage(List<OpenLineageOutputDataSet> outputs,
                                   List<CataloguedElement>        outputAssets,
                                   String                         iscQualifiedName,
                                   String                         eventDescription)
    {
        final String methodName = "syncColumnLineage";

        if ((outputs == null) || (outputAssets == null))
        {
            return;
        }

        for (int i = 0; i < outputs.size() && i < outputAssets.size(); i++)
        {
            OpenLineageOutputDataSet output      = outputs.get(i);
            CataloguedElement        outputAsset = outputAssets.get(i);

            if ((output == null) || (outputAsset == null) || (output.getFacets() == null))
            {
                continue;
            }

            try
            {
                OpenLineageColumnLineageDataSetFacet columnLineage = output.getFacets().getColumnLineage();

                if ((columnLineage != null) && (columnLineage.getFields() != null))
                {
                    for (String outputFieldName : columnLineage.getFields().keySet())
                    {
                        OpenLineageColumnLineageDataSetFacetField outputField = columnLineage.getFields().get(outputFieldName);

                        if ((outputField == null) || (outputField.getInputFields() == null))
                        {
                            continue;
                        }

                        CataloguedElement targetColumn = findColumn(outputAsset, outputFieldName);

                        if (targetColumn == null)
                        {
                            continue;
                        }

                        for (OpenLineageColumnLineageDataSetFacetInputField inputField : outputField.getInputFields())
                        {
                            if ((inputField != null) && (inputField.getField() != null))
                            {
                                CataloguedElement sourceAsset  = syncDataSet(inputField.getNamespace(), inputField.getName(), null, iscQualifiedName, eventDescription);
                                CataloguedElement sourceColumn = findColumn(sourceAsset, inputField.getField());

                                if (sourceColumn != null)
                                {
                                    DataMappingProperties properties = new DataMappingProperties();

                                    if (inputField.getTransformations() != null)
                                    {
                                        StringBuilder label       = new StringBuilder();
                                        StringBuilder description = new StringBuilder();

                                        for (OpenLineageColumnLineageDataSetFacetTransformation transformation : inputField.getTransformations())
                                        {
                                            if (transformation != null)
                                            {
                                                appendTransformation(label, description, transformation.getType(), transformation.getSubtype(), transformation.getDescription(), transformation.getMasking());
                                            }
                                        }

                                        setLabelAndDescription(properties, label, description);
                                    }
                                    else
                                    {
                                        properties.setLabel(outputField.getTransformationType());
                                        properties.setDescription(outputField.getTransformationDescription());
                                    }

                                    ensureLineage(sourceColumn, targetColumn, OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, properties, iscQualifiedName);
                                    ensureTableMapping(sourceAsset, outputAsset, iscQualifiedName);
                                }
                            }
                        }
                    }
                }

                OpenLineageLineageDataSetFacet lineage = output.getFacets().getLineage();

                if (lineage != null)
                {
                    if (lineage.getInputs() != null)
                    {
                        for (OpenLineageLineageInput input : lineage.getInputs())
                        {
                            CataloguedElement source = syncLineageInput(input, null, iscQualifiedName, eventDescription);

                            if (source != null)
                            {
                                ensureLineage(source, outputAsset, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, getLineageProperties(new DataFlowProperties(), input.getTransformations()), iscQualifiedName);
                                ensureAssetLevelDataFlow(source, outputAsset, iscQualifiedName);
                                ensureTableMapping(source, outputAsset, iscQualifiedName);
                            }
                        }
                    }

                    if (lineage.getFields() != null)
                    {
                        syncFieldLineage(outputAsset, lineage.getFields(), iscQualifiedName, eventDescription);
                    }
                }
            }
            catch (Exception error)
            {
                logOptionalMetadataFailed(methodName, "column lineage for " + outputAsset.qualifiedName(), eventDescription, error);
            }
        }
    }


    /**
     * Build the label and description of a lineage relationship from the transformations of a lineage facet input.
     *
     * @param properties properties to fill
     * @param transformations transformations (may be null)
     * @param <T> type of properties
     * @return filled properties
     */
    private <T extends LineageRelationshipProperties> T getLineageProperties(T                                  properties,
                                                                             List<OpenLineageLineageTransformation> transformations)
    {
        if (transformations != null)
        {
            StringBuilder label       = new StringBuilder();
            StringBuilder description = new StringBuilder();

            for (OpenLineageLineageTransformation transformation : transformations)
            {
                if (transformation != null)
                {
                    appendTransformation(label, description, transformation.getType(), transformation.getSubtype(), transformation.getDescription(), transformation.getMasking());
                }
            }

            setLabelAndDescription(properties, label, description);
        }

        return properties;
    }


    /**
     * Append the details of a transformation to a label and description.
     *
     * @param label label builder
     * @param description description builder
     * @param type transformation type
     * @param subtype transformation subtype
     * @param transformationDescription transformation description
     * @param masking is the data masked
     */
    private void appendTransformation(StringBuilder label,
                                      StringBuilder description,
                                      String        type,
                                      String        subtype,
                                      String        transformationDescription,
                                      Boolean       masking)
    {
        if (type != null)
        {
            if (label.length() > 0)
            {
                label.append(", ");
            }

            label.append(type);

            if (subtype != null)
            {
                label.append(":").append(subtype);
            }

            if (Boolean.TRUE.equals(masking))
            {
                label.append(" (masked)");
            }
        }

        if (transformationDescription != null)
        {
            if (description.length() > 0)
            {
                description.append("; ");
            }

            description.append(transformationDescription);
        }
    }


    /**
     * Set the label and description on lineage properties if they have content.
     *
     * @param properties properties
     * @param label label
     * @param description description
     */
    private void setLabelAndDescription(LineageRelationshipProperties properties,
                                        StringBuilder                 label,
                                        StringBuilder                 description)
    {
        if (label.length() > 0)
        {
            properties.setLabel(label.toString());
        }

        if (description.length() > 0)
        {
            properties.setDescription(description.toString());
        }
    }


    /**
     * Find a column (schema attribute) of a data asset by name, by walking the asset's schema.  This works for
     * schemas created by this connector and for schemas created by other connectors.  Nested fields are named
     * with dots (for example customer.email) and are followed through the nested schema attributes.  When the
     * dataset is a table (a schema attribute, such as a RelationalTable), its columns are its nested attributes.
     *
     * @param asset catalogued asset or table
     * @param fieldName name of the field
     * @return catalogued column or null if not found
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private CataloguedElement findColumn(CataloguedElement asset,
                                         String            fieldName) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        if ((asset == null) || (fieldName == null))
        {
            return null;
        }

        SchemaTypeClient      schemaTypeClient      = myContext.getSchemaTypeClient();
        SchemaAttributeClient schemaAttributeClient = myContext.getSchemaAttributeClient();

        OpenMetadataRootElement schemaType = null;

        if (! asset.schemaAttribute())
        {
            schemaType = schemaTypeClient.getSchemaTypeForAsset(asset.guid(), schemaTypeClient.getGetOptions());

            if (schemaType == null)
            {
                return null;
            }
        }

        String[]                parts   = fieldName.split("\\.");
        OpenMetadataRootElement current = null;
        int                     pageSize = myContext.getMaxPageSize();

        for (int level = 0; level < parts.length; level++)
        {
            String                        partName  = parts[level];
            OpenMetadataRootElement       found     = null;
            int                           startFrom = 0;
            List<OpenMetadataRootElement> attributes;

            if ((level == 0) && (schemaType != null))
            {
                attributes = schemaAttributeClient.getAttributesForSchemaType(schemaType.getElementHeader().getGUID(), schemaAttributeClient.getQueryOptions(startFrom, pageSize));
            }
            else
            {
                attributes = schemaAttributeClient.getNestedSchemaAttributes((level == 0) ? asset.guid() : current.getElementHeader().getGUID(), schemaAttributeClient.getQueryOptions(startFrom, pageSize));
            }

            while ((attributes != null) && (! attributes.isEmpty()) && (found == null))
            {
                for (OpenMetadataRootElement attribute : attributes)
                {
                    if ((attribute != null) && (attribute.getProperties() instanceof org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties attributeProperties))
                    {
                        if ((partName.equals(attributeProperties.getDisplayName())) ||
                            (fieldName.equals(attributeProperties.getDisplayName())) ||
                            ((attributeProperties.getQualifiedName() != null) && (attributeProperties.getQualifiedName().endsWith(QUALIFIED_NAME_SEPARATOR + partName))))
                        {
                            found = attribute;
                            break;
                        }
                    }
                }

                if ((found != null) || (attributes.size() < pageSize))
                {
                    break;
                }

                startFrom  = startFrom + pageSize;

                if ((level == 0) && (schemaType != null))
                {
                    attributes = schemaAttributeClient.getAttributesForSchemaType(schemaType.getElementHeader().getGUID(), schemaAttributeClient.getQueryOptions(startFrom, pageSize));
                }
                else
                {
                    attributes = schemaAttributeClient.getNestedSchemaAttributes((level == 0) ? asset.guid() : current.getElementHeader().getGUID(), schemaAttributeClient.getQueryOptions(startFrom, pageSize));
                }
            }

            if (found == null)
            {
                return null;
            }

            current = found;

            /*
             * A schema created from a flattened field list may hold the whole dotted name as one column.
             */
            if ((current.getProperties() instanceof org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties currentProperties) &&
                (fieldName.equals(currentProperties.getDisplayName())))
            {
                break;
            }
        }

        if (current == null)
        {
            return null;
        }

        return new CataloguedElement(current.getElementHeader().getGUID(), getQualifiedName(current), current);
    }


    /**
     * Create the DataFlow between a dataset and a process (or the reverse), and between the asset that a table
     * belongs to and the process, so that the lineage is visible at the asset level as well as in detail.
     *
     * @param end1 source of the data
     * @param end2 destination of the data
     * @param iscQualifiedName qualified name of the information supply chain (may be null)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void ensureDataFlow(CataloguedElement end1,
                                CataloguedElement end2,
                                String            iscQualifiedName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        if ((end1 == null) || (end2 == null))
        {
            return;
        }

        ensureLineage(end1, end2, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, new DataFlowProperties(), iscQualifiedName);
        ensureAssetLevelDataFlow(end1, end2, iscQualifiedName);
    }


    /**
     * When either end of a data flow is a table, repeat the data flow between the assets the tables belong to.
     *
     * @param end1 source of the data
     * @param end2 destination of the data
     * @param iscQualifiedName qualified name of the information supply chain (may be null)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void ensureAssetLevelDataFlow(CataloguedElement end1,
                                          CataloguedElement end2,
                                          String            iscQualifiedName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        if ((end1 == null) || (end2 == null) || ((end1.parentAsset() == null) && (end2.parentAsset() == null)))
        {
            return;
        }

        CataloguedElement assetLevelEnd1 = (end1.parentAsset() == null) ? end1 : end1.parentAsset();
        CataloguedElement assetLevelEnd2 = (end2.parentAsset() == null) ? end2 : end2.parentAsset();

        ensureLineage(assetLevelEnd1, assetLevelEnd2, OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, new DataFlowProperties(), iscQualifiedName);
    }


    /**
     * When data flows from one table to another, link the tables with a DataMapping (the columns are linked with
     * DataMapping too, where the column lineage is known).
     *
     * @param source source table
     * @param target target table
     * @param iscQualifiedName qualified name of the information supply chain (may be null)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void ensureTableMapping(CataloguedElement source,
                                    CataloguedElement target,
                                    String            iscQualifiedName) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        if ((source != null) && (target != null) && (source.schemaAttribute()) && (target.schemaAttribute()))
        {
            ensureLineage(source, target, OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, new DataMappingProperties(), iscQualifiedName);
        }
    }


    /**
     * Retrieve the full element for a catalogued dataset - an asset, or a schema attribute such as a table.
     *
     * @param asset catalogued dataset
     * @return element or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private OpenMetadataRootElement retrieveElement(CataloguedElement asset) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        if (asset.schemaAttribute())
        {
            SchemaAttributeClient schemaAttributeClient = myContext.getSchemaAttributeClient();

            return schemaAttributeClient.getSchemaAttributeByGUID(asset.guid(), schemaAttributeClient.getGetOptions());
        }

        AssetClient assetClient = myContext.getAssetClient();

        return assetClient.getAssetByGUID(asset.guid(), assetClient.getGetOptions());
    }


    /**
     * Create a lineage relationship between two elements if it does not already exist.
     *
     * @param end1 element at end 1
     * @param end2 element at end 2
     * @param relationshipTypeName type of lineage relationship
     * @param properties properties for the relationship
     * @param iscQualifiedName qualified name of the information supply chain that the lineage belongs to (may be null)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void ensureLineage(CataloguedElement             end1,
                               CataloguedElement             end2,
                               String                        relationshipTypeName,
                               LineageRelationshipProperties properties,
                               String                        iscQualifiedName) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        if ((end1 == null) || (end2 == null) || (end1.guid().equals(end2.guid())))
        {
            return;
        }

        properties.setISCQualifiedName(iscQualifiedName);

        if (! isRelationshipKnown(end1.guid(), end2.guid(), relationshipTypeName, iscQualifiedName))
        {
            LineageClient lineageClient = myContext.getLineageClient();

            lineageClient.linkLineage(end1.guid(),
                                      end2.guid(),
                                      relationshipTypeName,
                                      new MakeAnchorOptions(lineageClient.getMetadataSourceOptions()),
                                      properties);

            rememberRelationship(end1.guid(), end2.guid(), relationshipTypeName, iscQualifiedName);

            logLineageCatalogued(relationshipTypeName, end1.qualifiedName(), end2.qualifiedName());
        }
    }


    /**
     * Determine whether a relationship already exists between two elements - first from the connector's cache and
     * then from the repository.
     *
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param relationshipTypeName type of relationship
     * @return boolean
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private boolean isRelationshipKnown(String end1GUID,
                                        String end2GUID,
                                        String relationshipTypeName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        return isRelationshipKnown(end1GUID, end2GUID, relationshipTypeName, null);
    }


    /**
     * Determine whether a relationship already exists between two elements - first from the connector's cache and
     * then from the repository.  When an information supply chain is supplied, only a relationship for that
     * information supply chain counts: the same data flow can be part of several information supply chains, and
     * each has its own relationship.
     *
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param relationshipTypeName type of relationship
     * @param iscQualifiedName qualified name of the information supply chain (may be null)
     * @return boolean
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private boolean isRelationshipKnown(String end1GUID,
                                        String end2GUID,
                                        String relationshipTypeName,
                                        String iscQualifiedName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "isRelationshipKnown";

        String key = getRelationshipKey(end1GUID, end2GUID, relationshipTypeName, iscQualifiedName);

        if (knownRelationships.contains(key))
        {
            return true;
        }

        OpenMetadataStore openMetadataStore = myContext.getOpenMetadataStore();

        if (OpenMetadataType.PEER_DUPLICATE_LINK.typeName.equals(relationshipTypeName))
        {
            /*
             * Peer duplicate links are only returned to queries made for duplicate processing.
             */
            QueryOptions queryOptions = openMetadataStore.getQueryOptions(0, myContext.getMaxPageSize());

            queryOptions.setForDuplicateProcessing(true);

            OpenMetadataRelationshipList peerLinks = openMetadataStore.findRelationshipsBetweenMetadataElements(relationshipTypeName,
                                                                                                                null,
                                                                                                                List.of(end1GUID),
                                                                                                                List.of(end2GUID),
                                                                                                                null,
                                                                                                                null,
                                                                                                                queryOptions);

            if ((peerLinks != null) && (peerLinks.getRelationships() != null) && (! peerLinks.getRelationships().isEmpty()))
            {
                knownRelationships.add(key);
                return true;
            }

            return false;
        }

        int                          pageSize      = myContext.getMaxPageSize();
        int                          startFrom     = 0;
        OpenMetadataRelationshipList relationships = openMetadataStore.getMetadataElementRelationships(end1GUID, end2GUID, relationshipTypeName, startFrom, pageSize);

        while ((relationships != null) && (relationships.getRelationships() != null) && (! relationships.getRelationships().isEmpty()))
        {
            for (OpenMetadataRelationship relationship : relationships.getRelationships())
            {
                /*
                 * The relationships between the two elements are returned whichever way round they are, so a data
                 * flow from the process to a schema is not mistaken for one from the schema to the process.
                 */
                if ((relationship != null) && (end1GUID.equals(relationship.getElementGUIDAtEnd1())) &&
                    ((iscQualifiedName == null) ||
                     (iscQualifiedName.equals(propertyHelper.getStringProperty(connectorName,
                                                                               OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                                               relationship.getRelationshipProperties(),
                                                                               methodName)))))
                {
                    knownRelationships.add(key);
                    return true;
                }
            }

            if (relationships.getRelationships().size() < pageSize)
            {
                break;
            }

            startFrom     = startFrom + pageSize;
            relationships = openMetadataStore.getMetadataElementRelationships(end1GUID, end2GUID, relationshipTypeName, startFrom, pageSize);
        }

        return false;
    }


    /**
     * Record that a relationship exists.
     *
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param relationshipTypeName type of relationship
     */
    private void rememberRelationship(String end1GUID,
                                      String end2GUID,
                                      String relationshipTypeName)
    {
        rememberRelationship(end1GUID, end2GUID, relationshipTypeName, null);
    }


    /**
     * Record that a relationship exists.
     *
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param relationshipTypeName type of relationship
     * @param iscQualifiedName qualified name of the information supply chain (may be null)
     */
    private void rememberRelationship(String end1GUID,
                                      String end2GUID,
                                      String relationshipTypeName,
                                      String iscQualifiedName)
    {
        knownRelationships.add(getRelationshipKey(end1GUID, end2GUID, relationshipTypeName, iscQualifiedName));
    }


    /**
     * Return the key used to cache a relationship.
     *
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param relationshipTypeName type of relationship
     * @param iscQualifiedName qualified name of the information supply chain (may be null)
     * @return key
     */
    private String getRelationshipKey(String end1GUID,
                                      String end2GUID,
                                      String relationshipTypeName,
                                      String iscQualifiedName)
    {
        String key = end1GUID + "|" + end2GUID + "|" + relationshipTypeName;

        if (iscQualifiedName != null)
        {
            key = key + "|" + iscQualifiedName;
        }

        return key;
    }


    /* =======================================================================================
     * Statistics, data quality and data scope
     */

    /**
     * Capture the statistics and data quality results carried in the event as annotations in a survey report.
     * The survey report is anchored to the run (or the job's process if runs are not catalogued), the run/process is
     * recorded as the originator of the report and each dataset with annotations is a subject of the report.
     *
     * @param event run event
     * @param jobProcess catalogued process for the job
     * @param runProcess catalogued run (may be null)
     * @param inputAssets catalogued input assets (same order as the event's inputs)
     * @param outputAssets catalogued output assets (same order as the event's outputs)
     * @param eventDescription description of the event for logging
     */
    private void captureRunObservations(OpenLineageRunEvent     event,
                                        CataloguedElement       jobProcess,
                                        CataloguedElement       runProcess,
                                        List<CataloguedElement> inputAssets,
                                        List<CataloguedElement> outputAssets,
                                        String                  eventDescription)
    {
        final String methodName = "captureRunObservations";

        try
        {
            Set<String> subjectGUIDs = new HashSet<>();

            /*
             * Work out what there is to record before creating the report so that events without statistics
             * do not generate empty reports.
             */
            List<AnnotationRequest> annotationRequests = new ArrayList<>();

            if (event.getInputs() != null)
            {
                for (int i = 0; i < event.getInputs().size() && i < inputAssets.size(); i++)
                {
                    OpenLineageInputDataSet input = event.getInputs().get(i);
                    CataloguedElement       asset = inputAssets.get(i);

                    if ((input == null) || (asset == null))
                    {
                        continue;
                    }

                    if (captureStatistics)
                    {
                        if (input.getFacets() != null)
                        {
                            addMetricsAnnotation(annotationRequests, asset, "OpenLineage Dataset Metrics", input.getFacets().getDataQualityMetrics(), null, event);
                        }

                        if (input.getInputFacets() != null)
                        {
                            addMetricsAnnotation(annotationRequests, asset, "OpenLineage Input Metrics", input.getInputFacets().getDataQualityMetrics(), input.getInputFacets().getInputStatistics(), event);
                        }
                    }

                    if ((captureDataQuality) && (input.getInputFacets() != null) && (input.getInputFacets().getDataQualityAssertions() != null) &&
                        (input.getInputFacets().getDataQualityAssertions().getAssertions() != null))
                    {
                        for (OpenLineageDataQualityAssertionsInputDataSetFacetAssertions assertion : input.getInputFacets().getDataQualityAssertions().getAssertions())
                        {
                            if (assertion != null)
                            {
                                annotationRequests.add(new AnnotationRequest(asset, getAssertionAnnotation(assertion, event)));
                            }
                        }
                    }
                }
            }

            if (event.getOutputs() != null)
            {
                for (int i = 0; i < event.getOutputs().size() && i < outputAssets.size(); i++)
                {
                    OpenLineageOutputDataSet output = event.getOutputs().get(i);
                    CataloguedElement        asset  = outputAssets.get(i);

                    if ((output == null) || (asset == null))
                    {
                        continue;
                    }

                    if (captureStatistics)
                    {
                        if (output.getFacets() != null)
                        {
                            addMetricsAnnotation(annotationRequests, asset, "OpenLineage Dataset Metrics", output.getFacets().getDataQualityMetrics(), null, event);
                        }

                        if ((output.getOutputFacets() != null) && (output.getOutputFacets().getOutputStatistics() != null))
                        {
                            OpenLineageOutputStatisticsOutputDataSetFacet outputStatistics = output.getOutputFacets().getOutputStatistics();

                            ResourceProfileAnnotationProperties properties = getResourceProfileAnnotation("OpenLineage Output Statistics", event);
                            Map<String, Long> profileCounts = new HashMap<>();

                            putIfNotNull(profileCounts, "rowCount", outputStatistics.getRowCount());
                            putIfNotNull(profileCounts, "size", outputStatistics.getSize());
                            putIfNotNull(profileCounts, "fileCount", outputStatistics.getFileCount());

                            properties.setProfileCounts(profileCounts);
                            properties.setProfilePropertyNames(new ArrayList<>(profileCounts.keySet()));
                            properties.setSummary("Data written to " + output.getName() + " by run " + getRunId(event));

                            annotationRequests.add(new AnnotationRequest(asset, properties));
                        }
                    }
                }
            }

            if ((captureDataQuality) && (event.getRun() != null) && (event.getRun().getFacets() != null) && (event.getRun().getFacets().getTest() != null) &&
                (event.getRun().getFacets().getTest().getTests() != null))
            {
                for (OpenLineageTestRunFacetTestExecution test : event.getRun().getFacets().getTest().getTests())
                {
                    if (test != null)
                    {
                        annotationRequests.add(new AnnotationRequest((runProcess != null) ? runProcess : jobProcess, getTestAnnotation(test, event)));
                    }
                }
            }

            if (annotationRequests.isEmpty())
            {
                return;
            }

            /*
             * Create the survey report.
             */
            AssetClient reportClient = myContext.getAssetClient(OpenMetadataType.SURVEY_REPORT.typeName);
            CataloguedElement reportAnchor = (runProcess != null) ? runProcess : jobProcess;

            SurveyReportProperties reportProperties = new SurveyReportProperties();

            reportProperties.setTypeName(OpenMetadataType.SURVEY_REPORT.typeName);
            reportProperties.setQualifiedName(REPORT_QUALIFIED_NAME_PREFIX + getRunQualifiedNameSuffix(event) + QUALIFIED_NAME_SEPARATOR + event.getEventType() + QUALIFIED_NAME_SEPARATOR + event.getEventTime());
            reportProperties.setDisplayName("OpenLineage " + event.getEventType() + " event for run " + getRunId(event) + " of " + event.getJob().getName());
            reportProperties.setDescription("Statistics and data quality results reported in an OpenLineage event by " + event.getProducer());
            reportProperties.setPurpose("Record the statistics and data quality results observed by a run of a data pipeline.");
            reportProperties.setAnalysisStep(event.getEventType());
            reportProperties.setCompletionTime(parseTimestamp(event.getEventTime()));

            Map<String, String> analysisParameters = new HashMap<>();

            putIfNotNull(analysisParameters, "runId", getRunId(event));
            putIfNotNull(analysisParameters, "eventType", event.getEventType());
            putIfNotNull(analysisParameters, "producer", event.getProducer());
            reportProperties.setAnalysisParameters(analysisParameters);

            if ((event.getRun() != null) && (event.getRun().getFacets() != null) && (event.getRun().getFacets().getNominalTime() != null))
            {
                reportProperties.setStartTime(parseTimestamp(event.getRun().getFacets().getNominalTime().getNominalStartTime()));
            }

            NewElementOptions newElementOptions = new NewElementOptions(reportClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(reportAnchor.guid());
            newElementOptions.setIsOwnAnchor(false);

            String reportGUID = reportClient.createAsset(newElementOptions, null, reportProperties, null);

            reportClient.linkReportOriginator(reportAnchor.guid(), reportGUID, new MakeAnchorOptions(reportClient.getMetadataSourceOptions()), null);

            AnnotationClient annotationClient = myContext.getAnnotationClient();
            int              annotationCount  = 0;

            for (AnnotationRequest annotationRequest : annotationRequests)
            {
                annotationCount++;

                /*
                 * Every annotation needs a unique name of its own.
                 */
                annotationRequest.properties().setQualifiedName(reportProperties.getQualifiedName() + QUALIFIED_NAME_SEPARATOR +
                                                                        annotationRequest.properties().getAnnotationType() + QUALIFIED_NAME_SEPARATOR + annotationCount);

                NewElementOptions annotationOptions = new NewElementOptions(annotationClient.getMetadataSourceOptions());

                annotationOptions.setAnchorGUID(reportGUID);
                annotationOptions.setIsOwnAnchor(false);
                annotationOptions.setParentGUID(reportGUID);
                annotationOptions.setParentRelationshipTypeName(OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName);
                annotationOptions.setParentAtEnd1(true);

                String annotationGUID = annotationClient.createAnnotation(annotationOptions, null, annotationRequest.properties(), null);

                if (annotationRequest.describedElement() != null)
                {
                    annotationClient.linkAnnotationToDescribedElement(annotationRequest.describedElement().guid(),
                                                                      annotationGUID,
                                                                      new MakeAnchorOptions(annotationClient.getMetadataSourceOptions()),
                                                                      null);

                    if ((! annotationRequest.describedElement().guid().equals(reportAnchor.guid())) && (subjectGUIDs.add(annotationRequest.describedElement().guid())))
                    {
                        reportClient.linkReportSubject(annotationRequest.describedElement().guid(), reportGUID, new MakeAnchorOptions(reportClient.getMetadataSourceOptions()), null);
                    }
                }
            }

            logElementCatalogued(OpenMetadataType.SURVEY_REPORT.typeName, reportProperties.getQualifiedName(), reportGUID, "run", getRunId(event));
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "statistics and data quality results", eventDescription, error);
        }
    }


    /**
     * An annotation to create along with the element it describes.
     *
     * @param describedElement element the annotation describes (null if it describes the report's anchor)
     * @param properties annotation properties
     */
    private record AnnotationRequest(CataloguedElement    describedElement,
                                     AnnotationProperties properties)
    {
    }


    /**
     * Add a resource profile annotation for the data quality metrics and/or input statistics of a dataset, if there are any.
     *
     * @param annotationRequests list to add to
     * @param asset catalogued asset
     * @param annotationType type of annotation
     * @param metrics data quality metrics (may be null)
     * @param inputStatistics input statistics (may be null)
     * @param event run event
     */
    private void addMetricsAnnotation(List<AnnotationRequest>                     annotationRequests,
                                      CataloguedElement                           asset,
                                      String                                      annotationType,
                                      OpenLineageDataQualityMetricsDataSetFacet   metrics,
                                      OpenLineageInputStatisticsInputDataSetFacet inputStatistics,
                                      OpenLineageRunEvent                         event)
    {
        if ((metrics == null) && (inputStatistics == null))
        {
            return;
        }

        ResourceProfileAnnotationProperties properties    = getResourceProfileAnnotation(annotationType, event);
        Map<String, Long>                   profileCounts = new HashMap<>();
        Map<String, Double>                 profileDoubles = new HashMap<>();
        Map<String, Date>                   profileDates  = new HashMap<>();

        if (inputStatistics != null)
        {
            putIfNotNull(profileCounts, "rowCount", inputStatistics.getRowCount());
            putIfNotNull(profileCounts, "size", inputStatistics.getSize());
            putIfNotNull(profileCounts, "fileCount", inputStatistics.getFileCount());
        }

        if (metrics != null)
        {
            putIfNotNull(profileCounts, "rowCount", metrics.getRowCount());
            putIfNotNull(profileCounts, "bytes", metrics.getBytes());
            putIfNotNull(profileCounts, "fileCount", metrics.getFileCount());

            Date lastUpdated = parseTimestamp(metrics.getLastUpdated());

            if (lastUpdated != null)
            {
                profileDates.put("lastUpdated", lastUpdated);
            }

            if (metrics.getColumnMetrics() != null)
            {
                for (String columnName : metrics.getColumnMetrics().keySet())
                {
                    OpenLineageDataQualityMetricsColumnMetrics columnMetrics = metrics.getColumnMetrics().get(columnName);

                    if (columnMetrics != null)
                    {
                        putIfNotNull(profileCounts, columnName + ".nullCount", columnMetrics.getNullCount());
                        putIfNotNull(profileCounts, columnName + ".distinctCount", columnMetrics.getDistinctCount());
                        putIfNotNull(profileDoubles, columnName + ".sum", columnMetrics.getSum());
                        putIfNotNull(profileDoubles, columnName + ".count", columnMetrics.getCount());
                        putIfNotNull(profileDoubles, columnName + ".min", columnMetrics.getMin());
                        putIfNotNull(profileDoubles, columnName + ".max", columnMetrics.getMax());

                        if (columnMetrics.getQuantiles() != null)
                        {
                            for (String quantile : columnMetrics.getQuantiles().keySet())
                            {
                                putIfNotNull(profileDoubles, columnName + ".quantile." + quantile, columnMetrics.getQuantiles().get(quantile));
                            }
                        }
                    }
                }
            }
        }

        if ((profileCounts.isEmpty()) && (profileDoubles.isEmpty()) && (profileDates.isEmpty()))
        {
            return;
        }

        List<String> propertyNames = new ArrayList<>(profileCounts.keySet());
        propertyNames.addAll(profileDoubles.keySet());
        propertyNames.addAll(profileDates.keySet());

        properties.setProfilePropertyNames(propertyNames);

        if (! profileCounts.isEmpty())
        {
            properties.setProfileCounts(profileCounts);
        }
        if (! profileDoubles.isEmpty())
        {
            properties.setProfileDoubles(profileDoubles);
        }
        if (! profileDates.isEmpty())
        {
            properties.setProfileDates(profileDates);
        }

        properties.setSummary(annotationType + " for " + asset.qualifiedName() + " from run " + getRunId(event));

        annotationRequests.add(new AnnotationRequest(asset, properties));
    }


    /**
     * Add a resource profile annotation for the input-level data quality metrics and input statistics of a dataset.
     * The input-level metrics facet has the same content as the dataset-level one so it is converted and delegated.
     *
     * @param annotationRequests list to add to
     * @param asset catalogued asset
     * @param annotationType type of annotation
     * @param inputMetrics data quality metrics from the input facets (may be null)
     * @param inputStatistics input statistics (may be null)
     * @param event run event
     */
    private void addMetricsAnnotation(List<AnnotationRequest>                        annotationRequests,
                                      CataloguedElement                              asset,
                                      String                                         annotationType,
                                      OpenLineageDataQualityMetricsInputDataSetFacet inputMetrics,
                                      OpenLineageInputStatisticsInputDataSetFacet    inputStatistics,
                                      OpenLineageRunEvent                            event)
    {
        OpenLineageDataQualityMetricsDataSetFacet metrics = null;

        if (inputMetrics != null)
        {
            metrics = new OpenLineageDataQualityMetricsDataSetFacet();

            metrics.setRowCount(inputMetrics.getRowCount());
            metrics.setBytes(inputMetrics.getBytes());
            metrics.setFileCount(inputMetrics.getFileCount());
            metrics.setLastUpdated(inputMetrics.getLastUpdated());
            metrics.setColumnMetrics(inputMetrics.getColumnMetrics());
        }

        addMetricsAnnotation(annotationRequests, asset, annotationType, metrics, inputStatistics, event);
    }


    /**
     * Create the common properties for a resource profile annotation.
     *
     * @param annotationType type of annotation
     * @param event run event
     * @return properties
     */
    private ResourceProfileAnnotationProperties getResourceProfileAnnotation(String              annotationType,
                                                                             OpenLineageRunEvent event)
    {
        ResourceProfileAnnotationProperties properties = new ResourceProfileAnnotationProperties();

        properties.setTypeName(OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName);
        properties.setAnnotationType(annotationType);
        properties.setAnalysisStep(event.getEventType());
        properties.setExplanation("Reported by " + event.getProducer() + " in an OpenLineage " + event.getEventType() + " event at " + event.getEventTime());

        return properties;
    }


    /**
     * Create a quality annotation for a data quality assertion.
     *
     * @param assertion assertion
     * @param event run event
     * @return properties
     */
    private QualityAnnotationProperties getAssertionAnnotation(OpenLineageDataQualityAssertionsInputDataSetFacetAssertions assertion,
                                                               OpenLineageRunEvent                                          event)
    {
        QualityAnnotationProperties properties = new QualityAnnotationProperties();

        properties.setTypeName(OpenMetadataType.QUALITY_ANNOTATION.typeName);
        properties.setAnnotationType("OpenLineage Data Quality Assertion");
        properties.setAnalysisStep(event.getEventType());
        properties.setQualityDimension(assertion.getAssertion());
        properties.setQualityScore(assertion.isSuccess() ? 100 : 0);

        StringBuilder summary = new StringBuilder();

        if (assertion.getName() != null)
        {
            summary.append(assertion.getName());
        }
        else if (assertion.getAssertion() != null)
        {
            summary.append(assertion.getAssertion());
        }

        if (assertion.getColumn() != null)
        {
            summary.append(" on column ").append(assertion.getColumn());
        }

        summary.append(assertion.isSuccess() ? " passed" : " failed");

        properties.setSummary(summary.toString());
        properties.setQualityDescription(getQualityDescription(assertion.getDescription(), assertion.getExpected(), assertion.getActual(), assertion.getSeverity()));
        properties.setExpression(assertion.getContent());
        properties.setExplanation("Reported by " + event.getProducer() + " in an OpenLineage " + event.getEventType() + " event at " + event.getEventTime());
        properties.setJsonProperties(getJSON(assertion.getParams()));

        return properties;
    }


    /**
     * Create a quality annotation for a test execution from the test run facet.
     *
     * @param test test execution
     * @param event run event
     * @return properties
     */
    private QualityAnnotationProperties getTestAnnotation(OpenLineageTestRunFacetTestExecution test,
                                                          OpenLineageRunEvent                  event)
    {
        QualityAnnotationProperties properties = new QualityAnnotationProperties();

        properties.setTypeName(OpenMetadataType.QUALITY_ANNOTATION.typeName);
        properties.setAnnotationType("OpenLineage Test");
        properties.setAnalysisStep(event.getEventType());
        properties.setQualityDimension((test.getType() != null) ? test.getType() : test.getName());

        if ("pass".equalsIgnoreCase(test.getStatus()))
        {
            properties.setQualityScore(100);
        }
        else if ("fail".equalsIgnoreCase(test.getStatus()))
        {
            properties.setQualityScore(0);
        }

        properties.setSummary(test.getName() + " " + test.getStatus());
        properties.setQualityDescription(getQualityDescription(test.getDescription(), test.getExpected(), test.getActual(), test.getSeverity()));
        properties.setExpression(test.getContent());
        properties.setExplanation("Reported by " + event.getProducer() + " in an OpenLineage " + event.getEventType() + " event at " + event.getEventTime());
        properties.setJsonProperties(getJSON(test.getParams()));

        return properties;
    }


    /**
     * Build a description for a quality annotation.
     *
     * @param description description of the check
     * @param expected expected value
     * @param actual actual value
     * @param severity severity
     * @return description or null if there is nothing to say
     */
    private String getQualityDescription(String description,
                                         String expected,
                                         String actual,
                                         String severity)
    {
        StringBuilder result = new StringBuilder();

        if (description != null)
        {
            result.append(description);
        }
        if (expected != null)
        {
            result.append(" [expected: ").append(expected).append("]");
        }
        if (actual != null)
        {
            result.append(" [actual: ").append(actual).append("]");
        }
        if (severity != null)
        {
            result.append(" [severity: ").append(severity).append("]");
        }

        if (result.length() == 0)
        {
            return null;
        }

        return result.toString().trim();
    }


    /**
     * Maintain the DataScope classification on the datasets read and written by the run.  The classification
     * describes the data held in the store: dataCollectionStartTime is when the first record was stored and
     * dataCollectionEndTime is the last known write.  For outputs, the window is extended (or restarted when the
     * store is created, overwritten or truncated) from the time of the write, and the latest write statistics are
     * held in the additional properties.  For inputs, only the read statistics are recorded.
     *
     * @param event run event
     * @param jobProcess catalogued process for the job
     * @param inputAssets catalogued input assets
     * @param outputAssets catalogued output assets
     * @param eventDescription description of the event for logging
     */
    private void updateDataScope(OpenLineageRunEvent     event,
                                 CataloguedElement       jobProcess,
                                 List<CataloguedElement> inputAssets,
                                 List<CataloguedElement> outputAssets,
                                 String                  eventDescription)
    {
        final String methodName = "updateDataScope";

        boolean runComplete = "COMPLETE".equals(event.getEventType());

        try
        {
            if (event.getOutputs() != null)
            {
                for (int i = 0; i < event.getOutputs().size() && i < outputAssets.size(); i++)
                {
                    OpenLineageOutputDataSet output = event.getOutputs().get(i);
                    CataloguedElement        asset  = outputAssets.get(i);

                    if ((output == null) || (asset == null))
                    {
                        continue;
                    }

                    OpenLineageOutputStatisticsOutputDataSetFacet outputStatistics = (output.getOutputFacets() != null) ? output.getOutputFacets().getOutputStatistics() : null;

                    /*
                     * A write is recorded when the run completes, or when a streaming/RUNNING event reports output statistics.
                     */
                    if ((! runComplete) && (outputStatistics == null))
                    {
                        continue;
                    }

                    DataScopeProperties dataScope = getExistingDataScope(asset);
                    Map<String, String> additionalProperties = new HashMap<>();

                    if (dataScope.getAdditionalProperties() != null)
                    {
                        additionalProperties.putAll(dataScope.getAdditionalProperties());
                    }

                    /*
                     * The data collection window is the span of the data held in the store: it starts when the first
                     * record was stored and ends with the last known write.  The write time is the event time of the
                     * event that reports the write, or the dataset's lastUpdated metric if the producer supplies it.
                     * A CREATE, OVERWRITE or TRUNCATE means the store now holds only this write's records, so the
                     * window restarts.
                     */
                    Date writeTime = parseTimestamp(event.getEventTime());

                    if ((output.getFacets() != null) && (output.getFacets().getDataQualityMetrics() != null))
                    {
                        Date lastUpdated = parseTimestamp(output.getFacets().getDataQualityMetrics().getLastUpdated());

                        if ((lastUpdated != null) && ((writeTime == null) || (lastUpdated.after(writeTime))))
                        {
                            writeTime = lastUpdated;
                        }
                    }

                    String lifecycleStateChange = null;

                    if ((output.getFacets() != null) && (output.getFacets().getLifecycleStateChange() != null))
                    {
                        lifecycleStateChange = output.getFacets().getLifecycleStateChange().getLifecycleStateChange();
                    }

                    boolean storeReplaced = ("CREATE".equalsIgnoreCase(lifecycleStateChange)) ||
                                            ("OVERWRITE".equalsIgnoreCase(lifecycleStateChange)) ||
                                            ("TRUNCATE".equalsIgnoreCase(lifecycleStateChange));

                    if (writeTime != null)
                    {
                        if ((storeReplaced) || (dataScope.getDataCollectionStartTime() == null) || (writeTime.before(dataScope.getDataCollectionStartTime())))
                        {
                            dataScope.setDataCollectionStartTime(writeTime);
                        }

                        if ((storeReplaced) || (dataScope.getDataCollectionEndTime() == null) || (writeTime.after(dataScope.getDataCollectionEndTime())))
                        {
                            dataScope.setDataCollectionEndTime(writeTime);
                        }
                    }

                    if (runComplete)
                    {
                        incrementCounter(additionalProperties, WRITE_COUNT_PROPERTY);
                    }

                    putIfNotNull(additionalProperties, LAST_WRITTEN_BY_PROPERTY, getRunId(event));
                    putIfNotNull(additionalProperties, LAST_WRITTEN_AT_PROPERTY, event.getEventTime());

                    if (outputStatistics != null)
                    {
                        putIfNotNull(additionalProperties, LAST_ROW_COUNT_PROPERTY, outputStatistics.getRowCount());
                        putIfNotNull(additionalProperties, LAST_SIZE_PROPERTY, outputStatistics.getSize());
                        putIfNotNull(additionalProperties, LAST_FILE_COUNT_PROPERTY, outputStatistics.getFileCount());
                    }

                    if ((output.getOutputFacets() != null) && (output.getOutputFacets().getSubset() != null))
                    {
                        putIfNotNull(additionalProperties, LAST_PARTITION_PROPERTY, getJSON(output.getOutputFacets().getSubset().getOutputCondition()));
                    }

                    if (output.getFacets() != null)
                    {
                        if (output.getFacets().getLifecycleStateChange() != null)
                        {
                            putIfNotNull(additionalProperties, LAST_LIFECYCLE_CHANGE_PROPERTY, output.getFacets().getLifecycleStateChange().getLifecycleStateChange());
                        }
                        if (output.getFacets().getVersion() != null)
                        {
                            putIfNotNull(additionalProperties, DATASET_VERSION_PROPERTY, output.getFacets().getVersion().getDatasetVersion());
                        }
                    }

                    dataScope.setAdditionalProperties(additionalProperties);

                    saveDataScope(asset, dataScope);
                }
            }

            if ((runComplete) && (event.getInputs() != null))
            {
                for (int i = 0; i < event.getInputs().size() && i < inputAssets.size(); i++)
                {
                    OpenLineageInputDataSet input = event.getInputs().get(i);
                    CataloguedElement       asset = inputAssets.get(i);

                    if ((input == null) || (asset == null))
                    {
                        continue;
                    }

                    DataScopeProperties dataScope = getExistingDataScope(asset);
                    Map<String, String> additionalProperties = new HashMap<>();

                    if (dataScope.getAdditionalProperties() != null)
                    {
                        additionalProperties.putAll(dataScope.getAdditionalProperties());
                    }

                    incrementCounter(additionalProperties, READ_COUNT_PROPERTY);
                    putIfNotNull(additionalProperties, LAST_READ_BY_PROPERTY, getRunId(event));
                    putIfNotNull(additionalProperties, LAST_READ_AT_PROPERTY, event.getEventTime());

                    dataScope.setAdditionalProperties(additionalProperties);

                    saveDataScope(asset, dataScope);
                }
            }
        }
        catch (Exception error)
        {
            logOptionalMetadataFailed(methodName, "data scope", eventDescription, error);
        }
    }


    /**
     * Retrieve the current DataScope classification of an asset (a new empty one if it is not classified).
     *
     * @param asset catalogued asset
     * @return data scope properties
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private DataScopeProperties getExistingDataScope(CataloguedElement asset) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        AssetClient assetClient = myContext.getAssetClient();

        OpenMetadataRootElement element = retrieveElement(asset);

        if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getDataScope() != null))
        {
            ElementClassification classification = element.getElementHeader().getDataScope();

            if (classification.getClassificationProperties() instanceof DataScopeProperties existingProperties)
            {
                return new DataScopeProperties(existingProperties);
            }
        }

        return new DataScopeProperties();
    }


    /**
     * Save the DataScope classification on an asset - adding it if the asset was not classified.
     *
     * @param asset catalogued asset
     * @param dataScope properties
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void saveDataScope(CataloguedElement   asset,
                               DataScopeProperties dataScope) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        AssetClient                  assetClient          = myContext.getAssetClient();
        ClassificationExplorerClient classificationClient = myContext.getClassificationExplorerClient();

        OpenMetadataRootElement element = retrieveElement(asset);

        if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getDataScope() != null))
        {
            classificationClient.updateDataScopeClassification(asset.guid(), dataScope, classificationClient.getUpdateOptions(false));
        }
        else
        {
            classificationClient.addDataScopeClassification(asset.guid(), dataScope, classificationClient.getMetadataSourceOptions());
        }
    }


    /**
     * Add an Ownership classification to an element from the ownership facet of a job or dataset (first owner only).
     * The OpenLineage owner name is matched to an actor profile in open metadata (by user identity, then by name);
     * if one is found the classification identifies it, otherwise the classification carries the OpenLineage name.
     * The OpenLineage owner name and type are always recorded in the classification's additional properties.
     *
     * @param elementGUID element to classify
     * @param jobOwnership ownership facet from a job (may be null)
     * @param dataSetOwnership ownership facet from a dataset (may be null)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private void addOwnership(String                          elementGUID,
                              OpenLineageOwnershipJobFacet     jobOwnership,
                              OpenLineageOwnershipDataSetFacet dataSetOwnership) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        String ownerName = null;
        String ownerType = null;

        if ((jobOwnership != null) && (jobOwnership.getOwners() != null))
        {
            for (OpenLineageOwnershipJobFacetOwner owner : jobOwnership.getOwners())
            {
                if ((owner != null) && (owner.getName() != null))
                {
                    ownerName = owner.getName();
                    ownerType = owner.getType();
                    break;
                }
            }
        }
        else if ((dataSetOwnership != null) && (dataSetOwnership.getOwners() != null))
        {
            for (OpenLineageOwnershipDataSetFacetOwner owner : dataSetOwnership.getOwners())
            {
                if ((owner != null) && (owner.getName() != null))
                {
                    ownerName = owner.getName();
                    ownerType = owner.getType();
                    break;
                }
            }
        }

        if (ownerName != null)
        {
            ClassificationExplorerClient classificationClient = myContext.getClassificationExplorerClient();

            OwnershipProperties ownershipProperties = new OwnershipProperties();
            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put(OPEN_LINEAGE_OWNER_NAME_PROPERTY, ownerName);
            putIfNotNull(additionalProperties, OPEN_LINEAGE_OWNER_TYPE_PROPERTY, ownerType);

            OpenMetadataRootElement actorProfile = findActorProfile(ownerName);

            if ((actorProfile != null) && (actorProfile.getProperties() instanceof org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties profileProperties))
            {
                ownershipProperties.setOwner(profileProperties.getQualifiedName());
                ownershipProperties.setOwnerTypeName(actorProfile.getElementHeader().getType().getTypeName());
                ownershipProperties.setOwnerPropertyName(OpenMetadataProperty.QUALIFIED_NAME.name);
            }
            else
            {
                ownershipProperties.setOwner(ownerName);
            }

            ownershipProperties.setAdditionalProperties(additionalProperties);

            classificationClient.addOwnership(elementGUID, ownershipProperties, classificationClient.getMetadataSourceOptions());
        }
    }


    /**
     * Try to match an OpenLineage owner name to an actor profile.  OpenLineage recommends owner names as URNs such as
     * user:jdoe, team:data or application:foo, so the name is tried both as given and with the prefix removed:
     * first as a user identity (which leads to its profile), then as the qualified name, identifier or display name
     * of a profile.  A match is only accepted when it is unambiguous.
     *
     * @param ownerName owner name from the ownership facet
     * @return actor profile or null
     * @throws PropertyServerException repository problem
     * @throws UserNotAuthorizedException security problem
     */
    private OpenMetadataRootElement findActorProfile(String ownerName) throws PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        ActorProfileClient actorProfileClient = myContext.getActorProfileClient();

        List<String> candidates = new ArrayList<>();

        candidates.add(ownerName);

        int separator = ownerName.indexOf(':');

        if ((separator > 0) && (separator < ownerName.length() - 1))
        {
            candidates.add(ownerName.substring(separator + 1));
        }

        for (String candidate : candidates)
        {
            try
            {
                OpenMetadataRootElement profile = actorProfileClient.getActorProfileByUserId(candidate, actorProfileClient.getGetOptions());

                if (profile != null)
                {
                    return profile;
                }
            }
            catch (InvalidParameterException notFound)
            {
                /*
                 * No user identity with this userId.
                 */
            }

            try
            {
                List<OpenMetadataRootElement> profiles = actorProfileClient.getActorProfilesByName(candidate, actorProfileClient.getQueryOptions());

                if ((profiles != null) && (profiles.size() == 1) && (profiles.get(0) != null))
                {
                    return profiles.get(0);
                }
            }
            catch (InvalidParameterException notFound)
            {
                /*
                 * No profile with this name.
                 */
            }
        }

        return null;
    }


    /* =======================================================================================
     * Naming and conversion helpers
     */

    /**
     * Build the qualified name for a job's process using the standard convention {typeName}::{namespace}::{name}.
     *
     * @param namespace job namespace
     * @param name job name
     * @return qualified name
     */
    private String getJobQualifiedName(String namespace,
                                       String name)
    {
        return OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName + QUALIFIED_NAME_SEPARATOR + ((namespace == null) ? UNKNOWN_NAMESPACE : namespace) + QUALIFIED_NAME_SEPARATOR + name;
    }


    /**
     * Build the qualified name for a run.
     *
     * @param job job
     * @param runId run identifier
     * @return qualified name
     */
    private String getRunQualifiedName(OpenLineageJob job,
                                       String         runId)
    {
        return RUN_QUALIFIED_NAME_PREFIX + ((job.getNamespace() == null) ? UNKNOWN_NAMESPACE : job.getNamespace()) + QUALIFIED_NAME_SEPARATOR + job.getName() + QUALIFIED_NAME_SEPARATOR + runId;
    }


    /**
     * Build the namespace/job/run part of a qualified name.
     *
     * @param event run event
     * @return string
     */
    private String getRunQualifiedNameSuffix(OpenLineageRunEvent event)
    {
        return ((event.getJob().getNamespace() == null) ? UNKNOWN_NAMESPACE : event.getJob().getNamespace()) + QUALIFIED_NAME_SEPARATOR + event.getJob().getName() + QUALIFIED_NAME_SEPARATOR + getRunId(event);
    }


    /**
     * Build the qualified name for a dataset's asset using the standard convention {typeName}::{namespace}::{resourceName}.
     *
     * @param typeName open metadata type name of the asset
     * @param namespace dataset namespace
     * @param name dataset name
     * @return qualified name
     */
    private String getDataSetQualifiedName(String typeName,
                                           String namespace,
                                           String name)
    {
        return typeName + QUALIFIED_NAME_SEPARATOR + ((namespace == null) ? UNKNOWN_NAMESPACE : namespace) + QUALIFIED_NAME_SEPARATOR + name;
    }


    /**
     * Extract the URI scheme from a dataset namespace as it is written (in lower case), before alternative
     * spellings are reduced - for example sqlserver (used for Azure Synapse) rather than mssql.
     *
     * @param namespace namespace
     * @return scheme in lower case or null
     */
    private String getRawNamespaceScheme(String namespace)
    {
        if (namespace == null)
        {
            return null;
        }

        int separator = namespace.indexOf("://");

        if (separator <= 0)
        {
            separator = namespace.indexOf(':');
        }

        return ((separator > 0) ? namespace.substring(0, separator) : namespace).toLowerCase();
    }


    /**
     * Extract the URI scheme (technology) from a dataset namespace, for example "postgres" from "postgres://host:5432".
     * Namespaces without a scheme separator (for example "bigquery" or "file") are returned as is.  Alternative
     * spellings are reduced to the one in the OpenLineage naming conventions (for example postgresql to postgres).
     *
     * @param namespace namespace
     * @return canonical scheme in lower case or null
     */
    private String getNamespaceScheme(String namespace)
    {
        if (namespace == null)
        {
            return null;
        }

        int separator = namespace.indexOf("://");

        if (separator > 0)
        {
            return OpenLineageNamespace.getCanonicalScheme(namespace.substring(0, separator));
        }

        separator = namespace.indexOf(':');

        if (separator > 0)
        {
            return OpenLineageNamespace.getCanonicalScheme(namespace.substring(0, separator));
        }

        return OpenLineageNamespace.getCanonicalScheme(namespace);
    }


    /**
     * Return the run identifier of an event as a string.
     *
     * @param event run event
     * @return string or null
     */
    private String getRunId(OpenLineageRunEvent event)
    {
        if ((event.getRun() != null) && (event.getRun().getRunId() != null))
        {
            return event.getRun().getRunId().toString();
        }

        return null;
    }


    /**
     * Build a short description of the event for log messages.
     *
     * @param event run event
     * @return description
     */
    private String getEventDescription(OpenLineageRunEvent event)
    {
        StringBuilder description = new StringBuilder();

        description.append(event.getEventType()).append(" event");

        if (event.getJob() != null)
        {
            description.append(" for job ").append(event.getJob().getNamespace()).append("/").append(event.getJob().getName());
        }

        if (getRunId(event) != null)
        {
            description.append(" run ").append(getRunId(event));
        }

        description.append(" at ").append(event.getEventTime());

        return description.toString();
    }


    /**
     * Map the OpenLineage event type to an activity status.
     *
     * @param eventType event type (START, RUNNING, COMPLETE, ABORT, FAIL, OTHER)
     * @return activity status
     */
    private ActivityStatus getActivityStatus(String eventType)
    {
        if (eventType == null)
        {
            return ActivityStatus.OTHER;
        }

        return switch (eventType)
        {
            case "START", "RUNNING" -> ActivityStatus.IN_PROGRESS;
            case "COMPLETE" -> ActivityStatus.COMPLETED;
            case "FAIL" -> ActivityStatus.FAILED;
            case "ABORT" -> ActivityStatus.ABANDONED;
            default -> ActivityStatus.OTHER;
        };
    }


    /**
     * Parse an ISO-8601 timestamp from an OpenLineage event.  Timestamps without a zone are treated as UTC.
     *
     * @param timestamp string timestamp
     * @return date or null if the timestamp is missing or unparsable
     */
    private Date parseTimestamp(String timestamp)
    {
        if ((timestamp == null) || (timestamp.isBlank()))
        {
            return null;
        }

        try
        {
            return Date.from(OffsetDateTime.parse(timestamp).toInstant());
        }
        catch (Exception offsetError)
        {
            try
            {
                return Date.from(Instant.parse(timestamp));
            }
            catch (Exception instantError)
            {
                try
                {
                    return Date.from(LocalDateTime.parse(timestamp).toInstant(ZoneOffset.UTC));
                }
                catch (Exception localError)
                {
                    return null;
                }
            }
        }
    }


    /**
     * Convert a map to JSON for storing in a string property.
     *
     * @param map map (may be null)
     * @return JSON string or null
     */
    private String getJSON(Map<String, Object> map)
    {
        if ((map == null) || (map.isEmpty()))
        {
            return null;
        }

        try
        {
            return OBJECT_MAPPER.writeValueAsString(map);
        }
        catch (Exception error)
        {
            return map.toString();
        }
    }


    /**
     * Add a value to a string map if it is not null.
     *
     * @param map map
     * @param key key
     * @param value value
     */
    private void putIfNotNull(Map<String, String> map,
                              String              key,
                              Object              value)
    {
        if (value != null)
        {
            map.put(key, value.toString());
        }
    }


    /**
     * Add a value to a long map if it is not null.
     *
     * @param map map
     * @param key key
     * @param value value
     */
    private void putIfNotNull(Map<String, Long> map,
                              String            key,
                              Long              value)
    {
        if (value != null)
        {
            map.put(key, value);
        }
    }


    /**
     * Add a value to a double map if it is not null.
     *
     * @param map map
     * @param key key
     * @param value value
     */
    private void putIfNotNull(Map<String, Double> map,
                              String              key,
                              Double              value)
    {
        if (value != null)
        {
            map.put(key, value);
        }
    }


    /**
     * Increment a counter held as a string in a map.
     *
     * @param map map
     * @param key key
     */
    private void incrementCounter(Map<String, String> map,
                                  String              key)
    {
        addToCounter(map, key, 1);
    }


    /**
     * Add to a counter held as a string in a map.
     *
     * @param map map
     * @param key key
     * @param increment amount to add
     */
    private void addToCounter(Map<String, String> map,
                              String              key,
                              long                increment)
    {
        long current = 0;

        if (map.get(key) != null)
        {
            try
            {
                current = Long.parseLong(map.get(key));
            }
            catch (NumberFormatException error)
            {
                current = 0;
            }
        }

        map.put(key, Long.toString(current + increment));
    }


    /* =======================================================================================
     * Logging
     */

    /**
     * Log that a new element has been created.
     *
     * @param typeName open metadata type
     * @param qualifiedName qualified name
     * @param guid unique identifier
     * @param elementKind job, run or dataset
     * @param elementName name from the event
     */
    private void logElementCatalogued(String typeName,
                                      String qualifiedName,
                                      String guid,
                                      String elementKind,
                                      String elementName)
    {
        final String methodName = "logElementCatalogued";

        logRecord(methodName, OpenLineageIntegrationConnectorAuditCode.ELEMENT_CATALOGUED.getMessageDefinition(connectorName,
                                                                                                                typeName,
                                                                                                                qualifiedName,
                                                                                                                guid,
                                                                                                                elementKind,
                                                                                                                elementName));
    }


    /**
     * Log that a new relationship has been created.
     *
     * @param relationshipTypeName type of relationship
     * @param end1 qualified name of end 1
     * @param end2 qualified name of end 2
     */
    private void logLineageCatalogued(String relationshipTypeName,
                                      String end1,
                                      String end2)
    {
        final String methodName = "logLineageCatalogued";

        logRecord(methodName, OpenLineageIntegrationConnectorAuditCode.LINEAGE_CATALOGUED.getMessageDefinition(connectorName,
                                                                                                                relationshipTypeName,
                                                                                                                end1,
                                                                                                                end2));
    }


    /**
     * Log that an event (or part of it) is being ignored.
     *
     * @param eventDescription description of the event
     * @param reason reason
     */
    private void logEventIgnored(String eventDescription,
                                 String reason)
    {
        final String methodName = "logEventIgnored";

        logRecord(methodName, OpenLineageIntegrationConnectorAuditCode.EVENT_IGNORED.getMessageDefinition(connectorName,
                                                                                                           eventDescription,
                                                                                                           reason));
    }


    /**
     * Log that optional metadata could not be recorded.
     *
     * @param methodName calling method
     * @param metadataDescription what could not be recorded
     * @param eventDescription description of the event
     * @param error exception
     */
    private void logOptionalMetadataFailed(String    methodName,
                                           String    metadataDescription,
                                           String    eventDescription,
                                           Exception error)
    {
        logExceptionRecord(methodName,
                           OpenLineageIntegrationConnectorAuditCode.OPTIONAL_METADATA_FAILED.getMessageDefinition(connectorName,
                                                                                                                   metadataDescription,
                                                                                                                   eventDescription,
                                                                                                                   error.getClass().getName(),
                                                                                                                   error.getMessage()),
                           error);
    }


    /**
     * Log an unexpected exception from event processing.
     *
     * @param methodName calling method
     * @param error exception
     * @param rawEvent raw event (may be null)
     * @param event event bean
     */
    private void logUnexpectedException(String    methodName,
                                        Exception error,
                                        String    rawEvent,
                                        Object    event)
    {
        String stringEvent = rawEvent;

        if (stringEvent == null)
        {
            stringEvent = event.toString();
        }

        logExceptionRecord(methodName,
                           OpenLineageIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                              error.getClass().getName(),
                                                                                                              methodName,
                                                                                                              error.getMessage()),
                           stringEvent,
                           error);
    }
}
