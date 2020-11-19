/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React                          from "react";

import PropTypes                      from "prop-types";

import InstanceStatusDisplay          from "./InstanceStatusDisplay";

import InstancePropertiesDisplay      from "./InstancePropertiesDisplay";

import InstanceClassificationsDisplay from "./InstanceClassificationsDisplay";

import "./details-panel.scss";


export default function EntityDisplay(props) {


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



  const expEntity    = props.expEntity;

  const entity       = expEntity.entityDetail;
  const entityDigest = expEntity.entityDigest;

  const label        = entityDigest.label;
  const gen          = entityDigest.gen;
  const provenance   = entityDigest.provenance;

  const genEnterprise    = props.enterprise;  // The enterprise option of the gen of original retrieval

  /*
   * If 'provenance' (from the digest) shows that this entity was retrievd using enterprise scope,
   * then even if the genEnterprise is false, we should display the entity as having been retrieved
   * by an enterprise scope operation. This is most probably because the entity was originally
   * retrieved by a (local) retrieval of a relationship, which had this entity as a proxy. Rex has
   * gone after the full EntityDetail so that the user can also see the non-unique properties.
   * It may have done this using an enterprise operation so treat the displayed entity as an
   * enterprise scoped one.
   */
  const enterprise = genEnterprise || (provenance === "ent");

  const serverName   = expEntity.serverName;
  const platformName = expEntity.platformName;

  return (
    <div className="instance-details-container">
      <div className="instance-details-item">Entity : {label}</div>
      <div className="instance-details-item">Type : {entity.type.typeDefName}</div>
      <div className="instance-details-item">Version : {entity.version}</div>
      <div className="instance-details-item">Status : <InstanceStatusDisplay inst={entity} /></div>
      <div className="instance-details-item">Properties : { !entity.properties ? "empty" :
        <InstancePropertiesDisplay properties={entity.properties} />}</div>
      <div className="instance-details-item">Classifications : { !entity.classifications ? "empty" :
        <InstanceClassificationsDisplay classifications={entity.classifications} />}</div>
      <div className="instance-details-item">GUID : {entity.guid}</div>
      <div className="instance-details-item">Home Repository :
        <ul className="details-sublist">
          <li className="details-sublist-item">metadataCollectionName : {entity.metadataCollectionName}</li>
          <li className="details-sublist-item">metadataCollectionId : {entity.metadataCollectionId}</li>
        </ul>
      </div>

      <button className="collapsible-non-bold" id="querySummary" onClick={flipSection}> Rex Retrieval : </button>
      <div className="content">
        <div className="instance-details-item">Added in gen : {gen}</div>
        <div className="instance-details-item">Retrieved from server : {serverName} on platform : {platformName}</div>
        {enterprise &&
          <div className="instance-details-item">Operation was at enterprise scope</div>
        }
        {!enterprise && (provenance !== "proxy") &&
          <div className="instance-details-item">Entity is a {provenance} instance</div>
        }
        {!enterprise && (provenance === "proxy") &&
          <div className="instance-details-item">Entity {provenance} from local operation, enhanced by enterprise retrieval</div>
        }
      </div>


      <button className="collapsible-non-bold" id="controlProps" onClick={flipSection}> OMRS Control Properties : </button>
      <div className="content">
        <ul className="details-sublist">
          <li className="details-sublist-item">createdBy : {entity.createdBy}</li>
          <li className="details-sublist-item">createTime : {entity.createTime}</li>
          <li className="details-sublist-item">updatedBy : {entity.updatedBy ? entity.updatedBy : "empty"}</li>
          <li className="details-sublist-item">updateTime : {entity.updateTime ? entity.updateTime : "empty"}</li>
          <li className="details-sublist-item">maintainedBy : { !entity.maintainedBy ? "empty" :
            <ul className="details-sublist">
              {entity.maintainedBy.sort().map( (mtr) => <li className="details-sublist-item" key={mtr}> {mtr}</li> )}
            </ul>
            }
          </li>
          <li className="details-sublist-item">instanceLicense : {entity.instanceLicense ? entity.instanceLicense : "empty"}</li>
          <li className="details-sublist-item">instanceProvenanceType : {entity.instanceProvenanceType ? entity.instanceProvenanceType : "empty"}</li>
          <li className="details-sublist-item">replicatedBy : {entity.replicatedBy ? entity.replicatedBy : "empty"}</li>
        </ul>
      </div>
    </div>
  );
}

EntityDisplay.propTypes = {
  expEntity: PropTypes.object,
  enterprise: PropTypes.bool
};
  
