/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BitolContextDeserializer reads the AI context block, which ODCS v3.2.0 allows to be written either as an object
 * or as a plain string that is equivalent to supplying the instructions alone.
 */
public class BitolContextDeserializer extends JsonDeserializer<BitolContext>
{
    /**
     * Default constructor
     */
    public BitolContextDeserializer()
    {
    }


    /**
     * Read either a context object or the string shorthand.
     *
     * @param parser parser positioned at the start of the context
     * @param context deserialization context
     * @return context bean or null if the section is null
     * @throws IOException problem reading the document
     */
    @Override
    public BitolContext deserialize(JsonParser             parser,
                                    DeserializationContext context) throws IOException
    {
        ObjectCodec codec = parser.getCodec();
        JsonNode    node  = codec.readTree(parser);

        if ((node == null) || (node.isNull()))
        {
            return null;
        }

        BitolContext bitolContext = new BitolContext();

        if (node.isTextual())
        {
            bitolContext.setInstructions(node.asText());

            return bitolContext;
        }

        /*
         * Mapped field by field so that this deserializer is not re-entered for the BitolContext type.
         */
        JsonNode instructions = node.get("instructions");

        if ((instructions != null) && (! instructions.isNull()))
        {
            bitolContext.setInstructions(instructions.asText());
        }

        JsonNode statementsNode = node.get("verifiedStatements");

        if ((statementsNode != null) && (statementsNode.isArray()))
        {
            List<BitolVerifiedStatement> statements = new ArrayList<>();

            for (JsonNode statementNode : statementsNode)
            {
                statements.add(codec.treeToValue(statementNode, BitolVerifiedStatement.class));
            }

            bitolContext.setVerifiedStatements(statements);
        }

        JsonNode constraintsNode = node.get("constraints");

        if ((constraintsNode != null) && (constraintsNode.isArray()))
        {
            List<BitolContextConstraint> constraints = new ArrayList<>();

            for (JsonNode constraintNode : constraintsNode)
            {
                constraints.add(codec.treeToValue(constraintNode, BitolContextConstraint.class));
            }

            bitolContext.setConstraints(constraints);
        }

        return bitolContext;
    }
}
