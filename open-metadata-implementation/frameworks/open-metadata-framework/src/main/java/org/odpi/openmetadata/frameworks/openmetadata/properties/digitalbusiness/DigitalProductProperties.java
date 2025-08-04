/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalProductProperties describes the properties that describe a digital product.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DigitalProductProperties extends CollectionProperties
{
    private String userDefinedStatus = null;
    private String productName       = null;
    private String identifier        = null;
    private Date   introductionDate  = null;
    private String maturity          = null;
    private String serviceLife       = null;
    private Date   nextVersionDate   = null;
    private Date   withdrawDate      = null;


    /**
     * Default constructor
     */
    public DigitalProductProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.DIGITAL_PRODUCT.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public DigitalProductProperties(DigitalProductProperties template)
    {
        super(template);

        if (template != null)
        {
            userDefinedStatus = template.getUserDefinedStatus();
            productName       = template.getProductName();
            identifier        = template.getIdentifier();
            introductionDate  = template.getIntroductionDate();
            maturity          = template.getMaturity();
            serviceLife       = template.getServiceLife();
            nextVersionDate   = template.getNextVersionDate();
            withdrawDate      = template.getWithdrawDate();
        }
    }


    /**
     * Return the status of the product.
     *
     * @return string
     */
    public String getUserDefinedStatus()
    {
        return userDefinedStatus;
    }


    /**
     * Set up the status of the product
     *
     * @param userDefinedStatus string
     */
    public void setUserDefinedStatus(String userDefinedStatus)
    {
        this.userDefinedStatus = userDefinedStatus;
    }


    /**
     * Return the product name.
     *
     * @return string name
     */
    public String getProductName()
    {
        return productName;
    }


    /**
     * Set up the product name.
     *
     * @param productName string name
     */
    public void setProductName(String productName)
    {
        this.productName = productName;
    }


    /**
     * Return the product's identifier
     *
     * @return string
     */
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Set up the product's identifier.
     *
     * @param identifier string
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }



    /**
     * Return the date that the product was added to the market.
     *
     * @return date
     */
    public Date getIntroductionDate()
    {
        return introductionDate;
    }


    /**
     * Set up the date that the product was added to the market.
     *
     * @param introductionDate date
     */
    public void setIntroductionDate(Date introductionDate)
    {
        this.introductionDate = introductionDate;
    }


    /**
     * Return the maturity level of the product.
     *
     * @return string name
     */
    public String getMaturity()
    {
        return maturity;
    }


    /**
     * Set up the maturity level of the product.
     *
     * @param maturity string name
     */
    public void setMaturity(String maturity)
    {
        this.maturity = maturity;
    }


    /**
     * Return the estimated lifetime of the product.
     *
     * @return text
     */
    public String getServiceLife()
    {
        return serviceLife;
    }


    /**
     * Set up the estimated lifetime of the product.
     *
     * @param serviceLife text
     */
    public void setServiceLife(String serviceLife)
    {
        this.serviceLife = serviceLife;
    }


    /**
     * Return the date of the next version of the digital service.
     *
     * @return date
     */
    public Date getNextVersionDate()
    {
        return nextVersionDate;
    }


    /**
     * Set up the date of the next version of the digital service.
     *
     * @param nextVersionDate date
     */
    public void setNextVersionDate(Date nextVersionDate)
    {
        this.nextVersionDate = nextVersionDate;
    }


    /**
     * Return the date when the product is being withdrawn.
     *
     * @return date
     */
    public Date getWithdrawDate()
    {
        return withdrawDate;
    }


    /**
     * Set up the date when the product is being withdrawn.
     *
     * @param withdrawDate date
     */
    public void setWithdrawDate(Date withdrawDate)
    {
        this.withdrawDate = withdrawDate;
    }




    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DigitalProductProperties{" +
                "userDefinedStatus=" + userDefinedStatus +
                ", productName='" + productName + '\'' +
                ", identifier='" + identifier + '\'' +
                ", introductionDate=" + introductionDate +
                ", maturity='" + maturity + '\'' +
                ", serviceLife='" + serviceLife + '\'' +
                ", nextVersionDate=" + nextVersionDate +
                ", withdrawDate=" + withdrawDate +
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
        if (! (objectToCompare instanceof DigitalProductProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(userDefinedStatus, that.userDefinedStatus) &&
                       Objects.equals(productName, that.productName) &&
                       Objects.equals(identifier, that.identifier) &&
                       Objects.equals(introductionDate, that.introductionDate) &&
                       Objects.equals(maturity, that.maturity) &&
                       Objects.equals(serviceLife, that.serviceLife)  &&
                       Objects.equals(nextVersionDate, that.nextVersionDate) &&
                       Objects.equals(withdrawDate, that.withdrawDate);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), userDefinedStatus, productName, introductionDate,
                            maturity, serviceLife, nextVersionDate, withdrawDate, identifier);
    }
}
