import axios from 'axios';

const SERVICE_URL = 'http://localhost:8080/authenticate';

class AuthenticationService {
    async login(username, password) {
        const response = await axios.post(SERVICE_URL, { username, password });
        if (response.data.jwtToken) {
            localStorage.setItem('user', JSON.stringify(response.data));
        }
        return response.data;
    }

    logout() {
        localStorage.removeItem('user');
    }

    getLoggedInUser() {
        return JSON.parse(localStorage.getItem('user'));;
    }

    isAuthenticated() {
        return localStorage.getItem('user') !== null;
    }

}

export default new AuthenticationService();