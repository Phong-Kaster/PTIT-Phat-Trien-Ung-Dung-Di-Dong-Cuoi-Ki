package com.example.prudentialfinance.Model;

public class SpinnerElement {
    private String name;
    private String value;
    private boolean selected;

    public SpinnerElement(String name, String value, boolean selected) {
        this.name = name;
        this.value = value;
        this.selected = selected;
    }

    public SpinnerElement() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "SpinnerElement{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", selected=" + selected +
                '}';
    }
}
