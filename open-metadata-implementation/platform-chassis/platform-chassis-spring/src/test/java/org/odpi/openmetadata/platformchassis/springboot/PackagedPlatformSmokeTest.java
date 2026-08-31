/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.platformchassis.springboot;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * PackagedPlatformSmokeTest launches the packaged boot jar the way a user does - as a separate process -
 * and checks that the packaging chain works: manifest, launcher, loader classes and nested jars.
 * <br><br>
 * Every other test in this repository, the BVT and all nine FVT suites included, starts the platform
 * <em>in-process</em> through {@code SpringApplicationBuilder}. That is much faster and right for what
 * those suites test, but it means the packaged jar itself is never exercised: not its manifest, not its
 * launcher, not the loader classes the Spring Boot plugin packages alongside them. A jar can therefore be
 * completely unstartable while every test passes.
 * <br><br>
 * That is not hypothetical. The Spring Boot 3.1 to 3.5 upgrade left this module naming
 * {@code org.springframework.boot.loader.PropertiesLauncher} in the manifest, a class Spring Boot 3.2
 * moved to {@code org.springframework.boot.loader.launch}. The build stayed green, and the shipped jar
 * failed at startup with {@code ClassNotFoundException} for its own main class. The first test below
 * costs milliseconds and would have caught it.
 */
public class PackagedPlatformSmokeTest
{
    /**
     * Set by the build - see this module's build.gradle - to the boot jar this test should exercise.
     */
    private static final String JAR_PROPERTY = "platform.boot.jar";

    /**
     * Logged by Tomcat once the embedded servlet container has started inside the packaged jar.
     * <br><br>
     * Reaching this line is the whole point of the test.  It can only happen if the manifest's Main-Class
     * resolved, the packaged loader classes ran, the nested BOOT-INF jars were readable, the Start-Class
     * loaded from them, <em>and</em> the servlet container the platform serves its REST APIs from was
     * present and able to start.  That covers the entire packaging chain.
     * <br><br>
     * It deliberately waits for Tomcat rather than for Spring's earlier "Starting OMAGServerPlatform"
     * line.  That earlier line is logged before the web server is created, so it is reached even by a jar
     * that packages no servlet container at all - which is exactly what a capability conflict between
     * tomcat-embed-core and the standalone jakarta.servlet-api jar produces.  See the
     * jvmDependencyConflicts block in the root build.gradle.
     * <br><br>
     * The test still does not wait for the platform to become <em>ready</em>.  The boot jar alone cannot
     * get that far: a working platform also needs the metadata security and configuration store
     * connectors, which are packaged into the distribution's platform/lib directory and picked up through
     * -Dloader.path. Starting a complete platform is what the BVT and the FVT suites already do.
     */
    private static final String STARTING_MESSAGE = "Starting Servlet engine: [Apache Tomcat";

    private static final int STARTUP_TIMEOUT_SECONDS = 120;


    /**
     * Locate the boot jar the build handed us.
     *
     * @return the jar
     */
    private File bootJar()
    {
        String path = System.getProperty(JAR_PROPERTY);

        assertNotNull(path, "The build must set -D" + JAR_PROPERTY + " to the boot jar under test");

        File jar = new File(path);

        assertTrue(jar.isFile(), "Boot jar does not exist: " + path);

        return jar;
    }


    /**
     * The class named as the jar's Main-Class must actually be inside the jar.
     * <br><br>
     * This is the cheap half of the smoke test and catches a whole class of packaging mistake without
     * starting anything: a launcher renamed by a Spring Boot upgrade, a manifest override that has gone
     * stale, or loader classes that were not packaged at all.
     *
     * @throws Exception the jar could not be read
     */
    @Test
    void mainClassIsActuallyInTheJar() throws Exception
    {
        File jar = bootJar();

        try (JarFile jarFile = new JarFile(jar))
        {
            Manifest manifest = jarFile.getManifest();

            assertNotNull(manifest, "The boot jar has no manifest");

            String mainClass = manifest.getMainAttributes().getValue("Main-Class");

            assertNotNull(mainClass, "The boot jar's manifest declares no Main-Class");

            String entryName = mainClass.replace('.', '/') + ".class";

            assertNotNull(jarFile.getEntry(entryName),
                          "The manifest names " + mainClass + " as the Main-Class, but that class is not in the jar. " +
                                  "The jar cannot start. This usually means a hard-coded launcher class name has gone " +
                                  "stale - Spring Boot 3.2 moved the loader classes to " +
                                  "org.springframework.boot.loader.launch.");

            String startClass = manifest.getMainAttributes().getValue("Start-Class");

            assertNotNull(startClass, "The boot jar's manifest declares no Start-Class");
        }
    }


    /**
     * Run the packaged jar and check that it gets far enough to start the application.
     * <br><br>
     * The platform is started on port 0 so that it cannot collide with a development platform, or with
     * another test run, on this machine.
     *
     * @throws Exception the process could not be started, or was interrupted
     */
    @Test
    void packagedJarLaunchesTheApplication() throws Exception
    {
        File        jar     = bootJar();
        Path        workDir = Files.createTempDirectory("platform-smoke-test");
        List<String> output = new ArrayList<>();

        List<String> command = List.of(
                System.getProperty("java.home") + File.separator + "bin" + File.separator + "java",
                "-Dserver.port=0",
                "-Dstartup.server.list=",
                "-Dstartup.user=system",
                "-Dauthn.header.name.list=",
                "-Dcors.allowed-origins=*",
                "-Dscan.packages=org.odpi.openmetadata.*",
                "-Dspringdoc.api-docs.enabled=false",
                "-Dmanagement.health.cassandra.enabled=false",
                "-Dmanagement.health.redis.enabled=false",
                "-Dmanagement.health.ldap.enabled=false",
                "-Dplatform.configstore.provider=org.odpi.openmetadata.adapters.adminservices.configurationstore.file.FileBasedServerConfigStoreProvider",
                "-Dplatform.configstore.endpoint=data/servers/{0}/config/{0}.config",
                // No user directory is configured for this hermetic run, so switch off Spring Boot's
                // default security auto-configuration rather than fall back to a generated password.
                "-Dspring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,"
                        + "org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration,"
                        + "org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration",
                "-jar", jar.getAbsolutePath());

        ProcessBuilder builder = new ProcessBuilder(command);

        builder.directory(workDir.toFile());
        builder.redirectErrorStream(true);

        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)))
        {
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(STARTUP_TIMEOUT_SECONDS);
            String line;

            while ((line = reader.readLine()) != null)
            {
                output.add(line);

                if (line.contains(STARTING_MESSAGE))
                {
                    return;      // the packaging chain works, servlet container included
                }

                if (System.nanoTime() > deadline)
                {
                    break;
                }
            }

            fail("The packaged jar did not get as far as starting its servlet container within " +
                         STARTUP_TIMEOUT_SECONDS + " seconds.  Its output was:\n" + String.join("\n", output));
        }
        finally
        {
            process.destroyForcibly();
            process.waitFor(30, TimeUnit.SECONDS);
        }
    }
}
