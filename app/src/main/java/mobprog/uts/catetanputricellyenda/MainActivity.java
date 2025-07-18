package mobprog.uts.catetanputricellyenda;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import mobprog.uts.putricellyenda.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText judulCatatanEditText, detailCatatanEditText;
    private Button buttonLihatCatatan, buttonTambahCatatan;
    private OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        judulCatatanEditText = findViewById(R.id.judulCatatan);
        detailCatatanEditText = findViewById(R.id.detailCatatan);
        buttonLihatCatatan = findViewById(R.id.buttonLihatCatatan);
        buttonTambahCatatan = findViewById(R.id.buttonTambahCatatan);

        buttonLihatCatatan.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateNoteActivity.class);
            startActivity(intent);
        });

        buttonTambahCatatan.setOnClickListener(v -> tambahCatatan());
    }

    private void tambahCatatan() {
        String judulCatatan = judulCatatanEditText.getText().toString();
        String detailCatatan = detailCatatanEditText.getText().toString();
        String nim = "0102522029"; // Replace with the actual NIM

        if (judulCatatan.isEmpty() || detailCatatan.isEmpty()) {
            Toast.makeText(this, "Judul dan detail catatan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("req", "tambah_catatan");
            json.put("nim", nim);
            json.put("judul_catatan", judulCatatan);
            json.put("detail_catatan", detailCatatan);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating JSON request", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        String url = "http://103.178.153.230/uts/indexapipost.php";
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Gagal menambahkan catatan", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Catatan berhasil ditambahkan", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Gagal menambahkan catatan", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}


