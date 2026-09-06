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
 * BitolTeamDeserializer accepts both forms of the "team" section found in Open Data Contract Standard (ODCS) documents.
 * Since ODCS v3.1.0 the team is an object with a name, description and list of members.  Earlier v3 documents supplied a
 * bare list of team members; this form is deprecated but still valid.  When a list is encountered it is wrapped into a
 * BitolTeam with no name so that the rest of the processing only has to deal with one shape.  It is registered through
 * BitolModule rather than by annotating the beans, so that the beans depend only on the jackson-annotations package.
 */
public class BitolTeamDeserializer extends JsonDeserializer<BitolTeam>
{
    /**
     * Default constructor
     */
    public BitolTeamDeserializer()
    {
    }


    /**
     * Read either a team object or a legacy list of team members.
     *
     * @param parser parser positioned at the start of the team section
     * @param context deserialization context
     * @return team bean or null if the section is null
     * @throws IOException problem reading the document
     */
    @Override
    public BitolTeam deserialize(JsonParser            parser,
                                 DeserializationContext context) throws IOException
    {
        ObjectCodec codec = parser.getCodec();
        JsonNode    node  = codec.readTree(parser);

        if ((node == null) || (node.isNull()))
        {
            return null;
        }

        if (node.isArray())
        {
            BitolTeam             team    = new BitolTeam();
            List<BitolTeamMember> members = new ArrayList<>();

            for (JsonNode memberNode : node)
            {
                members.add(codec.treeToValue(memberNode, BitolTeamMember.class));
            }

            team.setMembers(members);

            return team;
        }

        /*
         * Map the object form field by field so that this deserializer is not re-entered for the BitolTeam type.
         */
        BitolTeam team = new BitolTeam();

        team.setId(getText(node, "id"));
        team.setName(getText(node, "name"));
        team.setDescription(getText(node, "description"));

        JsonNode membersNode = node.get("members");

        if ((membersNode != null) && (membersNode.isArray()))
        {
            List<BitolTeamMember> members = new ArrayList<>();

            for (JsonNode memberNode : membersNode)
            {
                members.add(codec.treeToValue(memberNode, BitolTeamMember.class));
            }

            team.setMembers(members);
        }

        JsonNode tagsNode = node.get("tags");

        if ((tagsNode != null) && (tagsNode.isArray()))
        {
            List<String> tags = new ArrayList<>();

            for (JsonNode tagNode : tagsNode)
            {
                tags.add(tagNode.asText());
            }

            team.setTags(tags);
        }

        JsonNode customPropertiesNode = node.get("customProperties");

        if ((customPropertiesNode != null) && (customPropertiesNode.isArray()))
        {
            List<BitolCustomProperty> customProperties = new ArrayList<>();

            for (JsonNode propertyNode : customPropertiesNode)
            {
                customProperties.add(codec.treeToValue(propertyNode, BitolCustomProperty.class));
            }

            team.setCustomProperties(customProperties);
        }

        JsonNode authoritativeDefinitionsNode = node.get("authoritativeDefinitions");

        if ((authoritativeDefinitionsNode != null) && (authoritativeDefinitionsNode.isArray()))
        {
            List<BitolAuthoritativeDefinition> authoritativeDefinitions = new ArrayList<>();

            for (JsonNode definitionNode : authoritativeDefinitionsNode)
            {
                authoritativeDefinitions.add(codec.treeToValue(definitionNode, BitolAuthoritativeDefinition.class));
            }

            team.setAuthoritativeDefinitions(authoritativeDefinitions);
        }

        return team;
    }


    /**
     * Return the text of a property of a node.
     *
     * @param node object node
     * @param propertyName name of property
     * @return text or null if the property is missing or null
     */
    private String getText(JsonNode node,
                           String   propertyName)
    {
        JsonNode value = node.get(propertyName);

        if ((value == null) || (value.isNull()))
        {
            return null;
        }

        return value.asText();
    }
}
