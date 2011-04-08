package edu.ucla.cens.mobilize.client.dataaccess;
import java.util.List;
import java.util.Map;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.ucla.cens.mobilize.client.model.AuthorizationTokenQueryAwData;
import edu.ucla.cens.mobilize.client.model.CampaignDetailedInfo;
import edu.ucla.cens.mobilize.client.model.CampaignConciseInfo;
import edu.ucla.cens.mobilize.client.model.DataPointAwData;
import edu.ucla.cens.mobilize.client.model.SurveyInfo;
import edu.ucla.cens.mobilize.client.model.SurveyResponse;
import edu.ucla.cens.mobilize.client.model.UserInfo;


/**
 * Defines the API to access each of the AndWellness query APIs.  These include /app/auth_token, 
 * /app/q/config, /app/q/upload_stat, /app/q/data_point, and /app/q/binary.  The service will
 * parse the returned data into Models defined in edu.ucla.cens.mobilize.client.model.
 * 
 * Throws a RequestException directly if the server cannot be contacted.
 * Calls AsyncCallback.onError() if the server returns an http status code error,
 * or an error in the JSON response.
 * 
 * @author jhicks
 * @author vhajdik
 *
 */
public interface DataService {
  /**
   * Fetches an authorization token plus a list of campaign names from the server.
   * Throws a runtime error if the username/password combination fails authentication.
   * 
   * @param userName Username to authorize.
   * @param password Password to authorize.
   * @param callback Callback to call when the server responds.
   */
  void fetchAuthorizationToken(String userName, String password, 
                               final AsyncCallback<AuthorizationTokenQueryAwData> callback);
  
  // users
  void fetchUserInfo(String username, final AsyncCallback<UserInfo> asyncCallback);
  
  // campaigns
  void fetchCampaignList(Map<String, List<String>> params, 
                         final AsyncCallback<List<CampaignConciseInfo>> callback);
  void fetchCampaignDetail(String campaignId, 
                           final AsyncCallback<CampaignDetailedInfo> callback);
  
  // same as fetchCampaignDetail but fetches multiple campaigns at once
  void fetchCampaignDetailList(List<String> campaignIds, 
                               final AsyncCallback<List<CampaignDetailedInfo>> callback);
  
  // get cached detail. returns null if campaigns have not been loaded
  CampaignDetailedInfo getCampaignDetail(String campaignId);
  
  void deleteCampaign(String campaignId,
                      final AsyncCallback<ResponseDelete> callback);
  
  // get data points for one campaign at a time
  void fetchPrivateSurveyResponses(String campaignId, // TODO: pass campaignInfo?
                                   final AsyncCallback<List<SurveyResponse>> callback);
  
  // void fetchPromptResponses
  // void fetchPromptInfos
  // void fetchDataPoints

  // FIXME: add date range
  void fetchPrivateDataPoints(String campaignId,
                              final AsyncCallback<List<DataPointAwData>> asyncCallback);
}