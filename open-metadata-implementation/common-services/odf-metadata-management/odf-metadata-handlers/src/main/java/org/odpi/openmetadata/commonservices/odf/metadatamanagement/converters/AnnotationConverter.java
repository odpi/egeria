/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ElementHeaderConverter;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.AnnotationMapper;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.Map;


/**
 * AnnotationConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an Annotation bean.
 */
public class AnnotationConverter extends ElementHeaderConverter
{
    private EntityDetail         annotationEntity;
    private Relationship         annotationReviewLink;
    private EntityDetail         annotationReview;
    private OMRSRepositoryHelper repositoryHelper;
    private String               serviceName;

    /**
     * Constructor captures the initial content
     *
     * @param annotationEntity properties to convert
     * @param annotationReviewLink relationship from main entity to review entity
     * @param annotationReview more properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public AnnotationConverter(EntityDetail         annotationEntity,
                               Relationship         annotationReviewLink,
                               EntityDetail         annotationReview,
                               OMRSRepositoryHelper repositoryHelper,
                               String               serviceName)
    {
        super(annotationEntity, annotationReviewLink, repositoryHelper, serviceName);

        this.annotationEntity     = annotationEntity;
        this.annotationReviewLink = annotationReviewLink;
        this.annotationReview     = annotationReview;
        this.repositoryHelper     = repositoryHelper;
        this.serviceName          = serviceName;
    }


    /**
     * Set up the bean to convert.
     *
     * @param bean output bean
     * @return extended properties
     */
    Map<String,Object> updateBean(Annotation bean)
    {
        final String methodName = "updateBean";

        Map<String,Object> extendedProperties = new HashMap<>();

        if (annotationEntity != null)
        {
            super.updateBean(bean);

            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            InstanceProperties instanceProperties = entity.getProperties();
            Map<String,Object> elementExtendedProperties;

            if (instanceProperties != null)
            {
                bean.setAnnotationType(repositoryHelper.removeStringProperty(serviceName,
                                                                             AnnotationMapper.ANNOTATION_TYPE_PROPERTY_NAME,
                                                                             instanceProperties,
                                                                             methodName));
                bean.setSummary(repositoryHelper.removeStringProperty(serviceName,
                                                                      AnnotationMapper.SUMMARY_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));
                bean.setConfidenceLevel(repositoryHelper.removeIntProperty(serviceName,
                                                                           AnnotationMapper.CONFIDENCE_LEVEL_PROPERTY_NAME,
                                                                           instanceProperties,
                                                                           methodName));
                bean.setExpression(repositoryHelper.removeStringProperty(serviceName,
                                                                         AnnotationMapper.EXPRESSION_PROPERTY_NAME,
                                                                         instanceProperties,
                                                                         methodName));
                bean.setExplanation(repositoryHelper.removeStringProperty(serviceName,
                                                                         AnnotationMapper.EXPLANATION_PROPERTY_NAME,
                                                                         instanceProperties,
                                                                         methodName));
                bean.setAnalysisStep(repositoryHelper.removeStringProperty(serviceName,
                                                                           AnnotationMapper.ANALYSIS_STEP_PROPERTY_NAME,
                                                                           instanceProperties,
                                                                           methodName));
                bean.setJsonProperties(repositoryHelper.removeStringProperty(serviceName,
                                                                             AnnotationMapper.JSON_PROPERTIES_PROPERTY_NAME,
                                                                             instanceProperties,
                                                                             methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          AnnotationMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));

                elementExtendedProperties = repositoryHelper.getInstancePropertiesAsMap(instanceProperties);

                if (elementExtendedProperties != null)
                {
                    extendedProperties.putAll(elementExtendedProperties);
                }
            }

            if (annotationReviewLink != null)
            {
                instanceProperties = annotationReviewLink.getProperties();

                bean.setAnnotationStatus(this.removeAnnotationStatusFromProperties(instanceProperties));

                elementExtendedProperties = repositoryHelper.getInstancePropertiesAsMap(instanceProperties);

                if (elementExtendedProperties != null)
                {
                    extendedProperties.putAll(elementExtendedProperties);
                }

                if (annotationReview != null)
                {
                    instanceProperties = annotationReviewLink.getProperties();

                    bean.setReviewDate(repositoryHelper.removeDateProperty(serviceName,
                                                                           AnnotationMapper.REVIEW_DATE_PROPERTY_NAME,
                                                                           instanceProperties,
                                                                           methodName));
                    bean.setSteward(repositoryHelper.removeStringProperty(serviceName,
                                                                          AnnotationMapper.STEWARD_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                    bean.setReviewComment(repositoryHelper.removeStringProperty(serviceName,
                                                                                AnnotationMapper.COMMENT_PROPERTY_NAME,
                                                                                instanceProperties,
                                                                                methodName));

                    elementExtendedProperties = repositoryHelper.getInstancePropertiesAsMap(instanceProperties);

                    if (elementExtendedProperties != null)
                    {
                        extendedProperties.putAll(elementExtendedProperties);
                    }
                }
            }
        }

        if (! extendedProperties.isEmpty())
        {
            return extendedProperties;
        }
        else
        {
            return null;
        }
    }


    /**
     * Request the bean is extracted from the repository elements passed on the constructor.
     *
     * @return output bean
     */
    public Annotation getBean()
    {
        Annotation      bean = null;

        if (annotationEntity != null)
        {
            bean = new Annotation();
            bean.setExtendedProperties(this.updateBean(bean));
        }

        return bean;
    }


    /**
     * Retrieve the AnnotationStatus enum property from the instance properties of the Annotation Review Link relationship.
     *
     * @param properties  entity properties
     * @return   enum value
     */
    private AnnotationStatus removeAnnotationStatusFromProperties(InstanceProperties   properties)
    {
        AnnotationStatus requestStatus = AnnotationStatus.UNKNOWN_STATUS;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(AnnotationMapper.ANNOTATION_STATUS_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
                {
                    case 0:
                        requestStatus = AnnotationStatus.NEW_ANNOTATION;
                        break;

                    case 1:
                        requestStatus = AnnotationStatus.REVIEWED_ANNOTATION;
                        break;

                    case 2:
                        requestStatus = AnnotationStatus.APPROVED_ANNOTATION;
                        break;

                    case 3:
                        requestStatus = AnnotationStatus.ACTIONED_ANNOTATION;
                        break;

                    case 4:
                        requestStatus = AnnotationStatus.INVALID_ANNOTATION;
                        break;

                    case 5:
                        requestStatus = AnnotationStatus.IGNORE_ANNOTATION;
                        break;

                    case 98:
                        requestStatus = AnnotationStatus.OTHER_STATUS;
                        break;

                    default:
                        requestStatus = AnnotationStatus.UNKNOWN_STATUS;
                        break;
                }

                instancePropertiesMap.remove(AnnotationMapper.ANNOTATION_STATUS_PROPERTY_NAME);

                properties.setInstanceProperties(instancePropertiesMap);
            }
        }

        return requestStatus;
    }
}
