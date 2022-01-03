package me.fineasgavre.pdp.lab8.Models;

import java.io.Serializable;
import java.util.StringJoiner;

public class Variable implements Serializable {
    private String name;
    private int value;
    private int version = 0;

    public Variable(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void incrementVersion() {
        version++;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Variable.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("value=" + value)
                .add("version=" + version)
                .toString();
    }
}
