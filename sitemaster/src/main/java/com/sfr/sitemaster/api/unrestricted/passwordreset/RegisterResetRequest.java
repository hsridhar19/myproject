package com.sfr.sitemaster.api.unrestricted.passwordreset;

/**
 * Created by tuxbear on 08/01/15
 *
 * Represents a request for a Password Reset Token for the given user
 *
 */
public class RegisterResetRequest {

    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

}
