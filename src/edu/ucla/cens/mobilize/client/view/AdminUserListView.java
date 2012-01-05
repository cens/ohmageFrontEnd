package edu.ucla.cens.mobilize.client.view;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.ucla.cens.mobilize.client.common.HistoryTokens;
import edu.ucla.cens.mobilize.client.common.RoleClass;
import edu.ucla.cens.mobilize.client.model.UserSearchInfo;
import edu.ucla.cens.mobilize.client.ui.AdminAddUsersToClass;
import edu.ucla.cens.mobilize.client.ui.AdminMenu;
import edu.ucla.cens.mobilize.client.ui.ConfirmDeleteDialog;
import edu.ucla.cens.mobilize.client.ui.ErrorDialog;
import edu.ucla.cens.mobilize.client.ui.UserSearchFilterWidget;
import edu.ucla.cens.mobilize.client.ui.WaitIndicator;

public class AdminUserListView extends Composite {

  private static AdminUserListViewUiBinder uiBinder = GWT
      .create(AdminUserListViewUiBinder.class);

  interface AdminUserListViewUiBinder extends
      UiBinder<Widget, AdminUserListView> {
  }
  
  public AdminUserListView() {
    initWidget(uiBinder.createAndBindUi(this));
    initComponents();
  }

  //***********************
  
  public interface AdminUserListViewStyle extends CssResource {
    String disabled();
    String oddRow();
    String detailsLink();
    String editLink();
    String userListHeaderRow();
  }

  @UiField AdminMenu adminMenu;
  @UiField Grid userListHeader;
  @UiField HTMLPanel instructionsPanel;
  @UiField InlineHyperlink showAllUsersLink;
  @UiField Grid userListGrid;
  @UiField AdminUserListViewStyle style;
  @UiField TextBox searchUsernameTextBox;
  @UiField Button searchUsernameButton;
  @UiField TextBox searchPersonalIdTextBox;
  @UiField Button searchPersonalIdButton;
  @UiField Anchor advancedSearchLink;
  @UiField Anchor errorLink;
  @UiField Button deleteButton;
  @UiField Button disableButton;
  @UiField Button enableButton;
  @UiField Button addToClassButton;
  @UiField InlineHyperlink manageClassesLink;
  

  private DialogBox addUsersToClassDialog;
  private AdminAddUsersToClass addUsersToClassWidget;
  private UserSearchFilterWidget advancedSearchWidget;

  private final String cellWidthCheckBox = "30px";
  private final String cellWidthEnabled = "60px";
  private final String cellWidthPersonalInfo = "130px";
  private final String cellWidthUsername = "100px";
  
  private static class Columns {
    static final int CHECKBOX = 0;
    static final int USERNAME = 1;
    static final int ENABLED = 2;
    static final int FIRST_NAME = 3;
    static final int LAST_NAME = 4;
    static final int PERSONAL_ID = 5;
    static final int ACTIONS = 6;
    
    static final int columnCount = 7;
  }
  
  private void initComponents() {
    // set target history token for show all link
    this.showAllUsersLink.setTargetHistoryToken(HistoryTokens.adminUserList("*", null, null, null, null, null, null, null, null, null));
    // select item in left nav
    this.adminMenu.selectManageUsers();
    // set up hyperlinks
    this.manageClassesLink.setTargetHistoryToken(HistoryTokens.adminClassList());
    // set up user list grid
    this.userListHeader.resize(1, Columns.columnCount);
    CheckBox selectAllCheckBox = new CheckBox();
    selectAllCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        if (event.getValue() == true) {
          selectAll();
        } else {
          selectNone();
        }
      }
    });
    //this.userListHeader.setStyleName(style.headerRow());
    this.userListHeader.getRowFormatter().setStyleName(0, style.userListHeaderRow());
    this.userListHeader.setWidget(0, Columns.CHECKBOX, selectAllCheckBox);
    this.userListHeader.setText(0, Columns.USERNAME, "Username");
    this.userListHeader.setText(0, Columns.ENABLED, "Enabled");
    this.userListHeader.setText(0, Columns.FIRST_NAME, "First Name");
    this.userListHeader.setText(0, Columns.LAST_NAME, "Last Name");
    this.userListHeader.setText(0, Columns.PERSONAL_ID, "Personal Id");
    this.userListHeader.setText(0, Columns.ACTIONS, "Actions");
    this.userListHeader.setCellSpacing(0);
    this.userListHeader.setCellPadding(5);
    this.userListGrid.setCellSpacing(0);
    this.userListGrid.setCellPadding(5);
    this.userListGrid.resizeColumns(Columns.columnCount);
    // set up column widths in header
    this.userListHeader.getColumnFormatter().setWidth(Columns.CHECKBOX, this.cellWidthCheckBox);
    this.userListHeader.getColumnFormatter().setWidth(Columns.USERNAME, this.cellWidthUsername);
    this.userListHeader.getColumnFormatter().setWidth(Columns.ENABLED, this.cellWidthEnabled);
    this.userListHeader.getColumnFormatter().setWidth(Columns.FIRST_NAME, this.cellWidthPersonalInfo);
    this.userListHeader.getColumnFormatter().setWidth(Columns.LAST_NAME, this.cellWidthPersonalInfo);
    this.userListHeader.getColumnFormatter().setWidth(Columns.PERSONAL_ID, this.cellWidthPersonalInfo);
    // set column widths in table to match those in header
    this.userListGrid.getColumnFormatter().setWidth(Columns.CHECKBOX, this.cellWidthCheckBox);
    this.userListGrid.getColumnFormatter().setWidth(Columns.USERNAME, this.cellWidthUsername);
    this.userListGrid.getColumnFormatter().setWidth(Columns.ENABLED, this.cellWidthEnabled);
    this.userListGrid.getColumnFormatter().setWidth(Columns.FIRST_NAME, this.cellWidthPersonalInfo);
    this.userListGrid.getColumnFormatter().setWidth(Columns.LAST_NAME, this.cellWidthPersonalInfo);
    this.userListGrid.getColumnFormatter().setWidth(Columns.PERSONAL_ID, this.cellWidthPersonalInfo);
    // error link is invisible until an error occurs
    this.errorLink.setVisible(false);
    // create advanced search widget so any search strings from the history token can be filled in
    this.advancedSearchWidget = new UserSearchFilterWidget();
  }
  
  public void setFilters(String searchString) {
  }
  
  public void setUserList(List<UserSearchInfo> users) {
    clearUserList();
    if (users == null || users.isEmpty()) {
      return;
    }
    this.userListGrid.resizeRows(users.size() + 1); // hack: +1 to make last row visible
    int row = 0;
    for (UserSearchInfo user : users) {
      // stripe odd rows for readability
      if (row % 2 == 1) this.userListGrid.getRowFormatter().addStyleName(row, style.oddRow());
      // fill in user info
      this.userListGrid.setWidget(row, Columns.CHECKBOX, new CheckBox());
      InlineHyperlink usernameLink = new InlineHyperlink(user.getUsername(), 
                                                         HistoryTokens.adminUserDetail(user.getUsername()));
      this.userListGrid.setWidget(row, Columns.USERNAME, usernameLink);
      this.userListGrid.setText(row, Columns.FIRST_NAME, user.getFirstName());
      this.userListGrid.setText(row, Columns.LAST_NAME, user.getLastName());
      this.userListGrid.setText(row, Columns.PERSONAL_ID, user.getPersonalId());
      // set up actions links
      this.userListGrid.setWidget(row, Columns.ACTIONS, getActionsWidget(user.getUsername()));
      if (user.isEnabled()) {
        markUserEnabled(row);
      } else {
        markUserDisabled(row);
      }
      row++;
    }
  }
  
  public void clearUserList() {
    this.userListGrid.resizeRows(0);
  }

  private Widget getActionsWidget(final String username) {
    Panel panel = new FlowPanel();
    
    InlineHyperlink detailsLink = new InlineHyperlink("view", HistoryTokens.adminUserDetail(username));
    detailsLink.addStyleName(style.detailsLink());
    panel.add(detailsLink);
    
    InlineHyperlink editLink = new InlineHyperlink("edit", HistoryTokens.adminUserEdit(username));
    editLink.addStyleName(style.editLink());
    panel.add(editLink);
    
    return panel.asWidget();
  }
    
  private void selectAll() {
    for (int row = 0; row < this.userListGrid.getRowCount(); row++) {
      CheckBox cb = (CheckBox)this.userListGrid.getWidget(row, Columns.CHECKBOX);
      if (cb != null) cb.setValue(true);
    }
  }
  
  private void selectNone() {
    for (int row = 0; row < this.userListGrid.getRowCount(); row++) {
      CheckBox cb = (CheckBox)this.userListGrid.getWidget(row, Columns.CHECKBOX);
      if (cb != null) cb.setValue(false);
    }
  }
  
  public HasClickHandlers getUserDeleteButton() {
    return this.deleteButton;
  }
  
  public HasClickHandlers getUserEnableButton() {
    return this.enableButton;
  }
  
  public HasClickHandlers getUserDisableButton() {
    return this.disableButton;
  }
  
  public HasClickHandlers getUserAddToClassButton() {
    return this.addToClassButton;
  }
  
  public HasClickHandlers getSearchUsernameButton() {
    return this.searchUsernameButton;
  }
  
  public HasKeyDownHandlers getSearchUsernameTextBox() {
    return this.searchUsernameTextBox;
  }
  
  public HasClickHandlers getSearchPersonalIdButton() {
    return this.searchPersonalIdButton;
  }
  
  public HasKeyDownHandlers getSearchPersonalIdTextBox() {
    return this.searchPersonalIdTextBox;
  }
  
  public HasClickHandlers getAdvancedSearchLink() {
    return this.advancedSearchLink;
  }
  
  public HasClickHandlers getErrorLink() {
    return this.errorLink;
  }
  
  public String getUsernameSearchString() {
    return this.searchUsernameTextBox.getText();
  }
  
  public void setUsernameSearchString(String username) {
    this.searchUsernameTextBox.setText(username);
  }
  
  public String getPersonalIdSearchString() {
    return this.searchPersonalIdTextBox.getText();
  }
  
  public void setPersonalIdSearchString(String personalId) {
    this.searchPersonalIdTextBox.setText(personalId);
  }
  
  public List<String> getSelectedUsernames() {
    List<String> usernames = new ArrayList<String>();
    for (int row = 0; row < this.userListGrid.getRowCount(); row++) {
      CheckBox cb = (CheckBox)this.userListGrid.getWidget(row, Columns.CHECKBOX);
      if (cb != null && cb.getValue()) {
        usernames.add(this.userListGrid.getText(row, Columns.USERNAME));
      }
    }
    return usernames;
  }

  /**
   * Shows delete confirmation dialog. Executes onConfirmDelete if the user clicks Delete.
   */
  public void showConfirmDelete(List<String> usernames, final ClickHandler onConfirmDelete) {
    String msg = null;
    if (usernames.size() == 1) {
      msg = "Are you sure you want to delete " + usernames.get(0) + "?";
    } else {
      msg = "You are about to delete " + usernames.size() + " users. Are you sure?";
    }
    ConfirmDeleteDialog.show(msg, onConfirmDelete);
  }
  
  public void showWaitIndicator() {
    WaitIndicator.show();
  }
  
  public void hideWaitIndicator() {
    WaitIndicator.hide();
  }
  
  public void showAdvancedSearchPopup(final ClickHandler handler) {
    final DialogBox dialog = new DialogBox();
    dialog.setText("Advanced search");
    dialog.setModal(true);
    dialog.setAutoHideEnabled(true);
    Button searchButton = new Button("Search");
    searchButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        if (handler != null) handler.onClick(event);
        dialog.hide();
      }
    });
    Button cancelButton = new Button("Cancel");
    cancelButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        dialog.hide();
      }
    });
    Button resetButton = new Button("Reset");
    resetButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        advancedSearchWidget.clearFields();
      }
    });
    FlowPanel panel = new FlowPanel();
    panel.add(this.advancedSearchWidget);
    
    HTMLPanel dialogFooter = new HTMLPanel("<div id='buttonDiv' class='infoDialogButtonDiv'></div>");
    dialogFooter.add(searchButton, "buttonDiv");
    dialogFooter.add(resetButton, "buttonDiv");
    dialogFooter.add(cancelButton, "buttonDiv");
    panel.add(dialogFooter);

    dialog.add(panel);
    dialog.showRelativeTo(this.advancedSearchLink);
  }
  
  // returns row index of user in the table or -1 if not found
  private int getUserRow(String username) {
    int userRow = -1;
    for (int row = 0; row < this.userListGrid.getRowCount(); row++) {
      if (username.equals(this.userListGrid.getText(row, Columns.USERNAME))) {
        userRow = row;
        break;
      }
    }
    return userRow;
  }
  
  public void markUserDisabled(int row) {
    this.userListGrid.getRowFormatter().addStyleName(row, style.disabled());
    this.userListGrid.setWidget(row, Columns.ENABLED, new Image("images/cross.png"));
  }
  
  public void markUserEnabled(int row) {
    this.userListGrid.getRowFormatter().removeStyleName(row, style.disabled());
    this.userListGrid.setWidget(row, Columns.ENABLED, new Image("images/tick.png"));
  }
  
  public void markUserDisabled(String username) {
    markUserDisabled(getUserRow(username));
  }
  
  public void markUserEnabled(String username) {
    markUserEnabled(getUserRow(username));
  }

  public void showAddUsersToClassDialog(List<String> usernames, List<String> classUrns, ClickHandler saveButtonClickHandler) {
    if (this.addUsersToClassDialog == null) { // lazy init
      this.addUsersToClassDialog = new DialogBox(true); // modal
      this.addUsersToClassDialog.setText("Add users to class");
      this.addUsersToClassWidget = new AdminAddUsersToClass();
      this.addUsersToClassWidget.getAddUsersButton().addClickHandler(saveButtonClickHandler);
      this.addUsersToClassWidget.getCancelButton().addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          hideAddUsersToClassDialog();
        }
      });
      this.addUsersToClassDialog.clear();
      this.addUsersToClassDialog.add(this.addUsersToClassWidget);
    }
    this.addUsersToClassWidget.setUsernames(usernames);
    Collections.sort(classUrns);
    this.addUsersToClassWidget.setClassList(classUrns);
    this.addUsersToClassDialog.center();
  }
  
  public void hideAddUsersToClassDialog() {
    if (this.addUsersToClassDialog != null) this.addUsersToClassDialog.hide();
  }
  
  public String getClassToAddUsers() {
    String classUrn = null;
    if (this.addUsersToClassWidget != null) {
      classUrn = this.addUsersToClassWidget.getClassUrn();
    }
    return classUrn;
  }

  public RoleClass getClassRoleForUsers() {
    return this.addUsersToClassWidget.getRole();
  }

  /**
   * @return true, false, or null
   */
  public Boolean getAdvancedSearchEnabled() {
    return this.advancedSearchWidget.getIsEnabled();
  }
  
  /**
   * @return true, false, or null
   */
  public Boolean getAdvancedSearchCanCreateCampaigns() {
    return this.advancedSearchWidget.getCanCreate();
  }
  
  /**
   * @return true, false, or null
   */
  public Boolean getAdvancedSearchIsAdmin() {
    return this.advancedSearchWidget.getIsAdmin();
  }
  
  public String getAdvancedSearchFirstNameSearchString() {
    return this.advancedSearchWidget.getFirstNameSearchString();
  }
  
  public String getAdvancedSearchLastNameSearchString() {
    return this.advancedSearchWidget.getLastNameSearchString();
  }
  
  public String getAdvancedSearchEmailSearchString() {
    return this.advancedSearchWidget.getEmailSearchString();
  }
  
  public String getAdvancedSearchOrganizationSearchString() {
    return this.advancedSearchWidget.getOrganizationSearchString();
  }
  
  public String getAdvancedSearchJsonSearchString() {
    return this.advancedSearchWidget.getJsonSearchString();
  }

  /**
   * @ true, false, or null
   */
  public void setAdvancedSearchEnabled(Boolean isEnabled) {
     this.advancedSearchWidget.setIsEnabled(isEnabled);
  }
  
  /**
   * @ true, false, or null
   */
  public void setAdvancedSearchCanCreateCampaigns(Boolean canCreate) {
     this.advancedSearchWidget.setCanCreate(canCreate);
  }
  
  /**
   * @ true, false, or null
   */
  public void setAdvancedSearchIsAdmin(Boolean isAdmin) {
     this.advancedSearchWidget.setIsAdmin(isAdmin);
  }
  
  public void setAdvancedSearchFirstNameSearchString(String str) {
     this.advancedSearchWidget.setFirstNameSearchString(str);
  }
  
  public void setAdvancedSearchLastNameSearchString(String str) {
     this.advancedSearchWidget.setLastNameSearchString(str);
  }
  
  public void setAdvancedSearchEmailSearchString(String str) {
     this.advancedSearchWidget.setEmailSearchString(str);
  }
  
  public void setAdvancedSearchOrganizationSearchString(String str) {
     this.advancedSearchWidget.setOrganizationSearchString(str);
  }
  
  public void setAdvancedSearchJsonSearchString(String str) {
     this.advancedSearchWidget.setJsonSearchString(str);
  }
  
  public void clearSearchBoxes() {
    this.searchUsernameTextBox.setText("");
    this.searchPersonalIdTextBox.setText("");
    this.advancedSearchWidget.clearFields();
  }
  
  public void showError(String msg, String detail) {
    ErrorDialog.show(msg, detail);
  }
  
  public void showErrorList(List<String> errors) {
    ErrorDialog.showErrorList("There was a problem completing the operation.", errors);
  }
  
  public void setErrorCount(int errorCount) {
    if (errorCount > 0) {
      this.errorLink.setText(errorCount + " Errors");
      this.errorLink.setVisible(true);
    } else {
      this.errorLink.setVisible(false);
    }
  }
  
  public void hideErrorLink() {
    this.errorLink.setVisible(false);
  }
  
  public void showInstructions() {
    this.instructionsPanel.setVisible(true);
    this.userListGrid.setVisible(false);
  }
  
  public void showUserList() {
    this.instructionsPanel.setVisible(false);
    this.userListGrid.setVisible(true);
  }
}
