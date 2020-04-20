/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * ValidValuesMapper provides property name mapping for ValidValueDefinitions and ValidValueSets.
 */
public class ValidValuesMapper
{
    public static final String VALID_VALUE_DEFINITION_TYPE_GUID         = "09b2133a-f045-42cc-bb00-ee602b74c618";   /* from Area 5 */
    public static final String VALID_VALUE_DEFINITION_TYPE_NAME         = "ValidValueDefinition";
    /* Referenceable */

    public static final String DISPLAY_NAME_PROPERTY_NAME               = "name";                 /* from ValidValueDefinition entity */
    public static final String DESCRIPTION_PROPERTY_NAME                = "description";          /* from ValidValueDefinition entity */
    public static final String USAGE_PROPERTY_NAME                      = "usage";                /* from ValidValueDefinition entity */
    public static final String SCOPE_PROPERTY_NAME                      = "scope";                /* from ValidValueDefinition entity */
    public static final String PREFERRED_VALUE_PROPERTY_NAME            = "preferredValue";       /* from ValidValueDefinition entity */
    public static final String IS_DEPRECATED_PROPERTY_NAME              = "isDeprecated";         /* from ValidValueDefinition entity */

    public static final String VALID_VALUE_SET_TYPE_GUID                = "7de10805-7c44-40e3-a410-ffc51306801b";   /* from Area 5 */
    public static final String VALID_VALUE_SET_TYPE_NAME                = "ValidValueSet";
    /* ValidValueDefinition */

    public static final String REFERENCE_DATA_CLASSIFICATION_TYPE_GUID  = "55e5ae33-39c6-4834-9d05-ef0ae4e0163b";  /* from Area 5 */
    public static final String REFERENCE_DATA_CLASSIFICATION_TYPE_NAME  = "ReferenceData";
    /* Linked to Asset */

    public static final String VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_GUID = "c5d48b73-eadd-47db-ab64-3be99b2fb32d";  /* from Area 5 */
    public static final String VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME = "ValidValuesAssignment";
    /* End1 = Referenceable; End 2 = ValidValuesDefinition */

    public static final String IS_STRICT_REQUIREMENT_PROPERTY_NAME      = "strictRequirement";     /* from ValidValuesAssignment relationship */

    public static final String VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_GUID = "6337c9cd-8e5a-461b-97f9-5151bcb97a9e";  /* from Area 5 */
    public static final String VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME = "ValidValuesMember";
    /* End1 = ValidValuesSet; End 2 = ValidValuesDefinition */

    public static final String VALID_VALUES_IMPL_RELATIONSHIP_TYPE_GUID = "d9a39553-6a47-4477-a217-844300c07cf2";  /* from Area 5 */
    public static final String VALID_VALUES_IMPL_RELATIONSHIP_TYPE_NAME = "ValidValuesImplementation";
    /* End1 = ValidValuesDefinition; End 2 = Asset */

    public static final String SYMBOLIC_NAME_PROPERTY_NAME             = "symbolicName";            /* from ValidValuesImplementation relationship */
    public static final String IMPLEMENTATION_VALUE_PROPERTY_NAME      = "implementationValue";     /* from ValidValuesImplementation relationship */
    public static final String ADDITIONAL_VALUES_PROPERTY_NAME         = "additionalValues";        /* from ValidValuesImplementation relationship */

    public static final String VALID_VALUES_MAP_RELATIONSHIP_TYPE_GUID = "203ce62c-3cbf-4542-bf82-81820cba718f";  /* from Area 5 */
    public static final String VALID_VALUES_MAP_RELATIONSHIP_TYPE_NAME = "ValidValuesMapping";
    /* End1 = ValidValuesDefinition; End 2 = ValidValuesDefinition */

    public static final String ASSOCIATION_DESCRIPTION_PROPERTY_NAME   = "associationDescription";  /* from ValidValuesMapping relationship */
    public static final String CONFIDENCE_PROPERTY_NAME                = "confidence";              /* from ValidValuesMapping and ReferenceValueAssignment relationship */
    public static final String STEWARD_PROPERTY_NAME                   = "steward";                 /* from ValidValuesMapping and ReferenceValueAssignment relationship */
    public static final String NOTES_PROPERTY_NAME                     = "notes";                   /* from ValidValuesMapping and ReferenceValueAssignment relationship */

    public static final String REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_GUID = "111e6d2e-94e9-43ed-b4ed-f0d220668cbf";  /* from Area 5 */
    public static final String REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_NAME = "ReferenceValueAssignment";
    /* End1 = Referenceable; End 2 = ValidValuesDefinition */
}
