package com.example.repository;


import com.example.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CoinRepository extends JpaRepository<Coin,String> {

//    List<Coin> getLocationsByType(String code);
}
