import axios from "axios";
import jwtHeader from './jwtService';

const SERVICE_URL = "http://localhost:8080/parecer";

class ParecerService {
    getPareceres() {
        return axios.get(`${SERVICE_URL}/get`, { headers: jwtHeader() });
    }

    getParecer(codigoParecer) {
        return axios.get(`${SERVICE_URL}/get/${codigoParecer}`, { headers: jwtHeader() });
    }

    deleteParecer(codigoParecer) {
        return axios.delete(`${SERVICE_URL}/delete/${codigoParecer}`, { headers: jwtHeader() });
    } 

    createParecer(parecer) {
        return axios.post(`${SERVICE_URL}/create`, parecer, { headers: jwtHeader() });
    }

    updateParecer(codigo, parecer) {
        return axios.put(`${SERVICE_URL}/update/${codigo}`, parecer, { headers: jwtHeader() });
    }
}

export default new ParecerService();