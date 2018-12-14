/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'non_steward_user' asset type in IGC, displayed as 'User' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class NonStewardUser extends Reference {

    public static String getIgcTypeId() { return "non_steward_user"; }

    /**
     * The 'full_name' property, displayed as 'Full Name' in the IGC UI.
     */
    protected String full_name;

    /**
     * The 'job_title' property, displayed as 'Job Title' in the IGC UI.
     */
    protected String job_title;

    /**
     * The 'email_address' property, displayed as 'Email Address' in the IGC UI.
     */
    protected String email_address;

    /**
     * The 'office_phone_number' property, displayed as 'Office Phone Number' in the IGC UI.
     */
    protected String office_phone_number;

    /**
     * The 'mobile_phone_number' property, displayed as 'Mobile Phone Number' in the IGC UI.
     */
    protected String mobile_phone_number;

    /**
     * The 'principal_id' property, displayed as 'User Name' in the IGC UI.
     */
    protected String principal_id;

    /**
     * The 'given_name' property, displayed as 'Given Name' in the IGC UI.
     */
    protected String given_name;

    /**
     * The 'surname' property, displayed as 'Surname' in the IGC UI.
     */
    protected String surname;

    /**
     * The 'courtesy_title' property, displayed as 'Courtesy Title' in the IGC UI.
     */
    protected String courtesy_title;

    /**
     * The 'organization' property, displayed as 'Organization' in the IGC UI.
     */
    protected String organization;

    /**
     * The 'location' property, displayed as 'Location' in the IGC UI.
     */
    protected String location;

    /**
     * The 'business_address' property, displayed as 'Business Address' in the IGC UI.
     */
    protected String business_address;

    /**
     * The 'home_phone_number' property, displayed as 'Home Phone Number' in the IGC UI.
     */
    protected String home_phone_number;

    /**
     * The 'fax_number' property, displayed as 'Fax Number' in the IGC UI.
     */
    protected String fax_number;

    /**
     * The 'pager_number' property, displayed as 'Pager Number' in the IGC UI.
     */
    protected String pager_number;

    /**
     * The 'instant_message_id' property, displayed as 'Instant Message ID' in the IGC UI.
     */
    protected String instant_message_id;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #full_name */ @JsonProperty("full_name")  public String getFullName() { return this.full_name; }
    /** @see #full_name */ @JsonProperty("full_name")  public void setFullName(String full_name) { this.full_name = full_name; }

    /** @see #job_title */ @JsonProperty("job_title")  public String getJobTitle() { return this.job_title; }
    /** @see #job_title */ @JsonProperty("job_title")  public void setJobTitle(String job_title) { this.job_title = job_title; }

    /** @see #email_address */ @JsonProperty("email_address")  public String getEmailAddress() { return this.email_address; }
    /** @see #email_address */ @JsonProperty("email_address")  public void setEmailAddress(String email_address) { this.email_address = email_address; }

    /** @see #office_phone_number */ @JsonProperty("office_phone_number")  public String getOfficePhoneNumber() { return this.office_phone_number; }
    /** @see #office_phone_number */ @JsonProperty("office_phone_number")  public void setOfficePhoneNumber(String office_phone_number) { this.office_phone_number = office_phone_number; }

    /** @see #mobile_phone_number */ @JsonProperty("mobile_phone_number")  public String getMobilePhoneNumber() { return this.mobile_phone_number; }
    /** @see #mobile_phone_number */ @JsonProperty("mobile_phone_number")  public void setMobilePhoneNumber(String mobile_phone_number) { this.mobile_phone_number = mobile_phone_number; }

    /** @see #principal_id */ @JsonProperty("principal_id")  public String getPrincipalId() { return this.principal_id; }
    /** @see #principal_id */ @JsonProperty("principal_id")  public void setPrincipalId(String principal_id) { this.principal_id = principal_id; }

    /** @see #given_name */ @JsonProperty("given_name")  public String getGivenName() { return this.given_name; }
    /** @see #given_name */ @JsonProperty("given_name")  public void setGivenName(String given_name) { this.given_name = given_name; }

    /** @see #surname */ @JsonProperty("surname")  public String getSurname() { return this.surname; }
    /** @see #surname */ @JsonProperty("surname")  public void setSurname(String surname) { this.surname = surname; }

    /** @see #courtesy_title */ @JsonProperty("courtesy_title")  public String getCourtesyTitle() { return this.courtesy_title; }
    /** @see #courtesy_title */ @JsonProperty("courtesy_title")  public void setCourtesyTitle(String courtesy_title) { this.courtesy_title = courtesy_title; }

    /** @see #organization */ @JsonProperty("organization")  public String getOrganization() { return this.organization; }
    /** @see #organization */ @JsonProperty("organization")  public void setOrganization(String organization) { this.organization = organization; }

    /** @see #location */ @JsonProperty("location")  public String getLocation() { return this.location; }
    /** @see #location */ @JsonProperty("location")  public void setLocation(String location) { this.location = location; }

    /** @see #business_address */ @JsonProperty("business_address")  public String getBusinessAddress() { return this.business_address; }
    /** @see #business_address */ @JsonProperty("business_address")  public void setBusinessAddress(String business_address) { this.business_address = business_address; }

    /** @see #home_phone_number */ @JsonProperty("home_phone_number")  public String getHomePhoneNumber() { return this.home_phone_number; }
    /** @see #home_phone_number */ @JsonProperty("home_phone_number")  public void setHomePhoneNumber(String home_phone_number) { this.home_phone_number = home_phone_number; }

    /** @see #fax_number */ @JsonProperty("fax_number")  public String getFaxNumber() { return this.fax_number; }
    /** @see #fax_number */ @JsonProperty("fax_number")  public void setFaxNumber(String fax_number) { this.fax_number = fax_number; }

    /** @see #pager_number */ @JsonProperty("pager_number")  public String getPagerNumber() { return this.pager_number; }
    /** @see #pager_number */ @JsonProperty("pager_number")  public void setPagerNumber(String pager_number) { this.pager_number = pager_number; }

    /** @see #instant_message_id */ @JsonProperty("instant_message_id")  public String getInstantMessageId() { return this.instant_message_id; }
    /** @see #instant_message_id */ @JsonProperty("instant_message_id")  public void setInstantMessageId(String instant_message_id) { this.instant_message_id = instant_message_id; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }


    public static final Boolean isNonStewardUser(Object obj) { return (obj.getClass() == NonStewardUser.class); }

}
