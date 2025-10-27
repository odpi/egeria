/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.refdata;


/**
 * SpecificationPropertyType provides definitions of the different types of specification properties supported by
 * Egeria's templates, connectors and governance actions.
 */
public enum SpecificationPropertyType
{
    /**
     * Name of attribute that should be provided when using the attached template.  Replacement attributes map to the root entity of the template, or, the first occurrence of the attribute in attached relationships, classifications or entities.
     */
    REPLACEMENT_ATTRIBUTE("ReplacementAttribute", "replacementAttribute", "Name of attribute that should be provided when using the attached template.  Replacement attributes map to the root entity of the template, or, the first occurrence of the attribute in attached relationships, classifications or entities."),

    /**
     * Map of placeholder names to values that should be replaced in a template.
     */
    PLACEHOLDER_PROPERTY("PlaceholderProperty", "placeholderProperty", "Placeholder names are located through the attribute values found in a template.  They are identified with tilda+left-curley-brace...right-curley-brace+tilda, for example ~{variableName}~.  The value supplied is substituted for the placeholder variable."),

    /**
     * Description of a template that should be used by the connector when creating new elements of a certain type.
     */
    SUPPORTED_TEMPLATE("SupportedTemplate", "supportedTemplate", "Description of a template that should be used by the connector when creating new elements of a certain type."),

    /**
     * Description of a configuration property that is used to affect the behaviour of a connector.
     */
    SUPPORTED_CONFIGURATION_PROPERTY("SupportedConfigurationProperty", "supportedConfigurationProperty", "Description of a configuration property that is used to affect the behaviour of a connector."),

    /**
     * Description of a request type used to describe a request to a governance engine/service.
     */
    SUPPORTED_REQUEST_TYPE("SupportedRequestType", "supportedRequestType", "Description of a request type used to describe a request to a governance engine/service."),

    /**
     * Description of a request parameter that is used to affect the behaviour of a governance service.
     */
    SUPPORTED_REQUEST_PARAMETER("SupportedRequestParameter", "supportedRequestParameter", "Description of a request parameter that is used to affect the behaviour of a governance service."),

    /**
     * Description of an element that is linked to a governance action request.  The resulting call to the governance engine/service will work with this element.
     */
    SUPPORTED_ACTION_TARGET("SupportedActionTarget", "supportedActionTarget", "Description of an element that is linked to a governance action request.  The resulting call to the governance engine/service will work with this element."),

    /**
     * Description of an analysis step supported by a survey action service.
     */
    SUPPORTED_ANALYSIS_STEP("SupportedAnalysisStep", "supportedAnalysisStep", "Description of an analysis step supported by a survey action service."),

    /**
     * Description of a guard that is returned by a governance service when it completes.
     */
    PRODUCED_GUARD("ProducedGuard", "producedGuard", "Description of a guard that is returned by a governance service when it completes."),

    /**
     * Description of a request parameter that is produced by a governance service and is used to affect the behaviour of the next governance service(s) called in the same governance action process.
     */
    PRODUCED_REQUEST_PARAMETER("ProducedRequestParameter", "producedRequestParameter", "Description of a request parameter that is produced by a governance service and is used to affect the behaviour of the next governance service(s) called in the same governance action process."),

    /**
     * Description of an element that is produced by a governance service and is passed to the next governance service(s) called in the same governance action process as an action target.
     */
    PRODUCED_ACTION_TARGET("ProducedActionTarget", "producedActionTarget", "Description of an element that is produced by a governance service and is passed to the next governance service(s) called in the same governance action process as an action target."),

    /**
     * Description of a type of annotation produced by a survey action service.
     */
    PRODUCED_ANNOTATION_TYPE("ProducedAnnotationType", "producedAnnotationType", "Description of a type of annotation produced by a survey action service."),

    ;

    /**
     * Name of properties bean to use when creating a specification property of this type.
     */
    public final String beanClass;

    /**
     * Property type value stored by Egeria in the SpecificationPropertyAssignment relationship.
     */
    public final String propertyType;

    /**
     * Description of the property.
     */
    public final String description;



    /**
     * Create a specific Enum constant.
     *
     * @param beanClass name to use on a REST API call
     * @param propertyType name of the attribute
     * @param description description of the request type
     */
    SpecificationPropertyType(String beanClass,
                              String propertyType,
                              String description)
    {
        this.beanClass    = beanClass;
        this.propertyType = propertyType;
        this.description  = description;
    }


    /**
     * Return the name of the properties bean to use when creating a specification property of this type.
     *
     * @return string
     */
    public String getBeanClass()
    {
        return beanClass;
    }


    /**
     * Return the name of the attribute.
     *
     * @return string name
     */
    public String getPropertyType()
    {
        return propertyType;
    }


    /**
     * Return the description of the attribute.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SpecificationPropertyType{" + propertyType + "}";
    }
}
