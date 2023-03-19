/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.metadataelements;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;

/**
 * MetadataElement is the common interface for all metadata elements.  It adds the header information that is stored with the properties.
 * This includes detains of its unique identifier, type and origin.
 */
public interface MetadataElement
{
    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    ElementHeader getElementHeader();


    /**
     * Set up the element header associated with the properties.
     *
     * @param header element header object
     */
    void setElementHeader(ElementHeader header);
}
