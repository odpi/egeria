/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;


/**
 * The GlossaryTermDefinition is used to populate the sustainability glossary.
 */
public enum GlossaryTermDefinition
{
    /**
     * A means of operating that makes effective use of replaceable resources.
     */
    SUSTAINABILITY("Sustainability",
             "A means of operating that makes effective use of replaceable resources.",
             "In the broadest sense, sustainability refers to the ability to maintain or support a process continuously over time. In business and policy contexts, sustainability seeks to prevent the depletion of natural or physical resources, so that they will remain available for the long term.",
                  "",
                   "https://en.wikipedia.org/wiki/Sustainability",
                   null),

    /**
     * The Greenhouse Gas Protocol set the standards to measure and manage harmful emissions.
     */
    GHG_PROTOCOL("Greenhouse Gas Protocol",
             "The Greenhouse Gas Protocol set the standards to measure and manage harmful emissions.",
             "GHG Protocol establishes comprehensive global standardized frameworks to measure and manage greenhouse gas (GHG) emissions from private and public sector operations, value chains and mitigation actions.\n" +
                     "Building on a 20-year partnership between World Resources Institute (WRI) and the World Business Council for Sustainable Development (WBCSD), GHG Protocol works with governments, industry associations, NGOs, businesses and other organizations.",
                 "GHG Protocol",
                 "https://ghgprotocol.org",
                 null),

    /**
     * The release of a harmful substance into the atmosphere.
     */
    EMISSION("Emission",
          "The release of a harmful substance into the atmosphere.",
          "Human activity is causing the release of substances into the earth's atmosphere that is impacting our climate and the health of the flora and fauna that we rely on to survive.  Reducing these emissions are the focus of sustainability initiatives in order to reduce the instability in the climate and availability of resources.",
             null,
             null,
             null),

    /**
     * A type of activity that produces CO2.
     */
    CO2_EMISSION_SCOPE("CO2 Emission Scope",
       "A type of activity that produces CO2.",
       "One aspect of sustainability is to reduce the amount of CO2 that is produced by the organization. The GHG protocol divides the reporting of CO2 emissions into three scopes: Scope 1, Scope 2 and Scope 3, to make it easier to monitor and build plans to reduce emissions.",
                       null,
                       null,
                       null),

    /**
     * A physical location that Coco Pharmaceuticals operates from.
     */
    SITE("Site",
       "A physical location that Coco Pharmaceuticals operates from.",
       "Coco Pharmaceuticals has a number of physical premises that is operates from.  Each of these premises is called a *site*",
         null,
         null,
         null),

    /**
     * The facility type captures a particular type of operation that Coco Pharmaceuticals has running at one of its sites.
     */
    FACILITY("Facility",
       "The facility type captures a particular type of operation that Coco Pharmaceuticals has running at one of its sites.",
       "Each type of facility, such as manufacturing, research, offices, ..., needs different equipment and are likely to have different sustainability challenges.  Therefore by breaking down the activity at each site into facilities, it is possible to create a separate focus on each type of facility.",
                  null,
                  null,
             null),

    /**
     * Graphical summary of Coco Pharmaceuticals' sustainability data.
     */
    SUSTAINABILITY_DASHBOARD("Sustainability Dashboard",
          "Graphical summary of Coco Pharmaceuticals' sustainability data.",
          "The sustainability dashboard provides detailed information about the impact of the different activities undertaken by Coco Pharmaceuticals' and how this impact is changing over time.",
                             null,
                             null,
                             null),

    /**
     * Carbon dioxide (chemical formula CO2) is a chemical compound made up of molecules that each have one carbon atom covalently double bonded to two oxygen atoms.
     */
    CARBON_DIOXIDE("Carbon Dioxide",
                   "Carbon dioxide (chemical formula CO2) is a chemical compound made up of molecules that each have one carbon atom covalently double bonded to two oxygen atoms.",
                   "Carbon dioxide is found in the gas state at room temperature, and as the source of available carbon in the carbon cycle, atmospheric CO2 is the primary carbon source for life on Earth. In the air, carbon dioxide is transparent to visible light but absorbs infrared radiation, acting as a greenhouse gas. Carbon dioxide is soluble in water and is found in groundwater, lakes, ice caps, and seawater. When carbon dioxide dissolves in water, it forms carbonate and mainly bicarbonate (HCO−3), which causes ocean acidification as atmospheric CO2 levels increase.",
                             "CO2",
                             "https://en.wikipedia.org/wiki/Carbon_dioxide",
                   GlossaryCategoryDefinition.CHEMICALS),

    /**
     * Hydrofluorocarbons (HFCs) are man-made organic compounds that contain fluorine and hydrogen atoms, and are the most common type of organofluorine compounds.
     */
    HFC("Hydrofluorocarbon",
                   "Hydrofluorocarbons (HFCs) are man-made organic compounds that contain fluorine and hydrogen atoms, and are the most common type of organofluorine compounds.",
                   "Most HFCs are gases at room temperature and pressure. They are frequently used in air conditioning and as refrigerants; R-134a (1,1,1,2-tetrafluoroethane) is one of the most commonly used HFC refrigerants. In order to aid the recovery of the stratospheric ozone layer, HFCs were adopted to replace the more potent chlorofluorocarbons (CFCs), which were phased out from use by the Montreal Protocol, and hydrochlorofluorocarbons (HCFCs) which are presently being phased out.[1] HFCs replaced older chlorofluorocarbons such as R-12 and hydrochlorofluorocarbons such as R-21.[2] HFCs are also used in insulating foams, aerosol propellants, as solvents and for fire protection." +
                           "They may not harm the ozone layer as much as the compounds they replace, but they still contribute to global warming --- with some like trifluoromethane having 11,700 times the warming potential of carbon dioxide.[3] Their atmospheric concentrations and contribution to anthropogenic greenhouse gas emissions are rapidly increasing[quantify], causing international concern about their radiative forcing. ",
                   "HFC",
                   "https://en.wikipedia.org/wiki/Hydrofluorocarbon",
                   GlossaryCategoryDefinition.CHEMICALS),

    ;

    private final String name;
    private final String summary;
    private final String description;
    private final String abbreviation;
    private final String url;
    private final GlossaryCategoryDefinition category;


    /**
     * The constructor creates an instance of the enum
     *
     * @param name   unique id for the enum
     * @param summary   name for the enum
     * @param description   description of the use of this value
     * @param abbreviation optional abbreviation
     * @param url optional url for the term
     * @param category optional category for the term
     */
    GlossaryTermDefinition(String                     name,
                           String                     summary,
                           String                     description,
                           String                     abbreviation,
                           String                     url,
                           GlossaryCategoryDefinition category)
    {
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.abbreviation = abbreviation;
        this.url = url;
        this.category = category;
    }

    public String getQualifiedName()
    {
        return "GlossaryTerm:" + name;
    }


    public String getName()
    {
        return name;
    }


    public String getSummary()
    {
        return summary;
    }


    public String getDescription()
    {
        return description;
    }


    public String getAbbreviation()
    {
        return abbreviation;
    }


    public String getUrl() { return url; }


    public GlossaryCategoryDefinition getCategory() { return category; }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "GlossaryTermDefinition{" + summary + '}';
    }
}
