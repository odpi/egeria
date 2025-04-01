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
     * Bowden Arrow Hospital
     */
    BOWDEN_ARROW("Bowden Arrow Hospital",
            "Partner hospital in USA that takes part in clinical trials.",
            OrganizationTypeDefinition.PARTNER),

    /**
     * Eagle House Hospital
     */
    EAGLE_HOUSE_HOSPITAL("Eagle House Hospital",
                         "Partner hospital in USA that takes part in clinical trials.",
                         OrganizationTypeDefinition.PARTNER),

    /**
     * Cleveland Way Hospital
     */
    CLEVELAND_WAY_HOSPITAL("Cleveland Way Hospital",
                         "Partner hospital in USA that takes part in clinical trials.",
                         OrganizationTypeDefinition.PARTNER),

    /**
     * Oak Dene Hospital
     */
    OAK_DENE_HOSPITAL("Oak Dene Hospital",
                      "Partner hospital in UK that takes part in clinical trials.",
                      OrganizationTypeDefinition.PARTNER),


    /**
     * Abbots Grove Hospital
     */
    ABBOTS_GROVE_HOSPITAL("Abbots Grove Hospital",
             "Partner hospital in UK that takes part in clinical trials.",
             OrganizationTypeDefinition.PARTNER),

    /**
     * Shipley Hospital
     */
    SHIPLEY_HOSPITAL("Shipley Hospital",
               "Partner hospital in the UK that takes part in clinical trials.",
               OrganizationTypeDefinition.PARTNER),


    /**
     * Bletchley Mission Hospital
     */
    BLETCHLEY_MISSION_HOSPITAL("Bletchley Mission Hospital",
                     "Partner hospital in the UK that takes part in clinical trials.",
                     OrganizationTypeDefinition.PARTNER),

    /**
     * Bushy Mead Hospital
     */
    BUSHY_MEAD_HOSPITAL("Bushy Mead Hospital",
               "Partner hospital in the UK that takes part in clinical trials.",
               OrganizationTypeDefinition.PARTNER),

    /**
     * Old Market Hospital
     */
    BECKON_TREE_HOSPITAL("Beckon Tree Hospital",
               "Partner hospital in the UK that takes part in clinical trials.",
               OrganizationTypeDefinition.PARTNER),

    /**
     * Old Market Hospital
     */
    OLD_MARKET("Old Market Hospital",
               "Partner hospital in the Netherlands that takes part in clinical trials.",
               OrganizationTypeDefinition.PARTNER),


    /**
     * Ferdinand Hospital
     */
    FERDINAND_HOSPITAL("Ferdinand Hospital",
               "Partner hospital in the Netherlands that takes part in clinical trials.",
               OrganizationTypeDefinition.PARTNER),


    /**
     * Lorne House Hospital
     */
    LORNE_HOUSE_HOSPITAL("Lorne House Hospital",
               "Partner hospital in the Netherlands that takes part in clinical trials.",
               OrganizationTypeDefinition.PARTNER),


    /**
     * Alexandra Hospital
     */
    ALEXANDRA_HOSPITAL("Alexandra Hospital",
               "Partner hospital in the Netherlands that takes part in clinical trials.",
               OrganizationTypeDefinition.PARTNER),

    /**
     * Portmand Clinic
     */
    PORTMAND_CLINIC("Portmand Clinic",
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
