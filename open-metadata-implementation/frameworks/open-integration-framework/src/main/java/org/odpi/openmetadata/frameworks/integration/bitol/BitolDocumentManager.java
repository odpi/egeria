/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol;

import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;

/**
 * BitolDocumentManager distributes Bitol documents (Open Data Contract Standard data contracts and Open Data
 * Product Standard data products) to the integration connectors that have registered a BitolDocumentListener.
 * It is implemented by the integration context manager and made available to integration connectors through the
 * integration context.  Documents may be published in their raw form (YAML or JSON) or as beans.
 */
public interface BitolDocumentManager
{
    /**
     * Register a listener to receive Bitol documents.  The listener is implemented by an integration connector.
     *
     * @param listener listener to call
     */
    void registerListener(BitolDocumentListener listener);


    /**
     * Publish a Bitol document of either kind.  The document is parsed and routed to the listeners according to
     * its "kind" property.
     *
     * @param rawDocument document in YAML or JSON format
     */
    void publishBitolDocument(String rawDocument);


    /**
     * Publish an Open Data Contract Standard (ODCS) data contract.
     *
     * @param rawDocument document in YAML or JSON format
     */
    void publishDataContract(String rawDocument);


    /**
     * Publish an Open Data Contract Standard (ODCS) data contract.
     *
     * @param dataContract bean for the document
     */
    void publishDataContract(DataContract dataContract);


    /**
     * Publish an Open Data Product Standard (ODPS) data product.
     *
     * @param rawDocument document in YAML or JSON format
     */
    void publishDataProduct(String rawDocument);


    /**
     * Publish an Open Data Product Standard (ODPS) data product.
     *
     * @param dataProduct bean for the document
     */
    void publishDataProduct(DataProduct dataProduct);
}
