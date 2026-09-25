package com.alan.clients.auth;

//add code
public interface AuthProvider {

    String username();

    boolean isAuthenticated();

    boolean login(String credential);

    void logout();
}
