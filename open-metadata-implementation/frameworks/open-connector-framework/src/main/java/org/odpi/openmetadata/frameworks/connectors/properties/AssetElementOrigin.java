/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementOrigin;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementType bean provides details of the type information associated with a metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetElementOrigin extends AssetPropertyElementBase
{
    private final ElementOrigin elementBean;


    /**
     * Bean constructor accepts bean version of the AssetElementOrigin to provide the values
     *
     * @param elementBean bean containing properties
     */
    public AssetElementOrigin(ElementOrigin elementBean)
    {
        super();

        if (elementBean == null)
        {
            this.elementBean = new ElementOrigin();
        }
        else
        {
            this.elementBean = elementBean;
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template type to clone
     */
    public AssetElementOrigin(AssetElementOrigin template)
    {
        super(template);

        if (template == null)
        {
            this.elementBean = new ElementOrigin();
        }
        else
        {
            this.elementBean = template.getElementBean();
        }
    }



    /**
     * Clone the bean as part of the deep clone constructor.
     *
     * @return cloned element bean
     */
    protected ElementOrigin getElementBean()
    {
        return new ElementOrigin(elementBean);
    }


    /**
     * Return the name of the server where the element was retrieved from.  Typically, this is
     * a server where the OMAS interfaces are activated.  If no name is known for the server then null is returned.
     *
     * @return elementSourceServerURL the url of the server where the element came from
     */
    public String getSourceServer()
    {
        return elementBean.getSourceServer();
    }

    /**
     * Return the origin of the metadata element.
     *
     * @return ElementOriginCategory enum
     */
    public ElementOriginCategory getOriginCategory() { return elementBean.getOriginCategory(); }


    /**
     * Returns the OMRS identifier for the metadata collection that is managed by the repository
     * where the element originates (its home repository).
     *
     * @return String metadata collection id
     */
    public String getHomeMetadataCollectionId()
    {
        return elementBean.getHomeMetadataCollectionId();
    }


    /**
     * Return the name of the metadata collection that this asset belongs to.
     *
     * @return name string
     */
    public String getHomeMetadataCollectionName()
    {
        return elementBean.getHomeMetadataCollectionName();
    }


    /**
     * Return the license associated with this metadata element (null means none).
     *
     * @return string license name
     */
    public String getLicense()
    {
        return elementBean.getLicense();
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {

        return elementBean.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof AssetElementOrigin))
        {
            return false;
        }
        AssetElementOrigin that = (AssetElementOrigin) objectToCompare;
        return Objects.equals(getSourceServer(), that.getSourceServer()) &&
                getOriginCategory() == that.getOriginCategory() &&
                Objects.equals(getHomeMetadataCollectionId(), that.getHomeMetadataCollectionId()) &&
                Objects.equals(getHomeMetadataCollectionName(), that.getHomeMetadataCollectionName()) &&
                Objects.equals(getLicense(), that.getLicense());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getSourceServer(), getOriginCategory(),
                            getHomeMetadataCollectionId(), getHomeMetadataCollectionName(),
                            getLicense());
    }
}
