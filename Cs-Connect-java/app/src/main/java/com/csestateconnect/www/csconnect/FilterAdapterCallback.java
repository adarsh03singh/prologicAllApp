package com.csestateconnect.www.csconnect;

public interface FilterAdapterCallback {
    void onBhkcheckedCallback(Integer position, Boolean checkedValue);
    void onStatuscheckedCallback(Integer position, Boolean checkedValue);
    void onAmenitycheckedCallback(Integer position, Boolean checkedValue);
    void onLocationcheckedCallback(Integer position, Boolean checkedValue);
}
