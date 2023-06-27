import javax.sql.rowset.spi.SyncResolver;
import java.util.*;
public class Raffle {
    int groupNumber=0;
    double potSize = 0;
    static int choice =0;
    String name;
    int numberOfTickets = 0;
    ArrayList<Integer> purchasedTickets;
    static ArrayList<Integer> winningTickets;
    private static Map<String, Integer> ticketCounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    public void mainMenu(String status) {
        System.out.println(status);
        System.out.println("[1] Start a New Draw");
        System.out.println("[2] Buy Tickets");
        System.out.println("[3] Run Raffle");
    }

    private Set<Integer> generateWinningTicket() {
        Set<Integer> winningTicket = new HashSet<>();
        Random random = new Random();
        while (winningTicket.size() < 5) {
            int number = random.nextInt(15) + 1;
            winningTicket.add(number);
        }
        return winningTicket;
    }
    private Map<String, Integer> identifyWinners(Set<Integer> winningTicket) {
        Map<String, Integer> winners = new HashMap<>();

        for (Map.Entry<String, Integer> entry : ticketCounts.entrySet()) {
            String attendee = entry.getKey();
            int numTickets = entry.getValue();
            int numMatchingNumbers = getNumMatchingNumbers(winningTicket, numTickets);

            if (numMatchingNumbers >= 1) {
                winners.put(attendee, numMatchingNumbers);
            }
        }

        return winners;
    }
    private int getNumMatchingNumbers(Set<Integer> winningTicket, int numTickets) {
        int numMatchingNumbers = 0;

        for (int i = 0; i < numTickets; i++) {
            Set<Integer> ticketNumbers = generateTicketNumbers();

            for (int number : ticketNumbers) {
                if (winningTicket.contains(number)) {
                    numMatchingNumbers++;
                    break;
                }
            }
        }
        return numMatchingNumbers;
    }
    private Set<Integer> generateTicketNumbers() {
        Set<Integer> ticketNumbers = new HashSet<>();
        Random random = new Random();

        while (ticketNumbers.size() < 5) {
            int number = random.nextInt(15) + 1;
            ticketNumbers.add(number);
        }

        return ticketNumbers;
    }
    private void displayWinners(Map<String, Integer> winners) {
        System.out.println("Group "+groupNumber+" winners:");

        for (Map.Entry<String, Integer> entry : winners.entrySet()) {
            String attendee = entry.getKey();
            int numMatchingNumbers = entry.getValue();
            double reward = calculateReward(numMatchingNumbers);
            System.out.println(attendee + " with "+numMatchingNumbers+" winning ticket(s)- $" + reward);
        }
    }
    private double calculateReward(int numMatchingNumbers) {
        double rewardPercentage;

        switch (numMatchingNumbers) {
            case 1:
                rewardPercentage = 10.0;
                break;
            case 2:
                rewardPercentage = 25.0;
                break;
            case 3:
                rewardPercentage = 50.0;
                break;
            case 4:
                rewardPercentage = 100.0;
                break;
            default:
                rewardPercentage = 0.0;
        }

        return potSize * (rewardPercentage / 100.0);
    }
    public void startNewDraw()
    {
        ticketCounts.clear();
        potSize = potSize + 100;
        System.out.println("New Raffle draw has been started. Initial pot size: $" + potSize +
                "\nPress any key to return to main menu");
        //new Scanner(System.in);
        scanner.nextLine(); //Clear the newline character from the previous input
        scanner.nextLine(); // Wait for any key press
        mainMenu("Welcome to My Raffle App \nStatus: Draw is ongoing. Raffle pot size is $" + potSize);
    }
    public void buyTickets()
    {
        System.out.print("Enter your name,");
        Scanner name_ticket = new Scanner(System.in);
        System.out.print(" no of tickets to purchase");
        name = name_ticket.nextLine();
        numberOfTickets = name_ticket.nextInt();
         /*Each ticket should consist of 5 unique randomly generated numbers between [1-15] as mentioned in the
         Ticket Structure.*/
        ticketCounts.put(name, ticketCounts.getOrDefault(name, 0) + numberOfTickets);
        System.out.println("Hi " + name + ", you have purchased " + numberOfTickets + " ticket(s)-");
        for (int t = 1; t <= numberOfTickets; t++) {
            purchasedTickets = new ArrayList<Integer>();
            for (int i = 1; i <= 15; i++) {
                purchasedTickets.add(i);
            }
            Collections.shuffle(purchasedTickets);
            //generate
            System.out.print("Ticket " + t + ": ");
            for (int j = 0; j < 5; j++) {
                System.out.print(purchasedTickets.get(j) + " ");
            }
            System.out.print("\n");
            potSize = potSize + (t * 5);
        }
        System.out.println("Press any key to return to main menu");
        scanner.nextLine(); //Clear the newline character from the previous input
        scanner.nextLine(); // Wait for any key press
        mainMenu("Welcome to My Raffle App \nStatus: Draw is ongoing. Raffle pot size is $" + potSize);
    }
    private void runRaffle() {
        groupNumber++;
        System.out.println("Running raffle...");
        // Generate the winning ticket
        Set<Integer> winningTicket = generateWinningTicket();
        System.out.print("Winning Ticket Numbers: ");
        for (int number : winningTicket) {
            System.out.print(number + " ");
        }
        System.out.println();
        // Match purchased tickets against the winning ticket and identify the winners
        Map<String, Integer> winners = identifyWinners(winningTicket);
        // Calculate rewards and display the winners
        displayWinners(winners);
        //potSize = 0.0;
        ticketCounts.clear();
        System.out.println("Press any key to return to main menu");
        scanner.nextLine(); //Clear the newline character from the previous input
        scanner.nextLine(); // Wait for any key press
        mainMenu("Welcome to My Raffle App \nStatus: Status: Draw has not started. Initial pot size is $" + potSize);
    }
    public void enterChoice()
    {
        choice = scanner.nextInt();
    }

    public static void main(String[] args) {
        boolean exit = false;
        //console app that displays the following options (aka main menu) to users-
        //System.out.println("Welcome to My Raffle App \n");
        Raffle raf = new Raffle();
        raf.mainMenu("Welcome to My Raffle App \nStatus: Draw has not started \n");
        choice = scanner.nextInt();
        while (!exit) {
            switch (choice) {
                case 1:
                    raf.startNewDraw();
                    raf.enterChoice();
                    break;
                case 2:
                    raf.buyTickets();
                    raf.enterChoice();
                    break;
                case 3:
                    raf.runRaffle();
                    raf.enterChoice();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    exit = true;
                    break;
            }
        }
    }

}