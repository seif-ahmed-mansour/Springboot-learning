package com.demo.crud.storage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User extends BaseEntity{
    private String name;
    private String email;
    private int age;

    public User(){
        super();
    }
    public User(String name, String email, int age){
        super();
        this.age=age;
        this.email=email;
        this.name=name;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
}
