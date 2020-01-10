package read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ReadLine {
    public static final String ANSI_RED_TEXT = "\033[0;31m";
    public static final String BLACK_BOLD = "\033[1;30m";
    public static final String BLACK = "\033[0;30m";
    List<String> listRemoteRepositories = new ArrayList<String>();
    Map<String, List<String>> mySresNexusMap = new HashMap<String, List<String>>();
    Map<String, List<String>> myJarSourceMap = new HashMap<String, List<String>>();
    String src;
    String jar;

    public static void main(String[] args) {
        ReadLine readLine = new ReadLine();
        File file = new File("C:\\Users\\zi715e\\Test");
        readLine.listAllFiles(file);
        readLine.getListSresNexus(readLine.listRemoteRepositories);
        readLine.printRepositorySourceList();
        System.out.println("-----------------------------------------------------------------------------");
        readLine.printRepositoryList();
        readLine.getListJarSource(readLine.listRemoteRepositories);
        readLine.printgetListJarSource();
    }

    // It checks whether it is directory or file.
    // If it's directory it again calls itself(recursion),till it finds file.
    //If it's a file it calls checkRemoteRepository to check whether it is remote repository or not.
    // If it's _remote.repository then it reads it's content.
    public void listAllFiles(File folder) {
        File[] fileNames = folder.listFiles();
        for (File file : fileNames) {
            // if directory call the same method again
            if (file.isDirectory()) {
                listAllFiles(file);
            } else {
                if (checkRemoteRepository(file.getName()))
                    readContent(file);
            }
        }
    }

    //Checks Whether file is ._remote.repositories or not
    private boolean checkRemoteRepository(String fileName) {
        return fileName.equals("_remote.repositories");
    }

    //It reads the file line by line.
    //It calls extractPomWar(line); for each line
    private void readContent(File file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                extractPomWar(line);
                //listRemoteRepositories.add(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    //It add list of jar/pom lines in listRemoteRepositories List.
    private void extractPomWar(String line) {
        if (line.contains(">")) {
            listRemoteRepositories.add(line);
        }

    }

    //Creates a map of Source to jars
    private void getListSresNexus(List<String> listRemoteRepositories) {
        for (String line : listRemoteRepositories) {
            extractsource(line);
            if (!mySresNexusMap.containsKey(src)) {
                mySresNexusMap.put(src, new ArrayList<String>());
            }
            mySresNexusMap.get(src).add(jar);
        }
    }


    //creates a map jars to source
    private void getListJarSource(List<String> listRemoteRepositories){
        for (String line : listRemoteRepositories) {
            extractsource(line);
            if (!myJarSourceMap.containsKey(jar)) {
                myJarSourceMap.put(jar, new ArrayList<String>());
            }
            myJarSourceMap.get(jar).add(src);
        }
    }
    private void extractsource(String line) {
        int startIndex = line.indexOf(">");
        int endIndex = line.indexOf("=");
        src = line.substring(startIndex + 1, endIndex);
        jar = line.substring(0, startIndex);
    }

    private void printRepositorySourceList(){
        System.out.println(BLACK_BOLD + "List of Jar Source");
        for (Map.Entry mapElement : mySresNexusMap.entrySet()) {
            String key = (String) mapElement.getKey();
            System.out.println(BLACK + key);
        }
    }
    private void printRepositoryList() {
        for (Map.Entry mapElement : mySresNexusMap.entrySet()) {
            String key = (String) mapElement.getKey();
            System.out.println(BLACK_BOLD+ key);
            List<String> jars = (List<String>) mapElement.getValue();
            int counter = 1;
            for (String jar : jars) {
                System.out.println(BLACK + counter++ +". " + jar);
            }
            System.out.println("_________________________________________________________________");
        }
    }

    private void printgetListJarSource(){
        System.out.println("Jar-Source Report");
        int counter = 1;
        for (Map.Entry mapElement : myJarSourceMap.entrySet()) {
            String key = (String) mapElement.getKey();
            System.out.println(BLACK + counter++ +". " +key);
            List<String> srcs = (List<String>) mapElement.getValue();
            for (String src : srcs) {
                if(srcs.size()>1)
                    System.out.println(ANSI_RED_TEXT + src);
                else
                    System.out.println(BLACK + src);
            }
            System.out.println("_________________________________________________________________");
        }
    }
}