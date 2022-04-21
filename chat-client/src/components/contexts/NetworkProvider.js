const LoadingStatus = Object.freeze({
    IDLE: "IDLE",
    LOADING: "LOADING",
    HTTP_409: "CONFLICT",
    HTTP_500: "INTERNAL_SERVER_ERROR",
    HTTP_503: "SERVICE_UNAVAILABLE",
    DISCONNECTED: "DISCONNECTED",
    UNKNOWN: "UNKNOWN"
});

export {LoadingStatus};