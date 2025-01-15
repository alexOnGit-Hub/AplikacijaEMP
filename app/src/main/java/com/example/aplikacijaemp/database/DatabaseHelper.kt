
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.aplikacijaemp.database.Address
import com.example.aplikacijaemp.database.CartItem
import com.example.aplikacijaemp.database.StoreItem

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTableQuery = """
            CREATE TABLE User (
                email TEXT PRIMARY KEY,
                postalCode TEXT,
                street TEXT,
                city TEXT
            )
        """
        db.execSQL(createUserTableQuery)

        val createStoreItemTableQuery = """
            CREATE TABLE StoreItem (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                price REAL NOT NULL
            )
        """
        db.execSQL(createStoreItemTableQuery)

        val createCartTableQuery = """
            CREATE TABLE Cart (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userEmail TEXT NOT NULL,
                storeItemId INTEGER NOT NULL,
                quantity INTEGER NOT NULL,
                FOREIGN KEY(userEmail) REFERENCES User(email),
                FOREIGN KEY(storeItemId) REFERENCES StoreItem(id)
            )
        """
        db.execSQL(createCartTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Cart")
        db.execSQL("DROP TABLE IF EXISTS StoreItem")
        db.execSQL("DROP TABLE IF EXISTS User")
        onCreate(db)
    }

    fun insertUser(email: String, postalCode: String? = null, street: String? = null, city: String? = null): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("email", email)
            put("postalCode", postalCode)
            put("street", street)
            put("city", city)
        }
        return db.insert("User", null, values)
    }

    fun getUserByEmail(email: String): Map<String, Any?>? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM User WHERE email = ?", arrayOf(email))
        return if (cursor.moveToFirst()) {
            val user = mapOf(
                "email" to cursor.getString(cursor.getColumnIndexOrThrow("email")),
                "postalCode" to cursor.getString(cursor.getColumnIndexOrThrow("postalCode")),
                "street" to cursor.getString(cursor.getColumnIndexOrThrow("street")),
                "city" to cursor.getString(cursor.getColumnIndexOrThrow("city"))
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }
    fun deleteStoreItemById(storeItemId: Int): Int {
        val db = writableDatabase
        val selection = "id = ?"
        val selectionArgs = arrayOf(storeItemId.toString())
        return db.delete("StoreItem", selection, selectionArgs)
    }

    fun removeAllCartItemsForUser(userEmail: String): Int {
        val db = writableDatabase
        val selection = "userEmail = ?"
        val selectionArgs = arrayOf(userEmail)
        return db.delete("Cart", selection, selectionArgs)
    }



    fun insertStoreItem(item: StoreItem): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", item.name)
            put("price", item.price)
        }
        return db.insert("StoreItem", null, values)
    }

    fun updateUserAddress(email: String, street: String, city: String, postalCode: String): Int {

        val db = writableDatabase

        if(street.isEmpty() || city.isEmpty() || postalCode.isEmpty()){
            return 1
        }

        val values = ContentValues().apply {
            street?.let { put("street", it) }
            city?.let { put("city", it) }
            postalCode?.let { put("postalCode", it) }
        }

        if (values.size() == 0) {
            return 0
        }

        return db.update(
            "User", values, "email = ?", arrayOf(email)
        )
    }

    fun getUserAddress(email: String): Address? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT street, city, postalCode FROM User WHERE email = ?", arrayOf(email))

        return if (cursor.moveToFirst()) {
            val street = cursor.getString(cursor.getColumnIndexOrThrow("street"))
            val city = cursor.getString(cursor.getColumnIndexOrThrow("city"))
            val postalCode = cursor.getString(cursor.getColumnIndexOrThrow("postalCode"))
            cursor.close()

            Address(street, city, postalCode)
        } else {
            cursor.close()
            null
        }
    }


    fun getAllStoreItems(): List<StoreItem> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM StoreItem", null)
        val items = mutableListOf<StoreItem>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
            items.add(StoreItem(id, name, price))
        }
        cursor.close()
        return items
    }

    fun addToCart(userEmail: String, storeItemId: Int, quantity: Int): Long {
        val db = writableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM Cart WHERE userEmail = ? AND storeItemId = ?",
            arrayOf(userEmail, storeItemId.toString())
        )

        return if (cursor.moveToFirst()) {
            val existingQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
            val newQuantity = existingQuantity + quantity
            val values = ContentValues().apply {
                put("quantity", newQuantity)
            }
            db.update("Cart", values, "userEmail = ? AND storeItemId = ?", arrayOf(userEmail, storeItemId.toString())).toLong()
        } else {
            val values = ContentValues().apply {
                put("userEmail", userEmail)
                put("storeItemId", storeItemId)
                put("quantity", quantity)
            }
            db.insert("Cart", null, values)
        }.also {
            cursor.close()
        }
    }

    fun removeFromCart(userEmail: String, storeItemId: Int): Int {
        val db = writableDatabase
        val selection = "userEmail = ? AND storeItemId = ?"
        val selectionArgs = arrayOf(userEmail, storeItemId.toString())
        return db.delete("Cart", selection, selectionArgs)
    }



    fun getAllCartItemsForUser(userEmail: String): List<CartItem> {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            """
            SELECT Cart.id, StoreItem.name, StoreItem.price, Cart.quantity 
            FROM Cart 
            INNER JOIN StoreItem ON Cart.storeItemId = StoreItem.id
            WHERE Cart.userEmail = ?
            """, arrayOf(userEmail)
        )
        val cartItems = mutableListOf<CartItem>()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
            cartItems.add(CartItem(id, name, price, quantity))
        }
        cursor.close()
        return cartItems
    }

    companion object {
        private const val DATABASE_NAME = "StoreDatabase.db"
        private const val DATABASE_VERSION = 4
    }
}
