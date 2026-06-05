package com.races.plugin.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<String, Long> map = new HashMap<>();

    public void set(UUID uuid, String key, long ms) {
        map.put(uuid + "_" + key, System.currentTimeMillis() + ms);
    }

    public boolean has(UUID uuid, String key) {
        Long exp = map.get(uuid + "_" + key);
        if (exp == null) return false;
        if (System.currentTimeMillis() >= exp) { map.remove(uuid + "_" + key); return false; }
        return true;
    }

    public long remaining(UUID uuid, String key) {
        Long exp = map.get(uuid + "_" + key);
        if (exp == null) return 0;
        long r = exp - System.currentTimeMillis();
        return r > 0 ? r / 1000 + 1 : 0;
    }
}
