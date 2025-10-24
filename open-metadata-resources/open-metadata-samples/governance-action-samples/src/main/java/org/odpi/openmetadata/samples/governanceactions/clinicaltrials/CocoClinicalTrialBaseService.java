/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;


/**
 * Provides shared functions for the clinical trial services.
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
        final String methodName = "getEmailForPersonGUID";

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
        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                                              processGUID,
                                                                              actionTargetGUID,
                                                                              null,
                                                                              null,
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
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.DESIGN_STEP.name,
                                                                        this.getClass().getName());

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.ISC_QUALIFIED_NAME.name,
                                                      informationSupplyChainQualifiedName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.ROLE.name,
                                                      role);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                              solutionComponentGUID,
                                                                              implementationGUID,
                                                                              null,
                                                                              null,
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
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.ROLE.name,
                                                                        role);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                      description);

        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName,
                                                                              solutionComponentGUID,
                                                                              implementationGUID,
                                                                              null,
                                                                              null,
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
        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                                              consumerGUID,
                                                                              resourceGUID,
                                                                              null,
                                                                              null,
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
        governanceContext.getOpenMetadataStore().createRelatedElementsInStore(OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                                              sandboxSchemaGUID,
                                                                              dataQualityCertificationTypeGUID,
                                                                              null,
                                                                              null,
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
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        "Apache Airflow DAG:" + airflowDAGName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                      airflowDAGName);
        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                      DeployedImplementationType.AIRFLOW_DAG.getDeployedImplementationType());

        return governanceContext.getOpenMetadataStore().createMetadataElementInStore(DeployedImplementationType.AIRFLOW_DAG.getAssociatedTypeName(),
                                                                                     ElementStatus.ACTIVE,
                                                                                     null,
                                                                                     null,
                                                                                     true,
                                                                                     topLevelProjectGUID,
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
        return governanceContext.getOpenMetadataStore().createMetadataElementFromTemplate(OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                                                                          null,
                                                                                          true,
                                                                                          topLevelProjectGUID,
                                                                                          null,
                                                                                          null,
                                                                                          PostgreSQLTemplateType.POSTGRES_SCHEMA_TEMPLATE.getTemplateGUID(),
                                                                                          null,
                                                                                          governanceContext.getRequestParameters(),
                                                                                          null,
                                                                                          null,
                                                                                          null,
                                                                                          true);
    }
}
