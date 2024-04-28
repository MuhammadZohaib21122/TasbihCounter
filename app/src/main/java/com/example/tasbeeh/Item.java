package com.example.tasbeeh;

import java.util.List;

public class Item {

    String wazaif;
   int id;

    public Item(String wazaif, int id) {
        this.wazaif = wazaif;
        this.id = id;

    }

    public static void add(String text, List<Item> itemList) {

        int newItemId = itemList.size() + 1;

        Item newItem = new Item(text, newItemId);


        itemList.add(newItem);
    }



    public String getWazaif() {
        return wazaif;
    }

    public void setWazaif(String wazaif) {
        this.wazaif = wazaif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
