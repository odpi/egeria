/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.AnnotationMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * SuspectDuplicateAnnotationBuilder supports the creation of the entities and
 * relationships that describe an Open Discovery Framework (ODF) SuspectDuplicateAnnotation.
 */
public class SuspectDuplicateAnnotationBuilder extends AnnotationBuilder
{
    protected List<String>         duplicateAnchorGUIDs;
    protected List<String>         matchingPropertyNames;
    protected List<String>         matchingClassificationNames;
    protected List<String>         matchingAttachmentGUIDs;
    protected List<String>         matchingRelationshipGUIDs;


    /**
     * Create a builder to convert the properties of the SuspectDuplicateAnnotation bean into repository services instances.
     *
     * @param annotationType type of annotation
     * @param summary summary of the annotation
     * @param confidenceLevel how confident was discovery service that the results are correct
     * @param expression expression that summarizes the results
     * @param explanation explanation of the results
     * @param analysisStep analysis step in the discovery service that produced this annotation
     * @param jsonProperties JSON properties passed to discovery service
     * @param duplicateAnchorGUIDs the unique identifiers of the matching assets
     * @param matchingPropertyNames the property names that match
     * @param matchingClassificationNames the classification names that match
     * @param matchingAttachmentGUIDs the unique identifiers of matching attachments
     * @param matchingRelationshipGUIDs the unique identifiers of matching relationships
     * @param additionalProperties additional properties
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SuspectDuplicateAnnotationBuilder(String               annotationType,
                                             String               summary,
                                             int                  confidenceLevel,
                                             String               expression,
                                             String               explanation,
                                             String               analysisStep,
                                             String               jsonProperties,
                                             List<String>         duplicateAnchorGUIDs,
                                             List<String>         matchingPropertyNames,
                                             List<String>         matchingClassificationNames,
                                             List<String>         matchingAttachmentGUIDs,
                                             List<String>         matchingRelationshipGUIDs,
                                             Map<String, String>  additionalProperties,
                                             OMRSRepositoryHelper repositoryHelper,
                                             String               serviceName,
                                             String               serverName)
    {
        super(annotationType,
              summary,
              confidenceLevel,
              expression,
              explanation,
              analysisStep,
              jsonProperties,
              additionalProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.duplicateAnchorGUIDs        = duplicateAnchorGUIDs;
        this.matchingPropertyNames       = matchingPropertyNames;
        this.matchingClassificationNames = matchingClassificationNames;
        this.matchingAttachmentGUIDs     = matchingAttachmentGUIDs;
        this.matchingRelationshipGUIDs   = matchingRelationshipGUIDs;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for the annotation entity.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getAnnotationInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getAnnotationInstanceProperties(methodName);

        if (duplicateAnchorGUIDs != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           AnnotationMapper.DUPLICATE_ANCHOR_GUIDS_PROPERTY_NAME,
                                                                           duplicateAnchorGUIDs,
                                                                           methodName);
        }

        if (matchingPropertyNames != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AnnotationMapper.MATCHING_PROPERTY_NAMES_PROPERTY_NAME,
                                                                      matchingPropertyNames,
                                                                      methodName);
        }

        if (matchingClassificationNames != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           AnnotationMapper.MATCHING_CLASSIFICATION_NAMES_PROPERTY_NAME,
                                                                           matchingClassificationNames,
                                                                           methodName);
        }

        if (matchingAttachmentGUIDs != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           AnnotationMapper.MATCHING_ATTACHMENT_GUIDS_PROPERTY_NAME,
                                                                           matchingAttachmentGUIDs,
                                                                           methodName);
        }

        if (matchingRelationshipGUIDs != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           AnnotationMapper.MATCHING_RELATIONSHIP_GUIDS_PROPERTY_NAME,
                                                                           matchingRelationshipGUIDs,
                                                                           methodName);
        }

        return properties;
    }
}
