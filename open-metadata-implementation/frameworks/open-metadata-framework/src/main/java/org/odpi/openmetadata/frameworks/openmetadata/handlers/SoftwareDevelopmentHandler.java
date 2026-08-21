/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwaredevelopment.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * SoftwareDevelopmentHandler provides methods to maintain the relationships and classifications that
 * describe how software is built: which techniques were reused, which components depend on which, and
 * which software assets were generated from which components.
 */
public class SoftwareDevelopmentHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName    name of this server (view server)
     * @param auditLog           logging destination
     * @param localServiceName   local service name
     * @param openMetadataClient access to open metadata
     */
    public SoftwareDevelopmentHandler(String             localServerName,
                                      AuditLog           auditLog,
                                      String             localServiceName,
                                      OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.RUNNABLE_SOFTWARE_COMPONENT.typeName);
    }

    /**
     * Attach an element to the reusable technique that it makes use of.
     *
     * @param userId                 userId of the user making the request
     * @param techniqueUserGUID unique identifier of the element at end 1
     * @param reusableTechniqueGUID unique identifier of the element at end 2
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkReusableTechniqueUse(String            userId,
                           String            techniqueUserGUID,
                           String            reusableTechniqueGUID,
                           MakeAnchorOptions makeAnchorOptions,
                           ReusableTechniqueUseProperties relationshipProperties) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName            = "linkReusableTechniqueUse";
        final String end1GUIDParameterName = "techniqueUserGUID";
        final String end2GUIDParameterName = "reusableTechniqueGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(techniqueUserGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(reusableTechniqueGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.REUSABLE_TECHNIQUE_USE_RELATIONSHIP.typeName,
                                                        techniqueUserGUID,
                                                        reusableTechniqueGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an element from a reusable technique that it no longer makes use of.
     *
     * @param userId                 userId of the user making the request.
     * @param techniqueUserGUID unique identifier of the element at end 1
     * @param reusableTechniqueGUID unique identifier of the element at end 2
     * @param deleteOptions          options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachReusableTechniqueUse(String        userId,
                             String        techniqueUserGUID,
                             String        reusableTechniqueGUID,
                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName            = "detachReusableTechniqueUse";
        final String end1GUIDParameterName = "techniqueUserGUID";
        final String end2GUIDParameterName = "reusableTechniqueGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(techniqueUserGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(reusableTechniqueGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.REUSABLE_TECHNIQUE_USE_RELATIONSHIP.typeName,
                                                        techniqueUserGUID,
                                                        reusableTechniqueGUID,
                                                        deleteOptions);
    }

    /**
     * Attach a runnable software component to a component that it needs in order to execute.
     *
     * @param userId                 userId of the user making the request
     * @param dependentComponentGUID unique identifier of the element at end 1
     * @param dependedOnComponentGUID unique identifier of the element at end 2
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDependentSoftwareComponent(String            userId,
                           String            dependentComponentGUID,
                           String            dependedOnComponentGUID,
                           MakeAnchorOptions makeAnchorOptions,
                           DependentSoftwareComponentProperties relationshipProperties) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName            = "linkDependentSoftwareComponent";
        final String end1GUIDParameterName = "dependentComponentGUID";
        final String end2GUIDParameterName = "dependedOnComponentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dependentComponentGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dependedOnComponentGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DEPENDENT_SOFTWARE_COMPONENT_RELATIONSHIP.typeName,
                                                        dependentComponentGUID,
                                                        dependedOnComponentGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a runnable software component from a component that it no longer needs in order to execute.
     *
     * @param userId                 userId of the user making the request.
     * @param dependentComponentGUID unique identifier of the element at end 1
     * @param dependedOnComponentGUID unique identifier of the element at end 2
     * @param deleteOptions          options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDependentSoftwareComponent(String        userId,
                             String        dependentComponentGUID,
                             String        dependedOnComponentGUID,
                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName            = "detachDependentSoftwareComponent";
        final String end1GUIDParameterName = "dependentComponentGUID";
        final String end2GUIDParameterName = "dependedOnComponentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dependentComponentGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dependedOnComponentGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DEPENDENT_SOFTWARE_COMPONENT_RELATIONSHIP.typeName,
                                                        dependentComponentGUID,
                                                        dependedOnComponentGUID,
                                                        deleteOptions);
    }

    /**
     * Attach a software asset to the software component that it is built from.
     *
     * @param userId                 userId of the user making the request
     * @param assetGUID unique identifier of the element at end 1
     * @param softwareComponentGUID unique identifier of the element at end 2
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSoftwareSource(String            userId,
                           String            assetGUID,
                           String            softwareComponentGUID,
                           MakeAnchorOptions makeAnchorOptions,
                           SoftwareSourceProperties relationshipProperties) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        final String methodName            = "linkSoftwareSource";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "softwareComponentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(softwareComponentGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOFTWARE_SOURCE_RELATIONSHIP.typeName,
                                                        assetGUID,
                                                        softwareComponentGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a software asset from the software component that it was built from.
     *
     * @param userId                 userId of the user making the request.
     * @param assetGUID unique identifier of the element at end 1
     * @param softwareComponentGUID unique identifier of the element at end 2
     * @param deleteOptions          options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSoftwareSource(String        userId,
                             String        assetGUID,
                             String        softwareComponentGUID,
                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName            = "detachSoftwareSource";
        final String end1GUIDParameterName = "assetGUID";
        final String end2GUIDParameterName = "softwareComponentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(softwareComponentGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOFTWARE_SOURCE_RELATIONSHIP.typeName,
                                                        assetGUID,
                                                        softwareComponentGUID,
                                                        deleteOptions);
    }

    /**
     * Classify an element to say that it is the generated output of a build program or script.
     *
     * @param userId                userId of the user making the request.
     * @param elementGUID           unique identifier of the element
     * @param properties            properties for the classification
     * @param metadataSourceOptions options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setElementAsGeneratedTarget(String                userId,
                                   String                elementGUID,
                                   GeneratedTargetProperties properties,
                                   MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName        = "setElementAsGeneratedTarget";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.GENERATED_TARGET_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the generated target designation from an element.
     *
     * @param userId                userId of the user making the request.
     * @param elementGUID           unique identifier of the element
     * @param metadataSourceOptions options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearElementAsGeneratedTarget(String                userId,
                                     String                elementGUID,
                                     MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName        = "clearElementAsGeneratedTarget";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.GENERATED_TARGET_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }

    /**
     * Classify an element to say that it is a technique that can be reused in multiple contexts.
     *
     * @param userId                userId of the user making the request.
     * @param elementGUID           unique identifier of the element
     * @param properties            properties for the classification
     * @param metadataSourceOptions options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void setElementAsReusableTechnique(String                userId,
                                   String                elementGUID,
                                   ReusableTechniqueProperties properties,
                                   MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName        = "setElementAsReusableTechnique";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          elementGUID,
                                                          OpenMetadataType.REUSABLE_TECHNIQUE_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the reusable technique designation from an element.
     *
     * @param userId                userId of the user making the request.
     * @param elementGUID           unique identifier of the element
     * @param metadataSourceOptions options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void clearElementAsReusableTechnique(String                userId,
                                     String                elementGUID,
                                     MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName        = "clearElementAsReusableTechnique";
        final String guidParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            elementGUID,
                                                            OpenMetadataType.REUSABLE_TECHNIQUE_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }
}
