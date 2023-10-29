package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Terminal {

    private Parser parser;
    private ArrayList<String> history = new ArrayList<String>();

    public void echo(String[] args) {
        System.out.println(String.join(" ", args));
    }


    public String pwd() {
        return System.getProperty("user.dir");
    }

    public void cd(String path) {
        System.setProperty("user.dir", path);
    }

    public void ls() {
        System.out.println("ls");
    }

    public void makeDir(String path) {
        System.out.println("mkdir " + path);
    }

    public void rmdir(String path) {
        System.out.println("rmdir " + path);
    }

    public void touch(String path) {
        System.out.println("touch " + path);
    }

    public void cp(String path1, String path2) {
        System.out.println("cp " + path1 + " " + path2);
    }

    public void rm(String path) {
        System.out.println("rm " + path);
    }
    
    public void cat(String path) {
        System.out.println("cat " + path);
    }
    
    public void wc(String path) {
        System.out.println("wc " + path);
    }

    public void history() {
        for (int i = 1; i <= history.size(); i++) {
            System.out.println(i + " " + history.get(i));
        }  
    }


    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        parser = new Parser();
        while (true) {
            System.out.print(pwd() + " $ ");
            try {
                input = reader.readLine();
            } catch (IOException e) {
                System.out.println("Error reading input");
                continue;
            }
            if (input.equals("exit")) {
                exit();
                break;
            }
            if (!parser.parse(input)) {
                System.out.println("Error parsing input");
                continue;
            }
            chooseCommandAction();
        }
    }

    public void exit() {
        System.out.println("exit");
    }

    public void chooseCommandAction() {
        String command = parser.getCommandName();
        String[] args = parser.getArgs();
        switch (command) {
            case "echo":
                echo(args);
                this.history.add("echo");
                break;
            case "pwd":
                System.out.println(pwd());
                this.history.add("pwd");
                break;
            case "cd":
                cd(args[0]);
                this.history.add("cd");
                break;
            case "ls":
                ls();
                this.history.add("ls");
                break;
            case "mkdir":
                makeDir(args[0]);
                break;
            case "rmdir":
                rmdir(args[0]);
                history.add("rmdir");
                break;
            case "touch":
                touch(args[0]);
                history.add("touch");
                break;
            case "cp":
                cp(args[0], args[1]);
                break;
            case "rm":
                rm(args[0]);
                history.add("rm");
                break;
            case "cat":
                cat(args[0]);
                history.add("cat");
                break;
            case "wc":
                wc(args[0]);
                history.add("wc");
                break;
            case "history":
                history();
                history.add("history");
                break;
            default:
                System.out.println("Command not found");
        }

    }
    

}
