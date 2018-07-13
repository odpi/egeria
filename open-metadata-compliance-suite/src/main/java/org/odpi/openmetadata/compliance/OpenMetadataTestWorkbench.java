/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance;

import org.odpi.openmetadata.compliance.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * The OpenMetadataTestWorkbench drives the execution of a batch of tests.
 */
public abstract class OpenMetadataTestWorkbench
{
    protected List<OpenMetadataTestCase>   testCases = null; /* setup by the subclass */
    protected String                       serverURLRoot;

    /**
     * Constructor taking in the URL root of the server under test.
     *
     * @param serverURLRoot string
     */
    public OpenMetadataTestWorkbench(String  serverURLRoot)
    {
        this.serverURLRoot = serverURLRoot;
    }


    /**
     * Method that runs the tests - it is implemented by the subclass.
     *
     * @return test results
     */
    public abstract OpenMetadataTestWorkbenchResults  runTests();
}
