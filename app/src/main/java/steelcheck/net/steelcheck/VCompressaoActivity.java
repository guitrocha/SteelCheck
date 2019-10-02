package steelcheck.net.steelcheck;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class VCompressaoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    private Spinner secao_comp;
    private LinearLayout linear_scroll;
    private String perfil_selected_str;
    private int perfil_selected_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vcompressao);
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

        secao_comp = (Spinner) findViewById(R.id.secao_vcomp);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.secao_comp, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secao_comp.setAdapter(adapter);
        secao_comp.setOnItemSelectedListener(new SecaoSpinnerClass());


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
            Intent intent = new Intent(new Intent(VCompressaoActivity.this,HomeActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_sobre) {
            Intent intent = new Intent(new Intent(VCompressaoActivity.this,SobreActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_contato) {

        } else if (id == R.id.nav_tracaoV) {
            Intent intent = new Intent(new Intent(VCompressaoActivity.this,VTracaoActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_compressaoV) {
            // keep activity
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class SecaoSpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

            linear_scroll = (LinearLayout) findViewById(R.id.linear_scroll_idcomp);
            System.out.println(id);
            linear_scroll.removeAllViews();

            if(position == 0)
            {
                //escolha seção
                linear_scroll.removeAllViews();
            }
            else
            {
                if(position == 1)
                {
                    //imagem
                    ImageView image = new ImageView(VCompressaoActivity.this);
                    image.setImageDrawable(ContextCompat.getDrawable(VCompressaoActivity.this, R.drawable.laminado));
                    linear_scroll.addView(image);
                    linear_scroll.setGravity(Gravity.CENTER);

                    //perfil spinner
                    Spinner spinner_desig = new Spinner(VCompressaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(VCompressaoActivity.this, R.array.laminado_perfis, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_desig.setAdapter(adapter);
                    spinner_desig.setOnItemSelectedListener(new PerfilSpinnerClass());
                    spinner_desig.setPadding(50,50,50,50);
                    linear_scroll.addView(spinner_desig);

                }
                else if(position == 2)
                {

                }

                //text1
                TextView Ncsd = new TextView(VCompressaoActivity.this);
                Ncsd.setText(Html.fromHtml("N<sub><small>c,Sd</small></sub> (kN):"));
                linear_scroll.addView(Ncsd);
                Ncsd.setTextSize(17);
                Ncsd.setPadding(0,10,0,10);

                //box1
                final EditText Ncsd_box = new EditText(VCompressaoActivity.this);
                Ncsd_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Ncsd_box);
                Ncsd_box.setPadding(100,10,100,10);
                Ncsd_box.canScrollHorizontally(1);

                //text2
                TextView fy = new TextView(VCompressaoActivity.this);
                fy.setText(Html.fromHtml("f<sub><small>y</small></sub> (MPa):"));
                linear_scroll.addView(fy);
                fy.setTextSize(17);
                fy.setPadding(0,10,0,10);

                //box2
                final EditText fy_box = new EditText(VCompressaoActivity.this);
                fy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(fy_box);
                fy_box.setPadding(100,10,100,10);
                fy_box.canScrollHorizontally(1);

                //text3
                TextView kx = new TextView(VCompressaoActivity.this);
                kx.setText(Html.fromHtml("k<sub><small>x</small></sub>:"));
                linear_scroll.addView(kx);
                kx.setTextSize(17);
                kx.setPadding(0,10,0,10);

                //box3
                final EditText kx_box = new EditText(VCompressaoActivity.this);
                kx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(kx_box);
                kx_box.setPadding(100,10,100,10);
                kx_box.canScrollHorizontally(1);

                //text4
                TextView ky = new TextView(VCompressaoActivity.this);
                ky.setText(Html.fromHtml("k<sub><small>y</small></sub>:"));
                linear_scroll.addView(ky);
                ky.setTextSize(17);
                ky.setPadding(0,10,0,10);

                //box4
                final EditText ky_box = new EditText(VCompressaoActivity.this);
                ky_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(ky_box);
                ky_box.setPadding(100,10,100,10);
                ky_box.canScrollHorizontally(1);

                //text5
                TextView kz = new TextView(VCompressaoActivity.this);
                kz.setText(Html.fromHtml("k<sub><small>z</small></sub>:"));
                linear_scroll.addView(kz);
                kz.setTextSize(17);
                kz.setPadding(0,10,0,10);

                //box5
                final EditText kz_box = new EditText(VCompressaoActivity.this);
                kz_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(kz_box);
                kz_box.setPadding(100,10,100,10);
                kz_box.canScrollHorizontally(1);

                //text6
                TextView Lx = new TextView(VCompressaoActivity.this);
                Lx.setText(Html.fromHtml("L<sub><small>x</small></sub> (cm):"));
                linear_scroll.addView(Lx);
                Lx.setTextSize(17);
                Lx.setPadding(0,10,0,10);

                //box6
                final EditText Lx_box = new EditText(VCompressaoActivity.this);
                Lx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Lx_box);
                Lx_box.setPadding(100,10,100,10);
                Lx_box.canScrollHorizontally(1);

                //text7
                TextView Ly = new TextView(VCompressaoActivity.this);
                Ly.setText(Html.fromHtml("L<sub><small>y</small></sub> (cm):"));
                linear_scroll.addView(Ly);
                Ly.setTextSize(17);
                Ly.setPadding(0,10,0,10);

                //box7
                final EditText Ly_box = new EditText(VCompressaoActivity.this);
                Ly_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Ly_box);
                Ly_box.setPadding(100,10,100,10);
                Ly_box.canScrollHorizontally(1);

                //text8
                TextView Lz = new TextView(VCompressaoActivity.this);
                Lz.setText(Html.fromHtml("L<sub><small>z</small></sub> (cm):"));
                linear_scroll.addView(Lz);
                Lz.setTextSize(17);
                Lz.setPadding(0,10,0,10);

                //box8
                final EditText Lz_box = new EditText(VCompressaoActivity.this);
                Lz_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Lz_box);
                Lz_box.setPadding(100,10,100,10);
                Lz_box.canScrollHorizontally(1);

                //final botao
                Button botao_verificar = new Button(VCompressaoActivity.this);
                botao_verificar.setText(R.string.verificar);
                linear_scroll.addView(botao_verificar);

                botao_verificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Ncsd_value = Ncsd_box.getText().toString();
                        String fy_value = fy_box.getText().toString();
                        String kx_value = kx_box.getText().toString();
                        String ky_value = ky_box.getText().toString();
                        String kz_value = kz_box.getText().toString();
                        String Lx_value = Lx_box.getText().toString();
                        String Ly_value = Ly_box.getText().toString();
                        String Lz_value = Lz_box.getText().toString();

                        if(Ncsd_value.isEmpty() || fy_value.isEmpty()
                                || kx_value.isEmpty() || ky_value.isEmpty() || kz_value.isEmpty()
                                || Lx_value.isEmpty() || Ly_value.isEmpty() || Lz_value.isEmpty() )
                        {
                            Toast.makeText(VCompressaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else if(position == 1 && perfil_selected_pos == 0)
                        {
                                Toast.makeText(VCompressaoActivity.this, R.string.warning_selecionar, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intent = new Intent(new Intent(VCompressaoActivity.this,OutputVCompressaoActivity.class));
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            if(position == 1)
                                intent.putExtra("perfil",perfil_selected_pos);
                            intent.putExtra("ncsd", Double.parseDouble(Ncsd_value));
                            intent.putExtra("fy", Double.parseDouble(fy_value));
                            intent.putExtra("kx", Double.parseDouble(kx_value));
                            intent.putExtra("ky", Double.parseDouble(ky_value));
                            intent.putExtra("kz", Double.parseDouble(kz_value));
                            intent.putExtra("lx", Double.parseDouble(Lx_value));
                            intent.putExtra("ly", Double.parseDouble(Ly_value));
                            intent.putExtra("lz", Double.parseDouble(Lz_value));

                            startActivity(intent);
                        }
                    }
                });

            }


        }
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("Nothing selected on Spinner Section");
        }
    }
    class PerfilSpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //linear_scroll = (LinearLayout) findViewById(R.id.linear_scroll_id);
            perfil_selected_pos = position;
            perfil_selected_str = parent.getAdapter().getItem(position).toString();
            System.out.println(perfil_selected_str);

        }

        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("Nothing selected on Spinner Profile");
        }
    }


}
