package steelcheck.net.steelcheck;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup;
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
    final int tam_grande = 20, tam_pequeno = 16, tam_dimens = 13;

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
            else
                show_ordem = generate_string_ordem(ordem,database,-1);
            Show_Results_LaminadoW(comp,database,i,flag, database.get_perfil(i),show_ordem, fy, database.get_zx(i),
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
        if(i==-1)
        {
            switch(ordem)
            {
                case "massa":
                    show = "Massa Linear";
                    break;
                case "d":
                    show = "d";
                    break;
                case "bf":
                    show = "b<small><sub>f</sub></small>";
                    break;
                case "tf":
                    show = "t<small><sub>f</sub></small>";
                    break;
                case "tw":
                    show = "t<small><sub>w</sub></small>";
                    break;
                case "area":
                    show = "A<small><sub>g</sub></small>";
                    break;
                case "ix":
                    show = "I<small><sub>x</sub></small>";
                    break;
                case "iy":
                    show = "I<small><sub>y</sub></small>";
                    break;
            }
        }
        else
        {
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

    private void Show_Results_LaminadoW(OutputVCompressaoActivity comp, DatabaseAccess db, int pos,boolean flag, String perfil, String ordem, double fy, double zx, double iy, double j, double cw, double wx, double mesa, double aba,
                                double rx, double ry, double ag, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz,
                                double qa, double qs, double q, double esbx, double esby, double esb, double nex, double ney, double nez, double ne,
                                        double esbzero, double X, double ncrd, double coef )
    {
        ScrollView sv = new ScrollView(OutputDCompressaoActivity.this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        sv.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.editbox_dropdown_light_frame));

        LinearLayout scroll_results = new LinearLayout(OutputDCompressaoActivity.this);
        scroll_results.setOrientation(LinearLayout.VERTICAL);
        sv.addView(scroll_results);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relcomp);
        rl.addView(sv);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        if(!flag)
        {
            //1 - PERFIL
            TextView TV_perfil = new TextView(OutputDCompressaoActivity.this);
            TV_perfil.setText("PERFIL ADEQUADO");
            TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_perfil.setTextSize(tam_grande);
            scroll_results.addView(TV_perfil);
            TV_perfil.setGravity(Gravity.CENTER);
            TV_perfil.setTextColor(Color.WHITE);
            TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_perfil.setPadding(50,20,50,20);

            TextView TV_perfil_dimen = new TextView(OutputDCompressaoActivity.this);
            TV_perfil_dimen.setText("NÃO ENCONTRADO");
            TV_perfil_dimen.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_perfil_dimen.setTextSize(tam_grande);
            scroll_results.addView(TV_perfil_dimen);
            TV_perfil_dimen.setGravity(Gravity.CENTER);
            TV_perfil_dimen.setTextColor(Color.WHITE);
            TV_perfil_dimen.setBackgroundColor(getResources().getColor(R.color.color_Nok));
            TV_perfil_dimen.setPadding(50,20,50,20);

            TextView TV_param = new TextView(OutputDCompressaoActivity.this);
            TV_param.setText(Html.fromHtml("Ordem: "+ ordem));
            TV_param.setTextSize(tam_dimens);
            TV_param.setPadding(50,15,0,50);
            scroll_results.addView(TV_param);

            TextView TV_nenhum = new TextView(OutputDCompressaoActivity.this);
            TV_nenhum.setText(Html.fromHtml("Nenhum perfil atende às condições impostas."));
            TV_nenhum.setTextSize(tam_pequeno);
            TV_nenhum.setPadding(50,15,50,15);
            scroll_results.addView(TV_nenhum);
            TV_nenhum.setTextColor(Color.BLACK);

            //2 - Parametros
            TextView TV_parametros = new TextView(OutputDCompressaoActivity.this);
            TV_parametros.setText("PARÂMETROS DO MATERIAL");
            TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_parametros.setTextSize(tam_grande);
            scroll_results.addView(TV_parametros);
            TV_parametros.setGravity(Gravity.CENTER);
            TV_parametros.setTextColor(Color.WHITE);
            TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_parametros.setPadding(50,20,50,20);

            TextView TV_elasticidade = new TextView(OutputDCompressaoActivity.this);
            TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
            TV_elasticidade.setTextSize(tam_pequeno);
            TV_elasticidade.setPadding(50,15,0,15);
            scroll_results.addView(TV_elasticidade);
            TV_elasticidade.setTextColor(Color.BLACK);

            TextView TV_fy = new TextView(OutputDCompressaoActivity.this);
            TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
            TV_fy.setTextSize(tam_pequeno);
            TV_fy.setPadding(50,15,0,50);
            scroll_results.addView(TV_fy);
            TV_fy.setTextColor(Color.BLACK);

            //3 - Solicitacoes e contorno
            TextView TV_solic = new TextView(OutputDCompressaoActivity.this);
            TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
            TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
            TV_solic.setTextSize(tam_grande);
            scroll_results.addView(TV_solic);
            TV_solic.setGravity(Gravity.CENTER);
            TV_solic.setTextColor(Color.WHITE);
            TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
            TV_solic.setPadding(50,20,50,20);

            LinearLayout contorno = new LinearLayout(OutputDCompressaoActivity.this);
            contorno.setOrientation(LinearLayout.HORIZONTAL);
            scroll_results.addView(contorno);

            LinearLayout contorno_1 = new LinearLayout(OutputDCompressaoActivity.this);
            contorno_1.setOrientation(LinearLayout.VERTICAL);
            contorno.addView(contorno_1);

            LinearLayout contorno_2 = new LinearLayout(OutputDCompressaoActivity.this);
            contorno_2.setOrientation(LinearLayout.VERTICAL);
            contorno.addView(contorno_2);

            TextView TV_ncsd = new TextView(OutputDCompressaoActivity.this);
            TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd,2) + " kN"));
            TV_ncsd.setTextSize(tam_pequeno);
            TV_ncsd.setPadding(50,15,0,15);
            contorno_1.addView(TV_ncsd);
            TV_ncsd.setTextColor(Color.BLACK);

            TextView TV_kx = new TextView(OutputDCompressaoActivity.this);
            TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx,2)));
            TV_kx.setTextSize(tam_pequeno);
            TV_kx.setPadding(50,15,0,15);
            contorno_1.addView(TV_kx);
            TV_kx.setTextColor(Color.BLACK);

            TextView TV_ky = new TextView(OutputDCompressaoActivity.this);
            TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky,2) ));
            TV_ky.setTextSize(tam_pequeno);
            TV_ky.setPadding(50,15,0,15);
            contorno_1.addView(TV_ky);
            TV_ky.setTextColor(Color.BLACK);

            TextView TV_kz = new TextView(OutputDCompressaoActivity.this);
            TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz,2) ));
            TV_kz.setTextSize(tam_pequeno);
            TV_kz.setPadding(50,15,0,50);
            contorno_1.addView(TV_kz);
            TV_kz.setTextColor(Color.BLACK);

            TextView TV_lx = new TextView(OutputDCompressaoActivity.this);
            TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx,2) + " cm"));
            TV_lx.setTextSize(tam_pequeno);
            TV_lx.setPadding(50,15,0,15);
            contorno_2.addView(TV_lx);
            TV_lx.setTextColor(Color.BLACK);

            TextView TV_ly = new TextView(OutputDCompressaoActivity.this);
            TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly,2) + " cm"));
            TV_ly.setTextSize(tam_pequeno);
            TV_ly.setPadding(50,15,0,15);
            contorno_2.addView(TV_ly);
            TV_ly.setTextColor(Color.BLACK);

            TextView TV_lz = new TextView(OutputDCompressaoActivity.this);
            TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz,2) + " cm"));
            TV_lz.setTextSize(tam_pequeno);
            TV_lz.setPadding(50,15,0,15);
            contorno_2.addView(TV_lz);
            TV_lz.setTextColor(Color.BLACK);

            return;
        }

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputDCompressaoActivity.this);
        TV_perfil.setText("PERFIL ADEQUADO");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        TextView TV_perfil_dimen = new TextView(OutputDCompressaoActivity.this);
        TV_perfil_dimen.setText(perfil);
        TV_perfil_dimen.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil_dimen.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil_dimen);
        TV_perfil_dimen.setGravity(Gravity.CENTER);
        TV_perfil_dimen.setTextColor(Color.WHITE);
        TV_perfil_dimen.setBackgroundColor(getResources().getColor(R.color.color_ok));
        TV_perfil_dimen.setPadding(50,20,50,20);

        TextView TV_param = new TextView(OutputDCompressaoActivity.this);
        TV_param.setText(Html.fromHtml("Ordem: "+ ordem));
        TV_param.setTextSize(tam_dimens);
        TV_param.setPadding(50,15,0,50);
        scroll_results.addView(TV_param);
        comp.Show_Dimensoes_Database_Perfil(db,scroll_results,OutputDCompressaoActivity.this,pos);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputDCompressaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputDCompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputDCompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputDCompressaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        LinearLayout contorno = new LinearLayout(OutputDCompressaoActivity.this);
        contorno.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(contorno);

        LinearLayout contorno_1 = new LinearLayout(OutputDCompressaoActivity.this);
        contorno_1.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_1);

        LinearLayout contorno_2 = new LinearLayout(OutputDCompressaoActivity.this);
        contorno_2.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_2);

        TextView TV_ncsd = new TextView(OutputDCompressaoActivity.this);
        TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd,2) + " kN"));
        TV_ncsd.setTextSize(tam_pequeno);
        TV_ncsd.setPadding(50,15,0,15);
        contorno_1.addView(TV_ncsd);
        TV_ncsd.setTextColor(Color.BLACK);

        TextView TV_kx = new TextView(OutputDCompressaoActivity.this);
        TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx,2)));
        TV_kx.setTextSize(tam_pequeno);
        TV_kx.setPadding(50,15,0,15);
        contorno_1.addView(TV_kx);
        TV_kx.setTextColor(Color.BLACK);

        TextView TV_ky = new TextView(OutputDCompressaoActivity.this);
        TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky,2) ));
        TV_ky.setTextSize(tam_pequeno);
        TV_ky.setPadding(50,15,0,15);
        contorno_1.addView(TV_ky);
        TV_ky.setTextColor(Color.BLACK);

        TextView TV_kz = new TextView(OutputDCompressaoActivity.this);
        TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz,2) ));
        TV_kz.setTextSize(tam_pequeno);
        TV_kz.setPadding(50,15,0,50);
        contorno_1.addView(TV_kz);
        TV_kz.setTextColor(Color.BLACK);

        TextView TV_lx = new TextView(OutputDCompressaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx,2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(50,15,0,15);
        contorno_2.addView(TV_lx);
        TV_lx.setTextColor(Color.BLACK);

        TextView TV_ly = new TextView(OutputDCompressaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly,2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(50,15,0,15);
        contorno_2.addView(TV_ly);
        TV_ly.setTextColor(Color.BLACK);

        TextView TV_lz = new TextView(OutputDCompressaoActivity.this);
        TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz,2) + " cm"));
        TV_lz.setTextSize(tam_pequeno);
        TV_lz.setPadding(50,15,0,15);
        contorno_2.addView(TV_lz);
        TV_lz.setTextColor(Color.BLACK);

        TextView TV_flamblocal = new TextView(OutputDCompressaoActivity.this);
        TV_flamblocal.setText("FLAMBAGEM LOCAL");
        TV_flamblocal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flamblocal.setTextSize(tam_grande);
        scroll_results.addView(TV_flamblocal);
        TV_flamblocal.setGravity(Gravity.CENTER);
        TV_flamblocal.setTextColor(Color.WHITE);
        TV_flamblocal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flamblocal.setPadding(50,20,50,20);

        TextView TV_Qa = new TextView(OutputDCompressaoActivity.this);
        TV_Qa.setText(Html.fromHtml("Q<small><sub>a</sub></small> = " + casasDecimais(qa,3)));
        TV_Qa.setTextSize(tam_pequeno);
        TV_Qa.setPadding(50,15,0,15);
        scroll_results.addView(TV_Qa);
        TV_Qa.setTextColor(Color.BLACK);

        TextView TV_Qs = new TextView(OutputDCompressaoActivity.this);
        TV_Qs.setText(Html.fromHtml("Q<small><sub>s</sub></small> = " + casasDecimais(qs,3)));
        TV_Qs.setTextSize(tam_pequeno);
        TV_Qs.setPadding(50,15,0,15);
        scroll_results.addView(TV_Qs);
        TV_Qs.setTextColor(Color.BLACK);

        TextView TV_Q = new TextView(OutputDCompressaoActivity.this);
        TV_Q.setText(Html.fromHtml("Q = " + casasDecimais(q,3)));
        TV_Q.setTextSize(tam_pequeno);
        TV_Q.setPadding(50,15,0,50);
        scroll_results.addView(TV_Q);
        TV_Q.setTextColor(Color.BLACK);

        TextView TV_flambglobal = new TextView(OutputDCompressaoActivity.this);
        TV_flambglobal.setText("FLAMBAGEM GLOBAL");
        TV_flambglobal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flambglobal.setTextSize(tam_grande);
        scroll_results.addView(TV_flambglobal);
        TV_flambglobal.setGravity(Gravity.CENTER);
        TV_flambglobal.setTextColor(Color.WHITE);
        TV_flambglobal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flambglobal.setPadding(50,20,50,20);

        LinearLayout glob = new LinearLayout(OutputDCompressaoActivity.this);
        glob.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(glob);

        LinearLayout globesq = new LinearLayout(OutputDCompressaoActivity.this);
        globesq.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globesq);

        LinearLayout globdir = new LinearLayout(OutputDCompressaoActivity.this);
        globdir.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globdir);


        TextView TV_nex = new TextView(OutputDCompressaoActivity.this);
        TV_nex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex,2) + " kN"));
        TV_nex.setTextSize(tam_pequeno);
        TV_nex.setPadding(50,15,0,15);
        globesq.addView(TV_nex);
        TV_nex.setTextColor(Color.BLACK);

        TextView TV_ney = new TextView(OutputDCompressaoActivity.this);
        TV_ney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney,2) + " kN"));
        TV_ney.setTextSize(tam_pequeno);
        TV_ney.setPadding(50,15,0,15);
        globesq.addView(TV_ney);
        TV_ney.setTextColor(Color.BLACK);

        TextView TV_nez = new TextView(OutputDCompressaoActivity.this);
        TV_nez.setText(Html.fromHtml("N<small><sub>ez</sub></small> = " + casasDecimais(nez,2) + " kN"));
        TV_nez.setTextSize(tam_pequeno);
        TV_nez.setPadding(50,15,0,50);
        globesq.addView(TV_nez);
        TV_nez.setTextColor(Color.BLACK);

        TextView TV_ne = new TextView(OutputDCompressaoActivity.this);
        TV_ne.setText(Html.fromHtml("N<small><sub>e</sub></small> = " + casasDecimais(ne,2) + " kN"));
        TV_ne.setTextSize(tam_pequeno);
        TV_ne.setPadding(50,15,0,15);
        globdir.addView(TV_ne);
        TV_ne.setTextColor(Color.BLACK);

        TextView TV_esbzero = new TextView(OutputDCompressaoActivity.this);
        TV_esbzero.setText(Html.fromHtml("λ<small><sub>0</sub></small> = " + casasDecimais(esbzero,3) ));
        TV_esbzero.setTextSize(tam_pequeno);
        TV_esbzero.setPadding(50,15,0,15);
        globdir.addView(TV_esbzero);
        TV_esbzero.setTextColor(Color.BLACK);

        TextView TV_flamb = new TextView(OutputDCompressaoActivity.this);
        TV_flamb.setText(Html.fromHtml("χ = " + casasDecimais(X,3) ));
        TV_flamb.setTextSize(tam_pequeno);
        TV_flamb.setPadding(50,15,0,50);
        globdir.addView(TV_flamb);
        TV_flamb.setTextColor(Color.BLACK);

        //2 - ESBELTEZ
        TextView TV_esbeltezglob = new TextView(OutputDCompressaoActivity.this);
        TV_esbeltezglob.setText("ESBELTEZ GLOBAL");
        TV_esbeltezglob.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esbeltezglob.setTextSize(tam_grande);
        scroll_results.addView(TV_esbeltezglob);
        TV_esbeltezglob.setGravity(Gravity.CENTER);
        TV_esbeltezglob.setTextColor(Color.WHITE);
        TV_esbeltezglob.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_esbeltezglob.setPadding(50,20,50,20);

        TextView TV_esbx = new TextView(OutputDCompressaoActivity.this);
        TV_esbx.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbx,2)));
        TV_esbx.setTextSize(tam_pequeno);
        TV_esbx.setPadding(50,15,0,15);
        scroll_results.addView(TV_esbx);
        TV_esbx.setTextColor(Color.BLACK);

        TextView TV_esby = new TextView(OutputDCompressaoActivity.this);
        TV_esby.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esby,2)));
        TV_esby.setTextSize(tam_pequeno);
        TV_esby.setPadding(50,15,0,15);
        scroll_results.addView(TV_esby);
        TV_esby.setTextColor(Color.BLACK);

        TextView TV_esb = new TextView(OutputDCompressaoActivity.this);
        TV_esb.setText(Html.fromHtml("λ = " + casasDecimais(esb,2)));
        TV_esb.setTextSize(tam_pequeno);
        TV_esb.setPadding(50,15,0,50);
        scroll_results.addView(TV_esb);
        TV_esb.setTextColor(Color.BLACK);

        TextView TV_compressao = new TextView(OutputDCompressaoActivity.this);
        TV_compressao.setText("COMPRESSÃO RESISTENTE DE CÁLCULO");
        TV_compressao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_compressao.setTextSize(tam_grande);
        scroll_results.addView(TV_compressao);
        TV_compressao.setGravity(Gravity.CENTER);
        TV_compressao.setTextColor(Color.WHITE);
        TV_compressao.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_compressao.setPadding(50,20,50,20);

        TextView TV_compNCRD = new TextView(OutputDCompressaoActivity.this);
        TV_compNCRD.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> = " + casasDecimais(ncrd,2) + " kN"));
        TV_compNCRD.setTextSize(tam_pequeno);
        TV_compNCRD.setPadding(50,15,0,50);
        scroll_results.addView(TV_compNCRD);
        TV_compNCRD.setTextColor(Color.BLACK);

        TextView TV_coef = new TextView(OutputDCompressaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);
        TV_coef.setGravity(Gravity.CENTER);
        TV_coef.setTextColor(Color.WHITE);
        TV_coef.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_coef.setPadding(50,20,50,20);

        TextView TV_coefvalor = new TextView(OutputDCompressaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small> = " + casasDecimais(coef,3) + " kN"));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(50,15,0,50);
        scroll_results.addView(TV_coefvalor);
        TV_coefvalor.setTextColor(Color.BLACK);


    }




}
