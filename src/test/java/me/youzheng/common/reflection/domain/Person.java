package me.youzheng.common.reflection.domain;

public class Person {

    private Person(final String name) {
        this.name = name;
    }

    public Person() {
    }

    public Person(final String name, final Integer age) {
        this.name = name;
        this.age = age;
    }

    private String name;
    private Integer age;
    public String teamName;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

}
