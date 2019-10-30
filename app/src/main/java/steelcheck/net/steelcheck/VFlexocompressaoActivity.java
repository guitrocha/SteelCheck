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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class VFlexocompressaoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    private Spinner secao_flexocomp;
    private LinearLayout linear_scroll;
    private String perfil_selected_str;
    private int perfil_selected_pos = 0;
    private double d_selected;
    private double tw_selected;
    private double bf_selected;
    private double tf_selected;
    private int analise_selected_pos = 0;
    private boolean isAmp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vflexocompressao);
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

        secao_flexocomp = (Spinner) findViewById(R.id.secao_vflexocompressao);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.secao_flexocompressao, android.R.layout.simple_spinner_item);
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
            Intent intent = new Intent(new Intent(VFlexocompressaoActivity.this,HomeActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_sobre) {
            Intent intent = new Intent(new Intent(VFlexocompressaoActivity.this,SobreActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_contato) {

        } else if (id == R.id.nav_tracaoV) {
            Intent intent = new Intent(new Intent(VFlexocompressaoActivity.this,VTracaoActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_compressaoV) {
            Intent intent = new Intent(new Intent(VFlexocompressaoActivity.this,VCompressaoActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexaoV) {
            Intent intent = new Intent(new Intent(VFlexocompressaoActivity.this,VFlexaoActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexocompressaoV) {
            //keep activity
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

            linear_scroll = (LinearLayout) findViewById(R.id.linear_scroll_idflexocompressao);
            linear_scroll.setGravity(Gravity.CENTER);
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
                    ImageView image = new ImageView(VFlexocompressaoActivity.this);
                    image.setImageDrawable(ContextCompat.getDrawable(VFlexocompressaoActivity.this, R.drawable.laminado));
                    linear_scroll.addView(image);
                    linear_scroll.setGravity(Gravity.CENTER);

                    //perfil spinner
                    Spinner spinner_perfil = new Spinner(VFlexocompressaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(VFlexocompressaoActivity.this, R.array.laminado_perfis, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_perfil.setAdapter(adapter);
                    spinner_perfil.setOnItemSelectedListener(new PerfilSpinnerClass());
                    spinner_perfil.setPadding(50,50,50,50);
                    linear_scroll.addView(spinner_perfil);

                }
                else if(position == 2)
                {

                    //d
                    LinearLayout d_layout = new LinearLayout(VFlexocompressaoActivity.this);
                    d_layout.setOrientation(LinearLayout.HORIZONTAL);
                    d_layout.setGravity(Gravity.CENTER);

                    TextView d = new TextView(VFlexocompressaoActivity.this);
                    d.setText(Html.fromHtml("d  (mm):"));
                    d_layout.addView(d);
                    d.setTextSize(17);
                    d.setPadding(0,10,0,10);

                    Spinner spinner_d = new Spinner(VFlexocompressaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter_d = ArrayAdapter.createFromResource(VFlexocompressaoActivity.this, R.array.d, android.R.layout.simple_spinner_item);
                    adapter_d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_d.setAdapter(adapter_d);
                    spinner_d.setOnItemSelectedListener(new dSpinnerClass());
                    spinner_d.setLayoutParams(new LinearLayout.LayoutParams(300,130));
                    d_layout.addView(spinner_d);

                    linear_scroll.addView(d_layout);

                    //tw
                    LinearLayout tw_layout = new LinearLayout(VFlexocompressaoActivity.this);
                    tw_layout.setOrientation(LinearLayout.HORIZONTAL);
                    tw_layout.setGravity(Gravity.CENTER);

                    TextView tw = new TextView(VFlexocompressaoActivity.this);
                    tw.setText(Html.fromHtml("t<sub><small>w</small></sub> (mm):"));
                    tw_layout.addView(tw);
                    tw.setTextSize(17);
                    tw.setPadding(0,10,0,10);

                    Spinner spinner_tw = new Spinner(VFlexocompressaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter_tw = ArrayAdapter.createFromResource(VFlexocompressaoActivity.this, R.array.tw, android.R.layout.simple_spinner_item);
                    adapter_tw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_tw.setAdapter(adapter_tw);
                    spinner_tw.setOnItemSelectedListener(new twSpinnerClass());
                    spinner_tw.setLayoutParams(new LinearLayout.LayoutParams(300,130));
                    tw_layout.addView(spinner_tw);

                    linear_scroll.addView(tw_layout);

                    //bf
                    LinearLayout bf_layout = new LinearLayout(VFlexocompressaoActivity.this);
                    bf_layout.setOrientation(LinearLayout.HORIZONTAL);
                    bf_layout.setGravity(Gravity.CENTER);

                    TextView bf = new TextView(VFlexocompressaoActivity.this);
                    bf.setText(Html.fromHtml("b<sub><small>f</small></sub> (mm):"));
                    bf_layout.addView(bf);
                    bf.setTextSize(17);
                    bf.setPadding(0,10,0,10);

                    Spinner spinner_bf = new Spinner(VFlexocompressaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter_bf = ArrayAdapter.createFromResource(VFlexocompressaoActivity.this, R.array.bf, android.R.layout.simple_spinner_item);
                    adapter_bf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_bf.setAdapter(adapter_bf);
                    spinner_bf.setOnItemSelectedListener(new bfSpinnerClass());
                    spinner_bf.setLayoutParams(new LinearLayout.LayoutParams(300,130));
                    bf_layout.addView(spinner_bf);

                    linear_scroll.addView(bf_layout);

                    //tf
                    LinearLayout tf_layout = new LinearLayout(VFlexocompressaoActivity.this);
                    tf_layout.setOrientation(LinearLayout.HORIZONTAL);
                    tf_layout.setGravity(Gravity.CENTER);

                    TextView tf = new TextView(VFlexocompressaoActivity.this);
                    tf.setText(Html.fromHtml("t<sub><small>f</small></sub> (mm):"));
                    tf_layout.addView(tf);
                    tf.setTextSize(17);
                    tf.setPadding(0,10,0,10);

                    Spinner spinner_tf = new Spinner(VFlexocompressaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter_tf = ArrayAdapter.createFromResource(VFlexocompressaoActivity.this, R.array.tf, android.R.layout.simple_spinner_item);
                    adapter_tf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_tf.setAdapter(adapter_tf);
                    spinner_tf.setOnItemSelectedListener(new tfSpinnerClass());
                    spinner_tf.setLayoutParams(new LinearLayout.LayoutParams(300,130));
                    tf_layout.addView(spinner_tf);


                    linear_scroll.addView(tf_layout);
                }

                //viga
                TextView TV_viga = new TextView(VFlexocompressaoActivity.this);
                TV_viga.setText("DADOS DA VIGA");
                TV_viga.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
                TV_viga.setTextSize(25);
                TV_viga.setPadding(0,70,0,0);
                linear_scroll.addView(TV_viga);

                //text1
                TextView Msdx = new TextView(VFlexocompressaoActivity.this);
                Msdx.setText(Html.fromHtml("M<sub><small>Sd,x</small></sub> (kNm):"));
                linear_scroll.addView(Msdx);
                Msdx.setTextSize(17);
                Msdx.setPadding(0,100,0,10);

                //box1
                final EditText Msdx_box = new EditText(VFlexocompressaoActivity.this);
                Msdx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Msdx_box);
                Msdx_box.setPadding(100,10,100,10);
                Msdx_box.canScrollHorizontally(1);

                //text1
                TextView Msdy = new TextView(VFlexocompressaoActivity.this);
                Msdy.setText(Html.fromHtml("M<sub><small>Sd,y</small></sub> (kNm):"));
                linear_scroll.addView(Msdy);
                Msdy.setTextSize(17);
                Msdy.setPadding(0,10,0,10);

                //box1
                final EditText Msdy_box = new EditText(VFlexocompressaoActivity.this);
                Msdy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Msdy_box);
                Msdy_box.setPadding(100,10,100,10);
                Msdy_box.canScrollHorizontally(1);

                //text3
                TextView lb = new TextView(VFlexocompressaoActivity.this);
                lb.setText(Html.fromHtml("ℓ<sub><small>b</small></sub> (cm):"));
                linear_scroll.addView(lb);
                lb.setTextSize(17);
                lb.setPadding(0,10,0,10);

                //box3
                final EditText lb_box = new EditText(VFlexocompressaoActivity.this);
                lb_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(lb_box);
                lb_box.setPadding(100,10,100,10);
                lb_box.canScrollHorizontally(1);

                //text4
                TextView cb = new TextView(VFlexocompressaoActivity.this);
                cb.setText(Html.fromHtml("C<sub><small>b</small></sub>:"));
                linear_scroll.addView(cb);
                cb.setTextSize(17);
                cb.setPadding(0,10,0,10);
                cb.setHint("1.0 ≤ Cb ≤ 3.0");

                //box4
                final EditText cb_box = new EditText(VFlexocompressaoActivity.this);
                cb_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(cb_box);
                cb_box.setPadding(100,10,100,10);
                cb_box.canScrollHorizontally(1);

                //coluna
                TextView TV_coluna = new TextView(VFlexocompressaoActivity.this);
                TV_coluna.setText("DADOS DA COLUNA");
                TV_coluna.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
                TV_coluna.setTextSize(25);
                TV_coluna.setPadding(0,70,0,0);
                linear_scroll.addView(TV_coluna);

                //text1
                TextView Ncsd = new TextView(VFlexocompressaoActivity.this);
                Ncsd.setText(Html.fromHtml("N<sub><small>c,Sd</small></sub> (kN):"));
                linear_scroll.addView(Ncsd);
                Ncsd.setTextSize(17);
                Ncsd.setPadding(0,100,0,10);

                //box1
                final EditText Ncsd_box = new EditText(VFlexocompressaoActivity.this);
                Ncsd_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Ncsd_box);
                Ncsd_box.setPadding(100,10,100,10);
                Ncsd_box.canScrollHorizontally(1);

                //text2
                TextView fy = new TextView(VFlexocompressaoActivity.this);
                fy.setText(Html.fromHtml("f<sub><small>y</small></sub> (MPa):"));
                linear_scroll.addView(fy);
                fy.setTextSize(17);
                fy.setPadding(0,10,0,10);

                //box2
                final EditText fy_box = new EditText(VFlexocompressaoActivity.this);
                fy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(fy_box);
                fy_box.setPadding(100,10,100,10);
                fy_box.canScrollHorizontally(1);

                //text3
                TextView kx = new TextView(VFlexocompressaoActivity.this);
                kx.setText(Html.fromHtml("k<sub><small>x</small></sub>:"));
                linear_scroll.addView(kx);
                kx.setTextSize(17);
                kx.setPadding(0,10,0,10);

                //box3
                final EditText kx_box = new EditText(VFlexocompressaoActivity.this);
                kx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(kx_box);
                kx_box.setPadding(100,10,100,10);
                kx_box.canScrollHorizontally(1);

                //text4
                TextView ky = new TextView(VFlexocompressaoActivity.this);
                ky.setText(Html.fromHtml("k<sub><small>y</small></sub>:"));
                linear_scroll.addView(ky);
                ky.setTextSize(17);
                ky.setPadding(0,10,0,10);

                //box4
                final EditText ky_box = new EditText(VFlexocompressaoActivity.this);
                ky_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(ky_box);
                ky_box.setPadding(100,10,100,10);
                ky_box.canScrollHorizontally(1);

                //text5
                TextView kz = new TextView(VFlexocompressaoActivity.this);
                kz.setText(Html.fromHtml("k<sub><small>z</small></sub>:"));
                linear_scroll.addView(kz);
                kz.setTextSize(17);
                kz.setPadding(0,10,0,10);

                //box5
                final EditText kz_box = new EditText(VFlexocompressaoActivity.this);
                kz_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(kz_box);
                kz_box.setPadding(100,10,100,10);
                kz_box.canScrollHorizontally(1);

                //text6
                TextView Lx = new TextView(VFlexocompressaoActivity.this);
                Lx.setText(Html.fromHtml("L<sub><small>x</small></sub> (cm):"));
                linear_scroll.addView(Lx);
                Lx.setTextSize(17);
                Lx.setPadding(0,10,0,10);

                //box6
                final EditText Lx_box = new EditText(VFlexocompressaoActivity.this);
                Lx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Lx_box);
                Lx_box.setPadding(100,10,100,10);
                Lx_box.canScrollHorizontally(1);

                //text7
                TextView Ly = new TextView(VFlexocompressaoActivity.this);
                Ly.setText(Html.fromHtml("L<sub><small>y</small></sub> (cm):"));
                linear_scroll.addView(Ly);
                Ly.setTextSize(17);
                Ly.setPadding(0,10,0,10);

                //box7
                final EditText Ly_box = new EditText(VFlexocompressaoActivity.this);
                Ly_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Ly_box);
                Ly_box.setPadding(100,10,100,10);
                Ly_box.canScrollHorizontally(1);

                //text8
                TextView Lz = new TextView(VFlexocompressaoActivity.this);
                Lz.setText(Html.fromHtml("L<sub><small>z</small></sub> (cm):"));
                linear_scroll.addView(Lz);
                Lz.setTextSize(17);
                Lz.setPadding(0,10,0,10);

                //box8
                final EditText Lz_box = new EditText(VFlexocompressaoActivity.this);
                Lz_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Lz_box);
                Lz_box.setPadding(100,10,100,10);
                Lz_box.canScrollHorizontally(1);

                //amplificacao
                TextView TV_amp = new TextView(VFlexocompressaoActivity.this);
                TV_amp.setText("AMPLIFICAÇÃO DE MOMENTOS FLETORES");
                TV_amp.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
                TV_amp.setTextSize(25);
                TV_amp.setPadding(0,70,0,0);
                linear_scroll.addView(TV_amp);

                //switch
                LinearLayout switch_layout = new LinearLayout(VFlexocompressaoActivity.this);
                switch_layout.setOrientation(LinearLayout.HORIZONTAL);

                TextView nao = new TextView(VFlexocompressaoActivity.this);
                nao.setText("Não");
                switch_layout.addView(nao);
                Switch amp = new Switch(VFlexocompressaoActivity.this);
                switch_layout.addView(amp);
                TextView sim = new TextView(VFlexocompressaoActivity.this);
                sim.setText("Sim");
                switch_layout.addView(sim);
                switch_layout.setPadding(0,20,0,50);

                linear_scroll.addView(switch_layout);

                final LinearLayout amp_layout = new LinearLayout(VFlexocompressaoActivity.this);
                amp_layout.setOrientation(LinearLayout.VERTICAL);
                linear_scroll.addView(amp_layout);

                //cmx
                final TextView cmx = new TextView(VFlexocompressaoActivity.this);
                cmx.setText(Html.fromHtml("C<sub><small>m,x</small></sub>:"));
                cmx.setTextSize(17);
                cmx.setPadding(0,10,0,10);

                //
                final EditText cmx_box = new EditText(VFlexocompressaoActivity.this);
                cmx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                cmx_box.setPadding(100,10,100,10);
                cmx_box.canScrollHorizontally(1);

                //cmx
                final TextView cmy = new TextView(VFlexocompressaoActivity.this);
                cmy.setText(Html.fromHtml("C<sub><small>m,y</small></sub>:"));
                cmy.setTextSize(17);
                cmy.setPadding(0,10,0,10);

                //
                final EditText cmy_box = new EditText(VFlexocompressaoActivity.this);
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




                //final botao
                Button botao_verificar = new Button(VFlexocompressaoActivity.this);
                botao_verificar.setText(R.string.verificar);
                linear_scroll.addView(botao_verificar);

                botao_verificar.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(VFlexocompressaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else if(Double.parseDouble(cb_value) < 1 || Double.parseDouble(cb_value) > 3)
                        {
                            Toast.makeText(VFlexocompressaoActivity.this, "Cb deve atender: 1.0 ≤ Cb ≤ 3.0", Toast.LENGTH_LONG).show();

                        }
                        else if(position == 1 && perfil_selected_pos == 0)
                        {
                            Toast.makeText(VFlexocompressaoActivity.this, R.string.warning_selecionar, Toast.LENGTH_SHORT).show();
                        }
                        else if(isAmp && (cmx_value.isEmpty() || cmy_value.isEmpty()))
                        {
                            Toast.makeText(VFlexocompressaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intent = new Intent(new Intent(VFlexocompressaoActivity.this,OutputVFlexaoActivity.class));
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            intent.putExtra("secao",position);
                            intent.putExtra("analise",analise_selected_pos);
                            if(position == 1)
                                intent.putExtra("perfil",perfil_selected_pos);
                            else if(position == 2)
                            {
                                intent.putExtra("d",d_selected);
                                intent.putExtra("tw",tw_selected);
                                intent.putExtra("bf",bf_selected);
                                intent.putExtra("tf",tf_selected);
                            }
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
    class dSpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String d = parent.getAdapter().getItem(position).toString();
            d_selected = Double.parseDouble(d);
            System.out.println(d_selected);

        }

        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("Nothing selected on Spinner d");
        }
    }
    class twSpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String tw = parent.getAdapter().getItem(position).toString();
            tw_selected = Double.parseDouble(tw);
            System.out.println(tw_selected);

        }

        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("Nothing selected on Spinner tw");
        }
    }
    class bfSpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String bf = parent.getAdapter().getItem(position).toString();
            bf_selected = Double.parseDouble(bf);
            System.out.println(bf_selected);

        }

        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("Nothing selected on Spinner bf");
        }
    }
    class tfSpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String tf = parent.getAdapter().getItem(position).toString();
            tf_selected = Double.parseDouble(tf);
            System.out.println(tf_selected);

        }

        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("Nothing selected on Spinner tf");
        }
    }

}
