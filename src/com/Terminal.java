package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;


public class Terminal {

    private Parser parser;
    private Queue<String> history;

    public void echo(String[] args) {
        System.out.println("echo " + String.join(" ", args));
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

    public Queue<String> history() {
        System.out.println("history");
        return null;
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

    }
    

}
