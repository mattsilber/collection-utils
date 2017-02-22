package com.guardanis.collections.adapters;

public interface Callback<T> {
    public void onTriggered(T value);
}
