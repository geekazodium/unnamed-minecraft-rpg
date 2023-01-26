package com.geekazodium.tests;

import com.geekazodium.unnamedminecraftrpg.util.PlayerProfileHelper;

public class TestsHandler {
    public static boolean IS_TESTING_ENV = false;
    public static void main(String[] args){
        IS_TESTING_ENV = true;
        PlayerProfileHelper.getPlayerProfile("angusthecow",false);
    }
}
