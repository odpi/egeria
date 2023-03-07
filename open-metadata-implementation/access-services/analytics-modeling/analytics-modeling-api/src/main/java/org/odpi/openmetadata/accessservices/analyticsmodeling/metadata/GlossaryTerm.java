/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.metadata;

/**
 * Glossary term contains the properties and guid for a term entity retrieved from the metadata repository.
 */
public class GlossaryTerm extends MetadataBase {

	String name;
	String description;
	String summary;

	/**
	 * Get name of the term.
	 * @return name of the term.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set name of the term bean.
	 * @param name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get description of the term.
	 * @return description of the term.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set description of the term.
	 * @param description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Get summary of the term.
	 * @return summary of the term.
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * Set summary of the term bean.
	 * @param summary to set.
	 */

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
