/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.governanceaction.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFErrorCode;
import org.odpi.openmetadata.frameworks.governanceaction.ffdc.GAFRuntimeException;

import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * AuditLogBase is a simple audit log capability.
 *
 * An AuditLogBase should be initialized with details of the caller, resource and context before the first call
 * to addLogRecord.  The contextId may be changed from time to time as the caller's activity changes.
 */
public class AuditLogBase implements AuditLog
{
    private String     callerId         = "";
    private String     callerName       = "";
    private String     callerType       = "";
    private String     resourceId       = "";
    private String     resourceName     = "";
    private String     resourceType     = "";
    private String     contextId        = "";
    private Properties properties       = new Properties();
    private Logger     auditLogFile     = null;
    private String     auditLogFileName;
    private String     omasServerURL;


    /**
     * Default constructor
     *
     * @param name - log name
     * @param serverURL - URL of the server to use
     */
    public AuditLogBase(String    name, String   serverURL)
    {
        /*
         * Will only set up the log file when addLogRecord() is called for the first time.
         */
        auditLogFileName = name;
        omasServerURL = serverURL;
    }


    private String getAuditLogName()
    {
        /*
         * The audit log file name is evaluated on first use to ensure the caller has had time to set up the audit
         * log attributes.
         */
        if (auditLogFileName == null)
        {
            auditLogFileName = "AuditLogBase" + callerType + ":" + callerName + ":" + callerId + ":" + resourceType + ":" + resourceName + ":" + resourceId;
        }

        return auditLogFileName;
    }


    /**
     * Set up the identifier of the caller.   This is used for correlation of output from the logs with metadata
     * in the metadata repository and other diagnostic information.
     *
     * @param newCallerId - guid of the caller
     */
    public void  setCallerId(String  newCallerId)
    {
        callerId = newCallerId;
    }


    /**
     * Return setting for the callerId.
     *
     * @return callerId - guid of the caller.
     */
    public String getCallerId()
    {
        return callerId;
    }


    /**
     * Set up a meaningful name for the caller. This is used for correlation of output from the logs with metadata
     * in the metadata repository and other diagnostic information.
     *
     * @param newCallerName - meaningful name of the caller - such as the name of the application using the Audit log.
     */
    public void  setCallerName(String newCallerName)
    {
        callerName = newCallerName;
    }


    /**
     * Return setting for the callerName.
     *
     * @return callerName - name of the caller
     */
    public String getCallerName()
    {
        return callerName;
    }


    /**
     * Set up the type of caller. This is used for correlation of output from the logs with metadata
     * in the metadata repository and other diagnostic information.
     *
     * @param newCallerType - meaningful descriptive type name - such as the type of the application using the Audit log.
     */
    public void  setCallerType(String newCallerType)
    {
        callerType = newCallerType;
    }


    /**
     * Return setting of the callerType.
     *
     * @return callerType - type of caller
     */
    public String getCallerType()
    {
        return callerType;
    }


    /**
     * Set up the identifier of the related resource, such as the connector to access data.   This is used for
     * correlation of output from the logs with metadata in the metadata repository and other diagnostic information.
     *
     * @param newResourceId - guid of the caller
     */
    public void  setResourceId(String  newResourceId)
    {
        resourceId = newResourceId;
    }


    /**
     * Return setting for the resourceId.
     *
     * @return resourceId - guid of the resource.
     */
    public String getResourceId()
    {
        return resourceId;
    }


    /**
     * Set up a meaningful name for the related resource. This is used for correlation of output from the logs with
     * metadata in the metadata repository and other diagnostic information.
     *
     * @param newResourceName - meaningful name of the resource - may be class name or (preferably) a name from related
     *             metadata entity.
     */
    public void  setResourceName(String newResourceName)
    {
        resourceName = newResourceName;
    }


    /**
     * Return setting for the resourceName.
     *
     * @return resourceName - name of the related resource
     */
    public String getResourceName()
    {
        return resourceName;
    }


    /**
     * Set up the type of the related resource. This is used for correlation of output from the logs with metadata
     * in the metadata repository and other diagnostic information.
     *
     * @param newResourceType - meaningful descriptive type name - such as "connector".
     */
    public void  setResourceType(String newResourceType)
    {
        resourceType = newResourceType;
    }


    /**
     * Return setting of the resourceType.
     *
     * @return resourceType - type of related resource
     */
    public String getResourceType()
    {
        return resourceType;
    }


    /**
     * Request a list of property names stored in the Audit log.  This is used in conjunction with get/setProperty.
     *
     * @return Enumeration - String - enumeration of property names
     */
    public List<String> getPropertyNames()
    {
        return null;
    }


    /**
     * Add or update a property in the Audit log.  Properties are used to record additional information about the
     * caller of the Audit log and their activity.
     *
     * @param newPropertyName - name of the property - if the property exists, it will update; otherwise add a new
     *                     property
     * @param newPropertyValue - value to associate with the property name.  If null is passed the property is deleted.
     */
    public void  setProperty(String  newPropertyName, String newPropertyValue)
    {
        if (newPropertyName == null)
        {
            throw new GAFRuntimeException(GAFErrorCode.NULL_AUDITLOG_PROPERTY_NAME.getMessageDefinition(getAuditLogName()),
                                          this.getClass().getName(),
                                          "setProperty");
        }
        else if (newPropertyValue == null)
        {
            properties.remove(newPropertyName);
        }
        else
        {
            properties.setProperty(newPropertyName, newPropertyValue);
        }
    }


    /**
     * Retrieve a property stored in the Audit log.  A null is returned if the property is not set.
     *
     * @param propertyName - name of property to return.
     * @return propertyValue - associated value.
     */
    public String  getProperty(String  propertyName)
    {
        return properties.getProperty(propertyName, null);
    }


    /**
     * Set a context id for the activity that the Audit log relates to.  This may change over time and its current
     * value will be added to each log record as it is created.  Examples of contextIds may be the function name,
     * or processId of the activity.
     *
     * @param newContextId - identifier for the current activity.
     */
    public void  setContextId(String  newContextId)
    {
        contextId = newContextId;
    }


    /**
     * Returns the current context id associated with the Audit log.
     *
     * @return contextId - id for the activity.
     */
    public String getContextId()
    {
        return contextId;
    }


    /**
     * Creates an Audit log record for the caller.  This log record will be augmented with details of the caller and
     * context and then pushed to disk for later study.   The caller may also make use of the properties stored in the
     * Audit log to augment the content of the log record before calling addLogMessage().
     *
     * @param message - log record content
     */
    public void  addLogMessage(String   message)
    {
        Date timestamp = new Date();

        if (auditLogFile == null)
        {
            /*
             * This is the first use of the audit log so create the log file.
             */
            auditLogFile = LoggerFactory.getLogger(getAuditLogName());
        }

        auditLogFile.info(timestamp.toString() + contextId + message);
    }
}
