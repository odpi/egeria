/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;

import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * The CommunityDefinition is used to feed the definition of the projects for Coco Pharmaceuticals scenarios.
 */
public enum CommunityDefinition
{
    /**
     * SIG:DataSci - Community of data scientists within Coco Pharmaceuticals.
     */
    DATA_SCI_SIG("SIG:DataSci",
                 "Data Science special interest group",
                 "Community of data scientists within Coco Pharmaceuticals.",
                 new PersonDefinition[]{PersonDefinition.CALLIE_QUARTILE},
                 new PersonDefinition[]{PersonDefinition.TESSA_TUBE,
                         PersonDefinition.ERIN_OVERVIEW,
                         PersonDefinition.STEW_FASTER,
                         PersonDefinition.TERRI_DARING,
                         PersonDefinition.STEVE_STARTER,
                         PersonDefinition.TOM_TALLY}),
    ;

    private final String             qualifiedName;
    private final String             displayName;
    private final String             description;
    private final PersonDefinition[] leaders;
    private final PersonDefinition[] members;

    /**
     * The constructor creates an instance of the enum
     *
     * @param qualifiedName   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     * @param leaders person to link into the leadership role
     * @param members list of people who are members of the team or organization
     */
    CommunityDefinition(String                     qualifiedName,
                        String                     displayName,
                        String                     description,
                        PersonDefinition[]         leaders,
                        PersonDefinition[]         members)
    {
        this.qualifiedName = qualifiedName;
        this.displayName = displayName;
        this.description = description;
        this.leaders = leaders;
        this.members = members;
    }

    public String getQualifiedName()
    {
        return "Community: " + qualifiedName;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public String getDescription()
    {
        return description;
    }


    public List<PersonDefinition> getLeaders()
    {
        return Arrays.asList(leaders);
    }


    public List<PersonDefinition> getMembers()
    {
        return Arrays.asList(members);
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "CommunityDefinition{" + displayName + '}';
    }
}
