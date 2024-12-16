import { check, group, sleep } from 'k6';
import { options } from '../options.js';
import healthTest from "../scripts/api/health-test.js";
import { API_URL, HEADERS } from '../config.js';

export { options };

export function setup() {
    // Setup code
    return { apiUrl: API_URL, headers: HEADERS };
}

export default function (data) {
    healthTest(data);
}

// Sleep for 1 second between scenarios, to avoid overwhelming the server
sleep(1);

// Add more scenarios here


export function teardown(data) {
    // Teardown code
}