package com.geekazodium.tests;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.geekazodium.cavernsofamethyst.util.PlayerProfileHelper;

import java.net.MalformedURLException;

public class TestsHandler {
    public static boolean IS_TESTING_ENV = false;
    public static void main(String[] args){
        IS_TESTING_ENV = true;
        PlayerProfileHelper.getPlayerProfile("angusthecow",false);
    }
}
