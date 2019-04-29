/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code non_steward_user} asset type in IGC, displayed as '{@literal User}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("non_steward_user")
public class NonStewardUser extends Reference {

    public static String getIgcTypeDisplayName() { return "User"; }

    /**
     * The {@code full_name} property, displayed as '{@literal Full Name}' in the IGC UI.
     */
    protected String full_name;

    /**
     * The {@code job_title} property, displayed as '{@literal Job Title}' in the IGC UI.
     */
    protected String job_title;

    /**
     * The {@code email_address} property, displayed as '{@literal Email Address}' in the IGC UI.
     */
    protected String email_address;

    /**
     * The {@code office_phone_number} property, displayed as '{@literal Office Phone Number}' in the IGC UI.
     */
    protected String office_phone_number;

    /**
     * The {@code mobile_phone_number} property, displayed as '{@literal Mobile Phone Number}' in the IGC UI.
     */
    protected String mobile_phone_number;

    /**
     * The {@code principal_id} property, displayed as '{@literal User Name}' in the IGC UI.
     */
    protected String principal_id;

    /**
     * The {@code given_name} property, displayed as '{@literal Given Name}' in the IGC UI.
     */
    protected String given_name;

    /**
     * The {@code surname} property, displayed as '{@literal Surname}' in the IGC UI.
     */
    protected String surname;

    /**
     * The {@code courtesy_title} property, displayed as '{@literal Courtesy Title}' in the IGC UI.
     */
    protected String courtesy_title;

    /**
     * The {@code organization} property, displayed as '{@literal Organization}' in the IGC UI.
     */
    protected String organization;

    /**
     * The {@code location} property, displayed as '{@literal Location}' in the IGC UI.
     */
    protected String location;

    /**
     * The {@code business_address} property, displayed as '{@literal Business Address}' in the IGC UI.
     */
    protected String business_address;

    /**
     * The {@code home_phone_number} property, displayed as '{@literal Home Phone Number}' in the IGC UI.
     */
    protected String home_phone_number;

    /**
     * The {@code fax_number} property, displayed as '{@literal Fax Number}' in the IGC UI.
     */
    protected String fax_number;

    /**
     * The {@code pager_number} property, displayed as '{@literal Pager Number}' in the IGC UI.
     */
    protected String pager_number;

    /**
     * The {@code instant_message_id} property, displayed as '{@literal Instant Message ID}' in the IGC UI.
     */
    protected String instant_message_id;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "full_name",
        "job_title",
        "email_address",
        "office_phone_number",
        "mobile_phone_number",
        "principal_id",
        "given_name",
        "surname",
        "courtesy_title",
        "organization",
        "location",
        "business_address",
        "home_phone_number",
        "fax_number",
        "pager_number",
        "instant_message_id",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "full_name",
        "job_title",
        "email_address",
        "office_phone_number",
        "mobile_phone_number",
        "principal_id",
        "given_name",
        "surname",
        "courtesy_title",
        "organization",
        "location",
        "business_address",
        "home_phone_number",
        "fax_number",
        "pager_number",
        "instant_message_id",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "full_name",
        "job_title",
        "email_address",
        "office_phone_number",
        "mobile_phone_number",
        "principal_id",
        "given_name",
        "surname",
        "courtesy_title",
        "organization",
        "location",
        "business_address",
        "home_phone_number",
        "fax_number",
        "pager_number",
        "instant_message_id",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isNonStewardUser(Object obj) { return (obj.getClass() == NonStewardUser.class); }

}
