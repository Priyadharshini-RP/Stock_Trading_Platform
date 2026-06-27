import java.util.*;
class Stock {
    String name;
    double price;

    Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
class Transaction {
    String type, stock;
    int qty;
    double amount;

    Transaction(String type, String stock, int qty, double amount) {
        this.type = type;
        this.stock = stock;
        this.qty = qty;
        this.amount = amount;
    }
}
class User {
    String username, password;
    double balance;
    HashMap<String, Integer> portfolio = new HashMap<>();
    ArrayList<Transaction> history = new ArrayList<>();

    User(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }
}
class TradingSystem {
    ArrayList<User> users = new ArrayList<>();
    HashMap<String, Stock> market = new HashMap<>();
    Scanner sc = new Scanner(System.in);
    TradingSystem() {
        market.put("TCS", new Stock("TCS", 3500));
        market.put("INFY", new Stock("INFY", 1500));
        market.put("RELIANCE", new Stock("RELIANCE", 2800));
        market.put("WIPRO", new Stock("WIPRO", 450));
    }
    void register() {
        System.out.print("Enter Username: ");
        String u = sc.next();

        for (User x : users) {
            if (x.username.equalsIgnoreCase(u)) {
                System.out.println("Username already exists!");
                return;
            }
        }
        System.out.print("Enter Password: ");
        String p = sc.next();
        users.add(new User(u, p, 50000));
        System.out.println("Registration Successful!");
    }
    User login() {
        System.out.print("Username: ");
        String u = sc.next();
        System.out.print("Password: ");
        String p = sc.next();
        for (User x : users) {
            if (x.username.equals(u) && x.password.equals(p)) {
                System.out.println("Login Successful!");
                return x;
            }
        }
        System.out.println("Invalid Login!");
        return null;
    }
    void showMarket() {
        System.out.println("\n------ MARKET ------");
        for (Stock s : market.values())
            System.out.println(s.name + " : Rs" + s.price);
    }
    void buy(User user) {
        showMarket();
        System.out.print("Enter Stock Name: ");
        String name = sc.next().toUpperCase();
        if (!market.containsKey(name)) {
            System.out.println("Stock Not Found!");
            return;
        }
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        if (qty <= 0) {
            System.out.println("Invalid Quantity!");
            return;
        }
        Stock s = market.get(name);
        double cost = qty * s.price;
        if (cost > user.balance) {
            System.out.println("Insufficient Balance!");
            return;
        }
        user.balance -= cost;
        user.portfolio.put(name,
                user.portfolio.getOrDefault(name, 0) + qty);
        user.history.add(new Transaction("BUY", name, qty, cost));
        System.out.println("Stock Purchased Successfully!");
    }
        void sell(User user) {
        System.out.print("Enter Stock Name: ");
        String name = sc.next().toUpperCase();
        if (!user.portfolio.containsKey(name)) {
            System.out.println("You don't own this stock!");
            return;
        }
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        int owned = user.portfolio.get(name);
        if (qty <= 0 || qty > owned) {
            System.out.println("Invalid Quantity!");
            return;
        }
        Stock s = market.get(name);
        double amount = qty * s.price;
        user.balance += amount;
        if (owned == qty)
            user.portfolio.remove(name);
        else
            user.portfolio.put(name, owned - qty);
        user.history.add(new Transaction("SELL", name, qty, amount));
        System.out.println("Stock Sold Successfully!");
    }
    void portfolio(User user) {
        System.out.println("\n------ PORTFOLIO ------");
        double stockValue = 0;
        if (user.portfolio.isEmpty()) {
            System.out.println("No Stocks Purchased.");
        } else {
            for (String key : user.portfolio.keySet()) {
                int qty = user.portfolio.get(key);
                double value = qty * market.get(key).price;
                stockValue += value;
                System.out.println(key + "  Qty:" + qty +
                        "  Value: Rs" + value);
            }
        }
        System.out.println("Stock Value : Rs" + stockValue);
        System.out.println("Cash Balance: Rs" + user.balance);
        System.out.println("Net Worth   : Rs" + (stockValue + user.balance));
    }
    void history(User user) {
        System.out.println("\n------ TRANSACTION HISTORY ------");
        if (user.history.isEmpty()) {
            System.out.println("No Transactions.");
            return;
        }
        for (Transaction t : user.history)
            System.out.println(t.type + "  " + t.stock +
                    "  Qty:" + t.qty +
                    "  Amount: Rs" + t.amount);
    }
    void userMenu(User user) {
        while (true) {
            System.out.println("\n===== USER MENU =====");
            System.out.println("1.View Market");
            System.out.println("2.Buy Stock");
            System.out.println("3.Sell Stock");
            System.out.println("4.View Portfolio");
            System.out.println("5.Transaction History");
            System.out.println("6.Logout");
            int ch = sc.nextInt();
            switch (ch) {
                case 1:
                    showMarket();
                    break;
                case 2:
                    buy(user);
                    break;
                case 3:
                    sell(user);
                    break;
                case 4:
                    portfolio(user);
                    break;
                case 5:
                    history(user);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
    void start() {
        while (true) {
            System.out.println("\n===== STOCK TRADING PLATFORM =====");
            System.out.println("1.Register");
            System.out.println("2.Login");
            System.out.println("3.Exit");
            int ch = sc.nextInt();
            switch (ch) {
                case 1:
                    register();
                    break;
                case 2:
                    User user = login();
                    if (user != null)
                        userMenu(user);
                    break;
                case 3:
                    System.out.println("Thank You!");
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
public class TradingApp {
    public static void main(String[] args) {
        TradingSystem app = new TradingSystem();
        app.start();
    }
}