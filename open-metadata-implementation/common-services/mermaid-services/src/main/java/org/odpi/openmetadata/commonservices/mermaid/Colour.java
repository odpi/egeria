/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.mermaid;

/**
 * Colour helps to capture the colours we are using in the mermaid graphs.
 */
public enum Colour
{
    BLACK       ("#000000"),
    WHITE       ("#FFFFFF"),
    CERISE      ("#AA00FF"),
    EGERIA_BLUE ("#39add1"),
    DARK_BLUE   ("#3079ab"),
    BLUE_SKY    ("#A8C2FF"),
    LIGHT_BLUE  ("#CCE5FF"),
    LIGHT_AQUA  ("#CCFFFF"),
    LIGHT_YELLOW("#FFFFCC"),
    PINKY       ("#E1D5E7"),
    MAUVE       ("#EFEFFF"),
    RED         ("#e15258"),
    ORANGE      ("#f9845b"),
    LAVENDER    ("#838cc7"),
    PURPLE      ("#7d669e"),
    TEAL        ("#53bbb4"),
    GREEN       ("#51b46d"),
    LIGHT_GREEN ("#B9E0A5"),

    MUSTARD     ("#e0ab18"),
    YELLOW      ("#FFFF88"),
    PINK        ("#f092b0"),
    LIGHT_GRAY  ("#b7c0c7"),
    DARK_GRAY   ("#637a91"),
    SLATE       ("#004563"),
    SAND        ("#F9F7ED")
    ;

    private final String colourNumber;


    /**
     * Construct each of the colours.
     *
     * @param colourNumber string
     */
    Colour(String colourNumber)
    {
        this.colourNumber = colourNumber;
    }


    /**
     * Return the colour number.
     *
     * @return string
     */
    public String getColourNumber()
    {
        return colourNumber;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "Colour{" +
                "colourNumber='" + colourNumber + '\'' +
                "} ";
    }
}
