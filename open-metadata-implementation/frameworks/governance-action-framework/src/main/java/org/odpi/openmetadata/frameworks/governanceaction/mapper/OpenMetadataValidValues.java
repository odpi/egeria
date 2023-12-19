/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.mapper;

/**
 * OpenMetadataValidValues provides the names used in constructing valid value sets for open metadata values.
 */
public class OpenMetadataValidValues
{
    /**
     * This scope value means that the associated value is valid across the open metadata ecosystem - that is all tools, runtimes, repositories
     * using open metadata standards.
     */
    public static final String OPEN_METADATA_ECOSYSTEM_SCOPE = "Open Metadata Ecosystem";

    /**
     * Egeria manages valid values for string metadata properties.  This value means that a valid value definition is used with open metadata.
     */
    public static final String VALID_METADATA_VALUES_USAGE = "Used to control valid values in open metadata.";

    /**
     * This is the name of the valid values set that contains all the valid values for open metadata
     */
    public static final String VALID_METADATA_VALUES_SET_CATEGORY   = "Open Metadata Valid Values";

    /**
     * This is the prefix used on valid value definitions for open metadata elements
     */
    public static final String VALID_METADATA_VALUES_QUALIFIED_NAME_PREFIX   = "Egeria:ValidMetadataValue:";

    /**
     * This is the official name of the Open Connector Framework (OCF) used in Connector Types.
     */
    public static final String CONNECTOR_FRAMEWORK_DEFAULT                  = "Open Connector Framework (OCF)";

    /**
     * This is the official name of the programming language used for OCF connectors.
     */
    public static final String CONNECTOR_INTERFACE_LANGUAGE_DEFAULT         = "Java";




    /**
     * Creates the qualifiedName of the element based on the properties supplied.
     *
     * <ul>
     *     <li><i>Egeria:ValidMetadataValue:</i> - This is the name of the top level set.</li>
     *     <li><i>Egeria:ValidMetadataValue:typeName:</i> - This is the name of a set for a specific type.</li>
     *     <li><i>Egeria:ValidMetadataValue:typeName:propertyName-</i> - This is the name of the set for a property name for a specific type.</li>
     *     <li><i>Egeria:ValidMetadataValue::propertyName-</i> - This is the name of the set for a property name for all types where the property name appears.</li>
     *     <li><i>Egeria:ValidMetadataValue:typeName:propertyName-(preferredValue)</i> - This is one of the valid metadata values for the property name when used with a specific type.</li>
     *     <li><i>Egeria:ValidMetadataValue::propertyName-(preferredValue)</i> - This is one of the valid metadata values for the property name when used with any type.</li>
     *     <li><i>Egeria:ValidMetadataValue:typeName:propertyName--mapName--</i> - This is a valid map name for a property name used within a specific type.</li>
     *     <li><i>Egeria:ValidMetadataValue::propertyName--mapName--</i> This is a valid map name for a property name used with any type</li>
     *     <li><i>Egeria:ValidMetadataValue:typeName:propertyName--mapName--(preferredValue)</i> - This is a valid metadata map value for a property name used within a specific type.</li>
     *     <li><i>Egeria:ValidMetadataValue::propertyName--mapName--(preferredValue)</i> - This is a valid metadata map value for a property name used with any type.</li>
     * </ul>
     *
     * @param typeName name of the type (can be null)
     * @param propertyName name of the property (can be null)
     * @param mapName name of the mapName (can be null)
     * @param preferredValue name of the valid value (can be null)
     * @return string (never null)
     */
    public static String constructValidValueQualifiedName(String typeName,
                                                          String propertyName,
                                                          String mapName,
                                                          String preferredValue)
    {
        String qualifiedName = "Egeria:ValidMetadataValue:";

        if ((typeName != null) || (propertyName != null))
        {
            if (typeName != null)
            {
                qualifiedName = qualifiedName + typeName + ":";
            }
            else
            {
                qualifiedName = qualifiedName + ":";
            }

            if (propertyName != null)
            {
                qualifiedName = qualifiedName + propertyName + "-";

                if (mapName != null)
                {
                    qualifiedName = qualifiedName + "-" + mapName + "--";
                }

                if (preferredValue != null)
                {
                    qualifiedName = qualifiedName + "(" + preferredValue + ")";
                }
            }
        }

        return qualifiedName;
    }


    /**
     * Creates the category of the element based on the properties supplied.
     *
     * <ul>
     *     <li><i>Open Metadata Valid Values</i> - This is the category for the top level set.</li>
     *     <li><i>typeName</i> - This is the name of a set for a specific type.</li>
     *     <li><i>typeName:propertyName</i> - This is the name of the set for a property name for a specific type.</li>
     *     <li><i>typeName:propertyName--mapName</i> - This is a valid map name for a property name used within a specific type.</li>
     *     <li><i>typeName:--mapName</i> This is a valid map name for a property name used with any type</li>
     *     <li><i>propertyName</i> - This is the name of the set for a property name for all types where the property name appears.</li>
     *     <li><i>propertyName--mapName</i> This is a valid map name for a property name used with any type</li>
     * </ul>
     *
     * @param typeName name of the type (can be null)
     * @param propertyName name of the property (can be null)
     * @param mapName name of the mapName (can be null)
     * @return string (never null)
     */
    public static String constructValidValueCategory(String typeName,
                                                     String propertyName,
                                                     String mapName)
    {
        final String propertyNameStartSpacer = ":";
        final String mapNameStartSpacer = "-";
        final String mapNameEndSpacer = "--";

        if (typeName != null)
        {
            if (propertyName != null)
            {
                if (mapName != null)
                {
                    return typeName + propertyNameStartSpacer + propertyName + mapNameStartSpacer + mapName + mapNameEndSpacer;
                }
                else // mapName = null
                {
                    return typeName + propertyNameStartSpacer + propertyName;
                }
            }
            else // propertyName = null
            {
                if (mapName != null)
                {
                    return typeName + mapNameStartSpacer + mapName + mapNameEndSpacer;
                }
                else // mapName = null
                {
                    return typeName;
                }
            }
        }
        else // typeName == null
        {
            if (propertyName != null)
            {
                if (mapName != null)
                {
                    return propertyNameStartSpacer + propertyName + mapNameStartSpacer + mapName + mapNameEndSpacer;
                }
                else // mapName = null
                {
                    return propertyNameStartSpacer + propertyName;
                }
            }
            else // propertyName = null
            {
                if (mapName != null)
                {
                    return mapNameStartSpacer + mapName + mapNameEndSpacer;
                }
                else // mapName = null
                {
                    return VALID_METADATA_VALUES_SET_CATEGORY;
                }
            }
        }
    }

}
