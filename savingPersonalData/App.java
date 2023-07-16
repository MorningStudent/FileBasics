package savingPersonalData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException {
        
        Scanner in = new Scanner(System.in);

        System.out.print("Please enter your name, last name, age and your rating: ");
        String name     = in.next();
        String lastName = in.next();
        String fullName = name + " " + lastName;
        String age      = in.next();
        String raiting  = in.next();

        System.out.println();
        System.out.print("Do you want to delete the information at the end of session? Answer with Yes or No! ");
        String answer = in.next();

        File file = new File("name.txt");
        if(file.exists()) {
            System.out.println("File found!");
        } else {
            System.out.println("File not found!");
            file.createNewFile();
            System.out.println("Such file is created!");
        }

        System.out.println("Hi " + fullName + "!");

        FileWriter fw = new FileWriter(file);
        fw.write(fullName + " " + age + " "  + raiting);
        fw.close();   // <-- when using fw.flush() it keeps the file opened and the Windows locks the file so we cann't delete
                      // it while it's locked, therefore we need to close all the operators that maitain the file openned

        if(answer.equals("Yes") || answer.equals("yes") || answer.equals("Y") || answer.equals("y")) {
            if(file.delete()) {
                System.out.println("Information deleted!");
            } else {
                System.out.println("Information deleting failed!");
            };
        } else if (answer.equals("No") || answer.equals("no") || answer.equals("N") || answer.equals("n"));

    }
}