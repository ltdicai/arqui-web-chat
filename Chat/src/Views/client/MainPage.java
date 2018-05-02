package Views.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;

public class MainPage implements EntryPoint {
    public Button LoginUser = new Button();
    public void onModuleLoad() {
        RootPanel.get("slot1").add(LoginUser);
    }
}
