package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "Coin")
//@AllArgsConstructor
//@NoArgsConstructor
public class Coin implements Serializable {

    @Id
    private String code;
    @Column(name="translate")
    private String translate;

    public Coin() {
    }

    public Coin(String code, String translate) {
//        this.id = id;
        this.code = code;
        this.translate = translate;
    }
}
