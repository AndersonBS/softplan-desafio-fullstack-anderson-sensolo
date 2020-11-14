export default function jwtHeader() {
    const user = JSON.parse(localStorage.getItem('user'));
  
    if (user && user.jwtToken) {
        return { Authorization: 'Bearer ' + user.jwtToken };
    } else {
        return {};
    }
}