/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

/**
 * Capture combinations of colours and shapes to use in mermaid diagrams.
 */
public enum VisualStyle
{
    DESCRIPTION(Colour.BLACK.getColourNumber(), Colour.SAND.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), "text"),

    PRINCIPAL_ASSET(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.SLATE.getColourNumber(), "hex"),
    ANCHOR_ASSET(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    ANCHORED_ELEMENT(Colour.WHITE.getColourNumber(), Colour.EGERIA_BLUE.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    LINKED_ELEMENT(Colour.BLACK.getColourNumber(), Colour.LIGHT_AQUA.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),
    LINEAGE_ELEMENT(Colour.BLACK.getColourNumber(), Colour.LIGHT_AQUA.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),

    HOST(Colour.WHITE.getColourNumber(), Colour.DARK_GRAY.getColourNumber(), Colour.YELLOW.getColourNumber(), "rect"),

    INFORMATION_SUPPLY_CHAIN(Colour.WHITE.getColourNumber(), Colour.SLATE.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), "flip-tri"),
    INFORMATION_SUPPLY_CHAIN_SEG(Colour.SLATE.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), Colour.SLATE.getColourNumber(), "hex"),
    INFORMATION_SUPPLY_CHAIN_IMPL(Colour.BLACK.getColourNumber(), Colour.MUSTARD.getColourNumber(), Colour.SLATE.getColourNumber(), "rounded"),


    SOLUTION_BLUEPRINT(Colour.DARK_BLUE.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "notch-rect"),
    SOLUTION_ROLE(Colour.WHITE.getColourNumber(), Colour.CERISE.getColourNumber(), Colour.PINKY.getColourNumber(), "trap-t"),


    DEFAULT_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.LAVENDER.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "rect"),
    AUTOMATED_PROCESS_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.LAVENDER.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "subproc"),
    THIRD_PARTY_PROCESS_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.LAVENDER.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "processes"),
    MANUAL_PROCESS_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.LAVENDER.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "docs"),
    DATA_STORAGE_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.LAVENDER.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "lin-cyl"),
    DATA_DISTRIBUTION_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.LAVENDER.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "das"),
    DOCUMENT_PUBLISHING_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.LAVENDER.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "odd"),
    INSIGHT_MODEL_SOLUTION_COMPONENT(Colour.WHITE.getColourNumber(), Colour.LAVENDER.getColourNumber(), Colour.DARK_BLUE.getColourNumber(), "stadium"),

    SOLUTION_PORT(Colour.WHITE.getColourNumber(), Colour.MAUVE.getColourNumber(), Colour.PINKY.getColourNumber(), "delay"),

    GOVERNANCE_ACTION_PROCESS(Colour.WHITE.getColourNumber(), Colour.TEAL.getColourNumber(), Colour.BLACK.getColourNumber(), "notch-rect"),
    GOVERNANCE_ACTION_PROCESS_STEP(Colour.WHITE.getColourNumber(), Colour.PINK.getColourNumber(), Colour.BLACK.getColourNumber(), "rounded"),


    GOVERNANCE_DEFINITION(Colour.BLACK.getColourNumber(), Colour.GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "doc"),
    SUPPORTING_GOVERNANCE_DEFINITION(Colour.BLACK.getColourNumber(), Colour.LIGHT_GREEN.getColourNumber(), Colour.BLACK.getColourNumber(), "doc"),
    GOVERNANCE_METRIC(Colour.BLACK.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), Colour.BLACK.getColourNumber(), "notch-pent"),
    EXTERNAL_REFERENCES(Colour.BLACK.getColourNumber(), Colour.YELLOW.getColourNumber(), Colour.BLACK.getColourNumber(), "lin-doc"),
    GOVERNED_ELEMENT(Colour.BLACK.getColourNumber(), Colour.LIGHT_BLUE.getColourNumber(), Colour.BLACK.getColourNumber(), "rounded"),


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
