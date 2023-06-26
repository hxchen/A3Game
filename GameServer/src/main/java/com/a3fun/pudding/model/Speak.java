package com.a3fun.pudding.model;

public class Speak {
    private long id;
    private String content;

    public Speak() {
    }

    public Speak(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public String toString() {
        return "Speak{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
