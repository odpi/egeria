/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

import java.util.Objects;


/**
 * AssetUniverse extends AssetDetail which extend AssetSummary.  AssetUniverse adds information about the
 * common open metadata entities related to this asset.
 * <ul>
 *     <li>Meanings - glossary term(s) assigned to this asset.</li>
 *     <li>Schema - details of the schema associated with the asset.</li>
 *     <li>Analysis - details of the annotations added by the discovery services.</li>
 *     <li>Feedback - details of the people, products and feedback that are connected to the asset.</li>
 *     <li>Locations - details of the known locations of the asset.</li>
 *     <li>Lineage - details of the lineage for the asset.</li>
 *     <li>Related Assets - details of the assets lined to this asset.</li>
 * </ul>
 *
 */
public class AssetUniverse extends AssetDetail
{
    protected AssetMeanings      meanings       = null;
    protected AssetSchemaType    schema         = null;
    protected AssetAnnotations   analysis       = null;
    protected AssetFeedback      feedback       = null;
    protected AssetLocations     knownLocations = null;
    protected AssetLineage       lineage        = null;
    protected RelatedAssets      relatedAssets  = null;


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
     * @param schema Schema object to query schema and related glossary definitions.
     * @param analysis Annotations from metadata discovery.
     * @param feedback Feedback object to query the feedback.
     * @param knownLocations Locations list
     * @param lineage lineage object to query the origin of the asset.
     * @param relatedAssets RelatedAssets list
     */
    public AssetUniverse(Asset                       assetBean,
                         AssetExternalIdentifiers    externalIdentifiers,
                         AssetRelatedMediaReferences relatedMediaReferences,
                         AssetNoteLogs               noteLogs,
                         AssetExternalReferences     externalReferences,
                         AssetConnections            connections,
                         AssetLicenses               licenses,
                         AssetCertifications         certifications,
                         AssetMeanings               meanings,
                         AssetSchemaType             schema,
                         AssetAnnotations            analysis,
                         AssetFeedback               feedback,
                         AssetLocations              knownLocations,
                         AssetLineage                lineage,
                         RelatedAssets               relatedAssets)
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
        this.analysis = analysis;
        this.feedback = feedback;
        this.knownLocations = knownLocations;
        this.lineage = lineage;
        this.relatedAssets = relatedAssets;
    }


    /**
     * Copy/clone Constructor note this is a deep copy
     *
     * @param templateAssetUniverse template to copy
     */
    public AssetUniverse(AssetUniverse templateAssetUniverse)
    {
        super(templateAssetUniverse);

        if (templateAssetUniverse != null)
        {
            /*
             * Create the top-level property objects for this new asset using the values from the template.
             * The get methods create clones of the returned objects so no need to duplicate objects here.
             */
            AssetMeanings    templateMeanings      = templateAssetUniverse.getMeanings();
            AssetSchemaType  templateSchema        = templateAssetUniverse.getSchema();
            AssetAnnotations templateAnalysis      = templateAssetUniverse.getAnalysis();
            AssetFeedback    templateFeedback      = templateAssetUniverse.getFeedback();
            AssetLocations   templateLocations     = templateAssetUniverse.getKnownLocations();
            AssetLineage     templateLineage       = templateAssetUniverse.getLineage();
            RelatedAssets    templateRelatedAssets = templateAssetUniverse.getRelatedAssets();

            if (templateMeanings != null)
            {
                meanings = templateMeanings.cloneIterator(this);
            }
            if (templateSchema != null)
            {
                templateSchema.cloneAssetSchemaElement(this);
            }
            if (templateAnalysis != null)
            {
                analysis = templateAnalysis.cloneIterator(this);
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
    public AssetMeanings getMeanings()
    {
        if (meanings == null)
        {
            return null;
        }
        else
        {
            return meanings.cloneIterator(this);
        }
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
            return schema.cloneAssetSchemaElement(this);
        }
    }


    /**
     * Return details of the metadata discovery analysis for the asset.
     *
     * @return Annotations List of annotations from metadata discovery
     */
    public AssetAnnotations getAnalysis()
    {
        if (analysis == null)
        {
            return null;
        }
        else
        {
            return analysis.cloneIterator(this);
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
     * @return RelatedAssets list
     */
    public RelatedAssets getRelatedAssets()
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
                ", analysis=" + analysis +
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
        if (!(objectToCompare instanceof AssetUniverse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetUniverse that = (AssetUniverse) objectToCompare;
        return Objects.equals(getMeanings(), that.getMeanings()) &&
                Objects.equals(getSchema(), that.getSchema()) &&
                Objects.equals(getAnalysis(), that.getAnalysis()) &&
                Objects.equals(getFeedback(), that.getFeedback()) &&
                Objects.equals(getKnownLocations(), that.getKnownLocations()) &&
                Objects.equals(getLineage(), that.getLineage()) &&
                Objects.equals(getRelatedAssets(), that.getRelatedAssets());
    }
}