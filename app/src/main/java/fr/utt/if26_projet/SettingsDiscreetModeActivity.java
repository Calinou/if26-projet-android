package fr.utt.if26_projet;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsDiscreetModeActivity extends AppCompatActivity {

  private TextView preview;
  private Switch discreetModeSwitch;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings_discreet_mode);

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    final SharedPreferences settings = getSharedPreferences("user", MODE_PRIVATE);
    preview = findViewById(R.id.settings_discreet_mode_preview);
    discreetModeSwitch = findViewById(R.id.settings_discreet_mode_switch);
    discreetModeSwitch.setChecked(
        settings.getBoolean(getString(R.string.setting_discreet_mode), false));

    // Update the preview when loading this activity
    updatePreview();

    discreetModeSwitch.setOnClickListener(
        new OnClickListener() {
          @Override
          public void onClick(View view) {
            final SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(
                getString(R.string.setting_discreet_mode), discreetModeSwitch.isChecked());
            editor.apply();

            updatePreview();
          }
        });
  }

  /** Updates the preview displayed below the discreet mode setting. */
  @SuppressLint("SetTextI18n")
  private void updatePreview() {
    if (discreetModeSwitch.isChecked()) {
      preview.setText("+##,## €");
    } else {
      preview.setText("+12,34 €");
    }
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
