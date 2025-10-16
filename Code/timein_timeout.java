import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime checkIn = null;

        double totalSeconds = 0;

        while(true){
            System.out.println("Type Check in");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("check in")){
                if(checkIn != null){
                    System.out.println("You checked in at:"+ checkIn.format(formatter));
                }else{
                    checkIn = LocalDateTime.now();
                    System.out.println("Check In Time:" + checkIn.format(formatter));
                }

            }
            else if (input.equals("check out")){
                if(checkIn == null){
                    System.out.println("Use need to check in first");
                    continue;
                }
                LocalDateTime checkOut = LocalDateTime.now();
                System.out.println("Check In Time:" + checkOut.format(formatter));
                if(checkOut.isBefore(checkIn)){
                    System.out.println("Check out time cannot be before check in time");
                }
                else{
                    Duration duration = Duration.between(checkIn, checkOut);

                    long seconds = duration.getSeconds();

                    totalSeconds += seconds;

                    double totalHours = totalSeconds/ 3600.0;
                    System.out.printf("Total Accumulated Time: %.2f hours %n", totalHours);
                }
                checkIn = null;
            }
            else if(input.equals("exit")){
                System.out.println("Exit Program");
                break;
            }
            else{
                System.out.println("Invalid input");
            }
        }
        scanner.close();

    }
}
