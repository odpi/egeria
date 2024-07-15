/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.samples.governanceactions.clinicaltrials;

import org.odpi.openmetadata.frameworks.governanceaction.controls.ActionTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * ActionTarget provides some standard action type names to use in the implementation of governance services.
 * Using standard action target names helps to simplify the use of the governance services in
 * governance action processes.
 */
public enum CocoClinicalTrialActionTarget
{
    /**
     * Project used to control the clinical trial.
     */
    PROJECT("clinicalTrialProject",
            "Project used to control the clinical trial.",
            OpenMetadataType.PROJECT.typeName,
            null,
            true),


    /**
     * Project used to control the clinical trial.
     */
    HOSPITAL("hospital",
            "Hospital that will be supplying data for the clinical trial.",
            OpenMetadataType.ORGANIZATION_TYPE_NAME,
            null,
            true),

    /**
     * Schema where the weekly measurements volume is to reside.
     */
    SCHEMA("dataLakeSchema",
           "Schema where the weekly measurements volume is to reside.",
           OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
           null,
           true),

    /**
     * Template used to catalog the new volume.
     */
    VOLUME_TEMPLATE("volumeTemplate",
                    "Template used to catalog the new volume.",
                    DeployedImplementationType.OSS_UC_VOLUME.getAssociatedTypeName(),
                    DeployedImplementationType.OSS_UC_VOLUME.getDeployedImplementationType(),
                    true),

    /**
     * Template used to catalog the landing area folder for a particular hospital.
     */
    HOSPITAL_TEMPLATE("hospitalTemplate",
                      "Template used to catalog the landing area folder for a particular hospital.",
                      OpenMetadataType.FILE_FOLDER.typeName,
                      null,
                      true),

    /**
     * Folder describing the root directory for the data lake.
     */
    ROOT_FOLDER("dataLakeRootFolder",
                "Folder describing the root directory for the data lake.",
                OpenMetadataType.FILE_FOLDER.typeName,
                null,
                true),

    /**
     * Folder describing the place where new files will be stored by the hospital.
     */
    LANDING_AREA_FOLDER("landingAreaFolder",
                "Folder describing the place where new files will be stored by the hospital.",
                OpenMetadataType.FILE_FOLDER.typeName,
                null,
                true),

    /**
     * Integration connector that maintains the last update date for the volume.
     */
    LAST_UPDATE_CONNECTOR("lastUpdateConnector",
                    "Integration connector that maintains the last update date for the volume.",
                    DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                    null,
                    false),

    /**
     * Integration connector that maintains the last update date for the volume.
     */
    LANDING_AREA_CONNECTOR("landingAreaConnector",
                          "Integration connector that maintains the last update date for the volume.",
                          DeployedImplementationType.INTEGRATION_CONNECTOR.getAssociatedTypeName(),
                          null,
                          true),


    /**
     * The process that should run to provision files from the landing area into the data lake.
     */
    NEW_ELEMENT_PROCESS("newElementProcess",
                        "The process that should run to provision files from the landing area into the data lake.",
                        OpenMetadataType.GOVERNANCE_ACTION_PROCESS_TYPE_NAME,
                        null,
                        true),


    /**
     * The person who can be contacted if there are problems with the data from this hospital.
     */
    CONTACT_PERSON("contactPerson",
                        "The person who can be contacted if there are problems with the data from this hospital.",
                        OpenMetadataType.PERSON_TYPE_NAME,
                        null,
                        true),

    ;

    /**
     * Action target name
     */
    public final String name;

    /**
     * Description of the action target.
     */
    public final String description;

    /**
     * The open metadata type name of the element that can be this type of action target.
     */
    public final String typeName;

    /**
     * The deployed implementation type allows the service to be more specific about the resources it works with.
     */
    private final String deployedImplementationType;

    /**
     * Is this ActionTarget required for the service to work successfully.
     */
    private final boolean required;


    /**
     * Constructor for the enum.
     *
     * @param name ActionTargetName
     * @param description description of action target
     * @param typeName Open Metadata Type Name for action target
     * @param deployedImplementationType optional deployed implementation type
     * @param required Is this ActionTarget required for the service to work successfully.
     */
    CocoClinicalTrialActionTarget(String  name,
                                  String  description,
                                  String  typeName,
                                  String  deployedImplementationType,
                                  boolean required)
    {
        this.name                       = name;
        this.description                = description;
        this.typeName                   = typeName;
        this.deployedImplementationType = deployedImplementationType;
        this.required                   = required;
    }


    /**
     * Return all the action targets defined in this enum.
     *
     * @return list
     */
    public static List<ActionTargetType> getHospitalActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        actionTargetTypes.add(PROJECT.getActionTargetType());
        actionTargetTypes.add(HOSPITAL.getActionTargetType());
        actionTargetTypes.add(HOSPITAL_TEMPLATE.getActionTargetType());
        actionTargetTypes.add(LANDING_AREA_CONNECTOR.getActionTargetType());
        actionTargetTypes.add(LANDING_AREA_FOLDER.getActionTargetType());
        actionTargetTypes.add(NEW_ELEMENT_PROCESS.getActionTargetType());
        actionTargetTypes.add(CONTACT_PERSON.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return all the action targets defined in this enum.
     *
     * @return list
     */
    public static List<ActionTargetType> getSetUpDataLakeActionTargetTypes()
    {
        List<ActionTargetType> actionTargetTypes = new ArrayList<>();

        actionTargetTypes.add(PROJECT.getActionTargetType());
        actionTargetTypes.add(SCHEMA.getActionTargetType());
        actionTargetTypes.add(ROOT_FOLDER.getActionTargetType());
        actionTargetTypes.add(VOLUME_TEMPLATE.getActionTargetType());
        actionTargetTypes.add(LAST_UPDATE_CONNECTOR.getActionTargetType());

        return actionTargetTypes;
    }


    /**
     * Return an action target type for use in the governance action service's provider.
     *
     * @return action target type
     */
    public ActionTargetType getActionTargetType()
    {
        ActionTargetType actionTargetType = new ActionTargetType();

        actionTargetType.setName(name);
        actionTargetType.setDescription(description);
        actionTargetType.setTypeName(typeName);
        actionTargetType.setDeployedImplementationType(deployedImplementationType);
        actionTargetType.setRequired(required);

        return actionTargetType;
    }


    /**
     * Return the name of the action target.
     *
     * @return string name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the action target.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the type name of the element that is linked to for this type of action target.
     *
     * @return type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the preferred value of the deployed implementation type of the element that is linked to for
     * this type of action target.  This is typically only set for assets.
     *
     * @return preferred value string
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ActionTarget{name='" + name + "}";
    }
}
