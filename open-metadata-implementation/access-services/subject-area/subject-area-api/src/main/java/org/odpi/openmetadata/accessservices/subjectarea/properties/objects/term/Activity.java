/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term;


import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.ActivityType;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

import java.io.Serializable;

/**
 * This is a Term that describes an activity
 */

public class Activity extends Term implements Serializable {
    private ActivityType activityType = null;

    public Activity() {
        nodeType = NodeType.Activity;
    }

    /**
     * Different types of activities.
     * @return <code>ActivityType</code>
     */
    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }
}
