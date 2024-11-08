import httpClient from "../http-common.js";

const register = data =>{
    return httpClient.post('/api/users/register',data);
}

const get = (rut) => {
    return httpClient.get('/api/users/getByRut',{
        params: {rut}
    })
}

const getRut = (userId) => {
    return httpClient.get(`/api/users/${userId}/getRut`);
}

export default{register, get, getRut};