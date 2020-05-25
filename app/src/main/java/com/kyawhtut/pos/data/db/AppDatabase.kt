package com.kyawhtut.pos.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kyawhtut.pos.data.db.dao.*
import com.kyawhtut.pos.data.db.entity.*

@Database(
    entities = [
        UserEntity::class,
        RoleEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        CustomerEntity::class,
        SellEntity::class,
        TicketEntity::class,
        CartHeaderEntity::class,
        CartEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun roleDao(): RoleDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun customerDao(): CustomerDao
    abstract fun sellDao(): SellDao
    abstract fun ticketDao(): TicketDao
    abstract fun cartDao(): CartDao
}

fun provideDB(context: Context): AppDatabase = Room.databaseBuilder(
    context,
    AppDatabase::class.java,
    "POS.db"
).fallbackToDestructiveMigration()
    .allowMainThreadQueries()
    .build()

fun provideUserDao(db: AppDatabase) = db.userDao()

fun provideRoleDao(db: AppDatabase) = db.roleDao()

fun provideCategoryDao(db: AppDatabase) = db.categoryDao()

fun provideProductDao(db: AppDatabase) = db.productDao()

fun provideCustomerDao(db: AppDatabase) = db.customerDao()

fun provideSellDao(db: AppDatabase) = db.sellDao()

fun provideTicketDao(db: AppDatabase) = db.ticketDao()

fun provideCartDao(db: AppDatabase) = db.cartDao()