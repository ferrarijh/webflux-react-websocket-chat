const LoadingStatus = Object.freeze({
    IDLE: "IDLE",
    LOADING: "LOADING",
    HTTP_409: "CONFLICT",
    HTTP_500: "INTERNAL_SERVER_ERROR",
    DISCONNECTED: "DISCONNECTED"
});

export {LoadingStatus};