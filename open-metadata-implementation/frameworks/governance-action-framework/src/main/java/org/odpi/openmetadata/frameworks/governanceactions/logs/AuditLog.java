/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.frameworks.governanceactions.logs;

import java.util.List;

/**
 * The AuditLog provides a standard interface for writing audit log
 * record during real-time management and use of Assets.
 */
public interface AuditLog
{
    /**
     * Set up the identifier of the caller.   This is used for correlation of output from the logs with metadata
     * in the metadata repository and other diagnostic information.
     *
     * @param newCallerId - guid of the caller
     */
    void  setCallerId(String  newCallerId);


    /**
     * Return setting for the callerId.
     *
     * @return callerId - guid of the caller.
     */
    String getCallerId();


    /**
     * Set up a meaningful name for the caller. This is used for correlation of output from the logs with metadata
     * in the metadata repository and other diagnostic information.
     *
     * @param newCallerName - meaningful name of the caller - such as the name of the application using the Audit log.
     */
    void  setCallerName(String newCallerName);


    /**
     * Return setting for the callerName.
     *
     * @return callerName - name of the caller
     */
    String getCallerName();


    /**
     * Set up the type of caller. This is used for correlation of output from the logs with metadata
     * in the metadata repository and other diagnostic information.
     *
     * @param newCallerType - meaningful descriptive type name - such as the type of the application using the Audit log.
     */
    void  setCallerType(String newCallerType);


    /**
     * Return setting of the callerType.
     *
     * @return callerType - type of caller
     */
    String getCallerType();


    /**
     * Set up the identifier of the related resource, such as the connector to access data.   This is used for
     * correlation of output from the logs with metadata in the metadata repository and other diagnostic information.
     *
     * @param newResourceId - guid of the caller
     */
    void  setResourceId(String  newResourceId);


    /**
     * Return setting for the resourceId.
     *
     * @return resourceId - guid of the resource.
     */
    String getResourceId();


    /**
     * Set up a meaningful name for the related resource. This is used for correlation of output from the logs with
     * metadata in the metadata repository and other diagnostic information.
     *
     * @param newResourceName - meaningful name of the resource - may be class name or (preferably) a name from related
     *             metadata entity.
     */
    void  setResourceName(String newResourceName);


    /**
     * Return setting for the resourceName.
     *
     * @return resourceName - name of the related resource
     */
    String getResourceName();


    /**
     * Set up the type of the related resource. This is used for correlation of output from the logs with metadata
     * in the metadata repository and other diagnostic information.
     *
     * @param newResourceType - meaningful descriptive type name - such as "connector".
     */
    void  setResourceType(String newResourceType);


    /**
     * Return setting of the resourceType.
     *
     * @return resourceType - type of related resource
     */
    String getResourceType();


    /**
     * Request a list of property names stored in the Audit log.  This is used in conjunction with get/setProperty.
     *
     * @return List - String - list of property names
     */
    List<String> getPropertyNames();


    /**
     * Add or update a property in the Audit log.  Properties are used to record additional information about the
     * caller of the Audit log and their activity.
     *
     * @param newPropertyName - name of the property - if the property exists, it will update; otherwise add a new
     *                          property
     * @param newPropertyValue - value to associate with the property name.  If null is passed the property is deleted.
     */
    void  setProperty(String  newPropertyName, String newPropertyValue);


    /**
     * Retrieve a property stored in the Audit log.  A null is returned if the property is not set.
     *
     * @param propertyName - name of property to return.
     * @return propertyValue - associated value.
     */
    String  getProperty(String  propertyName);


    /**
     * Set a context id for the activity that the Audit log relates to.  This may change over time and its current
     * value will be added to each log record as it is created.  Examples of contextIds may be the function name,
     * or processId of the activity.
     *
     * @param newContextId - identifier for the current activity.
     */
    void  setContextId(String  newContextId);


    /**
     * Returns the current context id associated with the Audit log.
     *
     * @return contextId - id for the activity.
     */
    String getContextId();


    /**
     * Creates an Audit log record for the caller.  This log record will be augmented with details of the caller and
     * context and then pushed to disk for later study.   The caller may also make use of the properties stored in the
     * Audit log to augment the content of the log record before calling addLogMessage().
     *
     * @param message - log record content
     */
    void  addLogMessage(String   message);
}
