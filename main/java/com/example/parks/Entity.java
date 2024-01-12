package com.example.parks;

public interface Entity<K>{
    K getID();
    void setID(K id);
}
