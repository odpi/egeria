/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ClassificationProxyRequest is used when working with classifications using an entity proxy.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationProxyRequest extends ClassificationRequest
{
    private static final long    serialVersionUID = 1L;

    private EntityProxy entityProxy = null;


    /**
     * Default constructor
     */
    public ClassificationProxyRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ClassificationProxyRequest(ClassificationProxyRequest template)
    {
        super(template);

        if (template != null)
        {
            this.entityProxy = template.getEntityProxy();
        }
    }


    /**
     * Return entity proxy.
     *
     * @return instance properties object
     */
    public EntityProxy getEntityProxy()
    {
        return entityProxy;
    }


    /**
     * Set up the entity proxy.
     *
     * @param entityProxy InstanceProperties object
     */
    public void setEntityProxy(EntityProxy entityProxy)
    {
        this.entityProxy = entityProxy;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ClassificationProxyRequest{" +
                       "entityProxy=" + entityProxy +
                       ", classificationOrigin=" + getClassificationOrigin() +
                       ", classificationOriginGUID='" + getClassificationOriginGUID() + '\'' +
                       ", classificationProperties=" + getClassificationProperties() +
                       ", metadataCollectionId='" + getMetadataCollectionId() + '\'' +
                       ", metadataCollectionName='" + getMetadataCollectionName() + '\'' +
                       '}';
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
        if (! (objectToCompare instanceof ClassificationProxyRequest))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        ClassificationProxyRequest that = (ClassificationProxyRequest) objectToCompare;

        return entityProxy != null ? entityProxy.equals(that.entityProxy) : that.entityProxy == null;
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (entityProxy != null ? entityProxy.hashCode() : 0);
        return result;
    }
}
