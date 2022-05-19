package com.example.prudentialfinance.Model;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

public class GlobalVariable extends Application {

    private String appName = "com.example.prudentialfinance";
    private String accessToken;

    private String contentType = "application/x-www-form-urlencoded";
    private Map<String, String> headers;


    private User AuthUser;
    private SiteSettings appInfo;

    public SiteSettings getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(SiteSettings appInfo) {
        this.appInfo = appInfo;
    }

    public User getAuthUser() {
        return AuthUser;
    }

    public void setAuthUser(User authUser) {
        AuthUser = authUser;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = "JWT " + accessToken;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    /***
     * @author Phong-Kaster
     *
     * this functions supports us establish a header which is used in a HTTP request
     *
     * @return
     */
    public Map<String, String> getHeaders() {

        this.headers = new HashMap<>();
        this.headers.put("Content-Type", contentType );
        this.headers.put("Authorization", accessToken);

        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
