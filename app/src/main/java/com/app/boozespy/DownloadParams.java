package com.app.boozespy;

import androidx.annotation.NonNull;

public class DownloadParams {
    private String searchTerm;
    private String location;

    public DownloadParams(String searchTerm, String location) {
        this.searchTerm = searchTerm;
        this.location = location;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String toString() {
        return "DownloadParams{" +
                "searchTerm='" + searchTerm + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
