package me.lesonnnn.hctvng.models;

public class TuVung {
    private int mId;
    private String mTu;
    private String mNoiDung;
    private int mIdListTV;

    public TuVung() {
    }

    public TuVung(String tu, String noidung) {
        mTu = tu;
        mNoiDung = noidung;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTu() {
        return mTu;
    }

    public void setTu(String tu) {
        mTu = tu;
    }

    public String getNghia() {
        return mNoiDung;
    }

    public void setNghia(String nghia) {
        mNoiDung = nghia;
    }

    public int getIdListTV() {
        return mIdListTV;
    }

    public void setIdListTV(int idListTV) {
        mIdListTV = idListTV;
    }
}
