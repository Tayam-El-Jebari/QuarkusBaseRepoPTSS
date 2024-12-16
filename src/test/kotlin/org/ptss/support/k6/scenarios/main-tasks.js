import { exec } from 'k6/execution';
import { check, group, sleep } from 'k6';
import { options } from '../../options.js';

export { options };

export default function () {
    exec('./scripts/api/health-test.js');
}

// Sleep for 1 second between scenarios
sleep(1);

// Add more scenarios here
