/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.PortType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.util.List;
import java.util.Map;

/**
 * ProvisioningGovernanceContext provides access to details of the provisioning request along with basic access to the
 * metadata store.
 *
 * A provisioning service is typically using the requestSourceElements to provision new resources at the actionTargetElements.
 * It may also create the lineage to cover its provisioning work.
 *
 * The methods provided to create the lineage are simple.  They include an optional templateGUID to copy the structure of a more complex element
 * if needed.  Alternatively, the provisioning service can specify a guard on completion to initiate a remediation service to fill out the
 * lineage metadata in more detail.
 */
public class ProvisioningGovernanceContext extends GovernanceContext
{
    /**
     * Constructor sets up the key parameters for processing the request to the governance action service.
     *
     * @param userId calling user
     * @param governanceActionGUID unique identifier of the governance action that triggered this governance service
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance action service
     * @param requestSourceElements metadata elements associated with the request to the governance action service
     * @param actionTargetElements metadata elements that need to be worked on by the governance action service
     * @param openMetadataStore client to the metadata store for use by the governance action service
     */
    public ProvisioningGovernanceContext(String                     userId,
                                         String                     governanceActionGUID,
                                         String                     requestType,
                                         Map<String, String>        requestParameters,
                                         List<RequestSourceElement> requestSourceElements,
                                         List<ActionTargetElement>  actionTargetElements,
                                         OpenMetadataClient         openMetadataStore)
    {
        super(userId, governanceActionGUID, requestType, requestParameters, requestSourceElements, actionTargetElements, openMetadataStore);
    }


    /**
     * Pack the basic properties into an ElementProperties object.
     *
     * @param qualifiedName the unique name of the new metadata element
     * @param name the technical name of the metadata element
     * @param description the description of the metadata element
     * @return ElementProperties contain the supplied properties.
     * @throws InvalidParameterException null qualifiedName
     */
    private ElementProperties packBasicProperties(String qualifiedName,
                                                  String name,
                                                  String description,
                                                  String methodName) throws InvalidParameterException
    {
        final String qualifiedNamePropertyName = "qualifiedName";
        final String namePropertyName = "name";
        final String descriptionPropertyName = "description";

        propertyHelper.validateMandatoryName(qualifiedName, qualifiedNamePropertyName, methodName);

        ElementProperties properties = propertyHelper.addStringProperty(null, qualifiedNamePropertyName, qualifiedName);

        if (name != null)
        {
            properties = propertyHelper.addStringProperty(properties, namePropertyName, name);
        }

        if (description != null)
        {
            properties = propertyHelper.addStringProperty(properties, descriptionPropertyName, description);
        }

        return properties;
    }


    /**
     * Create an asset such as a data file, database, API or server.  This is used if the provisioning
     * governance action service has created a new asset as part of the provisioning process.
     * This interface includes an optional templateGUID to copy the structure of an existing asset of the same type.
     *
     * @param assetTypeName the type name of the asset.  This is the name of an open metadata type that inherits from "Asset".
     * @param qualifiedName the unique name of the new asset
     * @param name the technical display name of the asset
     * @param description the description of the asset
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an asset
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public String createAsset(String assetTypeName,
                              String qualifiedName,
                              String name,
                              String description) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String methodName = "createAsset";

        ElementProperties properties = packBasicProperties(qualifiedName, name, description, methodName);

        return openMetadataStore.createMetadataElementInStore(assetTypeName, ElementStatus.ACTIVE, null, null, properties, null);
    }


    /**
     * Create an asset such as a data file, database, API or server.  This is used if the provisioning
     * governance action service has created a new asset as part of the provisioning process.
     * This interface includes an optional templateGUID to copy the structure of an existing asset of the same type.
     *
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all of the attachments
     *                     such as nested content, schema, connection etc)
     * @param qualifiedName the unique name of the new asset
     * @param name the technical display name of the asset
     * @param description the description of the asset
     *
     * @return unique identifier of the new asset
     *
     * @throws InvalidParameterException the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create an asset
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public String createAssetFromTemplate(String templateGUID,
                                          String qualifiedName,
                                          String name,
                                          String description) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String methodName = "createAssetFromTemplate";

        ElementProperties properties = packBasicProperties(qualifiedName, name, description, methodName);

        return openMetadataStore.createMetadataElementInStore(null, ElementStatus.ACTIVE, null, null, properties, templateGUID);
    }


    /**
     * Create a new process to represent the processing of this governance action process.
     *
     * @param processTypeName the type name of the process.  This is the name of an open metadata type that inherits from "Process".
     * @param initialStatus status value of the process
     * @param qualifiedName the unique name of the new process
     * @param name the technical display name of the process
     * @param description the description of the process
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a process
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public String createProcess(String        processTypeName,
                                ElementStatus initialStatus,
                                String        qualifiedName,
                                String        name,
                                String        description) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String methodName = "createProcess";

        ElementProperties properties = packBasicProperties(qualifiedName, name, description, methodName);

        return openMetadataStore.createMetadataElementInStore(processTypeName,
                                                              initialStatus,
                                                              null,
                                                              null,
                                                              properties,
                                                              null);
    }


    /**
     * Create a new process to represent the processing of this governance action process.
     *
     * @param templateGUID the unique identifier of the existing process to copy (this will copy all of the attachments such as ports, nested content,
     *                     schema, connection etc)
     * @param initialStatus status value of the process
     * @param qualifiedName the unique name of the new process
     * @param name the technical display name of the process
     * @param description the description of the process
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a process
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public String createProcessFromTemplate(String        templateGUID,
                                            ElementStatus initialStatus,
                                            String        qualifiedName,
                                            String        name,
                                            String        description) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "createProcess";

        ElementProperties properties = packBasicProperties(qualifiedName, name, description, methodName);

        return openMetadataStore.createMetadataElementInStore(null,
                                                              initialStatus,
                                                              null,
                                                              null,
                                                              properties,
                                                              templateGUID);
    }


    /**
     * Create a process that represents the processing instance of this governance action.
     * @param processTypeName the type name of the process.  This is the name of an open metadata type that inherits from "Process".
     * @param initialStatus status value of the process
     * @param qualifiedName the unique name of the new process
     * @param name the technical display name of the process
     * @param description the description of the process
     * @param parentGUID the unique identifier of the existing process to copy (this will copy all of the attachments such as ports, nested content,
     *                     schema, connection etc)
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException the type name or qualified name is null or invalid
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a process
     * @throws PropertyServerException there is a problem connecting to the metadata store
     */
    public String createChildProcess(String        processTypeName,
                                     ElementStatus initialStatus,
                                     String        qualifiedName,
                                     String        name,
                                     String        description,
                                     String        parentGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "createProcess";

        ElementProperties properties = packBasicProperties(qualifiedName, name, description, methodName);

        String processGUID = openMetadataStore.createMetadataElementInStore(processTypeName,
                                                                            initialStatus,
                                                                            null,
                                                                            null,
                                                                            properties,
                                                                            null);

        if (processGUID != null)
        {
            ElementProperties relationshipProperties = propertyHelper.addEnumProperty(null,
                                                                                      "containmentType",
                                                                                      "ProcessContainmentType",
                                                                                      "OWNED");

            openMetadataStore.createRelatedElementsInStore("ProcessHierarchy",
                                                           parentGUID,
                                                           processGUID,
                                                           null,
                                                           null,
                                                           relationshipProperties);
        }

        return processGUID;
    }


    /**
     * Add a port to a process.
     *
     * @param processGUID unique identifier of the process
     * @param qualifiedName unique name for the port
     * @param displayName display name for the port
     * @param portType type of port (direction of data flow)
     * @param templateGUID optional unique identifier of a template port to copy
     *
     * @return unique identifier of the new port
     *
     * @throws InvalidParameterException the processGUID or qualified name is null or is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createPort(String   processGUID,
                             String   qualifiedName,
                             String   displayName,
                             PortType portType,
                             String   templateGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "createProcess";

        final String portTypeName        = "PortImplementation";
        final String processPortTypeName = "ProcessPort";

        final String displayNamePropertyName = "displayName";
        final String portTypePropertyName = "portType";
        final String portTypeTypeName = "PortType";

        final String processGUIDParameterName = "processGUID";

        propertyHelper.validateGUID(processGUID, processGUIDParameterName, methodName);

        ElementProperties properties = packBasicProperties(qualifiedName, null, null, methodName);

        if (displayName != null)
        {
            properties = propertyHelper.addStringProperty(properties, displayNamePropertyName, displayName);
        }

        if (portType != null)
        {
            properties = propertyHelper.addEnumProperty(properties, portTypePropertyName, portTypeTypeName, portType.getOpenTypeSymbolicName());
        }

        String portGUID = openMetadataStore.createMetadataElementInStore(portTypeName, ElementStatus.ACTIVE, null, null, properties, templateGUID);

        openMetadataStore.createRelatedElementsInStore(processPortTypeName, processGUID, portGUID, null, null, null);

        return portGUID;
    }


    /**
     * Create a lineage mapping relationship between a source and target element.  This could be between two assets, two process ports or
     * two schema elements.
     *
     * @param sourceElementGUID unique identifier of the element that describes the source of the data.
     * @param targetElementGUID unique identifier of the element that describes the destination of the data.
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException one of the GUIDs is null or is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createLineageMapping(String sourceElementGUID,
                                       String targetElementGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "createLineageMapping";
        final String lineageMappingTypeName = "LineageMapping";

        final String sourceElementGUIDParameterName = "sourceElementGUID";
        final String targetElementGUIDParameterName = "targetElementGUID";

        propertyHelper.validateGUID(sourceElementGUID, sourceElementGUIDParameterName, methodName);
        propertyHelper.validateGUID(targetElementGUID, targetElementGUIDParameterName, methodName);

        return openMetadataStore.createRelatedElementsInStore(lineageMappingTypeName, sourceElementGUID, targetElementGUID, null, null, null);
    }
}
