/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import Launch32 from "../../images/Launch_32";

const InfoSection = props => (
  <div>
    <h3 className="info-section__heading">{props.heading}</h3>
    <section className={`bx--row ${props.className} info-section`}>
      {/* <section className={`bx--row ${props.className} info-section bx--col-md-8 bx--col-lg-4 bx--col-xlg-3`}> */}
      {props.children}
    </section>
  </div>
);

const InfoCard = props => {
  return (
    <article className="info-card bx--col-md-4 bx--col-lg-4 bx--col-xlg-3 bx--offset-xlg-1">
      <h4 className="info-card__heading">{props.heading}</h4>
      <p className="info-card__body">{props.body}</p>
      <div className="info-card__footer">
        {props.icon}
        <Launch32 link={props.link} external='true' />
      </div>
    </article>
  );
};
const LocalInfoCard = props => {
  return (
    <article className="info-card bx--col-md-4 bx--col-lg-4 bx--col-xlg-3 bx--offset-xlg-1">
      <h4 className="info-card__heading">{props.heading}</h4>
      <p className="info-card__body">{props.body}</p>
      <div className="info-card__footer">
        {props.icon}
       <Launch32 link={props.link} />
    
      </div>
    </article>
  );
};
export { InfoSection, InfoCard, LocalInfoCard };
