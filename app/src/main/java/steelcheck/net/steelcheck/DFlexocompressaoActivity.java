package steelcheck.net.steelcheck;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class DFlexocompressaoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    private Spinner secao_flexocomp;
    private LinearLayout linear_scroll;
    private boolean isAmp = false;
    private String orderby_selected;
    private ScrollView scv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dflexocompressao);
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

        secao_flexocomp = (Spinner) findViewById(R.id.secao_dflexocompressao);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.secao_perfil, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secao_flexocomp.setAdapter(adapter);
        secao_flexocomp.setOnItemSelectedListener(new SecaoSpinnerClass());


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

   /* @Override
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(DFlexocompressaoActivity.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_sobre) {
            Intent intent = new Intent(DFlexocompressaoActivity.this,SobreActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_contato) {
            Intent intent = new Intent(DFlexocompressaoActivity.this,ContatoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_tracaoV) {
            Intent intent = new Intent(DFlexocompressaoActivity.this,VTracaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_compressaoV) {
            Intent intent = new Intent(DFlexocompressaoActivity.this,VCompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexaoV) {
            Intent intent = new Intent(DFlexocompressaoActivity.this,VFlexaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexocompressaoV) {
            Intent intent = new Intent(DFlexocompressaoActivity.this,VFlexocompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_tracaoD) {
            Intent intent = new Intent(DFlexocompressaoActivity.this,DTracaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_compressaoD) {
            Intent intent = new Intent(DFlexocompressaoActivity.this,DCompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexaoD) {
            Intent intent = new Intent(DFlexocompressaoActivity.this,DFlexaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexocompressaoD) {
            //keep activity
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

            linear_scroll = (LinearLayout) findViewById(R.id.linear_scroll_idflexocompressao);
            linear_scroll.setGravity(Gravity.CENTER);
            System.out.println(id);
            linear_scroll.removeAllViews();
            scv = (ScrollView) findViewById(R.id.scrollView_dflexcomp_id);
            scv.setBackground(null);

            if(position == 0)
            {
                //escolha seção
                linear_scroll.removeAllViews();
            }
            else
            {   scv.setBackground(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));
                if(position == 1)
                {
                    //imagem
                    ImageView image = new ImageView(DFlexocompressaoActivity.this);
                    image.setImageDrawable(ContextCompat.getDrawable(DFlexocompressaoActivity.this, R.drawable.perfil_laminado));
                    linear_scroll.addView(image);
                    linear_scroll.setGravity(Gravity.CENTER);
                    image.setPadding(0,0,0,50);

                }


                //viga
                TextView TV_viga = new TextView(DFlexocompressaoActivity.this);
                TV_viga.setText("DADOS DA VIGA");
                TV_viga.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
                TV_viga.setTextSize(25);
                TV_viga.setPadding(0,70,0,0);
                linear_scroll.addView(TV_viga);

                //text1
                TextView Msdx = new TextView(DFlexocompressaoActivity.this);
                Msdx.setText(Html.fromHtml("M<sub><small>Sd,x</small></sub> (kNm):"));
                linear_scroll.addView(Msdx);
                Msdx.setTextSize(17);
                Msdx.setPadding(0,100,0,10);

                //box1
                final EditText Msdx_box = new EditText(DFlexocompressaoActivity.this);
                Msdx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Msdx_box);
                Msdx_box.setPadding(100,10,100,10);
                Msdx_box.canScrollHorizontally(1);

                //text1
                TextView Msdy = new TextView(DFlexocompressaoActivity.this);
                Msdy.setText(Html.fromHtml("M<sub><small>Sd,y</small></sub> (kNm):"));
                linear_scroll.addView(Msdy);
                Msdy.setTextSize(17);
                Msdy.setPadding(0,10,0,10);

                //box1
                final EditText Msdy_box = new EditText(DFlexocompressaoActivity.this);
                Msdy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Msdy_box);
                Msdy_box.setPadding(100,10,100,10);
                Msdy_box.canScrollHorizontally(1);

                //text3
                TextView lb = new TextView(DFlexocompressaoActivity.this);
                lb.setText(Html.fromHtml("ℓ<sub><small>b</small></sub> (cm):"));
                linear_scroll.addView(lb);
                lb.setTextSize(17);
                lb.setPadding(0,10,0,10);

                //box3
                final EditText lb_box = new EditText(DFlexocompressaoActivity.this);
                lb_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(lb_box);
                lb_box.setPadding(100,10,100,10);
                lb_box.canScrollHorizontally(1);

                //text4
                TextView cb = new TextView(DFlexocompressaoActivity.this);
                cb.setText(Html.fromHtml("C<sub><small>b</small></sub>:"));
                linear_scroll.addView(cb);
                cb.setTextSize(17);
                cb.setPadding(0,10,0,10);
                cb.setHint("1.0 ≤ Cb ≤ 3.0");

                //box4
                final EditText cb_box = new EditText(DFlexocompressaoActivity.this);
                cb_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(cb_box);
                cb_box.setPadding(100,10,100,10);
                cb_box.canScrollHorizontally(1);

                //coluna
                TextView TV_coluna = new TextView(DFlexocompressaoActivity.this);
                TV_coluna.setText("DADOS DA COLUNA");
                TV_coluna.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
                TV_coluna.setTextSize(25);
                TV_coluna.setPadding(0,70,0,0);
                linear_scroll.addView(TV_coluna);

                //text1
                TextView Ncsd = new TextView(DFlexocompressaoActivity.this);
                Ncsd.setText(Html.fromHtml("N<sub><small>c,Sd</small></sub> (kN):"));
                linear_scroll.addView(Ncsd);
                Ncsd.setTextSize(17);
                Ncsd.setPadding(0,100,0,10);

                //box1
                final EditText Ncsd_box = new EditText(DFlexocompressaoActivity.this);
                Ncsd_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Ncsd_box);
                Ncsd_box.setPadding(100,10,100,10);
                Ncsd_box.canScrollHorizontally(1);

                //text2
                TextView fy = new TextView(DFlexocompressaoActivity.this);
                fy.setText(Html.fromHtml("f<sub><small>y</small></sub> (MPa):"));
                linear_scroll.addView(fy);
                fy.setTextSize(17);
                fy.setPadding(0,10,0,10);

                //box2
                final EditText fy_box = new EditText(DFlexocompressaoActivity.this);
                fy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(fy_box);
                fy_box.setPadding(100,10,100,10);
                fy_box.canScrollHorizontally(1);

                //text3
                TextView kx = new TextView(DFlexocompressaoActivity.this);
                kx.setText(Html.fromHtml("k<sub><small>x</small></sub>:"));
                linear_scroll.addView(kx);
                kx.setTextSize(17);
                kx.setPadding(0,10,0,10);

                //box3
                final EditText kx_box = new EditText(DFlexocompressaoActivity.this);
                kx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(kx_box);
                kx_box.setPadding(100,10,100,10);
                kx_box.canScrollHorizontally(1);

                //text4
                TextView ky = new TextView(DFlexocompressaoActivity.this);
                ky.setText(Html.fromHtml("k<sub><small>y</small></sub>:"));
                linear_scroll.addView(ky);
                ky.setTextSize(17);
                ky.setPadding(0,10,0,10);

                //box4
                final EditText ky_box = new EditText(DFlexocompressaoActivity.this);
                ky_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(ky_box);
                ky_box.setPadding(100,10,100,10);
                ky_box.canScrollHorizontally(1);

                //text5
                TextView kz = new TextView(DFlexocompressaoActivity.this);
                kz.setText(Html.fromHtml("k<sub><small>z</small></sub>:"));
                linear_scroll.addView(kz);
                kz.setTextSize(17);
                kz.setPadding(0,10,0,10);

                //box5
                final EditText kz_box = new EditText(DFlexocompressaoActivity.this);
                kz_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(kz_box);
                kz_box.setPadding(100,10,100,10);
                kz_box.canScrollHorizontally(1);

                //text6
                TextView Lx = new TextView(DFlexocompressaoActivity.this);
                Lx.setText(Html.fromHtml("L<sub><small>x</small></sub> (cm):"));
                linear_scroll.addView(Lx);
                Lx.setTextSize(17);
                Lx.setPadding(0,10,0,10);

                //box6
                final EditText Lx_box = new EditText(DFlexocompressaoActivity.this);
                Lx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Lx_box);
                Lx_box.setPadding(100,10,100,10);
                Lx_box.canScrollHorizontally(1);

                //text7
                TextView Ly = new TextView(DFlexocompressaoActivity.this);
                Ly.setText(Html.fromHtml("L<sub><small>y</small></sub> (cm):"));
                linear_scroll.addView(Ly);
                Ly.setTextSize(17);
                Ly.setPadding(0,10,0,10);

                //box7
                final EditText Ly_box = new EditText(DFlexocompressaoActivity.this);
                Ly_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Ly_box);
                Ly_box.setPadding(100,10,100,10);
                Ly_box.canScrollHorizontally(1);

                //text8
                TextView Lz = new TextView(DFlexocompressaoActivity.this);
                Lz.setText(Html.fromHtml("L<sub><small>z</small></sub> (cm):"));
                linear_scroll.addView(Lz);
                Lz.setTextSize(17);
                Lz.setPadding(0,10,0,10);

                //box8
                final EditText Lz_box = new EditText(DFlexocompressaoActivity.this);
                Lz_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Lz_box);
                Lz_box.setPadding(100,10,100,10);
                Lz_box.canScrollHorizontally(1);

                //amplificacao
                TextView TV_amp = new TextView(DFlexocompressaoActivity.this);
                TV_amp.setText("AMPLIFICAÇÃO DE MOMENTOS FLETORES");
                TV_amp.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
                TV_amp.setTextSize(25);
                TV_amp.setPadding(0,70,0,0);
                linear_scroll.addView(TV_amp);

                //switch
                LinearLayout switch_layout = new LinearLayout(DFlexocompressaoActivity.this);
                switch_layout.setOrientation(LinearLayout.HORIZONTAL);

                TextView nao = new TextView(DFlexocompressaoActivity.this);
                nao.setText("Não");
                switch_layout.addView(nao);
                Switch amp = new Switch(DFlexocompressaoActivity.this);
                switch_layout.addView(amp);
                TextView sim = new TextView(DFlexocompressaoActivity.this);
                sim.setText("Sim");
                switch_layout.addView(sim);
                switch_layout.setPadding(0,20,0,50);

                linear_scroll.addView(switch_layout);

                final LinearLayout amp_layout = new LinearLayout(DFlexocompressaoActivity.this);
                amp_layout.setOrientation(LinearLayout.VERTICAL);
                linear_scroll.addView(amp_layout);

                //cmx
                final TextView cmx = new TextView(DFlexocompressaoActivity.this);
                cmx.setText(Html.fromHtml("C<sub><small>m,x</small></sub>:"));
                cmx.setTextSize(17);
                cmx.setPadding(0,10,0,10);

                //
                final EditText cmx_box = new EditText(DFlexocompressaoActivity.this);
                cmx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                cmx_box.setPadding(100,10,100,10);
                cmx_box.canScrollHorizontally(1);

                //cmx
                final TextView cmy = new TextView(DFlexocompressaoActivity.this);
                cmy.setText(Html.fromHtml("C<sub><small>m,y</small></sub>:"));
                cmy.setTextSize(17);
                cmy.setPadding(0,10,0,10);

                //
                final EditText cmy_box = new EditText(DFlexocompressaoActivity.this);
                cmy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                cmy_box.setPadding(100,10,100,10);
                cmy_box.canScrollHorizontally(1);

                amp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isAmp = isChecked;
                        if(isChecked)
                        {
                            amp_layout.addView(cmx);
                            amp_layout.addView(cmx_box);
                            amp_layout.addView(cmy);
                            amp_layout.addView(cmy_box);
                        }
                        else
                        {
                            amp_layout.removeAllViews();
                        }
                    }
                });


                Spinner spin_orderby = new Spinner(DFlexocompressaoActivity.this);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DFlexocompressaoActivity.this,R.array.orderby_flex, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_orderby.setAdapter(adapter);
                spin_orderby.setOnItemSelectedListener(new OrderBySpinnerClass());
                spin_orderby.setLayoutParams(new LinearLayout.LayoutParams(800,130));
                spin_orderby.setBackground(getResources().getDrawable(android.R.drawable.btn_dropdown));
                linear_scroll.addView(spin_orderby);

                //final botao
                Button botao_dimens = new Button(DFlexocompressaoActivity.this);
                botao_dimens.setText(R.string.verificar);
                linear_scroll.addView(botao_dimens);

                botao_dimens.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Msdy_value = Msdy_box.getText().toString();
                        String Msdx_value = Msdx_box.getText().toString();
                        String cb_value = cb_box.getText().toString();
                        String lb_value = lb_box.getText().toString();
                        String Ncsd_value = Ncsd_box.getText().toString();
                        String fy_value = fy_box.getText().toString();
                        String kx_value = kx_box.getText().toString();
                        String ky_value = ky_box.getText().toString();
                        String kz_value = kz_box.getText().toString();
                        String Lx_value = Lx_box.getText().toString();
                        String Ly_value = Ly_box.getText().toString();
                        String Lz_value = Lz_box.getText().toString();
                        String cmx_value = cmx_box.getText().toString();
                        String cmy_value = cmy_box.getText().toString();

                        if(Msdx_value.isEmpty() || Msdy_value.isEmpty() || fy_value.isEmpty()
                                || cb_value.isEmpty() || lb_value.isEmpty()
                                || Ncsd_value.isEmpty() || fy_value.isEmpty()
                                || kx_value.isEmpty() || ky_value.isEmpty() || kz_value.isEmpty()
                                || Lx_value.isEmpty() || Ly_value.isEmpty() || Lz_value.isEmpty())
                        {
                            Toast.makeText(DFlexocompressaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else if(Double.parseDouble(cb_value) < 1 || Double.parseDouble(cb_value) > 3)
                        {
                            Toast.makeText(DFlexocompressaoActivity.this, "Cb deve atender: 1.0 ≤ Cb ≤ 3.0", Toast.LENGTH_LONG).show();

                        }
                        else if(isAmp && (cmx_value.isEmpty() || cmy_value.isEmpty()))
                        {
                            Toast.makeText(DFlexocompressaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intent = new Intent(new Intent(DFlexocompressaoActivity.this,OutputDFlexocompressaoActivity.class));
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("amp",isAmp);
                            intent.putExtra("ordem",orderby_selected);
                            if(isAmp)
                            {
                                intent.putExtra("cmx",Double.parseDouble(cmx_value));
                                intent.putExtra("cmy",Double.parseDouble(cmy_value));
                            }

                            intent.putExtra("msdx",Double.parseDouble(Msdx_value));
                            intent.putExtra("msdy",Double.parseDouble(Msdy_value));
                            intent.putExtra("lb",Double.parseDouble(lb_value));
                            intent.putExtra("cb",Double.parseDouble(cb_value));
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
    class OrderBySpinnerClass implements AdapterView.OnItemSelectedListener{
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
            switch(position)
            {
                case 0:
                    orderby_selected = "massa";
                    break;
                case 1:
                    orderby_selected = "d";
                    break;
                case 2:
                    orderby_selected = "bf";
                    break;
                case 3:
                    orderby_selected = "tf";
                    break;
                case 4:
                    orderby_selected = "tw";
                    break;
                case 5:
                    orderby_selected = "area";
                    break;
                case 6:
                    orderby_selected = "ix";
                    break;
                case 7:
                    orderby_selected = "iy";
                    break;
                case 8:
                    orderby_selected = "wx";
                    break;
                case 9:
                    orderby_selected = "wy";
                    break;
                case 10:
                    orderby_selected = "zx";
                    break;
                case 11:
                    orderby_selected = "zy";
                    break;

            }
        }
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("Nothing selected on Spinner OrderBy");
        }
    }

}
