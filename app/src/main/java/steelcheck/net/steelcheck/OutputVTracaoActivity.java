package steelcheck.net.steelcheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OutputVTracaoActivity extends AppCompatActivity {

    private TextView perfil;
    private TextView ntsd_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_vtracao);
        Bundle extras = getIntent().getExtras();
        DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
        database.open();
        if(extras != null)
        {
            int perfil_selected_pos = extras.getInt("perfil");
            double ntsd = extras.getDouble("ntsd");
            double fy = extras.getDouble("fy");
            double lx = extras.getDouble("lx");
            double ly = extras.getDouble("ly");
            int radio = extras.getInt("radio");


            perfil = (TextView) findViewById(R.id.perfil_id);
            perfil.setText(database.get_perfil(perfil_selected_pos));
            ntsd_text = (TextView) findViewById(R.id.ntsd_id);
            ntsd_text.setText(""+ntsd);

        }
        database.close();
    }
}
