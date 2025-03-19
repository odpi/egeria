/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.archiveutilities.governanceengines;

import org.odpi.openmetadata.archiveutilities.openconnectors.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CertificationTypeDefinition;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.ProjectDefinition;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.CocoClinicalTrialActionTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines the request types for the governance engines that identify which governance service to call
 * for a specific request type.
 */
public enum CocoRequestTypeDefinition
{
    /**
     * set-up-data-lake
     */
    SET_UP_DATA_LAKE("set-up-data-lake",
                     null,
                     null,
                     null,
                     CocoGovernanceEngineDefinition.CLINICAL_TRIALS_ENGINE,
                     CocoGovernanceServiceDefinition.SET_UP_DATA_LAKE,
                     "4c33571b-043b-4fba-9b4f-ada7cd8910d3",
                     ProjectDefinition.CLINICAL_TRIALS.getQualifiedName()),


    /**
     * nominate-hospital
     */
    NOMINATE_HOSPITAL("nominate-hospital",
                     null,
                     null,
                     null,
                     CocoGovernanceEngineDefinition.CLINICAL_TRIALS_ENGINE,
                     CocoGovernanceServiceDefinition.NOMINATE_HOSPITAL,
                      "25f1f005-d051-4f78-a56a-7b94eda114aa",
                     ProjectDefinition.CLINICAL_TRIALS.getQualifiedName()),


    /**
     * certify-hospital
     */
    CERTIFY_HOSPITAL("certify-hospital",
                      null,
                      null,
                      null,
                      CocoGovernanceEngineDefinition.CLINICAL_TRIALS_ENGINE,
                      CocoGovernanceServiceDefinition.CERTIFY_HOSPITAL,
                     "12746ea1-750b-43ef-b6d8-74c145c0d18c",
                      ProjectDefinition.CLINICAL_TRIALS.getQualifiedName()),


    /**
     * onboard-hospital
     */
    ONBOARD_HOSPITAL("onboard-hospital",
                     null,
                     null,
                     null,
                     CocoGovernanceEngineDefinition.CLINICAL_TRIALS_ENGINE,
                     CocoGovernanceServiceDefinition.HOSPITAL_ONBOARDING,
                     "7d12e715-53c6-4c33-bc05-7db9156056c8",
                     ProjectDefinition.CLINICAL_TRIALS.getQualifiedName()),

    /**
     * check-weekly-measurements-data-quality
     */
    CHECK_DATA("check-weekly-measurements-data-quality",
                     null,
                     null,
                     null,
                     CocoGovernanceEngineDefinition.ASSET_QUALITY_ENGINE,
                     CocoGovernanceServiceDefinition.WEEKLY_MEASUREMENTS_DATA_QUALITY,
                     "7494e350-1478-491a-90c1-e22856a47372",
                     ProjectDefinition.CLINICAL_TRIALS.getQualifiedName()),


    /**
     * set-up-clinical-trial
     */
    SET_UP_CLINICAL_TRIAL("set-up-clinical-trial",
                          null,
                          null,
                          getSetUpActionTargets(),
                          CocoGovernanceEngineDefinition.CLINICAL_TRIALS_ENGINE,
                          CocoGovernanceServiceDefinition.SET_UP_CLINICAL_TRIAL,
                          "23ceab08-f644-49c7-b7f3-95f39fe41c84",
                          ProjectDefinition.CLINICAL_TRIALS.getQualifiedName()),

    /**
     * simulate-ftp
     */
    SIMULATE_FTP("simulate-ftp",
                 "copy-file",
                 getNoLineageRequestParameter(),
                 null,
                 CocoGovernanceEngineDefinition.CLINICAL_TRIALS_ENGINE,
                 CocoGovernanceServiceDefinition.FILE_PROVISIONER,
                 "286599f7-8f05-4378-84f0-7e9af5dfad7f",
                 ProjectDefinition.CLINICAL_TRIALS.getQualifiedName()),

    ;

    static Map<String, String> getNoLineageRequestParameter()
    {
        final String noLineagePropertyName = "noLineage";

        Map<String, String> requestParameters = new HashMap<>();

        requestParameters.put(noLineagePropertyName, "");

        return requestParameters;
    }

    static List<NewActionTarget> getSetUpActionTargets()
    {
        List<NewActionTarget> actionTargetList = new ArrayList<>();

        NewActionTarget newActionTarget = new NewActionTarget();

        newActionTarget.setActionTargetName(CocoClinicalTrialActionTarget.HOSPITAL_CERTIFICATION_TYPE.getName());
        newActionTarget.setActionTargetGUID(CertificationTypeDefinition.DROP_FOOT_APPROVED_HOSPITAL.getGUID());

        actionTargetList.add(newActionTarget);

        newActionTarget = new NewActionTarget();

        newActionTarget.setActionTargetName(CocoClinicalTrialActionTarget.DATA_QUALITY_CERTIFICATION_TYPE.getName());
        newActionTarget.setActionTargetGUID(CertificationTypeDefinition.DROP_FOOT_APPROVED_DATA.getGUID());

        actionTargetList.add(newActionTarget);

        newActionTarget = new NewActionTarget();

        newActionTarget.setActionTargetName(CocoClinicalTrialActionTarget.GENERIC_SET_UP_DATA_LAKE_GAT.getName());
        newActionTarget.setActionTargetGUID(SET_UP_DATA_LAKE.governanceActionTypeGUID);

        actionTargetList.add(newActionTarget);

        newActionTarget = new NewActionTarget();

        newActionTarget.setActionTargetName(CocoClinicalTrialActionTarget.GENERIC_HOSPITAL_NOMINATION_GAT.getName());
        newActionTarget.setActionTargetGUID(NOMINATE_HOSPITAL.governanceActionTypeGUID);

        actionTargetList.add(newActionTarget);

        newActionTarget = new NewActionTarget();

        newActionTarget.setActionTargetName(CocoClinicalTrialActionTarget.GENERIC_HOSPITAL_CERTIFICATION_GAT.getName());
        newActionTarget.setActionTargetGUID(CERTIFY_HOSPITAL.governanceActionTypeGUID);

        actionTargetList.add(newActionTarget);

        newActionTarget = new NewActionTarget();

        newActionTarget.setActionTargetName(CocoClinicalTrialActionTarget.GENERIC_HOSPITAL_ONBOARDING_GAT.getName());
        newActionTarget.setActionTargetGUID(ONBOARD_HOSPITAL.governanceActionTypeGUID);

        actionTargetList.add(newActionTarget);

        newActionTarget = new NewActionTarget();

        newActionTarget.setActionTargetName(CocoClinicalTrialActionTarget.LAST_UPDATE_CONNECTOR.getName());
        newActionTarget.setActionTargetGUID(IntegrationConnectorDefinition.MAINTAIN_LAST_UPDATE_CATALOGUER.getGUID());

        actionTargetList.add(newActionTarget);

        newActionTarget = new NewActionTarget();

        newActionTarget.setActionTargetName(CocoClinicalTrialActionTarget.LANDING_AREA_CONNECTOR.getName());
        newActionTarget.setActionTargetGUID(IntegrationConnectorDefinition.GENERAL_FOLDER_CATALOGUER.getGUID());

        actionTargetList.add(newActionTarget);

        return actionTargetList;
    }


    private final String                          governanceRequestType;
    private final String                          serviceRequestType;
    private final Map<String, String>             requestParameters;
    private final List<NewActionTarget>           actionTargets;
    private final CocoGovernanceEngineDefinition  governanceEngine;
    private final CocoGovernanceServiceDefinition governanceService;
    private final String                          governanceActionTypeGUID;
    private final String                          supportedElementQualifiedName;



    /**
     * Return the request type enum value.
     *
     * @param governanceRequestType request type used by the caller
     * @param serviceRequestType option map to a request type known by the service
     * @param requestParameters pre-defined request parameters
     * @param actionTargets predefined action targets (for governance action type)
     * @param governanceEngine governance engine that supports this request type
     * @param governanceService governance service that implements this request type
     * @param governanceActionTypeGUID unique identifier of the governance action type
     * @param supportedElementQualifiedName element supported by this call
     */
    CocoRequestTypeDefinition(String                          governanceRequestType,
                              String                          serviceRequestType,
                              Map<String, String>             requestParameters,
                              List<NewActionTarget>           actionTargets,
                              CocoGovernanceEngineDefinition  governanceEngine,
                              CocoGovernanceServiceDefinition governanceService,
                              String                          governanceActionTypeGUID,
                              String                          supportedElementQualifiedName)
    {
        this.governanceRequestType         = governanceRequestType;
        this.serviceRequestType            = serviceRequestType;
        this.requestParameters             = requestParameters;
        this.actionTargets                 = actionTargets;
        this.governanceEngine              = governanceEngine;
        this.governanceService             = governanceService;
        this.governanceActionTypeGUID      = governanceActionTypeGUID;
        this.supportedElementQualifiedName = supportedElementQualifiedName;
    }


    /**
     * Return the Request Type.
     *
     * @return string
     */
    public String getGovernanceRequestType()
    {
        return governanceRequestType;
    }


    /**
     * Return the service request type to map to.
     *
     * @return string
     */
    public String getServiceRequestType()
    {
        return serviceRequestType;
    }


    /**
     * Return the request parameters (if needed).
     *
     * @return map or null
     */
    public Map<String, String> getRequestParameters()
    {
        return requestParameters;
    }


    /**
     * Return predefined action targets used by this service.  They are attached to the governance action type.
     *
     * @return list
     */
    public List<NewActionTarget> getActionTargets()
    {
        return actionTargets;
    }


    /**
     * Return the governance engine where this request type belongs to.
     *
     * @return governance engine definition enum
     */
    public CocoGovernanceEngineDefinition getGovernanceEngine()
    {
        return governanceEngine;
    }


    /**
     * Return the governance service that this request type maps to,
     *
     * @return governance service definition enum
     */
    public CocoGovernanceServiceDefinition getGovernanceService()
    {
        return governanceService;
    }


    /**
     * Return the unique identifier of the governance action type.
     *
     * @return string
     */
    public String getGovernanceActionTypeGUID()
    {
        return governanceActionTypeGUID;
    }


    /**
     * Return the element that is supported by this request.
     *
     * @return qualified name string
     */
    public String getSupportedElementQualifiedName()
    {
        return supportedElementQualifiedName;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "RequestTypeDefinition{" + "name='" + governanceRequestType + '\'' + "}";
    }
}
