package com.example.showpointrma.util;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private static final String PREFS = "showpoint_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_IME = "ime";
    private static final String KEY_EMAIL = "email";

    private final SharedPreferences prefs;

    // kada koristimo login ekran
    public void saveSession(String token, String ime, String email) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_IME, ime)
                .putString(KEY_EMAIL, email)
                .apply();
    }

    public TokenManager(Context context) {
        this.prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // ostaje zbog privremenog DevLogin koda
    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getIme() {
        return prefs.getString(KEY_IME, null);
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    // brise sve vezano za sesiju
    public void clear() {
        prefs.edit().remove(KEY_TOKEN).apply();
    }
}
