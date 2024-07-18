package com.constructiveactivists.usermanagementmodule.controllers.user.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "social")
public class SocialProperties {

    private String googleClientId;
    private String facebookTokenUrl;
    private String facebookGraphApiUrl;


    public String getGoogleClientId() {
        return googleClientId;
    }

    public void setGoogleClientId(String googleClientId) {
        this.googleClientId = googleClientId;
    }

    public String getFacebookTokenUrl() {
        return facebookTokenUrl;
    }

    public void setFacebookTokenUrl(String facebookTokenUrl) {
        this.facebookTokenUrl = facebookTokenUrl;
    }

    public String getFacebookGraphApiUrl() {
        return facebookGraphApiUrl;
    }

    public void setFacebookGraphApiUrl(String facebookGraphApiUrl) {
        this.facebookGraphApiUrl = facebookGraphApiUrl;
    }
}
