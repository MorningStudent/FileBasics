package savingPersonalData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String name     = null;
        String lastName = null;
        int    age;
        double raiting;

        File file = new File("name.txt");

        if(file.exists()) {
            Scanner fin = new Scanner(file);
            System.out.println("File found!");
            name     = fin.next();
            lastName = fin.next();
            age      = fin.nextInt();
            raiting  = fin.nextDouble();
        } else {
            System.out.println("File not found!");
            file.createNewFile();
            System.out.println("Such file is created!");
            System.out.print("Please enter your name, last name, age and your rating: ");
            name     = in.next();
            lastName = in.next();
            age      = in.nextInt();
            raiting  = in.nextDouble();
            System.out.println();
            System.out.print("Do you want to delete the information at the end of session? Answer with Yes or No! ");
            String answer = in.next();
            FileWriter fw = new FileWriter(file);                         // A small guess
            fw.write(name + " " + lastName + " " + age + " "  + raiting); // <- Even though the "age" and "raiting" are primitive
                                                                          // int and double it still converts it into String?!
                                                                          // as I saw fw.write can accept only String, char and int...
            fw.close();

            if(answer.equals("Yes") || answer.equals("yes") || answer.equals("Y") || answer.equals("y")) {
                if(file.delete()) {
                    System.out.println("Information deleted!");
                } else {
                    System.out.println("Information deleting failed!");
                };
            } else if (answer.equals("No") || answer.equals("no") || answer.equals("N") || answer.equals("n"));

        }

        System.out.println("Hi " + name + " " + lastName  + "!");
    }

}