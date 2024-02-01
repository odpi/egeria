/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.organization;


/**
 * The BusinessAreaDefinition is used to feed the definition of the organization's business areas (which are types of business capabilities)
 * for Coco Pharmaceuticals scenarios.
 */
public enum BusinessAreaDefinition
{
    /**
     * Scientific exploration, discovery and validation of new treatments for cancer.
     */
    RESEARCH("RES",
             "Research",
             "Scientific exploration, discovery and validation of new treatments for cancer."),

    /**
     * Formal validation of new treatments for cancer with patients.
     */
    CLINICAL_TRIALS("C-TRI",
             "Clinical Trials",
             "Formal validation of new treatments for cancer with patients."),

    /**
     * Management of cash flow and financial health of Coco Pharmaceuticals.
     */
    FINANCE("FIN",
          "Finance",
          "Management of cash flow and financial health of Coco Pharmaceuticals."),

    /**
     * Management of people, their skills and well-being for Coco Pharmaceuticals.
     */
    HR("HR",
       "Human Resources",
       "Management of people, their skills and well-being for Coco Pharmaceuticals."),

    /**
     * Provision of computing services for Coco Pharmaceuticals.
     */
    IT("IT",
       "IT",
       "Provision of computing services for Coco Pharmaceuticals."),

    /**
     * Production of Coco Pharmaceuticals' products.
     */
    MANUFACTURING("MFG",
       "Manufacturing",
       "Production of Coco Pharmaceuticals' products."),


    /**
     * Warehouse management and distribution of Coco Pharmaceuticals' products to customers.
     */
    DISTRIBUTION("DIST",
                  "Distribution",
                  "Warehouse management and distribution of Coco Pharmaceuticals' products to customers."),

    /**
     * Managing customer relationships in order to sell them appropriate Coco Pharmaceuticals' products.
     */
    SALES("SALES",
          "Sales",
          "Managing customer relationships in order to sell them appropriate Coco Pharmaceuticals' products."),

    /**
     * Management of how Coco Pharmaceuticals operates, balancing business strategy and profits against ethics, legal requirements and regulations.
     */
    GOVERNANCE("GOV",
          "Governance",
          "Management of how Coco Pharmaceuticals operates, balancing business strategy and profits against ethics, legal requirements and regulations."),

    ;

    private final String identifier;
    private final String displayName;
    private final String description;


    /**
     * The constructor creates an instance of the enum
     *
     * @param identifier   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     */
    BusinessAreaDefinition(String identifier,
                           String displayName,
                           String description)
    {
        this.identifier = identifier;
        this.displayName = displayName;
        this.description = description;
    }

    public String getQualifiedName()
    {
        return "BusinessArea:" + identifier;
    }


    public String getIdentifier()
    {
        return identifier;
    }


    public String getDisplayName()
    {
        return displayName;
    }


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
        return "BusinessAreaDefinition{" + displayName + '}';
    }
}
