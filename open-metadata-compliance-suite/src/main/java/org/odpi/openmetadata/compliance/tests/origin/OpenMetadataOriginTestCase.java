/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.origin;

import org.odpi.openmetadata.compliance.OpenMetadataTestCase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * OpenMetadataTestCase is the superclass for an open metadata compliance test.  It manages the
 * test environment and reporting.
 */
public abstract class OpenMetadataOriginTestCase extends OpenMetadataTestCase
{
    private String serverName = null;
    private String serverURLRoot = null;

    /**
     * Constructor passes the standard descriptive information to the superclass.
     *
     * @param workbenchId  identifier of the workbench used to build the documentation URL.
     * @param testCaseId  id of the test case
     * @param testCaseName  name of the test case
     */
    protected OpenMetadataOriginTestCase(String workbenchId,
                                         String testCaseId,
                                         String testCaseName)
    {
        super(workbenchId, testCaseId, testCaseName);
    }


    /**
     * Return the name of the server.
     *
     * @return string server name
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Set up the name of the server.
     *
     * @param serverName string server name
     */
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }


    /**
     * Set up the URL to the repository that is being tested.
     *
     * @param serverURLRoot root
     */
    public void setServerURLRoot(String serverURLRoot)
    {
        this.serverURLRoot = serverURLRoot;
    }


    /**
     * Return the URL to the repository that is being tested.
     *
     * @return serverURLRoot root
     */
    protected String getServerURLRoot()
    {
        return serverURLRoot;
    }
}
