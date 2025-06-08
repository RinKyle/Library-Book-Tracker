/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Locqhz
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection conn;

    public BookDAO() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "#4lpha*Bet4");
    }

    public List<Book> getAllBooks() throws SQLException {
        List<Book> list = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM books");
        while (rs.next()) {
            list.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("genre")));
        }
        return list;
    }

    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, genre) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, book.getTitle());
        pst.setString(2, book.getAuthor());
        pst.setString(3, book.getGenre());
        pst.executeUpdate();
    }

    public void updateBook(Book book) throws SQLException {
        String sql = "UPDATE books SET title=?, author=?, genre=? WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, book.getTitle());
        pst.setString(2, book.getAuthor());
        pst.setString(3, book.getGenre());
        pst.setInt(4, book.getId());
        pst.executeUpdate();
    }

    public void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        pst.executeUpdate();
    }

    public List<Book> searchBooks(String keyword) throws SQLException {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR genre LIKE ?";
        PreparedStatement pst = conn.prepareStatement(sql);
        String kw = "%" + keyword + "%";
        pst.setString(1, kw);
        pst.setString(2, kw);
        pst.setString(3, kw);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            list.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("genre")));
        }
        return list;
    }
}
//Updates