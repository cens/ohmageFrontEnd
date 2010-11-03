package edu.ucla.cens.AndWellnessVisualizations.client.utils;

import java.util.Map;

import com.google.gwt.http.client.URL;

public class MapUtils {
    /**
     * Translate a map of key/values pairs into a string of parameters in the form of
     * k1=v1&k1=v2&...
     * @param toTranslate The map to translate.
     * @return The translated string.
     */
    public static String translateToParameters(Map<?,?> toTranslate) {
        StringBuffer parameterString = new StringBuffer();
        boolean firstParameter = true;  // Don't put an & for the first parameters
        
        for (Object key:toTranslate.keySet()) {
            // If this is not the first parameter, start with a separating ampersand
            if (!firstParameter) {
                parameterString.append("&");
            }
            
            // Add the key=value
            parameterString.append(URL.encode(key.toString()));
            parameterString.append("=");
            parameterString.append(URL.encode(toTranslate.get(key).toString()));
            
            firstParameter = false;
        }
        
        return parameterString.toString();
    }
}