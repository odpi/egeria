/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileRequestParameter;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.governanceaction.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode;
import org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * Retrieve the email for a specific person.
     *
     * @param personGUID unique identifier of the person entity for the individual
     * @return email address for the individual; or null if it is not found
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem access the metadata repository/server
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
                                                                            OpenMetadataProperty.NAME.name,
                                                                            person.getElementProperties(),
                                                                            methodName);

        List<RelatedMetadataElement> contactDetails = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(personGUID,
                                                                                                                          1,
                                                                                                                          OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                                                                                                          0,
                                                                                                                          0);
        if (contactDetails != null)
        {
            for (RelatedMetadataElement contactDetail : contactDetails)
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
        List<RelatedMetadataElement> projects = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(certificationTypeGUID,
                                                                                                                    1,
                                                                                                                    OpenMetadataType.GOVERNED_BY_TYPE_NAME,
                                                                                                                    projectStartFrom,
                                                                                                                    governanceContext.getMaxPageSize());
        while (projects != null)
        {
            for (RelatedMetadataElement project : projects)
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
                                                                                           OpenMetadataType.GOVERNED_BY_TYPE_NAME,
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
     * Create the governance action process asset.
     *
     * @param processQualifiedName new qualified name for the process
     * @param processName new name for the process
     * @param processDescription new description for the process
     * @return unique identifier of new governance action process
     * @throws InvalidParameterException parameter error
     * @throws PropertyServerException repository error
     * @throws UserNotAuthorizedException authorization error
     */
    protected String createGovernanceActionProcess(String processQualifiedName,
                                                   String processName,
                                                   String processDescription) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        ElementProperties processProperties = propertyHelper.addStringProperty(null,
                                                                               OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                               processQualifiedName);

        processProperties = propertyHelper.addStringProperty(processProperties,
                                                             OpenMetadataProperty.NAME.name,
                                                             processName);

        processProperties = propertyHelper.addStringProperty(processProperties,
                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                             processDescription);

        return governanceContext.getOpenMetadataStore().createMetadataElementInStore(OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                                                                                     ElementStatus.ACTIVE,
                                                                                     null,
                                                                                     null,
                                                                                     true,
                                                                                     null,
                                                                                     null,
                                                                                     processProperties,
                                                                                     null,
                                                                                     null,
                                                                                     null,
                                                                                     false);
    }
}
