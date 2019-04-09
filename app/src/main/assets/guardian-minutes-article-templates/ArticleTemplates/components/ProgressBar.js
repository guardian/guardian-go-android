import React, { useState, useEffect } from 'react';
import { getScrollPercentage } from '../js/utils';
import './ProgressBar.css'

export default function ProgressBar() {
  const [percentage, setPercentage] = useState(0);
  let ticking = false;

  useEffect(() => {
    const scrollListener = document.addEventListener('scroll', () => {
        
        if (!ticking) {
          window.requestAnimationFrame(function() {
            setPercentage(getScrollPercentage());
            ticking = false;
          });
      
          ticking = true;
        }
    });

    return () => {
        document.removeEventListener('scroll', scrollListener);
        // local storage article position
    }
  }, [percentage]);

  return (
    <progress value={percentage} max="100"></progress>
  );
}