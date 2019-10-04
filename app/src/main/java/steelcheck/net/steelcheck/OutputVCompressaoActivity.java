package steelcheck.net.steelcheck;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class OutputVCompressaoActivity extends AppCompatActivity {
    private final double gama_a1 = 1.10;
    private final double gama_a2 = 1.35;

    private LinearLayout scroll_results;
    private int plus_controler = 0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_vcompressao);
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK); // api21+
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
        Bundle extras = getIntent().getExtras();
        DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
        database.open();


        if(extras != null)
        {
            double NCSD = extras.getDouble("ncsd");
            double fy = extras.getDouble("fy");
            double kx = extras.getDouble("kx");
            double ky = extras.getDouble("ky");
            double kz = extras.getDouble("kz");
            double lx = extras.getDouble("lx");
            double ly = extras.getDouble("ly");
            double lz = extras.getDouble("lz");
            int secao = extras.getInt("secao");
            if(secao == 1)
            {
                int perfil_selected_pos = extras.getInt("perfil");
                Show_Results_LaminadoW(database.get_perfil(perfil_selected_pos),fy,database.get_zx(perfil_selected_pos),
                        database.get_iy(perfil_selected_pos),database.get_j(perfil_selected_pos),database.get_cw(perfil_selected_pos),
                        database.get_wx(perfil_selected_pos),database.get_mesa(perfil_selected_pos),database.get_aba(perfil_selected_pos),
                        database.get_rx(perfil_selected_pos),database.get_ry(perfil_selected_pos),database.get_area(perfil_selected_pos),
                        NCSD,kx,ky,kz,lx,ly,lz);
            }
            else if(secao == 2)
            {
                double d = extras.getDouble("d");
                double tw = extras.getDouble("tw");
                double bf = extras.getDouble("bf");
                double tf = extras.getDouble("tf");
                Show_Results_SoldadoCustom();
            }






        }
        database.close();
    }

    //CALCULOS DE VERIFICACAO


    //ARREDONDAMENTOS
    double casasDecimais(double original, int quant)
    {   double valor = original;
        String formato = "%." + quant + "f";
        valor = Double.valueOf(String.format(Locale.US, formato, valor));
        return valor;
    }

    //CRIACAO LAYOUT

    void Show_Results_LaminadoW(String perfil, double fy, double zx, double iy, double j, double cw, double wx, double mesa, double aba,
                                double rx, double ry, double ag, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idcomp);
        final int tam_grande = 25, tam_pequeno = 18;

        TextView TV_perfil = new TextView(OutputVCompressaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);

        TextView TV_elasticidade = new TextView(OutputVCompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200000 MPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(0,15,0,15);
        scroll_results.addView(TV_elasticidade);

        TextView TV_fy = new TextView(OutputVCompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(0,15,0,15);
        scroll_results.addView(TV_fy);

        TextView TV_zx = new TextView(OutputVCompressaoActivity.this);
        TV_zx.setText(Html.fromHtml("Z<small><sub>x</sub></small> = " + casasDecimais(zx,2) + " cm³"));
        TV_zx.setTextSize(tam_pequeno);
        TV_zx.setPadding(0,100,0,15);
        scroll_results.addView(TV_zx);

        TextView TV_iy = new TextView(OutputVCompressaoActivity.this);
        TV_iy.setText(Html.fromHtml("I<small><sub>y</sub></small> = " + casasDecimais(iy,2) + " cm<small><sup>4</sup></small>"));
        TV_iy.setTextSize(tam_pequeno);
        TV_iy.setPadding(0,15,0,15);
        scroll_results.addView(TV_iy);

        TextView TV_J = new TextView(OutputVCompressaoActivity.this);
        TV_J.setText(Html.fromHtml("J = " + casasDecimais(j,2) + " cm<small><sup>4</sup></small>"));
        TV_J.setTextSize(tam_pequeno);
        TV_J.setPadding(0,15,0,15);
        scroll_results.addView(TV_J);

        TextView TV_cw = new TextView(OutputVCompressaoActivity.this);
        TV_cw.setText(Html.fromHtml("C<small><sub>w</sub></small> = " + casasDecimais(cw,2) + " cm<small><sup>6</sup></small>"));
        TV_cw.setTextSize(tam_pequeno);
        TV_cw.setPadding(0,15,0,15);
        scroll_results.addView(TV_cw);

        TextView TV_wx = new TextView(OutputVCompressaoActivity.this);
        TV_wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + casasDecimais(wx,2) + " cm<small><sup>6</sup></small>"));
        TV_wx.setTextSize(tam_pequeno);
        TV_wx.setPadding(0,15,0,15);
        scroll_results.addView(TV_wx);

        TextView TV_mesa = new TextView(OutputVCompressaoActivity.this);
        TV_mesa.setText(Html.fromHtml("h<small><sub>w</sub></small> / t<small><sub>w</sub></small> = " + casasDecimais(mesa,2) ));
        TV_mesa.setTextSize(tam_pequeno);
        TV_mesa.setPadding(0,15,0,15);
        scroll_results.addView(TV_mesa);

        TextView TV_aba = new TextView(OutputVCompressaoActivity.this);
        TV_aba.setText(Html.fromHtml("0.5b<small><sub>f</sub></small> / t<small><sub>f</sub></small> = " + casasDecimais(aba,2) ));
        TV_aba.setTextSize(tam_pequeno);
        TV_aba.setPadding(0,15,0,15);
        scroll_results.addView(TV_aba);

        TextView TV_rx = new TextView(OutputVCompressaoActivity.this);
        TV_rx.setText(Html.fromHtml("r<small><sub>x</sub></small> = " + casasDecimais(rx,2) + " cm"));
        TV_rx.setTextSize(tam_pequeno);
        TV_rx.setPadding(0,15,0,15);
        scroll_results.addView(TV_rx);

        TextView TV_ry = new TextView(OutputVCompressaoActivity.this);
        TV_ry.setText(Html.fromHtml("r<small><sub>y</sub></small> = " + casasDecimais(ry,2) + " cm"));
        TV_ry.setTextSize(tam_pequeno);
        TV_ry.setPadding(0,15,0,15);
        scroll_results.addView(TV_ry);

        TextView TV_ag = new TextView(OutputVCompressaoActivity.this);
        TV_ag.setText(Html.fromHtml("A<small><sub>g</sub></small> = " + casasDecimais(ag,2) + " cm²"));
        TV_ag.setTextSize(tam_pequeno);
        TV_ag.setPadding(0,15,0,15);
        scroll_results.addView(TV_ag);

        TextView TV_ncsd = new TextView(OutputVCompressaoActivity.this);
        TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd,2) + " kN"));
        TV_ncsd.setTextSize(tam_pequeno);
        TV_ncsd.setPadding(0,100,0,15);
        scroll_results.addView(TV_ncsd);

        TextView TV_kx = new TextView(OutputVCompressaoActivity.this);
        TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx,2)));
        TV_kx.setTextSize(tam_pequeno);
        TV_kx.setPadding(0,15,0,15);
        scroll_results.addView(TV_kx);

        TextView TV_ky = new TextView(OutputVCompressaoActivity.this);
        TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky,2) ));
        TV_ky.setTextSize(tam_pequeno);
        TV_ky.setPadding(0,15,0,15);
        scroll_results.addView(TV_ky);

        TextView TV_kz = new TextView(OutputVCompressaoActivity.this);
        TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz,2) ));
        TV_kz.setTextSize(tam_pequeno);
        TV_kz.setPadding(0,15,0,15);
        scroll_results.addView(TV_kz);

        TextView TV_lx = new TextView(OutputVCompressaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx,2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(0,15,0,15);
        scroll_results.addView(TV_lx);

        TextView TV_ly = new TextView(OutputVCompressaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly,2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(0,15,0,15);
        scroll_results.addView(TV_ly);

        TextView TV_lz = new TextView(OutputVCompressaoActivity.this);
        TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz,2) + " cm"));
        TV_lz.setTextSize(tam_pequeno);
        TV_lz.setPadding(0,15,0,15);
        scroll_results.addView(TV_lz);
    }
    void Show_Results_SoldadoCustom()
    {

    }

}
