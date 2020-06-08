/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React               from "react";

import PropTypes           from "prop-types";

import TypeDisplay         from "./TypeDisplay"

/**
*
* DetailsPanel implements a web component for presentation of details of the focus or view type
*
* It should present to the user a summary of the key characteristics of the focus or view type. This includes:
*
*  For entity types:
*    * type name
*    * description
*    * attributes (listing names of types)
*    * relationships (listing attribute names and types and providing a link to the relationship details)
*    * classifications (listing attribute names and types and providing a link to the classification details)
*
*  For relationship types:
*    * type name
*    * description
*    * attributes (listing names of types)
*    * ends (listing entity types and providing a link to the entity details)
*
*  For classification types:
*
*    * type name
*    * description
*    * attributes (listing names of types)
*    * valid entity types (listing entity types and providing a link to the entity details)
*
* The details panel reacts to events that indicate a change of focus (an entity type's details should be shown)
* or a change of view (a relationship or classification details should be shown).
* Because the details panel also includes links to entity, relationship and classification types (as outlined above)
* the details panel also generates events requesting changeFocus or changeView.
*
* When the UI is first loaded there will be no type information, and until a focus or view type is selected there is
* no particular type to display - therefore following initial load and until a focus/view type is selected, the details
* panel will be blank.
*
*/
export default function DetailsPanel() {

  return (
    
    <div className="details-panel">       
       <h2> Details Panel </h2>             
        <TypeDisplay />        
    </div>
  
  );
}

DetailsPanel.propTypes = {  
  //className  : PropTypes.string
}

