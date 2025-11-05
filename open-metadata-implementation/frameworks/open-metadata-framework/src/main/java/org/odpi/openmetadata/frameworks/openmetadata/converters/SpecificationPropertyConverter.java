/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.enums.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.reports.ConnectorActivityReportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.SpecificationPropertyAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.SpecificationPropertyValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.reports.ConnectorActivityReport;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * SpecificationPropertyConverter provides common methods for transferring relevant properties from an
 * Open Metadata Element object into an appropriate Specification Properties bean.
 */
public class SpecificationPropertyConverter<B> extends OpenMetadataConverterBase<B>
{
    /**
     * Constructor
     *
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SpecificationPropertyConverter(PropertyHelper propertyHelper,
                                          String         serviceName,
                                          String         serverName)
    {
        super(propertyHelper, serviceName, serverName);
    }


    /**
     * Return a specification property by extracting information from the retrieved related element.
     *
     * @param relatedSpecificationProperty retrieved related element
     * @return subclass of specification property
     */
    public SpecificationProperty getSpecificationProperty(RelatedMetadataElementSummary relatedSpecificationProperty)
    {
        if (relatedSpecificationProperty != null)
        {
            if (relatedSpecificationProperty.getRelationshipProperties() instanceof SpecificationPropertyAssignmentProperties specificationPropertyAssignmentProperties)
            {
                String propertyName = specificationPropertyAssignmentProperties.getPropertyName();

                if (relatedSpecificationProperty.getRelatedElement().getProperties() instanceof SpecificationPropertyValueProperties properties)
                {
                    if (SpecificationPropertyType.REPLACEMENT_ATTRIBUTE.getPropertyType().equals(propertyName))
                    {
                        ReplacementAttribute specificationProperty = new ReplacementAttribute();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.REPLACEMENT_ATTRIBUTE);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setDataType(properties.getDataType());

                        Map<String, String> otherPropertyValues = new HashMap<>();

                        if (properties.getAdditionalProperties() != null)
                        {
                            for (String additionalPropertyName : properties.getAdditionalProperties().keySet())
                            {
                                if (additionalPropertyName != null)
                                {
                                    if (additionalPropertyName.equals(OpenMetadataProperty.EXAMPLE.name))
                                    {
                                        specificationProperty.setExample(properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                    else if (additionalPropertyName.equals(OpenMetadataProperty.REQUIRED.name))
                                    {
                                        specificationProperty.setRequired(Boolean.getBoolean(properties.getAdditionalProperties().get(additionalPropertyName)));
                                    }
                                    else
                                    {
                                        otherPropertyValues.put(additionalPropertyName, properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                }
                            }
                        }

                        if (! otherPropertyValues.isEmpty())
                        {
                            specificationProperty.setOtherPropertyValues(otherPropertyValues);
                        }

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.PLACEHOLDER_PROPERTY.getPropertyType().equals(propertyName))
                    {
                        PlaceholderProperty specificationProperty = new PlaceholderProperty();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.PLACEHOLDER_PROPERTY);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setDataType(properties.getDataType());

                        Map<String, String> otherPropertyValues = new HashMap<>();

                        if (properties.getAdditionalProperties() != null)
                        {
                            for (String additionalPropertyName : properties.getAdditionalProperties().keySet())
                            {
                                if (additionalPropertyName != null)
                                {
                                    if (additionalPropertyName.equals(OpenMetadataProperty.EXAMPLE.name))
                                    {
                                        specificationProperty.setExample(properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                    else if (additionalPropertyName.equals(OpenMetadataProperty.REQUIRED.name))
                                    {
                                        specificationProperty.setRequired(Boolean.getBoolean(properties.getAdditionalProperties().get(additionalPropertyName)));
                                    }
                                    else
                                    {
                                        otherPropertyValues.put(additionalPropertyName, properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                }
                            }
                        }

                        if (! otherPropertyValues.isEmpty())
                        {
                            specificationProperty.setOtherPropertyValues(otherPropertyValues);
                        }

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.SUPPORTED_TEMPLATE.getPropertyType().equals(propertyName))
                    {
                        SupportedTemplate specificationProperty = new SupportedTemplate();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.SUPPORTED_TEMPLATE);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setOpenMetadataTypeName(properties.getDataType());

                        Map<String, String> otherPropertyValues = new HashMap<>();

                        if (properties.getAdditionalProperties() != null)
                        {
                            for (String additionalPropertyName : properties.getAdditionalProperties().keySet())
                            {
                                if (additionalPropertyName != null)
                                {
                                    if (additionalPropertyName.equals(OpenMetadataProperty.REQUIRED.name))
                                    {
                                        specificationProperty.setRequired(Boolean.getBoolean(properties.getAdditionalProperties().get(additionalPropertyName)));
                                    }
                                    else
                                    {
                                        otherPropertyValues.put(additionalPropertyName, properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                }
                            }
                        }

                        if (! otherPropertyValues.isEmpty())
                        {
                            specificationProperty.setOtherPropertyValues(otherPropertyValues);
                        }

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.SUPPORTED_CONFIGURATION_PROPERTY.getPropertyType().equals(propertyName))
                    {
                        SupportedConfigurationProperty specificationProperty = new SupportedConfigurationProperty();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.SUPPORTED_CONFIGURATION_PROPERTY);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setDataType(properties.getDataType());

                        Map<String, String> otherPropertyValues = new HashMap<>();

                        if (properties.getAdditionalProperties() != null)
                        {
                            for (String additionalPropertyName : properties.getAdditionalProperties().keySet())
                            {
                                if (additionalPropertyName != null)
                                {
                                    if (additionalPropertyName.equals(OpenMetadataProperty.EXAMPLE.name))
                                    {
                                        specificationProperty.setExample(properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                    else if (additionalPropertyName.equals(OpenMetadataProperty.REQUIRED.name))
                                    {
                                        specificationProperty.setRequired(Boolean.getBoolean(properties.getAdditionalProperties().get(additionalPropertyName)));
                                    }
                                    else
                                    {
                                        otherPropertyValues.put(additionalPropertyName, properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                }
                            }
                        }

                        if (! otherPropertyValues.isEmpty())
                        {
                            specificationProperty.setOtherPropertyValues(otherPropertyValues);
                        }

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.SUPPORTED_REQUEST_TYPE.getPropertyType().equals(propertyName))
                    {
                        SupportedRequestType specificationProperty = new SupportedRequestType();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.SUPPORTED_REQUEST_TYPE);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setOtherPropertyValues(properties.getAdditionalProperties());

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.SUPPORTED_REQUEST_PARAMETER.getPropertyType().equals(propertyName))
                    {
                        SupportedRequestParameter specificationProperty = new SupportedRequestParameter();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.SUPPORTED_REQUEST_PARAMETER);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setDataType(properties.getDataType());

                        Map<String, String> otherPropertyValues = new HashMap<>();

                        if (properties.getAdditionalProperties() != null)
                        {
                            for (String additionalPropertyName : properties.getAdditionalProperties().keySet())
                            {
                                if (additionalPropertyName != null)
                                {
                                    if (additionalPropertyName.equals(OpenMetadataProperty.EXAMPLE.name))
                                    {
                                        specificationProperty.setExample(properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                    else if (additionalPropertyName.equals(OpenMetadataProperty.REQUIRED.name))
                                    {
                                        specificationProperty.setRequired(Boolean.getBoolean(properties.getAdditionalProperties().get(additionalPropertyName)));
                                    }
                                    else
                                    {
                                        otherPropertyValues.put(additionalPropertyName, properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                }
                            }
                        }

                        if (! otherPropertyValues.isEmpty())
                        {
                            specificationProperty.setOtherPropertyValues(otherPropertyValues);
                        }

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.SUPPORTED_ACTION_TARGET.getPropertyType().equals(propertyName))
                    {
                        SupportedActionTarget specificationProperty = new SupportedActionTarget();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.SUPPORTED_ACTION_TARGET);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setOpenMetadataTypeName(properties.getDataType());

                        Map<String, String> otherPropertyValues = new HashMap<>();

                        if (properties.getAdditionalProperties() != null)
                        {
                            for (String additionalPropertyName : properties.getAdditionalProperties().keySet())
                            {
                                if (additionalPropertyName != null)
                                {
                                    if (additionalPropertyName.equals(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name))
                                    {
                                        specificationProperty.setDeployedImplementationType(properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                    else if (additionalPropertyName.equals(OpenMetadataProperty.REQUIRED.name))
                                    {
                                        specificationProperty.setRequired(Boolean.getBoolean(properties.getAdditionalProperties().get(additionalPropertyName)));
                                    }
                                    else
                                    {
                                        otherPropertyValues.put(additionalPropertyName, properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                }
                            }
                        }

                        if (! otherPropertyValues.isEmpty())
                        {
                            specificationProperty.setOtherPropertyValues(otherPropertyValues);
                        }

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.SUPPORTED_ANALYSIS_STEP.getPropertyType().equals(propertyName))
                    {
                        SupportedAnalysisStep specificationProperty = new SupportedAnalysisStep();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.SUPPORTED_ANALYSIS_STEP);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setOtherPropertyValues(properties.getAdditionalProperties());

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.PRODUCED_GUARD.getPropertyType().equals(propertyName))
                    {
                        ProducedGuard specificationProperty = new ProducedGuard();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.PRODUCED_GUARD);
                        specificationProperty.setDescription(properties.getDescription());

                        Map<String, String> otherPropertyValues = new HashMap<>();

                        if (properties.getAdditionalProperties() != null)
                        {
                            for (String additionalPropertyName : properties.getAdditionalProperties().keySet())
                            {
                                if (additionalPropertyName != null)
                                {
                                    if (additionalPropertyName.equals("completionStatus"))
                                    {
                                        for (CompletionStatus completionStatus : CompletionStatus.values())
                                        {
                                            if (completionStatus.getName().equals(properties.getAdditionalProperties().get(additionalPropertyName)))
                                            {
                                                specificationProperty.setCompletionStatus(completionStatus);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        otherPropertyValues.put(additionalPropertyName, properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                }
                            }
                        }

                        if (! otherPropertyValues.isEmpty())
                        {
                            specificationProperty.setOtherPropertyValues(otherPropertyValues);
                        }

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.PRODUCED_REQUEST_PARAMETER.getPropertyType().equals(propertyName))
                    {
                        ProducedRequestParameter specificationProperty = new ProducedRequestParameter();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.PRODUCED_REQUEST_PARAMETER);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setDataType(properties.getDataType());

                        Map<String, String> otherPropertyValues = new HashMap<>();

                        if (properties.getAdditionalProperties() != null)
                        {
                            for (String additionalPropertyName : properties.getAdditionalProperties().keySet())
                            {
                                if (additionalPropertyName != null)
                                {
                                    if (additionalPropertyName.equals(OpenMetadataProperty.EXAMPLE.name))
                                    {
                                        specificationProperty.setExample(properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                    else if (additionalPropertyName.equals(OpenMetadataProperty.REQUIRED.name))
                                    {
                                        specificationProperty.setRequired(Boolean.getBoolean(properties.getAdditionalProperties().get(additionalPropertyName)));
                                    }
                                    else
                                    {
                                        otherPropertyValues.put(additionalPropertyName, properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                }
                            }
                        }

                        if (! otherPropertyValues.isEmpty())
                        {
                            specificationProperty.setOtherPropertyValues(otherPropertyValues);
                        }

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.PRODUCED_ACTION_TARGET.getPropertyType().equals(propertyName))
                    {
                        ProducedActionTarget specificationProperty = new ProducedActionTarget();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.PRODUCED_ACTION_TARGET);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setOpenMetadataTypeName(properties.getDataType());

                        Map<String, String> otherPropertyValues = new HashMap<>();

                        if (properties.getAdditionalProperties() != null)
                        {
                            for (String additionalPropertyName : properties.getAdditionalProperties().keySet())
                            {
                                if (additionalPropertyName != null)
                                {
                                    if (additionalPropertyName.equals(OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name))
                                    {
                                        specificationProperty.setDeployedImplementationType(properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                    else if (additionalPropertyName.equals(OpenMetadataProperty.REQUIRED.name))
                                    {
                                        specificationProperty.setRequired(Boolean.getBoolean(properties.getAdditionalProperties().get(additionalPropertyName)));
                                    }
                                    else
                                    {
                                        otherPropertyValues.put(additionalPropertyName, properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                }
                            }
                        }

                        if (! otherPropertyValues.isEmpty())
                        {
                            specificationProperty.setOtherPropertyValues(otherPropertyValues);
                        }

                        return specificationProperty;
                    }
                    else if (SpecificationPropertyType.PRODUCED_ANNOTATION_TYPE.getPropertyType().equals(propertyName))
                    {
                        ProducedAnnotationType specificationProperty = new ProducedAnnotationType();

                        specificationProperty.setName(properties.getPreferredValue());
                        specificationProperty.setSpecificationPropertyType(SpecificationPropertyType.PRODUCED_ANNOTATION_TYPE);
                        specificationProperty.setDescription(properties.getDescription());
                        specificationProperty.setOpenMetadataTypeName(properties.getDataType());

                        Map<String, String> otherPropertyValues = new HashMap<>();

                        if (properties.getAdditionalProperties() != null)
                        {
                            for (String additionalPropertyName : properties.getAdditionalProperties().keySet())
                            {
                                if (additionalPropertyName != null)
                                {
                                    if (additionalPropertyName.equals(OpenMetadataProperty.EXPRESSION.name))
                                    {
                                        specificationProperty.setExpression(properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                    else if (additionalPropertyName.equals(OpenMetadataProperty.EXPLANATION.name))
                                    {
                                        specificationProperty.setExplanation(properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                    else if (additionalPropertyName.equals(OpenMetadataProperty.ANALYSIS_STEP.name))
                                    {
                                        specificationProperty.setAnalysisStepName(properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                    else
                                    {
                                        otherPropertyValues.put(additionalPropertyName, properties.getAdditionalProperties().get(additionalPropertyName));
                                    }
                                }
                            }
                        }

                        if (! otherPropertyValues.isEmpty())
                        {
                            specificationProperty.setOtherPropertyValues(otherPropertyValues);
                        }

                        return specificationProperty;
                    }
                }
            }
        }

        return null;
    }



    /**
     * Convert a list of relationships into a specification.
     *
     * @param relatedSpecificationProperties list of related elements linked by the SpecificationPropertyAssignment relationship.
     * @return specification
     */
    public Map<String, List<SpecificationProperty>> getSpecificationFromOMF(List<RelatedMetadataElement> relatedSpecificationProperties) throws PropertyServerException
    {
        return getSpecification(super.getRelatedElements(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                         relatedSpecificationProperties));
    }


    /**
     * Convert a list of relationships into a specification.
     *
     * @param relatedSpecificationProperties list of related elements linked by the SpecificationPropertyAssignment relationship.
     * @return specification
     */
    public Map<String, List<SpecificationProperty>> getSpecification(List<RelatedMetadataElementSummary> relatedSpecificationProperties)
    {
        if (relatedSpecificationProperties != null)
        {
            Map<String, List<SpecificationProperty>> results = new HashMap<>();

            for (RelatedMetadataElementSummary relatedSpecificationProperty : relatedSpecificationProperties)
            {
                if (relatedSpecificationProperty != null)
                {
                    SpecificationProperty specificationProperty = this.getSpecificationProperty(relatedSpecificationProperty);

                    if (specificationProperty != null)
                    {
                        String propertyType = specificationProperty.getSpecificationPropertyType().getPropertyType();
                        List<SpecificationProperty> specificProperties = results.get(propertyType);

                        if (specificProperties == null)
                        {
                            specificProperties = new ArrayList<>();
                        }

                        specificProperties.add(specificationProperty);
                        results.put(propertyType, specificProperties);
                    }
                }
            }

            return results;
        }

        return null;
    }
}
