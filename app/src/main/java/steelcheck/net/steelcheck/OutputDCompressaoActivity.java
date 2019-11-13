package steelcheck.net.steelcheck;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

public class OutputDCompressaoActivity extends AppCompatActivity {
    public final double gama_a1 = 1.10;
    public final double gama_a2 = 1.35;
    public final double E_aco = 20000.0; // kN/cm²
    public final double G = 7700.0; // kN/cm²
    public final int tam_grande = 25, tam_pequeno = 18;

    private LinearLayout scroll_results;
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
        setContentView(R.layout.activity_output_dcompressao);
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK); // api21+
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button
        Bundle extras = getIntent().getExtras();

        OutputVCompressaoActivity comp = new OutputVCompressaoActivity();


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
            String ordem = extras.getString("ordem");

            DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
            database.open();
            database.order_by(ordem);
            int i;
            long count = database.quantTuplas();
            boolean flag = false;
            double qa=0,qs=0,q=0,nex=0,ney=0,nez=0,ne=0,esbx=0,esby=0,esb=0,esbzero=0,X=0,NCRD=0,coef=0;
            for(i=1 ; i < count && flag == false ; ++i ) {
                qa = comp.Qa(fy, database.get_mesa(i), database.get_tw(i), database.get_area(i), database.get_dlinha(i));
                qs = comp.Qs_laminado(fy, database.get_aba(i));
                q = comp.Q(qa, qs);
                nex = comp.Nex(database.get_ix(i), kx, lx);
                ney = comp.Ney(database.get_iy(i), ky, ly);
                nez = comp.Nez(database.get_rx(i), database.get_ry(i), database.get_cw(i), kz, lz, database.get_j(i));
                ne = comp.Ne(nex, ney, nez);
                esbx = comp.esbeltez_x(kx, lx, database.get_rx(i));
                esby = comp.esbeltez_y(ky, ly, database.get_ry(i));
                esb = comp.esbeltez_final(esbx, esby);
                esbzero = comp.esbeltez_zero(q, database.get_area(i), fy, ne);
                X = comp.X(esbzero);
                NCRD = comp.NCRD(X, q, database.get_area(i), fy);
                coef = comp.Coeficiente_Utilização(NCSD, NCRD);

                flag = comp.NCRD_MaiorIgual_NCSD(NCRD,NCSD) && comp.ESBELTEZ_MenorIgual_200(esb);
            }
            i--;
            String show_ordem = "";
            if(flag)
                show_ordem = generate_string_ordem(ordem,database,i);
            Show_Results_LaminadoW(flag, database.get_perfil(i),show_ordem, fy, database.get_zx(i),
                        database.get_iy(i), database.get_j(i), database.get_cw(i),
                        database.get_wx(i), database.get_mesa(i), database.get_aba(i),
                        database.get_rx(i), database.get_ry(i), database.get_area(i),
                        NCSD, kx, ky, kz, lx, ly, lz, qa, qs, q, esbx, esby, esb, nex, ney, nez, ne, esbzero, X, NCRD, coef);

            database.close();

        }
    }

    private String generate_string_ordem(String ordem, DatabaseAccess db, int i)
    {
        String show = "";
        switch(ordem)
        {
            case "massa":
                show = "Massa Linear = " + db.get_massa(i) + " kg/m";
                break;
            case "d":
                show = "d = " + db.get_d(i) + " mm";
                break;
            case "bf":
                show = "b<small><sub>f</sub></small> = " + db.get_bf(i) + " mm";
                break;
            case "tf":
                show = "t<small><sub>f</sub></small> = " + db.get_tf(i) + " mm";
                break;
            case "tw":
                show = "t<small><sub>w</sub></small> = " + db.get_tw(i) + " mm";
                break;
            case "area":
                show = "A<small><sub>g</sub></small> = " + db.get_area(i) + " cm²";
                break;
            case "ix":
                show = "I<small><sub>x</sub></small> = " + db.get_ix(i) + " cm<small><sup>4</sup></small>";
                break;
            case "iy":
                show = "I<small><sub>y</sub></small> = " + db.get_iy(i) + " cm<small><sup>4</sup></small>";
                break;
        }
        return show;
    }
    //ARREDONDAMENTOS E CONVERSOES
    private double casasDecimais(double original, int quant)
    {   double valor = original;
        String formato = "%." + quant + "f";
        valor = Double.valueOf(String.format(Locale.US, formato, valor));
        return valor;
    }

    //CRIACAO LAYOUT

    private void Show_Results_LaminadoW(boolean flag, String perfil, String ordem, double fy, double zx, double iy, double j, double cw, double wx, double mesa, double aba,
                                double rx, double ry, double ag, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz,
                                double qa, double qs, double q, double esbx, double esby, double esb, double nex, double ney, double nez, double ne,
                                        double esbzero, double X, double ncrd, double coef )
    {
        ScrollView sv = new ScrollView(OutputDCompressaoActivity.this);

        LinearLayout scroll_results = new LinearLayout(OutputDCompressaoActivity.this);
        scroll_results.setOrientation(LinearLayout.VERTICAL);
        sv.addView(scroll_results);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relcomp);
        rl.addView(sv);

        if(!flag)
        {
            TextView ERRO = new TextView(OutputDCompressaoActivity.this);
            ERRO.setText("Nenhum perfil é adequado!");
            scroll_results.addView(ERRO);
            ERRO.setTextColor(getResources().getColor(R.color.color_Nok));
            ERRO.setTextSize(tam_grande);
            return;
        }

        TextView TV_perfil = new TextView(OutputDCompressaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        TV_perfil.setTextColor(getResources().getColor(R.color.color_ok));
        scroll_results.addView(TV_perfil);

        TextView TV_ordem = new TextView(OutputDCompressaoActivity.this);
        TV_ordem.setText(Html.fromHtml(ordem));
        TV_ordem.setTextSize(tam_pequeno);
        TV_ordem.setPadding(0,50,0,15);
        scroll_results.addView(TV_ordem);

        TextView TV_elasticidade = new TextView(OutputDCompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200000 MPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(0,50,0,15);
        scroll_results.addView(TV_elasticidade);

        TextView TV_fy = new TextView(OutputDCompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(0,15,0,15);
        scroll_results.addView(TV_fy);

        TextView TV_zx = new TextView(OutputDCompressaoActivity.this);
        TV_zx.setText(Html.fromHtml("Z<small><sub>x</sub></small> = " + casasDecimais(zx,2) + " cm³"));
        TV_zx.setTextSize(tam_pequeno);
        TV_zx.setPadding(0,100,0,15);
        scroll_results.addView(TV_zx);

        TextView TV_iy = new TextView(OutputDCompressaoActivity.this);
        TV_iy.setText(Html.fromHtml("I<small><sub>y</sub></small> = " + casasDecimais(iy,2) + " cm<small><sup>4</sup></small>"));
        TV_iy.setTextSize(tam_pequeno);
        TV_iy.setPadding(0,15,0,15);
        scroll_results.addView(TV_iy);

        TextView TV_J = new TextView(OutputDCompressaoActivity.this);
        TV_J.setText(Html.fromHtml("J = " + casasDecimais(j,2) + " cm<small><sup>4</sup></small>"));
        TV_J.setTextSize(tam_pequeno);
        TV_J.setPadding(0,15,0,15);
        scroll_results.addView(TV_J);

        TextView TV_cw = new TextView(OutputDCompressaoActivity.this);
        TV_cw.setText(Html.fromHtml("C<small><sub>w</sub></small> = " + casasDecimais(cw,2) + " cm<small><sup>6</sup></small>"));
        TV_cw.setTextSize(tam_pequeno);
        TV_cw.setPadding(0,15,0,15);
        scroll_results.addView(TV_cw);

        TextView TV_wx = new TextView(OutputDCompressaoActivity.this);
        TV_wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + casasDecimais(wx,2) + " cm<small><sup>3</sup></small>"));
        TV_wx.setTextSize(tam_pequeno);
        TV_wx.setPadding(0,15,0,15);
        scroll_results.addView(TV_wx);

        TextView TV_mesa = new TextView(OutputDCompressaoActivity.this);
        TV_mesa.setText(Html.fromHtml("h<small><sub>w</sub></small> / t<small><sub>w</sub></small> = " + casasDecimais(mesa,2) ));
        TV_mesa.setTextSize(tam_pequeno);
        TV_mesa.setPadding(0,15,0,15);
        scroll_results.addView(TV_mesa);

        TextView TV_aba = new TextView(OutputDCompressaoActivity.this);
        TV_aba.setText(Html.fromHtml("0.5b<small><sub>f</sub></small> / t<small><sub>f</sub></small> = " + casasDecimais(aba,2) ));
        TV_aba.setTextSize(tam_pequeno);
        TV_aba.setPadding(0,15,0,15);
        scroll_results.addView(TV_aba);

        TextView TV_rx = new TextView(OutputDCompressaoActivity.this);
        TV_rx.setText(Html.fromHtml("r<small><sub>x</sub></small> = " + casasDecimais(rx,2) + " cm"));
        TV_rx.setTextSize(tam_pequeno);
        TV_rx.setPadding(0,15,0,15);
        scroll_results.addView(TV_rx);

        TextView TV_ry = new TextView(OutputDCompressaoActivity.this);
        TV_ry.setText(Html.fromHtml("r<small><sub>y</sub></small> = " + casasDecimais(ry,2) + " cm"));
        TV_ry.setTextSize(tam_pequeno);
        TV_ry.setPadding(0,15,0,15);
        scroll_results.addView(TV_ry);

        TextView TV_ag = new TextView(OutputDCompressaoActivity.this);
        TV_ag.setText(Html.fromHtml("A<small><sub>g</sub></small> = " + casasDecimais(ag,2) + " cm²"));
        TV_ag.setTextSize(tam_pequeno);
        TV_ag.setPadding(0,15,0,15);
        scroll_results.addView(TV_ag);

        TextView TV_ncsd = new TextView(OutputDCompressaoActivity.this);
        TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd,2) + " kN"));
        TV_ncsd.setTextSize(tam_pequeno);
        TV_ncsd.setPadding(0,100,0,15);
        scroll_results.addView(TV_ncsd);

        TextView TV_kx = new TextView(OutputDCompressaoActivity.this);
        TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx,2)));
        TV_kx.setTextSize(tam_pequeno);
        TV_kx.setPadding(0,15,0,15);
        scroll_results.addView(TV_kx);

        TextView TV_ky = new TextView(OutputDCompressaoActivity.this);
        TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky,2) ));
        TV_ky.setTextSize(tam_pequeno);
        TV_ky.setPadding(0,15,0,15);
        scroll_results.addView(TV_ky);

        TextView TV_kz = new TextView(OutputDCompressaoActivity.this);
        TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz,2) ));
        TV_kz.setTextSize(tam_pequeno);
        TV_kz.setPadding(0,15,0,15);
        scroll_results.addView(TV_kz);

        TextView TV_lx = new TextView(OutputDCompressaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx,2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(0,15,0,15);
        scroll_results.addView(TV_lx);

        TextView TV_ly = new TextView(OutputDCompressaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly,2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(0,15,0,15);
        scroll_results.addView(TV_ly);

        TextView TV_lz = new TextView(OutputDCompressaoActivity.this);
        TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz,2) + " cm"));
        TV_lz.setTextSize(tam_pequeno);
        TV_lz.setPadding(0,15,0,100);
        scroll_results.addView(TV_lz);

        TextView TV_flamblocal = new TextView(OutputDCompressaoActivity.this);
        TV_flamblocal.setText("FLAMBAGEM LOCAL");
        TV_flamblocal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flamblocal.setTextSize(tam_grande);
        scroll_results.addView(TV_flamblocal);

        TextView TV_Qa = new TextView(OutputDCompressaoActivity.this);
        TV_Qa.setText(Html.fromHtml("Q<small><sub>a</sub></small> = " + casasDecimais(qa,3)));
        TV_Qa.setTextSize(tam_pequeno);
        TV_Qa.setPadding(0,50,0,15);
        scroll_results.addView(TV_Qa);

        TextView TV_Qs = new TextView(OutputDCompressaoActivity.this);
        TV_Qs.setText(Html.fromHtml("Q<small><sub>s</sub></small> = " + casasDecimais(qs,3)));
        TV_Qs.setTextSize(tam_pequeno);
        TV_Qs.setPadding(0,15,0,15);
        scroll_results.addView(TV_Qs);

        TextView TV_Q = new TextView(OutputDCompressaoActivity.this);
        TV_Q.setText(Html.fromHtml("Q = " + casasDecimais(q,3)));
        TV_Q.setTextSize(tam_pequeno);
        TV_Q.setPadding(0,15,0,100);
        scroll_results.addView(TV_Q);

        TextView TV_flambglobal = new TextView(OutputDCompressaoActivity.this);
        TV_flambglobal.setText("FLAMBAGEM GLOBAL");
        TV_flambglobal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flambglobal.setTextSize(tam_grande);
        scroll_results.addView(TV_flambglobal);

        TextView TV_esbx = new TextView(OutputDCompressaoActivity.this);
        TV_esbx.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbx,2)));
        TV_esbx.setTextSize(tam_pequeno);
        TV_esbx.setPadding(0,50,0,15);
        scroll_results.addView(TV_esbx);

        TextView TV_esby = new TextView(OutputDCompressaoActivity.this);
        TV_esby.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esby,2)));
        TV_esby.setTextSize(tam_pequeno);
        TV_esby.setPadding(0,15,0,15);
        scroll_results.addView(TV_esby);

        TextView TV_esb = new TextView(OutputDCompressaoActivity.this);
        TV_esb.setText(Html.fromHtml("λ = " + casasDecimais(esb,2)));
        TV_esb.setTextSize(tam_pequeno);
        TV_esb.setPadding(0,15,0,15);
        scroll_results.addView(TV_esb);

        TextView TV_nex = new TextView(OutputDCompressaoActivity.this);
        TV_nex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex,2) + " kN"));
        TV_nex.setTextSize(tam_pequeno);
        TV_nex.setPadding(0,100,0,15);
        scroll_results.addView(TV_nex);

        TextView TV_ney = new TextView(OutputDCompressaoActivity.this);
        TV_ney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney,2) + " kN"));
        TV_ney.setTextSize(tam_pequeno);
        TV_ney.setPadding(0,15,0,15);
        scroll_results.addView(TV_ney);

        TextView TV_nez = new TextView(OutputDCompressaoActivity.this);
        TV_nez.setText(Html.fromHtml("N<small><sub>ez</sub></small> = " + casasDecimais(nez,2) + " kN"));
        TV_nez.setTextSize(tam_pequeno);
        TV_nez.setPadding(0,15,0,15);
        scroll_results.addView(TV_nez);

        TextView TV_ne = new TextView(OutputDCompressaoActivity.this);
        TV_ne.setText(Html.fromHtml("N<small><sub>e</sub></small> = " + casasDecimais(ne,2) + " kN"));
        TV_ne.setTextSize(tam_pequeno);
        TV_ne.setPadding(0,15,0,15);
        scroll_results.addView(TV_ne);

        TextView TV_esbzero = new TextView(OutputDCompressaoActivity.this);
        TV_esbzero.setText(Html.fromHtml("λ<small><sub>0</sub></small> = " + casasDecimais(esbzero,3) ));
        TV_esbzero.setTextSize(tam_pequeno);
        TV_esbzero.setPadding(0,100,0,15);
        scroll_results.addView(TV_esbzero);

        TextView TV_flamb = new TextView(OutputDCompressaoActivity.this);
        TV_flamb.setText(Html.fromHtml("χ = " + casasDecimais(X,3) ));
        TV_flamb.setTextSize(tam_pequeno);
        TV_flamb.setPadding(0,15,0,15);
        scroll_results.addView(TV_flamb);

        TextView TV_compressao = new TextView(OutputDCompressaoActivity.this);
        TV_compressao.setText("NORMAL RESISTENTE DE CÁLCULO");
        TV_compressao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_compressao.setTextSize(tam_grande);
        scroll_results.addView(TV_compressao);

        TextView TV_compNCRD = new TextView(OutputDCompressaoActivity.this);
        TV_compNCRD.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> = " + casasDecimais(ncrd,2) + " kN"));
        TV_compNCRD.setTextSize(tam_pequeno);
        TV_compNCRD.setPadding(0,50,0,100);
        scroll_results.addView(TV_compNCRD);

        TextView TV_coef = new TextView(OutputDCompressaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);

        TextView TV_coefvalor = new TextView(OutputDCompressaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small> = " + casasDecimais(coef,3)));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(0,50,0,100);
        scroll_results.addView(TV_coefvalor);


    }




}
