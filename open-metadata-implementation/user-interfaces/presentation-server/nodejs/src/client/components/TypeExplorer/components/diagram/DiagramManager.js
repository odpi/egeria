/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext, useState }    from "react";

import { TypesContext }                   from "../../contexts/TypesContext";

//import { FocusContext }                   from "../../contexts/FocusContext";

import EntityInheritanceDiagram           from "./EntityInheritanceDiagram";

import PropTypes                          from "prop-types";




/*
 * The TypeExplorer diagram manager offers a choice of two diagrams:
 *  - entity inheritance
 *  - neighborhood
 */

export default function DiagramManager(props) {

 
  const typesContext = useContext(TypesContext);

  //const focusContext = useContext(FocusContext);

  const [selectedDiagram, setSelectedDiagram] = useState("Entity Inheritance");
  /*
   * Although there is a default diagram it should not be set until after the types are loaded.
   * It is therefore set explicitly on conditional rendering rather than used as the initial state
   * of selectedDiagram.
   */
  //const defaultDiagram = "Entity Inheritance";


  //const typesLoaded = typesContext.getEntityTypes() !== null;

  const diagramChangeHandler = (evt) => {
    const diagramName = evt.target.value;
    console.log("DiagramManager: diagram selected - "+diagramName);
    setSelectedDiagram(diagramName);
  }


  const diagramTypes = [ "Entity Inheritance" , "Neighborhood" ];


  const diagram = () => {
    let diagram;

    if (typesContext.getEntityTypes() === null) {
        diagram = <div>Waiting for type information to be loaded...</div>
    }
    else {
      switch (selectedDiagram) {
        case "Entity Inheritance":
          //diagram = <div>EntityInheritance Diagram will be drawn here</div>
          diagram = <EntityInheritanceDiagram />
          break;
        case "Neighborhood":
          diagram = <div>Neighborhood Diagram will be drawn here</div>
          break;
      }
    }

    return diagram;
  }

  const renderDiagramSelector = () => {

    if (typesContext.getEntityTypes() === null) {

      return null;

    }
    else {

      return (
      <div>
      <label htmlFor="diagramSelector">Diagram Type:  </label>
      <select className="diagramSelector" 
              id="diagramSelector" 
              name="diagramSelector" 
              defaultValue={selectedDiagram}
              onChange={diagramChangeHandler}>
          {diagramTypes.map(diagramName => (
              <option key={diagramName} value={diagramName}> {diagramName} </option>
          ))}

      </select>
      </div>
      );
    }
  }


  return (
    <div>
      <h2>Diagram Manager</h2>

      {renderDiagramSelector()}
      <br/>
      {diagram()}

    </div>     
  );
  
}


DiagramManager.propTypes = {
  children: PropTypes.node
}