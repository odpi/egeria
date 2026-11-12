/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat;

import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDocument;

/**
 * DcatDocumentListener is the interface that an integration connector implements and registers with the
 * integration context in order to receive the DCAT documents that are published to the integration daemon.
 */
public interface DcatDocumentListener
{
    /**
     * Called each time a DCAT document is published to the integration daemon.  The listener is called for each
     * document that is successfully parsed.
     *
     * @param document bean version of the document
     * @param rawDocument JSON-LD version of the document
     */
    void processDcatDocument(DcatDocument document,
                             String       rawDocument);
}
