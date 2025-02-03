/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

/**
 * Capture combinations of colours and shapes to use in mermaid diagrams.
 */
public enum VisualStyle
{
    DESCRIPTION(Colour.BLACK.getColourNumber(), Colour.SAND.getColourNumber(), Colour.LIGHT_GRAY.getColourNumber(), "text"),

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
