/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

/**
 * JavaSourceTokenizer builds a "masked" copy of a java source file in which every comment and every string or
 * character literal has been replaced by spaces.  The masked copy is exactly the same length as the original,
 * so an offset found in the masked copy can be used directly on the original text.  Searching the masked copy
 * means that braces, brackets, semi-colons and commas that appear inside comments and message text are not
 * mistaken for java syntax.
 */
class JavaSourceTokenizer
{
    private final String sourceText;
    private final String maskedText;


    /**
     * Constructor - masks the supplied source.
     *
     * @param sourceText contents of a java source file
     */
    JavaSourceTokenizer(String sourceText)
    {
        this.sourceText = sourceText;
        this.maskedText = maskText(sourceText);
    }


    /**
     * Return the original source text.
     *
     * @return unchanged contents of the source file
     */
    String getSourceText() { return sourceText; }


    /**
     * Return the masked source text.  It is the same length as the source text, but all comments and literals
     * have been replaced by spaces.
     *
     * @return masked contents of the source file
     */
    String getMaskedText() { return maskedText; }


    /**
     * Replace every comment, string literal and character literal with spaces.  Newlines are preserved so that
     * line numbers can still be calculated from the masked text.
     *
     * @param sourceText contents of a java source file
     * @return masked text of the same length
     */
    private String maskText(String sourceText)
    {
        char[] masked = sourceText.toCharArray();
        int    length = sourceText.length();
        int    position = 0;

        while (position < length)
        {
            char currentChar = sourceText.charAt(position);

            if ((currentChar == '/') && (position + 1 < length) && (sourceText.charAt(position + 1) == '/'))
            {
                int endOfComment = sourceText.indexOf('\n', position);

                position = blank(masked, position, (endOfComment < 0) ? length : endOfComment);
            }
            else if ((currentChar == '/') && (position + 1 < length) && (sourceText.charAt(position + 1) == '*'))
            {
                int endOfComment = sourceText.indexOf("*/", position + 2);

                position = blank(masked, position, (endOfComment < 0) ? length : endOfComment + 2);
            }
            else if ((currentChar == '"') || (currentChar == '\''))
            {
                position = blank(masked, position, endOfLiteral(sourceText, position, currentChar));
            }
            else
            {
                position = position + 1;
            }
        }

        return new String(masked);
    }


    /**
     * Locate the end of a string or character literal.
     *
     * @param sourceText contents of a java source file
     * @param startPosition offset of the opening quote
     * @param quoteChar the quote character in use
     * @return offset of the first character after the literal
     */
    private int endOfLiteral(String sourceText, int startPosition, char quoteChar)
    {
        int length   = sourceText.length();
        int position = startPosition + 1;

        while (position < length)
        {
            char currentChar = sourceText.charAt(position);

            if (currentChar == '\\')
            {
                position = position + 2;
            }
            else if ((currentChar == quoteChar) || (currentChar == '\n'))
            {
                return position + 1;
            }
            else
            {
                position = position + 1;
            }
        }

        return length;
    }


    /**
     * Replace a range of characters with spaces, leaving any newlines in place.
     *
     * @param masked working copy of the source
     * @param startPosition first character to blank out
     * @param endPosition first character after the range
     * @return the end position, so that the caller can continue scanning from there
     */
    private int blank(char[] masked, int startPosition, int endPosition)
    {
        int limit = Math.min(endPosition, masked.length);

        for (int position = startPosition; position < limit; position++)
        {
            if (masked[position] != '\n')
            {
                masked[position] = ' ';
            }
        }

        return Math.max(limit, startPosition + 1);
    }


    /**
     * Return the offset of the character that closes the bracket at the supplied position.
     *
     * @param startPosition offset of an opening bracket in the masked text
     * @return offset of the matching closing bracket, or -1 if the source is unbalanced
     */
    int findMatchingBracket(int startPosition)
    {
        int depth = 0;

        for (int position = startPosition; position < maskedText.length(); position++)
        {
            char currentChar = maskedText.charAt(position);

            if ((currentChar == '(') || (currentChar == '[') || (currentChar == '{'))
            {
                depth = depth + 1;
            }
            else if ((currentChar == ')') || (currentChar == ']') || (currentChar == '}'))
            {
                depth = depth - 1;

                if (depth == 0)
                {
                    return position;
                }
            }
        }

        return -1;
    }
}
