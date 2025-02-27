/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.surveyaction.properties.AnnotationStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;
import java.util.Map;

/**
 * AnnotationReviewBuilder supports the creation of the entities and
 * relationships that describe an Governance Action Framework (GAF) AnnotationReview and the link to the
 * annotation.
 */
public class AnnotationReviewBuilder extends OpenMetadataAPIGenericBuilder
{
    private final int                  annotationStatus;
    private final Date                 reviewDate;
    private final String               steward;
    private final String               reviewComment;


    /**
     * Create a builder to convert the properties of the annotation review bean into repository services instances.
     *
     * @param annotationStatus status of annotation
     * @param reviewDate date annotation reviewed
     * @param steward name of steward
     * @param reviewComment comments from the steward
     * @param typeGUID type GUID to use for the entity
     * @param typeName type name to use for the entity
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public AnnotationReviewBuilder(int                  annotationStatus,
                                   Date                 reviewDate,
                                   String               steward,
                                   String               reviewComment,
                                   String               typeGUID,
                                   String               typeName,
                                   Map<String, Object>  extendedProperties,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        super(typeGUID,
              typeName,
              extendedProperties,
              null,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.annotationStatus       = annotationStatus;
        this.reviewDate             = reviewDate;
        this.steward                = steward;
        this.reviewComment          = reviewComment;
        this.extendedProperties     = extendedProperties;
        this.repositoryHelper       = repositoryHelper;
        this.serviceName            = serviceName;
        this.serverName             = serverName;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException no support for AnnotationStatus Enum
     */
    public InstanceProperties getReviewLinkInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = null;

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    null,
                                                                    OpenMetadataProperty.ANNOTATION_STATUS.name,
                                                                    AnnotationStatus.getOpenTypeGUID(),
                                                                    AnnotationStatus.getOpenTypeName(),
                                                                    annotationStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, AnnotationStatus.getOpenTypeName());
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName)
    {
        InstanceProperties properties = new InstanceProperties();

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.REVIEW_DATE.name,
                                                                reviewDate,
                                                                methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.STEWARD.name,
                                                                  steward,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.COMMENT.name,
                                                                  reviewComment,
                                                                  methodName);

        return properties;
    }
}
