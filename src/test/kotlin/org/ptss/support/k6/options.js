import { errorRate } from './metrics.js';

export const options = {
    stages: [
        { duration: '30s', target: 2 }, // 2 VUs for 30 seconds (vus = virtual users)
        { duration: '1m', target: 4 }, // 4 VUs for 1 minute
        { duration: '30s', target: 0 }, // Ramp down to 0 VUs
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],
        'errors': ['rate<0.01'],
    }
};