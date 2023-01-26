package com.geekazodium.cavernsofamethyst.soundtracks;

public enum Pitch{//useless

    C_0(0f),
    Cs_0(1f/12f),
    D_0(2f/12f),
    Ef_0(3f/12f),
    E_0(4f/12f),
    F_0(5f/12f),
    Fs_0(6f/12f),
    G_0(7f/12f),
    Gs_0(8f/12f),
    A_0(9f/12f),
    Bf_0(10f/12f),
    B_0(11f/12f),
    C_1(1f),
    Cs_1(1f+1f/12f),
    D_1(1f+2f/12f),
    Ef_1(1f+3f/12f),
    E_1(1f+4f/12f),
    F_1(1f+5f/12f),
    Fs_1(1f+6f/12f),
    G_1(1f+7f/12f),
    Gs_1(1f+8f/12f),
    A_1(1f+9f/12f),
    Bf_1(1f+10f/12f),
    B_1(1f+11f/12f),
    C_2(2f);

    private final float p;
    Pitch(float v) {
        p = v;
    }
    public float pitch(){
        return p;
    }
}
