package edu.ucla.cens.mobilize.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHyperlink;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.ucla.cens.mobilize.client.common.HistoryTokens;

public class AccountViewImpl extends Composite implements AccountView {

  private static AccountViewUiBinder uiBinder = GWT
      .create(AccountViewUiBinder.class);

  @UiTemplate("AccountView.ui.xml")
  interface AccountViewUiBinder extends UiBinder<Widget, AccountViewImpl> {
  }

  @UiField InlineLabel messageBox;
  @UiField InlineLabel loginLabel;
  @UiField InlineLabel canCreateLabel;
  @UiField VerticalPanel classesVerticalPanel;
  @UiField HTMLPanel passwordChangePanel;
  @UiField Button passwordChangeButton;
  @UiField Button passwordChangeCancelButton;
  @UiField PasswordTextBox oldPasswordTextBox;
  @UiField PasswordTextBox newPasswordTextBox;
  @UiField Label passwordMismatchError;
  @UiField PasswordTextBox newPasswordConfirmTextBox;
  @UiField Button passwordChangeSubmitButton;
  
  public AccountViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
    
    // some elements stay hidden unless needed
    hideMessage();
    passwordMismatchError.setVisible(false);
    hidePasswordChangeForm();
  }

  @Override
  public void setUserName(String userName) {
    this.loginLabel.setText(userName);
  }

  @Override
  public void setCanCreate(boolean canCreate) {
    this.canCreateLabel.setText(canCreate ? "Yes" : "No");
  }
  
  @Override
  public void clearClassList() {
    this.classesVerticalPanel.clear();
  }

  @Override
  public void addClass(String classId, String className) {
    this.classesVerticalPanel.add(new InlineHyperlink(className, 
                                                      HistoryTokens.classDetail(classId)));    
  }

  @Override
  public void showPasswordChangeForm() {
    this.passwordChangePanel.setVisible(true);
  }

  @Override
  public void hidePasswordChangeForm() {
    this.passwordChangePanel.setVisible(false);
  }

  @Override
  public void resetPasswordChangeForm() {
    this.oldPasswordTextBox.setText("");
    this.newPasswordTextBox.setText("");
    this.newPasswordConfirmTextBox.setText("");
  }
  
  @Override
  public HasClickHandlers getPasswordChangeButton() {
    return this.passwordChangeButton;    
  }

  @Override
  public HasClickHandlers getPasswordChangeSubmitButton() {
    return this.passwordChangeSubmitButton;
  }
  
  @Override
  public HasClickHandlers getPasswordChangeCancelButton() {
    return this.passwordChangeCancelButton;
  }

  @Override
  public String getUserName() {
    return this.loginLabel.getText();
  }
  
  @Override
  public String getOldPassword() {
    return this.oldPasswordTextBox.getText();    
  }

  @Override
  public String getNewPassword() {
    return this.newPasswordTextBox.getText();
  }

  @Override
  public String getNewPasswordConfirm() {
    return this.newPasswordConfirmTextBox.getText();
  }

  @Override
  public void showPasswordMismatchError() {
    this.passwordMismatchError.setVisible(true);
  }

  @Override
  public void showMessage(String message) {
    // TODO: add hide link
    this.messageBox.setText(message);
    this.messageBox.setVisible(true);
  }

  @Override
  public void showError(String message) {
    // TODO: add style
    this.messageBox.setText(message);
    this.messageBox.setVisible(true);
  }

  @Override
  public void hideMessage() {
    this.messageBox.setText("");
    this.messageBox.setVisible(false);
  }
}