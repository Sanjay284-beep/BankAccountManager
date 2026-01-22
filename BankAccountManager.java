
import java.util.*;

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class BankAccount {
    private String accountNumber;
    private String accountHolder;
    protected double balance;
    
    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }
    
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }
    
    public void deposit(double amount) {
        if(amount <= 0) {
            System.out.println("Invalid amount!");
            return;
        }
        balance += amount;
        System.out.printf("Deposited: Rs.%.2f | Balance: Rs.%.2f\n", amount, balance);
    }
    
    public void withdraw(double amount) throws InsufficientFundsException {
        if(amount <= 0) {
            System.out.println("Invalid amount!");
            return;
        }
        if(amount > balance) {
            throw new InsufficientFundsException("Insufficient funds! Available: Rs." + balance);
        }
        balance -= amount;
        System.out.printf("Withdrawn: Rs.%.2f | Balance: Rs.%.2f\n", amount, balance);
    }
    
    public void displayInfo() {
        System.out.println("\n========== ACCOUNT INFO ==========");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolder);
        System.out.printf("Balance: Rs.%.2f\n", balance);
        System.out.println("Type: " + getAccountType());
        System.out.println("==================================\n");
    }
    
    public String getAccountType() {
        return "Regular Account";
    }
}

class SavingsAccount extends BankAccount {
    private double interestRate = 4.5;
    
    public SavingsAccount(String accountNumber, String accountHolder, double initialBalance) {
        super(accountNumber, accountHolder, initialBalance);
    }
    
    @Override
    public String getAccountType() {
        return "Savings Account";
    }
    
    public void addInterest() {
        double interest = balance * interestRate / 100;
        balance += interest;
        System.out.printf("Interest added: Rs.%.2f (%.1f%%)\n", interest, interestRate);
    }
}

class CurrentAccount extends BankAccount {
    private double overdraftLimit = 5000;
    
    public CurrentAccount(String accountNumber, String accountHolder, double initialBalance) {
        super(accountNumber, accountHolder, initialBalance);
    }
    
    @Override
    public String getAccountType() {
        return "Current Account";
    }
    
    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if(amount <= 0) {
            System.out.println("Invalid amount!");
            return;
        }
        if(amount > (balance + overdraftLimit)) {
            throw new InsufficientFundsException("Insufficient funds! Available: Rs." + (balance + overdraftLimit));
        }
        balance -= amount;
        System.out.printf("Withdrawn: Rs.%.2f | Balance: Rs.%.2f", amount, balance);
        if(balance < 0) System.out.print(" (Using overdraft)");
        System.out.println();
    }
}

class BankingSystem {
    private ArrayList<BankAccount> accounts;
    
    public BankingSystem() {
        accounts = new ArrayList<>();
    }
    
    public void addAccount(BankAccount account) {
        accounts.add(account);
        System.out.println("Account created successfully!");
    }
    
    public BankAccount findAccount(String accountNumber) {
        for(BankAccount acc : accounts) {
            if(acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null;
    }
    
    public void displayAllAccounts() {
        if(accounts.isEmpty()) {
            System.out.println("No accounts found!");
            return;
        }
        System.out.println("\n========== ALL ACCOUNTS ==========");
        for(BankAccount acc : accounts) {
            System.out.printf("%-10s | %-20s | Rs.%-10.2f | %s\n", 
                acc.getAccountNumber(), acc.getAccountHolder(), 
                acc.getBalance(), acc.getAccountType());
        }
        System.out.println("==================================\n");
    }
}

public class BankAccountManager {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankingSystem bank = new BankingSystem();
        
        System.out.println("======================================");
        System.out.println("   BANK ACCOUNT MANAGER SYSTEM");
        System.out.println("======================================\n");
        
        while(true) {
            System.out.println("1. Create Savings Account");
            System.out.println("2. Create Current Account");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Check Balance");
            System.out.println("6. Add Interest (Savings)");
            System.out.println("7. View All Accounts");
            System.out.println("8. Exit");
            System.out.print("\nChoice: ");
            
            int choice = sc.nextInt();
            sc.nextLine();
            
            try {
                switch(choice) {
                    case 1:
                        System.out.print("Account Number: ");
                        String accNo = sc.nextLine();
                        System.out.print("Name: ");
                        String name = sc.nextLine();
                        System.out.print("Initial Deposit: ");
                        double amount = sc.nextDouble();
                        bank.addAccount(new SavingsAccount(accNo, name, amount));
                        break;
                        
                    case 2:
                        System.out.print("Account Number: ");
                        accNo = sc.nextLine();
                        System.out.print("Name: ");
                        name = sc.nextLine();
                        System.out.print("Initial Deposit: ");
                        amount = sc.nextDouble();
                        bank.addAccount(new CurrentAccount(accNo, name, amount));
                        break;
                        
                    case 3:
                        System.out.print("Account Number: ");
                        accNo = sc.nextLine();
                        BankAccount acc = bank.findAccount(accNo);
                        if(acc == null) {
                            System.out.println("Account not found!");
                        } else {
                            System.out.print("Amount: ");
                            amount = sc.nextDouble();
                            acc.deposit(amount);
                        }
                        break;
                        
                    case 4:
                        System.out.print("Account Number: ");
                        accNo = sc.nextLine();
                        acc = bank.findAccount(accNo);
                        if(acc == null) {
                            System.out.println("Account not found!");
                        } else {
                            System.out.print("Amount: ");
                            amount = sc.nextDouble();
                            try {
                                acc.withdraw(amount);
                            } catch(InsufficientFundsException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        }
                        break;
                        
                    case 5:
                        System.out.print("Account Number: ");
                        accNo = sc.nextLine();
                        acc = bank.findAccount(accNo);
                        if(acc == null) {
                            System.out.println("Account not found!");
                        } else {
                            acc.displayInfo();
                        }
                        break;
                        
                    case 6:
                        System.out.print("Account Number: ");
                        accNo = sc.nextLine();
                        acc = bank.findAccount(accNo);
                        if(acc == null) {
                            System.out.println("Account not found!");
                        } else if(acc instanceof SavingsAccount) {
                            ((SavingsAccount) acc).addInterest();
                        } else {
                            System.out.println("Only for Savings Account!");
                        }
                        break;
                        
                    case 7:
                        bank.displayAllAccounts();
                        break;
                        
                    case 8:
                        System.out.println("Thank you!");
                        sc.close();
                        return;
                        
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch(InputMismatchException e) {
                System.out.println("Invalid input!");
                sc.nextLine();
            }
            System.out.println();
        }
    }
}
