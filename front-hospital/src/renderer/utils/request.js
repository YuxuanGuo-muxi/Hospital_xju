import axios from 'axios';

export const BASE_URL = process.env.NODE_ENV == 'development' ? '': 'http://localhost:8081';

export default axios.create({
    baseURL: BASE_URL
})