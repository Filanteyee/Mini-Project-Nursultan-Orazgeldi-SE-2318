import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Project", "postgres", "1234");
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                System.out.println("\nChoose operation:");
                System.out.println("1. Create player");
                System.out.println("2. Read players");
                System.out.println("3. Update player");
                System.out.println("4. Delete player");

                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        createPlayer(conn, scanner);
                        break;
                    case 2:
                        readPlayers(conn);
                        break;
                    case 3:
                        updatePlayer(conn, scanner);
                        break;
                    case 4:
                        deletePlayer(conn, scanner);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

    private static void createPlayer(Connection conn, Scanner scanner)
            throws SQLException {
        System.out.println("Enter name:");
        String name = scanner.nextLine();
        System.out.println("Enter surname:");
        String surname = scanner.nextLine();
        System.out.println("Enter club:");
        String club = scanner.nextLine();
        System.out.println("Enter position:");
        String position = scanner.nextLine();

        String sql = "INSERT INTO players1 (name, surname, club, position) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setString(3, club);
            ps.setString(4, position);
            ps.executeUpdate();
            System.out.println("Player created successfully.");
        }
    }

    private static void readPlayers(Connection conn)
            throws SQLException {
        String sql = "SELECT * FROM players1";
        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Surname: %s, Club: %s, Position: %s%n",
                        rs.getInt("plrid"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("club"),
                        rs.getString("position"));
                System.out.println("______________________________________________________");
            }
        }
    }

    private static void updatePlayer(Connection conn, Scanner scanner)
            throws SQLException {
        System.out.println("Enter player ID to update:");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter new name:");
        String name = scanner.nextLine();
        System.out.println("Enter new surname:");
        String surname = scanner.nextLine();
        System.out.println("Enter new club:");
        String club = scanner.nextLine();
        System.out.println("Enter new position:");
        String position = scanner.nextLine();

        String sql = "UPDATE players1 SET name=?, surname=?, club=?, position=? WHERE plrid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, surname);
            ps.setString(3, club);
            ps.setString(4, position);
            ps.setInt(5, id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Player updated successfully.");
            } else {
                System.out.println("No player found with ID " + id);
            }
        }
    }

    private static void deletePlayer(Connection conn, Scanner scanner)
            throws SQLException {
        System.out.println("Enter player ID to delete:");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM players1 WHERE plrid=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Player deleted successfully.");
            } else {
                System.out.println("No player found with ID " + id);
            }
        }
    }
}
