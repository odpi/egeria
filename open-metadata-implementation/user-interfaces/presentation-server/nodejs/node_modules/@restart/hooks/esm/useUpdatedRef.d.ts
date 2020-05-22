/// <reference types="react" />
/**
 * Returns a ref that is immediately updated with the new value
 *
 * @param value The Ref value
 */
export default function useUpdatedRef<T>(value: T): import("react").MutableRefObject<T>;
