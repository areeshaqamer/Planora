package com.example.planora.models;

public class ChecklistItem {
    public String name;
    public boolean checked;

    public ChecklistItem(String name) {
        this.name = name;
        this.checked = false;
    }
}