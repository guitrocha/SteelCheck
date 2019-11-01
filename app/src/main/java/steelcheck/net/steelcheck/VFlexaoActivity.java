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

public class VFlexaoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    private Spinner secao_flexao;
    private LinearLayout linear_scroll;
    private String perfil_selected_str;
    private int perfil_selected_pos = 0;
    private double d_selected;
    private double tw_selected;
    private double bf_selected;
    private double tf_selected;
    private int analise_selected_pos = 0;
    private LinearLayout analise_auxiliar_layout;
    private TextView vao;
    private EditText vao_box;
    private TextView flechamax;
    private EditText flechamax_box;
    private TextView Vsdx;
    private EditText Vsdx_box;
    private TextView Vsdy;
    private EditText Vsdy_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vflexao);
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

        secao_flexao = (Spinner) findViewById(R.id.secao_vflexao);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.secao_flexao, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secao_flexao.setAdapter(adapter);
        secao_flexao.setOnItemSelectedListener(new SecaoSpinnerClass());


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
            Intent intent = new Intent(VFlexaoActivity.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_sobre) {
            Intent intent = new Intent(VFlexaoActivity.this,SobreActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_contato) {

        } else if (id == R.id.nav_tracaoV) {
            Intent intent = new Intent(VFlexaoActivity.this,VTracaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_compressaoV) {
            Intent intent = new Intent(VFlexaoActivity.this,VCompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexaoV) {
            //keep activity
        } else if (id == R.id.nav_flexocompressaoV) {
            Intent intent = new Intent(VFlexaoActivity.this,VFlexocompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_tracaoD) {
            Intent intent = new Intent(VFlexaoActivity.this,DTracaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_compressaoD) {
            Intent intent = new Intent(VFlexaoActivity.this,DCompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexaoD) {
            Intent intent = new Intent(VFlexaoActivity.this,DFlexaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexocompressaoD) {
            Intent intent = new Intent(VFlexaoActivity.this,DFlexocompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
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

            linear_scroll = (LinearLayout) findViewById(R.id.linear_scroll_idflexao);
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
                    ImageView image = new ImageView(VFlexaoActivity.this);
                    image.setImageDrawable(ContextCompat.getDrawable(VFlexaoActivity.this, R.drawable.laminado));
                    linear_scroll.addView(image);
                    linear_scroll.setGravity(Gravity.CENTER);

                    //perfil spinner
                    Spinner spinner_perfil = new Spinner(VFlexaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(VFlexaoActivity.this, R.array.laminado_perfis, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_perfil.setAdapter(adapter);
                    spinner_perfil.setOnItemSelectedListener(new PerfilSpinnerClass());
                    spinner_perfil.setPadding(50,50,50,50);
                    linear_scroll.addView(spinner_perfil);

                }
                else if(position == 2)
                {

                    //d
                    LinearLayout d_layout = new LinearLayout(VFlexaoActivity.this);
                    d_layout.setOrientation(LinearLayout.HORIZONTAL);
                    d_layout.setGravity(Gravity.CENTER);

                    TextView d = new TextView(VFlexaoActivity.this);
                    d.setText(Html.fromHtml("d  (mm):"));
                    d_layout.addView(d);
                    d.setTextSize(17);
                    d.setPadding(0,10,0,10);

                    Spinner spinner_d = new Spinner(VFlexaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter_d = ArrayAdapter.createFromResource(VFlexaoActivity.this, R.array.d, android.R.layout.simple_spinner_item);
                    adapter_d.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_d.setAdapter(adapter_d);
                    spinner_d.setOnItemSelectedListener(new dSpinnerClass());
                    spinner_d.setLayoutParams(new LinearLayout.LayoutParams(300,130));
                    d_layout.addView(spinner_d);

                    linear_scroll.addView(d_layout);

                    //tw
                    LinearLayout tw_layout = new LinearLayout(VFlexaoActivity.this);
                    tw_layout.setOrientation(LinearLayout.HORIZONTAL);
                    tw_layout.setGravity(Gravity.CENTER);

                    TextView tw = new TextView(VFlexaoActivity.this);
                    tw.setText(Html.fromHtml("t<sub><small>w</small></sub> (mm):"));
                    tw_layout.addView(tw);
                    tw.setTextSize(17);
                    tw.setPadding(0,10,0,10);

                    Spinner spinner_tw = new Spinner(VFlexaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter_tw = ArrayAdapter.createFromResource(VFlexaoActivity.this, R.array.tw, android.R.layout.simple_spinner_item);
                    adapter_tw.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_tw.setAdapter(adapter_tw);
                    spinner_tw.setOnItemSelectedListener(new twSpinnerClass());
                    spinner_tw.setLayoutParams(new LinearLayout.LayoutParams(300,130));
                    tw_layout.addView(spinner_tw);

                    linear_scroll.addView(tw_layout);

                    //bf
                    LinearLayout bf_layout = new LinearLayout(VFlexaoActivity.this);
                    bf_layout.setOrientation(LinearLayout.HORIZONTAL);
                    bf_layout.setGravity(Gravity.CENTER);

                    TextView bf = new TextView(VFlexaoActivity.this);
                    bf.setText(Html.fromHtml("b<sub><small>f</small></sub> (mm):"));
                    bf_layout.addView(bf);
                    bf.setTextSize(17);
                    bf.setPadding(0,10,0,10);

                    Spinner spinner_bf = new Spinner(VFlexaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter_bf = ArrayAdapter.createFromResource(VFlexaoActivity.this, R.array.bf, android.R.layout.simple_spinner_item);
                    adapter_bf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_bf.setAdapter(adapter_bf);
                    spinner_bf.setOnItemSelectedListener(new bfSpinnerClass());
                    spinner_bf.setLayoutParams(new LinearLayout.LayoutParams(300,130));
                    bf_layout.addView(spinner_bf);

                    linear_scroll.addView(bf_layout);

                    //tf
                    LinearLayout tf_layout = new LinearLayout(VFlexaoActivity.this);
                    tf_layout.setOrientation(LinearLayout.HORIZONTAL);
                    tf_layout.setGravity(Gravity.CENTER);

                    TextView tf = new TextView(VFlexaoActivity.this);
                    tf.setText(Html.fromHtml("t<sub><small>f</small></sub> (mm):"));
                    tf_layout.addView(tf);
                    tf.setTextSize(17);
                    tf.setPadding(0,10,0,10);

                    Spinner spinner_tf = new Spinner(VFlexaoActivity.this);
                    final ArrayAdapter<CharSequence> adapter_tf = ArrayAdapter.createFromResource(VFlexaoActivity.this, R.array.tf, android.R.layout.simple_spinner_item);
                    adapter_tf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_tf.setAdapter(adapter_tf);
                    spinner_tf.setOnItemSelectedListener(new tfSpinnerClass());
                    spinner_tf.setLayoutParams(new LinearLayout.LayoutParams(300,130));
                    tf_layout.addView(spinner_tf);


                    linear_scroll.addView(tf_layout);
                }

                //text1
                TextView Msdx = new TextView(VFlexaoActivity.this);
                Msdx.setText(Html.fromHtml("M<sub><small>Sd,x</small></sub> (kNm):"));
                linear_scroll.addView(Msdx);
                Msdx.setTextSize(17);
                Msdx.setPadding(0,100,0,10);

                //box1
                final EditText Msdx_box = new EditText(VFlexaoActivity.this);
                Msdx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Msdx_box);
                Msdx_box.setPadding(100,10,100,10);
                Msdx_box.canScrollHorizontally(1);

                //text1
                TextView Msdy = new TextView(VFlexaoActivity.this);
                Msdy.setText(Html.fromHtml("M<sub><small>Sd,y</small></sub> (kNm):"));
                linear_scroll.addView(Msdy);
                Msdy.setTextSize(17);
                Msdy.setPadding(0,10,0,10);

                //box1
                final EditText Msdy_box = new EditText(VFlexaoActivity.this);
                Msdy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Msdy_box);
                Msdy_box.setPadding(100,10,100,10);
                Msdy_box.canScrollHorizontally(1);

                //text2
                TextView fy = new TextView(VFlexaoActivity.this);
                fy.setText(Html.fromHtml("f<sub><small>y</small></sub> (MPa):"));
                linear_scroll.addView(fy);
                fy.setTextSize(17);
                fy.setPadding(0,10,0,10);

                //box2
                final EditText fy_box = new EditText(VFlexaoActivity.this);
                fy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(fy_box);
                fy_box.setPadding(100,10,100,10);
                fy_box.canScrollHorizontally(1);

                //text3
                TextView lb = new TextView(VFlexaoActivity.this);
                lb.setText(Html.fromHtml("ℓ<sub><small>b</small></sub> (cm):"));
                linear_scroll.addView(lb);
                lb.setTextSize(17);
                lb.setPadding(0,10,0,10);

                //box3
                final EditText lb_box = new EditText(VFlexaoActivity.this);
                lb_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(lb_box);
                lb_box.setPadding(100,10,100,10);
                lb_box.canScrollHorizontally(1);

                //text4
                TextView cb = new TextView(VFlexaoActivity.this);
                cb.setText(Html.fromHtml("C<sub><small>b</small></sub>:"));
                linear_scroll.addView(cb);
                cb.setTextSize(17);
                cb.setPadding(0,10,0,10);
                cb.setHint("1.0 ≤ Cb ≤ 3.0");

                //box4
                final EditText cb_box = new EditText(VFlexaoActivity.this);
                cb_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(cb_box);
                cb_box.setPadding(100,10,100,10);
                cb_box.canScrollHorizontally(1);

                //tipo analise

                TextView analise = new TextView(VFlexaoActivity.this);
                analise.setText(Html.fromHtml("Tipo de análise:"));
                linear_scroll.addView(analise);
                analise.setTextSize(17);
                analise.setPadding(0,100,0,10);
                analise.setTextColor(Color.BLACK);

                Spinner spinner_analise = new Spinner(VFlexaoActivity.this);
                final ArrayAdapter<CharSequence> adapter_analise = ArrayAdapter.createFromResource(VFlexaoActivity.this, R.array.analiseflexao, android.R.layout.simple_spinner_item);
                adapter_analise.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_analise.setAdapter(adapter_analise);
                spinner_analise.setOnItemSelectedListener(new analiseSpinnerClass());
                spinner_analise.setLayoutParams(new LinearLayout.LayoutParams(600,130));
                linear_scroll.addView(spinner_analise);

                analise_auxiliar_layout = new LinearLayout(VFlexaoActivity.this);
                analise_auxiliar_layout.setOrientation(LinearLayout.VERTICAL);
                linear_scroll.addView(analise_auxiliar_layout);

                //text
                Vsdx = new TextView(VFlexaoActivity.this);
                Vsdx.setText(Html.fromHtml("V<sub><small>Sd,x</small></sub> (kN):"));
                Vsdx.setTextSize(17);
                Vsdx.setPadding(0,10,0,10);

                //box
                Vsdx_box = new EditText(VFlexaoActivity.this);
                Vsdx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                Vsdx_box.setPadding(100,10,100,10);
                Vsdx_box.canScrollHorizontally(1);

                //text
                Vsdy = new TextView(VFlexaoActivity.this);
                Vsdy.setText(Html.fromHtml("V<sub><small>Sd,y</small></sub> (kN):"));
                Vsdy.setTextSize(17);
                Vsdy.setPadding(0,10,0,10);

                //box
                Vsdy_box = new EditText(VFlexaoActivity.this);
                Vsdy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                Vsdy_box.setPadding(100,10,100,10);
                Vsdy_box.canScrollHorizontally(1);

                //text
                flechamax = new TextView(VFlexaoActivity.this);
                flechamax.setText(Html.fromHtml("δ<sub><small>max</small></sub> (mm):"));
                flechamax.setTextSize(17);
                flechamax.setPadding(0,10,0,10);

                //box
                flechamax_box = new EditText(VFlexaoActivity.this);
                flechamax_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                flechamax_box.setPadding(100,10,100,10);
                flechamax_box.canScrollHorizontally(1);

                //text
                vao = new TextView(VFlexaoActivity.this);
                vao.setText(Html.fromHtml("Vão<sub><small>max</small></sub> (m):"));
                vao.setTextSize(17);
                vao.setPadding(0,10,0,10);

                //box
                vao_box = new EditText(VFlexaoActivity.this);
                vao_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                vao_box.setPadding(100,10,100,10);
                vao_box.canScrollHorizontally(1);

                //final botao
                Button botao_verificar = new Button(VFlexaoActivity.this);
                botao_verificar.setText(R.string.verificar);
                linear_scroll.addView(botao_verificar);

                botao_verificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Msdy_value = Msdy_box.getText().toString();
                        String Msdx_value = Msdx_box.getText().toString();
                        String fy_value = fy_box.getText().toString();
                        String cb_value = cb_box.getText().toString();
                        String lb_value = lb_box.getText().toString();
                        String vsdx_value = Vsdx_box.getText().toString();
                        String vsdy_value = Vsdy_box.getText().toString();
                        String vao_value = vao_box.getText().toString();
                        String flecha_value = flechamax_box.getText().toString();

                        if(Msdx_value.isEmpty() || Msdy_value.isEmpty() || fy_value.isEmpty()
                                || cb_value.isEmpty() || lb_value.isEmpty()  )
                        {
                            Toast.makeText(VFlexaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else if(Double.parseDouble(cb_value) < 1 || Double.parseDouble(cb_value) > 3)
                        {
                            Toast.makeText(VFlexaoActivity.this, "Cb deve atender: 1.0 ≤ Cb ≤ 3.0", Toast.LENGTH_LONG).show();

                        }
                        else if(position == 1 && perfil_selected_pos == 0)
                        {
                            Toast.makeText(VFlexaoActivity.this, R.string.warning_selecionar, Toast.LENGTH_SHORT).show();
                        }
                        else if(analise_selected_pos == 1 && (vsdx_value.isEmpty() || vsdy_value.isEmpty()))
                        {
                            Toast.makeText(VFlexaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else if(analise_selected_pos == 2 && (vao_value.isEmpty() || flecha_value.isEmpty()))
                        {
                            Toast.makeText(VFlexaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else if(analise_selected_pos == 3 && (vsdx_value.isEmpty() || vsdy_value.isEmpty() || vao_value.isEmpty() || flecha_value.isEmpty()))
                        {
                            Toast.makeText(VFlexaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intent = new Intent(new Intent(VFlexaoActivity.this,OutputVFlexaoActivity.class));
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
                            if(analise_selected_pos == 1)
                            {
                                intent.putExtra("vsdx",Double.parseDouble(vsdx_value));
                                intent.putExtra("vsdy",Double.parseDouble(vsdy_value));
                            }
                            else if(analise_selected_pos == 2)
                            {
                                intent.putExtra("flecha",Double.parseDouble(flecha_value));
                                intent.putExtra("vao",Double.parseDouble(vao_value));
                            }
                            else if(analise_selected_pos == 3)
                            {
                                intent.putExtra("vsdx",Double.parseDouble(vsdx_value));
                                intent.putExtra("vsdy",Double.parseDouble(vsdy_value));
                                intent.putExtra("flecha",Double.parseDouble(flecha_value));
                                intent.putExtra("vao",Double.parseDouble(vao_value));
                            }
                            intent.putExtra("msdx",Double.parseDouble(Msdx_value));
                            intent.putExtra("msdy",Double.parseDouble(Msdy_value));
                            intent.putExtra("fy",Double.parseDouble(fy_value));
                            intent.putExtra("lb",Double.parseDouble(lb_value));
                            intent.putExtra("cb",Double.parseDouble(cb_value));

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
    class analiseSpinnerClass implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            String analise = parent.getAdapter().getItem(position).toString();
            analise_selected_pos = position;
            System.out.println(analise_selected_pos);
            if(position == 0)
            {
                analise_auxiliar_layout.removeAllViews();
            }
            else
            {


                analise_auxiliar_layout.removeAllViews();

                if(position == 1)
                {
                    analise_auxiliar_layout.addView(Vsdx);
                    analise_auxiliar_layout.addView(Vsdx_box);
                    analise_auxiliar_layout.addView(Vsdy);
                    analise_auxiliar_layout.addView(Vsdy_box);
                }
                else if(position == 2)
                {
                    analise_auxiliar_layout.addView(flechamax);
                    analise_auxiliar_layout.addView(flechamax_box);
                    analise_auxiliar_layout.addView(vao);
                    analise_auxiliar_layout.addView(vao_box);
                }
                else if(position == 3)
                {
                    analise_auxiliar_layout.addView(Vsdx);
                    analise_auxiliar_layout.addView(Vsdx_box);
                    analise_auxiliar_layout.addView(Vsdy);
                    analise_auxiliar_layout.addView(Vsdy_box);
                    analise_auxiliar_layout.addView(flechamax);
                    analise_auxiliar_layout.addView(flechamax_box);
                    analise_auxiliar_layout.addView(vao);
                    analise_auxiliar_layout.addView(vao_box);
                }
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("Nothing selected on Spinner analise");
        }
    }
}
