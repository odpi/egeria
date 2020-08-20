/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

/**
 * MockSchemaElementBean provides a concrete class for the SchemaElement bean.
 */
public class MockSchemaElement extends SchemaElement
{
    private static final long     serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public MockSchemaElement()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param templateSchema template object to copy.
     */
    public MockSchemaElement(MockSchemaElement templateSchema)
    {
        super(templateSchema);
    }


    /**
     * Return a clone of this schema element.  This method is needed because schema element
     * is abstract.
     *
     * @return Either a StructSchemaType or a PrimitiveSchemaType depending on the type of the template.
     */
    public MockSchemaElement cloneSchemaElement()
    {
        return new MockSchemaElement(this);
    }
}