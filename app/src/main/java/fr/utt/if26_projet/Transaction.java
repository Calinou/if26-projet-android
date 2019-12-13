package fr.utt.if26_projet;

import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.os.Build.VERSION_CODES;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "transactions")
public class Transaction implements Serializable {

  /** The transaction's unique identifier. */
  @PrimaryKey(autoGenerate = true)
  private int id;

  /** The amount of the transaction (in cents). */
  private int amount;

  /** The transaction date (stored as an UNIX timestamp). */
  private int date;

  /** The account the transaction was made with. */
  private Account account;

  /** The category the transaction belongs to. */
  private Category category;

  /** An one-line description of the transaction (shown on the list of transactions). */
  @NonNull private String contents;

  /** A multi-line description of the transaction (not shown on the list of transactions). */
  private String notes;

  /** If `true`, the transaction is a transfer from an account to another. */
  private boolean transfer;

  /** The type of the transaction. */
  enum Kind {
    INCOME,
    EXPENSE,
    TRANSFER,
  }

  enum Account {
    CASH(0, "Espèces"),
    CARD(1, "Carte bancaire");

    private final int id;
    private final String value;

    Account(int id, String value) {
      this.id = id;
      this.value = value;
    }

    public int getId() {
      return id;
    }

    public String getValue() {
      return value;
    }
  }

  enum Category {
    FOOD(0, "Nourriture"),
    LEISURE(1, "Loisirs"),
    OTHER(2, "Autres");

    private final int id;
    private final String value;

    Category(int id, String value) {
      this.id = id;
      this.value = value;
    }

    public int getId() {
      return id;
    }

    public String getValue() {
      return value;
    }
  }

  public Transaction(
      int id,
      int amount,
      int date,
      Account account,
      Category category,
      @NonNull String contents,
      String notes,
      boolean transfer) {
    this.id = id;
    this.amount = amount;
    this.date = date;
    this.account = account;
    this.category = category;
    this.contents = contents;
    this.notes = notes;
    this.transfer = transfer;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  int getDate() {
    return date;
  }

  public void setDate(int date) {
    this.date = date;
  }

  Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  @NonNull
  String getContents() {
    return contents;
  }

  public void setContents(@NonNull String contents) {
    this.contents = contents;
  }

  String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  boolean isTransfer() {
    return transfer;
  }

  public void setTransfer(boolean transfer) {
    this.transfer = transfer;
  }

  /**
   * Returns the amount as a formatted currency string (with French localization). If discreet mode
   * is enabled, returns a string that doesn't disclose the amount.
   */
  @RequiresApi(api = VERSION_CODES.N)
  String getAmountString(boolean discreetMode) {
    if (discreetMode) {
      // Add a "+" prefix for positive amounts to make income more explicit.
      // We also need to add the "-" prefix manually as we don't display the number here.
      return String.format("%s##,## €", getAmountSign(true));
    } else {
      final DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
      decimalFormatSymbols.setDecimalSeparator(',');
      decimalFormatSymbols.setGroupingSeparator(' ');
      DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);

      // Add a "+" prefix for positive amounts to make income more explicit
      return String.format("%s%s €", getAmountSign(false), decimalFormat.format(amount / 100.0));
    }
  }

  /** Returns the color the amount should be displayed with (as an hexadecimal code). */
  int getAmountColor() {
    return transfer || amount == 0
        ? 0xff808080 // Transfer/neutral
        : amount > 0
            ? 0xff3388ff // Income
            : 0xffff4433; // Expense
  }

  /** Returns the sign corresponding to the transaction amount. */
  private String getAmountSign(boolean alsoNegative) {
    if (transfer || amount == 0 || (amount < 0 && !alsoNegative)) {
      return "";
    } else if (amount > 0) {
      return "+";
    } else {
      return "-";
    }
  }
}
