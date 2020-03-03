/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.AnnotationMapper;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.Map;

/**
 * AnnotationReviewBuilder supports the creation of the entities and
 * relationships that describe an Governance Action Framework (GAF) AnnotationReview and the link to the
 * annotation.
 */
public class AnnotationReviewBuilder
{
    protected AnnotationStatus     annotationStatus;
    protected Date                 reviewDate;
    protected String               steward;
    protected String               reviewComment;
    protected Map<String, String>  additionalProperties;
    protected Map<String, Object>  extendedProperties;
    protected OMRSRepositoryHelper repositoryHelper;
    protected String               serviceName;
    protected String               serverName;


    /**
     * Create a builder to convert the properties of the annotation review bean into repository services instances.
     *
     * @param annotationStatus status of annotation
     * @param reviewDate date annotation reviewed
     * @param steward name of steward
     * @param reviewComment comments from the steward
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public AnnotationReviewBuilder(AnnotationStatus     annotationStatus,
                                   Date                 reviewDate,
                                   String               steward,
                                   String               reviewComment,
                                   Map<String, String>  additionalProperties,
                                   Map<String, Object>  extendedProperties,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName,
                                   String               serverName)
    {
        this.annotationStatus       = annotationStatus;
        this.reviewDate             = reviewDate;
        this.steward                = steward;
        this.reviewComment          = reviewComment;
        this.additionalProperties   = additionalProperties;
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
     */
    public InstanceProperties getReviewLinkInstanceProperties(String  methodName)
    {
        InstanceProperties properties = new InstanceProperties();

        if (annotationStatus != null)
        {
            properties = this.addAnnotationStatusToProperties(properties, methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getAnnotationReviewInstanceProperties(String  methodName)
    {
        InstanceProperties properties = new InstanceProperties();

        if (reviewDate != null)
        {
            properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                    properties,
                                                                    AnnotationMapper.REVIEW_DATE_PROPERTY_NAME,
                                                                    reviewDate,
                                                                    methodName);
        }

        if (steward != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AnnotationMapper.STEWARD_PROPERTY_NAME,
                                                                      steward,
                                                                      methodName);
        }

        if (reviewComment != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      AnnotationMapper.COMMENT_PROPERTY_NAME,
                                                                      reviewComment,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Add the AnnotationStatus enum to the properties.
     *
     * @param properties current properties
     * @param methodName calling method
     * @return updated properties
     */
    protected InstanceProperties addAnnotationStatusToProperties(InstanceProperties properties,
                                                                 String             methodName)
    {
        InstanceProperties resultingProperties = properties;

        switch (annotationStatus)
        {
            case NEW_ANNOTATION:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 AnnotationMapper.ANNOTATION_STATUS_PROPERTY_NAME,
                                                                                 0,
                                                                                 "New",
                                                                                 "The annotation is new.",
                                                                                 methodName);
                break;

            case REVIEWED_ANNOTATION:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 AnnotationMapper.ANNOTATION_STATUS_PROPERTY_NAME,
                                                                                 1,
                                                                                 "Reviewed",
                                                                                 "The annotation has been reviewed by a steward.",
                                                                                 methodName);
                break;

            case APPROVED_ANNOTATION:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 AnnotationMapper.ANNOTATION_STATUS_PROPERTY_NAME,
                                                                                 2,
                                                                                 "Approved",
                                                                                 "The annotation has been approved.",
                                                                                 methodName);
                break;

            case ACTIONED_ANNOTATION:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 AnnotationMapper.ANNOTATION_STATUS_PROPERTY_NAME,
                                                                                 3,
                                                                                 "Actioned",
                                                                                 "The request has been actioned.",
                                                                                 methodName);
                break;

            case INVALID_ANNOTATION:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 AnnotationMapper.ANNOTATION_STATUS_PROPERTY_NAME,
                                                                                 4,
                                                                                 "Invalid",
                                                                                 "The annotation is invalid or incorrect.",
                                                                                 methodName);
                break;

            case IGNORE_ANNOTATION:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 AnnotationMapper.ANNOTATION_STATUS_PROPERTY_NAME,
                                                                                 5,
                                                                                 "Ignore",
                                                                                 "The annotation should be ignored.",
                                                                                 methodName);
                break;

            case OTHER_STATUS:
                resultingProperties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                                 resultingProperties,
                                                                                 AnnotationMapper.ANNOTATION_STATUS_PROPERTY_NAME,
                                                                                 99,
                                                                                 "Other",
                                                                                 "Another status.",
                                                                                 methodName);
                break;
        }

        return resultingProperties;
    }
}
