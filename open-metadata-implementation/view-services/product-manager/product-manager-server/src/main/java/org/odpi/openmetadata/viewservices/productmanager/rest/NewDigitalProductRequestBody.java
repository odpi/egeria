/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewDigitalProductRequestBody describes a new digital product.  The product itself is defined in the same way as
 * any other new element; the additional fields name the existing elements that the new product is linked to.
 * Each of them is optional.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewDigitalProductRequestBody extends NewElementRequestBody
{
    private String       productManagerGUID        = null;
    private String       productCommunityGUID      = null;
    private List<String> collectionGUIDs           = null;
    private List<String> questionGUIDs             = null;
    private String       productAssetGUID          = null;
    private List<String> governanceDefinitionGUIDs = null;
    private String       dataSpecGUID              = null;


    /**
     * Default constructor
     */
    public NewDigitalProductRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewDigitalProductRequestBody(NewDigitalProductRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.productManagerGUID        = template.getProductManagerGUID();
            this.productCommunityGUID      = template.getProductCommunityGUID();
            this.collectionGUIDs           = template.getCollectionGUIDs();
            this.questionGUIDs             = template.getQuestionGUIDs();
            this.productAssetGUID          = template.getProductAssetGUID();
            this.governanceDefinitionGUIDs = template.getGovernanceDefinitionGUIDs();
            this.dataSpecGUID              = template.getDataSpecGUID();
        }
    }


    /**
     * Return the unique identifier of the actor role that manages the product.
     *
     * @return guid
     */
    public String getProductManagerGUID()
    {
        return productManagerGUID;
    }


    /**
     * Set up the unique identifier of the actor role that manages the product.
     *
     * @param productManagerGUID guid
     */
    public void setProductManagerGUID(String productManagerGUID)
    {
        this.productManagerGUID = productManagerGUID;
    }


    /**
     * Return the unique identifier of the community that discusses the product.
     *
     * @return guid
     */
    public String getProductCommunityGUID()
    {
        return productCommunityGUID;
    }


    /**
     * Set up the unique identifier of the community that discusses the product.
     *
     * @param productCommunityGUID guid
     */
    public void setProductCommunityGUID(String productCommunityGUID)
    {
        this.productCommunityGUID = productCommunityGUID;
    }


    /**
     * Return the unique identifiers of the collections that the product is a member of - typically the product
     * catalog folders and digital product families it belongs to.
     *
     * @return list of guids
     */
    public List<String> getCollectionGUIDs()
    {
        return collectionGUIDs;
    }


    /**
     * Set up the unique identifiers of the collections that the product is a member of.
     *
     * @param collectionGUIDs list of guids
     */
    public void setCollectionGUIDs(List<String> collectionGUIDs)
    {
        this.collectionGUIDs = collectionGUIDs;
    }


    /**
     * Return the unique identifiers of the glossary terms that describe the questions the product answers.
     *
     * @return list of guids
     */
    public List<String> getQuestionGUIDs()
    {
        return questionGUIDs;
    }


    /**
     * Set up the unique identifiers of the glossary terms that describe the questions the product answers.
     *
     * @param questionGUIDs list of guids
     */
    public void setQuestionGUIDs(List<String> questionGUIDs)
    {
        this.questionGUIDs = questionGUIDs;
    }


    /**
     * Return the unique identifier of the asset that holds the product's data.
     *
     * @return guid
     */
    public String getProductAssetGUID()
    {
        return productAssetGUID;
    }


    /**
     * Set up the unique identifier of the asset that holds the product's data.
     *
     * @param productAssetGUID guid
     */
    public void setProductAssetGUID(String productAssetGUID)
    {
        this.productAssetGUID = productAssetGUID;
    }


    /**
     * Return the unique identifiers of the governance definitions that the product is governed by, such as the
     * license type granted to subscribers.
     *
     * @return list of guids
     */
    public List<String> getGovernanceDefinitionGUIDs()
    {
        return governanceDefinitionGUIDs;
    }


    /**
     * Set up the unique identifiers of the governance definitions that the product is governed by.
     *
     * @param governanceDefinitionGUIDs list of guids
     */
    public void setGovernanceDefinitionGUIDs(List<String> governanceDefinitionGUIDs)
    {
        this.governanceDefinitionGUIDs = governanceDefinitionGUIDs;
    }


    /**
     * Return the unique identifier of the data specification collection that describes the product's data.
     *
     * @return guid
     */
    public String getDataSpecGUID()
    {
        return dataSpecGUID;
    }


    /**
     * Set up the unique identifier of the data specification collection that describes the product's data.
     *
     * @param dataSpecGUID guid
     */
    public void setDataSpecGUID(String dataSpecGUID)
    {
        this.dataSpecGUID = dataSpecGUID;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "NewDigitalProductRequestBody{" +
                "productManagerGUID='" + productManagerGUID + '\'' +
                ", productCommunityGUID='" + productCommunityGUID + '\'' +
                ", collectionGUIDs=" + collectionGUIDs +
                ", questionGUIDs=" + questionGUIDs +
                ", productAssetGUID='" + productAssetGUID + '\'' +
                ", governanceDefinitionGUIDs=" + governanceDefinitionGUIDs +
                ", dataSpecGUID='" + dataSpecGUID + '\'' +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof NewDigitalProductRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(productManagerGUID, that.productManagerGUID) &&
                Objects.equals(productCommunityGUID, that.productCommunityGUID) &&
                Objects.equals(collectionGUIDs, that.collectionGUIDs) &&
                Objects.equals(questionGUIDs, that.questionGUIDs) &&
                Objects.equals(productAssetGUID, that.productAssetGUID) &&
                Objects.equals(governanceDefinitionGUIDs, that.governanceDefinitionGUIDs) &&
                Objects.equals(dataSpecGUID, that.dataSpecGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), productManagerGUID, productCommunityGUID, collectionGUIDs, questionGUIDs,
                            productAssetGUID, governanceDefinitionGUIDs, dataSpecGUID);
    }
}
