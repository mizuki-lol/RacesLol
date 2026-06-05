package com.races.plugin.races;

public enum Race {
    ANGEL  ("angel",   "Ангел"),
    SHARK  ("shark",   "Акула"),
    GHOUL  ("ghoul",   "Гуль"),
    ELECTRO("electro", "Электро"),
    DRAGON ("dragon",  "Дракон"),
    TUNDRA ("tundra",  "Тундра");

    private final String id;
    private final String display;

    Race(String id, String display) { this.id = id; this.display = display; }

    public String getId()      { return id; }
    public String getDisplay() { return display; }

    public static Race fromId(String id) {
        for (Race r : values()) if (r.id.equalsIgnoreCase(id)) return r;
        return null;
    }

    public static Race random() {
        Race[] v = values();
        return v[(int)(Math.random() * v.length)];
    }
}
