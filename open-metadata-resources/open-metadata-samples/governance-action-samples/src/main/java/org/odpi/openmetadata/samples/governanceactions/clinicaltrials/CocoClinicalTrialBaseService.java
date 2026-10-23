/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Provides shared functions for the clinical trial services.
 * <br><br>
 * The services are written to be re-runnable: if a run fails part way through, or a clinical trial is set up a
 * second time, each element and relationship that the earlier run created is found and reused rather than created
 * again.  Without this, the second run fails on the first element it tries to create, since its qualified name is
 * already taken (OMAG-COMMON-409-001), and the elements the earlier run left behind have to be removed by hand
 * before the service can be run again.  The find-or-create methods below provide this behaviour.
 */
public class CocoClinicalTrialBaseService extends GeneralGovernanceActionService
{
    protected static class PersonContactDetails
    {
        String personGUID   = null;
        String contactQName = null;
        String contactName  = null;
        String contactEmail = null;
    }


    /**
     * Return the unique identifier of the element with this qualified name, or null if there is none.
     *
     * @param qualifiedName qualified name to look for
     * @return unique identifier or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem accessing the metadata repository/server
     * @throws UserNotAuthorizedException security problem
     */
    protected String getElementGUIDByQualifiedName(String qualifiedName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        if (qualifiedName == null)
        {
            return null;
        }

        OpenMetadataStore openMetadataStore = governanceContext.getOpenMetadataStore();

        return openMetadataStore.getMetadataElementGUIDByUniqueName(qualifiedName,
                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                    openMetadataStore.getGetOptions());
    }


    /**
     * Return the unique identifier of the element that would be created with these properties, if it already exists.
     *
     * @param properties properties of the new element, which include its qualified name
     * @return unique identifier or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem accessing the metadata repository/server
     * @throws UserNotAuthorizedException security problem
     */
    protected String getExistingElementGUID(ElementProperties properties) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "getExistingElementGUID";

        return this.getElementGUIDByQualifiedName(propertyHelper.getStringProperty(governanceServiceName,
                                                                                   OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                   properties,
                                                                                   methodName));
    }


    /**
     * Return the unique identifier of the element that a template would create, if it already exists.  The new
     * element's qualified name comes from the replacement properties if they set one, and from the template otherwise,
     * with the placeholders filled in the same way the template is copied.  If a placeholder is left unfilled, the
     * name cannot be predicted, and null is returned so that the element is created.
     *
     * @param templateGUID unique identifier of the template
     * @param replacementProperties properties that override the template's (may be null)
     * @param placeholderProperties placeholder values (may be null)
     * @return unique identifier or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem accessing the metadata repository/server
     * @throws UserNotAuthorizedException security problem
     */
    protected String getExistingTemplatedElementGUID(String              templateGUID,
                                                     ElementProperties   replacementProperties,
                                                     Map<String, String> placeholderProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getExistingTemplatedElementGUID";

        String qualifiedName = propertyHelper.getStringProperty(governanceServiceName,
                                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                replacementProperties,
                                                                methodName);

        if ((qualifiedName == null) && (templateGUID != null))
        {
            OpenMetadataElement template = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(templateGUID);

            if (template != null)
            {
                qualifiedName = propertyHelper.getStringProperty(governanceServiceName,
                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                 template.getElementProperties(),
                                                                 methodName);
            }
        }

        qualifiedName = propertyHelper.replacePrimitiveStringWithPlaceholders(qualifiedName, placeholderProperties);

        if ((qualifiedName == null) || (qualifiedName.contains("~{")))
        {
            return null;
        }

        return this.getElementGUIDByQualifiedName(qualifiedName);
    }


    /**
     * Return the unique identifier of the relationship of this type from end1 to end2 whose properties include all
     * the supplied string properties, or null if there is none.  Matching on the properties as well as the ends keeps
     * apart relationships that are legitimately repeated between the same two elements - an action target under two
     * different names, a person assigned to a project in two roles, or lineage for two information supply chains.
     *
     * @param relationshipTypeName type of relationship
     * @param end1GUID unique identifier of the element at end 1
     * @param end2GUID unique identifier of the element at end 2
     * @param properties properties of the new relationship (may be null)
     * @return unique identifier of the relationship or null
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem accessing the metadata repository/server
     * @throws UserNotAuthorizedException security problem
     */
    protected String getExistingRelationshipGUID(String            relationshipTypeName,
                                                 String            end1GUID,
                                                 String            end2GUID,
                                                 ElementProperties properties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        Map<String, String> expectedProperties = this.getPropertiesAsStrings(properties);

        int                        startFrom = 0;
        RelatedMetadataElementList related   = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(end1GUID,
                                                                                                                  1,
                                                                                                                  relationshipTypeName,
                                                                                                                  startFrom,
                                                                                                                  governanceContext.getMaxPageSize());
        while ((related != null) && (related.getElementList() != null) && (! related.getElementList().isEmpty()))
        {
            for (RelatedMetadataElement relatedElement : related.getElementList())
            {
                if ((relatedElement != null) && (end2GUID.equals(relatedElement.getElement().getElementGUID())))
                {
                    Map<String, String> existingProperties = this.getPropertiesAsStrings(relatedElement.getRelationshipProperties());

                    if (existingProperties.entrySet().containsAll(expectedProperties.entrySet()))
                    {
                        return relatedElement.getRelationshipGUID();
                    }
                }
            }

            startFrom = startFrom + governanceContext.getMaxPageSize();

            related = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(end1GUID,
                                                                                         1,
                                                                                         relationshipTypeName,
                                                                                         startFrom,
                                                                                         governanceContext.getMaxPageSize());
        }

        return null;
    }


    /**
     * Return the properties as a string map, which is empty rather than null when there are none.
     *
     * @param properties properties (may be null)
     * @return map of property name to value
     */
    private Map<String, String> getPropertiesAsStrings(ElementProperties properties)
    {
        Map<String, String> propertiesAsStrings = (properties == null) ? null : properties.getPropertiesAsStrings();

        return (propertiesAsStrings == null) ? Collections.emptyMap() : propertiesAsStrings;
    }


    /**
     * Create a relationship unless an identical one is already in place.
     *
     * @param relationshipTypeName type of relationship
     * @param end1GUID unique identifier of the element at end 1
     * @param end2GUID unique identifier of the element at end 2
     * @param properties properties of the relationship (may be null)
     * @return unique identifier of the new or existing relationship
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem accessing the metadata repository/server
     * @throws UserNotAuthorizedException security problem
     */
    protected String createRelationshipIfAbsent(String            relationshipTypeName,
                                                String            end1GUID,
                                                String            end2GUID,
                                                ElementProperties properties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        String relationshipGUID = this.getExistingRelationshipGUID(relationshipTypeName, end1GUID, end2GUID, properties);

        if (relationshipGUID == null)
        {
            relationshipGUID = governanceContext.getOpenMetadataStore().createRelatedElementsInStore(relationshipTypeName,
                                                                                                     end1GUID,
                                                                                                     end2GUID,
                                                                                                     null,
                                                                                                     null,
                                                                                                     properties);
        }

        return relationshipGUID;
    }


    /**
     * Create a lineage relationship unless an identical one is already in place.  The parameters are those of
     * GovernanceActionContext.createLineageRelationship.
     *
     * @param relationshipName  either LineageMapping, ProcessCall, DataFlow, ControlFlow.
     * @param sourceElementGUID unique identifier of the element that describes the source of the data.
     * @param iscQualifiedName qualifiedName of the information supply chain
     * @param label label for when the lineage relationship is visualized
     * @param description description of the lineage relationship
     * @param formula expression summary
     * @param formulaType language used to express the formula
     * @param queryId identifier for the process query - DataMapping Relationship
     * @param query the process query - DataMapping Relationship
     * @param guard value that indicated a control path to take - ControlFlow relationship
     * @param lineNumber line number where the call is made - ProcessCall Relationship
     * @param targetElementGUID unique identifier of the element that describes the destination of the data.
     * @return unique identifier of the new or existing relationship
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem accessing the metadata repository/server
     * @throws UserNotAuthorizedException security problem
     */
    protected String createLineageRelationshipIfAbsent(String relationshipName,
                                                       String sourceElementGUID,
                                                       String iscQualifiedName,
                                                       String label,
                                                       String description,
                                                       String formula,
                                                       String formulaType,
                                                       String queryId,
                                                       String query,
                                                       String guard,
                                                       String lineNumber,
                                                       String targetElementGUID) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        ElementProperties properties = propertyHelper.addStringProperty(null, OpenMetadataProperty.ISC_QUALIFIED_NAME.name, iscQualifiedName);

        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.LABEL.name, label);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.DESCRIPTION.name, description);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.FORMULA.name, formula);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.FORMULA_TYPE.name, formulaType);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.QUERY_ID.name, queryId);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.QUERY.name, query);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.GUARD.name, guard);
        properties = propertyHelper.addStringProperty(properties, OpenMetadataProperty.LINE_NUMBER.name, lineNumber);

        String relationshipTypeName = (relationshipName == null) ? OpenMetadataType.LINEAGE_MAPPING_RELATIONSHIP.typeName : relationshipName;
        String relationshipGUID     = this.getExistingRelationshipGUID(relationshipTypeName, sourceElementGUID, targetElementGUID, properties);

        if (relationshipGUID == null)
        {
            relationshipGUID = governanceContext.createLineageRelationship(relationshipName,
                                                                           sourceElementGUID,
                                                                           iscQualifiedName,
                                                                           label,
                                                                           description,
                                                                           formula,
                                                                           formulaType,
                                                                           queryId,
                                                                           query,
                                                                           guard,
                                                                           lineNumber,
                                                                           targetElementGUID);
        }

        return relationshipGUID;
    }


    /**
     * Return whether the element already has the named classification.
     *
     * @param elementGUID unique identifier of the element
     * @param classificationName name of the classification
     * @return flag
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem accessing the metadata repository/server
     * @throws UserNotAuthorizedException security problem
     */
    protected boolean isClassified(String elementGUID,
                                   String classificationName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        OpenMetadataElement element = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(elementGUID);

        if ((element != null) && (element.getClassifications() != null))
        {
            for (AttachedClassification classification : element.getClassifications())
            {
                if ((classification != null) && (classificationName.equals(classification.getClassificationName())))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Create a specific governance action process from a generic governance action type, unless a process with
     * this qualified name already exists.  The parameters are those of createProcessFromGovernanceActionType.
     *
     * @param processType type of the process
     * @param processQualifiedName new qualified name for the process
     * @param processName new name for the process
     * @param processDescription new description for the process
     * @param domainIdentifier governance domain
     * @param governanceActionTypeGUID the unique identifier of the governance action type
     * @param additionalRequestParameters the additional, predefined request parameters to add to the
     *                                   GovernanceActionProcessFlow relationship
     * @param anchorGUID unique identifier of the anchor
     * @param anchorScopeGUIDs unique identifier for the top level project - used as a search scope
     * @return unique identifier of the new or existing governance action process
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    protected String createProcessFromGovernanceActionTypeIfAbsent(String              processType,
                                                                   String              processQualifiedName,
                                                                   String              processName,
                                                                   String              processDescription,
                                                                   int                 domainIdentifier,
                                                                   String              governanceActionTypeGUID,
                                                                   Map<String, String> additionalRequestParameters,
                                                                   String              anchorGUID,
                                                                   List<String>        anchorScopeGUIDs) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        String processGUID = this.getElementGUIDByQualifiedName(processQualifiedName);

        if (processGUID == null)
        {
            processGUID = governanceContext.createProcessFromGovernanceActionType(processType,
                                                                                  processQualifiedName,
                                                                                  processName,
                                                                                  processDescription,
                                                                                  domainIdentifier,
                                                                                  governanceActionTypeGUID,
                                                                                  additionalRequestParameters,
                                                                                  anchorGUID,
                                                                                  anchorScopeGUIDs);
        }

        return processGUID;
    }


    /**
     * Extract the qualified name for the information supply chain.
     * 
     * @param informationSupplyChainGUID unique identifier of the information supply chain element
     * @return string
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem accessing the metadata repository/server
     * @throws UserNotAuthorizedException security problem
     */
    protected String getInformationSupplyChainQualifiedName(String informationSupplyChainGUID) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "getInformationSupplyChainQualifiedName";
        final String parameterName = "informationSupplyChainGUID";

        propertyHelper.validateGUID(informationSupplyChainGUID, parameterName, methodName);
        
        OpenMetadataElement informationSupplyChain = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(informationSupplyChainGUID);
        
        if (informationSupplyChain != null)
        {
            return propertyHelper.getStringProperty(governanceServiceName,
                                                    OpenMetadataProperty.QUALIFIED_NAME.name, 
                                                    informationSupplyChain.getElementProperties(),
                                                    methodName);
        }
        
        return null;
    }

    /**
     * Retrieve the email for a specific person.
     *
     * @param personGUID unique identifier of the person entity for the individual
     * @return email address for the individual; or null if it is not found
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem accessing the metadata repository/server
     * @throws UserNotAuthorizedException security problem
     */
    protected PersonContactDetails getContactDetailsForPersonGUID(String personGUID) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getContactDetailsForPersonGUID";
        final String parameterName = "personGUID";

        propertyHelper.validateGUID(personGUID, parameterName, methodName);

        PersonContactDetails personContactDetails = new PersonContactDetails();

        personContactDetails.personGUID = personGUID;

        OpenMetadataElement person = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(personGUID);

        personContactDetails.contactQName = propertyHelper.getStringProperty(governanceServiceName,
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            person.getElementProperties(),
                                                                            methodName);

        personContactDetails.contactName = propertyHelper.getStringProperty(governanceServiceName,
                                                                            OpenMetadataProperty.DISPLAY_NAME.name,
                                                                            person.getElementProperties(),
                                                                            methodName);

        RelatedMetadataElementList contactDetails = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(personGUID,
                                                                                                                        1,
                                                                                                                        OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                                                                                                        0,
                                                                                                                        0);
        if ((contactDetails != null) && (contactDetails.getElementList() != null))
        {
            for (RelatedMetadataElement contactDetail : contactDetails.getElementList())
            {
                if (contactDetail != null)
                {
                    personContactDetails.contactEmail = propertyHelper.getStringProperty(governanceServiceName,
                                                                                         OpenMetadataProperty.CONTACT_METHOD_VALUE.name,
                                                                                         contactDetail.getElement().getElementProperties(),
                                                                                         methodName);
                }
            }
        }

        return personContactDetails;
    }


    /**
     * Check that the certification type is associated with the requested project.  Otherwise, the certification does not make sense.
     *
     * @param projectGUID unique identifier of the project
     * @param certificationTypeGUID unique identifier of the certification type
     *
     * @throws ConnectorCheckedException the certification type is not linked to the project
     * @throws InvalidParameterException invalid parameter passed somehow
     * @throws PropertyServerException problem connecting to the open metadata repository
     * @throws UserNotAuthorizedException security problem
     */
    protected void checkCertificationValidForProject(String projectGUID,
                                                     String certificationTypeGUID) throws ConnectorCheckedException,
                                                                                          InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName = "checkCertificationValidForProject";
        final String parameterName = "certificationTypeGUID";
        final String parameterName2 = "projectGUID";

        propertyHelper.validateGUID(certificationTypeGUID, parameterName, methodName);
        propertyHelper.validateGUID(projectGUID, parameterName2, methodName);

        int projectStartFrom = 0;
        RelatedMetadataElementList projects = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(certificationTypeGUID,
                                                                                                                  1,
                                                                                                                  OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                                                                                  projectStartFrom,
                                                                                                                  governanceContext.getMaxPageSize());
        while ((projects != null) && (projects.getElementList() != null))
        {
            for (RelatedMetadataElement project : projects.getElementList())
            {
                if (project != null)
                {
                    if (projectGUID.equals(project.getElement().getElementGUID()))
                    {
                        return;
                    }
                }
            }

            projectStartFrom = projectStartFrom + governanceContext.getMaxPageSize();

            projects = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(certificationTypeGUID,
                                                                                           1,
                                                                                           OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                                                           projectStartFrom,
                                                                                           governanceContext.getMaxPageSize());
        }

        /*
         * If we get to this point, the certification type is not linked to the clinical trial.
         */
        throw new ConnectorCheckedException(GovernanceActionSamplesErrorCode.WRONG_CERTIFICATION_TYPE_FOR_TRIAL.getMessageDefinition(governanceServiceName,
                                                                                                                                     certificationTypeGUID,
                                                                                                                                     projectGUID),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Link an action target to a process.  This action target will be available for all steps in the process.
     *
     * @param processGUID unique identifier of the process
     * @param actionTargetName name of the target for action relationship
     * @param actionTargetGUID unique identifier of the target
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    protected void addActionTargetToProcess(String processGUID,
                                            String actionTargetName,
                                            String actionTargetGUID) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        String methodName = "addActionTargetToProcess(" + processGUID + ", " + actionTargetName + ", " + actionTargetGUID + ")";
        final String parameterName = "actionTargetGUID";
        final String parameterName2 = "processGUID";

        propertyHelper.validateGUID(actionTargetGUID, parameterName, methodName);
        propertyHelper.validateGUID(processGUID, parameterName2, methodName);

        this.createRelationshipIfAbsent(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                        processGUID,
                                        actionTargetGUID,
                                        propertyHelper.addStringProperty(null,
                                                                         OpenMetadataProperty.ACTION_TARGET_NAME.name,
                                                                         actionTargetName));
    }


    /**
     * Set up the ImplementedBy relationship between an implementation component and a solution component.
     * The information supply chain qualified names ensures that the appropriate assets are returned
     * on a specific ISC lineage query.
     *
     * @param solutionComponentGUID unique identifier of the solution component
     * @param implementationGUID unique identifier of the newly set up governance action process
     * @param informationSupplyChainQualifiedName option name of information supply chain - used to identify solution component
     *                         implementations that are specific to a particular information supply chain
     * @param role optional role of the implementation
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected void addSolutionComponentImplementedByRelationship(String solutionComponentGUID,
                                                                 String implementationGUID,
                                                                 String informationSupplyChainQualifiedName,
                                                                 String role) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "addSolutionComponentImplementedByRelationship";
        final String parameterName = "implementationGUID";
        final String parameterName2 = "solutionComponentGUID";

        propertyHelper.validateGUID(implementationGUID, parameterName, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, parameterName2, methodName);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.DESIGN_STEP.name,
                                                                        this.getClass().getName());

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                      informationSupplyChainQualifiedName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.ROLE.name,
                                                      role);

        this.createRelationshipIfAbsent(OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                        solutionComponentGUID,
                                        implementationGUID,
                                        properties);
    }


    /**
     * Set up the ImplementedBy relationship between an implementation component and a solution component.
     * The information supply chain qualified names ensures that the appropriate assets are returned
     * on a specific ISC lineage query.
     *
     * @param solutionComponentGUID unique identifier of the solution component
     * @param implementationGUID unique identifier of the newly set up governance action process
     * @param role role of the component in an implementation
     * @param description optional description of the implementation
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected void addSolutionComponentImplementationResource(String solutionComponentGUID,
                                                              String implementationGUID,
                                                              String role,
                                                              String description) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "addSolutionComponentImplementationResource";
        final String parameterName = "implementationGUID";
        final String parameterName2 = "solutionComponentGUID";

        propertyHelper.validateGUID(implementationGUID, parameterName, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, parameterName2, methodName);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.ROLE.name,
                                                                        role);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                      description);

        this.createRelationshipIfAbsent(OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName,
                                        solutionComponentGUID,
                                        implementationGUID,
                                        properties);
    }


    /**
     * Set up the ResourceList relationship between an implementation component and a solution component.
     *
     * @param consumerGUID unique identifier of the element that is to use the element
     * @param resourceGUID unique identifier of the resource
     * @param resourceUse how is the resource to be used?
     *
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected void addResourceListRelationship(String      consumerGUID,
                                               String      resourceGUID,
                                               ResourceUse resourceUse) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "addResourceListRelationship";
        final String parameterName = "consumerGUID";
        final String parameterName2 = "resourceGUID";

        propertyHelper.validateGUID(consumerGUID, parameterName, methodName);
        propertyHelper.validateGUID(resourceGUID, parameterName2, methodName);

        this.createRelationshipIfAbsent(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                        consumerGUID,
                                        resourceGUID,
                                        this.getResourceUseProperties(resourceUse));
    }


    /**
     * Set up the properties for a resource use enum.
     *
     * @param resourceUse enum
     * @return element properties
     */
    protected NewElementProperties getResourceUseProperties(ResourceUse resourceUse)
    {
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.RESOURCE_USE.name,
                                                                        resourceUse.getResourceUse());

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                      resourceUse.getResourceUse());

        return new NewElementProperties(properties);
    }


    /**
     * Link the certification type to the schema.
     *
     * @param dataQualityCertificationTypeGUID certification type
     * @param sandboxSchemaGUID database schema for the sandbox
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected void addGovernedByRelationship(String dataQualityCertificationTypeGUID,
                                             String sandboxSchemaGUID) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "addGovernedByRelationship";
        final String parameterName = "sandboxSchemaGUID";
        final String parameterName2 = "dataQualityCertificationTypeGUID";

        propertyHelper.validateGUID(sandboxSchemaGUID, parameterName, methodName);
        propertyHelper.validateGUID(dataQualityCertificationTypeGUID, parameterName2, methodName);

        this.createRelationshipIfAbsent(OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                        sandboxSchemaGUID,
                                        dataQualityCertificationTypeGUID,
                                        null);
    }


    /**
     * Create a process to represent the Airflow DAG that populates the sandbox.
     *
     * @param airflowDAGName name
     * @param topLevelProjectGUID unique identifier for the top level project - used as a search scope
     * @return guid
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected String createPopulateSandboxDAG(String airflowDAGName,
                                              String topLevelProjectGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        final String methodName = "createPopulateSandboxDAG";
        final String parameterName = "airflowDAGName";
        final String parameterName2 = "topLevelProjectGUID";

        propertyHelper.validateMandatoryName(airflowDAGName, parameterName, methodName);
        propertyHelper.validateGUID(topLevelProjectGUID, parameterName2, methodName);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Apache Airflow DAG:" + airflowDAGName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                      airflowDAGName);
        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                      DeployedImplementationType.AIRFLOW_DAG.getDeployedImplementationType());

        String existingDAGGUID = this.getExistingElementGUID(properties);

        if (existingDAGGUID != null)
        {
            return existingDAGGUID;
        }

        return governanceContext.getOpenMetadataStore().createMetadataElementInStore(DeployedImplementationType.AIRFLOW_DAG.getAssociatedTypeName(),
                                                                                     null,
                                                                                     null,
                                                                                     true,
                                                                                     Collections.singletonList(topLevelProjectGUID),
                                                                                     new NewElementProperties(properties),
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     false);
    }


    /**
     * Create the sandbox database schema - it is assumed that details of the schema are passed in the request parameters.
     *
     * @param topLevelProjectGUID unique identifier for the top level project - used as a search scope
     * @return guid
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException security error
     */
    protected String createSandboxSchema(String topLevelProjectGUID) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        final String methodName = "createSandboxSchema";
        final String parameterName = "topLevelProjectGUID";
        propertyHelper.validateGUID(topLevelProjectGUID, parameterName, methodName);

        String existingSchemaGUID = this.getExistingTemplatedElementGUID(PostgreSQLTemplateType.POSTGRES_SCHEMA_TEMPLATE.getTemplateGUID(),
                                                                         null,
                                                                         governanceContext.getRequestParameters());

        if (existingSchemaGUID != null)
        {
            return existingSchemaGUID;
        }

        return governanceContext.getOpenMetadataStore().createMetadataElementFromTemplate(OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                                                                          null,
                                                                                          true,
                                                                                          Collections.singletonList(topLevelProjectGUID),
                                                                                          null,
                                                                                          null,
                                                                                          PostgreSQLTemplateType.POSTGRES_SCHEMA_TEMPLATE.getTemplateGUID(),
                                                                                          null,
                                                                                          null,
                                                                                          governanceContext.getRequestParameters(),
                                                                                          null,
                                                                                          null,
                                                                                          null,
                                                                                          true);
    }
}
