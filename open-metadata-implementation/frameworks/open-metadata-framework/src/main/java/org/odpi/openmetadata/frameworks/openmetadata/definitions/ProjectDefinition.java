/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.definitions;


import org.odpi.openmetadata.frameworks.openmetadata.refdata.ProjectStatus;

import java.util.Arrays;
import java.util.List;

/**
 * The ProjectDefinition is used to feed the definition of the projects for Coco Pharmaceuticals scenarios.
 */
public interface ProjectDefinition extends ReferenceableDefinition
{

    /**
     * Return the mission for the project.
     *
     * @return string
     */
    String getMission();



    /**
     * Is this project a campaign?
     *
     * @return boolean
     */
    boolean isCampaign();


    /**
     * Is the project a task?
     *
     * @return boolean
     */
    boolean isTask();


    /**
     * How should this project be classified?
     *
     * @return classification
     */
    String getProjectTypeClassification();

    /**
     * What is the project's current status?
     *
     * @return status
     */
    ProjectStatus getProjectStatus();


    /**
     * Which project provides direction?
     *
     * @return project
     */
    ProjectDefinition getControllingProject();


    /**
     * Which projects is this project dependent on?
     *
     * @return list of projects
     */
    List<ProjectDefinition> getDependentOn();


    /**
     * Who is the leader for this project?
     *
     * @return person
     */
    ActorDefinition getLeader();


    /**
     * Return the team members.
     *
     * @return list of people
     */
    List<ActorDefinition> getMembers();

    /**
     * Who is the exec sponsor for this project?
     *
     * @return person
     */
    ActorDefinition getSponsor();
}
