package com.example.tornet.model;


import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

;public enum  Role {
    Admin, Users;


    public String toString() {
        return "ROLE_" + name();
    }

}
