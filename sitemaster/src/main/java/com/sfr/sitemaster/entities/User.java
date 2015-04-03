/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.entities;

import com.google.code.morphia.annotations.Entity;
import com.sfr.apicore.annotations.Merge;
import com.sfr.apicore.entities.SFREntityObject;

/**
 * Entity.
 * 
 * @author yves
 */
@Entity(value = "users", noClassnameStored = true)
public class User extends SFREntityObject {

    private static final long serialVersionUID = 1L;

    @Merge(no_merge = true, null_allowed = false)
    private String email;

    @Merge(no_merge = true, null_allowed = false)
    private String password;

    private String name;

    @Merge(no_merge = true, null_allowed = false)
    private String salt;

    public User() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(final Object obj) { //NOPMD
        return super.equals(obj);
    }

    @Override
    public int hashCode() {     //NOPMD
        return super.hashCode();
    }
}
