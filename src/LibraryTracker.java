/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Locqhz
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class LibraryTracker extends JFrame {
    private JTextField txtTitle, txtAuthor, txtGenre, txtSearch;
    private JTable table;
    private DefaultTableModel model;
    private BookDAO dao;

    public LibraryTracker() {
        try {
            dao = new BookDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            System.exit(1);
        }

        initComponents();
        loadBooks();
    }

    private void initComponents() {
        setTitle("Library Book Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelTop = new JPanel(new GridLayout(2, 5, 5, 5));
        txtTitle = new JTextField();
        txtAuthor = new JTextField();
        txtGenre = new JTextField();
        txtSearch = new JTextField();

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnSearch = new JButton("Search");

        panelTop.add(new JLabel("Title"));
        panelTop.add(new JLabel("Author"));
        panelTop.add(new JLabel("Genre"));
        panelTop.add(new JLabel("Search"));
        panelTop.add(new JLabel(""));

        panelTop.add(txtTitle);
        panelTop.add(txtAuthor);
        panelTop.add(txtGenre);
        panelTop.add(txtSearch);
        panelTop.add(btnSearch);

        add(panelTop, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Genre"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelBottom = new JPanel();
        panelBottom.add(btnAdd);
        panelBottom.add(btnUpdate);
        panelBottom.add(btnDelete);
        add(panelBottom, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addBook());
        btnUpdate.addActionListener(e -> updateBook());
        btnDelete.addActionListener(e -> deleteBook());
        btnSearch.addActionListener(e -> searchBooks());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtTitle.setText(model.getValueAt(row, 1).toString());
                txtAuthor.setText(model.getValueAt(row, 2).toString());
                txtGenre.setText(model.getValueAt(row, 3).toString());
            }
        });
    }

    private void loadBooks() {
        try {
            model.setRowCount(0);
            List<Book> books = dao.getAllBooks();
            for (Book b : books) {
                model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getGenre()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Load failed: " + e.getMessage());
        }
    }

    private void addBook() {
        try {
            Book b = new Book(0, txtTitle.getText(), txtAuthor.getText(), txtGenre.getText());
            dao.addBook(b);
            loadBooks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Add failed: " + e.getMessage());
        }
    }

    private void updateBook() {
        try {
            int row = table.getSelectedRow();
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            Book b = new Book(id, txtTitle.getText(), txtAuthor.getText(), txtGenre.getText());
            dao.updateBook(b);
            loadBooks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
        }
    }

    private void deleteBook() {
        try {
            int row = table.getSelectedRow();
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            dao.deleteBook(id);
            loadBooks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Delete failed: " + e.getMessage());
        }
    }

    private void searchBooks() {
        try {
            model.setRowCount(0);
            List<Book> books = dao.searchBooks(txtSearch.getText());
            for (Book b : books) {
                model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getGenre()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Search failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryTracker().setVisible(true));
    }
}
//Updates