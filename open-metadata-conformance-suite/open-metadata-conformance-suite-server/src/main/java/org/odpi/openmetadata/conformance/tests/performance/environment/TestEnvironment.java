/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.performance.environment;

import org.odpi.openmetadata.conformance.tests.performance.OpenMetadataPerformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceProfile;
import org.odpi.openmetadata.conformance.workbenches.performance.PerformanceWorkPad;

import java.lang.management.*;


/**
 * Report on characteristics of the environment used for the performance test.
 */
public class TestEnvironment extends OpenMetadataPerformanceTestCase
{

    private static final String TEST_CASE_ID   = "repository-performance-environment";
    private static final String TEST_CASE_NAME = "Environment statistics for the performance tests";

    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     */
    public TestEnvironment(PerformanceWorkPad workPad)
    {
        super(workPad, PerformanceProfile.ENVIRONMENT.getProfileId());

        super.updateTestId(TEST_CASE_ID, TEST_CASE_ID, TEST_CASE_NAME);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        assertCondition(true,
                "repository-performance-environment-properties",
                "Environmental information for the OMAG Platform running the performance test CTS.",
                PerformanceProfile.ENVIRONMENT.getProfileId(),
                null);
        addTestConfigProperties();
        addInstanceCountProperties();
        addSystemInfoProperties();
    }


    /**
     * Record the test configuration.
     */
    private void addTestConfigProperties()
    {
        addProperty("instancesPerType", performanceWorkPad.getInstancesPerType());
        addProperty("maxSearchResults", performanceWorkPad.getMaxSearchResults());
        addProperty("waitBetweenScenarios", performanceWorkPad.getWaitBetweenScenarios());
        addProperty("profilesToSkip", performanceWorkPad.getProfilesToSkip());
    }


    /**
     * Record the counts of various instances in the environment as discovered properties.
     */
    private void addInstanceCountProperties()
    {
        addProperty("totalEntitiesCreated", performanceWorkPad.getTotalEntitiesCreated());
        addProperty("totalEntitiesFound", performanceWorkPad.getTotalEntitiesFound());
        addProperty("totalRelationshipsCreated", performanceWorkPad.getTotalRelationshipsCreated());
        addProperty("totalRelationshipsFound", performanceWorkPad.getTotalRelationshipsFound());
    }


    /**
     * Record some basic information about the environment in which the performance test was executed.
     * (Note that this currently reports details regarding the OMAG Platform running the CTS itself, not the technology
     * under test: therefore it will likely only be relevant where the technology under test is running in the same
     * OMAG Platform as the CTS suite itself.)
     */
    private void addSystemInfoProperties()
    {
        addOperatingSystemProperties();
        addSystemResourceProperties();
        addJVMProperties();
        // TODO: capture details on filesystem usage for the plugin repository / embedded server (size of './data/servers')
    }


    /**
     * Record basic information about the operating system in which the performance test was executed.
     * (Note that this currently reports details regarding the OMAG Platform running the CTS itself, not the technology
     * under test: therefore it will likely only be relevant where the technology under test is running in the same
     * OMAG Platform as the CTS suite itself.)
     */
    private void addOperatingSystemProperties()
    {
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        String osDetails = os.getName() + " (" + os.getVersion() + ")";
        addProperty("operatingSystem", osDetails);
        addProperty("operatingSystemArchitecture", os.getArch());
        addProperty("operatingSystemAvailableProcessors", os.getAvailableProcessors());
        addProperty("operatingSystemLoadAverage", os.getSystemLoadAverage());
    }


    /**
     * Record basic information about the memory usage of the environment in which the performance test was executed.
     * (Note that this currently reports details regarding the OMAG Platform running the CTS itself, not the technology
     * under test: therefore it will likely only be relevant where the technology under test is running in the same
     * OMAG Platform as the CTS suite itself.)
     */
    private void addSystemResourceProperties()
    {
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memory.getHeapMemoryUsage();
        addProperty("heapUsage", heap.getUsed() + "/" + heap.getMax());
        MemoryUsage nonHeap = memory.getNonHeapMemoryUsage();
        addProperty("nonHeapUsage", nonHeap.getUsed() + "/" + nonHeap.getMax());
    }


    /**
     * Record basic information about the JVM of the environment in which the performance test was executed.
     * (Note that this currently reports details regarding the OMAG Platform running the CTS itself, not the technology
     * under test: therefore it will likely only be relevant where the technology under test is running in the same
     * OMAG Platform as the CTS suite itself.)
     */
    private void addJVMProperties()
    {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String jvmSpec = runtime.getSpecName() + " " + runtime.getSpecVendor() + " " + runtime.getSpecVersion();
        addProperty("jvmSpec", jvmSpec);
        String jvmImplementation = runtime.getVmName() + " " + runtime.getVmVendor() + " " + runtime.getVmVersion();
        addProperty("jvmImplementation", jvmImplementation);
    }


    /**
     * Record the provided property as one for the environment.
     *
     * @param name of the property
     * @param value of the property
     */
    private void addProperty(String name, Object value)
    {
        addDiscoveredProperty(name, value, PerformanceProfile.ENVIRONMENT.getProfileId(), null);
    }

}