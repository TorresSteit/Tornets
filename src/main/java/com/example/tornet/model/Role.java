package com.example.tornet.model;





public enum  Role {
    Admin, Users;



    @Override
    public String toString() {
        return "ROLE_" + name();
    }

}
