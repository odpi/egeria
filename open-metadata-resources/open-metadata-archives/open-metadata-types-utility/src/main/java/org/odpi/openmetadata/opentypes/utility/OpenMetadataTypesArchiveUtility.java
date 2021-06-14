/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes.utility;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchiveWriter;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;


/**
 * OpenMetadataTypesArchiveWriter create a physical open metadata archive file for the supplied open metadata archives
 * encoded using Open Metadata Repository Services (OMRS) formats.
 */
public class OpenMetadataTypesArchiveUtility
{

    /**
     * Main program to control the archive writer.
     * Note: See issue #3392 if logging is needed
     *
     * @param args ignored arguments
     * @throws JoranException something wrong with logback
     */
    public static void main(String[] args) throws JoranException
    {
        OpenMetadataTypesArchiveWriter archiveWriter = new OpenMetadataTypesArchiveWriter();

        archiveWriter.writeOpenMetadataTypesArchive();
    }
}
