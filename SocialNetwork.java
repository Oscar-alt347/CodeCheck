import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SocialNetwork {

    // Define User class to hold user details
    static class User {
        String username;
        String displayName;
        String state;
        List<String> friends;

        // Constructor for User
        User(String username, String displayName, String state, List<String> friends) {
            this.username = username;
            this.displayName = displayName;
            this.state = state;
            this.friends = friends;
        }
    }

    // Define Post class to hold post details
    static class Post {
        String postId;
        String userId;
        String visibility;

        // Constructor for Post
        Post(String postId, String userId, String visibility) {
            this.postId = postId;
            this.userId = userId;
            this.visibility = visibility;
        }
    }

    // Lists to store all users and posts
    static List<User> users = new ArrayList<>();
    static List<Post> posts = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            // Display menu options
            System.out.println("1. Load input data");
            System.out.println("2. Check visibility");
            System.out.println("3. Retrieve posts");
            System.out.println("4. Search users by location");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    loadUserData(scanner);
                    loadPostData(scanner);
                    System.out.println("Data loaded successfully.");
                    break;
                case "2":
                    checkVisibility(scanner);
                    break;
                case "3":
                    retrievePosts(scanner);
                    break;
                case "4":
                    searchUsersByLocation(scanner);
                    break;
                case "5":
                    // Exit the loop and program
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        } while (!choice.equals("5"));
    }

    // Method to load user data from a specified file
    private static void loadUserData(Scanner scanner) throws IOException {
        System.out.print("Enter path to user data file: ");
        String path = scanner.nextLine();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(";");
            if (tokens.length < 4) continue;
            String username = tokens[0];
            String displayName = tokens[1];
            String state = tokens[2];
            List<String> friends = Arrays.asList(tokens[3].substring(1, tokens[3].length() - 1).split(", "));
            users.add(new User(username, displayName, state, friends));
        }
    }

    // Method to load post data from a specified file
    private static void loadPostData(Scanner scanner) throws IOException {
        System.out.print("Enter path to post data file: ");
        String path = scanner.nextLine();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(";");
            if (tokens.length < 3) continue;
            String postId = tokens[0];
            String userId = tokens[1];
            String visibility = tokens[2];
            posts.add(new Post(postId, userId, visibility));
        }
    }

    // Method to check the visibility of a post given a post ID and a username
    private static void checkVisibility(Scanner scanner) {
        System.out.print("Enter post ID: ");
        String postId = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        Optional<Post> post = posts.stream().filter(p -> p.postId.equals(postId)).findFirst();
        if (!post.isPresent()) {
            System.out.println("Post not found.");
            return;
        }
        if (post.get().visibility.equals("public")) {
            System.out.println("Access Permitted");
            return;
        }
        Optional<User> user = users.stream().filter(u -> u.username.equals(username)).findFirst();
        if (!user.isPresent()) {
            System.out.println("User not found.");
            return;
        }
        if (user.get().friends.contains(post.get().userId)) {
            System.out.println("Access Permitted");
        } else {
            System.out.println("Access Denied");
        }
    }

    // Method to retrieve posts accessible to a specific user
    private static void retrievePosts(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        Optional<User> user = users.stream().filter(u -> u.username.equals(username)).findFirst();
        if (!user.isPresent()) {
            System.out.println("User not found.");
            return;
        }
        List<String> accessiblePosts = posts.stream()
            .filter(p -> p.visibility.equals("public") || p.userId.equals(username) || user.get().friends.contains(p.userId))
            .map(p -> p.postId)
            .collect(Collectors.toList());
        System.out.println("Accessible posts:");
        accessiblePosts.forEach(System.out::println);
    }

    // Method to search for users by state location
    private static void searchUsersByLocation(Scanner scanner) {
        System.out.print("Enter state: ");
        String state = scanner.nextLine();
        List<String> matchingUsers = users.stream()
            .filter(u -> u.state.equals(state))
            .map(u -> u.displayName)
            .collect(Collectors.toList());
        if (!matchingUsers.isEmpty()) {
            System.out.println("Users in " + state + ":");
            matchingUsers.forEach(System.out::println);
        } else {
            System.out.println("No users found in " + state + ".");
        }
    }
}
