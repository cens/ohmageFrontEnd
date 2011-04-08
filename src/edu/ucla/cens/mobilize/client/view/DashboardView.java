package edu.ucla.cens.mobilize.client.view;

import java.util.List;
import java.util.Map;

public interface DashboardView {

  interface Presenter {
    void setView(DashboardView view);
  }
  void setPresenter(Presenter p);
  
  // recent activity data setters
  void setNumUnreadSurveyResponses(int num);
  void setNumActiveParticipantCampaigns(int num);
  void setNumActiveAuthorCampaigns(int num);
    
  // quick link setters
  void setPermissions(boolean canEdit, boolean canUpload);
}