package CLI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class Terminal {

    private Parser parser;
    private Path path;
    private ArrayList<String> history = new ArrayList<String>();

    public Terminal() {
        parser = new Parser();
        path = Paths.get(System.getProperty("user.dir"));
    }


    public String echo(String[] args) {
        if (args[args.length - 1].matches("([A-Z]:){0,1}[a-zA-Z0-9/]+.txt") && args[args.length - 2].equals(">")){
            try {
                writeToFile(String.join(" ", Arrays.copyOfRange(args, 0, args.length-2)), args[args.length - 1], false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (args[args.length - 1].matches("[/a-zA-Z0-9]+.txt") && args[args.length - 2].equals(">>")){
            try {
                writeToFile(String.join(" ", Arrays.copyOfRange(args, 0, args.length-2)), args[args.length - 1], true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println(String.join(" ", args));
        }
        return String.join(" ", args);
    }

    public String pwd() {
        return path.toString();
    }


    public void cd(String path) {
        
        if (path.equals(".")) {
            return;
        }
        Path pathToCheck = this.path.resolve(path);
        if (path.equals("..")) {
            Path parent = this.path.getParent();
            if (parent != null) {
                this.path = parent;
                System.setProperty("user.dir", parent.toString());
            }
            else {
                System.out.println("You are in root directory");
            }
        }
        else if (Files.isDirectory(pathToCheck)) {
            if (Files.exists(pathToCheck)) {
                this.path = pathToCheck;
                System.setProperty("user.dir", this.path.toString());

            } else {
                System.out.println("Path is not exist");

            }
        }
        else {
            System.out.println("It's not a directory");
        }


    }

    public void cd() {
    this.path = Paths.get(System.getProperty("user.dir"));
    }

    public void ls(String[] args) {
        File file = new File(pwd());
        String[] files = file.list();
        Arrays.sort(files);
        String output = "";
        for (String name : files) {
            output += name + '\n';
        }
        
        if (args.length > 0) {
            if (args[args.length - 1].matches("([A-Z]:){0,1}[/a-zA-Z0-9]+.txt") && args[args.length - 2].equals(">")) {
                try {
                    writeToFile(output, args[args.length - 1], false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (args[args.length - 1].matches("([A-Z]:){0,1}[/a-zA-Z0-9]+.txt")
                    && args[args.length - 2].equals(">>")) {
                try {
                    writeToFile(output, args[args.length - 1], true);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } 
        else{
            System.out.println(output);
        }
    }

    public void  lsr(String[] args) {
        File file = new File(pwd());
        String[] files = file.list();
        Arrays.sort(files, Collections.reverseOrder());
        String output = "";
        for (String name : files) {
            output += name + '\n';
        }
        if (args[args.length - 1].matches("([A-Z]:){0,1}[/a-zA-Z0-9]+.txt") && args[args.length - 2] == ">"){
            try {
                writeToFile(output, args[args.length - 1], false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (args[args.length - 1].matches("([A-Z]:){0,1}[/a-zA-Z0-9]+.txt") && args[args.length - 2] == ">>"){
            try {
                writeToFile(output, args[args.length - 1], true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println(output);
        }
    }

    

    public void makeDir(String[] args) {
        for (String argument : args) {
            File directory = new File(argument);
            if (!directory.exists()) {
                if (directory.mkdir()) {
                } else {
                    System.out.println("Error creating directory");
                }
            } else {
                System.out.println("Directory already exists: " + argument);
            }
        }
    }



    public void rmdir(String arg) {
        File directory;

        if (Objects.equals(arg, "*")) {
            directory = new File(pwd());
            if (!directory.exists() || !directory.isDirectory()) {
                System.out.println("directory not found");
                return;
            }

            File[] files = directory.listFiles();

            for (File file : files) {
                if (file.isDirectory() && file.listFiles().length == 0) {
                    file.delete();
                }
            }
        } else {
            directory = new File(arg);
            if (directory.exists() && directory.isDirectory()) {
                if (directory.listFiles().length == 0) {
                    directory.delete();
                } else {
                    System.out.println("directory not empty");
                }
            } else {
                System.out.println("directory not found");
            }
        }
    }



    public void touch(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                System.out.println("File already exists.");
            } else {
                File parentDirectory = file.getParentFile();
                if (parentDirectory != null && !parentDirectory.exists()) {
                    parentDirectory.mkdirs();
                }

                if (file.createNewFile()) {
                    System.out.println("File created: " + path);
                } else {
                    System.out.println("Failed to create the file: " + path);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public void cp(String src, String dest) throws IOException {
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
    
    public void cpr(String src, String dest) throws IOException {
        File sourceDir = new File(src);
        File destDir = new File(dest);

        // Check if the source directory exists
        if (!sourceDir.exists()) {
            System.out.println("Source directory doesn't exist.");
            return;
        }

        // Create the destination directory if it doesn't exist
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        // List all files and subdirectories in the source directory
        File[] files = sourceDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // If it's a directory, recursively copy it
                    cpr(file.getAbsolutePath(), destDir.getAbsolutePath() + File.separator + file.getName());
                } else {
                    // If it's a file, copy it to the destination directory
                    try (InputStream in = new FileInputStream(file)) {
                        try (OutputStream out = new FileOutputStream(
                                destDir.getAbsolutePath() + File.separator + file.getName())) {
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                        }
                    }
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

    public void cat(String firstPath, String secondPath) throws IOException{
        File file1 = new File(firstPath);
        File file2 = new File(secondPath);
        if (!file2.exists()){
            System.out.println("SecondPath: " + secondPath + " doesn't exist");
            return;
        }
        if (!file1.exists()){
            System.out.println("FilePath: " + firstPath + " doesn't exist");
            return;
        }
        try(FileInputStream inputStream = new FileInputStream(file1)){
            byte[] buf = new byte[1024];
            while (inputStream.read(buf) > 0) {
                System.out.println(new String(buf, StandardCharsets.UTF_8));
            }
        }
        try(FileInputStream inputStream = new FileInputStream(file2)){
            byte[] buf = new byte[1024];
            while (inputStream.read(buf) > 0) {
                System.out.println(new String(buf, StandardCharsets.UTF_8));
            }
        }
    }

    public void wc(String path) throws IOException{
        File file = new File(path);
        if (!file.exists()){
            System.out.println("FilePath: " + path + " doesn't exist");
            return;
        }
        try(FileInputStream inputStream = new FileInputStream(file)){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            int nLines = 0, nWords = 0, nChars = 0;
            while ((line = reader.readLine()) != null){
                nLines++;
                String[] words = line.split("\\s+");
                nWords += words.length;
                nChars += line.length();
            }
            System.out.println(nLines + " " + nWords + " " + nChars + " " + file.getName());
        }
    }

    public void writeToFile(String output, String path, boolean append) throws FileNotFoundException{
        File file = new File(path);
        if (!file.exists()){
            touch(path);
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file, append);
            outputStream.write(output.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void history() {
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + " " + history.get(i));
        }
    }

    public void run() throws IOException{
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

    public void chooseCommandAction() throws IOException {
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
            if (!args[0].equals("-r")) {
                ls(args);
                this.history.add("ls");
                break;
            }
            else{
                lsr(args);
                this.history.add("ls");
                break;
            }
            case "mkdir":
                makeDir(args);
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
                if (args.length == 2) {
                    cp(args[0], args[1]);
                    history.add("cp");
                    break;
                }
                else if (args.length == 3 && args[0].equals("-r")) {
                    cpr(args[1], args[2]);
                    history.add("cp");
                    break;
                }
                else {
                    System.out.println("Command not found");
                    break;
                }
            case "rm":
                rm(args[0]);
                history.add("rm");
                break;
            case "cat":
                if (args.length == 2) {
                    cat(args[0], args[1]);
                    history.add("cat");
                    break;
                }
                else {
                    System.out.println("Command not found");
                    break;
                }
                
            case "wc":
                try {
                    wc(args[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
