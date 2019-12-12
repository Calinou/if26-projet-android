package fr.utt.if26_projet;

import androidx.room.TypeConverter;
import fr.utt.if26_projet.Transaction.Account;
import fr.utt.if26_projet.Transaction.Category;

class Converters {

  // Cache the list of accounts and categories for performance
  private static Account[] accounts = Account.values();
  private static Category[] categories = Category.values();

  @TypeConverter
  public static Account toAccount(int value) {
    return accounts[value];
  }

  @TypeConverter
  public static int fromAccount(Account account) {
    return account.getId();
  }

  @TypeConverter
  public static Category toCategory(int value) {
    return categories[value];
  }

  @TypeConverter
  public static int fromCategory(Category category) {
    return category.getId();
  }
}
