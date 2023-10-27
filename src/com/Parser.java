package com;

public class Parser {
    private String commandName;
    private String[] args;

    public boolean parse(String input) {
        String[] parts = input.split(" ");
        if (parts.length == 0) {
            return false;
        }
        commandName = parts[0];
        args = new String[parts.length - 1];
        for (int i = 1; i < parts.length; i++) {
            args[i - 1] = parts[i];
        }
        return true;
    }

    public String getCommandName() {

        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}
