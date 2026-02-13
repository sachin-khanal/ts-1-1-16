import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import utilities.Cipher;

public class FileHandler {

    private static final String filesFolder = "data";
    //variable that stores file name for data.

    public List<String> getFiles() {
    //method to get files in data folder

        List<String> fileNames = new ArrayList<>();
        Path folderPath = Paths.get(filesFolder);

        if (!(Files.exists(folderPath)) || !(Files.isDirectory(folderPath))) {
            throw new RuntimeException("folder not found");
        }//check if file exists

        try{
            File folder = folderPath.toFile();
            //convert path to folder
            File[] files = folder.listFiles();
            //store files in array.

            if (files != null) {//make sure files arent empty
                for(File file : files ){
                    if(file.isFile()){
                        fileNames.add(file.getName());
                    }
                }//add files to List.
            }
        }
        catch (Exception e) {
            System.out.println("error reading data folder" + e.getMessage());
        }
        //trycatch block to catch any errors while getting files from data folder

        return fileNames;
    }

    public String readFile(String fileName) {
        String fileContents = "";
        File file = new File(filesFolder + File.separator + fileName);
        //create file object for targetted file. get file path with File.seperator.

        if (!file.exists()) {
            throw new RuntimeException("file not found");
        }//if file is missing throw error

        try{
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            //objects for line by line reading

            String line = "";
            //string to store each line of the file


            while((line = bufferedReader.readLine()) != null){
                fileContents += line;
                fileContents += "\n";
            }//read each line in file per iteration and adds line to the string. go to new line with \n

            bufferedReader.close();
            //close file reader.


        }
        catch (IOException e){
            System.out.println("error reading the file" + e.getMessage());
        }
        //use try-catch block to catch any errors while reading file so code doesn't just crash

        if(fileName.toLowerCase().endsWith(".cip")){
            try{
                Cipher cipher = new Cipher();
                fileContents = cipher.decrypt(fileContents);
                //set the string to decrypted contents
            }
            catch (RuntimeException e){
                System.out.println("error decrypting file" + e.getMessage());
            }
        }//if the file ends with cip, decrypt it first.


        return fileContents;
        //return file as a string
    }

}
