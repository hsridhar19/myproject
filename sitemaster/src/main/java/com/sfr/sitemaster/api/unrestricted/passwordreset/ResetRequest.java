package com.sfr.sitemaster.api.unrestricted.passwordreset;

/**
 * Created by tuxbear on 08/01/15.
 *
 * Contains a request to change a user's password. The tokenstring is used to determine the user.
 *
 */
public class ResetRequest {

    String token;
    String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }
}
