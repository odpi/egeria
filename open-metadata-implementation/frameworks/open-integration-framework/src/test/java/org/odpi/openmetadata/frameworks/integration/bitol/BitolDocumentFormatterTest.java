/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol;

import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentTest.readResource;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Validate the BitolDocumentFormatter utility.
 */
public class BitolDocumentFormatterTest
{
    /**
     * Default constructor
     */
    public BitolDocumentFormatterTest()
    {
    }


    /**
     * Documents parse by kind and round trip through YAML and JSON.
     *
     * @throws IOException problem reading the examples
     */
    @Test public void testParseAndWrite() throws IOException
    {
        String        rawContract = readResource("bitol/odcs/full-example.odcs.yaml");
        BitolDocument document    = BitolDocumentFormatter.parseDocument(rawContract);

        assertEquals(BitolDocumentFormatter.getKind(rawContract), BitolDocument.DATA_CONTRACT_KIND);
        assertTrue(document instanceof DataContract);

        String yaml = BitolDocumentFormatter.toYAML(document);
        assertFalse(yaml.startsWith("---"), "document start marker should be suppressed");
        assertEquals(BitolDocumentFormatter.parseDataContract(yaml), document);

        String json = BitolDocumentFormatter.toJSON(document);
        assertTrue(json.startsWith("{"));
        assertEquals(BitolDocumentFormatter.parseDocument(json), document);

        String rawProduct = readResource("bitol/odps/simple-data-product.odps.yaml");

        assertEquals(BitolDocumentFormatter.getKind(rawProduct), BitolDocument.DATA_PRODUCT_KIND);
        assertTrue(BitolDocumentFormatter.parseDocument(rawProduct) instanceof DataProduct);
        assertEquals(BitolDocumentFormatter.parseDataProduct(rawProduct).getName(), "Simple Data Product");
    }


    /**
     * Malformed and mismatched documents fail with an IOException.
     */
    @Test public void testErrors()
    {
        try
        {
            BitolDocumentFormatter.parseDocument("kind: SomethingElse\nid: x\n");
            fail("unknown kind should not parse");
        }
        catch (IOException expected)
        {
            // expected
        }

        try
        {
            BitolDocumentFormatter.parseDataContract("kind: DataProduct\nid: x\nstatus: active\napiVersion: v1.0.0\n");
            fail("a data product should not parse as a data contract");
        }
        catch (IOException expected)
        {
            // expected
        }

        try
        {
            BitolDocumentFormatter.getKind("{ not yaml: [");
            fail("malformed document should not parse");
        }
        catch (IOException expected)
        {
            // expected
        }

        try
        {
            assertNull(BitolDocumentFormatter.getKind("id: x\n"), "document without kind");
        }
        catch (IOException error)
        {
            fail("a document without kind is still valid YAML");
        }
    }
}
