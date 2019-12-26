package fr.utt.if26_projet;

import static org.junit.Assert.*;

import fr.utt.if26_projet.Transaction.Account;
import fr.utt.if26_projet.Transaction.Category;
import org.junit.Test;

public class TransactionUnitTest {
  @Test
  public void transactionKindStringResource_isCorrect() {
    final Transaction income =
        new Transaction(0, 500, 0, Account.CARD, Category.OTHER, "Exemple", "", false);

    final Transaction expense =
        new Transaction(0, -500, 0, Account.CARD, Category.OTHER, "Exemple", "", false);

    final Transaction transfer =
        new Transaction(0, 500, 0, Account.CARD, Category.OTHER, "Exemple", "", true);

    assertEquals(R.string.transaction_kind_income, income.getKindStringResource());
    assertEquals(R.string.transaction_kind_expense, expense.getKindStringResource());
    assertEquals(R.string.transaction_kind_transfer, transfer.getKindStringResource());
  }
}
