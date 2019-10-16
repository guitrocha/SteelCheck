package steelcheck.net.steelcheck;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class OutputVCompressaoActivity extends AppCompatActivity {
    private final double gama_a1 = 1.10;
    private final double gama_a2 = 1.35;
    private final double E_aco = 20000.0; // kN/cm²
    private final double G = 7700.0; // kN/cm²
    final int tam_grande = 25, tam_pequeno = 18;

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
                DatabaseAccess database = DatabaseAccess.getInstance(getApplicationContext());
                database.open();
                int perfil_selected_pos = extras.getInt("perfil");
                double qa = Qa(fy,database.get_mesa(perfil_selected_pos),database.get_tw(perfil_selected_pos),database.get_area(perfil_selected_pos),database.get_dlinha(perfil_selected_pos));
                double qs = Qs_laminado(fy,database.get_aba(perfil_selected_pos));
                double q = Q(qa,qs);
                double nex = Nex(database.get_ix(perfil_selected_pos),kx,lx);
                double ney = Ney(database.get_iy(perfil_selected_pos),ky,ly);
                double nez = Nez(database.get_rx(perfil_selected_pos),database.get_ry(perfil_selected_pos),database.get_cw(perfil_selected_pos),kz,lz,database.get_j(perfil_selected_pos));
                double ne = Ne(nex,ney,nez);
                double esbx = esbeltez_x(kx,lx,database.get_rx(perfil_selected_pos));
                double esby = esbeltez_y(ky,ly,database.get_ry(perfil_selected_pos));
                double esb = esbeltez_final(esbx,esby);
                double esbzero = esbeltez_zero(q,database.get_area(perfil_selected_pos),fy,ne);
                double X = X(esbzero);
                double NCRD = NCRD(X,q,database.get_area(perfil_selected_pos),fy);
                double coef = Coeficiente_Utilização(NCSD,NCRD);
                Show_Results_LaminadoW(database.get_perfil(perfil_selected_pos),fy,database.get_zx(perfil_selected_pos),
                        database.get_iy(perfil_selected_pos),database.get_j(perfil_selected_pos),database.get_cw(perfil_selected_pos),
                        database.get_wx(perfil_selected_pos),database.get_mesa(perfil_selected_pos),database.get_aba(perfil_selected_pos),
                        database.get_rx(perfil_selected_pos),database.get_ry(perfil_selected_pos),database.get_area(perfil_selected_pos),
                        NCSD,kx,ky,kz,lx,ly,lz,qa,qs,q,esbx,esby,esb,nex,ney,nez,ne,esbzero,X,NCRD,coef);

                database.close();
            }
            else if(secao == 2)
            {
                double d = extras.getDouble("d");
                double tw = extras.getDouble("tw");
                double bf = extras.getDouble("bf");
                double tf = extras.getDouble("tf");
                DatabaseCustom database = new DatabaseCustom();
                database.calcularValores(d,tw,bf,tf);
                double kc = Kc(database.getH(),tw);
                System.out.println("kc = " + kc);
                if(kc >=0.35 && kc <=0.76) {
                    double qa = Qa(fy, database.getMesa(), tw, database.getAg(), database.getH());
                    double qs = Qs_custom(fy, database.getAba(), kc);
                    double q = Q(qa, qs);
                    double nex = Nex(database.getIx(), kx, lx);
                    double ney = Ney(database.getIy(), ky, ly);
                    double nez = Nez(database.getRx(), database.getRy(), database.getCw(), kz, lz, database.getJ());
                    double ne = Ne(nex, ney, nez);
                    double esbx = esbeltez_x(kx, lx, database.getRx());
                    double esby = esbeltez_y(ky, ly, database.getRy());
                    double esb = esbeltez_final(esbx, esby);
                    double esbzero = esbeltez_zero(q, database.getAg(), fy, ne);
                    double X = X(esbzero);
                    double NCRD = NCRD(X, q, database.getAg(), fy);
                    double coef = Coeficiente_Utilização(NCSD, NCRD);
                    Show_Results_SoldadoCustom(fy, d, tw, bf, tf, database.getZx(), database.getIy(), database.getJ(), database.getCw(), database.getWx(), database.getMesa(), database.getAba(),
                            database.getRx(), database.getRy(), database.getAg(), NCSD, kx, ky, kz, lx, ly, lz, qa, qs, q, esbx, esby, esb, nex, ney, nez, ne, esbzero, X, NCRD, coef);
                }
                else
                    Show_Kc_erro(kc,database.getH(),tw);

            }






        }

    }

    //CALCULOS DE VERIFICACAO
        //flambagem local
    private double esbeltez_limite_AA(double fy) // b/t lim
    {
        return (1.49) * (Math.sqrt(E_aco/(fy/10)));
    }
    private double esbeltez_de_placa_AA(double mesa) // recebe do banco de dados
    {
        return mesa;
    }
    private boolean esb_lim_MAIORIGUAL_placa(double lim, double placa)
    {
        return lim >= placa;
    }
    private double bef(double tw, double fy, double mesa)
    {
        return (1.92)*(tw)*(Math.sqrt(E_aco/(fy/10)))*( (1)-((0.34/mesa)*(Math.sqrt(E_aco/(fy/10)))) );
    }
    private double Aef(double ag, double dlinha, double bef, double tw)
    {
        return (ag) - (((dlinha-bef)*tw)/100); //mm para cm dlinha e bef divide 100
    }
    private double Qa_sem_flambagem()
    {
        return 1.0;
    }
    private double Qa_com_flambagem(double aef, double ag)
    {
        return aef / ag;
    }
    private double Qa(double fy,double mesa,double tw,double ag,double dlinha)
    {
        double e_lim = esbeltez_limite_AA(fy);
        double e_placa = esbeltez_de_placa_AA(mesa);
        if(esb_lim_MAIORIGUAL_placa(e_lim,e_placa))
            return Qa_sem_flambagem();
        else
        {
            double valor_bef = bef(tw,fy,mesa);
            double valor_aef = Aef(ag,dlinha,valor_bef,tw);
            return Qa_com_flambagem(valor_aef,ag);
        }
    }

    private double esbeltez_limite1_AL_laminado(double fy) // b/t lim
    {
        return (0.56) * (Math.sqrt(E_aco/(fy/10)));
    }
    private double esbeltez_limite1_AL_custom(double fy, double kc) // b/t lim
    {
        return (0.64) * (Math.sqrt(E_aco/((fy/10)/kc)));
    }
    private double esbeltez_limite2_AL_laminado(double fy)
    {
        return (1.03) * (Math.sqrt(E_aco/(fy/10)));
    }
    private double esbeltez_limite2_AL_custom(double fy, double kc)
    {
        return (1.17) * (Math.sqrt(E_aco/((fy/10)/kc)));
    }
    private double esbeltez_de_placa_AL(double aba) // banco de dados
    {
        return aba;
    }
    private boolean lim1_MENOR_placa_MENORIGUAL_lim2(double lim1, double placa, double lim2)
    {
        return (lim1 < placa) && (placa <= lim2);
    }
    private boolean placa_MAIOR_lim2(double placa, double lim2)
    {
        return placa > lim2;
    }
    private double Kc(double h, double tw)
    {
        return 4 / Math.sqrt(h/tw);
    }
    private double Qs_sem_flambagem()
    {
        return 1.0;
    }
    private double Qs_com_flambagem_1_laminado(double aba, double fy)
    {
        return 1.415 - (0.74*aba*(Math.sqrt((fy/10)/E_aco)));
    }
    private double Qs_com_flambagem_1_custom(double aba, double fy, double kc)
    {
        return 1.415 - (0.65*aba*(Math.sqrt((fy/10)/(E_aco*kc))));
    }
    private double Qs_com_flambagem_2_laminado(double aba, double fy)
    {
        return (0.69*E_aco)/((fy/10)*(Math.pow(aba,2)));
    }
    private double Qs_com_flambagem_2_custom(double aba, double fy, double kc)
    {
        return (0.90*kc*E_aco)/((fy/10)*(Math.pow(aba,2)));
    }
    private double Qs_laminado(double fy, double aba)
    {
        double e_lim1 = esbeltez_limite1_AL_laminado(fy);
        double e_lim2 = esbeltez_limite2_AL_laminado(fy);
        double e_placa = esbeltez_de_placa_AL(aba);
        if(e_lim1 >= e_placa)
        {
            return Qs_sem_flambagem();
        }
        else if(lim1_MENOR_placa_MENORIGUAL_lim2(e_lim1,e_placa,e_lim2))
        {
            return Qs_com_flambagem_1_laminado(e_placa,fy);
        }
        else //if(placa_MAIOR_lim2(e_placa,e_lim2))
        {
            return Qs_com_flambagem_2_laminado(e_placa,fy);
        }
    }
    private double Qs_custom(double fy, double aba, double kc)
    {
        double e_lim1 = esbeltez_limite1_AL_custom(fy,kc);
        double e_lim2 = esbeltez_limite2_AL_custom(fy,kc);
        double e_placa = esbeltez_de_placa_AL(aba);
        if(e_lim1 >= e_placa)
        {
            return Qs_sem_flambagem();
        }
        else if(lim1_MENOR_placa_MENORIGUAL_lim2(e_lim1,e_placa,e_lim2))
        {
            return Qs_com_flambagem_1_custom(e_placa,fy,kc);
        }
        else //if(placa_MAIOR_lim2(e_placa,e_lim2))
        {
            return Qs_com_flambagem_2_custom(e_placa,fy,kc);
        }
    }
    private double Q(double qa, double qs)
    {
        return qa*qs;
    }

        //flambagem global

    private double esbeltez_x(double kx, double lx, double rx)
    {
        return (kx*lx)/rx;
    }
    private double esbeltez_y(double ky, double ly, double ry)
    {
        return (ky*ly)/ry;
    }
    private double esbeltez_final(double e_x, double e_y)
    {   if(e_x > e_y)
            return e_x;
        return e_y;
    }
    private double Nex(double ix, double kx, double lx)
    {
        return ( Math.pow(Math.PI,2) * E_aco * ix ) / ( Math.pow(kx*lx,2) );
    }
    private double Ney(double iy, double ky, double ly)
    {
        return ( Math.pow(Math.PI,2) * E_aco * iy ) / ( Math.pow(ky*ly,2) );
    }
    private double Nez(double rx, double ry, double cw, double kz, double lz, double j)
    {
        double r0_quadrado = (Math.pow(rx,2)+Math.pow(ry,2));
        return ( 1/r0_quadrado ) * ( ( ( Math.pow(Math.PI,2)*E_aco*cw )/( Math.pow(kz*lz,2) ) ) + ( G*j ) );
    }
    private double Ne(double nex, double ney, double nez)
    {
        double menor = nex;
        if(ney < menor)
            menor = ney;
        if(nez < menor)
            menor = nez;
        return menor;
    }
    private double esbeltez_zero(double Q, double ag, double fy, double Ne) //força critica de flambagem global
    {
        return Math.sqrt((Q*ag*(fy/10))/Ne);
    }
    private double X_1(double esb_zero)
    {
        return Math.pow(0.658,Math.pow(esb_zero,2));
    }
    private double X_2(double esb_zero)
    {
        return 0.877/Math.pow(esb_zero,2);
    }
    private double X(double esb_zero)
    {
        if(esb_zero <= 1.5)
            return X_1(esb_zero);
        return X_2(esb_zero);
    }

        //normal resistente e coef
    private double NCRD(double X, double Q, double ag, double fy)
    {
        return ( X*Q*ag*(fy/10) )/( gama_a1 );
    }
    private double Coeficiente_Utilização(double NCSD, double NCRD)
    {   return ( NCSD / NCRD );
    }

        //verificacoes
    boolean NCRD_MaiorIgual_NCSD(double NCRD, double NCSD)
    {
        return NCRD >= NCSD;
    }
    boolean ESBELTEZ_MenorIgual_200(double esbeltez)
    {
        return esbeltez <= 200;
    }
    //ARREDONDAMENTOS E CONVERSOES
    private double casasDecimais(double original, int quant)
    {   double valor = original;
        String formato = "%." + quant + "f";
        valor = Double.valueOf(String.format(Locale.US, formato, valor));
        return valor;
    }

    //CRIACAO LAYOUT

    private void Show_Results_LaminadoW(String perfil, double fy, double zx, double iy, double j, double cw, double wx, double mesa, double aba,
                                double rx, double ry, double ag, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz,
                                double qa, double qs, double q, double esbx, double esby, double esb, double nex, double ney, double nez, double ne,
                                        double esbzero, double X, double ncrd, double coef )
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idcomp);

        TextView TV_perfil = new TextView(OutputVCompressaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);

        TextView TV_elasticidade = new TextView(OutputVCompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200000 MPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(0,50,0,15);
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
        TV_wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + casasDecimais(wx,2) + " cm<small><sup>3</sup></small>"));
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
        TV_lz.setPadding(0,15,0,100);
        scroll_results.addView(TV_lz);

        TextView TV_flamblocal = new TextView(OutputVCompressaoActivity.this);
        TV_flamblocal.setText("FLAMBAGEM LOCAL");
        TV_flamblocal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flamblocal.setTextSize(tam_grande);
        scroll_results.addView(TV_flamblocal);

        TextView TV_Qa = new TextView(OutputVCompressaoActivity.this);
        TV_Qa.setText(Html.fromHtml("Q<small><sub>a</sub></small> = " + casasDecimais(qa,3)));
        TV_Qa.setTextSize(tam_pequeno);
        TV_Qa.setPadding(0,50,0,15);
        scroll_results.addView(TV_Qa);

        TextView TV_Qs = new TextView(OutputVCompressaoActivity.this);
        TV_Qs.setText(Html.fromHtml("Q<small><sub>s</sub></small> = " + casasDecimais(qs,3)));
        TV_Qs.setTextSize(tam_pequeno);
        TV_Qs.setPadding(0,15,0,15);
        scroll_results.addView(TV_Qs);

        TextView TV_Q = new TextView(OutputVCompressaoActivity.this);
        TV_Q.setText(Html.fromHtml("Q = " + casasDecimais(q,3)));
        TV_Q.setTextSize(tam_pequeno);
        TV_Q.setPadding(0,15,0,100);
        scroll_results.addView(TV_Q);

        TextView TV_flambglobal = new TextView(OutputVCompressaoActivity.this);
        TV_flambglobal.setText("FLAMBAGEM GLOBAL");
        TV_flambglobal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flambglobal.setTextSize(tam_grande);
        scroll_results.addView(TV_flambglobal);

        TextView TV_esbx = new TextView(OutputVCompressaoActivity.this);
        TV_esbx.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbx,2)));
        TV_esbx.setTextSize(tam_pequeno);
        TV_esbx.setPadding(0,50,0,15);
        scroll_results.addView(TV_esbx);

        TextView TV_esby = new TextView(OutputVCompressaoActivity.this);
        TV_esby.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esby,2)));
        TV_esby.setTextSize(tam_pequeno);
        TV_esby.setPadding(0,15,0,15);
        scroll_results.addView(TV_esby);

        TextView TV_esb = new TextView(OutputVCompressaoActivity.this);
        TV_esb.setText(Html.fromHtml("λ = " + casasDecimais(esb,2)));
        TV_esb.setTextSize(tam_pequeno);
        TV_esb.setPadding(0,15,0,15);
        scroll_results.addView(TV_esb);

        TextView TV_nex = new TextView(OutputVCompressaoActivity.this);
        TV_nex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex,2) + " kN"));
        TV_nex.setTextSize(tam_pequeno);
        TV_nex.setPadding(0,100,0,15);
        scroll_results.addView(TV_nex);

        TextView TV_ney = new TextView(OutputVCompressaoActivity.this);
        TV_ney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney,2) + " kN"));
        TV_ney.setTextSize(tam_pequeno);
        TV_ney.setPadding(0,15,0,15);
        scroll_results.addView(TV_ney);

        TextView TV_nez = new TextView(OutputVCompressaoActivity.this);
        TV_nez.setText(Html.fromHtml("N<small><sub>ez</sub></small> = " + casasDecimais(nez,2) + " kN"));
        TV_nez.setTextSize(tam_pequeno);
        TV_nez.setPadding(0,15,0,15);
        scroll_results.addView(TV_nez);

        TextView TV_ne = new TextView(OutputVCompressaoActivity.this);
        TV_ne.setText(Html.fromHtml("N<small><sub>e</sub></small> = " + casasDecimais(ne,2) + " kN"));
        TV_ne.setTextSize(tam_pequeno);
        TV_ne.setPadding(0,15,0,15);
        scroll_results.addView(TV_ne);

        TextView TV_esbzero = new TextView(OutputVCompressaoActivity.this);
        TV_esbzero.setText(Html.fromHtml("λ<small><sub>0</sub></small> = " + casasDecimais(esbzero,3) ));
        TV_esbzero.setTextSize(tam_pequeno);
        TV_esbzero.setPadding(0,100,0,15);
        scroll_results.addView(TV_esbzero);

        TextView TV_flamb = new TextView(OutputVCompressaoActivity.this);
        TV_flamb.setText(Html.fromHtml("χ = " + casasDecimais(X,3) ));
        TV_flamb.setTextSize(tam_pequeno);
        TV_flamb.setPadding(0,15,0,15);
        scroll_results.addView(TV_flamb);

        TextView TV_compressao = new TextView(OutputVCompressaoActivity.this);
        TV_compressao.setText("NORMAL RESISTENTE DE CÁLCULO");
        TV_compressao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_compressao.setTextSize(tam_grande);
        scroll_results.addView(TV_compressao);

        TextView TV_compNCRD = new TextView(OutputVCompressaoActivity.this);
        TV_compNCRD.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> = " + casasDecimais(ncrd,2) + " kN"));
        TV_compNCRD.setTextSize(tam_pequeno);
        TV_compNCRD.setPadding(0,50,0,100);
        scroll_results.addView(TV_compNCRD);

        TextView TV_coef = new TextView(OutputVCompressaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);

        TextView TV_coefvalor = new TextView(OutputVCompressaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small> = " + casasDecimais(coef,3) + " kN"));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(0,50,0,100);
        scroll_results.addView(TV_coefvalor);

        TextView TV_verif = new TextView(OutputVCompressaoActivity.this);
        TV_verif.setText("VERIFICAÇÕES");
        TV_verif.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verif.setTextSize(tam_grande);
        scroll_results.addView(TV_verif);

        if(NCRD_MaiorIgual_NCSD(ncrd,ncsd))
        {
            TextView TV_nc = new TextView(OutputVCompressaoActivity.this);
            TV_nc.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> maior que N<small><sub>c,Sd</sub></small>: OK! "));
            TV_nc.setTextSize(tam_pequeno);
            TV_nc.setPadding(0,15,0,100);
            TV_nc.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_nc);
        }
        else
        {
            TextView TV_nc = new TextView(OutputVCompressaoActivity.this);
            TV_nc.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> menor que N<small><sub>c,Sd</sub></small>: NÃO OK! "));
            TV_nc.setTextSize(tam_pequeno);
            TV_nc.setPadding(0,15,0,100);
            TV_nc.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_nc);
        }

        if(ESBELTEZ_MenorIgual_200(esb))
        {
            TextView TV_esb200 = new TextView(OutputVCompressaoActivity.this);
            TV_esb200.setText("Esbeltez menor que 200: OK!");
            TV_esb200.setTextSize(tam_pequeno);
            TV_esb200.setPadding(0,15,0,100);
            TV_esb200.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_esb200);
        }
        else
        {
            TextView TV_esb200 = new TextView(OutputVCompressaoActivity.this);
            TV_esb200.setText("Esbeltez maior que 200: NÃO OK!");
            TV_esb200.setTextSize(tam_pequeno);
            TV_esb200.setPadding(0,15,0,100);
            TV_esb200.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_esb200);
        }


    }
    private void Show_Results_SoldadoCustom(double fy, double d, double tw, double bf, double tf, double zx, double iy, double j, double cw, double wx, double mesa, double aba,
                                            double rx, double ry, double ag, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz,
                                            double qa, double qs, double q, double esbx, double esby, double esb, double nex, double ney, double nez, double ne,
                                            double esbzero, double X, double ncrd, double coef  )
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idcomp);

        TextView TV_perfil = new TextView(OutputVCompressaoActivity.this);
        TV_perfil.setText("PERFIL CUSTOMIZADO");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);

        TextView TV_elasticidade = new TextView(OutputVCompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200000 MPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(0,50,0,15);
        scroll_results.addView(TV_elasticidade);

        TextView TV_fy = new TextView(OutputVCompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(0,15,0,15);
        scroll_results.addView(TV_fy);

        TextView TV_d = new TextView(OutputVCompressaoActivity.this);
        TV_d.setText(Html.fromHtml("d = " + casasDecimais(d,2) + " mm"));
        TV_d.setTextSize(tam_pequeno);
        TV_d.setPadding(0,100,0,15);
        scroll_results.addView(TV_d);

        TextView TV_tw = new TextView(OutputVCompressaoActivity.this);
        TV_tw.setText(Html.fromHtml("t<small><sub>w</sub></small> = " + casasDecimais(tw,2) + " mm"));
        TV_tw.setTextSize(tam_pequeno);
        TV_tw.setPadding(0,15,0,15);
        scroll_results.addView(TV_tw);

        TextView TV_bf = new TextView(OutputVCompressaoActivity.this);
        TV_bf.setText(Html.fromHtml("b<small><sub>f</sub></small> = " + casasDecimais(bf,2) + " mm"));
        TV_bf.setTextSize(tam_pequeno);
        TV_bf.setPadding(0,15,0,15);
        scroll_results.addView(TV_bf);

        TextView TV_tf = new TextView(OutputVCompressaoActivity.this);
        TV_tf.setText(Html.fromHtml("t<small><sub>f</sub></small> = " + casasDecimais(tf,2) + " mm"));
        TV_tf.setTextSize(tam_pequeno);
        TV_tf.setPadding(0,15,0,15);
        scroll_results.addView(TV_tf);

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
        TV_wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + casasDecimais(wx,2) + " cm<small><sup>3</sup></small>"));
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
        TV_lz.setPadding(0,15,0,100);
        scroll_results.addView(TV_lz);

        TextView TV_flamblocal = new TextView(OutputVCompressaoActivity.this);
        TV_flamblocal.setText("FLAMBAGEM LOCAL");
        TV_flamblocal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flamblocal.setTextSize(tam_grande);
        scroll_results.addView(TV_flamblocal);

        TextView TV_Qa = new TextView(OutputVCompressaoActivity.this);
        TV_Qa.setText(Html.fromHtml("Q<small><sub>a</sub></small> = " + casasDecimais(qa,3)));
        TV_Qa.setTextSize(tam_pequeno);
        TV_Qa.setPadding(0,50,0,15);
        scroll_results.addView(TV_Qa);

        TextView TV_Qs = new TextView(OutputVCompressaoActivity.this);
        TV_Qs.setText(Html.fromHtml("Q<small><sub>s</sub></small> = " + casasDecimais(qs,3)));
        TV_Qs.setTextSize(tam_pequeno);
        TV_Qs.setPadding(0,15,0,15);
        scroll_results.addView(TV_Qs);

        TextView TV_Q = new TextView(OutputVCompressaoActivity.this);
        TV_Q.setText(Html.fromHtml("Q = " + casasDecimais(q,3)));
        TV_Q.setTextSize(tam_pequeno);
        TV_Q.setPadding(0,15,0,100);
        scroll_results.addView(TV_Q);

        TextView TV_flambglobal = new TextView(OutputVCompressaoActivity.this);
        TV_flambglobal.setText("FLAMBAGEM GLOBAL");
        TV_flambglobal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flambglobal.setTextSize(tam_grande);
        scroll_results.addView(TV_flambglobal);

        TextView TV_esbx = new TextView(OutputVCompressaoActivity.this);
        TV_esbx.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbx,2)));
        TV_esbx.setTextSize(tam_pequeno);
        TV_esbx.setPadding(0,50,0,15);
        scroll_results.addView(TV_esbx);

        TextView TV_esby = new TextView(OutputVCompressaoActivity.this);
        TV_esby.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esby,2)));
        TV_esby.setTextSize(tam_pequeno);
        TV_esby.setPadding(0,15,0,15);
        scroll_results.addView(TV_esby);

        TextView TV_esb = new TextView(OutputVCompressaoActivity.this);
        TV_esb.setText(Html.fromHtml("λ = " + casasDecimais(esb,2)));
        TV_esb.setTextSize(tam_pequeno);
        TV_esb.setPadding(0,15,0,15);
        scroll_results.addView(TV_esb);

        TextView TV_nex = new TextView(OutputVCompressaoActivity.this);
        TV_nex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex,2) + " kN"));
        TV_nex.setTextSize(tam_pequeno);
        TV_nex.setPadding(0,100,0,15);
        scroll_results.addView(TV_nex);

        TextView TV_ney = new TextView(OutputVCompressaoActivity.this);
        TV_ney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney,2) + " kN"));
        TV_ney.setTextSize(tam_pequeno);
        TV_ney.setPadding(0,15,0,15);
        scroll_results.addView(TV_ney);

        TextView TV_nez = new TextView(OutputVCompressaoActivity.this);
        TV_nez.setText(Html.fromHtml("N<small><sub>ez</sub></small> = " + casasDecimais(nez,2) + " kN"));
        TV_nez.setTextSize(tam_pequeno);
        TV_nez.setPadding(0,15,0,15);
        scroll_results.addView(TV_nez);

        TextView TV_ne = new TextView(OutputVCompressaoActivity.this);
        TV_ne.setText(Html.fromHtml("N<small><sub>e</sub></small> = " + casasDecimais(ne,2) + " kN"));
        TV_ne.setTextSize(tam_pequeno);
        TV_ne.setPadding(0,15,0,15);
        scroll_results.addView(TV_ne);

        TextView TV_esbzero = new TextView(OutputVCompressaoActivity.this);
        TV_esbzero.setText(Html.fromHtml("λ<small><sub>0</sub></small> = " + casasDecimais(esbzero,3) ));
        TV_esbzero.setTextSize(tam_pequeno);
        TV_esbzero.setPadding(0,100,0,15);
        scroll_results.addView(TV_esbzero);

        TextView TV_flamb = new TextView(OutputVCompressaoActivity.this);
        TV_flamb.setText(Html.fromHtml("χ = " + casasDecimais(X,3) ));
        TV_flamb.setTextSize(tam_pequeno);
        TV_flamb.setPadding(0,15,0,15);
        scroll_results.addView(TV_flamb);

        TextView TV_compressao = new TextView(OutputVCompressaoActivity.this);
        TV_compressao.setText("NORMAL RESISTENTE DE CÁLCULO");
        TV_compressao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_compressao.setTextSize(tam_grande);
        scroll_results.addView(TV_compressao);

        TextView TV_compNCRD = new TextView(OutputVCompressaoActivity.this);
        TV_compNCRD.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> = " + casasDecimais(ncrd,2) + " kN"));
        TV_compNCRD.setTextSize(tam_pequeno);
        TV_compNCRD.setPadding(0,50,0,100);
        scroll_results.addView(TV_compNCRD);

        TextView TV_coef = new TextView(OutputVCompressaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);

        TextView TV_coefvalor = new TextView(OutputVCompressaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small> = " + casasDecimais(coef,3) + " kN"));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(0,50,0,100);
        scroll_results.addView(TV_coefvalor);

        TextView TV_verif = new TextView(OutputVCompressaoActivity.this);
        TV_verif.setText("VERIFICAÇÕES");
        TV_verif.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verif.setTextSize(tam_grande);
        scroll_results.addView(TV_verif);

        if(NCRD_MaiorIgual_NCSD(ncrd,ncsd))
        {
            TextView TV_nc = new TextView(OutputVCompressaoActivity.this);
            TV_nc.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> maior que N<small><sub>c,Sd</sub></small>: OK! "));
            TV_nc.setTextSize(tam_pequeno);
            TV_nc.setPadding(0,15,0,100);
            TV_nc.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_nc);
        }
        else
        {
            TextView TV_nc = new TextView(OutputVCompressaoActivity.this);
            TV_nc.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> menor que N<small><sub>c,Sd</sub></small>: NÃO OK! "));
            TV_nc.setTextSize(tam_pequeno);
            TV_nc.setPadding(0,15,0,100);
            TV_nc.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_nc);
        }

        if(ESBELTEZ_MenorIgual_200(esb))
        {
            TextView TV_esb200 = new TextView(OutputVCompressaoActivity.this);
            TV_esb200.setText("Esbeltez menor que 200: OK!");
            TV_esb200.setTextSize(tam_pequeno);
            TV_esb200.setPadding(0,15,0,100);
            TV_esb200.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_esb200);
        }
        else
        {
            TextView TV_esb200 = new TextView(OutputVCompressaoActivity.this);
            TV_esb200.setText("Esbeltez maior que 200: NÃO OK!");
            TV_esb200.setTextSize(tam_pequeno);
            TV_esb200.setPadding(0,15,0,100);
            TV_esb200.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_esb200);
        }

    }
    private void Show_Kc_erro(double kc, double h, double tw)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idcomp);

        TextView TV_perfil = new TextView(OutputVCompressaoActivity.this);
        TV_perfil.setText("ERRO - PERFIL CUSTOMIZADO\n");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        TV_perfil.setTextColor(getResources().getColor(R.color.color_Nok));
        scroll_results.addView(TV_perfil);

        ImageView IV_warning = new ImageView(OutputVCompressaoActivity.this);
        IV_warning.setImageResource(android.R.drawable.ic_delete);
        scroll_results.addView(IV_warning);

        TextView TV_kc = new TextView(OutputVCompressaoActivity.this);
        TV_kc.setGravity(Gravity.CENTER);
        TV_kc.setText("\n\nKc = 4/( √(h/tw) ) \n= 4/( √(" + casasDecimais(h,2) + "/" + casasDecimais(tw,2) + ") ) \n= " + casasDecimais(kc,2));
        TV_kc.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_kc.setTextSize(tam_pequeno);
        scroll_results.addView(TV_kc);

        TextView TV_restricao = new TextView(OutputVCompressaoActivity.this);
        TV_restricao.setGravity(Gravity.CENTER);
        TV_restricao.setText("\nE Kc precisa atender a restrição :\n 0.35 ≤ Kc ≤ 0.76");
        TV_restricao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_restricao.setTextSize(tam_pequeno);
        scroll_results.addView(TV_restricao);


    }



}
