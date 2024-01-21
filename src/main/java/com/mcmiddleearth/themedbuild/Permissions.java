package com.mcmiddleearth.themedbuild;

public enum Permissions {

    MANAGER     ("themedbuilds.manager"),
    MODERATOR   ("themedbuilds.moderator"),
    VIEWER      ("themedbuilds.viewer"),
    VOTER       ("themedbuilds.voter"),
    RATER       ("themedbuilds.rater"),
    STAR_VOTER  ("themedbuilds.voter.star"),
    BUILDER     ("themedbuilds.builder");

    private final String permissionNode;

    Permissions(String permissionNode) {
        this.permissionNode = permissionNode;
    }

    public String getNode() {
        return permissionNode;
    }
}

