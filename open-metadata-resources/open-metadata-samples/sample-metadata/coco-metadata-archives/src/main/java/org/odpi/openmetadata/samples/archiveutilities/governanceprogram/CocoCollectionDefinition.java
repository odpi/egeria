/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

/**
 * The CocoCollectionDefinition describes the different folders that are used to organize various definitions for
 * Coco Pharmaceutical's governance program
 */
public enum CocoCollectionDefinition
{
    /**
     * Corporate Regulation Library
     */
    CORPORATE_REGULATION_LIBRARY("f565ca0f-617f-4a55-8ad8-2a9d66861239",
                                 OpenMetadataType.ROOT_COLLECTION.typeName,
                                 null,
                                 null,
                                 "Corporate Regulation Library",
                                 "Definitions used to describe key regulations and standards used by Coco Pharmaceuticals.",
                                 null),

    /**
     * Security Regulations
     */
    SECURITY_REGULATIONS("54a95fb3-2147-4bf1-ab1d-9d424647f188",
                         OpenMetadataType.COLLECTION_FOLDER.typeName,
                         null,
                         CORPORATE_REGULATION_LIBRARY,
                         "Security Regulations",
                         "Resources relating to security regulations.",
                         null),

    /**
     * Financial Regulations
     */
    FINANCIAL_REGULATIONS("78f885b8-5243-40c6-8722-a70abcd03eb0",
                          OpenMetadataType.COLLECTION_FOLDER.typeName,
                          null,
                          CORPORATE_REGULATION_LIBRARY,
                          "Financial Regulations",
                          "Resources relating to financial regulations.",
                          null),

    /**
     * Sustainability Regulations
     */
    SUSTAINABILITY_REGULATIONS("3a3139b4-1648-4d2a-a685-88b4d1eae44f",
                          OpenMetadataType.COLLECTION_FOLDER.typeName,
                          null,
                          CORPORATE_REGULATION_LIBRARY,
                          "Sustainability Regulations",
                          "Resources relating to sustainability regulations.",
                          null),

    /**
     * Privacy Regulations
     */
    PRIVACY_REGULATIONS("27e48750-a09d-4a45-a3d4-3dd8f70290a9",
                        OpenMetadataType.COLLECTION_FOLDER.typeName,
                        null,
                        CORPORATE_REGULATION_LIBRARY,
                        "Privacy Regulations",
                        "Resources relating to privacy regulations.",
                        null),

    /**
     * Pharmaceutical Industry Regulations
     */
    PHARMACEUTICAL_INDUSTRY_REGULATIONS("3a45bb15-0f11-4e05-8f64-0ca395a08636",
                                        OpenMetadataType.COLLECTION_FOLDER.typeName,
                                        null,
                                        CORPORATE_REGULATION_LIBRARY,
                                        "Pharmaceutical Industry Regulations",
                                        "Resources relating to regulations specific to the pharmaceutical industry.",
                                        null),
    /**
     * Clinical Trial Regulations
     */
    CLINICAL_TRIAL_REGULATIONS("ba707979-b79b-44ef-a8a9-ecbdd1130f09",
                               OpenMetadataType.COLLECTION_FOLDER.typeName,
                               null,
                               PHARMACEUTICAL_INDUSTRY_REGULATIONS,
                               "Clinical Trial Regulations",
                               "Resources relating to regulations for conducting clinical trials.",
                               null),

    /**
     * Governance Folios - RootCollection::Coco::Governance Folios
     */
    GOVERNANCE_FOLIOS("e3567b73-1d1b-4ee2-ad06-d1d70dcdf70e",
                      OpenMetadataType.ROOT_COLLECTION.typeName,
                      null,
                      null,
                      "Governance Folios",
                      "Organization of governance definitions into folios of responsibilities.",
                      null),


    /**
     * Coco Pharmaceuticals Solutions
     */
    SOLUTIONS("af34b0cc-8ace-4622-bc8a-46813027d695",
                      OpenMetadataType.ROOT_COLLECTION.typeName,
                      null,
                      null,
                      "Strategic Solutions",
                      "These solutions are critical for the success of Coco Pharmaceuticals.",
                      null),


    /**
     * Strategic Data Hubs - RootCollection::Coco::Strategic Data Hubs
     */
    DATA_HUBS("90725652-7878-4f32-aef9-be6a94670411",
              OpenMetadataType.ROOT_COLLECTION.typeName,
              null,
              null,
              "Strategic Data Hubs",
              "These Data Hubs manage authoritative data at Coco Pharmaceuticals.  They are designed for sharing and distribution within the organization.",
              null),

    /**
     * IT Systems Inventory - RootCollection::Coco::IT Systems Inventory
     */
    IT_SUBSYSTEMS("2df4c8bb-2781-4426-9590-1ac064b64c8a",
              OpenMetadataType.ROOT_COLLECTION.typeName,
              null,
              null,
              "IT Systems Inventory",
              "These systems are the strategic systems supporting Coco Pharmaceuticals transformation.  They are organized by subsystem.",
              null),

    WAREHOUSE_SUBSYSTEM("8ae44828-efdc-46cc-8f55-1bdd254c621f",
                        OpenMetadataType.IT_SUBSYSTEM.typeName,
                        null,
                        IT_SUBSYSTEMS,
                        "Warehouse Subsystem",
                        "These systems support the storage and distribution of Coco Pharmaceuticals products.",
                        null),

    MANUFACTURING_SUBSYSTEM("4118206e-cb88-48c9-b410-3023bfa27f1e",
                            OpenMetadataType.IT_SUBSYSTEM.typeName,
                            null,
                            IT_SUBSYSTEMS,
                            "Manufacturing Subsystem",
                            "These systems support the manufacturing of Coco Pharmaceuticals products in the various production facilities.",
                            null),

    DELIVERY_SUBSYSTEM("61be3abf-ef77-4146-be7d-c908725b96fc",
                       OpenMetadataType.IT_SUBSYSTEM.typeName,
                       null,
                       IT_SUBSYSTEMS,
                       "Delivery Subsystem",
                       "These systems support the delivery of Coco Pharmaceuticals products to hospitals and other healthcare facilities.",
                       null),

    PROCUREMENT_SUBSYSTEM("c9948c78-173e-4ebb-901a-31f875bf4a4f",
                          OpenMetadataType.IT_SUBSYSTEM.typeName,
                          null,
                          IT_SUBSYSTEMS,
                          "Procurement Subsystem",
                          "These systems support the procurement and supply of raw materials and other goods and services for Coco Pharmaceuticals operations.",
                          null),

    FINANCE_SUBSYSTEM("d2874610-2bd0-41a5-9a16-61771cfde3c4",
                      OpenMetadataType.IT_SUBSYSTEM.typeName,
                      null,
                      IT_SUBSYSTEMS,
                      "Finance Subsystem",
                      "These systems support the financial management of Coco Pharmaceuticals.",
                      null),

    PATIENT_TREATMENT_SUBSYSTEM("435f87e7-8de2-4a5c-8daf-983adb52440b",
                                OpenMetadataType.IT_SUBSYSTEM.typeName,
                                null,
                                IT_SUBSYSTEMS,
                                "Patient Treatment Subsystem",
                                "These systems support the sales and direct marketing of Coco Pharmaceuticals products.",
                                null),

    RESEARCH_SUBSYSTEM("b70ac365-eb4c-4438-98ec-2ed34d634a64",
                       OpenMetadataType.IT_SUBSYSTEM.typeName,
                       null,
                       IT_SUBSYSTEMS,
                       "Research Subsystem",
                       "These systems support the research and development of Coco Pharmaceuticals products.",
                       null),

    ADMIN_SUBSYSTEM("74d34f01-24b6-4492-9aaa-d215f5846ea7",
                       OpenMetadataType.IT_SUBSYSTEM.typeName,
                       null,
                       IT_SUBSYSTEMS,
                       "Administration Subsystem",
                       "These systems support the administration operations of Coco Pharmaceuticals.",
                       null),

    SUBJECT_AREAS("af13f129-8170-4025-a701-43fb41309a7b",
                    OpenMetadataType.ROOT_COLLECTION.typeName,
                    null,
                    null,
                    "Subject Areas",
                    "Subject Areas describe the types of data that are important to Coco Pharmaceuticals.  Each subject area includes glossary terms, data classes, valid value sets and validation rule for data in the subject area.",
                    null),

    ;


    private final String                   guid;
    private final String                   typeName;
    private final String                   classificationName;
    private final CocoCollectionDefinition parent;
    private final String                   displayName;
    private final String                   description;
    private final String                   category;


    /**
     * Constructor for enum value.
     *
     * @param guid unique identifier
     * @param typeName type of folder
     * @param classificationName optional classification
     * @param parent optional parent folder
     * @param displayName display name
     * @param description description
     * @param category category
     */
    CocoCollectionDefinition(String                   guid,
                             String                   typeName,
                             String                   classificationName,
                             CocoCollectionDefinition parent,
                             String                   displayName,
                             String                   description,
                             String                   category)
    {
        this.guid               = guid;
        this.typeName           = typeName;
        this.classificationName = classificationName;
        this.parent             = parent;
        this.displayName        = displayName;
        this.description        = description;
        this.category           = category;
    }


    /**
     * Returns the unique identifier for the collection entity.
     *
     * @return string
     */
    public String getGUID()
    {
        return guid;
    }

    /**
     * Returns the unique name for the collection entity.
     *
     * @return qualified name
     */
    public String getQualifiedName()
    {
        return typeName + "::Coco::" + displayName;
    }


    /**
     * Returns the unique name for the collection type.
     *
     * @return type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Returns the optional classification.
     *
     * @return type name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Return the entry of the parent folder - null for top level.
     *
     * @return enum
     */
    public CocoCollectionDefinition getParent()
    {
        return parent;
    }


    /**
     * Returns a descriptive name of the collection.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Returns a detailed description of the contents of the collection.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Returns an optional category for the collection.
     *
     * @return string
     */
    public String getCategory()
    {
        return category;
    }


    /**
     * String containing the definition values
     *
     * @return printable string
     */
    @Override
    public String toString()
    {
        return "CocoCollectionDefinition{" +
                "typeName='" + typeName + '\'' +
                ", classificationName='" + classificationName + '\'' +
                ", parent=" + parent +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                "} " + super.toString();
    }
}
