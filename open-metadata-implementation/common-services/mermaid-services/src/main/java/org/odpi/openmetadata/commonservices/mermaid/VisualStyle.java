/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

/**
 * Capture combinations of colours and shapes to use in mermaid diagrams.
 */
public enum VisualStyle
{
    DESCRIPTION(Colour.BLACK.getColourNumber(), Colour.SAND.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), "text"),

    MEMENTO(Colour.SLATE.getColourNumber(), Colour.GHOST_WHITE.getColourNumber(), Colour.SLATE.getColourNumber(),"notch-pent"),
    TEMPLATE(Colour.SLATE.getColourNumber(), Colour.POWDER_BLUE.getColourNumber(), Colour.SLATE.getColourNumber(),"card"),

    PRINCIPAL_ASSET(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.SLATE.getColourNumber(), "hex"),
    ANCHOR_ELEMENT(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    ANCHORED_ELEMENT(Colour.BLACK.getColourNumber(), Colour.LIGHT_YELLOW.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    LINKED_ELEMENT(Colour.BLACK.getColourNumber(), Colour.EGERIA_BLUE.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    LINEAGE_ELEMENT(Colour.BLACK.getColourNumber(), Colour.LIGHT_AQUA.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    LINEAGE_ANCHOR(Colour.BLACK.getColourNumber(), Colour.LIGHT_YELLOW.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),

    HOST(Colour.WHITE.getColourNumber(), Colour.DARK_GRAY.getColourNumber(), Colour.YELLOW.getColourNumber(), "rect"),

    INFORMATION_SUPPLY_CHAIN(Colour.WHITE.getColourNumber(), Colour.SLATE.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), "flip-tri"),
    INFORMATION_SUPPLY_CHAIN_SEG(Colour.SLATE.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), Colour.SLATE.getColourNumber(), "hex"),
    INFORMATION_SUPPLY_CHAIN_SEG_GRAPH(Colour.SLATE.getColourNumber(), Colour.WHITE.getColourNumber(), Colour.SLATE.getColourNumber(), "hex"),
    INFORMATION_SUPPLY_CHAIN_IMPL(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),

    WHITE_SUBGRAPH(Colour.WHITE.getColourNumber(), Colour.WHITE.getColourNumber(), Colour.WHITE.getColourNumber(), "hex"),


    SOLUTION_BLUEPRINT(Colour.WHITE.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "lin-doc"),
    SOLUTION_BLUEPRINT_GRAPH(Colour.WHITE.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "lin-doc"),
    SOLUTION_ROLE(Colour.WHITE.getColourNumber(), Colour.CERISE.getColourNumber(), Colour.PINKY.getColourNumber(), "trap-t"),
    SOLUTION_SUBGRAPH(Colour.BLACK.getColourNumber(), Colour.LIGHT_STEEL_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "trap-t"),


    DEFAULT_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    AUTOMATED_PROCESS_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "subproc"),
    THIRD_PARTY_PROCESS_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "processes"),
    MANUAL_PROCESS_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "docs"),
    DATA_STORAGE_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "lin-cyl"),
    DATA_DISTRIBUTION_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "das"),
    DOCUMENT_PUBLISHING_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "odd"),
    INSIGHT_MODEL_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.PLUM.getColourNumber(), Colour.BLACK.getColourNumber(), "stadium"),

    SOLUTION_PORT(Colour.WHITE.getColourNumber(), Colour.MAUVE.getColourNumber(), Colour.PINKY.getColourNumber(), "delay"),

    GOVERNANCE_ACTION_PROCESS(Colour.WHITE.getColourNumber(), Colour.TEAL.getColourNumber(), Colour.BLACK.getColourNumber(), "tag-rect"),
    GOVERNANCE_ACTION_PROCESS_STEP(Colour.WHITE.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "rounded"),
    ACTION_TARGET(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    REQUEST_PARAMETERS(Colour.BLACK.getColourNumber(), Colour.SAND.getColourNumber(), Colour.BLACK.getColourNumber(), "flag"),
    FAILED_GOVERNANCE_ACTION_PROCESS_STEP(Colour.WHITE.getColourNumber(), Colour.RED.getColourNumber(), Colour.BLACK.getColourNumber(), "rounded"),


    GOVERNANCE_DEFINITION(Colour.BLACK.getColourNumber(), Colour.GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "doc"),
    SUPPORTING_GOVERNANCE_DEFINITION(Colour.BLACK.getColourNumber(), Colour.LIGHT_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "doc"),
    GOVERNANCE_METRIC(Colour.BLACK.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), Colour.BLACK.getColourNumber(), "notch-pent"),
    EXTERNAL_REFERENCES(Colour.BLACK.getColourNumber(), Colour.YELLOW.getColourNumber(), Colour.BLACK.getColourNumber(), "docs"),
    GOVERNED_ELEMENT(Colour.BLACK.getColourNumber(), Colour.LIGHT_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "rounded"),


    PROJECT(Colour.WHITE.getColourNumber(), Colour.DUSKY_ROSE.getColourNumber(), Colour.BLACK.getColourNumber(), "notch-rect"),
    RELATED_PROJECT(Colour.BLACK.getColourNumber(), Colour.PINK.getColourNumber(), Colour.BLACK.getColourNumber(), "notch-rect"),
    PROJECT_RESOURCE(Colour.BLACK.getColourNumber(), Colour.PINKY.getColourNumber(), Colour.BLACK.getColourNumber(), "doc"),
    PROJECT_ROLE(Colour.BLACK.getColourNumber(), Colour.LIGHT_ORANGE.getColourNumber(), Colour.BLACK.getColourNumber(), "trap-t"),

    DATA_STRUCTURE(Colour.BLACK.getColourNumber(), Colour.ORANGE.getColourNumber(), Colour.BLACK.getColourNumber(), "rect"),
    DATA_STRUCTURE_INTERNALS(Colour.BLACK.getColourNumber(), Colour.PALE_ORANGE.getColourNumber(), Colour.ORANGE.getColourNumber(), "rect"),
    DATA_FIELD(Colour.BLACK.getColourNumber(), Colour.WHITE_ORANGE.getColourNumber(), Colour.ORANGE.getColourNumber(), "rect"),

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
