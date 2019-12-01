package fr.utt.if26_projet;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * A fragment representing a single Transaction detail screen. This fragment is either contained in
 * a {@link TransactionListActivity} in two-pane mode (on tablets) or a {@link
 * TransactionDetailActivity} on handsets.
 */
public class TransactionDetailFragment extends Fragment {

  /** The fragment argument representing the item ID that this fragment represents. */
  static final String ARG_ITEM_ID = "item_id";

  /** The dummy content this fragment is presenting. */
  private Transaction transaction;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
   * screen orientation changes).
   */
  public TransactionDetailFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    System.out.println("Loading TransactionDetailFragment");

    if (getArguments() != null && getArguments().containsKey(ARG_ITEM_ID)) {
      final Activity activity = this.getActivity();
      final CollapsingToolbarLayout appBarLayout;

      TransactionViewModel transactionViewModel =
          new ViewModelProvider(this).get(TransactionViewModel.class);
      transactionViewModel
          .get(getArguments().getInt(ARG_ITEM_ID))
          .observe(
              this,
              new Observer<Transaction>() {
                @Override
                public void onChanged(@Nullable final Transaction transaction2) {
                  // Update the cached copy of the transaction in the fragment
                  System.out.println(
                      "Updating cached copy of transaction in TransactionDetailFragment.");
                  transaction = transaction2;
                }
              });

      if (activity != null) {
        appBarLayout = activity.findViewById(R.id.toolbar_layout);

        if (appBarLayout != null) {
          appBarLayout.setTitle(transaction.getContents());
        }
      }
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View rootView = inflater.inflate(R.layout.transaction_detail, container, false);

    if (transaction != null) {
      ((TextView) rootView.findViewById(R.id.transaction_detail)).setText(transaction.getNotes());
    }

    return rootView;
  }
}
