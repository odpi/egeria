/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.samples.subjectareas;

import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceDefinitionManager;
import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceMetricsManager;
import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceProgramReviewManager;
import org.odpi.openmetadata.accessservices.governanceprogram.client.SubjectAreaManager;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceMetricElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.SubjectAreaDefinition;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.SubjectAreaElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionStatus;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceMetricProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SubjectAreaClassificationProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SubjectAreaProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.http.HttpHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CreateSubjectAreasSample illustrates the use of the Governance Program OMAS API to create subject areas
 * for Coco Pharmaceuticals.
 */
public class CreateSubjectAreasSample
{
    private final String  serverName;
    private final String  serverURLRoot;
    private final String  clientUserId;

    private final Map<String, String> subjectAreaMap = new HashMap<>();

    private SubjectAreaManager             subjectAreaManager = null;
    private GovernanceMetricsManager       metricsManager     = null;
    private GovernanceDefinitionManager    definitionManager  = null;
    private GovernanceProgramReviewManager reviewManager      = null;

    /**
     * Set up the parameters for the sample.
     *
     * @param serverName server to call
     * @param serverURLRoot location of server
     * @param clientUserId userId to access the server
     */
    public CreateSubjectAreasSample(String  serverName,
                                    String  serverURLRoot,
                                    String  clientUserId)
    {
        this.serverName = serverName;
        this.serverURLRoot = serverURLRoot;
        this.clientUserId = clientUserId;
    }


    /**
     * Set up a new subject area definition
     *
     * @param subjectAreaDefinitionName qualified name
     * @param parentName optional parent subject area
     * @param displayName display name
     * @param description longer description
     * @param scope how broadly is this used
     * @param usage how is this subject area used
     * @param domainIdentifier which governance domain does this subject area belong to
     * @throws InvalidParameterException bad parameters passed to client
     * @throws UserNotAuthorizedException userId is not allowed to create zones
     * @throws PropertyServerException service is not running - or is in trouble
     */
    private void createSubjectAreaDefinition(String     subjectAreaDefinitionName,
                                             String     parentName,
                                             String     displayName,
                                             String     description,
                                             String     scope,
                                             String     usage,
                                             int        domainIdentifier) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        System.out.println("------------------------------------------------------------------------");
        System.out.println(subjectAreaDefinitionName);
        System.out.println("------------------------------------------------------------------------");
        System.out.println(" ==> subjectAreaName: " + subjectAreaDefinitionName);
        System.out.println(" ==> displayName:     " + displayName);
        System.out.println(" ==> description:     " + description);
        System.out.println(" ==> scope:           " + scope);
        System.out.println(" ==> usage:           " + usage);
        System.out.println(" ==> domain:          " + domainIdentifier);
        System.out.println(" ");

        SubjectAreaProperties properties = new SubjectAreaProperties();

        properties.setQualifiedName("SubjectAreaDefinition:" + subjectAreaDefinitionName);
        properties.setSubjectAreaName(subjectAreaDefinitionName);
        properties.setDisplayName(displayName);
        properties.setDescription(description);
        properties.setScope(scope);
        properties.setUsage(usage);
        properties.setDomainIdentifier(domainIdentifier);

        String subjectAreaGUID = subjectAreaManager.createSubjectArea(clientUserId, properties);

        if (parentName != null)
        {
            System.out.println("Subject area " + subjectAreaDefinitionName + " parent " + parentName + " has GUID of " + subjectAreaMap.get(parentName));

            subjectAreaManager.linkSubjectAreasInHierarchy(clientUserId, subjectAreaMap.get(parentName), subjectAreaGUID);
        }

        /*
         * Check the resulting subject area
         */
        SubjectAreaDefinition subjectAreaDefinition = subjectAreaManager.getSubjectAreaDefinitionByGUID(clientUserId, subjectAreaGUID);

        if (subjectAreaDefinition == null)
        {
            errorExit("No definition returned for " + subjectAreaDefinitionName + " with GUID " + subjectAreaGUID);
        }
        else if (subjectAreaDefinition.getElementHeader() == null)
        {
            errorExit("No header  returned for " + subjectAreaDefinitionName + " with GUID " + subjectAreaGUID + ": " + subjectAreaDefinition);
        }
        else if (! subjectAreaGUID.equals(subjectAreaDefinition.getElementHeader().getGUID()))
        {
            errorExit("GUID returned for " + subjectAreaDefinitionName + " is not " + subjectAreaGUID + ": " + subjectAreaDefinition);
        }
        else if (! subjectAreaDefinitionName.equals(subjectAreaDefinition.getProperties().getSubjectAreaName()))
        {
            errorExit("Unexpected subject area name returned for " + subjectAreaDefinitionName + ": " + subjectAreaDefinition);
        }
        else if (parentName != null)
        {
            /*
             * Check the hierarchy is in place.
             */
            if (! subjectAreaMap.get(parentName).equals(subjectAreaDefinition.getParentSubjectAreaGUID()))
            {
                errorExit("GUID returned for parent " + parentName + " is not " + subjectAreaMap.get(parentName) + ": " + subjectAreaDefinition);
            }

            /*
             * Retrieve the parent and check the child is in place.
             */
            SubjectAreaDefinition parentSubjectAreaDefinition = subjectAreaManager.getSubjectAreaDefinitionByGUID(clientUserId, subjectAreaMap.get(parentName));

            if ((parentSubjectAreaDefinition.getNestedSubjectAreaGUIDs() == null) || (parentSubjectAreaDefinition.getNestedSubjectAreaGUIDs().isEmpty()))
            {
                errorExit("No children returned for parent " + parentName + ": " + parentSubjectAreaDefinition);
            }
            else
            {
                boolean found = false;

                for (String childGUID : parentSubjectAreaDefinition.getNestedSubjectAreaGUIDs())
                {
                    if (subjectAreaGUID.equals(childGUID))
                    {
                        found = true;
                        break;
                    }
                }

                if (! found)
                {
                    errorExit("No child of " + subjectAreaGUID + " returned for parent " + parentName + ": " + parentSubjectAreaDefinition);
                }
            }
        }
        else
        {
            /*
             * Check no parent is in place.
             */
            if (subjectAreaDefinition.getParentSubjectAreaGUID() != null)
            {
                errorExit("Unexpected parent " + subjectAreaDefinition.getParentSubjectAreaGUID() + " returned: " + subjectAreaDefinition);
            }
        }

        subjectAreaMap.put(subjectAreaDefinitionName, subjectAreaGUID);
        System.out.println("Mapped " + subjectAreaDefinitionName + " to: " + subjectAreaGUID);
    }


    /**
     * Retrieve the subject areas using different methods.
     *
     * @throws InvalidParameterException bad parameters passed to client
     * @throws UserNotAuthorizedException userId is not allowed to create zones
     * @throws PropertyServerException service is not running - or is in trouble
     */
    private void reviewSubjectAreas() throws InvalidParameterException,
                                             UserNotAuthorizedException,
                                             PropertyServerException
    {
        /*
         * Using the subject area name
         */
        for (String subjectAreaName : subjectAreaMap.keySet())
        {
            SubjectAreaElement subjectAreaDefinition = subjectAreaManager.getSubjectAreaByName(clientUserId, subjectAreaName);

            if (subjectAreaDefinition == null)
            {
                errorExit("No subject area definition returned for: " + subjectAreaName);
            }
            else if (! subjectAreaMap.get(subjectAreaName).equals(subjectAreaDefinition.getElementHeader().getGUID()))
            {
                errorExit("Unexpected subject area returned for subject area name " + subjectAreaName + ": " + subjectAreaDefinition);
            }
        }

        /*
         * Using the subject area definition guid
         */
        for (String subjectAreaName : subjectAreaMap.keySet())
        {
            SubjectAreaElement subjectAreaDefinition = subjectAreaManager.getSubjectAreaByGUID(clientUserId, subjectAreaMap.get(subjectAreaName));

            if (subjectAreaDefinition == null)
            {
                errorExit("No subject area definition returned for: " + subjectAreaMap.get(subjectAreaName));
            }
            else if (! subjectAreaMap.get(subjectAreaName).equals(subjectAreaDefinition.getElementHeader().getGUID()))
            {
                errorExit("Unexpected subject area returned for subject area GUID " + subjectAreaMap.get(subjectAreaName) + ": " + subjectAreaDefinition);
            }
        }

        /*
         * Update the subject area definitions relating to product to the product assurance governance domain (8)
         * and then retrieve by governance domain.
         */
        List<String> productSubjectAreaNames = new ArrayList<>();

        for (SubjectAreaSampleDefinitions sampleDefinition : SubjectAreaSampleDefinitions.values())
        {
            if (sampleDefinition.getSubjectAreaName().contains("Product"))
            {
                System.out.println("Adding " + sampleDefinition.getSubjectAreaName() + " to the product assurance domain.");

                SubjectAreaProperties updateProperties = new SubjectAreaProperties();

                updateProperties.setDomainIdentifier(8);

                subjectAreaManager.updateSubjectArea(clientUserId, subjectAreaMap.get(sampleDefinition.getSubjectAreaName()), true, updateProperties);

                productSubjectAreaNames.add(sampleDefinition.getSubjectAreaName());
            }
        }

        List<SubjectAreaElement> productSubjectAreas = subjectAreaManager.getSubjectAreasForDomain(clientUserId, 8, 0, 0);

        if ((productSubjectAreas == null) || (productSubjectAreas.isEmpty()))
        {
            errorExit("No product subject areas");
        }
        else if (productSubjectAreas.size() != productSubjectAreaNames.size())
        {
            errorExit("No product subject areas");
        }
        else
        {
            for (String name : productSubjectAreaNames)
            {
                boolean found = false;

                for (SubjectAreaElement subjectAreaElement : productSubjectAreas)
                {
                    if (name.equals(subjectAreaElement.getProperties().getSubjectAreaName()))
                    {
                        found = true;

                        if (! subjectAreaMap.get(name).equals(subjectAreaElement.getElementHeader().getGUID()))
                        {
                            errorExit("Unexpected product subject area: " + name);
                        }
                    }
                }

                if (! found)
                {
                    errorExit("Missing product subject area: " + name);
                }
            }
        }
    }


    /**
     * Associate elements with the subject area and check they are retrieved.
     *
     * @throws InvalidParameterException bad parameters passed to client
     * @throws UserNotAuthorizedException userId is not allowed to create zones
     * @throws PropertyServerException service is not running - or is in trouble
     */
    private void accessSubjectAreaMembers() throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException
    {
        /*
         * Create governance definition graph and link one to subject area definitions.
         */
        final String governanceDriverTypeName = "BusinessImperative";
        final String governanceDriverDocId = "GOV-BUS-001";
        final String governanceDriverTitle = "Many regulations require accurate reporting.";
        final String governancePolicyTypeName = "GovernancePrinciple";
        final String governancePolicyDocId = "GOV-DATA-001";
        final String governancePolicyTitle = "All critical data elements should be defined and controlled.";
        final int    governancePolicyDomain = 1;
        final String governanceControl1TypeName = "OrganizationalControl";
        final String governanceControl1DocId = "GOV-SUBJECT-AREA-001";
        final String governanceControl1Title = "Use subject areas to manage the definitions of critical data items.";
        final int    governanceControl1Domain = 1;
        final String governanceControl2TypeName = "TechnicalControl";
        final String governanceControl2DocId = "GOV-METRICS-001";
        final String governanceControl2Title = "Implement governance metrics using defined governance processes/rules.";
        final int    governanceControl2Domain = 0;

        GovernanceDefinitionProperties governanceDefinitionProperties = new GovernanceDefinitionProperties();
        governanceDefinitionProperties.setTypeName(governanceDriverTypeName);
        governanceDefinitionProperties.setDocumentIdentifier(governanceDriverDocId);
        governanceDefinitionProperties.setTitle(governanceDriverTitle);

        String governanceDriverGUID = definitionManager.createGovernanceDefinition(clientUserId, governanceDefinitionProperties, GovernanceDefinitionStatus.ACTIVE);

        governanceDefinitionProperties = new GovernanceDefinitionProperties();
        governanceDefinitionProperties.setTypeName(governancePolicyTypeName);
        governanceDefinitionProperties.setDocumentIdentifier(governancePolicyDocId);
        governanceDefinitionProperties.setTitle(governancePolicyTitle);
        governanceDefinitionProperties.setDomainIdentifier(governancePolicyDomain);

        String governancePolicyGUID = definitionManager.createGovernanceDefinition(clientUserId, governanceDefinitionProperties, GovernanceDefinitionStatus.ACTIVE);

        definitionManager.setupSupportingDefinition(clientUserId, governanceDriverGUID, governancePolicyGUID, "GovernanceResponse", null);

        governanceDefinitionProperties = new GovernanceDefinitionProperties();
        governanceDefinitionProperties.setTypeName(governanceControl1TypeName);
        governanceDefinitionProperties.setDocumentIdentifier(governanceControl1DocId);
        governanceDefinitionProperties.setTitle(governanceControl1Title);
        governanceDefinitionProperties.setDomainIdentifier(governanceControl1Domain);

        String governanceControl1GUID = definitionManager.createGovernanceDefinition(clientUserId, governanceDefinitionProperties, GovernanceDefinitionStatus.ACTIVE);
        definitionManager.setupSupportingDefinition(clientUserId, governancePolicyGUID, governanceControl1GUID, "GovernanceImplementation", null);

        governanceDefinitionProperties = new GovernanceDefinitionProperties();
        governanceDefinitionProperties.setTypeName(governanceControl2TypeName);
        governanceDefinitionProperties.setDocumentIdentifier(governanceControl2DocId);
        governanceDefinitionProperties.setTitle(governanceControl2Title);
        governanceDefinitionProperties.setDomainIdentifier(governanceControl2Domain);

        String governanceControl2GUID = definitionManager.createGovernanceDefinition(clientUserId, governanceDefinitionProperties, GovernanceDefinitionStatus.ACTIVE);

        definitionManager.setupSupportingDefinition(clientUserId, governancePolicyGUID, governanceControl2GUID, "GovernanceImplementation", null);
        definitionManager.linkPeerDefinitions(clientUserId, governanceControl1GUID, governanceControl2GUID, "GovernanceControlLink", null);

        GovernanceDefinitionGraph governanceDefinitionGraph = reviewManager.getGovernanceDefinitionInContext(clientUserId, governancePolicyGUID);

        if ((governanceDefinitionGraph.getParents() == null) ||
                    (governanceDefinitionGraph.getChildren() == null) ||
                    (governanceDefinitionGraph.getPeers() != null) ||
                    (governanceDefinitionGraph.getElementHeader() == null) ||
                    (governanceDefinitionGraph.getProperties() == null))
        {
            System.out.println("Unexpected policy governance definition graph: " + governanceDefinitionGraph);
        }

        governanceDefinitionGraph = reviewManager.getGovernanceDefinitionInContext(clientUserId, governanceDriverGUID);

        if ((governanceDefinitionGraph.getParents() != null) ||
                    (governanceDefinitionGraph.getChildren() == null) ||
                    (governanceDefinitionGraph.getPeers() != null) ||
                    (governanceDefinitionGraph.getElementHeader() == null) ||
                    (governanceDefinitionGraph.getProperties() == null))
        {
            System.out.println("Unexpected driver governance definition graph: " + governanceDefinitionGraph);
        }

        governanceDefinitionGraph = reviewManager.getGovernanceDefinitionInContext(clientUserId, governanceControl1GUID);

        if ((governanceDefinitionGraph.getParents() == null) ||
                    (governanceDefinitionGraph.getChildren() != null) ||
                    (governanceDefinitionGraph.getPeers() == null) ||
                    (governanceDefinitionGraph.getElementHeader() == null) ||
                    (governanceDefinitionGraph.getProperties() == null))
        {
            System.out.println("Unexpected control1 governance definition graph: " + governanceDefinitionGraph);
        }

        /*
         * Link each subject area definition to the control 1 governance definition that covers subject areas.
         */
        for (String subjectAreaGUID : subjectAreaMap.values())
        {
            subjectAreaManager.setupGovernedBy(clientUserId, subjectAreaGUID, null, governanceControl1GUID);
        }

        List<RelatedElement> governedElements = subjectAreaManager.getGovernedElements(clientUserId, governanceControl1GUID, 0, 0);

        if ((governedElements == null) || (governedElements.isEmpty()))
        {
            errorExit("No governed elements for governance control 1: " + governanceControl1GUID);
        }
        else
        {
            List<String> missingLinks = new ArrayList<>();

            for (String subjectAreaGUID : subjectAreaMap.values())
            {
                boolean found = false;

                for (RelatedElement relatedElement : governedElements)
                {
                    if (subjectAreaGUID.equals(relatedElement.getRelatedElement().getGUID()))
                    {
                        found = true;
                        break;
                    }
                }

                if (! found)
                {
                    missingLinks.add(subjectAreaGUID);
                }
            }

            if (! missingLinks.isEmpty())
            {
                errorExit("Missing governed elements for governance control 1: " + missingLinks);
            }
        }

        /*
         * Try working with a governance metric
         */
        final String metricName = "Trial Coverage";
        final String metricDescription = "The estimated proportion of the population covered by a clinical trial.";
        final String measurement = "Proportion of genome covered by trial participants.";
        final String target = "80%";
        final int    domainIdentifier = 8; // product assurance

        GovernanceMetricProperties metricProperties = new GovernanceMetricProperties();

        metricProperties.setQualifiedName("GovernanceMetric:" + metricName);
        metricProperties.setDisplayName(metricName);
        metricProperties.setDescription(metricDescription);
        metricProperties.setMeasurement(measurement);
        metricProperties.setTarget(target);
        metricProperties.setDomainIdentifier(domainIdentifier);

        String metricGUID = metricsManager.createGovernanceMetric(clientUserId, metricProperties);

        SubjectAreaClassificationProperties subjectAreaClassificationProperties = new SubjectAreaClassificationProperties();

        subjectAreaClassificationProperties.setSubjectAreaName(SubjectAreaSampleDefinitions.PRODUCT.getSubjectAreaName());

        subjectAreaManager.addSubjectAreaMemberClassification(clientUserId, metricGUID, subjectAreaClassificationProperties);

        /*
         * Retrieve the metric to check it is part of the subject area.
         */
        GovernanceMetricElement metricElement = metricsManager.getGovernanceMetricByGUID(clientUserId, metricGUID);

        if (! metricGUID.equals(metricElement.getElementHeader().getGUID()))
        {
            errorExit("GUID returned for " + metricName + " is not " + metricGUID + ": " + metricElement);
        }
        if (! metricName.equals(metricElement.getProperties().getDisplayName()))
        {
            errorExit("Unexpected metric name returned for " + metricName + ": " + metricElement);
        }

        if (metricElement.getElementHeader().getClassifications() != null)
        {
            boolean found = false;

            for (ElementClassification classification : metricElement.getElementHeader().getClassifications())
            {
                if ("SubjectArea".equals(classification.getClassificationName()))
                {
                    found = true;

                    Map<String, Object> properties = classification.getClassificationProperties();

                    if ((properties == null) || (properties.isEmpty()))
                    {
                        errorExit("No SubjectArea classification properties returned for " + metricName + ": " + metricElement);
                    }
                    else
                    {
                        Object retrievedSubjectAreaProperty = properties.get("name");

                        if (retrievedSubjectAreaProperty == null)
                        {
                            errorExit("No SubjectArea name property returned for " + metricName + ": " + metricElement);
                        }
                        else if (! SubjectAreaSampleDefinitions.PRODUCT.getSubjectAreaName().equals(retrievedSubjectAreaProperty.toString()))
                        {
                            errorExit("Unexpected SubjectArea name property (" + retrievedSubjectAreaProperty + ") returned for " + metricName + ": " + metricElement);
                        }
                    }
                }
            }

            if (! found)
            {
                errorExit("No subject area classification of " + SubjectAreaSampleDefinitions.PRODUCT.getSubjectAreaName() + " returned for metric " + metricName + ": " + metricElement);
            }
        }
        else
        {
            errorExit("No classifications for " + metricName + ": " + metricElement);
        }

        /*
         * Retrieve the elements associated with the subject area and check they are
         */
        List<ElementStub> members = subjectAreaManager.getMembersOfSubjectArea(clientUserId, SubjectAreaSampleDefinitions.PRODUCT.getSubjectAreaName(), 0, 0);

        if ((members == null) || (members.isEmpty()))
        {
            errorExit("No classified elements for subject area " + SubjectAreaSampleDefinitions.PRODUCT.getSubjectAreaName());
        }
    }

    private void errorExit(String errorMessage)
    {
        System.out.println(errorMessage);
        System.exit(-1);
    }

    /**
     * This runs the sample
     *
     * @throws Exception generated errors
     */
    public void run() throws Exception
    {
        try
        {
            subjectAreaManager = new SubjectAreaManager(serverName, serverURLRoot);
            metricsManager = new GovernanceMetricsManager(serverName, serverURLRoot);
            definitionManager = new GovernanceDefinitionManager(serverName, serverURLRoot);
            reviewManager = new GovernanceProgramReviewManager(serverName, serverURLRoot);

            SubjectAreaSampleDefinitions[] subjectAreaDefinitions = SubjectAreaSampleDefinitions.values();

            for (SubjectAreaSampleDefinitions definition : subjectAreaDefinitions)
            {
                createSubjectAreaDefinition(definition.getSubjectAreaName(),
                                            definition.getParentName(),
                                            definition.getDisplayName(),
                                            definition.getDescription(),
                                            definition.getScope(),
                                            definition.getUsage(),
                                            definition.getDomain());
            }

            reviewSubjectAreas();
            accessSubjectAreaMembers();
        }
        catch (Exception error)
        {
            System.out.println("There was an exception when calling the SubjectAreaManager client.  Error message is: " + error.getMessage());
            throw error;
        }
    }


    /**
     * Main program that controls the operation of the sample.  The parameters are passed space separated.
     * The file name must be passed as parameter 1.  The other parameters are used to override the
     * sample's default values.
     *
     * @param args 1. file name 2. server name, 3. URL root for the server, 4. client userId
     */
    public static void main(String[] args)
    {
        String  serverName = "fvtMDS";
        String  serverURLRoot = "https://localhost:9443";
        String  clientUserId = "erinoverview";


        if (args.length > 1)
        {
            serverName = args[1];
        }

        if (args.length > 2)
        {
            serverURLRoot = args[2];
        }

        if (args.length > 3)
        {
            clientUserId = args[3];
        }

        System.out.println("===============================");
        System.out.println("Create Subject Area Definitions Sample   ");
        System.out.println("===============================");
        System.out.println("Running against server: " + serverName + " at " + serverURLRoot);
        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        HttpHelper.noStrictSSL();


        try
        {
            CreateSubjectAreasSample sample = new CreateSubjectAreasSample(serverName, serverURLRoot, clientUserId);

            sample.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
