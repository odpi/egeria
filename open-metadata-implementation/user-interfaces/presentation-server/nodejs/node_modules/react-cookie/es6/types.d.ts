/// <reference types="react" />
import Cookies, { Cookie } from 'universal-cookie';
export declare type ReactCookieProps = {
    cookies?: Cookies;
    allCookies?: {
        [name: string]: Cookie;
    };
    children?: any;
    ref?: React.RefObject<{}>;
};
