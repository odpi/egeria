/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.context;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.integration.reports.IntegrationReportWriter;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ArchiveProperties;

import java.util.Date;
import java.util.Map;

/**
 * OpenMetadataAccess provides an interface to the open metadata store.  This is part of the Governance Action Framework (GAF)
 * and provides a comprehensive interface for working with all types of metadata, subject to the user's (and this OMAS's) security permissions.
 * The interface supports search, maintenance of metadata elements, classifications and relationships.
 */
public class OpenMetadataAccess extends OpenMetadataStore
{

    private final IntegrationReportWriter reportWriter;


    /**
     * The constructor needs an implementation of the open metadata store.
     *
     * @param openMetadataClient client implementation
     * @param userId calling user
     * @param externalSourceGUID unique identifier for external source (or null)
     * @param externalSourceName unique name for external source (or null)
     * @param originatorGUID unique identifier of the source of the to do
     * @param reportWriter report writer (maybe null)
     */
    public OpenMetadataAccess(OpenMetadataClient      openMetadataClient,
                              String                  userId,
                              String                  externalSourceGUID,
                              String                  externalSourceName,
                              String                  originatorGUID,
                              IntegrationReportWriter reportWriter)
    {
        super(openMetadataClient, userId, externalSourceGUID, externalSourceName, originatorGUID);

        this.reportWriter = reportWriter;
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param properties properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        String metadataElementGUID = super.createMetadataElementInStore(metadataElementTypeName,
                                                                        initialStatus,
                                                                        properties);

        if ((metadataElementGUID != null) && (reportWriter != null))
        {
            reportWriter.reportElementCreation(metadataElementGUID);
        }

        return metadataElementGUID;
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String            metadataElementTypeName,
                                               ElementStatus     initialStatus,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        String metadataElementGUID = super.createMetadataElementInStore(metadataElementTypeName,
                                                                        initialStatus,
                                                                        effectiveFrom,
                                                                        effectiveTo,
                                                                        properties);

        if ((metadataElementGUID != null) && (reportWriter != null))
        {
            reportWriter.reportElementCreation(metadataElementGUID);
        }

        return metadataElementGUID;
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String                         metadataElementTypeName,
                                               ElementStatus                  initialStatus,
                                               Map<String, ElementProperties> initialClassifications,
                                               String                         anchorGUID,
                                               boolean                        isOwnAnchor,
                                               Date                           effectiveFrom,
                                               Date                           effectiveTo,
                                               ElementProperties              properties,
                                               String                         parentGUID,
                                               String                         parentRelationshipTypeName,
                                               ElementProperties              parentRelationshipProperties,
                                               boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        String metadataElementGUID = super.createMetadataElementInStore(metadataElementTypeName,
                                                                        initialStatus,
                                                                        initialClassifications,
                                                                        anchorGUID,
                                                                        isOwnAnchor,
                                                                        effectiveFrom,
                                                                        effectiveTo,
                                                                        properties,
                                                                        parentGUID,
                                                                        parentRelationshipTypeName,
                                                                        parentRelationshipProperties,
                                                                        parentAtEnd1);

        if ((metadataElementGUID != null) && (reportWriter != null))
        {
            reportWriter.reportElementCreation(metadataElementGUID);
        }

        return metadataElementGUID;
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param initialStatus initial status of the metadata element
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementInStore(String                         externalSourceGUID,
                                               String                         externalSourceName,
                                               String                         metadataElementTypeName,
                                               ElementStatus                  initialStatus,
                                               Map<String, ElementProperties> initialClassifications,
                                               String                         anchorGUID,
                                               boolean                        isOwnAnchor,
                                               Date                           effectiveFrom,
                                               Date                           effectiveTo,
                                               ElementProperties              properties,
                                               String                         parentGUID,
                                               String                         parentRelationshipTypeName,
                                               ElementProperties              parentRelationshipProperties,
                                               boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        String metadataElementGUID = super.createMetadataElementInStore(externalSourceGUID,
                                                                        externalSourceName,
                                                                        metadataElementTypeName,
                                                                        initialStatus,
                                                                        initialClassifications,
                                                                        anchorGUID,
                                                                        isOwnAnchor,
                                                                        effectiveFrom,
                                                                        effectiveTo,
                                                                        properties,
                                                                        parentGUID,
                                                                        parentRelationshipTypeName,
                                                                        parentRelationshipProperties,
                                                                        parentAtEnd1);

        if ((metadataElementGUID != null) && (reportWriter != null))
        {
            reportWriter.reportElementCreation(metadataElementGUID);
        }

        return metadataElementGUID;
    }


    /**
     * Create a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the placeholder values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementFromTemplate(String                         metadataElementTypeName,
                                                    String                         anchorGUID,
                                                    boolean                        isOwnAnchor,
                                                    Date                           effectiveFrom,
                                                    Date                           effectiveTo,
                                                    String                         templateGUID,
                                                    ElementProperties              replacementProperties,
                                                    Map<String, String>            placeholderProperties,
                                                    String                         parentGUID,
                                                    String                         parentRelationshipTypeName,
                                                    ElementProperties              parentRelationshipProperties,
                                                    boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        String metadataElementGUID = super.createMetadataElementFromTemplate(metadataElementTypeName,
                                                                             anchorGUID,
                                                                             isOwnAnchor,
                                                                             effectiveFrom,
                                                                             effectiveTo,
                                                                             templateGUID,
                                                                             replacementProperties,
                                                                             placeholderProperties,
                                                                             parentGUID,
                                                                             parentRelationshipTypeName,
                                                                             parentRelationshipProperties,
                                                                             parentAtEnd1);

        if ((metadataElementGUID != null) && (reportWriter != null))
        {
            reportWriter.reportElementCreation(metadataElementGUID);
        }

        return metadataElementGUID;
    }


    /**
     * Create a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the placeholder values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createMetadataElementFromTemplate(String                         externalSourceGUID,
                                                    String                         externalSourceName,
                                                    String                         metadataElementTypeName,
                                                    String                         anchorGUID,
                                                    boolean                        isOwnAnchor,
                                                    Date                           effectiveFrom,
                                                    Date                           effectiveTo,
                                                    String                         templateGUID,
                                                    ElementProperties              replacementProperties,
                                                    Map<String, String>            placeholderProperties,
                                                    String                         parentGUID,
                                                    String                         parentRelationshipTypeName,
                                                    ElementProperties              parentRelationshipProperties,
                                                    boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        String metadataElementGUID = super.createMetadataElementFromTemplate(externalSourceGUID,
                                                                             externalSourceName,
                                                                             metadataElementTypeName,
                                                                             anchorGUID,
                                                                             isOwnAnchor,
                                                                             effectiveFrom,
                                                                             effectiveTo,
                                                                             templateGUID,
                                                                             replacementProperties,
                                                                             placeholderProperties,
                                                                             parentGUID,
                                                                             parentRelationshipTypeName,
                                                                             parentRelationshipProperties,
                                                                             parentAtEnd1);

        if ((metadataElementGUID != null) && (reportWriter != null))
        {
            reportWriter.reportElementCreation(metadataElementGUID);
        }

        return metadataElementGUID;
    }


    /**
     * Create/get a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the placeholder values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String getMetadataElementFromTemplate(String                         metadataElementTypeName,
                                                 String                         anchorGUID,
                                                 boolean                        isOwnAnchor,
                                                 Date                           effectiveFrom,
                                                 Date                           effectiveTo,
                                                 String                         templateGUID,
                                                 ElementProperties              replacementProperties,
                                                 Map<String, String>            placeholderProperties,
                                                 String                         parentGUID,
                                                 String                         parentRelationshipTypeName,
                                                 ElementProperties              parentRelationshipProperties,
                                                 boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        String metadataElementGUID = super.getMetadataElementFromTemplate(metadataElementTypeName,
                                                                          anchorGUID,
                                                                          isOwnAnchor,
                                                                          effectiveFrom,
                                                                          effectiveTo,
                                                                          templateGUID,
                                                                          replacementProperties,
                                                                          placeholderProperties,
                                                                          parentGUID,
                                                                          parentRelationshipTypeName,
                                                                          parentRelationshipProperties,
                                                                          parentAtEnd1);

        if ((metadataElementGUID != null) && (reportWriter != null))
        {
            reportWriter.reportElementCreation(metadataElementGUID);
        }

        return metadataElementGUID;
    }


    /**
     * Create/get a new metadata element in the metadata store using the template identified by the templateGUID.
     * The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * The template and any similar anchored objects are
     * copied in this process.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementTypeName type name of the new metadata element
     * @param anchorGUID unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     *                   or the Anchors classification is included in the initial classifications.
     * @param isOwnAnchor boolean flag to day that the element should be classified as its own anchor once its element
     *                    is created in the repository.
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the placeholder values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentGUID unique identifier of optional parent entity
     * @param parentRelationshipTypeName type of relationship to connect the new element to the parent
     * @param parentRelationshipProperties properties to include in parent relationship
     * @param parentAtEnd1 which end should the parent GUID go in the relationship
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name, status or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String getMetadataElementFromTemplate(String                         externalSourceGUID,
                                                 String                         externalSourceName,
                                                 String                         metadataElementTypeName,
                                                 String                         anchorGUID,
                                                 boolean                        isOwnAnchor,
                                                 Date                           effectiveFrom,
                                                 Date                           effectiveTo,
                                                 String                         templateGUID,
                                                 ElementProperties              replacementProperties,
                                                 Map<String, String>            placeholderProperties,
                                                 String                         parentGUID,
                                                 String                         parentRelationshipTypeName,
                                                 ElementProperties              parentRelationshipProperties,
                                                 boolean                        parentAtEnd1) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        String metadataElementGUID = super.getMetadataElementFromTemplate(externalSourceGUID,
                                                                          externalSourceName,
                                                                          metadataElementTypeName,
                                                                          anchorGUID,
                                                                          isOwnAnchor,
                                                                          effectiveFrom,
                                                                          effectiveTo,
                                                                          templateGUID,
                                                                          replacementProperties,
                                                                          placeholderProperties,
                                                                          parentGUID,
                                                                          parentRelationshipTypeName,
                                                                          parentRelationshipProperties,
                                                                          parentAtEnd1);

        if ((metadataElementGUID != null) && (reportWriter != null))
        {
            reportWriter.reportElementCreation(metadataElementGUID);
        }

        return metadataElementGUID;
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementInStore(String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        super.updateMetadataElementInStore(metadataElementGUID, replaceProperties, properties);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the metadata element
     *
     * @throws InvalidParameterException either the unique identifier or the properties are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementInStore(String            externalSourceGUID,
                                             String            externalSourceName,
                                             String            metadataElementGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        super.updateMetadataElementInStore(externalSourceGUID,
                                           externalSourceName,
                                           metadataElementGUID,
                                           replaceProperties,
                                           properties);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param newElementStatus new status value - or null to leave as is
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementStatusInStore(String        metadataElementGUID,
                                                   ElementStatus newElementStatus) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        super.updateMetadataElementStatusInStore(metadataElementGUID, newElementStatus);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param newElementStatus new status value - or null to leave as is
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementStatusInStore(String        externalSourceGUID,
                                                   String        externalSourceName,
                                                   String        metadataElementGUID,
                                                   ElementStatus newElementStatus) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        super.updateMetadataElementStatusInStore(externalSourceGUID,
                                                 externalSourceName,
                                                 metadataElementGUID,
                                                 newElementStatus);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementEffectivityInStore(String        metadataElementGUID,
                                                        Date          effectiveFrom,
                                                        Date          effectiveTo) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        super.updateMetadataElementEffectivityInStore(metadataElementGUID,
                                                      effectiveFrom,
                                                      effectiveTo);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateMetadataElementEffectivityInStore(String        externalSourceGUID,
                                                        String        externalSourceName,
                                                        String        metadataElementGUID,
                                                        Date          effectiveFrom,
                                                        Date          effectiveTo) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        super.updateMetadataElementEffectivityInStore(externalSourceGUID,
                                                      externalSourceName,
                                                      metadataElementGUID,
                                                      effectiveFrom,
                                                      effectiveTo);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Delete a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to delete
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteMetadataElementInStore(String  metadataElementGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        super.deleteMetadataElementInStore(metadataElementGUID);

        if (reportWriter != null)
        {
            reportWriter.reportElementDelete(metadataElementGUID);
        }
    }


    /**
     * Delete a specific metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteMetadataElementInStore(String externalSourceGUID,
                                             String externalSourceName,
                                             String metadataElementGUID) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        super.deleteMetadataElementInStore(externalSourceGUID,
                                           externalSourceName,
                                           metadataElementGUID);

        if (reportWriter != null)
        {
            reportWriter.reportElementDelete(metadataElementGUID);
        }
    }


    /**
     * Archive a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to archive
     * @param archiveProperties details of the archive process
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to archive this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void archiveMetadataElementInStore(String            metadataElementGUID,
                                              ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        super.archiveMetadataElementInStore(metadataElementGUID, archiveProperties);

        if (reportWriter != null)
        {
            reportWriter.reportElementDelete(metadataElementGUID);
        }
    }


    /**
     * Archive a specific metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param archiveProperties description of the archiving process
     *
     * @throws InvalidParameterException the unique identifier is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void archiveMetadataElementInStore(String            externalSourceGUID,
                                              String            externalSourceName,
                                              String            metadataElementGUID,
                                              ArchiveProperties archiveProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        super.archiveMetadataElementInStore(externalSourceGUID,
                                            externalSourceName,
                                            metadataElementGUID,
                                            archiveProperties);

        if (reportWriter != null)
        {
            reportWriter.reportElementDelete(metadataElementGUID);
        }
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param effectiveFrom the date when this classification is active - null for active now
     * @param effectiveTo the date when this classification becomes inactive - null for active until deleted
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String            metadataElementGUID,
                                               String            classificationName,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        super.classifyMetadataElementInStore(metadataElementGUID,
                                             classificationName,
                                             effectiveFrom,
                                             effectiveTo,
                                             properties);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String            externalSourceGUID,
                                               String            externalSourceName,
                                               String            metadataElementGUID,
                                               String            classificationName,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        super.classifyMetadataElementInStore(externalSourceGUID,
                                             externalSourceName,
                                             metadataElementGUID,
                                             classificationName,
                                             properties);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param effectiveFrom the date when this classification is active - null for active now
     * @param effectiveTo the date when this classification becomes inactive - null for active until deleted
     * @param properties properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void classifyMetadataElementInStore(String            externalSourceGUID,
                                               String            externalSourceName,
                                               String            metadataElementGUID,
                                               String            classificationName,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        super.classifyMetadataElementInStore(externalSourceGUID,
                                             externalSourceName,
                                             metadataElementGUID,
                                             classificationName,
                                             effectiveFrom,
                                             effectiveTo,
                                             properties);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void reclassifyMetadataElementInStore(String            metadataElementGUID,
                                                 String            classificationName,
                                                 boolean           replaceProperties,
                                                 ElementProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        super.reclassifyMetadataElementInStore(metadataElementGUID,
                                               classificationName,
                                               replaceProperties,
                                               properties);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the classification
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void reclassifyMetadataElementInStore(String            externalSourceGUID,
                                                 String            externalSourceName,
                                                 String            metadataElementGUID,
                                                 String            classificationName,
                                                 boolean           replaceProperties,
                                                 ElementProperties properties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        super.reclassifyMetadataElementInStore(externalSourceGUID,
                                               externalSourceName,
                                               metadataElementGUID,
                                               classificationName,
                                               replaceProperties,
                                               properties);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateClassificationEffectivityInStore(String  metadataElementGUID,
                                                       String  classificationName,
                                                       Date    effectiveFrom,
                                                       Date    effectiveTo) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        super.updateClassificationEffectivityInStore(metadataElementGUID,
                                                     classificationName,
                                                     effectiveFrom,
                                                     effectiveTo);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateClassificationEffectivityInStore(String  externalSourceGUID,
                                                       String  externalSourceName,
                                                       String  metadataElementGUID,
                                                       String  classificationName,
                                                       Date    effectiveFrom,
                                                       Date    effectiveTo) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        super.updateClassificationEffectivityInStore(externalSourceGUID,
                                                     externalSourceName,
                                                     metadataElementGUID,
                                                     classificationName,
                                                     effectiveFrom,
                                                     effectiveTo);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     *
     * @throws InvalidParameterException the unique identifier or classification name is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to remove this classification
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void declassifyMetadataElementInStore(String  metadataElementGUID,
                                                 String  classificationName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        super.declassifyMetadataElementInStore(metadataElementGUID, classificationName);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(metadataElementGUID);
        }
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createRelatedElementsInStore(String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        String relationshipGUID = super.createRelatedElementsInStore(relationshipTypeName,
                                                                     metadataElement1GUID,
                                                                     metadataElement2GUID,
                                                                     effectiveFrom,
                                                                     effectiveTo,
                                                                     properties);

        if ((relationshipGUID != null) && (reportWriter != null))
        {
            reportWriter.reportElementCreation(relationshipGUID);
        }

        return relationshipGUID;
    }




    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties the properties of the relationship
     *
     * @return unique identifier of the new relationship
     *
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String createRelatedElementsInStore(String            externalSourceGUID,
                                               String            externalSourceName,
                                               String            relationshipTypeName,
                                               String            metadataElement1GUID,
                                               String            metadataElement2GUID,
                                               Date              effectiveFrom,
                                               Date              effectiveTo,
                                               ElementProperties properties) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        String relationshipGUID = super.createRelatedElementsInStore(externalSourceGUID,
                                                                     externalSourceName,
                                                                     relationshipTypeName,
                                                                     metadataElement1GUID,
                                                                     metadataElement2GUID,
                                                                     effectiveFrom,
                                                                     effectiveTo,
                                                                     properties);

        if ((relationshipGUID != null) && (reportWriter != null))
        {
            reportWriter.reportElementCreation(relationshipGUID);
        }

        return relationshipGUID;
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the relationship
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsInStore(String            relationshipGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        super.updateRelatedElementsInStore(relationshipGUID, replaceProperties, properties);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(relationshipGUID);
        }
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param relationshipGUID unique identifier of the relationship to update
     * @param replaceProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param properties new properties for the relationship
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsInStore(String            externalSourceGUID,
                                             String            externalSourceName,
                                             String            relationshipGUID,
                                             boolean           replaceProperties,
                                             ElementProperties properties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        super.updateRelatedElementsInStore(externalSourceGUID,
                                           externalSourceName,
                                           relationshipGUID,
                                           replaceProperties,
                                           properties);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(relationshipGUID);
        }
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param relationshipGUID unique identifier of the relationship to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsEffectivityInStore(String  relationshipGUID,
                                                        Date    effectiveFrom,
                                                        Date    effectiveTo) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        super.updateRelatedElementsEffectivityInStore(relationshipGUID, effectiveFrom, effectiveTo);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(relationshipGUID);
        }
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param relationshipGUID unique identifier of the relationship to update
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     *
     * @throws InvalidParameterException either the unique identifier or the status are invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to update this element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void updateRelatedElementsEffectivityInStore(String  externalSourceGUID,
                                                        String  externalSourceName,
                                                        String  relationshipGUID,
                                                        Date    effectiveFrom,
                                                        Date    effectiveTo) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        super.updateRelatedElementsEffectivityInStore(externalSourceGUID,
                                                      externalSourceName,
                                                      relationshipGUID,
                                                      effectiveFrom,
                                                      effectiveTo);

        if (reportWriter != null)
        {
            reportWriter.reportElementUpdate(relationshipGUID);
        }
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param relationshipGUID unique identifier of the relationship to delete
    *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteRelatedElementsInStore(String  relationshipGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        super.deleteRelatedElementsInStore(relationshipGUID);

        if (reportWriter != null)
        {
            reportWriter.reportElementDelete(relationshipGUID);
        }
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param externalSourceGUID      unique identifier of the software capability that owns this collection
     * @param externalSourceName      unique name of the software capability that owns this collection
     * @param relationshipGUID unique identifier of the relationship to delete
     *
     * @throws InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     * @throws UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public void deleteRelatedElementsInStore(String  externalSourceGUID,
                                             String  externalSourceName,
                                             String  relationshipGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        super.deleteRelatedElementsInStore(externalSourceGUID,
                                           externalSourceName,
                                           relationshipGUID);

        if (reportWriter != null)
        {
            reportWriter.reportElementDelete(relationshipGUID);
        }
    }
}