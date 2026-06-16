/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;


/**
 * The GlossaryCategoryDefinition is used to populate the sustainability glossary.
 */
public enum GlossaryCategoryDefinition
{
    /**
     * Types of chemicals that are significant in managing sustainability.
     */
    CHEMICALS("Chemicals",
                  "Types of chemicals that are significant in managing sustainability.  Chemicals represented in common names as well as scientific names."),

    EMISSIONS("Emissions",
            "Information used to describe, measure and report on sustainability related emissions."),

    EMISSIONS_CLASS("Emissions Class",
            "A category of GHG emission classes."),

    EMISSIONS_MEASUREMENT("Emissions Measurement",
            "About measurement of GHG emissions."),

    EMISSIONS_TARGETS("Emissions Targets",
           "About emissions targets and related goals."),

    SUSTAINABILITY_FOR_PHARMA("Sustainability for Pharma",
            "A resource of some kind that is used in both Tinderbox and Egeria."),

    CLIMATE("Climate",
            "A resource of some kind that is used in both Tinderbox and Egeria."),

    WATER("Water",
         "A resource of some kind that is used in both Tinderbox and Egeria."),

    CHEMICAL_CONTAMINANT("Chemical Contaminant",
          "A resource of some kind that is used in both Tinderbox and Egeria."),

    CIRCULAR_ECONOMY("Circular Economy",
           "A resource of some kind that is used in both Tinderbox and Egeria."),

    GHG_PROTOCOL("GHG Protocol",
            "Based on the GHG Protocol Corporate Standard. Some of these terms may have been harvested from the freeform text. Sub-categories represent specific definitions found in the appendices."),

    GHG_PROTOCOL_GLOSSARY("GHG Protocol Glossary",
            "Terms from the Glossary Appendix in the GHG Protocol Corporate Standard.\n"),

    GHG_EMISSIONS_CALCULATIONS("GHG Emissions Calculations",
            "Concepts and terms used in calculating GHG emissions using the GHG Protocols. Many of these calculations can be found in the GHG Calculation Tools (Spreadsheets)."),

    SUSTAINABILITY_REGULATIONS("Sustainability Regulations",
            "Standards and regulations covering the sustainability space."),

    EU_SUSTAINABILITY_REGULATIONS("EU - Sustainability Regulations",
            "Regulations originating from the European Union (EU)"),

    GENERAL_KNOWLEDGE("General Knowledge",
            "General information known and used across many communities."),

    ORGANIZATIONS("Organizations",
             "Public organizations"),

    FINANCIAL_ACCOUNTING("Financial Accounting",
             "Terms used in financial accounting practices."),

    GHG_MRV("GHG MRV",
            "Greenhouse Gas Measurement, Reporting, and Verification, a critical process for tracking and managing greenhouse gas emissions to support climate change mitigation efforts."),

    GOVERNANCE("Governance",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    LICENSES("Licenses",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    WORK_PROPERTIES("Work Properties",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    LICENSE_PROPERTIES("License Properties",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    CONTRACTS("Contracts",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    STANDARDS("Standards",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    STANDARDS_TERMINOLOGY("Standards Terminology",
            "Terminology used in the specification and description of standards."),
    W3C_STANDARDS("W3C Standards",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    PCAF("PCAF",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    PCAF_STANDARD_GLOSSARY("PCAF Standard Glossary",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    TCFD_STANDARD("TCFD Standard",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    TCFD_STANDARD_GLOSSARY("TCFD Standard Glossary",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    REFERENCE_DATA("Reference Data",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    MEASURE("Measure",
            "A resource of some kind that is used in both Tinderbox and Egeria."),
    ACRONYMS("Acronyms",
            "A resource of some kind that is used in both Tinderbox and Egeria."),

    ;

    private final String name;
    private final String description;


    /**
     * The constructor creates an instance of the enum
     *
     * @param name   unique id for the enum
     * @param description   description of the use of this value
     */
    GlossaryCategoryDefinition(String name,
                               String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getQualifiedName()
    {
        return "GlossaryCategory::" + name;
    }


    public String getName()
    {
        return name;
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
        return "GlossaryCategoryDefinition{" + name + '}';
    }
}
