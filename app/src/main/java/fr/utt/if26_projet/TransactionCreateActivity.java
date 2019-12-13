package fr.utt.if26_projet;

import android.icu.text.SimpleDateFormat;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import fr.utt.if26_projet.Transaction.Account;
import fr.utt.if26_projet.Transaction.Category;
import fr.utt.if26_projet.Transaction.Kind;
import java.text.ParseException;

public class TransactionCreateActivity extends AppCompatActivity {

  private TransactionViewModel transactionViewModel;
  private RadioGroup kindRadioGroup;
  private TextView dateTextView;
  private TextView amountTextView;
  private TextView contentsTextView;
  private TextView notesTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_transaction_create);
    setTitle(R.string.title_transaction_create);

    transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

    kindRadioGroup = findViewById(R.id.transaction_create_kind);
    dateTextView = findViewById(R.id.transaction_create_date);
    amountTextView = findViewById(R.id.transaction_create_amount);
    contentsTextView = findViewById(R.id.transaction_create_contents);
    notesTextView = findViewById(R.id.transaction_create_notes);

    final Button submitButton = findViewById(R.id.transaction_create_submit);
    submitButton.setOnClickListener(
        new View.OnClickListener() {
          @RequiresApi(api = VERSION_CODES.N)
          @Override
          public void onClick(View view) {
            // Determine the selected kind radio button
            final View radioButton =
                kindRadioGroup.findViewById(kindRadioGroup.getCheckedRadioButtonId());
            final Kind kind = Kind.values()[kindRadioGroup.indexOfChild(radioButton)];

            // Handle French decimal separators in the amount
            final double amount =
                Double.parseDouble(amountTextView.getText().toString().replace(',', '.'));

            // Convert the date to an UNIX timestamp for storage purposes
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
            final int date;
            try {
              date =
                  Math.toIntExact(
                      dateFormat.parse(dateTextView.getText().toString()).getTime() / 1000L);

              // The transaction kind isn't stored directly.
              // Instead, the transaction amount is stored as a positive or negative integer
              // depending on the transaction kind.
              final Transaction transaction =
                  new Transaction(
                      0,
                      (kind == Kind.EXPENSE ? -1 : 1) * (int) (amount * 100.0),
                      date,
                      Account.CARD,
                      Category.OTHER,
                      contentsTextView.getText().toString(),
                      notesTextView.getText().toString(),
                      kind == Kind.TRANSFER);
              transactionViewModel.insert(transaction);

              final TextView resultTextView = findViewById(R.id.transaction_create_result);
              resultTextView.setText(R.string.transaction_added);
            } catch (ParseException e) {
              e.printStackTrace();
            }
          }
        });
  }
}
