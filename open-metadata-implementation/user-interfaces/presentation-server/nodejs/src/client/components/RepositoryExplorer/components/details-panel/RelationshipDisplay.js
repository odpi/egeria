/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                     from "react";

import PropTypes                 from "prop-types";

import InstanceStatusDisplay     from "./InstanceStatusDisplay";

import InstancePropertiesDisplay from "./InstancePropertiesDisplay";

import EntityProxyDisplay        from "./EntityProxyDisplay";

import "./details-panel.scss";



export default function RelationshipDisplay(props) {


  /*
   * Handler for flopping a collapsible
   */
  const flipSection = (evt) => {
    /*
     * Use currentTarget (not target) - because we need to operate relative to the button,
     * which is where the handler is defined, in order for the content section to be the sibling.
     */
    const element = evt.currentTarget;
    element.classList.toggle("active");
    const content = element.nextElementSibling;
    if (content.style.display === "block") {
      content.style.display = "none";
    }
    else {
      content.style.display = "block";
    }
  };


  const expRelationship    = props.expRelationship;
  const enterprise         = props.enterprise;

  const relationship       = expRelationship.relationship;
  const relationshipDigest = expRelationship.relationshipDigest;
  const label              = relationshipDigest.label;
  const gen                = relationshipDigest.gen;

  const provenance         = relationshipDigest.provenance;

  const serverName         = expRelationship.serverName;
  const platformName       = expRelationship.platformName;
    
  return (
    <div className="instance-details-container">
      <div className="instance-details-item">Relationship : {label}</div>
      <div className="instance-details-item">Type : {relationship.type.typeDefName}</div>       
      <div className="instance-details-item">Version : {relationship.version}</div>
      <div className="instance-details-item">Status : <InstanceStatusDisplay inst={relationship} /></div>
      <div className="instance-details-item">Properties : { !relationship.properties ? "empty" :
        <InstancePropertiesDisplay properties={relationship.properties} />}</div>         
      <div className="instance-details-item">GUID : {relationship.guid}</div>
      <div className="instance-details-item">Home Repository : 
        <ul className="details-sublist">
          <li className="details-sublist-item">metadataCollectionName : {relationship.metadataCollectionName}</li>
          <li className="details-sublist-item">metadataCollectionId : {relationship.metadataCollectionId}</li>
        </ul>
      </div>
      <div className="instance-details-item">Relationship Ends :
        <div className="details-sub-container">
          Entity One : <EntityProxyDisplay entityProxy={relationship.entityOneProxy} />
        </div>
        <div className="details-sub-container">
          Entity Two : <EntityProxyDisplay entityProxy={relationship.entityTwoProxy} />
        </div>
      </div>

      <button className="collapsible-non-bold" id="querySummary" onClick={flipSection}> Rex Retrieval : </button>
        <div className="content">
          <div className="instance-details-item">Added in gen : {gen}</div>
          <div className="instance-details-item">Retrieved from server : {serverName} on platform : {platformName}</div>
          {enterprise &&
            <div className="instance-details-item">Operation was at enterprise scope</div>
          }
          {!enterprise &&
            <div className="instance-details-item">Relationship is a {provenance} instance</div>
          }
        </div>

      <button className="collapsible-non-bold" id="controlProps" onClick={flipSection}> OMRS Control Properties : </button>
      <div className="content">
        <ul className="details-sublist">
          <li className="details-sublist-item">createdBy : {relationship.createdBy}</li>
          <li className="details-sublist-item">createTime : {relationship.createTime}</li>
          <li className="details-sublist-item">updatedBy : {relationship.updatedBy ? relationship.updatedBy : "empty"}</li>
          <li className="details-sublist-item">updateTime : {relationship.updateTime ? relationship.updateTime : "empty"}</li>
          <li className="details-sublist-item">maintainedBy  { !relationship.maintainedBy ? "empty" :
            <ul className="details-sublist">
              {relationship.maintainedBy.sort().map( (mtr) => <li className="details-sublist-item" key={mtr}> {mtr}</li> )}      
            </ul>
            }              
          </li>         
          <li className="details-sublist-item">instanceLicense : {relationship.instanceLicense ? relationship.instanceLicense : "empty"}</li>
          <li className="details-sublist-item">instanceProvenanceType : {relationship.instanceProvenanceType ? relationship.instanceProvenanceType : "empty"}</li> 
          <li className="details-sublist-item">replicatedBy : {relationship.replicatedBy ? relationship.replicatedBy : "empty"}</li>
        </ul>
      </div>
    </div>
  );
}

 
 RelationshipDisplay.propTypes = {
  expRelationship: PropTypes.object,
  enterprise: PropTypes.bool
 };
