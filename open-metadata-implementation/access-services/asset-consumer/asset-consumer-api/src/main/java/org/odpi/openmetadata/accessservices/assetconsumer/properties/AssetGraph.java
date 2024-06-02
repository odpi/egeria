/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetconsumer.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;

import java.util.List;

/**
 * AssetGraph is used to return an asset along with all of its anchored elements and the relationships
 * that they have between one another and to other elements.
 */
public class AssetGraph extends Asset
{
    List<MetadataElement>      anchoredElements = null;
    List<MetadataRelationship> relationships = null;

    /**
     * Default constructor
     */
    public AssetGraph()
    {
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template template values for asset summary
     */
    public AssetGraph(Asset template)
    {
        super(template);

        if (template != null)
        {

        }
    }


}
