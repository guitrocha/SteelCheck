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
    private int radio_selected = 0;

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
            Intent intent = new Intent(new Intent(VTracaoActivity.this,VCompressaoActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexaoV) {
            Intent intent = new Intent(new Intent(VTracaoActivity.this,VFlexaoActivity.class));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
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
            linear_scroll.removeAllViews();
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
                    spinner_desig.setOnItemSelectedListener(new PerfilSpinnerClass());
                    spinner_desig.setPadding(50,50,50,50);
                    linear_scroll.addView(spinner_desig);

                        //text1
                    TextView Ntsd = new TextView(VTracaoActivity.this);
                    Ntsd.setText(Html.fromHtml("N<sub><small>t,Sd</small></sub> (kN):"));
                    linear_scroll.addView(Ntsd);
                    Ntsd.setTextSize(17);
                    Ntsd.setPadding(0,10,0,10);

                    //box1
                    final EditText Ntsd_box = new EditText(VTracaoActivity.this);
                    Ntsd_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(Ntsd_box);
                    Ntsd_box.setPadding(100,10,100,10);
                    Ntsd_box.canScrollHorizontally(1);
                        //text2
                    TextView fy = new TextView(VTracaoActivity.this);
                    fy.setText(Html.fromHtml("f<sub><small>y</small></sub> (MPa):"));
                    linear_scroll.addView(fy);
                    fy.setTextSize(17);
                    fy.setPadding(0,10,0,10);
                        //box2
                    final EditText fy_box = new EditText(VTracaoActivity.this);
                    fy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(fy_box);
                    fy_box.setPadding(100,10,100,10);
                    fy_box.canScrollHorizontally(1);
                        //text3
                    TextView Lx = new TextView(VTracaoActivity.this);
                    Lx.setText(Html.fromHtml("L<sub><small>x</small></sub> (cm):"));
                    linear_scroll.addView(Lx);
                    Lx.setTextSize(17);
                    Lx.setPadding(0,10,0,10);
                        //box3
                    final EditText Lx_box = new EditText(VTracaoActivity.this);
                    Lx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(Lx_box);
                    Lx_box.setPadding(100,10,100,10);
                    Lx_box.canScrollHorizontally(1);
                        //text4
                    TextView Ly = new TextView(VTracaoActivity.this);
                    Ly.setText(Html.fromHtml("L<sub><small>y</small></sub> (cm):"));
                    linear_scroll.addView(Ly);
                    Ly.setTextSize(17);
                    Ly.setPadding(0,10,0,10);
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

                        //layout auxiliar para componentes gerados a partir do radiogroup
                    final LinearLayout linear_aux_radio = new LinearLayout(VTracaoActivity.this);
                    linear_aux_radio.setOrientation(LinearLayout.VERTICAL);
                    linear_scroll.addView(linear_aux_radio);

                    //text An
                    final TextView An = new TextView(VTracaoActivity.this);
                    An.setText(Html.fromHtml("A<sub><small>n</small></sub> (cm<sup><small>2</small></sup>):"));

                    An.setTextSize(17);
                    An.setPadding(0,10,0,10);
                    //box An
                    final EditText An_box = new EditText(VTracaoActivity.this);
                    An_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    An_box.setPadding(100,10,100,10);
                    An_box.canScrollHorizontally(1);
                    //text ct
                    final TextView Ct = new TextView(VTracaoActivity.this);
                    Ct.setText(Html.fromHtml("C<sub><small>t</small></sub> :"));

                    Ct.setTextSize(17);
                    Ct.setPadding(0,10,0,10);
                    //box ct
                    final EditText Ct_box = new EditText(VTracaoActivity.this);
                    Ct_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    Ct_box.setPadding(100,10,100,10);
                    Ct_box.canScrollHorizontally(1);
                    //text fu
                    final TextView fu = new TextView(VTracaoActivity.this);
                    fu.setText(Html.fromHtml("f<sub><small>u</small></sub> (MPa):"));

                    fu.setTextSize(17);
                    fu.setPadding(0,10,0,10);
                    //box ct
                    final EditText fu_box = new EditText(VTracaoActivity.this);
                    fu_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    fu_box.setPadding(100,10,100,10);
                    fu_box.canScrollHorizontally(1);

                    tipo_analise.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            RadioButton button = (RadioButton) group.findViewById(checkedId);
                            String resposta = button.getText().toString();
                            linear_aux_radio.removeAllViews();
                            if (resposta.equals("Escoamento seção bruta"))
                            {   TextView escoa = new TextView(VTracaoActivity.this);
                                escoa.setText(Html.fromHtml("A<sub><small>n</small></sub> = A<sub><small>g</small></sub>"));
                                linear_aux_radio.addView(escoa);
                                escoa.setTextSize(17);
                                escoa.setPadding(0,10,0,40);
                                radio_selected = 1;

                            }
                            else if (resposta.equals("Ruptura seção líquida"))
                            {
                                linear_aux_radio.addView(An);
                                linear_aux_radio.addView(An_box);
                                linear_aux_radio.addView(Ct);
                                linear_aux_radio.addView(Ct_box);
                                linear_aux_radio.addView(fu);
                                linear_aux_radio.addView(fu_box);
                                Ct_box.setHint("0 < Ct ≤ 1.0");
                                radio_selected = 2;
                            }
                        }
                    });



                    // final

                    Button botao_verificar = new Button(VTracaoActivity.this);
                    botao_verificar.setText(R.string.verificar);
                    linear_scroll.addView(botao_verificar);

                    botao_verificar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String Ntsd_value = Ntsd_box.getText().toString();
                            String fy_value = fy_box.getText().toString();
                            String Lx_value = Lx_box.getText().toString();
                            String Ly_value = Ly_box.getText().toString();
                            String An_value = An_box.getText().toString();
                            String Ct_value = Ct_box.getText().toString();
                            String fu_value = fu_box.getText().toString();

                            if(Ntsd_value.isEmpty() || fy_value.isEmpty()
                                    || Lx_value.isEmpty() || Ly_value.isEmpty()
                                    || tipo_analise.getCheckedRadioButtonId() == -1 )
                            {
                                Toast.makeText(VTracaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                            }
                            else if(perfil_selected_pos == 0)
                            {
                                Toast.makeText(VTracaoActivity.this, R.string.warning_selecionar, Toast.LENGTH_SHORT).show();
                            }
                            else if((radio_selected == 2) &&
                                    (An_value.isEmpty() || Ct_value.isEmpty() || fu_value.isEmpty()))
                            {
                                Toast.makeText(VTracaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                                Intent intent = new Intent(new Intent(VTracaoActivity.this,OutputVTracaoActivity.class));
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("perfil",perfil_selected_pos);
                                intent.putExtra("ntsd", Double.parseDouble(Ntsd_value));
                                intent.putExtra("fy", Double.parseDouble(fy_value));
                                intent.putExtra("lx", Double.parseDouble(Lx_value));
                                intent.putExtra("ly", Double.parseDouble(Ly_value));
                                intent.putExtra("radio", radio_selected);
                                if(radio_selected == 2) {
                                    DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
                                    database.open();
                                    Double ct = Double.parseDouble(Ct_value), an = Double.parseDouble(An_value);
                                    Double area_bruta = database.get_area(perfil_selected_pos);
                                    if(ct <= 0.0 || ct > 1.0)
                                        Toast.makeText(VTracaoActivity.this, "O valor de Ct deve ser: 0<Ct≤1.0", Toast.LENGTH_LONG).show();
                                    else if(an > area_bruta)
                                        Toast.makeText(VTracaoActivity.this, "O valor de An para o perfil " + perfil_selected_str + " deve ser menor ou igual a " + area_bruta, Toast.LENGTH_LONG).show();
                                    else {
                                        intent.putExtra("an", an);
                                        intent.putExtra("ct", ct);
                                        intent.putExtra("fu", Double.parseDouble(fu_value));
                                        startActivity(intent);
                                    }
                                    database.close();

                                }
                                else
                                    startActivity(intent);


                            }
                        }
                    });

                    break;
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

        }
    }


}
