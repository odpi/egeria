import { useRef } from 'react';
import useStableMemo from './useStableMemo';
/**
 * An _immediate_ effect that runs an effect callback when its dependency array
 * changes. This is helpful for updates should must run during render, most
 * commonly state derived from props; a more ergonomic version of https://reactjs.org/docs/hooks-faq.html#how-do-i-implement-getderivedstatefromprops
 *
 * ```ts
 * function Example({ value }) {
 *   const [intermediaryValue, setValue] = useState(value);
 *
 *   useImmediateUpdateEffect(() => {
 *     setValue(value)
 *   }, [value])
 * ```
 */

function useImmediateUpdateEffect(effect, deps) {
  var firstRef = useRef(true);
  useStableMemo(function () {
    if (firstRef.current) {
      firstRef.current = false;
      return;
    }

    effect();
  }, deps);
}

export default useImmediateUpdateEffect;