package main.com.syos.repository;

import main.com.syos.model.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDAO {
    // Inserts a new item record into the database
    void createItem(Item item) throws SQLException;

    // Retrieves an Item by its unique code.
    Item findByCode(String code) throws SQLException;

    // Fetches all items in the catalog.
    List<Item> findAll() throws SQLException;

    // Updates the unit price of the specified item.
    void updatePrice(String code, double newPrice) throws SQLException;

    // Adjusts the reorder level threshold for stock alerts.
    void updateReorderLevel(String code, int newLevel) throws SQLException;

    // Performs a logical delete or deactivation of an item record.
    void deleteItem(String code) throws SQLException;
}
