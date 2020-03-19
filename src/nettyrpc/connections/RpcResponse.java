package nettyrpc.connections;

public class RpcResponse {
    private String requestId;
    private String errorMessage;
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
