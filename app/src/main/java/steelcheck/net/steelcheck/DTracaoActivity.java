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

public class DTracaoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener{

    private Spinner secao_perfil;
    private String orderby_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtracao);
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

        secao_perfil = (Spinner) findViewById(R.id.secao_dtracao);
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

    /*@Override
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
            Intent intent = new Intent(DTracaoActivity.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_sobre) {
            Intent intent = new Intent(DTracaoActivity.this,SobreActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_contato) {
            Intent intent = new Intent(DTracaoActivity.this,ContatoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_tracaoV) {
            Intent intent = new Intent(DTracaoActivity.this,VTracaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_compressaoV) {
            Intent intent = new Intent(DTracaoActivity.this,VCompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexaoV) {
            Intent intent = new Intent(DTracaoActivity.this,VFlexaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexocompressaoV) {
            Intent intent = new Intent(DTracaoActivity.this,VFlexocompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_tracaoD) {
            //keep activity
        } else if (id == R.id.nav_compressaoD) {
            Intent intent = new Intent(DTracaoActivity.this,DCompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexaoD) {
            Intent intent = new Intent(DTracaoActivity.this,DFlexaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        } else if (id == R.id.nav_flexocompressaoD) {
            Intent intent = new Intent(DTracaoActivity.this,DFlexocompressaoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
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
                    ImageView image = new ImageView(DTracaoActivity.this);
                    image.setImageDrawable(ContextCompat.getDrawable(DTracaoActivity.this, R.drawable.laminado));
                    linear_scroll.addView(image);
                    linear_scroll.setGravity(Gravity.CENTER);

                        //text1
                    TextView Ntsd = new TextView(DTracaoActivity.this);
                    Ntsd.setText(Html.fromHtml("N<sub><small>t,Sd</small></sub> (kN):"));
                    linear_scroll.addView(Ntsd);
                    Ntsd.setTextSize(17);
                    Ntsd.setPadding(0,100,0,10);

                    //box1
                    final EditText Ntsd_box = new EditText(DTracaoActivity.this);
                    Ntsd_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(Ntsd_box);
                    Ntsd_box.setPadding(100,10,100,10);
                    Ntsd_box.canScrollHorizontally(1);
                        //text2
                    TextView fy = new TextView(DTracaoActivity.this);
                    fy.setText(Html.fromHtml("f<sub><small>y</small></sub> (MPa):"));
                    linear_scroll.addView(fy);
                    fy.setTextSize(17);
                    fy.setPadding(0,10,0,10);
                        //box2
                    final EditText fy_box = new EditText(DTracaoActivity.this);
                    fy_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(fy_box);
                    fy_box.setPadding(100,10,100,10);
                    fy_box.canScrollHorizontally(1);
                        //text3
                    TextView Lx = new TextView(DTracaoActivity.this);
                    Lx.setText(Html.fromHtml("L<sub><small>x</small></sub> (cm):"));
                    linear_scroll.addView(Lx);
                    Lx.setTextSize(17);
                    Lx.setPadding(0,10,0,10);
                        //box3
                    final EditText Lx_box = new EditText(DTracaoActivity.this);
                    Lx_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(Lx_box);
                    Lx_box.setPadding(100,10,100,10);
                    Lx_box.canScrollHorizontally(1);
                        //text4
                    TextView Ly = new TextView(DTracaoActivity.this);
                    Ly.setText(Html.fromHtml("L<sub><small>y</small></sub> (cm):"));
                    linear_scroll.addView(Ly);
                    Ly.setTextSize(17);
                    Ly.setPadding(0,10,0,10);
                        //box4
                    final EditText Ly_box = new EditText(DTracaoActivity.this);
                    Ly_box.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    linear_scroll.addView(Ly_box);
                    Ly_box.setPadding(100,10,100,10);
                    Ly_box.canScrollHorizontally(1);

                    TextView TV_ordenar = new TextView(DTracaoActivity.this);
                    TV_ordenar.setText(Html.fromHtml("Escolher perfil em ordem de:"));
                    linear_scroll.addView(TV_ordenar);
                    TV_ordenar.setTextSize(17);
                    TV_ordenar.setPadding(0,100,0,30);

                    Spinner spin_orderby = new Spinner(DTracaoActivity.this);
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(DTracaoActivity.this,R.array.orderby_tracao, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_orderby.setAdapter(adapter);
                    spin_orderby.setOnItemSelectedListener(new OrderBySpinnerClass());
                    spin_orderby.setLayoutParams(new LinearLayout.LayoutParams(800,130));
                    linear_scroll.addView(spin_orderby);
                    // final

                    Button botao_dimens = new Button(DTracaoActivity.this);
                    botao_dimens.setText(R.string.dimensionar);
                    linear_scroll.addView(botao_dimens);

                    botao_dimens.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String Ntsd_value = Ntsd_box.getText().toString();
                            String fy_value = fy_box.getText().toString();
                            String Lx_value = Lx_box.getText().toString();
                            String Ly_value = Ly_box.getText().toString();

                            if(Ntsd_value.isEmpty() || fy_value.isEmpty()
                                    || Lx_value.isEmpty() || Ly_value.isEmpty() )
                            {
                                Toast.makeText(DTracaoActivity.this, R.string.warning_preencher, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                                Intent intent = new Intent(DTracaoActivity.this,OutputDTracaoActivity.class);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                intent.putExtra("ntsd", Double.parseDouble(Ntsd_value));
                                intent.putExtra("fy", Double.parseDouble(fy_value));
                                intent.putExtra("lx", Double.parseDouble(Lx_value));
                                intent.putExtra("ly", Double.parseDouble(Ly_value));
                                intent.putExtra("ordem", orderby_selected);
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
            }
        }
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("Nothing selected on Spinner OrderBy");
        }
    }

}
