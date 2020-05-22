import * as React from 'react';
import { ReactCookieProps } from './types';
declare type Diff<T, U> = T extends U ? never : T;
declare type Omit<T, K extends keyof T> = Pick<T, Diff<keyof T, K>>;
export default function withCookies<T extends ReactCookieProps>(WrappedComponent: React.ComponentType<T>): React.ComponentType<Omit<T, keyof ReactCookieProps>>;
export {};
