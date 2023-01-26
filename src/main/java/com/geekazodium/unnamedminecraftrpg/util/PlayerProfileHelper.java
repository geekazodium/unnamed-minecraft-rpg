package com.geekazodium.unnamedminecraftrpg.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.*;
import org.bukkit.entity.Player;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class PlayerProfileHelper {
    public static HttpsURLConnection getConnection(URL url) throws IOException, NoSuchAlgorithmException {
        SSLContext ctx = SSLContext.getInstance("TLS");
        SSLContext.setDefault(ctx);
        try {
            ctx.init(new KeyManager[]{},new TrustManager[]{new MojangAPICertificateManager()}, SecureRandom.getInstanceStrong());
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        return connection;
    }

    public static void setPlayerSkin(Player player, JsonObject object){
        JsonArray properties = object.get("properties").getAsJsonArray();
        PlayerProfile playerProfile = player.getPlayerProfile();
        properties.forEach(jsonElement -> {
            JsonObject property = jsonElement.getAsJsonObject();
            if (property.get("name").getAsString().equals("textures")){
                playerProfile.setProperty(new ProfileProperty(
                        "textures",
                        property.get("value").getAsString(),
                        property.get("signature").getAsString()
                        ));
            }
        });
        player.setPlayerProfile(playerProfile);
    }

    public static JsonObject getPlayerProfile(String s,boolean isUuid) {
        if(s == null || s.length()==0){
            return null;
        }
        String uuid;
        if(isUuid){
            uuid = s;
        }else{
            try {
                JsonObject object = requestJson("https://api.mojang.com/users/profiles/minecraft/"+ s);
                uuid = object.get("id").getAsString();
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return requestJson("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid+"?unsigned=false");
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonObject requestJson(String s) throws IOException, NoSuchAlgorithmException {
        HttpsURLConnection connection = getConnection(new URL(s));
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        String string = new String(inputStream.readAllBytes());
        JsonElement json = JsonParser.parseString(string);
        connection.disconnect();
        return json.getAsJsonObject();
    }

    private static class MojangAPICertificateManager implements X509TrustManager{
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            for (X509Certificate certificate : x509Certificates) {
                certificate.checkValidity();
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
            for (X509Certificate certificate : paramArrayOfX509Certificate) {
                certificate.checkValidity();
            }
        }
    }
}
