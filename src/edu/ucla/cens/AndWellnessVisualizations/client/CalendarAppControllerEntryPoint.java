package edu.ucla.cens.AndWellnessVisualizations.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

import edu.ucla.cens.AndWellnessVisualizations.client.common.AuthorizationManager;
import edu.ucla.cens.AndWellnessVisualizations.client.common.TokenLoginManager;
import edu.ucla.cens.AndWellnessVisualizations.client.event.DataPointLabelSelectionEvent;
import edu.ucla.cens.AndWellnessVisualizations.client.rpcservice.AndWellnessRpcService;
import edu.ucla.cens.AndWellnessVisualizations.client.rpcservice.LocalAndWellnessRpcService;

public class CalendarAppControllerEntryPoint implements EntryPoint {

    /**
     * Called when the webpage finished loading.  Create any necessary objects and
     * starts the AppController.
     */
    public void onModuleLoad() {
        // Initialize the rpc service and event bus for the app
        AndWellnessRpcService rpcService = new LocalAndWellnessRpcService();
        EventBus eventBus = new SimpleEventBus();
        TokenLoginManager loginManager = new TokenLoginManager(eventBus);
        
        // Run the URL authorization
        AuthorizationManager.instantiate(eventBus, loginManager);
        
        // Create and run the controller
        CalendarAppController appController = new CalendarAppController(rpcService, eventBus, loginManager);
        appController.go();
        
        // Fake a data point selection event to make things work
        eventBus.fireEvent(new DataPointLabelSelectionEvent("alcoholNumberOfDrinks"));
    }
}
