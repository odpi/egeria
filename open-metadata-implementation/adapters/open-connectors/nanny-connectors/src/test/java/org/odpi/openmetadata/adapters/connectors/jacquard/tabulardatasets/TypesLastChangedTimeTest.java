/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets;

import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes.OpenMetadataPropertiesDataSetConnector;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataEntityDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefGallery;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Date;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;


/**
 * The Open Metadata Data Types and Properties data sets take their create and update time from the time the
 * open metadata types last changed, so that Jacquard can tell whether they have changed.
 */
public class TypesLastChangedTimeTest
{
    /**
     * The latest of the type definitions' create and update times is used.
     */
    @Test
    public void testLatestChangeIsUsed()
    {
        OpenMetadataDataSetConnectorBase connector = new OpenMetadataPropertiesDataSetConnector();

        OpenMetadataTypeDefGallery gallery = new OpenMetadataTypeDefGallery();

        gallery.setTypeDefs(Arrays.asList(getTypeDef(new Date(1000L), null),
                                          getTypeDef(new Date(2000L), new Date(5000L)),
                                          null,
                                          getTypeDef(new Date(4000L), null)));

        assertEquals(connector.getTypesLastChangedTime(gallery), new Date(5000L));
        assertNull(connector.getTypesLastChangedTime(new OpenMetadataTypeDefGallery()));
        assertNull(connector.getTypesLastChangedTime(null));
    }


    private OpenMetadataTypeDef getTypeDef(Date createTime, Date updateTime)
    {
        OpenMetadataTypeDef typeDef = new OpenMetadataEntityDef();

        typeDef.setCreateTime(createTime);
        typeDef.setUpdateTime(updateTime);

        return typeDef;
    }
}
