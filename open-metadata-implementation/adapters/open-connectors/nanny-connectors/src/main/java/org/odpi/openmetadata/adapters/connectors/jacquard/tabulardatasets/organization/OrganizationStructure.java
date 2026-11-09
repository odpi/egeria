/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.organization;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationshipSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * OrganizationStructure is a snapshot of the teams in open metadata and the TeamStructure relationships that
 * arrange them into reporting hierarchies.  A department is a team that is part of such a hierarchy - it
 * reports to another team, or other teams report to it.  Organizations are teams too, but they are the tops of
 * the hierarchies rather than departments within them, and they are listed by the Organizations product.
 */
class OrganizationStructure
{
    private final Map<String, OpenMetadataRootElement> teams         = new LinkedHashMap<>();
    private final Map<String, String>                  superTeamOf   = new HashMap<>();
    private final Map<String, Integer>                 subTeamCounts = new HashMap<>();


    /**
     * Build the structure from the retrieved metadata.
     *
     * @param teamElements every element of type Team (which includes Organizations)
     * @param teamStructureRelationships every TeamStructure relationship - end 1 is the super team, end 2 the sub team
     */
    OrganizationStructure(List<OpenMetadataRootElement>     teamElements,
                          List<MetadataRelationshipSummary> teamStructureRelationships)
    {
        for (OpenMetadataRootElement team : teamElements)
        {
            teams.put(team.getElementHeader().getGUID(), team);
        }

        for (MetadataRelationshipSummary relationship : teamStructureRelationships)
        {
            if ((relationship.getEnd1() != null) && (relationship.getEnd2() != null))
            {
                String superTeamGUID = relationship.getEnd1().getGUID();
                String subTeamGUID   = relationship.getEnd2().getGUID();

                /*
                 * A team reports to one team.  Should the metadata say otherwise, the first relationship
                 * created wins, since the relationships are retrieved oldest first.
                 */
                if (! superTeamOf.containsKey(subTeamGUID))
                {
                    superTeamOf.put(subTeamGUID, superTeamGUID);
                }

                subTeamCounts.merge(superTeamGUID, 1, Integer::sum);
            }
        }
    }


    /**
     * Return the departments: the teams that are part of a reporting hierarchy, other than organizations.
     *
     * @return list of team elements in creation order
     */
    List<OpenMetadataRootElement> getDepartments()
    {
        List<OpenMetadataRootElement> departments = new ArrayList<>();

        for (OpenMetadataRootElement team : teams.values())
        {
            if (isDepartment(team.getElementHeader().getGUID()))
            {
                departments.add(team);
            }
        }

        return departments;
    }


    /**
     * Is this element a department?
     *
     * @param guid unique identifier of the element
     * @return boolean
     */
    boolean isDepartment(String guid)
    {
        OpenMetadataRootElement team = teams.get(guid);

        if ((team == null) || (isOrganization(team.getElementHeader())))
        {
            return false;
        }

        return superTeamOf.containsKey(guid) || subTeamCounts.containsKey(guid);
    }


    /**
     * Return the team that this team reports to.
     *
     * @param guid unique identifier of the team
     * @return team element, or null if it reports to no team (or the super team is not visible)
     */
    OpenMetadataRootElement getSuperTeam(String guid)
    {
        String superTeamGUID = superTeamOf.get(guid);

        if (superTeamGUID != null)
        {
            return teams.get(superTeamGUID);
        }

        return null;
    }


    /**
     * Return the unique identifier of the team that this team reports to, whether it is visible or not.
     *
     * @param guid unique identifier of the team
     * @return guid or null
     */
    String getSuperTeamGUID(String guid)
    {
        return superTeamOf.get(guid);
    }


    /**
     * Return the organization at the top of the team's reporting hierarchy.  The walk stops at the first
     * organization found, so a business unit modelled as an organization within a larger one is the answer
     * for the departments within it.
     *
     * @param guid unique identifier of the team
     * @return organization element, or null if the hierarchy does not lead to an organization
     */
    OpenMetadataRootElement getOrganization(String guid)
    {
        Set<String> visited = new HashSet<>();

        String currentGUID = superTeamOf.get(guid);

        while ((currentGUID != null) && (visited.add(currentGUID)))
        {
            OpenMetadataRootElement current = teams.get(currentGUID);

            if ((current != null) && (isOrganization(current.getElementHeader())))
            {
                return current;
            }

            currentGUID = superTeamOf.get(currentGUID);
        }

        return null;
    }


    /**
     * Return the number of teams reporting directly to this team.
     *
     * @param guid unique identifier of the team
     * @return count
     */
    int getSubTeamCount(String guid)
    {
        return subTeamCounts.getOrDefault(guid, 0);
    }


    /**
     * Return the display name from an element's properties.
     *
     * @param properties properties
     * @return display name or null
     */
    static String getDisplayName(OpenMetadataRootProperties properties)
    {
        if (properties instanceof ReferenceableProperties referenceableProperties)
        {
            return referenceableProperties.getDisplayName();
        }

        return null;
    }


    /**
     * Is the element an organization?
     *
     * @param elementHeader header of the element
     * @return boolean
     */
    static boolean isOrganization(ElementHeader elementHeader)
    {
        if ((elementHeader == null) || (elementHeader.getType() == null))
        {
            return false;
        }

        return (OpenMetadataType.ORGANIZATION.typeName.equals(elementHeader.getType().getTypeName())) ||
                ((elementHeader.getType().getSuperTypeNames() != null) &&
                        (elementHeader.getType().getSuperTypeNames().contains(OpenMetadataType.ORGANIZATION.typeName)));
    }
}
