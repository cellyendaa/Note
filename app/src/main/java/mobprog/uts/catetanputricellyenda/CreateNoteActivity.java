package mobprog.uts.catetanputricellyenda;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

import mobprog.uts.putricellyenda.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateNoteActivity extends AppCompatActivity {

    private TextView judulCatatanTextView, detailCatatanTextView, createDateCatatanTextView;
    private Button buttonHapusCatatan, buttonNextCatatan, buttonPreviousCatatan, buttonKembali;
    private OkHttpClient client = new OkHttpClient();
    private JSONArray dataCatatan;
    private int currentIndexCatatan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        judulCatatanTextView = findViewById(R.id.judulCatatan1);
        detailCatatanTextView = findViewById(R.id.detailCatatan1);
        createDateCatatanTextView = findViewById(R.id.createDateCatatan1);
        buttonHapusCatatan = findViewById(R.id.buttonHapusCatatan1);
        buttonNextCatatan = findViewById(R.id.buttonNextCatatan1);
        buttonPreviousCatatan = findViewById(R.id.buttonPreviousCatatan1);
        buttonKembali = findViewById(R.id.buttonKembali);

        getCatatan();

        buttonHapusCatatan.setOnClickListener(v -> hapusCatatan());
        buttonNextCatatan.setOnClickListener(v -> navigateCatatan(true));
        buttonPreviousCatatan.setOnClickListener(v -> navigateCatatan(false));
        buttonKembali.setOnClickListener(v -> onBackPressed());
    }

    private void getCatatan() {
        String url = "http://103.178.153.230/uts/indexapi.php?req=get_catatan&nim=0102522029";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(CreateNoteActivity.this, "Gagal mengambil data catatan", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            if (jsonObject.getBoolean("success")) {
                                dataCatatan = jsonObject.getJSONArray("data_catatan");
                                updateCatatanView();
                            } else {
                                Toast.makeText(CreateNoteActivity.this, jsonObject.getString("pesan"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(CreateNoteActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(CreateNoteActivity.this, "Gagal mengambil data catatan", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void hapusCatatan() {
        if (dataCatatan != null && dataCatatan.length() > currentIndexCatatan) {
            dataCatatan.remove(currentIndexCatatan);
            updateCatatanView();
            Toast.makeText(this, "Catatan dihapus", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Catatan tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCatatanView() {
        try {
            if (dataCatatan.length() > 0) {
                JSONObject catatan = dataCatatan.getJSONObject(currentIndexCatatan);
                judulCatatanTextView.setText(catatan.getString("judul_catatan"));
                detailCatatanTextView.setText(catatan.getString("detail_catatan"));
                createDateCatatanTextView.setText("Dibuat pada: " + catatan.getString("create_date"));

                buttonHapusCatatan.setEnabled(true);
                buttonNextCatatan.setEnabled(dataCatatan.length() > 1);
                buttonPreviousCatatan.setEnabled(dataCatatan.length() > 1);
            } else {
                judulCatatanTextView.setText("");
                detailCatatanTextView.setText("");
                createDateCatatanTextView.setText("");
                buttonHapusCatatan.setEnabled(false);
                buttonNextCatatan.setEnabled(false);
                buttonPreviousCatatan.setEnabled(false);
                Toast.makeText(CreateNoteActivity.this, "Tidak ada catatan yang tersisa", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CreateNoteActivity.this, "Error updating view", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateCatatan(boolean isNext) {
        if (isNext) {
            currentIndexCatatan = (currentIndexCatatan + 1) % dataCatatan.length();
        } else {
            currentIndexCatatan = (currentIndexCatatan - 1 + dataCatatan.length()) % dataCatatan.length();
        }
        updateCatatanView();
    }
}
