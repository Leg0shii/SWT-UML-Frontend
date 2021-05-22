package de.swt.manager;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class IPAdressManager {

    private HashMap<Integer, String> ipHashMap;

    public IPAdressManager() {
        this.ipHashMap = new HashMap<>();
    }

}
