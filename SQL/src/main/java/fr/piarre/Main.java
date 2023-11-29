package fr.piarre;

import fr.piarre.Exceptions.Auth.UserNotFoundException;
import fr.piarre.Exceptions.Auth.WrongPasswordException;
import fr.piarre.Managers.DatabaseManager;
import fr.piarre.Managers.UserManager;
import fr.piarre.Models.LoginReturn;
import fr.piarre.Models.User;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) throws SQLException, UserNotFoundException, WrongPasswordException {
        Scanner scanner = new Scanner(System.in);


        Statement statement = new DatabaseManager().connect();
        UserManager userManager = new UserManager(statement);
        User loggedUser = null;
        boolean isLogged = false;

        System.out.println("Welcome to the Lemoine app!");
        System.out.println("Please login or register to continue.\n");


        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit\n");

        System.out.print("Please enter your choice:");
        String choice = scanner.nextLine();
        System.out.println();
        switch (choice) {
            case "1":
                while (!isLogged) {
                    String email = null;
                    User user = null;

                    while (user == null) {
                        System.out.print("Please enter your email: ");
                        email = scanner.nextLine();
                        user = userManager.GetByEmail(email);

                        if (user == null) {
                            System.out.println("\nUser not found. Please try again.");
                        }
                    }

                    System.out.print("Please enter your password: ");
                    String password = scanner.nextLine();

                    LoginReturn loginReturn = userManager.Login(new User(email, password));

                    if (loginReturn == LoginReturn.WRONG_PASSWORD) {
                        System.out.println("\nWrong password. Please try again.");
                    } else {
                        System.out.println("\nLogin successful!\n");
                        isLogged = true;
                        loggedUser = user;
                    }
                }
                loggedMenu(statement, userManager, loggedUser);
                break;
            case "2":
                break;
            case "3":
                System.out.println("Exiting the Lemoine app. Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
                break;
        }
    }

    public static void loggedMenu(Statement statement, UserManager userManager, User loggedUser) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("You can now:");
        System.out.println("1. Add new user");
        System.out.println("2. List all users");
        System.out.println("3. Update a user");
        System.out.println("4. Delete a user");
        System.out.println("5. Exit\n");

        System.out.print("Please enter your choice:");
        String choice = scanner.nextLine();
        System.out.println();

        while (true) {
            switch (choice) {
                case "1":
                    String email = null;
                    String password = null;

                    System.out.println("Creating new user:");
                    System.out.print("Please enter your email: ");
                    email = scanner.nextLine();
                    System.out.print("Please enter your password: ");
                    password = scanner.nextLine();

                    if (email == null) {
                        System.out.println("Email cannot be null.");
                        System.out.print("Please enter your email: ");
                        email = scanner.nextLine();
                    } else if (password == null) {
                        System.out.println("Password cannot be null.");
                        System.out.print("Please enter your password: ");
                        password = scanner.nextLine();
                    } else if (!Pattern.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", email)) {
                        System.out.println("Email is not valid.");
                        break;
                    } else if (email.length() > 255) {
                        System.out.println("Email is too long.");
                        System.out.print("Please enter your email: ");
                        email = scanner.nextLine();
                    } else if (password.length() > 255) {
                        System.out.println("Password is too long.");
                        System.out.print("Please enter your password: ");
                        password = scanner.nextLine();
                    }

                    userManager.AddUser(new User(email, password), loggedUser);
                    choice = scanner.nextLine();
                    break;
                case "2":
                    userManager.GetAllUsers();
                    choice = scanner.nextLine();
                    break;
                case "5":
                    System.out.println("Exiting the Lemoine app. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.print("Invalid choice. Please enter a valid option.");
                    choice = scanner.nextLine();
                    break;
            }
        }
    }
}