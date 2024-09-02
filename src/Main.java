import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static final String password = "xxxx";
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        while(1 > 0){ //Password Check
            System.out.print("Welcome to the Application Tracker! Please Enter your password : ");
            String passAttempt = scan.nextLine();
                if(passAttempt.equals(password)){
                    break;
            }
        }
        //What do I wanna do to my database, Subject to change if I make it HTML

        System.out.println("Welcome Cullen! What would you like to do?");
        System.out.println("1.Addition \n2.Deletion \n3.Find Job App \n4.Display Job Apps \n5.Exit");
        int action = scan.nextInt();
        if(action == 1){ // Adding
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.print("Company Name: ");
                String compName = br.readLine(); 
                
                System.out.print("Position: ");
                String position = br.readLine(); 
                
                System.out.print("Date: ");
                String date = br.readLine(); 
                
                System.out.print("Location: ");
                String location = br.readLine(); 
                
                System.out.print("Urgency: ");
                int urgency = Integer.parseInt(br.readLine()); 
                
                System.out.print("Site: ");
                String site = br.readLine(); 
                AddRow a = new AddRow();
                a.add(compName,position, date, location, urgency, site);
               System.out.println("Job : " + compName + " " + position + " " + date + " " + location + " " + urgency + " " + site );
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception, e.g., log it or notify the user
            } 

        } else if(action == 2){ //Deletion
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.print("Company Name: ");
                String compName = br.readLine(); 
                DeleteRow bye = new DeleteRow();
                bye.delete(compName);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception, e.g., log it or notify the user
            } 

        } else if(action == 3){ //Find Row
            try(BufferedReader brotha = new BufferedReader(new InputStreamReader(System.in))){
                System.out.print("Company Name? : ");
                String CompNam = brotha.readLine();
                FindRow hello = new FindRow();
                hello.findName(CompNam);
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception, e.g., log it or notify the user
            } 
        } else if(action == 4){
            PrintJobs a = new PrintJobs();
            a.print();
        }
    }
    
}
    
