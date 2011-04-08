package edu.ucla.cens.mobilize.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import edu.ucla.cens.mobilize.client.model.*;

public interface ResponseView extends IsWidget {
  
  interface Presenter {
    void onFilterChange();
    void onShare(); // response param
    void onUnshare(); // response
    void onDelete(); // response
    void setView(ResponseView view);
  }

  void setPresenter(Presenter presenter);

  // load data in filters
  void setParticipantList(List<String> participantNames);
  void setCampaignList(List<String> campaignNames);
  void setSurveyList(List<String> surveyNames);
  
  // set selected filters
  void selectParticipant(String participant);
  void selectCampaign(String campaign);
  void selectSurvey(String survey);

  // display
  void renderPrivate(List<SurveyResponse> responses);
  void renderPublic(List<SurveyResponse> responses);
  
}