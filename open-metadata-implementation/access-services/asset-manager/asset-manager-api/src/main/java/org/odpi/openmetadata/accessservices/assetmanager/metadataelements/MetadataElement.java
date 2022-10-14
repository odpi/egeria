/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.metadataelements;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

import java.util.List;

/**
 * MetadataElement is the common interface for all metadata elements.  It adds the header information that is stored with the properties.
 * This includes detains of its unique identifier, type and origin.
 */
public interface MetadataElement
{
    /**
     * Return the element header associated with the open metadata element.
     *
     * @return element header object
     */
    ElementHeader getElementHeader();


    /**
     * Set up the element header associated with the open metadata element.
     *
     * @param elementHeader element header object
     */
    void setElementHeader(ElementHeader elementHeader);


    /**
     * Return the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @return list of correlation properties objects
     */
    List<MetadataCorrelationHeader> getCorrelationHeaders();


    /**
     * Set up the details of the external identifier and other correlation properties about the metadata source.
     * There is one entry in the list for each element in the third party technology that maps to the single open source
     * element.
     *
     * @param correlationHeaders list of correlation properties objects
     */
    void setCorrelationHeaders(List<MetadataCorrelationHeader> correlationHeaders);
}
