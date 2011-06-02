package edu.ucla.cens.mobilize.client.ui;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import edu.ucla.cens.mobilize.client.AwConstants;
import edu.ucla.cens.mobilize.client.common.HistoryTokens;
import edu.ucla.cens.mobilize.client.common.Privacy;
import edu.ucla.cens.mobilize.client.common.RunningState;
import edu.ucla.cens.mobilize.client.common.RoleCampaign;
import edu.ucla.cens.mobilize.client.dataaccess.DataService;
import edu.ucla.cens.mobilize.client.model.CampaignShortInfo;
import edu.ucla.cens.mobilize.client.view.CampaignView.Presenter;

public class CampaignList extends Composite {

  // expose css styles from the uibinder template
  public interface CampaignListStyle extends CssResource {
    String campaignGrid();
    String campaignGridNameColumn();
    String campaignGridHeader();
    String campaignGridRunningStateHeader();
    String campaignGridPrivacyHeader();
    String oddRow();
    String privacyShared();
    String privacyPrivate();
    String running();
    String stopped();
    // action links
    String analyzeLink();
    String detailsLink();
    String editLink();
    String exportLink();
  }
  
  private static CampaignListWidgetUiBinder uiBinder = GWT
      .create(CampaignListWidgetUiBinder.class);

  @UiTemplate("CampaignList.ui.xml")
  interface CampaignListWidgetUiBinder extends
      UiBinder<Widget, CampaignList> {
  }
  
  // declare uibinder fields
  @UiField HTMLPanel mainPanel;
  @UiField ListBox stateListBox;
  @UiField ListBox userRoleListBox;
  @UiField DateBox fromDateBox;
  @UiField DateBox toDateBox;
  @UiField Grid campaignGrid;
  @UiField CampaignListStyle style;
  
  // table columns 
  private enum Column { NAME, RUNNING_STATE, PRIVACY, ACTIONS };
  
  private DataService dataService;
  
  public CampaignList() {
    initWidget(uiBinder.createAndBindUi(this));
    initComponents();
  }

  public void setDataService(DataService dataService) {
    this.dataService = dataService;
  }
  
  private void initComponents() {
    // whether campaign is running
    stateListBox.addItem("Any");
    stateListBox.addItem("Running");
    stateListBox.addItem("Stopped");

    // current user's role in the campaign
    // FIXME: only show roles from user's roles list
    userRoleListBox.addItem("Any");
    userRoleListBox.addItem(RoleCampaign.ANALYST.toString());
    userRoleListBox.addItem(RoleCampaign.PARTICIPANT.toString());
    userRoleListBox.addItem(RoleCampaign.AUTHOR.toString());
    userRoleListBox.addItem(RoleCampaign.SUPERVISOR.toString());
    userRoleListBox.setSelectedIndex(0);
    
    // start and end dates
    @SuppressWarnings("deprecation")
    DateBox.Format fmt = new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat());
    fromDateBox.setFormat(fmt);
    toDateBox.setFormat(fmt);
    
    // set grid size. rows = # campaigns (guess) + 1 for header, cols comes from enum
    int maxCampaignsGuess = 20; // more row are added in addCampaign if needed
    campaignGrid.resize(maxCampaignsGuess + 1, Column.values().length); 
    
    // set up table heading
    campaignGrid.getRowFormatter().setStyleName(0, style.campaignGridHeader());
    campaignGrid.setText(0, Column.NAME.ordinal(), "Campaign Name");
    campaignGrid.setText(0, Column.RUNNING_STATE.ordinal(), "Running State");
    campaignGrid.setText(0, Column.PRIVACY.ordinal(), "Privacy");
    campaignGrid.setText(0, Column.ACTIONS.ordinal(), "Actions");
    
    // css styles
    campaignGrid.addStyleName(style.campaignGrid());
    campaignGrid.setCellSpacing(0);
    campaignGrid.setCellPadding(4);
    campaignGrid.getCellFormatter().setStyleName(0, 
                                                 Column.NAME.ordinal(), 
                                                 style.campaignGridNameColumn());
    campaignGrid.getCellFormatter().setStyleName(0, 
                                                 Column.RUNNING_STATE.ordinal(), 
                                                 style.campaignGridRunningStateHeader());
    campaignGrid.getCellFormatter().setStyleName(0, 
                                                 Column.PRIVACY.ordinal(), 
                                                 style.campaignGridPrivacyHeader());
  }
  
  /**
   * renders a list of campaigns in a table
   * @param campaigns
   */
  public void setCampaigns(List<CampaignShortInfo> campaigns) {
    this.campaignGrid.resizeRows(campaigns.size() + 1); // one extra row for header
    int row = 1; // 0th row is header
    for (CampaignShortInfo campaignInfo : campaigns) {
      addCampaign(row++, campaignInfo);
    }
  }
  
  private void addCampaign(int row, CampaignShortInfo campaignInfo) {
    // stripe odd rows
    if (row % 2 != 0) {
      this.campaignGrid.getRowFormatter().addStyleName(row, style.oddRow());
    }
    
    // campaign name links to campaign detail page
    Hyperlink campaignDetailLink = 
      new Hyperlink(campaignInfo.getCampaignName(), 
                    HistoryTokens.campaignDetail(campaignInfo.getCampaignId()));
    this.campaignGrid.setWidget(row, Column.NAME.ordinal(), campaignDetailLink); 
    this.campaignGrid.getCellFormatter().setStyleName(row, 
                                                      Column.NAME.ordinal(), 
                                                      style.campaignGridNameColumn());
    
    // running state 
    RunningState state = campaignInfo.getRunningState();
    this.campaignGrid.setText(row, Column.RUNNING_STATE.ordinal(), state.toString());
    this.campaignGrid.getCellFormatter().setStyleName(row, 
                                                      Column.RUNNING_STATE.ordinal(), 
                                                      getRunningStateStyle(state));
    
    // privacy column
    Privacy privacy = campaignInfo.getPrivacy();
    this.campaignGrid.setText(row, Column.PRIVACY.ordinal(), privacy.toString());
    this.campaignGrid.getCellFormatter().setStyleName(row, 
                                                      Column.PRIVACY.ordinal(), 
                                                      getPrivacyStyle(privacy));


    // actions column
    this.campaignGrid.setWidget(row, Column.ACTIONS.ordinal(), getActionsWidget(campaignInfo));
    
    
  }
  
  private Widget getActionsWidget(CampaignShortInfo campaign) {
    Panel panel = new FlowPanel();
    final String campaignId = campaign.getCampaignId();
    if (campaign.userCanViewDetails()) {
      InlineHyperlink detailsLink = 
        new InlineHyperlink("view", HistoryTokens.campaignDetail(campaignId));
      detailsLink.setStyleName(style.detailsLink());
      panel.add(detailsLink);
    }
    if (campaign.userCanAnalyze()) {
      InlineHyperlink analyzeLink = 
        new InlineHyperlink("analyze", HistoryTokens.campaignAnalyze(campaignId));
      analyzeLink.setStyleName(style.analyzeLink());
      panel.add(analyzeLink);
    }
    if (campaign.userCanEdit()) {
      InlineHyperlink editLink = 
        new InlineHyperlink("edit", HistoryTokens.campaignEdit(campaignId));
      editLink.setStyleName(style.editLink());
      panel.add(editLink);
    }
    if (campaign.userCanAnalyze()) {
      Anchor exportLink = new Anchor("export");
      exportLink.setStyleName(style.exportLink());
      panel.add(exportLink);
      exportLink.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          exportCsv(campaignId);
        }
      });
    }
    
    return panel.asWidget();
  }
  
  private void exportCsv(String campaignId) {
    assert dataService != null : "DataService is null. Did you forget to call CampaignList.setDataService?";
    FormPanel exportForm = new FormPanel("_blank"); // target="_blank" to open new window
    exportForm.setAction(AwConstants.getSurveyResponseReadUrl()); // FIXME
    exportForm.setMethod(FormPanel.METHOD_POST);
    FlowPanel innerContainer = new FlowPanel();
    
    Map<String, String> params = dataService.getSurveyResponseExportParams(campaignId);
    for (String paramName : params.keySet()) {
      Hidden field = new Hidden();
      field.setName(paramName);
      field.setValue(params.get(paramName));
      innerContainer.add(field);
    }
    exportForm.add(innerContainer);
    mainPanel.add(exportForm);
    exportForm.submit();
    exportForm.removeFromParent();
  }
  
  private String getPrivacyStyle(Privacy privacy) {
    String styleName = "";
    switch (privacy) {
      case PRIVATE: styleName = style.privacyPrivate(); break;
      case SHARED: styleName = style.privacyShared(); break;
      case INVISIBLE: break;
      default: break;
    }
    return styleName;
  }
  
  private String getRunningStateStyle(RunningState runningState) {
    String styleName = "";
    switch (runningState) {
      case RUNNING: styleName = style.running(); break;
      case STOPPED: styleName = style.stopped(); break;
      default: break;
    }
    return styleName;
  }

}
