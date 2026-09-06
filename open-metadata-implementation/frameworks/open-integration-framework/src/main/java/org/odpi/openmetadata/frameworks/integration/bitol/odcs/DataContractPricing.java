/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the pricing information for the data described by an Open Data Contract Standard (ODCS)
 * data contract.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractPricing
{
    private String id = null;
    private Number priceAmount = null;
    private String priceCurrency = null;
    private String priceUnit = null;


    /**
     * Default constructor
     */
    public DataContractPricing()
    {
    }


    /**
     * Return the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @return string identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @param id string identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the subscription price per unit of measure.
     *
     * @return numeric amount
     */
    public Number getPriceAmount()
    {
        return priceAmount;
    }


    /**
     * Set up the subscription price per unit of measure.
     *
     * @param priceAmount numeric amount
     */
    public void setPriceAmount(Number priceAmount)
    {
        this.priceAmount = priceAmount;
    }


    /**
     * Return the currency of the subscription price (for example USD).
     *
     * @return string currency code
     */
    public String getPriceCurrency()
    {
        return priceCurrency;
    }


    /**
     * Set up the currency of the subscription price (for example USD).
     *
     * @param priceCurrency string currency code
     */
    public void setPriceCurrency(String priceCurrency)
    {
        this.priceCurrency = priceCurrency;
    }


    /**
     * Return the unit of measure used to calculate the cost (for example megabyte).
     *
     * @return string unit
     */
    public String getPriceUnit()
    {
        return priceUnit;
    }


    /**
     * Set up the unit of measure used to calculate the cost (for example megabyte).
     *
     * @param priceUnit string unit
     */
    public void setPriceUnit(String priceUnit)
    {
        this.priceUnit = priceUnit;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractPricing{" +
                       "id='" + id + '\'' +
                       ", priceAmount=" + priceAmount +
                       ", priceCurrency='" + priceCurrency + '\'' +
                       ", priceUnit='" + priceUnit + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        DataContractPricing that = (DataContractPricing) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(priceAmount, that.priceAmount) &&
                       Objects.equals(priceCurrency, that.priceCurrency) &&
                       Objects.equals(priceUnit, that.priceUnit);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, priceAmount, priceCurrency, priceUnit);
    }
}
