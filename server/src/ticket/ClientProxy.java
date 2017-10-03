package ticket;

import java.util.Map;

public class ClientProxy implements IClient {
    private Map response = null;

    public void setClientModel(Map model) {
        this.response = model;
    }

    public void showError(E.BaseException e) {
        this.response = Server.error(e.getCode(), e.getMessage());
    }

    public Map getResponse() {
        if (response == null) {
            throw new RuntimeException(
                    "called getResponse before setClientModel or showError");
        }
        return response;
    }
}
        
