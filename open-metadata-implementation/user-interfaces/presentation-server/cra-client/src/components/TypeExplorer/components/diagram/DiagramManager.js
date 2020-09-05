/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext, useState }    from "react";

import { TypesContext }                   from "../../contexts/TypesContext";

import EntityInheritanceDiagram           from "./EntityInheritanceDiagram";

import EntityNeighborhoodDiagram          from "./EntityNeighborhoodDiagram";

import PropTypes                          from "prop-types";

import "./diagram.scss";



/*
 * The TypeExplorer diagram manager offers a choice of two diagrams:
 *  - entity inheritance
 *  - neighborhood
 */

export default function DiagramManager(props) {

  let height = props.height;
  let width = props.width;
 
  const typesContext = useContext(TypesContext);


  const [selectedDiagram, setSelectedDiagram] = useState("Entity Inheritance");

  /*
   * Although there is a default diagram it should not be set until after the types are loaded.
   * It is therefore set explicitly on conditional rendering rather than used as initial state.
   */

  const diagramChangeHandler = (evt) => {
    const diagramName = evt.target.value;
    setSelectedDiagram(diagramName);
  }


  const diagramTypes = [ "Entity Inheritance" , "Neighborhood" ];


  const diagram = () => {
    let diagram;

    if (typesContext.getEntityTypes() === null) {
        diagram = <p>Diagrams appear here once server details are set...</p>
    }
    else {
      switch (selectedDiagram) {
        case "Entity Inheritance":
          //diagram = <div>EntityInheritance Diagram will be drawn here</div>
          diagram = <EntityInheritanceDiagram outerHeight={height} outerWidth={width}/>
          break;
        case "Neighborhood":
          diagram = <EntityNeighborhoodDiagram outerHeight={height} outerWidth={width}/>
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
    <div className="diagram-manager">
      {renderDiagramSelector()}
      <br/>
      {diagram()}
    </div>     
  );
}


DiagramManager.propTypes = {
  children: PropTypes.node,
  height   : PropTypes.number,
  width   : PropTypes.number
}