/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol;

import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;

/**
 * BitolDocumentListener is implemented by an integration connector that wishes to receive the Bitol documents
 * (Open Data Contract Standard data contracts and Open Data Product Standard data products) published to the
 * integration daemon.  The listener is registered through the integration context.  Each method receives both the
 * parsed bean and the raw document so that the connector can work with whichever is more convenient.  The bean is
 * null if the document could not be parsed, or if its apiVersion is not one that Egeria's beans represent, in which
 * case the raw document is still supplied so that it can be stored or forwarded.  Both methods have empty default
 * implementations so that a connector only interested in one kind of document need only override that method.
 */
public interface BitolDocumentListener
{
    /**
     * Called each time an Open Data Contract Standard (ODCS) data contract is published to the integration daemon.
     *
     * @param dataContract parsed document (null if the raw document could not be parsed into the bean)
     * @param rawDocument the document as received (YAML or JSON)
     */
    default void processDataContract(DataContract dataContract,
                                     String       rawDocument)
    {
    }


    /**
     * Called each time an Open Data Product Standard (ODPS) data product is published to the integration daemon.
     *
     * @param dataProduct parsed document (null if the raw document could not be parsed into the bean)
     * @param rawDocument the document as received (YAML or JSON)
     */
    default void processDataProduct(DataProduct dataProduct,
                                    String      rawDocument)
    {
    }
}
