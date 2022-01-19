/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.archivemanager.connector;

import org.odpi.openmetadata.engineservices.archivemanager.ffdc.ArchiveManagerErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;


/**
 * ArchiveService describes a specific type of connector that is responsible for managing the content
 * of a specific open metadata archive.  Information about the available metadata is passed in the archive context.
 * The returned archive context also contains the status of this service.
 */
public abstract class ArchiveService extends ConnectorBase implements AuditLoggingComponent
{
    protected String         archiveServiceName = "<Unknown>";
    protected ArchiveContext archiveContext = null;
    protected AuditLog       auditLog = null;

    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Set up details of the asset to analyze and the results of any previous analysis.
     *
     * @param archiveContext information about the asset to analyze and the results of analysis of
     *                         other archive service request.  Partial results from other archive
     *                         services run as part of the same archive service request may also be
     *                         stored in the newAnnotations list.
     */
    public synchronized void setArchiveContext(ArchiveContext archiveContext)
    {
        this.archiveContext = archiveContext;
    }


    /**
     * Set up the archive service name.  This is used in error messages.
     *
     * @param archiveServiceName name of the archive service
     */
    public void setArchiveServiceName(String archiveServiceName)
    {
        this.archiveServiceName = archiveServiceName;
    }


    /**
     * Return the archive context for this archive service.  This is typically called after the disconnect()
     * method is called.  If called before disconnect(), it may only contain partial results.
     *
     * @return archive context containing the results discovered (so far) by the archive service.
     */
    public synchronized ArchiveContext getArchiveContext()
    {
        return archiveContext;
    }


    /**
     * Indicates that the archive service is completely configured and can begin processing.
     * This is where the function of the archive service is implemented.
     *
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the archive service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        validateContext(archiveContext);
    }


    /**
     * Provide a common exception for unexpected errors.
     *
     * @param methodName calling method
     * @param error caught exception
     * @throws ConnectorCheckedException wrapped exception
     */
    protected void handleUnexpectedException(String      methodName,
                                             Exception   error) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(ArchiveManagerErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(archiveServiceName,
                                                                                                              error.getClass().getName(),
                                                                                                              methodName,
                                                                                                              error.getMessage()),
                                            this.getClass().getName(),
                                            methodName);
    }


    /**
     * Verify that the context has been set up in the subclass
     *
     * @param governanceContext context from the subclass
     * @throws ConnectorCheckedException error to say that the connector (governance action service) is not able to proceed because
     * it has not been set up correctly.
     */
    public void validateContext(ArchiveContext governanceContext) throws ConnectorCheckedException
    {
        final String methodName = "start";

        if (governanceContext == null)
        {
            throw new ConnectorCheckedException(ArchiveManagerErrorCode.NULL_ARCHIVE_CONTEXT.getMessageDefinition(archiveServiceName),
                                                 this.getClass().getName(),
                                                 methodName);
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.  This is a standard
     * method from the Open Connector Framework (OCF).  If you need to override this method
     * be sure to call super.disconnect() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the archive service.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();
    }
}
