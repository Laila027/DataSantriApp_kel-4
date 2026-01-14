package com.example.datasantri;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.*;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ListView listViewSantri;
    Button btnAddSantri;
    ArrayList<Santri> dataSantri;
    ArrayAdapter<Santri> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Mengubah warna background seluruh layar jadi soft pink
        findViewById(android.R.id.content).setBackgroundColor(android.graphics.Color.parseColor("#FCE4EC"));

        listViewSantri = findViewById(R.id.listViewSantri);
        btnAddSantri = findViewById(R.id.btnAddSantri);

        dataSantri = new ArrayList<>();
        // Sekarang kita pakai adapter buatan sendiri agar warnanya muncul
        adapter = new SantriAdapter(this, dataSantri);
        listViewSantri.setAdapter(adapter);

        // --- AMBIL DATA DARI DATABASE ONLINE ---
        DatabaseHelper db = new DatabaseHelper();
        db.ambilSantri((jsonString) -> {
            if (jsonString != null) {
                runOnUiThread(() -> {
                    try {
                        JSONArray array = new JSONArray(jsonString);
                        dataSantri.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            dataSantri.add(new Santri(
                                    obj.getString("nama"),
                                    obj.getString("kelas"),
                                    obj.getString("asal"),
                                    obj.getString("no_hp")
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) { e.printStackTrace(); }
                });
            }
            return null;
        });

        btnAddSantri.setOnClickListener(v -> showAddDialog());
        listViewSantri.setOnItemClickListener((parent, view, position, id) -> showEditDialog(position));

        // --- HAPUS DATA PERMANEN ---
        listViewSantri.setOnItemLongClickListener((parent, view, position, id) -> {
            Santri terpilih = dataSantri.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Hapus Data")
                    .setMessage("Yakin hapus " + terpilih.getNama() + " dari server?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        db.hapusSantri(terpilih.getNama(), (berhasil) -> {
                            runOnUiThread(() -> {
                                if (berhasil) {
                                    dataSantri.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(this, "Terhapus!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return null;
                        });
                    })
                    .setNegativeButton("Batal", null).show();
            return true;
        });
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Santri");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 20, 30, 10);

        EditText inputNama = new EditText(this); inputNama.setHint("Nama"); layout.addView(inputNama);
        EditText inputKelas = new EditText(this); inputKelas.setHint("Kelas"); layout.addView(inputKelas);
        EditText inputAsal = new EditText(this); inputAsal.setHint("Asal"); layout.addView(inputAsal);
        EditText inputNoHp = new EditText(this); inputNoHp.setHint("No HP"); layout.addView(inputNoHp);

        builder.setView(layout);
        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String nama = inputNama.getText().toString();
            if (!nama.isEmpty()) {
                new DatabaseHelper().tambahSantri(nama, inputKelas.getText().toString(),
                        inputAsal.getText().toString(), inputNoHp.getText().toString(), (berhasil) -> {
                            runOnUiThread(() -> {
                                if (berhasil) {
                                    dataSantri.add(new Santri(nama, inputKelas.getText().toString(),
                                            inputAsal.getText().toString(), inputNoHp.getText().toString()));
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            return null;
                        });
            }
        });
        builder.setNegativeButton("Batal", null).show();
    }

    private void showEditDialog(int position) {
        Santri santri = dataSantri.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Data " + santri.getNama());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 20, 30, 10);

        EditText inputKelas = new EditText(this); inputKelas.setText(santri.getKelas()); layout.addView(inputKelas);
        EditText inputAsal = new EditText(this); inputAsal.setText(santri.getAsal()); layout.addView(inputAsal);
        EditText inputNoHp = new EditText(this); inputNoHp.setText(santri.getNoHp()); layout.addView(inputNoHp);

        builder.setView(layout);
        builder.setPositiveButton("Update", (dialog, which) -> {
            String kls = inputKelas.getText().toString();
            String asl = inputAsal.getText().toString();
            String hp = inputNoHp.getText().toString();

            new DatabaseHelper().updateSantri(santri.getNama(), kls, asl, hp, (berhasil) -> {
                runOnUiThread(() -> {
                    if (berhasil) {
                        dataSantri.set(position, new Santri(santri.getNama(), kls, asl, hp));
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Berhasil Diperbarui!", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            });
        });
        builder.setNegativeButton("Batal", null).show();
    }
}