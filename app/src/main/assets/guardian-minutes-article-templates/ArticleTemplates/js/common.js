import React from 'react';
import ReactDOM from 'react-dom';
import ProgressBar from '../components/ProgressBar';

ReactDOM.render(
    <ProgressBar />,
    document.getElementById('progress')
);

const summary = document.getElementById('summary');
const article = document.querySelector('.article');

summary.addEventListener('click', () => {
    article.classList.remove('hidden');
    summary.parentNode.removeChild(summary);
});