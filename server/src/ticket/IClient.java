package ticket;

import java.util.Map;

public interface IClient {
    void setClientModel(Map model);
    void showError(E.BaseException e);
}
