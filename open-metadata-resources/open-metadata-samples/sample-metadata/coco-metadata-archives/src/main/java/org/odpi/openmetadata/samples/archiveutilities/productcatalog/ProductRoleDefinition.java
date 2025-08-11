/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.productcatalog;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.archiveutilities.businesssystems.ScopeDefinition;

/**
 * The ProductRoleDefinition is used to feed the definition of the actor roles for
 * Coco Pharmaceuticals' product catalog.
 */
public enum ProductRoleDefinition
{
    PRODUCT_MANAGER("eca8fc08-224b-462f-ba21-00cb73795c69",
                    OpenMetadataType.DIGITAL_PRODUCT_MANAGER.typeName,
                    "CocoComboReferenceDataProductManagerRole",
                    "Coco Combo Reference Data Product Manager",
                    "The product manager role covering the reference data sets that originate from the CocoCombo open metadata archive."),

    PRODUCT_CONSUMER("f36d049c-5a49-4131-9615-afbfcbf0c08a",
                     OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                     "CocoComboProductConsumer",
                     "Coco Combo Subscriber",
                     "Subscriber to one of the reference data products from CocoComboArchive."),

    PRODUCT_SUPPLIER("25c06984-637b-4604-b19e-ee920358349d",
                     OpenMetadataType.SOLUTION_ACTOR_ROLE.typeName,
                     "CocoComboSupplier",
                     "Egeria Community",
                     "The Egeria Open Source Community is responsible for supplying the contents of the CocoComboArchive."),
    ;

    private final String                 guid;
    private final String                 typeName;
    private final String                 identifier;
    private final String                 displayName;
    private final String                 description;


    /**
     * ProductRoleDefinition constructor creates an instance of the enum
     *
     * @param guid         unique identifier for the role
     * @param identifier   unique Id for the role
     * @param displayName   text for the role
     * @param description   description of the assets in the role
     */
    ProductRoleDefinition(String guid,
                          String typeName,
                          String identifier,
                          String displayName,
                          String description)
    {
        this.guid        = guid;
        this.typeName    = typeName;
        this.identifier  = identifier;
        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Return the name of the PersonRole type to use.
     *
     * @return type name
     */
    public String getGUID()
    {
        return guid;
    }

    /**
     * Return the type name
     *
     * @return string
     */
    public String getTypeName()
    {
        return typeName;
    }

    /**
     * Returns the unique name for the role entity.
     *
     * @return identifier
     */
    public String getQualifiedName()
    {
        return "SolutionActorRole::" + guid + "::" + identifier;
    }


    /**
     * Returns the unique name for the role.
     *
     * @return identifier
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Returns a descriptive name of the role.
     *
     * @return display name
     */
    public String getDisplayName()
    {
        return displayName;
    }

    /**
     * Returns a detailed description of the role.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns the breadth of responsibility for the role.
     *
     * @return scope
     */
    public ScopeDefinition getScope()
    {
        return ScopeDefinition.WITHIN_SOLUTION;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductRoleDefinition{" + "identifier='" + identifier + '}';
    }
}
