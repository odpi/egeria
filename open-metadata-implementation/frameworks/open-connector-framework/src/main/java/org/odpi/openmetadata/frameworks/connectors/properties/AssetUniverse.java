/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Meaning;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * AssetUniverse extends AssetDetail which extend AssetSummary.  AssetUniverse adds information about the
 * common open metadata entities related to this asset.
 * <ul>
 *     <li>meanings - glossary term(s) assigned to this asset.</li>
 *     <li>schema - details of the schema type associated with the asset.</li>
 *     <li>feedback - details of the reviews, tags and comments that are connected to the asset.</li>
 *     <li>knownLocations - details of the known locations of the asset.</li>
 *     <li>lineage - details of the lineage for the asset.</li>
 *     <li>relatedAssets - details of the assets lined to this asset.</li>
 * </ul>
 *
 */
public class AssetUniverse extends AssetDetail
{
    private static final long     serialVersionUID = 1L;

    protected List<AssetMeaning> meanings       = null;
    protected AssetSchemaType    schema         = null;
    protected AssetFeedback      feedback       = null;
    protected AssetLocations     knownLocations = null;
    protected AssetLineage       lineage        = null;
    protected AssetRelatedAssets relatedAssets  = null;


    /**
     * Default constructor only for subclasses
     */
    protected AssetUniverse()
    {
        super();
    }


    /**
     * Typical Constructor
     *
     * @param assetBean details of this asset
     * @param externalIdentifiers ExternalIdentifiers  list
     * @param relatedMediaReferences RelatedMediaReferences  list
     * @param noteLogs NoteLogs iterator
     * @param externalReferences ExternalReferences iterator
     * @param connections List of connections attached to the asset
     * @param licenses List of licenses
     * @param certifications Certifications list of certifications
     * @param meanings Meanings list of glossary definitions.
     * @param schema StructSchemaType object to query schema and related glossary definitions.

     * @param feedback Feedback object to query the feedback.
     * @param knownLocations Locations list
     * @param lineage lineage object to query the origin of the asset.
     * @param relatedAssets AssetRelatedAssets list
     */
    public AssetUniverse(Asset                       assetBean,
                         AssetExternalIdentifiers    externalIdentifiers,
                         AssetRelatedMediaReferences relatedMediaReferences,
                         AssetNoteLogs               noteLogs,
                         AssetExternalReferences     externalReferences,
                         AssetConnections            connections,
                         AssetLicenses               licenses,
                         AssetCertifications         certifications,
                         List<AssetMeaning>          meanings,
                         AssetSchemaType             schema,
                         AssetFeedback               feedback,
                         AssetLocations              knownLocations,
                         AssetLineage                lineage,
                         AssetRelatedAssets relatedAssets)
    {
        super(assetBean,
              externalIdentifiers,
              relatedMediaReferences,
              noteLogs,
              externalReferences,
              connections,
              licenses,
              certifications);

        this.meanings = meanings;
        this.schema = schema;
        this.feedback = feedback;
        this.knownLocations = knownLocations;
        this.lineage = lineage;
        this.relatedAssets = relatedAssets;
    }


    /**
     * Copy/clone Constructor note this is a deep copy
     *
     * @param template template to copy
     */
    public AssetUniverse(AssetUniverse template)
    {
        super(template);

        if (template != null)
        {
            meanings = template.getMeanings();
            /*
             * Create the top-level property objects for this new asset using the values from the template.
             * The get methods create clones of the returned objects so no need to duplicate objects here.
             */
            AssetSchemaType    templateSchema        = template.getSchema();
            AssetFeedback      templateFeedback      = template.getFeedback();
            AssetLocations     templateLocations     = template.getKnownLocations();
            AssetLineage       templateLineage       = template.getLineage();
            AssetRelatedAssets templateRelatedAssets = template.getRelatedAssets();

            if (templateSchema != null)
            {
                templateSchema.cloneAssetSchemaType(this);
            }
            if (templateFeedback != null)
            {
                feedback = new AssetFeedback(this, templateFeedback);
            }
            if (templateLocations != null)
            {
                knownLocations = templateLocations.cloneIterator(this);
            }
            if (templateLineage != null)
            {
                lineage = new AssetLineage(this, templateLineage);
            }
            if (templateRelatedAssets != null)
            {
                relatedAssets = templateRelatedAssets.cloneIterator(this);
            }
        }
    }


    /**
     * Return the list of glossary definitions assigned directly to this asset.
     *
     * @return Meanings list of glossary definitions.
     */
    public List<AssetMeaning> getMeanings()
    {
        List<Meaning>       meanings = assetBean.getMeanings();

        if (meanings != null)
        {
            List<AssetMeaning> assetMeanings = new ArrayList<>();

            for (Meaning  meaning : meanings)
            {
                if (meaning != null)
                {
                    assetMeanings.add(new AssetMeaning(this, meaning));
                }
            }

            if (! assetMeanings.isEmpty())
            {
                return assetMeanings;
            }
        }

        return null;
    }


    /**
     * Return details of the schema associated with the asset.
     *
     * @return SchemaElement schema object to query the schema associated with the connected asset.
     */
    public AssetSchemaType getSchema()
    {
        if (schema == null)
        {
            return null;
        }
        else
        {
            return schema.cloneAssetSchemaType(this);
        }
    }


    /**
     * Return details of the people, products and feedback that are connected to the asset.
     *
     * @return Feedback feedback object to query the feedback on the asset.
     */
    public AssetFeedback getFeedback()
    {
        if (feedback == null)
        {
            return null;
        }
        else
        {
            return new AssetFeedback(this, feedback);
        }
    }


    /**
     * Return the list of locations for the asset.
     *
     * @return Locations list of locations.
     */
    public AssetLocations getKnownLocations()
    {
        if (knownLocations == null)
        {
            return null;
        }
        else
        {
            return knownLocations.cloneIterator(this);
        }
    }


    /**
     * Return details of the lineage for the asset.
     *
     * @return Lineage  lineage object that allows queries about the lineage of the asset.
     */
    public AssetLineage getLineage()
    {
        if (lineage == null)
        {
            return null;
        }
        else
        {
            return new AssetLineage(this, lineage);
        }
    }


    /**
     * Return the list of assets related to this asset.
     *
     * @return AssetRelatedAssets list
     */
    public AssetRelatedAssets getRelatedAssets()
    {
        if (relatedAssets == null)
        {
            return null;
        }
        else
        {
            return relatedAssets.cloneIterator(this);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetUniverse{" +
                "assetBean=" + assetBean +
                ", assetMeanings=" + meanings +
                ", schema=" + schema +
                ", feedback=" + feedback +
                ", knownLocations=" + knownLocations +
                ", lineage=" + lineage +
                ", relatedAssets=" + relatedAssets +
                ", externalIdentifiers=" + externalIdentifiers +
                ", relatedMediaReferences=" + relatedMediaReferences +
                ", noteLogs=" + noteLogs +
                ", externalReferences=" + externalReferences +
                ", connections=" + connections +
                ", licenses=" + licenses +
                ", certifications=" + certifications +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetUniverse that = (AssetUniverse) objectToCompare;
        return Objects.equals(meanings, that.meanings) &&
                       Objects.equals(schema, that.schema) &&
                       Objects.equals(feedback, that.feedback) &&
                       Objects.equals(knownLocations, that.knownLocations) &&
                       Objects.equals(lineage, that.lineage) &&
                       Objects.equals(relatedAssets, that.relatedAssets);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), meanings, schema, feedback, knownLocations, lineage, relatedAssets);
    }
}