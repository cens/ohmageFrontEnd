package edu.ucla.cens.AndWellnessVisualizations.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.RootPanel;

import edu.ucla.cens.AndWellnessVisualizations.client.rpcservice.DataFilterService;

/**
 * For now the main entry point into the GWT code.  Creates a new service
 * and event bus, and passes them into a new AppController.
 * 
 * @author jhicks
 *
 */
public class DataFilter implements EntryPoint {
    // Static (global) so it can be easily accessed by javascript code
    // TODO: Remove when the javascript code disappears
    public static EventBus eventBus;
    
    public void onModuleLoad() {
        DataFilterService rpcService = new DataFilterService();
        eventBus = new SimpleEventBus();
        AppController appViewer = new AppController(rpcService, eventBus);
        appViewer.go(RootPanel.get("gwt_test"));
    }
}
