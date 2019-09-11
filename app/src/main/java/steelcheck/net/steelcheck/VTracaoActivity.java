package steelcheck.net.steelcheck;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VTracaoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    private Spinner secao_perfil;
    private String perfil_selected_str;
    private int perfil_selected_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vtracao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        secao_perfil = (Spinner) findViewById(R.id.secao_vtracao);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.secao_perfil, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secao_perfil.setAdapter(adapter);
        secao_perfil.setOnItemSelectedListener(new SecaoSpinnerClass());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(new Intent(VTracaoActivity.this,HomeActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_sobre) {
            Intent intent = new Intent(new Intent(VTracaoActivity.this,SobreActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_contato) {

        } else if (id == R.id.nav_tracaoV) {
            // keep activity
        } else if (id == R.id.nav_compressaoV) {

        } else if (id == R.id.nav_flexaoV) {

        } else if (id == R.id.nav_flexocompressaoV) {

        } else if (id == R.id.nav_tracaoD) {

        } else if (id == R.id.nav_compressaoD) {

        } else if (id == R.id.nav_flexaoD) {

        } else if (id == R.id.nav_flexocompressaoD) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private LinearLayout linear_scroll;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class SecaoSpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            linear_scroll = (LinearLayout) findViewById(R.id.linear_scroll_id);
            System.out.println(id);

            switch (position) {
                case 0: //escolha seção
                    linear_scroll.removeAllViews();
                    break;
                case 1: //laminado W
                    ImageView image = new ImageView(VTracaoActivity.this);
                    image.setImageDrawable(ContextCompat.getDrawable(VTracaoActivity.this, R.drawable.laminado));
                    linear_scroll.addView(image);
                    linear_scroll.setGravity(Gravity.CENTER);

                    Spinner spinner_desig = new Spinner(VTracaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(VTracaoActivity.this, R.array.laminado_perfis, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_desig.setAdapter(adapter);
                    spinner_desig.setOnItemSelectedListener(new DesigSpinnerClass());
                    spinner_desig.setPadding(50,50,50,50);
                    linear_scroll.addView(spinner_desig);

                        //text1
                    TextView Ntsd = new TextView(VTracaoActivity.this);
                    Ntsd.setText(R.string.ntsd);
                    linear_scroll.addView(Ntsd);
                    Ntsd.setTextSize(17);
                        //box1
                    final EditText Ntsd_box = new EditText(VTracaoActivity.this);
                    Ntsd_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(Ntsd_box);
                    Ntsd_box.setPadding(100,10,100,10);
                    Ntsd_box.canScrollHorizontally(1);
                        //text2
                    TextView fy = new TextView(VTracaoActivity.this);
                    fy.setText(R.string.fy);
                    linear_scroll.addView(fy);
                    fy.setTextSize(17);
                        //box2
                    final EditText fy_box = new EditText(VTracaoActivity.this);
                    fy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(fy_box);
                    fy_box.setPadding(100,10,100,10);
                    fy_box.canScrollHorizontally(1);
                        //text3
                    TextView Lx = new TextView(VTracaoActivity.this);
                    Lx.setText(R.string.lx);
                    linear_scroll.addView(Lx);
                    Lx.setTextSize(17);
                        //box3
                    final EditText Lx_box = new EditText(VTracaoActivity.this);
                    Lx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(Lx_box);
                    Lx_box.setPadding(100,10,100,10);
                    Lx_box.canScrollHorizontally(1);
                        //text4
                    TextView Ly = new TextView(VTracaoActivity.this);
                    Ly.setText(R.string.ly);
                    linear_scroll.addView(Ly);
                    Ly.setTextSize(17);
                        //box4
                    final EditText Ly_box = new EditText(VTracaoActivity.this);
                    Ly_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(Ly_box);
                    Ly_box.setPadding(100,10,100,10);
                    Ly_box.canScrollHorizontally(1);

                    //terceira linha radio button tipo analise

                    final RadioGroup tipo_analise = new RadioGroup(VTracaoActivity.this);
                    RadioButton escoamento = new RadioButton(VTracaoActivity.this);
                    RadioButton ruptura = new RadioButton(VTracaoActivity.this);

                    tipo_analise.addView(escoamento);
                    tipo_analise.addView(ruptura);
                    escoamento.setText(R.string.escoamento);
                    ruptura.setText(R.string.ruptura);
                    tipo_analise.setPadding(0,50,0,30);

                    linear_scroll.addView(tipo_analise);

                    // final

                    Button botao_verificar = new Button(VTracaoActivity.this);
                    botao_verificar.setText(R.string.verificar);
                    linear_scroll.addView(botao_verificar);

                    botao_verificar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(Ntsd_box.getText().toString().isEmpty() || fy_box.getText().toString().isEmpty()
                                    || Lx_box.getText().toString().isEmpty() || Ly_box.getText().toString().isEmpty()
                                    || tipo_analise.getCheckedRadioButtonId() == -1)
                            {
                                Toast.makeText(VTracaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                            }
                            else if(perfil_selected_pos == 0)
                            {
                                Toast.makeText(VTracaoActivity.this, R.string.warning_selecionar, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {   DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
                                database.open();
                                System.out.println(database.get_zx(perfil_selected_pos));
                                Toast.makeText(VTracaoActivity.this, database.get_perfil(perfil_selected_pos), Toast.LENGTH_SHORT).show();


                            }
                        }
                    });

                    break;
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    class DesigSpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //linear_scroll = (LinearLayout) findViewById(R.id.linear_scroll_id);
            perfil_selected_pos = position;
            perfil_selected_str = parent.getAdapter().getItem(position).toString();
            System.out.println(perfil_selected_str);

        }

        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
