package com.mcmiddleearth.themedbuild.exception;

public class NoCurrentThemeException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Themed-builds are not set up correctly. You need to start a chain of themes.";
    }
}
