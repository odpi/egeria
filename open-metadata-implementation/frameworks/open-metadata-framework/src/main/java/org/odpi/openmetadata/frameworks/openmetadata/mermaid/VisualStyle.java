/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.mermaid;

/**
 * Capture combinations of colours and shapes to use in mermaid diagrams.
 */
public enum VisualStyle
{
    DESCRIPTION(Colour.BLACK.getColourNumber(), Colour.SAND.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), "text"),
    MORE_ELEMENTS(Colour.BLACK.getColourNumber(), Colour.SAND.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), "stadium"),

    EXTERNAL_ID(Colour.WHITE.getColourNumber(), Colour.MEDIUM_VIOLET_RED.getColourNumber(), Colour.SLATE.getColourNumber(),"sl-rect"),
    FEEDBACK(Colour.BLACK.getColourNumber(), Colour.MEDIUM_PURPLE.getColourNumber(), Colour.SLATE.getColourNumber(),"flag"),
    TAG(Colour.YELLOW_GREEN.getColourNumber(), Colour.DARK_MAGENTA.getColourNumber(), Colour.SLATE.getColourNumber(),"delay"),
    MEMENTO(Colour.SLATE.getColourNumber(), Colour.GHOST_WHITE.getColourNumber(), Colour.SLATE.getColourNumber(),"notch-pent"),
    TEMPLATE(Colour.SLATE.getColourNumber(), Colour.POWDER_BLUE.getColourNumber(), Colour.SLATE.getColourNumber(),"card"),
    VALID_VALUE(Colour.SLATE.getColourNumber(), Colour.POWDER_BLUE.getColourNumber(), Colour.SLATE.getColourNumber(),"hex"),
    VALID_VALUE_SET(Colour.WHITE.getColourNumber(), Colour.CADET_BLUE.getColourNumber(), Colour.SLATE.getColourNumber(),"hex"),

    PRINCIPAL_ASSET(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.SLATE.getColourNumber(), "hex"),
    ASSET(Colour.BLACK.getColourNumber(), Colour.DARK_KHAKI.getColourNumber(), Colour.SLATE.getColourNumber(), "hex"),
    PROCESS(Colour.BLACK.getColourNumber(), Colour.DARK_KHAKI.getColourNumber(), Colour.SLATE.getColourNumber(), "subproc"),
    DEPLOYED_API(Colour.BLACK.getColourNumber(), Colour.DARK_KHAKI.getColourNumber(), Colour.SLATE.getColourNumber(), "odd"),
    CONNECTOR(Colour.BLACK.getColourNumber(), Colour.DARK_KHAKI.getColourNumber(), Colour.SLATE.getColourNumber(), "stadium"),
    IT_ASSET(Colour.BLACK.getColourNumber(), Colour.DARK_KHAKI.getColourNumber(), Colour.SLATE.getColourNumber(), "hex"),
    DATA_ASSET(Colour.BLACK.getColourNumber(), Colour.DARK_KHAKI.getColourNumber(), Colour.SLATE.getColourNumber(), "cyl"),
    FILE_FOLDER(Colour.BLACK.getColourNumber(), Colour.DARK_KHAKI.getColourNumber(), Colour.SLATE.getColourNumber(), "div-rect"),
    DATA_FILE(Colour.BLACK.getColourNumber(), Colour.DARK_KHAKI.getColourNumber(), Colour.SLATE.getColourNumber(), "tag-doc"),
    DATA_STORE(Colour.BLACK.getColourNumber(), Colour.DARK_KHAKI.getColourNumber(), Colour.SLATE.getColourNumber(), "lin-cyl"),
    DEPLOYED_DB_SCHEMA(Colour.BLACK.getColourNumber(), Colour.DARK_KHAKI.getColourNumber(), Colour.SLATE.getColourNumber(), "win-pane"),
    ANCHOR_ELEMENT(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    ANCHORED_ELEMENT(Colour.BLACK.getColourNumber(), Colour.LIGHT_YELLOW.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    LINKED_ELEMENT(Colour.BLACK.getColourNumber(), Colour.EGERIA_BLUE.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    LINEAGE_ELEMENT(Colour.BLACK.getColourNumber(), Colour.LIGHT_AQUA.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    LINEAGE_ANCHOR(Colour.BLACK.getColourNumber(), Colour.LIGHT_YELLOW.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    REQUEST_FOR_ACTION(Colour.LIGHT_YELLOW.getColourNumber(), Colour.ROSY_BROWN.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    TO_DO(Colour.LIGHT_YELLOW.getColourNumber(), Colour.ROSY_BROWN.getColourNumber(), Colour.SLATE.getColourNumber(), "trap-b"),

    HOST(Colour.WHITE.getColourNumber(), Colour.DARK_GRAY.getColourNumber(), Colour.YELLOW.getColourNumber(), "rect"),

    INFORMATION_SUPPLY_CHAIN(Colour.WHITE.getColourNumber(), Colour.SLATE.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), "flip-tri"),
    INFORMATION_SUPPLY_CHAIN_SEG(Colour.SLATE.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), Colour.SLATE.getColourNumber(), "flip-tri"),
    PRINCIPLE_INFORMATION_SUPPLY_CHAIN(Colour.WHITE.getColourNumber(), Colour.SLATE.getColourNumber(), Colour.MUSTARD.getColourNumber(), "flip-tri"),
    INFORMATION_SUPPLY_CHAIN_IMPL(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),

    WHITE_SUBGRAPH(Colour.WHITE.getColourNumber(), Colour.WHITE.getColourNumber(), Colour.WHITE.getColourNumber(), "hex"),


    SOLUTION_BLUEPRINT(Colour.BLACK.getColourNumber(), Colour.DEEP_SKY_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "lin-doc"),
    SOLUTION_BLUEPRINT_GRAPH(Colour.WHITE.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "lin-doc"),
    SOLUTION_ROLE(Colour.WHITE.getColourNumber(), Colour.CERISE.getColourNumber(), Colour.PINKY.getColourNumber(), "trap-t"),
    SOLUTION_SUBGRAPH(Colour.BLACK.getColourNumber(), Colour.LIGHT_STEEL_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "trap-t"),


    DEFAULT_SOLUTION_COMPONENT(Colour.BLACK.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    AUTOMATED_PROCESS_SOLUTION_COMPONENT(Colour.BLACK.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "subproc"),
    THIRD_PARTY_PROCESS_SOLUTION_COMPONENT(Colour.BLACK.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "processes"),
    MANUAL_PROCESS_SOLUTION_COMPONENT(Colour.BLACK.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "docs"),
    DATA_STORAGE_SOLUTION_COMPONENT(Colour.BLACK.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "lin-cyl"),
    DATA_DISTRIBUTION_SOLUTION_COMPONENT(Colour.BLACK.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "das"),
    DOCUMENT_PUBLISHING_SOLUTION_COMPONENT(Colour.BLACK.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "odd"),
    INSIGHT_MODEL_SOLUTION_COMPONENT(Colour.BLACK.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "stadium"),

    SOLUTION_PORT(Colour.WHITE.getColourNumber(), Colour.MAUVE.getColourNumber(), Colour.PINKY.getColourNumber(), "delay"),

    GOVERNANCE_ACTION(Colour.BLACK.getColourNumber(), Colour.TURQUOISE.getColourNumber(), Colour.BLACK.getColourNumber(), "tag-rect"),
    GOVERNANCE_ACTION_PROCESS_STEP(Colour.WHITE.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "rounded"),
    ENGINE_ACTION(Colour.BLACK.getColourNumber(), Colour.AQUAMARINE.getColourNumber(), Colour.BLACK.getColourNumber(), "trap-t"),
    ACTION_TARGET(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    REQUEST_PARAMETERS(Colour.BLACK.getColourNumber(), Colour.SAND.getColourNumber(), Colour.BLACK.getColourNumber(), "flag"),
    FAILED_GOVERNANCE_ACTION_PROCESS_STEP(Colour.WHITE.getColourNumber(), Colour.RED.getColourNumber(), Colour.BLACK.getColourNumber(), "rounded"),


    GOVERNANCE_DEFINITION(Colour.WHITE.getColourNumber(), Colour.DARK_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "doc"),
    SUPPORTING_GOVERNANCE_DEFINITION(Colour.BLACK.getColourNumber(), Colour.LIGHT_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "doc"),
    GOVERNANCE_METRIC(Colour.BLACK.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), Colour.BLACK.getColourNumber(), "notch-pent"),
    EXTERNAL_REFERENCE(Colour.BLACK.getColourNumber(), Colour.YELLOW.getColourNumber(), Colour.BLACK.getColourNumber(), "docs"),
    GOVERNED_ELEMENT(Colour.BLACK.getColourNumber(), Colour.LIGHT_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "rounded"),
    GOVERNANCE_ACTOR(Colour.BLACK.getColourNumber(), Colour.LIGHT_ORANGE.getColourNumber(), Colour.BLACK.getColourNumber(), "trap-t"),
    GOVERNANCE_TEAM(Colour.BLACK.getColourNumber(), Colour.LIGHT_ORANGE.getColourNumber(), Colour.BLACK.getColourNumber(), "tri"),
    USER_IDENTITY(Colour.BLACK.getColourNumber(), Colour.DARK_ORANGE.getColourNumber(), Colour.BLACK.getColourNumber(), "bow-rect"),


    PRINCIPAL_PROJECT(Colour.WHITE.getColourNumber(), Colour.DUSKY_ROSE.getColourNumber(), Colour.BLACK.getColourNumber(), "notch-rect"),
    PROJECT(Colour.BLACK.getColourNumber(), Colour.PINK.getColourNumber(), Colour.BLACK.getColourNumber(), "notch-rect"),
    PROJECT_RESOURCE(Colour.BLACK.getColourNumber(), Colour.PINKY.getColourNumber(), Colour.BLACK.getColourNumber(), "doc"),
    PROJECT_ROLE(Colour.BLACK.getColourNumber(), Colour.LIGHT_ORANGE.getColourNumber(), Colour.BLACK.getColourNumber(), "trap-t"),

    AGREEMENT(Colour.BLACK.getColourNumber(), Colour.YELLOW_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    DATA_SHARING_AGREEMENT(Colour.WHITE.getColourNumber(), Colour.OLIVE_DRAB.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    DIGITAL_SUBSCRIPTION(Colour.BLACK.getColourNumber(), Colour.DARK_OLIVE_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),

    DATA_DICTIONARY(Colour.BLACK.getColourNumber(), Colour.CHOCOLATE.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    DATA_SPEC(Colour.BLACK.getColourNumber(), Colour.BURLY_WOOD.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    OBJECT_IDENTIFIER(Colour.BLACK.getColourNumber(), Colour.WHITE_ORANGE.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),

    DATA_STRUCTURE(Colour.BLACK.getColourNumber(), Colour.ORANGE.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    DATA_STRUCTURE_INTERNALS(Colour.BLACK.getColourNumber(), Colour.PALE_ORANGE.getColourNumber(), Colour.ORANGE.getColourNumber(), "rect"),
    DATA_FIELD(Colour.BLACK.getColourNumber(), Colour.WHITE_ORANGE.getColourNumber(), Colour.ORANGE.getColourNumber(), "rect"),
    DATA_CLASS(Colour.BLACK.getColourNumber(), Colour.CORNFLOWER_BLUE.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "rect"),

    COLLECTION(Colour.BLACK.getColourNumber(), Colour.MINT_CREAM.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    ROOT_COLLECTION(Colour.BLACK.getColourNumber(), Colour.SEA_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    FOLDER(Colour.BLACK.getColourNumber(), Colour.DARK_SEA_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    HOME_COLLECTION(Colour.BLACK.getColourNumber(), Colour.MEDIUM_SEA_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    RESULTS_SET(Colour.BLACK.getColourNumber(), Colour.SPRING_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    RECENT_ACCESS(Colour.BLACK.getColourNumber(), Colour.LIME_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    WORK_ITEM_LIST(Colour.BLACK.getColourNumber(), Colour.FOREST_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),

    DIGITAL_PRODUCT(Colour.BLACK.getColourNumber(), Colour.LAVENDER.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "rect"),


    GLOSSARY_TERM(Colour.BLACK.getColourNumber(), Colour.MEDIUM_AQUAMARINE.getColourNumber(), Colour.TEAL.getColourNumber(), "rect"),
    GLOSSARY(Colour.WHITE.getColourNumber(), Colour.TEAL.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),

    SCHEMA_ELEMENT(Colour.LIGHT_YELLOW.getColourNumber(), Colour.PURPLE.getColourNumber(), Colour.LIGHT_AQUA.getColourNumber(), "rect"),

    ;



    private final String textColour;
    private final String fillColour;
    private final String lineColour;
    private final String shape;


    VisualStyle(String textColour, String fillColour, String lineColour, String shape)
    {
        this.textColour = textColour;
        this.fillColour = fillColour;
        this.lineColour = lineColour;
        this.shape      = shape;
    }


    /**
     * Return the colour number to use for the text.
     *
     * @return string (eg #FFFFFF)
     */
    public String getTextColour()
    {
        return textColour;
    }


    /**
     * Return the colour number to use for the text.
     *
     * @return string (eg #FFFFFF)
     */
    public String getFillColour()
    {
        return fillColour;
    }


    /**
     * Return the colour number to use for the text.
     *
     * @return string (eg #FFFFFF)
     */
    public String getLineColour()
    {
        return lineColour;
    }

    /**
     * Return the associated shape.
     *
     * @return string shape name
     */
    public String getShape()
    {
        return shape;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "VisualStyle{" +
                "textColour='" + textColour + '\'' +
                ", fillColour='" + fillColour + '\'' +
                ", lineColour='" + lineColour + '\'' +
                ", shape='" + shape + '\'' +
                "} ";
    }
}
