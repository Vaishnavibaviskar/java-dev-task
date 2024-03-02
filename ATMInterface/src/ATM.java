import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ATM<users> {
    private Map<String, User> users;

    public <users> ATM() {
        users = new HashMap<>();
        // Initialize users with some dummy data
        users.put("user1", new User("user1", "1234", 1000));
        users.put("user2", new User("user2", "5678", 2000));
    }

    public boolean authenticateUser(String userID, String userPIN) {
        if (users.containsKey(userID)) {
            User user = users.get(userID);
            return user.getUserPIN().equals(userPIN);
        }
        return false;
    }

    public double checkBalance(String userID) {
        if (users.containsKey(userID)) {
            return users.get(userID).getAccountBalance();
        }
        return -1; // User not found
    }

    public boolean withdraw(String userID, double amount) {
        if (users.containsKey(userID)) {
            User user = users.get(userID);
            if (user.getAccountBalance() >= amount) {
                user.setAccountBalance(user.getAccountBalance() - amount);
                return true; // Withdrawal successful
            }
        }
        return false; // Insufficient funds or user not found
    }

    public void deposit(String userID, double amount) {
        if (users.containsKey(userID)) {
            User user = users.get(userID);
            user.setAccountBalance(user.getAccountBalance() + amount);
        }
    }

    public static void main(String[] args) {
        ATM atm = new ATM(); // Create an instance of the ATM
        Scanner scanner = new Scanner(System.in);

        // Prompt for user ID and PIN
        System.out.print("Enter User ID: ");
        String userID = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String userPIN = scanner.nextLine();

        // Authenticate user
        if (atm.authenticateUser(userID, userPIN)) {
            System.out.println("Authentication successful!");
            boolean quit = false;
            while (!quit) {
                System.out.println("\nATM Menu:");
                System.out.println("1. Check Balance");
                System.out.println("2. Withdraw");
                System.out.println("3. Deposit");
                System.out.println("4. Quit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("Current Balance: $" + atm.checkBalance(userID));
                        break;
                    case 2:
                        System.out.print("Enter amount to withdraw: $");
                        double withdrawAmount = scanner.nextDouble();
                        if (atm.withdraw(userID, withdrawAmount))
                            System.out.println("Withdrawal successful.");
                        else
                            System.out.println("Withdrawal failed. Insufficient funds.");
                        break;
                    case 3:
                        System.out.print("Enter amount to deposit: $");
                        double depositAmount = scanner.nextDouble();
                        atm.deposit(userID, depositAmount);
                        System.out.println("Deposit successful.");
                        break;
                    case 4:
                        quit = true;
                        System.out.println("Thank you for using the ATM.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } else {
            System.out.println("Authentication failed. Please try again.");
        }
        scanner.close();
    }
}
