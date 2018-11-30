/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'xml_schema_library' asset type in IGC, displayed as 'XML Schema Library' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class XmlSchemaLibrary extends MainObject {

    public static final String IGC_TYPE_ID = "xml_schema_library";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'xml_schema_definitions' property, displayed as 'XML Schema Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link XmlSchemaDefinition} objects.
     */
    protected ReferenceList xml_schema_definitions;

    /**
     * The 'id' property, displayed as 'ID' in the IGC UI.
     */
    protected String id;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #xml_schema_definitions */ @JsonProperty("xml_schema_definitions")  public ReferenceList getXmlSchemaDefinitions() { return this.xml_schema_definitions; }
    /** @see #xml_schema_definitions */ @JsonProperty("xml_schema_definitions")  public void setXmlSchemaDefinitions(ReferenceList xml_schema_definitions) { this.xml_schema_definitions = xml_schema_definitions; }

    /** @see #id */ @JsonProperty("id")  public String getTheId() { return this.id; }
    /** @see #id */ @JsonProperty("id")  public void setTheId(String id) { this.id = id; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isXmlSchemaLibrary(Object obj) { return (obj.getClass() == XmlSchemaLibrary.class); }

}
