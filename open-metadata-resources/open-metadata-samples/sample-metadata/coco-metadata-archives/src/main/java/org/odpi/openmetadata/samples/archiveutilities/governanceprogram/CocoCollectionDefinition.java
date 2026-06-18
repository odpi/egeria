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
     * Governance Folios
     */
    GOVERNANCE_FOLIOS("e3567b73-1d1b-4ee2-ad06-d1d70dcdf70e",
                      OpenMetadataType.ROOT_COLLECTION.typeName,
                      null,
                      null,
                      "Governance Folios",
                      "Organization of governance definitions into folios of responsibilities.",
                      null),


    /**
     * Coco Pharmaceuticals' Solutions
     */
    SOLUTIONS("af34b0cc-8ace-4622-bc8a-46813027d695",
                      OpenMetadataType.ROOT_COLLECTION.typeName,
                      null,
                      null,
                      "Coco Pharmaceuticals' Solutions",
                      "These solutions are critical for the success of Coco Pharmaceuticals.",
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
