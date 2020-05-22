import * as React from 'react';
import Cookies from 'universal-cookie';
import { ReactCookieProps } from './types';
export default class CookiesProvider extends React.Component<ReactCookieProps, any> {
    cookies: Cookies;
    constructor(props: ReactCookieProps);
    render(): JSX.Element;
}
