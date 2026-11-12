/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat;

/**
 * DcatDocumentManager distributes DCAT documents to the integration connectors that have registered a listener.
 */
public interface DcatDocumentManager
{
    /**
     * The listener is implemented by the integration connector.  Once it is registered with the context, its
     * processDcatDocument() method is called each time a DCAT document is published to the integration daemon.
     *
     * @param listener listener to call
     */
    void registerDcatListener(DcatDocumentListener listener);


    /**
     * Publish a DCAT document.  The document is parsed and passed to each of the registered listeners.
     *
     * @param rawDocument document in JSON-LD format
     */
    void publishDcatDocument(String rawDocument);
}
