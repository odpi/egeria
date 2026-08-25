/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MessageSetParser extracts the message definitions from the source of a message set enum.
 * <br><br>
 * The values in a message definition are literal constants in the enum's source, so they are read straight out
 * of the source file rather than by loading the class.  This means that the documentation covers every message
 * set in the repository - including the ones in modules that are not part of the OMAG Server Platform - without
 * this utility needing a dependency on each of them.
 * <br><br>
 * The parser is deliberately strict.  If it meets a message set that it does not understand it throws a
 * {@link MessageSetParsingException} so that the build fails rather than the documentation silently losing a
 * message.
 */
class MessageSetParser
{
    private static final Pattern PACKAGE_PATTERN     = Pattern.compile("^\\s*package\\s+([\\w.]+)\\s*;", Pattern.MULTILINE);
    private static final Pattern ENUM_PATTERN        = Pattern.compile("\\benum\\s+(\\w+)\\s+implements\\s+(\\w+)\\s*\\{");
    private static final Pattern CONSTANT_PATTERN    = Pattern.compile("([A-Z][A-Z0-9_]*)\\s*\\(");
    private static final Pattern STRING_PART_PATTERN = Pattern.compile("\"((?:\\\\.|[^\"\\\\])*)\"");
    private static final Pattern SEVERITY_PATTERN    = Pattern.compile("[\\w.]*?(\\w+)\\s*$");

    /*
     * The order of the values passed to each type of message definition.  These match the constructors of
     * ExceptionMessageDefinition, AuditLogMessageDefinition and MessageDefinition.
     */
    private static final int EXCEPTION_VALUE_COUNT    = 5;
    private static final int AUDIT_LOG_VALUE_COUNT    = 5;
    private static final int NOTIFICATION_VALUE_COUNT = 4;

    /*
     * A message definition may add a link to further reading as its last value.  It is optional because
     * there is not always a page that says anything useful about the situation the message describes.
     */
    private static final int OPTIONAL_URL_VALUE = 1;


    /**
     * Parse the source of a message set enum.
     *
     * @param sourcePath path of the source file, relative to the repository root - used in the documentation
     *                   and in any error messages
     * @param sourceText contents of the source file
     * @return description of the message set, or null if this source file does not define a message set
     * @throws MessageSetParsingException the source file defines a message set that the parser cannot read
     */
    MessageSetDescription parseMessageSet(String sourcePath,
                                          String sourceText) throws MessageSetParsingException
    {
        JavaSourceTokenizer tokenizer  = new JavaSourceTokenizer(sourceText);
        String              maskedText = tokenizer.getMaskedText();
        Matcher             enumMatcher = ENUM_PATTERN.matcher(maskedText);

        if (! enumMatcher.find())
        {
            return null;
        }

        String         enumName       = enumMatcher.group(1);
        MessageSetType messageSetType = MessageSetType.getMessageSetType(enumMatcher.group(2));

        if (messageSetType == null)
        {
            return null;
        }

        Matcher packageMatcher = PACKAGE_PATTERN.matcher(maskedText);
        String  packageName    = packageMatcher.find() ? packageMatcher.group(1) : "";

        int bodyStart    = enumMatcher.end();
        int constantsEnd = findEndOfConstants(maskedText, bodyStart);

        if (constantsEnd < 0)
        {
            throw new MessageSetParsingException(sourcePath,
                                                 "the end of the enum's constant list could not be found");
        }

        List<MessageDescription> messages = parseConstants(sourcePath,
                                                           tokenizer,
                                                           messageSetType,
                                                           bodyStart,
                                                           constantsEnd);

        return new MessageSetDescription(enumName,
                                         packageName,
                                         messageSetType,
                                         sourcePath,
                                         extractClassDescription(sourceText, enumMatcher.start()),
                                         messages);
    }


    /**
     * Locate the semi-colon that ends the enum's list of constants.  It is the first semi-colon that is not
     * inside a nested bracket.
     *
     * @param maskedText masked source
     * @param bodyStart offset of the first character after the enum's opening brace
     * @return offset of the semi-colon, or -1 if it cannot be found
     */
    private int findEndOfConstants(String maskedText, int bodyStart)
    {
        int depth = 0;

        for (int position = bodyStart; position < maskedText.length(); position++)
        {
            char currentChar = maskedText.charAt(position);

            if ((currentChar == '(') || (currentChar == '[') || (currentChar == '{'))
            {
                depth = depth + 1;
            }
            else if ((currentChar == ')') || (currentChar == ']') || (currentChar == '}'))
            {
                if (depth == 0)
                {
                    return -1;
                }

                depth = depth - 1;
            }
            else if ((currentChar == ';') && (depth == 0))
            {
                return position;
            }
        }

        return -1;
    }


    /**
     * Work through the enum constants, turning each one into a message description.
     *
     * @param sourcePath path of the source file - used in any error messages
     * @param tokenizer masked and unmasked source
     * @param messageSetType which of the message set interfaces the enum implements
     * @param bodyStart offset of the first character after the enum's opening brace
     * @param constantsEnd offset of the semi-colon that ends the constant list
     * @return the message descriptions, in declaration order
     * @throws MessageSetParsingException one of the constants could not be read
     */
    private List<MessageDescription> parseConstants(String              sourcePath,
                                                    JavaSourceTokenizer tokenizer,
                                                    MessageSetType      messageSetType,
                                                    int                 bodyStart,
                                                    int                 constantsEnd) throws MessageSetParsingException
    {
        List<MessageDescription> messages   = new ArrayList<>();
        String                   maskedText = tokenizer.getMaskedText();
        int                      position   = bodyStart;

        while (position < constantsEnd)
        {
            if (! startsNewConstant(maskedText, position, bodyStart))
            {
                position = position + 1;
                continue;
            }

            Matcher constantMatcher = CONSTANT_PATTERN.matcher(maskedText);

            if ((! constantMatcher.find(position)) || (constantMatcher.start() != position))
            {
                position = position + 1;
                continue;
            }

            String constantName        = constantMatcher.group(1);
            int    openingBracket      = constantMatcher.end() - 1;
            int    closingBracket      = tokenizer.findMatchingBracket(openingBracket);

            if ((closingBracket < 0) || (closingBracket > constantsEnd))
            {
                throw new MessageSetParsingException(sourcePath,
                                                     "the values of message definition " + constantName +
                                                             " are not enclosed in matching brackets");
            }

            messages.add(parseConstant(sourcePath,
                                       tokenizer,
                                       messageSetType,
                                       constantName,
                                       openingBracket + 1,
                                       closingBracket));

            position = closingBracket + 1;
        }

        return messages;
    }


    /**
     * Is the supplied offset the start of an enum constant?  An enum constant is the first thing on its line
     * and is followed by its list of values.
     *
     * @param maskedText masked source
     * @param position offset being tested
     * @param bodyStart offset of the first character after the enum's opening brace
     * @return true if a constant starts here
     */
    private boolean startsNewConstant(String maskedText, int position, int bodyStart)
    {
        char currentChar = maskedText.charAt(position);

        if ((currentChar < 'A') || (currentChar > 'Z'))
        {
            return false;
        }

        int lineStart = maskedText.lastIndexOf('\n', position - 1) + 1;

        if (lineStart < bodyStart)
        {
            lineStart = bodyStart;
        }

        return maskedText.substring(lineStart, position).isBlank();
    }


    /**
     * Turn the values of a single enum constant into a message description.
     *
     * @param sourcePath path of the source file - used in any error messages
     * @param tokenizer masked and unmasked source
     * @param messageSetType which of the message set interfaces the enum implements
     * @param constantName name of the enum constant
     * @param valuesStart offset of the first character after the opening bracket
     * @param valuesEnd offset of the closing bracket
     * @return message description
     * @throws MessageSetParsingException the values could not be read
     */
    private MessageDescription parseConstant(String              sourcePath,
                                             JavaSourceTokenizer tokenizer,
                                             MessageSetType      messageSetType,
                                             String              constantName,
                                             int                 valuesStart,
                                             int                 valuesEnd) throws MessageSetParsingException
    {
        List<String> values        = splitValues(tokenizer, valuesStart, valuesEnd);
        int          expectedCount = switch (messageSetType)
                                     {
                                         case EXCEPTION    -> EXCEPTION_VALUE_COUNT;
                                         case AUDIT_LOG    -> AUDIT_LOG_VALUE_COUNT;
                                         case NOTIFICATION -> NOTIFICATION_VALUE_COUNT;
                                     };

        if ((values.size() != expectedCount) && (values.size() != expectedCount + OPTIONAL_URL_VALUE))
        {
            throw new MessageSetParsingException(sourcePath,
                                                 "message definition " + constantName + " supplies " +
                                                         values.size() + " values but a " +
                                                         messageSetType.getInterfaceName() + " needs " +
                                                         expectedCount + " or " +
                                                         (expectedCount + OPTIONAL_URL_VALUE));
        }

        String url = null;

        if ((values.size() > expectedCount) && (! "null".equals(values.get(expectedCount).trim())))
        {
            url = getStringValue(sourcePath, constantName, "url", values.get(expectedCount));
        }

        return switch (messageSetType)
               {
                   case EXCEPTION -> new MessageDescription(constantName,
                                                            getStringValue(sourcePath, constantName, "messageId", values.get(1)),
                                                            getStringValue(sourcePath, constantName, "messageTemplate", values.get(2)),
                                                            getStringValue(sourcePath, constantName, "systemAction", values.get(3)),
                                                            getStringValue(sourcePath, constantName, "userAction", values.get(4)),
                                                            getIntValue(sourcePath, constantName, "httpErrorCode", values.get(0)),
                                                            null,
                                                            url);

                   case AUDIT_LOG -> new MessageDescription(constantName,
                                                            getStringValue(sourcePath, constantName, "messageId", values.get(0)),
                                                            getStringValue(sourcePath, constantName, "messageTemplate", values.get(2)),
                                                            getStringValue(sourcePath, constantName, "systemAction", values.get(3)),
                                                            getStringValue(sourcePath, constantName, "userAction", values.get(4)),
                                                            0,
                                                            getSeverityValue(sourcePath, constantName, values.get(1)),
                                                            url);

                   case NOTIFICATION -> new MessageDescription(constantName,
                                                               getStringValue(sourcePath, constantName, "messageId", values.get(0)),
                                                               getStringValue(sourcePath, constantName, "messageTemplate", values.get(1)),
                                                               getStringValue(sourcePath, constantName, "systemAction", values.get(2)),
                                                               getStringValue(sourcePath, constantName, "userAction", values.get(3)),
                                                               0,
                                                               null,
                                                               url);
               };
    }


    /**
     * Split the values of an enum constant at the commas that are not nested inside brackets.
     *
     * @param tokenizer masked and unmasked source
     * @param valuesStart offset of the first character after the opening bracket
     * @param valuesEnd offset of the closing bracket
     * @return the values, as they appear in the source
     */
    private List<String> splitValues(JavaSourceTokenizer tokenizer, int valuesStart, int valuesEnd)
    {
        List<String> values     = new ArrayList<>();
        String       maskedText = tokenizer.getMaskedText();
        String       sourceText = tokenizer.getSourceText();
        int          depth      = 0;
        int          valueStart = valuesStart;

        for (int position = valuesStart; position < valuesEnd; position++)
        {
            char currentChar = maskedText.charAt(position);

            if ((currentChar == '(') || (currentChar == '[') || (currentChar == '{'))
            {
                depth = depth + 1;
            }
            else if ((currentChar == ')') || (currentChar == ']') || (currentChar == '}'))
            {
                depth = depth - 1;
            }
            else if ((currentChar == ',') && (depth == 0))
            {
                values.add(sourceText.substring(valueStart, position).trim());
                valueStart = position + 1;
            }
        }

        values.add(sourceText.substring(valueStart, valuesEnd).trim());

        return values;
    }


    /**
     * Convert a java string expression into the string that it evaluates to.  Message text is often built by
     * concatenating several string literals so that the source stays within a sensible line length.
     *
     * @param sourcePath path of the source file - used in any error messages
     * @param constantName name of the enum constant - used in any error messages
     * @param valueName name of the value being read - used in any error messages
     * @param expression the value as it appears in the source
     * @return the value of the expression
     * @throws MessageSetParsingException the expression is not a series of string literals
     */
    private String getStringValue(String sourcePath,
                                  String constantName,
                                  String valueName,
                                  String expression) throws MessageSetParsingException
    {
        StringBuilder stringValue = new StringBuilder();
        Matcher       matcher     = STRING_PART_PATTERN.matcher(expression);
        int           consumed    = 0;

        while (matcher.find())
        {
            if (! expression.substring(consumed, matcher.start()).replace("+", "").isBlank())
            {
                throw new MessageSetParsingException(sourcePath,
                                                     "the " + valueName + " of message definition " +
                                                             constantName + " is not a simple string constant");
            }

            stringValue.append(unescape(matcher.group(1)));
            consumed = matcher.end();
        }

        if ((consumed == 0) || (! expression.substring(consumed).replace("+", "").isBlank()))
        {
            throw new MessageSetParsingException(sourcePath,
                                                 "the " + valueName + " of message definition " + constantName +
                                                         " is not a simple string constant");
        }

        return stringValue.toString();
    }


    /**
     * Convert the escape sequences of a java string literal into the characters that they represent.
     *
     * @param literalText the text between the quotes of a string literal
     * @return the value of the literal
     */
    private String unescape(String literalText)
    {
        StringBuilder value    = new StringBuilder();
        int           position = 0;

        while (position < literalText.length())
        {
            char currentChar = literalText.charAt(position);

            if ((currentChar == '\\') && (position + 1 < literalText.length()))
            {
                char escapedChar = literalText.charAt(position + 1);

                switch (escapedChar)
                {
                    case 'n'  -> value.append('\n');
                    case 't'  -> value.append('\t');
                    case 'r'  -> value.append('\r');
                    case 'b'  -> value.append('\b');
                    case 'f'  -> value.append('\f');
                    case 's'  -> value.append(' ');
                    case '0'  -> value.append('\0');
                    default   -> value.append(escapedChar);
                }

                position = position + 2;
            }
            else
            {
                value.append(currentChar);
                position = position + 1;
            }
        }

        return value.toString();
    }


    /**
     * Convert a java integer expression into its value.
     *
     * @param sourcePath path of the source file - used in any error messages
     * @param constantName name of the enum constant - used in any error messages
     * @param valueName name of the value being read - used in any error messages
     * @param expression the value as it appears in the source
     * @return the value of the expression
     * @throws MessageSetParsingException the expression is not an integer literal
     */
    private int getIntValue(String sourcePath,
                            String constantName,
                            String valueName,
                            String expression) throws MessageSetParsingException
    {
        try
        {
            return Integer.parseInt(expression.trim());
        }
        catch (NumberFormatException error)
        {
            throw new MessageSetParsingException(sourcePath,
                                                 "the " + valueName + " of message definition " + constantName +
                                                         " is not an integer constant");
        }
    }


    /**
     * Extract the name of the audit log severity from a reference such as
     * <i>AuditLogRecordSeverityLevel.EXCEPTION</i>.
     *
     * @param sourcePath path of the source file - used in any error messages
     * @param constantName name of the enum constant - used in any error messages
     * @param expression the value as it appears in the source
     * @return severity name
     * @throws MessageSetParsingException the expression is not a reference to a severity
     */
    private String getSeverityValue(String sourcePath,
                                    String constantName,
                                    String expression) throws MessageSetParsingException
    {
        Matcher matcher = SEVERITY_PATTERN.matcher(expression.trim());

        if (! matcher.matches())
        {
            throw new MessageSetParsingException(sourcePath,
                                                 "the severity of message definition " + constantName +
                                                         " is not a reference to an audit log severity");
        }

        return matcher.group(1);
    }


    /**
     * Return the text of the javadoc comment that sits immediately in front of the enum declaration.
     *
     * @param sourceText contents of the source file
     * @param enumStart offset of the "enum" keyword
     * @return description, or null if the enum has no javadoc
     */
    private String extractClassDescription(String sourceText, int enumStart)
    {
        int commentEnd = sourceText.lastIndexOf("*/", enumStart);

        if (commentEnd < 0)
        {
            return null;
        }

        int commentStart = sourceText.lastIndexOf("/**", commentEnd);

        if (commentStart < 0)
        {
            return null;
        }

        /*
         * Anything other than the class modifiers between the comment and the enum keyword means that the
         * comment belongs to something else.
         */
        String betweenCommentAndEnum = sourceText.substring(commentEnd + 2, enumStart).trim();

        if (! betweenCommentAndEnum.matches("(public|protected|private|abstract|static|final|\\s)*"))
        {
            return null;
        }

        return MarkdownBuilder.tidyJavadoc(sourceText.substring(commentStart + 3, commentEnd));
    }
}
