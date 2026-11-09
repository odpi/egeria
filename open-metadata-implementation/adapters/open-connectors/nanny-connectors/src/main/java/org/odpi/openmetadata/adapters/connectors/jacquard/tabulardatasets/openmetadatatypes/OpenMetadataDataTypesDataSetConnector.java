/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataAttributeTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefGallery;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataDataTypesDataSetConnector lists the data types that can be assigned to a property in the open
 * metadata types (the primitives such as string and int, the collections such as map&lt;string,string&gt;, and
 * the enums) as if they are a tabular data set.  There is one row per data type, in name order.
 */
public class OpenMetadataDataTypesDataSetConnector extends OpenMetadataRowDataSetConnectorBase
{
    private static final String myConnectorName = OpenMetadataDataTypesDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public OpenMetadataDataTypesDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.DATA_TYPES_LIST);
    }


    /**
     * One row per attribute type definition.
     *
     * @throws Exception problem retrieving the types
     */
    @Override
    protected void collectRows() throws Exception
    {
        OpenMetadataTypeDefGallery gallery = connectorContext.getOpenMetadataTypesClient().getAllTypes(true, true);

        if ((gallery == null) || (gallery.getAttributeTypeDefs() == null))
        {
            return;
        }

        List<OpenMetadataAttributeTypeDef> dataTypes = new ArrayList<>();

        for (OpenMetadataAttributeTypeDef dataType : gallery.getAttributeTypeDefs())
        {
            if ((dataType != null) && (dataType.getName() != null))
            {
                dataTypes.add(dataType);
            }
        }

        dataTypes.sort(Comparator.comparing(OpenMetadataAttributeTypeDef::getName));

        for (OpenMetadataAttributeTypeDef dataType : dataTypes)
        {
            Map<String, String> values = newValues();

            putValue(values, ProductDataFieldDefinition.DATA_TYPE, dataType.getName());
            putValue(values, ProductDataFieldDefinition.GUID, dataType.getGUID());
            putValue(values, ProductDataFieldDefinition.DESCRIPTION, dataType.getDescription());
            putValue(values, ProductDataFieldDefinition.CATEGORY, dataType.getCategory() == null ? null : dataType.getCategory().getTypeName());
            putValue(values, ProductDataFieldDefinition.VERSION_IDENTIFIER, dataType.getVersionName());
            putValue(values, ProductDataFieldDefinition.VERSION, dataType.getVersion());

            addRow(null, null, values);
        }
    }
}
