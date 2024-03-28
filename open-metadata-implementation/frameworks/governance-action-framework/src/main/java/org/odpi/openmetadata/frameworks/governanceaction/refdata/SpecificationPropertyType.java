/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.refdata;


/**
 * ReferenceValueAttributeName provides definitions of the standard attributeName values used in the
 * ReferenceValueAssignment relationship.
 */
public enum SpecificationPropertyType
{
    /**
     * Name of attribute that should be provided when using the attached template.  Replacement attributes map to the root entity of the template, or, the first occurrence of the attribute in attached relationships, classifications or entities.
     */
    REPLACEMENT_ATTRIBUTE("replacementAttribute", "Name of attribute that should be provided when using the attached template.  Replacement attributes map to the root entity of the template, or, the first occurrence of the attribute in attached relationships, classifications or entities."),

    /**
     * Map of placeholder names to values that should be replaced in a template.
     */
    PLACEHOLDER_PROPERTY("placeholderProperty", "Placeholder names are located through the attribute values found in a template.  They are identified with double braces, for example {{variableName}}.  The value supplied is substituted for the placeholder variable."),

    /**
     * Description of a configuration property that is used to affect the behaviour of a connector.
     */
    SUPPORTED_CONFIGURATION_PROPERTY("supportedConfigurationProperty", "Description of a configuration property that is used to affect the behaviour of a connector."),

    /**
     * Description of a request type used to describe a request to a governance engine/service.
     */
    SUPPORTED_REQUEST_TYPE("supportedRequestType", "Description of a request type used to describe a request to a governance engine/service."),

    /**
     * Description of a request parameter that is used to affect the behaviour of a governance service.
     */
    SUPPORTED_REQUEST_PARAMETER("supportedRequestParameter", "Description of a request parameter that is used to affect the behaviour of a governance service."),

    /**
     * Description of an element that is linked to a governance action request.  The resulting call to the governance engine/service will work with this element.
     */
    SUPPORTED_ACTION_TARGET("supportedActionTarget", "Description of an element that is linked to a governance action request.  The resulting call to the governance engine/service will work with this element."),

    /**
     * Description of an analysis step supported by a survey action service.
     */
    SUPPORTED_ANALYSIS_STEP("supportedAnalysisStep", "Description of an analysis step supported by a survey action service."),

    /**
     * Description of a guard that is returned by a governance service when it completes.
     */
    PRODUCED_GUARD("producedGuard", "Description of a guard that is returned by a governance service when it completes."),

    /**
     * Description of a request parameter that is produced by a governance service and is used to affect the behaviour of the next governance service(s) called in the same governance action process.
     */
    PRODUCED_REQUEST_PARAMETER("producedRequestParameter", "Description of a request parameter that is produced by a governance service and is used to affect the behaviour of the next governance service(s) called in the same governance action process."),

    /**
     * Description of an element that is produced by a governance service and is passed to the next governance service(s) called in the same governance action process as an action target.
     */
    PRODUCED_ACTION_TARGET("producedActionTarget", "Description of an element that is produced by a governance service and is passed to the next governance service(s) called in the same governance action process as an action target."),

    /**
     * Description of a type of annotation produced by a survey action service.
     */
    PRODUCED_ANNOTATION_TYPE("producedAnnotationType", "Description of a type of annotation produced by a survey action service."),

    ;

    public final String attributeName;
    public final String description;



    /**
     * Create a specific Enum constant.
     *
     * @param attributeName name of the attribute
     * @param description description of the request type
     */
    SpecificationPropertyType(String attributeName,
                              String description)
    {
        this.attributeName = attributeName;
        this.description   = description;
    }


    /**
     * Return the name of the attribute.
     *
     * @return string name
     */
    public String getPropertyType()
    {
        return attributeName;
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
        return "ReferenceValueAttributeName{" + attributeName + "}";
    }
}
