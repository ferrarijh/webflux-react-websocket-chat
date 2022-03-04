let hostname = process.env.REACT_APP_SERVER_HOST || "localhost";
let port = process.env.REACT_APP_SERVER_PORT || 8080;

const Resources = Object.freeze({
    HOSTNAME: "localhost",
    PORT: port
});

export default Resources;