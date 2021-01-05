/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import Launch32 from "../../../../images/carbon/Launch_32";
import { Accordion, AccordionItem } from "carbon-components-react";

const NodeCardSection = (props) => (
  <div>
    <h3 className=".node-card-section__heading">{props.heading}</h3>
    <section className={`bx--row ${props.className} node-card-section`}>
      {/* <section className={`bx--row ${props.className} node-card-section bx--col-md-8 bx--col-lg-4 bx--col-xlg-3`}> */}
      {props.children}
    </section>
  </div>
);

const NodeCard = (props) => {
  return (
    <article className="node-card bx--col-md-4 bx--col-lg-4 bx--col-xlg-3 bx--offset-xlg-1">
      <h4 className="node-card__heading">{props.heading}</h4>
      <p className="node-card__body">{props.body}</p>
      <div className="node-card__footer">
        {props.icon}
        <Launch32 link={props.link} external="true" />
      </div>
    </article>
  );
};

const LocalNodeCard = (props) => {
  const onChange = (e) => {
    if (e.target.checked) {
      console.log("onchange LocalNodeCard selected " + props.guid);
      props.setSelected(props.guid);
    } else {
      console.log("onchange LocalNodeCard unselected " + props.heading);
      props.setSelected(undefined);
    }
  };
  return (
    <article className="node-card bx--col-md-4 bx--col-lg-4 bx--col-xlg-3 bx--offset-xlg-1">
      <div className="node-card-section__heading">
        <h4>{props.heading}</h4>
        <input type="checkbox" checked={props.isSelected} onChange={onChange} />
      </div>
      <div style={{display:'none'}}>{props.guid}</div> 
      <Accordion>
        <AccordionItem title="Description">{props.body}</AccordionItem>
      </Accordion>
      <div>
         {props.icon}
      </div>
    </article>
  );
};

export { NodeCardSection, NodeCard, LocalNodeCard };
