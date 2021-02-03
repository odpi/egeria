/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;


/**
 * OpenMetadataTypesArchiveWriter create a physical open metadata archive file for the supplied open metadata archives
 * encoded using Open Metadata Repository Services (OMRS) formats.
 */
public class OpenMetadataTypesArchiveWriter extends OMRSArchiveWriter
{
    private static final String defaultOpenMetadataArchiveFileName = "OpenMetadataTypes.json";

    /**
     * Default constructor
     */
    private OpenMetadataTypesArchiveWriter()
    {
    }


    /**
     * Generates and writes out an open metadata archive containing all of the open metadata types.
     */
    private void writeOpenMetadataTypesArchive()
    {
        OpenMetadataTypesArchive openMetadataTypesArchive = new OpenMetadataTypesArchive();

        this.writeOpenMetadataArchive(defaultOpenMetadataArchiveFileName,
                                      openMetadataTypesArchive.getOpenMetadataArchive());
    }


    /**
     * Main program to control the archive writer.
     * Note: See issue #3392 if logging is needed
     *
     * @param args ignored arguments
     * @throws JoranException something wrong with logback
     */
    public static void main(String[] args) throws JoranException
    {
        //if logback.xml is not present and no config in the command line, set default configuration to logback-om.xml
        if (ClassLoader.getSystemResource("logback.xml") == null
                &&  System.getProperty(ContextInitializer.CONFIG_FILE_PROPERTY) == null)
        {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            context.reset();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            configurator.doConfigure(OpenMetadataTypesArchiveWriter.class.getResourceAsStream("/logback-om.xml"));
        }

        OpenMetadataTypesArchiveWriter archiveWriter = new OpenMetadataTypesArchiveWriter();

        archiveWriter.writeOpenMetadataTypesArchive();
    }
}
