/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.datamanager.fvt.execution;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.datamanager.fvt.connections.CreateConnectionTest;
import org.odpi.openmetadata.accessservices.datamanager.fvt.databases.CreateDatabaseTest;
import org.odpi.openmetadata.accessservices.datamanager.fvt.errorhandling.InvalidParameterTest;
import org.odpi.openmetadata.accessservices.datamanager.fvt.events.CreateEventsTest;
import org.odpi.openmetadata.fvt.utilities.FVTConstants;
import org.odpi.openmetadata.accessservices.datamanager.fvt.clientconstructors.ClientConstructorTest;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTTestFailureException;
import org.odpi.openmetadata.http.HttpHelper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DataManagerOMASIT is the failsafe wrapper for ClientConstructorTest.
 */
public class DataManagerOMASIT
{
    @BeforeAll
    public static void disableStrictSSL(){
        HttpHelper.noStrictSSL();
    }

    @ParameterizedTest
        @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testClientConstructors(String serverName)
    {
        FVTResults results = ClientConstructorTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"),FVTConstants.SERVER_PLATFORM_URL_ROOT));

        results.printResults(serverName);
        assertTrue(results.isSuccessful());
    }


    @ParameterizedTest
        @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testCreateDB(String serverName)
    {
        FVTResults results = CreateDatabaseTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT), FVTConstants.USERID);

        results.printResults(serverName);
        assertTrue(results.isSuccessful());
    }


    @ParameterizedTest
        @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testCreateEvents(String serverName)
    {
        FVTResults results = CreateEventsTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT), FVTConstants.USERID);

        results.printResults(serverName);
        assertTrue(results.isSuccessful());
    }

    @ParameterizedTest
    @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testCreateConnections(String serverName)
    {
        FVTResults results = CreateConnectionTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT), FVTConstants.USERID);

        results.printResults(serverName);
        assertTrue(results.isSuccessful());
    }


    @ParameterizedTest
        @ValueSource(strings = {FVTConstants.IN_MEMORY_SERVER})
    public void testInvalidParameters(String server)
    {
        assertDoesNotThrow(() -> runInvalidParameters(server));
    }


    public void runInvalidParameters(String serverName) throws FVTTestFailureException
    {
        FVTResults results = InvalidParameterTest.performFVT(serverName, StringUtils.defaultIfEmpty(System.getProperty("fvt.url"), FVTConstants.SERVER_PLATFORM_URL_ROOT), FVTConstants.USERID);

        results.printResults(serverName);
        if (! results.isSuccessful())
        {
            throw new FVTTestFailureException(results);
        }
    }
}
