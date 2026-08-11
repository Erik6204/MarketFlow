package com.example.marketflow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class emptyflle{
    public static void main(String[] args){
        List<String>list1=new ArrayList<>(List.of("Alicve","sda","dsa"));
        list1.add("Bob");
        System.out.println(list1.get(1));

        List<String>list2=new LinkedList<>(List.of("12sd","Alicve","sda","dsa"));
        System.out.println(list2.get(1));
        String key="A";
        String k="A";
        System.out.println(k.equals(key));
    }
}