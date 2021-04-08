/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.watchdog;


import org.odpi.openmetadata.frameworks.governanceaction.GovernanceActionServiceProviderBase;


/**
 * MoveCopyFileGovernanceActionProvider is the OCF connector provider for the Move or Copy File Provisioning Governance Action Service.
 * This is a Provisioning Governance Action Service.
 */
public abstract class GenericWatchdogGovernanceActionProvider extends GovernanceActionServiceProviderBase
{
    /*
     * This value restricts the monitor to a single instance.
     */
    static final String INSTANCE_TO_MONITOR_PROPERTY    = "instanceToMonitor";

    /*
     * This type name defined the type of element that this monitor is focused on.  The default value is
     * "DataFile" - effectively all types of files.
     */
    static final String INTERESTING_TYPE_NAME_PROPERTY = "interestingTypeName";

    /*
     * Action target name for the element that has the event.
     */
    static final String ACTION_TARGET_NAME_PROPERTY     = "actionTargetName";
    static final String ACTION_TARGET_TWO_NAME_PROPERTY = "actionTargetNameTwo";

    /*
     * These properties define which types of events to listen for and which process to kick off if
     * the event occurs.  They can be set in the configuration properties of the connection.
     * These values can be overridden in the requestParameters. If the value for one of these properties
     * is null then the corresponding events are ignored.
     */
    static final String NEW_ELEMENT_PROCESS_NAME_PROPERTY          = "newElementProcessName";
    static final String UPDATED_ELEMENT_PROCESS_NAME_PROPERTY      = "updatedElementProcessName";
    static final String DELETED_ELEMENT_PROCESS_NAME_PROPERTY      = "deletedElementProcessName";
    static final String CLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY   = "classifiedElementProcessName";
    static final String RECLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY = "reclassifiedElementProcessName";
    static final String DECLASSIFIED_ELEMENT_PROCESS_NAME_PROPERTY = "declassifiedElementProcessName";
    static final String NEW_RELATIONSHIP_PROCESS_NAME_PROPERTY     = "newRelationshipProcessName";
    static final String UPDATED_RELATIONSHIP_PROCESS_NAME_PROPERTY = "updatedRelationshipProcessName";
    static final String DELETED_RELATIONSHIP_PROCESS_NAME_PROPERTY = "deletedRelationshipProcessName";

    /*
     * Common separated list of property name that have changed values - added to the request properties.
     */
    static final String CHANGED_PROPERTY_NAMES = "ChangedProperties";


    /*
     * These are the guards that could be returned.  The monitor will only complete if it encounters an unrecoverable error
     * or it is set up to listen for a single event and that event occurs.
     */
    static final String MONITORING_COMPLETE = "monitoring-complete"; /* requested single event occurred */
    static final String MONITORING_FAILED   = "monitoring-failed";   /* monitor not configured correctly or failed */
}
