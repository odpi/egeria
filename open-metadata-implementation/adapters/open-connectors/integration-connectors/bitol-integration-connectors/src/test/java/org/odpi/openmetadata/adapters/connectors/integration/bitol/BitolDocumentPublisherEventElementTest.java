/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.expectThrows;


/**
 * The publisher reacts to events about digital products and agreements.  An element the event names may have
 * gone, or may not be visible to the publisher's user (a proposed product, for example); the publisher skips it
 * rather than reporting an unexpected exception for every such event.
 */
public class BitolDocumentPublisherEventElementTest
{
    /**
     * An available element is returned.
     *
     * @throws Exception not expected
     */
    @Test
    public void testAvailableElementReturned() throws Exception
    {
        OpenMetadataRootElement element = new OpenMetadataRootElement();

        assertSame(BitolDocumentPublisherIntegrationConnector.readEventElement(() -> element), element);
    }


    /**
     * An element that is not visible (or no longer there) is reported by the repository as an invalid parameter
     * and is skipped.
     *
     * @throws Exception not expected
     */
    @Test
    public void testUnavailableElementSkipped() throws Exception
    {
        assertNull(BitolDocumentPublisherIntegrationConnector.readEventElement(() ->
        {
            throw new InvalidParameterException(OMFErrorCode.UNKNOWN_ELEMENT.getMessageDefinition("guid", "digitalProductGUID", "getCollectionByGUID"),
                                                this.getClass().getName(),
                                                "getCollectionByGUID",
                                                "digitalProductGUID");
        }));
    }


    /**
     * Any other problem is still reported.
     */
    @Test
    public void testOtherProblemReported()
    {
        expectThrows(PropertyServerException.class, () -> BitolDocumentPublisherIntegrationConnector.readEventElement(() ->
        {
            throw new PropertyServerException(OMFErrorCode.UNKNOWN_ELEMENT.getMessageDefinition("guid", "digitalProductGUID", "getCollectionByGUID"),
                                              this.getClass().getName(),
                                              "getCollectionByGUID");
        }));
    }
}
