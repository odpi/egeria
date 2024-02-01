/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;

/**
 * The OrganizationDefinition is used to feed the definition of the organizations for Coco Pharmaceuticals scenarios.
 */
public enum OrganizationDefinition
{
    /**
     * Coco Pharmaceuticals Ltd
     */
    COCO("Coco Pharmaceuticals Ltd",
         "Fictitious mid-sized pharmaceutical company specializing in cancer treatments.",
         OrganizationTypeDefinition.PRIMARY),


    /**
     * Hampton Hospital
     */
    HAMPTON("Hampton Hospital",
            "Partner hospital in USA that takes part in clinical trials.",
            OrganizationTypeDefinition.PARTNER),

    /**
     * Oak Dene Hospital
     */
    OAK_DENE("Oak Dene Hospital",
             "Partner hospital in UK that takes part in clinical trials.",
             OrganizationTypeDefinition.PARTNER),

    /**
     * Old Market Hospital
     */
    OLD_MARKET("Old Market Hospital",
               "Partner hospital in the Netherlands that takes part in clinical trials.",
               OrganizationTypeDefinition.PARTNER),

    /**
     * XDC Partner IT Consultancy
     */
    XDC("XDC",
        "Partner IT consultancy employed to improve IT systems.",
        OrganizationTypeDefinition.PARTNER),

    /**
     * Sec Inc Consultancy Services
     */
    SEC_INC("Sec Inc Consultancy Services",
        "Partner security consultancy employed to investigate suspected instances of fraud.",
        OrganizationTypeDefinition.PARTNER),

    /**
     * Sales and Marketing Cloud Provider
     */
    SALES_CP("Sales and Marketing Cloud Provider",
            "Organization providing customer sales and marketing cloud services.",
            OrganizationTypeDefinition.PARTNER),

    /**
     * Accounting Services Cloud Provider
     */
    FINANCE_CP("Accounting Services Cloud Provider",
             "Organization providing accounting software through cloud services.",
             OrganizationTypeDefinition.PARTNER),

    /**
     * TravelPlanner Cloud Provider
     */
    TRAVEL_CP("TravelPlanner Cloud Provider",
               "Travel planning, approval, booking and expenses.",
               OrganizationTypeDefinition.PARTNER),
    ;

    public static final String               propertyName = "organization";

    private final String                     displayName;
    private final String                     description;
    private final OrganizationTypeDefinition organizationType;

    /**
     * The constructor creates an instance of the enum
     *
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     * @param organizationType type of team/organization
     */
    OrganizationDefinition(String                     displayName,
                           String                     description,
                           OrganizationTypeDefinition organizationType)
    {
        this.displayName = displayName;
        this.description = description;
        this.organizationType = organizationType;
    }


    /**
     * Return the unique name of the organization entity.
     *
     * @return name
     */
    public String getQualifiedName()
    {
        return "Organization:" + displayName;
    }


    /**
     * Return the name of the organization.
     *
     * @return name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the organization.
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the type of organization to fill in the "teamType" property.
     *
     * @return enum
     */
    public OrganizationTypeDefinition getOrganizationType()
    {
        return organizationType;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "OrganizationDefinition{" + displayName + '}';
    }
}
