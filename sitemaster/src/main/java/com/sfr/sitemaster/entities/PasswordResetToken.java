package com.sfr.sitemaster.entities;

import java.util.Date;
import java.util.UUID;

import com.google.code.morphia.annotations.Entity;
import com.sfr.apicore.entities.SFREntityObject;

/**
 * Created by tuxbear on 07/01/15.
 *
 * Stores a token that grants the privelege of changing this user's password
 * until it expires.
 *
 */
@Entity(value = "passwordresettoken", noClassnameStored = true)
public class PasswordResetToken extends SFREntityObject {
    private static final long serialVersionUID = 1L;

    private String token;
    private Date validUntil;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Date getValidUntil() {
        return validUntil == null ? null : new Date(validUntil.getTime());
    }

    public void setValidUntil(final Date validUntil) {
        this.validUntil = validUntil == null ? null : new Date(validUntil.getTime());
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public PasswordResetToken(final Date validUntil, final String username) {
        this.token = UUID.randomUUID().toString();
        this.validUntil = validUntil == null ? null : new Date(validUntil.getTime());
        this.username = username;
    }

    public PasswordResetToken() {
        // For serialization
    }

    public boolean isValidAt(final Date date) {
        return date.before(getValidUntil());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final PasswordResetToken that = (PasswordResetToken) o;

        if (token != null ? !token.equals(that.token) : that.token != null) { // NOPMD
            return false;
        }
        if (username != null ? !username.equals(that.username) : that.username != null) { // NOPMD
            return false;
        }
        if (validUntil != null ? !validUntil.equals(that.validUntil) : that.validUntil != null) { // NOPMD
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (token != null ? token.hashCode() : 0); // NOPMD
        result = 31 * result + (validUntil != null ? validUntil.hashCode() : 0); // NOPMD
        result = 31 * result + (username != null ? username.hashCode() : 0); // NOPMD
        return result;
    }
}
