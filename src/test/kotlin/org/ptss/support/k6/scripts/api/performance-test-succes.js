import http from 'k6/http';
import { check, group, sleep } from 'k6';
import { options } from '../../options.js';
import { errorRate } from '../../metrics.js';
import { API_URL, HEADERS } from '../../config.js';

export { options };

// 1. init code

export function setup() {
    // 2. setup code
    return { apiUrl: API_URL, headers: HEADERS };
}

export default function (data) {
    // 3. VU code
    group('Base Test Scenarios', () => {
        group('Test Success Endpoint', () => {
            try {
                // Make a GET request to the API
                const res = http.get(data.apiUrl, { headers: data.headers });

                // Check if the response is as expected
                const checkRes = check(res, {
                    'status is 200': (r) => r.status === 200,
                    'body contains Success': (r) => r.body.includes('Success')
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

export function teardown(data) {
    // 4. teardown code
}