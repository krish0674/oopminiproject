import java.util.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;

class UserAuthentication {
    private String username;
    private String password;

    // Constructor to initialize the username and password
    public UserAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Method to authenticate the user
    public boolean authenticateUser(String enteredUsername, String enteredPassword) {
        return username.equals(enteredUsername) && password.equals(enteredPassword);
    }
}

class FinancialManager {
    private ArrayList<Double> incomes;
    private ArrayList<Double> expenses;
    private Map<String, ArrayList<Double>> categorizedExpenses; // New data structure for categorized expenses

    public FinancialManager() {
        this.incomes = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.categorizedExpenses = new HashMap<>();
    }

    // Modified method to record income with return type
    public String recordIncome(double amount) {
        incomes.add(amount);
        return "Income of $" + amount + " recorded successfully.";
    }

    // Modified method to record expense with return type
     public String recordExpense(double amount, String category) {
        expenses.add(amount);
        categorizedExpenses.putIfAbsent(category, new ArrayList<>());
        categorizedExpenses.get(category).add(amount);
        return "Expense of $" + amount + " for " + category + " recorded successfully.";
        private void addFinancialControls(GridPane grid, int col, int row) {
    // ... other controls setup ...

    // Add a label and text field for the expense category
    Label categoryLabel = new Label("Category:");
    grid.add(categoryLabel, col, row + 3);
    TextField categoryField = new TextField();
    grid.add(categoryField, col + 1, row + 3);

    // Adjust the position for the record button
    Button recordButton = new Button("Record Transaction");
    grid.add(recordButton, col + 1, row + 4); // Row position adjusted

    // ... other controls ...

    recordButton.setOnAction(e -> {
        try {
            double incomeAmount = Double.parseDouble(incomeField.getText());
            String incomeMessage = financialManager.recordIncome(incomeAmount);
            showAlert("Income Recorded", incomeMessage);

            // Get the category text
            String category = categoryField.getText();
            double expenseAmount = Double.parseDouble(expenseField.getText());
            String expenseMessage = financialManager.recordExpense(expenseAmount, category);
            showAlert("Expense Recorded", expenseMessage);

            // Optionally clear the fields after recording
            incomeField.clear();
            expenseField.clear();
            categoryField.clear();

            // Update summary display
            String summaryMessage = financialManager.displaySummary();
            showAlert("Financial Summary", summaryMessage);
        } catch (NumberFormatException ex) {
            showAlert("Invalid Input", "Please enter valid numeric values for income and expense.");
        }
    });

    }
    // Method to display income and expense summary with return type
    public String displaySummary() {
        double totalIncome = incomes.stream().mapToDouble(Double::doubleValue).sum();
        double totalExpense = expenses.stream().mapToDouble(Double::doubleValue).sum();
        double balance = totalIncome - totalExpense;

        return String.format("Total Income: $%.2f\nTotal Expense: $%.2f\nBalance: $%.2f", totalIncome, totalExpense, balance);
    }

    // Additional methods to provide access to the lists of incomes and expenses
    public ArrayList<Double> getIncomes() {
        return new ArrayList<>(incomes); // Return a copy to prevent external modification
    }

    public ArrayList<Double> getExpenses() {
        return new ArrayList<>(expenses); // Return a copy to prevent external modification
    }

    // Method to get the total income
    public double getTotalIncome() {
        return incomes.stream().mapToDouble(Double::doubleValue).sum();
    }

    // Method to get the total expense
    public double getTotalExpense() {
        return expenses.stream().mapToDouble(Double::doubleValue).sum();
    }

    // Method to get the current balance
    /**
     * @return
     */
   public double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }
       public Map<String, Double> getCategoryExpensesMap() {
        Map<String, Double> categoryExpensesMap = new HashMap<>();
        for (Map.Entry<String, ArrayList<Double>> entry : categorizedExpenses.entrySet()) {
            double total = entry.getValue().stream().mapToDouble(Double::doubleValue).sum();
            categoryExpensesMap.put(entry.getKey(), total);
        }
        return categoryExpensesMap;
    }
}

class Transaction {
    private Date date;
    private String description;
    private double amount;

    // Constructor to initialize a transaction
    public Transaction(String description, double amount) {
        this.date = new Date();
        this.description = description;
        this.amount = amount;
    }

    // Method to get a formatted string representation of the transaction
    public String getFormattedTransaction() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Date: " + dateFormat.format(date) + "\nDescription: " + description + "\nAmount: $" + amount;
    }
}

class TransactionHistoryManager {
    private ArrayList<Transaction> transactions;

    // Constructor to initialize the transaction history list
    public TransactionHistoryManager() {
        this.transactions = new ArrayList<>();
    }

    // Method to record a transaction and return its formatted representation
    public String recordTransaction(String description, double amount) {
        Transaction transaction = new Transaction(description, amount);
        transactions.add(transaction);
        return transaction.getFormattedTransaction();
    }

    // Method to get a formatted string representation of the transaction history
    public String getTransactionHistory() {
        StringBuilder history = new StringBuilder("Transaction History:\n");
        for (Transaction transaction : transactions) {
            history.append(transaction.getFormattedTransaction()).append("\n------------------------\n");
        }
        return history.toString();
    }
}

class BudgetManager {
    private Map<String, Double> budgets;

    // Constructor to initialize the budget map
    public BudgetManager() {
        this.budgets = new HashMap<>();
    }

    // Method to set a budget for a specific category
    public void setBudget(String category, double amount) {
        budgets.put(category, amount);
        System.out.println("Budget set successfully for " + category + ": $" + amount);
    }

    // Method to track spending against budgets
    public void trackSpending(String category, double spentAmount) {
    if (budgets.containsKey(category)) {
        double budgetAmount = budgets.get(category);
        double remainingBudget = budgetAmount - spentAmount;

        // Update the budget value
        budgets.put(category, remainingBudget);

        System.out.println("Remaining budget for " + category + ": $" + remainingBudget);
    } else {
        System.out.println("No budget set for " + category + ". Consider setting a budget.");
    }
}

    public double getBudget(String category) {
        return budgets.getOrDefault(category, 0.0);
    }
}

class FinancialReportGenerator {
     private FinancialManager financialManager;

    // Constructor to initialize with a FinancialManager instance
    public FinancialReportGenerator(FinancialManager financialManager) {
        this.financialManager = financialManager;
    }

    // Method to generate an income vs. expense summary
   public String generateIncomeExpenseSummary() {
        double totalIncome = financialManager.getTotalIncome();
        double totalExpense = financialManager.getTotalExpense();
        double netIncome = totalIncome - totalExpense;

        StringBuilder summary = new StringBuilder();
        summary.append("Income vs. Expense Summary:\n");
        summary.append("Total Income: $").append(String.format("%.2f", totalIncome)).append("\n");
        summary.append("Total Expense: $").append(String.format("%.2f", totalExpense)).append("\n");
        summary.append("Net Income: $").append(String.format("%.2f", netIncome));

        return summary.toString();
    }

    // Method to generate category-wise spending report
    public String generateCategorySpendingReport() {
        // Placeholder logic
        StringBuilder report = new StringBuilder();
        report.append("Category-wise Spending Report:\n");

        // Assume you have a list of categories
        List<String> categories = Arrays.asList("CategoryA", "CategoryB", "CategoryC");

        for (String category : categories) {
            // Replace the following line with your logic to calculate spending for each
            // category
            double categorySpending = calculateCategorySpending(category);
            report.append(category).append(": $").append(categorySpending).append("\n");
        }

        return report.toString();
    }

    // Method to generate budget comparison report
  public String generateBudgetComparisonReport(BudgetManager budgetManager) {
        StringBuilder report = new StringBuilder();
        report.append("Budget Comparison Report:\n");

        Map<String, Double> categoryExpenses = financialManager.getCategoryExpensesMap();
        for (String category : categoryExpenses.keySet()) {
            double budget = budgetManager.getBudget(category);
            double actualSpending = categoryExpenses.get(category);
            double remainingBudget = budget - actualSpending;

            report.append(category)
                .append(": Budget: $").append(String.format("%.2f", budget))
                .append(", Actual Spending: $").append(String.format("%.2f", actualSpending))
                .append(", Remaining Budget: $").append(String.format("%.2f", remainingBudget))
                .append("\n");
        }

        return report.toString();
    }

    // Placeholder method to calculate category spending
    private double calculateCategorySpending(String category) {
        // Replace this with your actual logic to calculate spending for the given
        // category
        // For example, you might iterate over transactions and sum the amounts for the
        // category.
        return 0.0;
    }

    // Placeholder method to get budget for a category
    private double getBudgetForCategory(String category) {
        // Replace this with your actual logic to get the budget for the given category
        // You might use the BudgetManager to get the budget for the category.
        return 0.0;
    }
}

class SavingsManager {
    private Map<String, Double> savings;

    // Constructor to initialize the savings map
    public SavingsManager() {
        this.savings = new HashMap<>();
    }

    // Method to record a savings or investment account
    public void recordSavings(String accountName, double amount, double interestRate, String maturityDate) {
        savings.put(accountName, amount);
        System.out.println("Savings recorded successfully: " + accountName +
                " - Amount: $" + amount + ", Interest Rate: " + interestRate + "%, Maturity Date: " + maturityDate);
    }

    // Method to display savings and investment details
    public void displaySavings() {
        System.out.println("Savings and Investments:");
        for (Map.Entry<String, Double> entry : savings.entrySet()) {
            System.out.println(entry.getKey() + " - Amount: $" + entry.getValue());
        }
    }
}

class Goal {
    private String description;
    private double targetAmount;
    private double currentAmount;

    // Constructor to initialize a financial goal
    public Goal(String description, double targetAmount) {
        this.description = description;
        this.targetAmount = targetAmount;
        this.currentAmount = 0.0;
    }

    // Method to record progress towards a goal
    public void recordProgress(double amount) {
        currentAmount += amount;
        System.out.println("Progress recorded towards " + description + ": $" + amount);
    }

    // Method to check if the goal is achieved
    public boolean isGoalAchieved() {
        return currentAmount >= targetAmount;
    }

    // Method to display goal details
    public void displayGoalDetails() {
        System.out.println("Goal: " + description);
        System.out.println("Target Amount: $" + targetAmount);
        System.out.println("Current Amount: $" + currentAmount);
    }
}

class GoalManager {
    private Map<String, Goal> goals;

    // Constructor to initialize the goals map
    public GoalManager() {
        this.goals = new HashMap<>();
    }

    // Method to set a financial goal
    public void setGoal(String goalName, String description, double targetAmount) {
        Goal goal = new Goal(description, targetAmount);
        goals.put(goalName, goal);
        System.out.println("Financial goal set successfully: " + goalName);
    }

    // Method to record progress towards a goal
    public void recordProgress(String goalName, double amount) {
        if (goals.containsKey(goalName)) {
            Goal goal = goals.get(goalName);
            goal.recordProgress(amount);
            if (goal.isGoalAchieved()) {
                System.out.println("Congratulations! You've achieved your financial goal: " + goalName);
            }
        } else {
            System.out.println("No goal set with the name: " + goalName);
        }
    }
}

class ExpenseAnalytics {
    private ArrayList<Double> expenses;
    private Map<String, Double> categoryExpenses;

    // Constructor to initialize expense lists and category expenses
    public ExpenseAnalytics() {
        this.expenses = new ArrayList<>();
        this.categoryExpenses = new HashMap<>();
    }

    // Method to record an expense
    public void recordExpense(double amount, String category) {
        expenses.add(amount);

        // Update category-wise expenses
        if (categoryExpenses.containsKey(category)) {
            double currentCategoryExpense = categoryExpenses.get(category);
            categoryExpenses.put(category, currentCategoryExpense + amount);
        } else {
            categoryExpenses.put(category, amount);
        }

        System.out.println("Expense recorded successfully: $" + amount + " in category " + category);
    }

    // Method to view detailed analytics on spending habits
    public void viewExpenseAnalytics() {
        System.out.println("Expense Analytics:");
        System.out.println("Total Expenses: $" + expenses.stream().mapToDouble(Double::doubleValue).sum());

        System.out.println("Category-wise Expenses:");
        for (Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
            System.out.println(entry.getKey() + ": $" + entry.getValue());
        }
    }
}

class CurrencyConverter {
    private Map<String, Double> exchangeRates;

    // Constructor to initialize the exchange rates
    public CurrencyConverter() {
        this.exchangeRates = new HashMap<>();
    }

    // Method to set exchange rates
    public void setExchangeRate(String currencyCode, double rate) {
        exchangeRates.put(currencyCode, rate);
        System.out.println("Exchange rate set successfully for " + currencyCode + ": " + rate);
    }

    // Method to convert an amount from one currency to another
    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        if (exchangeRates.containsKey(fromCurrency) && exchangeRates.containsKey(toCurrency)) {
            double fromRate = exchangeRates.get(fromCurrency);
            double toRate = exchangeRates.get(toCurrency);
            return amount * (toRate / fromRate);
        } else {
            System.out.println("Invalid currency codes. Please check the exchange rates.");
            return -1.0; // indicating an error
        }
    }
}

public class App extends Application {
    private UserAuthentication userAuth;
    private FinancialManager financialManager;
    private TransactionHistoryManager transactionHistoryManager;
    private BudgetManager budgetManager;
    private FinancialReportGenerator reportGenerator;
    private TextField usernameField;
private PasswordField passwordField;
private GridPane grid;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        userAuth = new UserAuthentication("Kshitij", "pass123");
        financialManager = new FinancialManager();
        transactionHistoryManager = new TransactionHistoryManager();
        budgetManager = new BudgetManager();
        reportGenerator = new FinancialReportGenerator(financialManager);


        primaryStage.setTitle("User Authentication Test");

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        

        // Add only authentication controls initially
        addAuthenticationControls(grid, 0, 0);

        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    private void addAuthenticationControls(GridPane grid, int col, int row) {
        Label authLabel = new Label("User Authentication");
        grid.add(authLabel, col, row, 2, 1);

        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, col, row + 1);

        usernameField = new TextField();
        grid.add(usernameField, col + 1, row + 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, col, row + 2);

        passwordField = new PasswordField();
        grid.add(passwordField, col + 1, row + 2);

        Button authButton = new Button("Authenticate");
        grid.add(authButton, col + 1, row + 3);

        authButton.setOnAction(e -> {
            String enteredUsername = usernameField.getText();
            String enteredPassword = passwordField.getText();

            if (userAuth.authenticateUser(enteredUsername, enteredPassword)) {
                // If authentication is successful, show the other controls
                addFinancialControls(grid, 0, 4);
                addTransactionHistoryControls(grid, 2, 0);
                addBudgetManagerControls(grid, 2, 5);
                addReportGeneratorControls(grid, 4, 0);

                showAlert("Authentication Successful", "Access granted.");

                // Remove authentication controls
                grid.getChildren().removeAll(authLabel, usernameLabel, usernameField, passwordLabel, passwordField, authButton);
            } else {
                showAlert("Authentication Failed", "Access denied.");
            }
        });
    }

    private void addFinancialControls(GridPane grid, int col, int row) {
        Label financialLabel = new Label("Financial Management");
        grid.add(financialLabel, col, row, 2, 1);

        Label incomeLabel = new Label("Income:");
        grid.add(incomeLabel, col, row + 1);

        TextField incomeField = new TextField();
        grid.add(incomeField, col + 1, row + 1);

        Label expenseLabel = new Label("Expense:");
        grid.add(expenseLabel, col, row + 2);

        TextField expenseField = new TextField();
        grid.add(expenseField, col + 1, row + 2);

        Button recordButton = new Button("Record Transaction");
        grid.add(recordButton, col + 1, row + 3);
        Button showSummaryButton = new Button("Show Summary");
    grid.add(showSummaryButton, col + 1, row + 4);

        recordButton.setOnAction(e -> {
            try {
                double incomeAmount = Double.parseDouble(incomeField.getText());
                double expenseAmount = Double.parseDouble(expenseField.getText());
        
                String incomeMessage = financialManager.recordIncome(incomeAmount);
                String expenseMessage = financialManager.recordExpense(expenseAmount);
        
                // Display summary
                showAlert("Transaction Recorded", incomeMessage + "\n" + expenseMessage);
                financialManager.displaySummary();
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter valid numeric values for income and expense.");
            }
        });
        showSummaryButton.setOnAction(e -> {
            // Display financial summary
            String summaryMessage = financialManager.displaySummary();
            showAlert("Financial Summary", summaryMessage);
        });
    }

    private void addTransactionHistoryControls(GridPane grid, int col, int row) {
        Label historyLabel = new Label("Transaction History");
        grid.add(historyLabel, col, row, 2, 1);

        Label descriptionLabel = new Label("Description:");
        grid.add(descriptionLabel, col, row + 1);

        TextField descriptionField = new TextField();
        grid.add(descriptionField, col + 1, row + 1);

        Label amountLabel = new Label("Amount:");
        grid.add(amountLabel, col, row + 2);

        TextField amountField = new TextField();
        grid.add(amountField, col + 1, row + 2);
        Button recordTransactionButton = new Button("Record Transaction");
        grid.add(recordTransactionButton, col + 1, row + 3);

        Button displayHistoryButton = new Button("Display Transaction History");
        grid.add(displayHistoryButton, col + 1, row + 4);

        recordTransactionButton.setOnAction(e -> {
            try {
                String description = descriptionField.getText();
                double amount = Double.parseDouble(amountField.getText());

                transactionHistoryManager.recordTransaction(description, amount);

                // Display recorded transaction in an alert
                showAlert("Transaction Recorded", "Description: " + description + "\nAmount: $" + amount);

            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid numeric value for amount.");
            }
        });

        displayHistoryButton.setOnAction(e -> {
            // Display transaction history in an alert
            showAlert("Transaction History", transactionHistoryManager.getTransactionHistory());
        });
        
    }

    private void addBudgetManagerControls(GridPane grid, int col, int row) {
        Label budgetLabel = new Label("Budget Management");
        grid.add(budgetLabel, col, row, 2, 1);

        Label categoryLabel = new Label("Category:");
        grid.add(categoryLabel, col, row + 1);

        TextField categoryField = new TextField();
        grid.add(categoryField, col + 1, row + 1);

        Label amountLabel = new Label("Amount:");
        grid.add(amountLabel, col, row + 2);

        TextField amountField = new TextField();
        grid.add(amountField, col + 1, row + 2);

        Button setBudgetButton = new Button("Set Budget");
        grid.add(setBudgetButton, col + 1, row + 3);

        Button trackSpendingButton = new Button("Track Spending");
        grid.add(trackSpendingButton, col + 1, row + 4);

        setBudgetButton.setOnAction(e -> {
            try {
                String category = categoryField.getText();
                double amount = Double.parseDouble(amountField.getText());

                budgetManager.setBudget(category, amount);
                showAlert("Budget Set", "Budget set successfully for " + category + ": $" + amount);
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid numeric value for amount.");
            }
        });

        trackSpendingButton.setOnAction(e -> {
            try {
                String category = categoryField.getText();
                double spentAmount = Double.parseDouble(amountField.getText());
        
                budgetManager.trackSpending(category, spentAmount);
        
                // Get the updated remaining budget
                double remainingBudget = budgetManager.getBudget(category);
        
                // Display the remaining budget in an alert
                showAlert("Spending Tracked", "Remaining budget for " + category + ": $" + remainingBudget);
        
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please enter a valid numeric value for spending amount.");
            }
        });
    }

    private void addReportGeneratorControls(GridPane grid, int col, int row) {
        // ... existing control setup ...

        Button budgetComparisonReportButton = new Button("Generate Budget Comparison Report");
        grid.add(budgetComparisonReportButton, col, row + 3);

        budgetComparisonReportButton.setOnAction(e -> {
            // Generate and display budget comparison report
            String report = reportGenerator.generateBudgetComparisonReport(budgetManager);
            showAlert("Budget Comparison Report", report);
        });
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        if (message != null && !message.isEmpty()) {
            alert.setContentText(message);
        } else {
            alert.setContentText("No message to display.");
        }
        alert.showAndWait();
    }
}