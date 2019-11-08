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

public class DFlexaoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    private Spinner secao_flexao;
    private LinearLayout linear_scroll;
    private String orderby_selected;
    private LinearLayout analise_auxiliar_layout;
    private TextView vao;
    private EditText vao_box;
    private TextView flechamax;
    private EditText flechamax_box;
    private TextView Vsdx;
    private EditText Vsdx_box;
    private TextView Vsdy;
    private EditText Vsdy_box;
    private int analise_selected_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dflexao);
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

        secao_flexao = (Spinner) findViewById(R.id.secao_dflexao);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.secao_perfil, android.R.layout.simple_spinner_item);
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
            Intent intent = new Intent(DFlexaoActivity.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_sobre) {
            Intent intent = new Intent(DFlexaoActivity.this,SobreActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_contato) {
            Intent intent = new Intent(DFlexaoActivity.this,ContatoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_tracaoV) {
            Intent intent = new Intent(DFlexaoActivity.this,VTracaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_compressaoV) {
            Intent intent = new Intent(DFlexaoActivity.this,VCompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexaoV) {
            Intent intent = new Intent(DFlexaoActivity.this,VFlexaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexocompressaoV) {
            Intent intent = new Intent(DFlexaoActivity.this,VFlexocompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_tracaoD) {
            Intent intent = new Intent(DFlexaoActivity.this,DTracaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_compressaoD) {
            Intent intent = new Intent(DFlexaoActivity.this,DCompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexaoD) {
            //keep activity
        } else if (id == R.id.nav_flexocompressaoD) {
            Intent intent = new Intent(DFlexaoActivity.this,DFlexocompressaoActivity.class);
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
                    ImageView image = new ImageView(DFlexaoActivity.this);
                    image.setImageDrawable(ContextCompat.getDrawable(DFlexaoActivity.this, R.drawable.laminado));
                    linear_scroll.addView(image);
                    linear_scroll.setGravity(Gravity.CENTER);


                }


                //text1
                TextView Msdx = new TextView(DFlexaoActivity.this);
                Msdx.setText(Html.fromHtml("M<sub><small>Sd,x</small></sub> (kNm):"));
                linear_scroll.addView(Msdx);
                Msdx.setTextSize(17);
                Msdx.setPadding(0,100,0,10);

                //box1
                final EditText Msdx_box = new EditText(DFlexaoActivity.this);
                Msdx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Msdx_box);
                Msdx_box.setPadding(100,10,100,10);
                Msdx_box.canScrollHorizontally(1);

                //text1
                TextView Msdy = new TextView(DFlexaoActivity.this);
                Msdy.setText(Html.fromHtml("M<sub><small>Sd,y</small></sub> (kNm):"));
                linear_scroll.addView(Msdy);
                Msdy.setTextSize(17);
                Msdy.setPadding(0,10,0,10);

                //box1
                final EditText Msdy_box = new EditText(DFlexaoActivity.this);
                Msdy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(Msdy_box);
                Msdy_box.setPadding(100,10,100,10);
                Msdy_box.canScrollHorizontally(1);

                //text2
                TextView fy = new TextView(DFlexaoActivity.this);
                fy.setText(Html.fromHtml("f<sub><small>y</small></sub> (MPa):"));
                linear_scroll.addView(fy);
                fy.setTextSize(17);
                fy.setPadding(0,10,0,10);

                //box2
                final EditText fy_box = new EditText(DFlexaoActivity.this);
                fy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(fy_box);
                fy_box.setPadding(100,10,100,10);
                fy_box.canScrollHorizontally(1);

                //text3
                TextView lb = new TextView(DFlexaoActivity.this);
                lb.setText(Html.fromHtml("ℓ<sub><small>b</small></sub> (cm):"));
                linear_scroll.addView(lb);
                lb.setTextSize(17);
                lb.setPadding(0,10,0,10);

                //box3
                final EditText lb_box = new EditText(DFlexaoActivity.this);
                lb_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(lb_box);
                lb_box.setPadding(100,10,100,10);
                lb_box.canScrollHorizontally(1);

                //text4
                TextView cb = new TextView(DFlexaoActivity.this);
                cb.setText(Html.fromHtml("C<sub><small>b</small></sub>:"));
                linear_scroll.addView(cb);
                cb.setTextSize(17);
                cb.setPadding(0,10,0,10);
                cb.setHint("1.0 ≤ Cb ≤ 3.0");

                //box4
                final EditText cb_box = new EditText(DFlexaoActivity.this);
                cb_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                linear_scroll.addView(cb_box);
                cb_box.setPadding(100,10,100,10);
                cb_box.canScrollHorizontally(1);

                //tipo analise

                TextView analise = new TextView(DFlexaoActivity.this);
                analise.setText(Html.fromHtml("Tipo de análise:"));
                linear_scroll.addView(analise);
                analise.setTextSize(17);
                analise.setPadding(0,100,0,10);
                analise.setTextColor(Color.BLACK);

                Spinner spinner_analise = new Spinner(DFlexaoActivity.this);
                final ArrayAdapter<CharSequence> adapter_analise = ArrayAdapter.createFromResource(DFlexaoActivity.this, R.array.analiseflexao, android.R.layout.simple_spinner_item);
                adapter_analise.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_analise.setAdapter(adapter_analise);
                spinner_analise.setOnItemSelectedListener(new analiseSpinnerClass());
                spinner_analise.setLayoutParams(new LinearLayout.LayoutParams(600,130));
                linear_scroll.addView(spinner_analise);

                analise_auxiliar_layout = new LinearLayout(DFlexaoActivity.this);
                analise_auxiliar_layout.setOrientation(LinearLayout.VERTICAL);
                linear_scroll.addView(analise_auxiliar_layout);

                //text
                Vsdx = new TextView(DFlexaoActivity.this);
                Vsdx.setText(Html.fromHtml("V<sub><small>Sd,x</small></sub> (kN):"));
                Vsdx.setTextSize(17);
                Vsdx.setPadding(0,10,0,10);

                //box
                Vsdx_box = new EditText(DFlexaoActivity.this);
                Vsdx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                Vsdx_box.setPadding(100,10,100,10);
                Vsdx_box.canScrollHorizontally(1);

                //text
                Vsdy = new TextView(DFlexaoActivity.this);
                Vsdy.setText(Html.fromHtml("V<sub><small>Sd,y</small></sub> (kN):"));
                Vsdy.setTextSize(17);
                Vsdy.setPadding(0,10,0,10);

                //box
                Vsdy_box = new EditText(DFlexaoActivity.this);
                Vsdy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                Vsdy_box.setPadding(100,10,100,10);
                Vsdy_box.canScrollHorizontally(1);

                //text
                flechamax = new TextView(DFlexaoActivity.this);
                flechamax.setText(Html.fromHtml("δ<sub><small>max</small></sub> (mm):"));
                flechamax.setTextSize(17);
                flechamax.setPadding(0,10,0,10);

                //box
                flechamax_box = new EditText(DFlexaoActivity.this);
                flechamax_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                flechamax_box.setPadding(100,10,100,10);
                flechamax_box.canScrollHorizontally(1);

                //text
                vao = new TextView(DFlexaoActivity.this);
                vao.setText(Html.fromHtml("Vão<sub><small>max</small></sub> (m):"));
                vao.setTextSize(17);
                vao.setPadding(0,10,0,10);

                //box
                vao_box = new EditText(DFlexaoActivity.this);
                vao_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                vao_box.setPadding(100,10,100,10);
                vao_box.canScrollHorizontally(1);

                TextView TV_ordenar = new TextView(DFlexaoActivity.this);
                TV_ordenar.setText(Html.fromHtml("Escolher perfil em ordem de:"));
                linear_scroll.addView(TV_ordenar);
                TV_ordenar.setTextSize(17);
                TV_ordenar.setPadding(0,100,0,30);

                Spinner spin_orderby = new Spinner(DFlexaoActivity.this);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DFlexaoActivity.this,R.array.orderby_flex, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin_orderby.setAdapter(adapter);
                spin_orderby.setOnItemSelectedListener(new OrderBySpinnerClass());
                spin_orderby.setLayoutParams(new LinearLayout.LayoutParams(800,130));
                linear_scroll.addView(spin_orderby);

                //final botao
                Button botao_dimens = new Button(DFlexaoActivity.this);
                botao_dimens.setText(R.string.dimensionar);
                linear_scroll.addView(botao_dimens);

                botao_dimens.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(DFlexaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else if(Double.parseDouble(cb_value) < 1 || Double.parseDouble(cb_value) > 3)
                        {
                            Toast.makeText(DFlexaoActivity.this, "Cb deve atender: 1.0 ≤ Cb ≤ 3.0", Toast.LENGTH_LONG).show();

                        }
                        else if(analise_selected_pos == 1 && (vsdx_value.isEmpty() || vsdy_value.isEmpty()))
                        {
                            Toast.makeText(DFlexaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else if(analise_selected_pos == 2 && (vao_value.isEmpty() || flecha_value.isEmpty()))
                        {
                            Toast.makeText(DFlexaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else if(analise_selected_pos == 3 && (vsdx_value.isEmpty() || vsdy_value.isEmpty() || vao_value.isEmpty() || flecha_value.isEmpty()))
                        {
                            Toast.makeText(DFlexaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent intent = new Intent(new Intent(DFlexaoActivity.this,OutputDFlexaoActivity.class));
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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
                            intent.putExtra("ordem", orderby_selected);
                            intent.putExtra("analise",analise_selected_pos);
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
