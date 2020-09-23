/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useContext, useEffect }       from "react";

import PropTypes                              from "prop-types";

import { InteractionContext }                 from "./contexts/InteractionContext";

import ReactMarkdown                          from 'react-markdown';

import TopologyDiagramImage                   from './TopologyDiagram.png';
import DinoInterfaceImage                     from './DinoInterface.png';

import "./dino.scss";

const imageRefs = {
    image1 : DinoInterfaceImage,
    image2 : TopologyDiagramImage
};


export default function ReadmeHandler(props) {

  const interactionContext    = useContext(InteractionContext);

 
  const readme              = props.readme;

  const cancelCallback = () => {
    interactionContext.hidePortal();
    props.onCancel();
  }

  const submitCallback = () => {
    interactionContext.hidePortal();
    props.onSubmit();
  }

  const tranformImageURI = (uri) => {
    return imageRefs[uri];
  }

  const triggerPortal = () => {

    let dialogDisplay;

    if (!readme.markdown) {

      /* 
       * There is nothing to display... 
       */
     
      dialogDisplay = (
      
      <div className="dialog-text">
            
        <p>
        There is no readme information to display.
        </p>  
  
        <div className="dismiss-button-container">                     
          <button className="multiselect-button" onClick={cancelCallback}>  Cancel  </button>
          <button className="multiselect-button" onClick={submitCallback}>  OK     </button>
        </div>
              
      </div>
        
      );
    }
    
    else {

      /* 
       * There is readme information to display... 
       */

      let resultsDisplay = (         

        <div>
          <ReactMarkdown source={readme.markdown} transformImageUri={tranformImageURI}/>
        </div>
      );


      dialogDisplay = (
      
        <div className="dialog-text">
          
          <p  className="dialog-text">
          README for Egeria Dino user interface:
          </p>    
   
          <hr></hr>
               
          <div className="history-results-area">                        
            {resultsDisplay}
          </div>

          <div className="dismiss-button-container">                     
            <button className="multiselect-button" onClick={cancelCallback}>  Cancel  </button>
            <button className="multiselect-button" onClick={submitCallback}>  OK     </button>
          </div>
            
        </div>
      
      );
    }

    interactionContext.showPortal(dialogDisplay);
  };


  /*
   * Emulate componentDidMount - to append the wrapper element
   */
  const componentDidMount = () => {    
    if (props.status === "complete") {      
      triggerPortal();
    }   
  };
  useEffect (componentDidMount ,[ props.status, props.history ]);
  
  /*
   * Render nothing - this component is invisible but controls what is displayed by the portal
   */
  return null;

}

ReadmeHandler.propTypes = {  
  status               : PropTypes.string,
  onCancel             : PropTypes.func.isRequired, 
  onSubmit             : PropTypes.func.isRequired, 
  readme               : PropTypes.object
   
};
