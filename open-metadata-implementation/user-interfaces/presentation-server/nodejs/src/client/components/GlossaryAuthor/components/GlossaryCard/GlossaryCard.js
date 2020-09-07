/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import React from "react";
import Launch32 from "../../../../images/Launch_32";

const GlossaryCardSection = (props) => (
  <div>
    <h3 className=".glossary-card-section__heading">{props.heading}</h3>
    <section className={`bx--row ${props.className} glossary-card-section`}>
      {/* <section className={`bx--row ${props.className} glossary-card-section bx--col-md-8 bx--col-lg-4 bx--col-xlg-3`}> */}
      {props.children}
    </section>
  </div>
);

const GlossaryCard = (props) => {
  return (
    <article className="glossary-card bx--col-md-4 bx--col-lg-4 bx--col-xlg-3 bx--offset-xlg-1">
      <h4 className="glossary-card__heading">{props.heading}</h4>
      <p className="glossary-card__body">{props.body}</p>
      <div className="glossary-card__footer">
        {props.icon}
        <Launch32 link={props.link} external="true" />
      </div>
    </article>
  );
};
const EmptyGlossaryCard = (props) => {
  return (
    <article className="empty-glossary-card bx--col-md-4 bx--col-lg-4 bx--col-xlg-3 bx--offset-xlg-1"></article>
  );
};
const LocalGlossaryCard = (props) => {
  return (
    <article className="glossary-card bx--col-md-4 bx--col-lg-4 bx--col-xlg-3 bx--offset-xlg-1">
      <h4 className="glossary-card__heading">{props.heading}</h4>
      <p className="glossary-card__body">{props.body}</p>
      <div>
          {/* <a is not correct as it kills the session - TODO sort this out properly with the router and breadcrumbs */}
        <a href={props.link}>{props.icon}</a>
      </div>
    </article>
  );
};
export { GlossaryCardSection, GlossaryCard, LocalGlossaryCard };
