package CLI;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Terminal {

    private Parser parser;
    private Path path;
    private ArrayList<String> history = new ArrayList<String>();
    public Terminal() {
        path = Paths.get(System.getProperty("user.dir"));
    }


    public void echo(String[] args) {
        System.out.println(String.join(" ", args));
    }

    public String pwd() {
        return path.toString();
    }


    public void cd(String path) {
        System.setProperty("user.dir", path);
        if (path.equals(".")) {
            return;
        }
        Path pathToCheck = this.path.resolve(path);
        if (path.equals("..")) {
            Path parent = this.path.getParent();
            if (parent != null) {
                this.path = parent;
            }
            else {
                System.out.println("You are in root directory");
            }
        }
        else if (Files.isDirectory(pathToCheck)) {
            if (Files.exists(pathToCheck)) {
                this.path = pathToCheck;

            } else {
                System.out.println("Path is not exist");

            }
        }
        else {
            System.out.println("It's not a directory");
        }


    }

    public void cd() {
        this.path = Path.of(System.getProperty("user.dir"));
    }

    public void ls() {
        System.out.println("ls");
    }

    public void makeDir(String path) {

    }


    public void rmdir(String path) {
        System.out.println("rmdir " + path);
    }

    public void touch(String path) {
        System.out.println("touch " + path);
    }

    public void cp(String src, String dest)throws  IOException {
        File file = new File(src);
        if (!file.exists()) {
            System.out.println("File doesn't exist");
            return;
        }
        File file2 = new File(dest);
        if (!file2.exists()) {
            file2.createNewFile();
        }
        try (FileInputStream in = new FileInputStream(file)) {
            try (FileOutputStream out = new FileOutputStream(file2)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }




    public Boolean rm(String Filename) {
        String currentDirectory = pwd();
        File file = new File (currentDirectory , Filename);
        if (file.exists()){
            file.delete();
            System.out.println("The file is deleted successfully");
            return true;
        }
        else {
            System.out.println("The file is not in the current directory");
            return false;
        }
    }

    public void cat(String path) {
        System.out.println("cat " + path);
    }

    public void wc(String path) {
        System.out.println("wc " + path);
    }

    public void history() {
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + " " + history.get(i));
        }
    }

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;
        parser = new Parser();
        while (true) {
            System.out.print(pwd() + " $");
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
                if (args.length == 0) {
                    cd();
                    this.history.add("cd");
                    break;
                }
                else {
                    cd(args[0]);
                    this.history.add("cd");
                    break;
                }
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
                try {
                    cp(args[0],args[1]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
