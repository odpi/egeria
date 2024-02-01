/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.context;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ArchiveProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.integration.reports.IntegrationReportWriter;

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
     * @param reportWriter report writer (maybe null)
     */
    public OpenMetadataAccess(OpenMetadataClient      openMetadataClient,
                              String                  userId,
                              String                  externalSourceGUID,
                              String                  externalSourceName,
                              IntegrationReportWriter reportWriter)
    {
        super(openMetadataClient, userId, externalSourceGUID, externalSourceName);

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
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc.)
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
                                               ElementProperties properties,
                                               String            templateGUID) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        String metadataElementGUID = super.createMetadataElementInStore(metadataElementTypeName,
                                                                        initialStatus,
                                                                        effectiveFrom,
                                                                        effectiveTo,
                                                                        properties,
                                                                        templateGUID);

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
     * @param effectiveFrom the date when this element is active - null for active on creation
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param properties properties of the new metadata element
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc.)
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
                                               Date                           effectiveFrom,
                                               Date                           effectiveTo,
                                               ElementProperties              properties,
                                               String                         templateGUID,
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
                                                                        effectiveFrom,
                                                                        effectiveTo,
                                                                        properties,
                                                                        templateGUID,
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
}