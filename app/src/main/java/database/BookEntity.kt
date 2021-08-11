package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="Books")
data class BookEntity(
    @PrimaryKey @ColumnInfo(name = "book_id") val bookId: Int,
    @ColumnInfo(name = "book_name") val bookName: String,
    @ColumnInfo(name = "book_author")val bookAuthor: String,
    @ColumnInfo(name = "book_rating")val bookRating: String,
    @ColumnInfo(name = "book_price")val bookPrice: String,
    @ColumnInfo(name = "book_desc")val bookDesc: String,
    @ColumnInfo(name = "book_image")val bookImage: String
)