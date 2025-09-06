package com.demo.crud.storage;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BaseEntity {

    private long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BaseEntity(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }

    public BaseEntity(long id){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
        this.id=id;
    }

//    public long getId(){
//        return id;
//    }
//    public void setId(long id){
//        this.id=id;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
}
