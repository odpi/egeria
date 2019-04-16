/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches;

/**
 * The OpenMetadataConformanceWorkbench drives the execution of a batch of tests.
 */
public abstract class OpenMetadataConformanceWorkbench implements Runnable
{
    protected String                     workbenchId;               /* setup by the subclass */
    protected String                     workbenchName;             /* setup by the subclass */
    protected String                     versionNumber;             /* setup by the subclass */
    protected String                     workbenchDocumentationURL; /* setup by the subclass */

    protected static volatile boolean    runningFlag = true;

    /**
     * Constructor taking in the URL root of the server under test.
     *
     * @param workbenchId unique identifier of this workbench
     * @param workbenchName name of this workbench
     * @param versionNumber version number of workbench
     * @param workbenchDocumentationURL url to the documentation for this workbench
     */
    public OpenMetadataConformanceWorkbench(String                     workbenchId,
                                            String                     workbenchName,
                                            String                     versionNumber,
                                            String                     workbenchDocumentationURL)
    {
        this.workbenchId = workbenchId;
        this.workbenchName = workbenchName;
        this.versionNumber = versionNumber;
        this.workbenchDocumentationURL = workbenchDocumentationURL;
    }


    /**
     * Return the workbench id.
     *
     * @return id
     */
    public String getWorkbenchId()
    {
        return workbenchId;
    }

    /**
     * Return the workbench name.
     *
     * @return name
     */
    public String getWorkbenchName()
    {
        return workbenchName;
    }


    /**
     * Return the string version number of the workbench.
     *
     * @return version number
     */
    public String getVersionNumber()
    {
        return versionNumber;
    }


    /**
     * Return the link to the documentation for this workbench
     *
     * @return string url
     */
    public String getWorkbenchDocumentationURL()
    {
        return workbenchDocumentationURL;
    }


    /**
     * Shutdown the running thread
     */
    public synchronized void stopRunning()
    {
        runningFlag = false;
    }


    /**
     * Test to see if the thread should keep running.
     *
     * @return boolean flag
     */
    protected synchronized boolean isRunning()
    {
        return runningFlag;
    }
}
