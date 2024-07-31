/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceengine.metadataelements;


import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;

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
}
