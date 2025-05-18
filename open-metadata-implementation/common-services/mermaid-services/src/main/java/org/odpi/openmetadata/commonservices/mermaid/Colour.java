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
    CORNFLOWER_BLUE  ("#6495ed"),
    BLUE_SKY    ("#A8C2FF"),
    POWDER_BLUE("#b0e0e6"),
    LIGHT_BLUE  ("#CCE5FF"),
    STEEL_BLUE  ("#04682b4"),
    LIGHT_STEEL_BLUE("#b0c4de"),
    PALE_TURQUOISE("#afeeee"),
    LIGHT_AQUA  ("#CCFFFF"),
    TURQUOISE   ("#53bbb4"),
    MEDIUM_AQUAMARINE("#66cdaa"),
    TEAL        ("#008080"),


    LIGHT_YELLOW("#FFFFCC"),

    RED         ("#e15258"),


    ROSY_BROWN  ("#bc8f8f"),
    PINKY       ("#E1D5E7"),
    DUSKY_ROSE  ("#B5739D"),
    MAUVE       ("#EFEFFF"),
    LAVENDER    ("#838cc7"),
    PLUM        ("#dda0dd"),
    PURPLE      ("#7d669e"),


    DARK_GREEN  ("#006400"),
    GREEN       ("#008000"),
    LIGHT_GREEN ("#90ee90"),
    LIME        ("#00ff00"),

    LIME_GREEN("#32cd32"),
    FOREST_GREEN("#228b22"),
    DARK_SEA_GREEN("#8fbc8f"),
    SEA_GREEN("#2e8b57"),
    MEDIUM_SEA_GREEN("#3cb371"),
    SPRING_GREEN("#00ff7f"),
    MINT_CREAM("#f5fffa"),

    BURLY_WOOD  ("#deb887"),
    CHOCOLATE   ("#d2691e"),

    MUSTARD     ("#e0ab18"),
    YELLOW      ("#FFFF88"),
    PINK        ("#f092b0"),
    LIGHT_GRAY  ("#b7c0c7"),
    DARK_GRAY   ("#637a91"),
    SLATE       ("#004563"),
    SAND        ("#F9F7ED"),

    GHOST_WHITE ("#f8f8ff"),

    LIGHT_ORANGE("#FFE599"),
    ORANGE      ("#f9845b"),
    WHITE_ORANGE ("#ffe3cc"),
    PALE_ORANGE  ("#ffab66"),
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
