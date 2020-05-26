/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useEffect, useState }     from "react";

import ReactDOM                           from "react-dom";

import PropTypes                          from "prop-types";


import "./portal.scss";



export default function Portal(props) {

  
  //console.log("Portal: props "+props);


  /*
   * THE REASON NONE OF THE ATTEMPTS BELOW WORKED WAS THAT YOU HAVE TO WAIT FOR THE PARENT
   * TO BE RENDERED - MOVING ANY OF THESE INTO COMPONENTDIDMOUNT SHOULD WORK - I USED THE 
   * CALLBACK METHOD BUT OTHERS MAY ALSO BE OK. I QUITE LIKE THE CALLBACK ONE AS IT MAKES 
   * THE PORTAL ENCAPSULATED AND ALLOWS THE CALLER TO DICTATE THE ANCHOR POINT. DIRECT PROP
   * CONTAINING ANCHOR WOULD ALSO BE OK FOR THAT. 
   */
  //const portalRoot = document.getElementById("portal"); WORKS - BUT ALWAYS ROOTED IN INDEX.HTML
  //const portalRoot = document.getElementById("rex-portal"); DOES NOT WORK
  //const portalRoot = props.anchor.current; ALWAYS NULL
  // FORWARD REFS LOOK UGLY AND AM NOT SURE THEY DO WHAT I INTEND
  // const portalRoot = props.anchorCB().current; NOPE - CURRENT IS NULL


  const [newElement, setNewElement] = useState(null);
  

  // Emulate componentDidMount - to append the wrapper element
  const componentDidMount = () => {
    const portalRoot = props.anchorCB().current; 
    console.log("Portal: did mount - newElement is "+newElement);
    
    let locElement = document.createElement("div");
    //locElement.className = "portal-div";    
    portalRoot.appendChild(locElement);
    setNewElement(locElement); 

    return () => {
      // componentWillUnmount... to remove the wrapper element
      console.log("Portal: will unmount - newElement is "+newElement+" locElement is "+locElement);
      portalRoot.removeChild(locElement);
      setNewElement(null); 
    }
  };
  useEffect (componentDidMount ,[]);

  let content = (
    <div className="portal-backdrop">
      <div className="portal-div">
        {props.children}      
      
      </div>
    </div>
  );

  // Render the Portal's children in the wrapper element...  
  if (props.show) {
    if (newElement) {
      return (
        ReactDOM.createPortal(content, newElement)
      );
    }
  }
  return null;


}

Portal.propTypes = {
  children: PropTypes.node,
  show : PropTypes.bool,
  anchorCB: PropTypes.func.isRequired, 
  //cancelCallback : PropTypes.func.isRequired, 
  //submitCallback : PropTypes.func.isRequired
};
