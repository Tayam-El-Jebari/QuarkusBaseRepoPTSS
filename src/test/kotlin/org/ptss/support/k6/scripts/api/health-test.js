import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { errorRate } from '../../metrics.js';

export default function (data) {
    // 3. VU code
    group('Health Check Scenarios', () => {
        group('Readiness Check', () => {
            try {
                // Make a GET request to the API
                const res = http.get(`${data.apiUrl}/q/health/ready`, {
                    headers: data.headers
                });

                // Check if the response and body structure is as expected
                const checkRes = check(res, {
                    'status is 200': (r) => r.status === 200,
                    'response is JSON': (r) => r.headers['Content-Type'].includes('application/json'),
                    'status is UP': (r) => JSON.parse(r.body).status === 'UP',
                    'checks array exists': (r) => Array.isArray(JSON.parse(r.body).checks)
                });

                if (!checkRes) {
                    errorRate.add(1);
                }
            } catch (error) {
                console.error(`Request failed: ${error.message}`);
                errorRate.add(1);
            }
        });
    });

    sleep(1);
}