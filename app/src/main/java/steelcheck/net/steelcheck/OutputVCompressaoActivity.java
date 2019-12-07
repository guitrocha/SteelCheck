package steelcheck.net.steelcheck;

import android.content.Context;
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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

public class OutputVCompressaoActivity extends AppCompatActivity {
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
                database.order_by("id");
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
                Show_Results_LaminadoW(database,perfil_selected_pos,database.get_perfil(perfil_selected_pos),fy,
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
                    Show_Results_SoldadoCustom(database,"CUSTOMIZADO",fy, NCSD, kx, ky, kz, lx, ly, lz, qa, qs, q, esbx, esby, esb, nex, ney, nez, ne, esbzero, X, NCRD, coef);
                }
                else
                    Show_Kc_erro(kc,database.getH(),tw);

            }

        }

    }

    //CALCULOS DE VERIFICACAO
        //flambagem local
    public double esbeltez_limite_AA(double fy) // b/t lim
    {
        return (1.49) * (Math.sqrt(E_aco/(fy/10)));
    }
    public double esbeltez_de_placa_AA(double mesa) // recebe do banco de dados
    {
        return mesa;
    }
    public boolean esb_lim_MAIORIGUAL_placa(double lim, double placa)
    {
        return lim >= placa;
    }
    public double bef(double tw, double fy, double mesa)
    {
        return (1.92)*(tw)*(Math.sqrt(E_aco/(fy/10)))*( (1)-((0.34/mesa)*(Math.sqrt(E_aco/(fy/10)))) );
    }
    public double Aef(double ag, double dlinha, double bef, double tw)
    {
        return (ag) - (((dlinha-bef)*tw)/100); //mm para cm dlinha e bef divide 100
    }
    public double Qa_sem_flambagem()
    {
        return 1.0;
    }
    public double Qa_com_flambagem(double aef, double ag)
    {
        return aef / ag;
    }
    public double Qa(double fy,double mesa,double tw,double ag,double dlinha)
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

    public double esbeltez_limite1_AL_laminado(double fy) // b/t lim
    {
        return (0.56) * (Math.sqrt(E_aco/(fy/10)));
    }
    public double esbeltez_limite1_AL_custom(double fy, double kc) // b/t lim
    {
        return (0.64) * (Math.sqrt(E_aco/((fy/10)/kc)));
    }
    public double esbeltez_limite2_AL_laminado(double fy)
    {
        return (1.03) * (Math.sqrt(E_aco/(fy/10)));
    }
    public double esbeltez_limite2_AL_custom(double fy, double kc)
    {
        return (1.17) * (Math.sqrt(E_aco/((fy/10)/kc)));
    }
    public double esbeltez_de_placa_AL(double aba) // banco de dados
    {
        return aba;
    }
    public boolean lim1_MENOR_placa_MENORIGUAL_lim2(double lim1, double placa, double lim2)
    {
        return (lim1 < placa) && (placa <= lim2);
    }
    public boolean placa_MAIOR_lim2(double placa, double lim2)
    {
        return placa > lim2;
    }
    public double Kc(double h, double tw)
    {
        return 4 / Math.sqrt(h/tw);
    }
    public double Qs_sem_flambagem()
    {
        return 1.0;
    }
    public double Qs_com_flambagem_1_laminado(double aba, double fy)
    {
        return 1.415 - (0.74*aba*(Math.sqrt((fy/10)/E_aco)));
    }
    public double Qs_com_flambagem_1_custom(double aba, double fy, double kc)
    {
        return 1.415 - (0.65*aba*(Math.sqrt((fy/10)/(E_aco*kc))));
    }
    public double Qs_com_flambagem_2_laminado(double aba, double fy)
    {
        return (0.69*E_aco)/((fy/10)*(Math.pow(aba,2)));
    }
    public double Qs_com_flambagem_2_custom(double aba, double fy, double kc)
    {
        return (0.90*kc*E_aco)/((fy/10)*(Math.pow(aba,2)));
    }
    public double Qs_laminado(double fy, double aba)
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
    public double Qs_custom(double fy, double aba, double kc)
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
    public double Q(double qa, double qs)
    {
        return qa*qs;
    }

        //flambagem global

    public double esbeltez_x(double kx, double lx, double rx)
    {
        return (kx*lx)/rx;
    }
    public double esbeltez_y(double ky, double ly, double ry)
    {
        return (ky*ly)/ry;
    }
    public double esbeltez_final(double e_x, double e_y)
    {   if(e_x > e_y)
            return e_x;
        return e_y;
    }
    public double Nex(double ix, double kx, double lx)
    {
        return ( Math.pow(Math.PI,2) * E_aco * ix ) / ( Math.pow(kx*lx,2) );
    }
    public double Ney(double iy, double ky, double ly)
    {
        return ( Math.pow(Math.PI,2) * E_aco * iy ) / ( Math.pow(ky*ly,2) );
    }
    public double Nez(double rx, double ry, double cw, double kz, double lz, double j)
    {
        double r0_quadrado = (Math.pow(rx,2)+Math.pow(ry,2));
        return ( 1/r0_quadrado ) * ( ( ( Math.pow(Math.PI,2)*E_aco*cw )/( Math.pow(kz*lz,2) ) ) + ( G*j ) );
    }
    public double Ne(double nex, double ney, double nez)
    {
        double menor = nex;
        if(ney < menor)
            menor = ney;
        if(nez < menor)
            menor = nez;
        return menor;
    }
    public double esbeltez_zero(double Q, double ag, double fy, double Ne) //força critica de flambagem global
    {
        return Math.sqrt((Q*ag*(fy/10))/Ne);
    }
    public double X_1(double esb_zero)
    {
        return Math.pow(0.658,Math.pow(esb_zero,2));
    }
    public double X_2(double esb_zero)
    {
        return 0.877/Math.pow(esb_zero,2);
    }
    public double X(double esb_zero)
    {
        if(esb_zero <= 1.5)
            return X_1(esb_zero);
        return X_2(esb_zero);
    }

        //normal resistente e coef

    public double NCRD(double X, double Q, double ag, double fy)
    {
        return ( X*Q*ag*(fy/10) )/( gama_a1 );
    }
    public double Coeficiente_Utilização(double NCSD, double NCRD)
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
    public void Show_Dimensoes_Database_Perfil(DatabaseAccess db, LinearLayout scroll, Context context, int pos)
    {
        LinearLayout switch_layout = new LinearLayout(context);
        switch_layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView nao = new TextView(context);
        nao.setText("Mostrar Dimensões:\tNão");
        switch_layout.addView(nao);
        Switch sw_dimens = new Switch(context);
        switch_layout.addView(sw_dimens);
        TextView sim = new TextView(context);
        sim.setText("Sim");
        switch_layout.addView(sim);
        switch_layout.setPadding(50,20,0,50);

        scroll.addView(switch_layout);

        final LinearLayout dimens_layout = new LinearLayout(context);
        dimens_layout.setOrientation(LinearLayout.HORIZONTAL);
        scroll.addView(dimens_layout);

        final LinearLayout col_1 = new LinearLayout(context);
        col_1.setOrientation(LinearLayout.VERTICAL);
        dimens_layout.addView(col_1);

        final LinearLayout col_2 = new LinearLayout(context);
        col_2.setOrientation(LinearLayout.VERTICAL);
        dimens_layout.addView(col_2);
        dimens_layout.setGravity(Gravity.CENTER);

        final TextView bf = new TextView(context);
        bf.setText(Html.fromHtml("b<small><sub>f</sub></small> = " + db.get_bf(pos) + " mm"));
        bf.setTextSize(tam_dimens);
        bf.setPadding(50,15,0,15);
        bf.setTextColor(Color.BLACK);

        final TextView tf = new TextView(context);
        tf.setText(Html.fromHtml("t<small><sub>f</sub></small> = " + db.get_tf(pos) + " mm"));
        tf.setTextSize(tam_dimens);
        tf.setPadding(50,15,0,15);
        tf.setTextColor(Color.BLACK);

        final TextView h0 = new TextView(context);
        h0.setText(Html.fromHtml("h<small><sub>0</sub></small> = " + db.get_h(pos) + " mm"));
        h0.setTextSize(tam_dimens);
        h0.setPadding(50,15,0,15);
        h0.setTextColor(Color.BLACK);

        final TextView hw = new TextView(context);
        hw.setText(Html.fromHtml("h<small><sub>w</sub></small> = " + db.get_dlinha(pos) + " mm"));
        hw.setTextSize(tam_dimens);
        hw.setPadding(50,15,0,15);
        hw.setTextColor(Color.BLACK);

        final TextView tw = new TextView(context);
        tw.setText(Html.fromHtml("t<small><sub>w</sub></small> = " + db.get_tw(pos) + " mm"));
        tw.setTextSize(tam_dimens);
        tw.setPadding(50,15,0,15);
        tw.setTextColor(Color.BLACK);

        final TextView ag = new TextView(context);
        ag.setText(Html.fromHtml("A<small><sub>g</sub></small> = " + db.get_area(pos) + " cm²"));
        ag.setTextSize(tam_dimens);
        ag.setPadding(50,15,0,15);
        ag.setTextColor(Color.BLACK);

        final TextView ix = new TextView(context);
        ix.setText(Html.fromHtml("I<small><sub>x</sub></small> = " + db.get_ix(pos) + " cm<small><sup>4</sup></small>"));
        ix.setTextSize(tam_dimens);
        ix.setPadding(50,15,0,15);
        ix.setTextColor(Color.BLACK);

        final TextView wx = new TextView(context);
        wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + db.get_wx(pos) + " cm<small><sup>3</sup></small>"));
        wx.setTextSize(tam_dimens);
        wx.setPadding(50,15,0,15);
        wx.setTextColor(Color.BLACK);

        final TextView zx = new TextView(context);
        zx.setText(Html.fromHtml("Z<small><sub>x</sub></small> = " + db.get_zx(pos) + " cm<small><sup>3</sup></small>"));
        zx.setTextSize(tam_dimens);
        zx.setPadding(50,15,0,50);
        zx.setTextColor(Color.BLACK);

        final TextView rx = new TextView(context);
        rx.setText(Html.fromHtml("r<small><sub>x</sub></small> = " + db.get_rx(pos) + " cm"));
        rx.setTextSize(tam_dimens);
        rx.setPadding(50,15,0,15);
        rx.setTextColor(Color.BLACK);

        final TextView iy = new TextView(context);
        iy.setText(Html.fromHtml("I<small><sub>y</sub></small> = " + db.get_iy(pos) + " cm<small><sup>4</sup></small>"));
        iy.setTextSize(tam_dimens);
        iy.setPadding(50,15,0,15);
        iy.setTextColor(Color.BLACK);

        final TextView wy = new TextView(context);
        wy.setText(Html.fromHtml("W<small><sub>y</sub></small> = " + db.get_wy(pos) + " cm<small><sup>3</sup></small>"));
        wy.setTextSize(tam_dimens);
        wy.setPadding(50,15,0,15);
        wy.setTextColor(Color.BLACK);

        final TextView zy = new TextView(context);
        zy.setText(Html.fromHtml("Z<small><sub>y</sub></small> = " + db.get_zy(pos) + " cm<small><sup>3</sup></small>"));
        zy.setTextSize(tam_dimens);
        zy.setPadding(50,15,0,15);
        zy.setTextColor(Color.BLACK);

        final TextView ry = new TextView(context);
        ry.setText(Html.fromHtml("r<small><sub>y</sub></small> = " + db.get_ry(pos) + " cm"));
        ry.setTextSize(tam_dimens);
        ry.setPadding(50,15,0,15);
        ry.setTextColor(Color.BLACK);

        final TextView rt = new TextView(context);
        rt.setText(Html.fromHtml("r<small><sub>t</sub></small> = " + db.get_rt(pos) + " cm"));
        rt.setTextSize(tam_dimens);
        rt.setPadding(50,15,0,15);
        rt.setTextColor(Color.BLACK);

        final TextView it = new TextView(context);
        it.setText(Html.fromHtml("I<small><sub>t</sub></small> = " + db.get_j(pos) + " cm<small><sup>4</sup></small>"));
        it.setTextSize(tam_dimens);
        it.setPadding(50,15,0,15);
        it.setTextColor(Color.BLACK);

        final TextView cw = new TextView(context);
        cw.setText(Html.fromHtml("C<small><sub>w</sub></small> = " + db.get_cw(pos) + " cm<small><sup>6</sup></small>"));
        cw.setTextSize(tam_dimens);
        cw.setPadding(50,15,0,15);
        cw.setTextColor(Color.BLACK);

        final TextView massa = new TextView(context);
        massa.setText(Html.fromHtml("M. linear = " + db.get_massa(pos) + " kg/m"));
        massa.setTextSize(tam_dimens);
        massa.setPadding(50,15,0,50);
        massa.setTextColor(Color.BLACK);

        sw_dimens.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    col_1.addView(bf);
                    col_1.addView(tf);
                    col_1.addView(h0);
                    col_1.addView(hw);
                    col_1.addView(tw);
                    col_1.addView(ag);
                    col_1.addView(ix);
                    col_1.addView(wx);
                    col_1.addView(zx);
                    col_2.addView(rx);
                    col_2.addView(iy);
                    col_2.addView(wy);
                    col_2.addView(zy);
                    col_2.addView(ry);
                    col_2.addView(rt);
                    col_2.addView(it);
                    col_2.addView(cw);
                    col_2.addView(massa);
                }
                else
                {
                    col_1.removeAllViews();
                    col_2.removeAllViews();
                }
            }
        });
    }
    public void Show_Dimensoes_Database_Perfil(DatabaseCustom db, LinearLayout scroll, Context context )
    {
        LinearLayout switch_layout = new LinearLayout(context);
        switch_layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView nao = new TextView(context);
        nao.setText("Mostrar Dimensões:\tNão");
        switch_layout.addView(nao);
        Switch sw_dimens = new Switch(context);
        switch_layout.addView(sw_dimens);
        TextView sim = new TextView(context);
        sim.setText("Sim");
        switch_layout.addView(sim);
        switch_layout.setPadding(50,20,0,50);

        scroll.addView(switch_layout);

        final LinearLayout dimens_layout = new LinearLayout(context);
        dimens_layout.setOrientation(LinearLayout.HORIZONTAL);
        scroll.addView(dimens_layout);

        final LinearLayout col_1 = new LinearLayout(context);
        col_1.setOrientation(LinearLayout.VERTICAL);
        dimens_layout.addView(col_1);

        final LinearLayout col_2 = new LinearLayout(context);
        col_2.setOrientation(LinearLayout.VERTICAL);
        dimens_layout.addView(col_2);
        dimens_layout.setGravity(Gravity.CENTER);

        final TextView bf = new TextView(context);
        bf.setText(Html.fromHtml("b<small><sub>f</sub></small> = " + casasDecimais(db.getBf(),2) + " mm"));
        bf.setTextSize(tam_dimens);
        bf.setPadding(50,15,0,15);
        bf.setTextColor(Color.BLACK);

        final TextView tf = new TextView(context);
        tf.setText(Html.fromHtml("t<small><sub>f</sub></small> = " + casasDecimais(db.getTf(),2) + " mm"));
        tf.setTextSize(tam_dimens);
        tf.setPadding(50,15,0,15);
        tf.setTextColor(Color.BLACK);

        final TextView h0 = new TextView(context);
        h0.setText(Html.fromHtml("h<small><sub>0</sub></small> = " + casasDecimais(db.getH(),2) + " mm"));
        h0.setTextSize(tam_dimens);
        h0.setPadding(50,15,0,15);
        h0.setTextColor(Color.BLACK);

        final TextView tw = new TextView(context);
        tw.setText(Html.fromHtml("t<small><sub>w</sub></small> = " + casasDecimais(db.getTw(),2) + " mm"));
        tw.setTextSize(tam_dimens);
        tw.setPadding(50,15,0,15);
        tw.setTextColor(Color.BLACK);

        final TextView ag = new TextView(context);
        ag.setText(Html.fromHtml("A<small><sub>g</sub></small> = " + casasDecimais(db.getAg(),2) + " cm²"));
        ag.setTextSize(tam_dimens);
        ag.setPadding(50,15,0,15);
        ag.setTextColor(Color.BLACK);

        final TextView ix = new TextView(context);
        ix.setText(Html.fromHtml("I<small><sub>x</sub></small> = " + casasDecimais(db.getIx(),2) + " cm<small><sup>4</sup></small>"));
        ix.setTextSize(tam_dimens);
        ix.setPadding(50,15,0,15);
        ix.setTextColor(Color.BLACK);

        final TextView wx = new TextView(context);
        wx.setText(Html.fromHtml("W<small><sub>x</sub></small> = " + casasDecimais(db.getWx(),2) + " cm<small><sup>3</sup></small>"));
        wx.setTextSize(tam_dimens);
        wx.setPadding(50,15,0,15);
        wx.setTextColor(Color.BLACK);

        final TextView zx = new TextView(context);
        zx.setText(Html.fromHtml("Z<small><sub>x</sub></small> = " + casasDecimais(db.getZx(),2) + " cm<small><sup>3</sup></small>"));
        zx.setTextSize(tam_dimens);
        zx.setPadding(50,15,0,50);
        zx.setTextColor(Color.BLACK);

        final TextView rx = new TextView(context);
        rx.setText(Html.fromHtml("r<small><sub>x</sub></small> = " + casasDecimais(db.getRx(),2) + " cm"));
        rx.setTextSize(tam_dimens);
        rx.setPadding(50,15,0,15);
        rx.setTextColor(Color.BLACK);

        final TextView iy = new TextView(context);
        iy.setText(Html.fromHtml("I<small><sub>y</sub></small> = " + casasDecimais(db.getIy(),2) + " cm<small><sup>4</sup></small>"));
        iy.setTextSize(tam_dimens);
        iy.setPadding(50,15,0,15);
        iy.setTextColor(Color.BLACK);

        final TextView wy = new TextView(context);
        wy.setText(Html.fromHtml("W<small><sub>y</sub></small> = " + casasDecimais(db.getWy(),2) + " cm<small><sup>3</sup></small>"));
        wy.setTextSize(tam_dimens);
        wy.setPadding(50,15,0,15);
        wy.setTextColor(Color.BLACK);

        final TextView zy = new TextView(context);
        zy.setText(Html.fromHtml("Z<small><sub>y</sub></small> = " + casasDecimais(db.getZy(),2) + " cm<small><sup>3</sup></small>"));
        zy.setTextSize(tam_dimens);
        zy.setPadding(50,15,0,15);
        zy.setTextColor(Color.BLACK);

        final TextView ry = new TextView(context);
        ry.setText(Html.fromHtml("r<small><sub>y</sub></small> = " + casasDecimais(db.getRy(),2) + " cm"));
        ry.setTextSize(tam_dimens);
        ry.setPadding(50,15,0,15);
        ry.setTextColor(Color.BLACK);

        final TextView rt = new TextView(context);
        rt.setText(Html.fromHtml("r<small><sub>t</sub></small> = " + casasDecimais(db.getRt(),2) + " cm"));
        rt.setTextSize(tam_dimens);
        rt.setPadding(50,15,0,15);
        rt.setTextColor(Color.BLACK);

        final TextView it = new TextView(context);
        it.setText(Html.fromHtml("I<small><sub>t</sub></small> = " + casasDecimais(db.getJ(),2) + " cm<small><sup>4</sup></small>"));
        it.setTextSize(tam_dimens);
        it.setPadding(50,15,0,15);
        it.setTextColor(Color.BLACK);

        final TextView cw = new TextView(context);
        cw.setText(Html.fromHtml("C<small><sub>w</sub></small> = " + casasDecimais(db.getCw(),2) + " cm<small><sup>6</sup></small>"));
        cw.setTextSize(tam_dimens);
        cw.setPadding(50,15,0,50);
        cw.setTextColor(Color.BLACK);

        sw_dimens.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    col_1.addView(bf);
                    col_1.addView(tf);
                    col_1.addView(h0);
                    col_1.addView(tw);
                    col_1.addView(ag);
                    col_1.addView(ix);
                    col_1.addView(wx);
                    col_1.addView(zx);
                    col_2.addView(rx);
                    col_2.addView(iy);
                    col_2.addView(wy);
                    col_2.addView(zy);
                    col_2.addView(ry);
                    col_2.addView(rt);
                    col_2.addView(it);
                    col_2.addView(cw);
                    //col_2.addView(massa);
                }
                else
                {
                    col_1.removeAllViews();
                    col_2.removeAllViews();
                }
            }
        });
    }
    private void Show_Results_LaminadoW(DatabaseAccess db, int pos, String perfil, double fy, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz,
                                double qa, double qs, double q, double esbx, double esby, double esb, double nex, double ney, double nez, double ne,
                                        double esbzero, double X, double ncrd, double coef )
    {
        //getting linear layout scroll view
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idcomp);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputVCompressaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        Show_Dimensoes_Database_Perfil(db,scroll_results,OutputVCompressaoActivity.this,pos);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputVCompressaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputVCompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputVCompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputVCompressaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        LinearLayout contorno = new LinearLayout(OutputVCompressaoActivity.this);
        contorno.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(contorno);

        LinearLayout contorno_1 = new LinearLayout(OutputVCompressaoActivity.this);
        contorno_1.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_1);

        LinearLayout contorno_2 = new LinearLayout(OutputVCompressaoActivity.this);
        contorno_2.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_2);

        TextView TV_ncsd = new TextView(OutputVCompressaoActivity.this);
        TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd,2) + " kN"));
        TV_ncsd.setTextSize(tam_pequeno);
        TV_ncsd.setPadding(50,15,0,15);
        contorno_1.addView(TV_ncsd);
        TV_ncsd.setTextColor(Color.BLACK);

        TextView TV_kx = new TextView(OutputVCompressaoActivity.this);
        TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx,2)));
        TV_kx.setTextSize(tam_pequeno);
        TV_kx.setPadding(50,15,0,15);
        contorno_1.addView(TV_kx);
        TV_kx.setTextColor(Color.BLACK);

        TextView TV_ky = new TextView(OutputVCompressaoActivity.this);
        TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky,2) ));
        TV_ky.setTextSize(tam_pequeno);
        TV_ky.setPadding(50,15,0,15);
        contorno_1.addView(TV_ky);
        TV_ky.setTextColor(Color.BLACK);

        TextView TV_kz = new TextView(OutputVCompressaoActivity.this);
        TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz,2) ));
        TV_kz.setTextSize(tam_pequeno);
        TV_kz.setPadding(50,15,0,50);
        contorno_1.addView(TV_kz);
        TV_kz.setTextColor(Color.BLACK);

        TextView TV_lx = new TextView(OutputVCompressaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx,2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(50,15,0,15);
        contorno_2.addView(TV_lx);
        TV_lx.setTextColor(Color.BLACK);

        TextView TV_ly = new TextView(OutputVCompressaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly,2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(50,15,0,15);
        contorno_2.addView(TV_ly);
        TV_ly.setTextColor(Color.BLACK);

        TextView TV_lz = new TextView(OutputVCompressaoActivity.this);
        TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz,2) + " cm"));
        TV_lz.setTextSize(tam_pequeno);
        TV_lz.setPadding(50,15,0,15);
        contorno_2.addView(TV_lz);
        TV_lz.setTextColor(Color.BLACK);

        TextView TV_flamblocal = new TextView(OutputVCompressaoActivity.this);
        TV_flamblocal.setText("FLAMBAGEM LOCAL");
        TV_flamblocal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flamblocal.setTextSize(tam_grande);
        scroll_results.addView(TV_flamblocal);
        TV_flamblocal.setGravity(Gravity.CENTER);
        TV_flamblocal.setTextColor(Color.WHITE);
        TV_flamblocal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flamblocal.setPadding(50,20,50,20);

        TextView TV_Qa = new TextView(OutputVCompressaoActivity.this);
        TV_Qa.setText(Html.fromHtml("Q<small><sub>a</sub></small> = " + casasDecimais(qa,3)));
        TV_Qa.setTextSize(tam_pequeno);
        TV_Qa.setPadding(50,15,0,15);
        scroll_results.addView(TV_Qa);
        TV_Qa.setTextColor(Color.BLACK);

        TextView TV_Qs = new TextView(OutputVCompressaoActivity.this);
        TV_Qs.setText(Html.fromHtml("Q<small><sub>s</sub></small> = " + casasDecimais(qs,3)));
        TV_Qs.setTextSize(tam_pequeno);
        TV_Qs.setPadding(50,15,0,15);
        scroll_results.addView(TV_Qs);
        TV_Qs.setTextColor(Color.BLACK);

        TextView TV_Q = new TextView(OutputVCompressaoActivity.this);
        TV_Q.setText(Html.fromHtml("Q = " + casasDecimais(q,3)));
        TV_Q.setTextSize(tam_pequeno);
        TV_Q.setPadding(50,15,0,50);
        scroll_results.addView(TV_Q);
        TV_Q.setTextColor(Color.BLACK);

        TextView TV_flambglobal = new TextView(OutputVCompressaoActivity.this);
        TV_flambglobal.setText("FLAMBAGEM GLOBAL");
        TV_flambglobal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flambglobal.setTextSize(tam_grande);
        scroll_results.addView(TV_flambglobal);
        TV_flambglobal.setGravity(Gravity.CENTER);
        TV_flambglobal.setTextColor(Color.WHITE);
        TV_flambglobal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flambglobal.setPadding(50,20,50,20);

        LinearLayout glob = new LinearLayout(OutputVCompressaoActivity.this);
        glob.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(glob);

        LinearLayout globesq = new LinearLayout(OutputVCompressaoActivity.this);
        globesq.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globesq);

        LinearLayout globdir = new LinearLayout(OutputVCompressaoActivity.this);
        globdir.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globdir);


        TextView TV_nex = new TextView(OutputVCompressaoActivity.this);
        TV_nex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex,2) + " kN"));
        TV_nex.setTextSize(tam_pequeno);
        TV_nex.setPadding(50,15,0,15);
        globesq.addView(TV_nex);
        TV_nex.setTextColor(Color.BLACK);

        TextView TV_ney = new TextView(OutputVCompressaoActivity.this);
        TV_ney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney,2) + " kN"));
        TV_ney.setTextSize(tam_pequeno);
        TV_ney.setPadding(50,15,0,15);
        globesq.addView(TV_ney);
        TV_ney.setTextColor(Color.BLACK);

        TextView TV_nez = new TextView(OutputVCompressaoActivity.this);
        TV_nez.setText(Html.fromHtml("N<small><sub>ez</sub></small> = " + casasDecimais(nez,2) + " kN"));
        TV_nez.setTextSize(tam_pequeno);
        TV_nez.setPadding(50,15,0,50);
        globesq.addView(TV_nez);
        TV_nez.setTextColor(Color.BLACK);

        TextView TV_ne = new TextView(OutputVCompressaoActivity.this);
        TV_ne.setText(Html.fromHtml("N<small><sub>e</sub></small> = " + casasDecimais(ne,2) + " kN"));
        TV_ne.setTextSize(tam_pequeno);
        TV_ne.setPadding(50,15,0,15);
        globdir.addView(TV_ne);
        TV_ne.setTextColor(Color.BLACK);

        TextView TV_esbzero = new TextView(OutputVCompressaoActivity.this);
        TV_esbzero.setText(Html.fromHtml("λ<small><sub>0</sub></small> = " + casasDecimais(esbzero,3) ));
        TV_esbzero.setTextSize(tam_pequeno);
        TV_esbzero.setPadding(50,15,0,15);
        globdir.addView(TV_esbzero);
        TV_esbzero.setTextColor(Color.BLACK);

        TextView TV_flamb = new TextView(OutputVCompressaoActivity.this);
        TV_flamb.setText(Html.fromHtml("χ = " + casasDecimais(X,3) ));
        TV_flamb.setTextSize(tam_pequeno);
        TV_flamb.setPadding(50,15,0,50);
        globdir.addView(TV_flamb);
        TV_flamb.setTextColor(Color.BLACK);

        //2 - ESBELTEZ
        TextView TV_esbeltezglob = new TextView(OutputVCompressaoActivity.this);
        TV_esbeltezglob.setText("ESBELTEZ GLOBAL");
        TV_esbeltezglob.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esbeltezglob.setTextSize(tam_grande);
        scroll_results.addView(TV_esbeltezglob);
        TV_esbeltezglob.setGravity(Gravity.CENTER);
        TV_esbeltezglob.setTextColor(Color.WHITE);
        TV_esbeltezglob.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_esbeltezglob.setPadding(50,20,50,20);

        TextView TV_esbx = new TextView(OutputVCompressaoActivity.this);
        TV_esbx.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbx,2)));
        TV_esbx.setTextSize(tam_pequeno);
        TV_esbx.setPadding(50,15,0,15);
        scroll_results.addView(TV_esbx);
        TV_esbx.setTextColor(Color.BLACK);

        TextView TV_esby = new TextView(OutputVCompressaoActivity.this);
        TV_esby.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esby,2)));
        TV_esby.setTextSize(tam_pequeno);
        TV_esby.setPadding(50,15,0,15);
        scroll_results.addView(TV_esby);
        TV_esby.setTextColor(Color.BLACK);

        TextView TV_esb = new TextView(OutputVCompressaoActivity.this);
        TV_esb.setText(Html.fromHtml("λ = " + casasDecimais(esb,2)));
        TV_esb.setTextSize(tam_pequeno);
        TV_esb.setPadding(50,15,0,50);
        scroll_results.addView(TV_esb);
        TV_esb.setTextColor(Color.BLACK);

        TextView TV_compressao = new TextView(OutputVCompressaoActivity.this);
        TV_compressao.setText("COMPRESSÃO RESISTENTE DE CÁLCULO");
        TV_compressao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_compressao.setTextSize(tam_grande);
        scroll_results.addView(TV_compressao);
        TV_compressao.setGravity(Gravity.CENTER);
        TV_compressao.setTextColor(Color.WHITE);
        TV_compressao.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_compressao.setPadding(50,20,50,20);

        TextView TV_compNCRD = new TextView(OutputVCompressaoActivity.this);
        TV_compNCRD.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> = " + casasDecimais(ncrd,2) + " kN"));
        TV_compNCRD.setTextSize(tam_pequeno);
        TV_compNCRD.setPadding(50,15,0,50);
        scroll_results.addView(TV_compNCRD);
        TV_compNCRD.setTextColor(Color.BLACK);

        TextView TV_coef = new TextView(OutputVCompressaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);
        TV_coef.setGravity(Gravity.CENTER);
        TV_coef.setTextColor(Color.WHITE);
        TV_coef.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_coef.setPadding(50,20,50,20);

        TextView TV_coefvalor = new TextView(OutputVCompressaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small> = " + casasDecimais(coef,3) + " kN"));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(50,15,0,50);
        scroll_results.addView(TV_coefvalor);
        TV_coefvalor.setTextColor(Color.BLACK);

        TextView TV_verif = new TextView(OutputVCompressaoActivity.this);
        TV_verif.setText("VERIFICAÇÕES");
        TV_verif.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verif.setTextSize(tam_grande);
        scroll_results.addView(TV_verif);
        TV_verif.setGravity(Gravity.CENTER);
        TV_verif.setTextColor(Color.WHITE);
        TV_verif.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_verif.setPadding(50,20,50,20);

        if(NCRD_MaiorIgual_NCSD(ncrd,ncsd))
        {
            TextView TV_nc = new TextView(OutputVCompressaoActivity.this);
            TV_nc.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> > N<small><sub>c,Sd</sub></small>: OK! "));
            TV_nc.setTextSize(tam_pequeno);
            TV_nc.setPadding(50,15,0,15);
            TV_nc.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_nc);
        }
        else
        {
            TextView TV_nc = new TextView(OutputVCompressaoActivity.this);
            TV_nc.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> < N<small><sub>c,Sd</sub></small>: NÃO OK! "));
            TV_nc.setTextSize(tam_pequeno);
            TV_nc.setPadding(50,15,0,15);
            TV_nc.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_nc);
        }

        if(ESBELTEZ_MenorIgual_200(esb))
        {
            TextView TV_esb200 = new TextView(OutputVCompressaoActivity.this);
            TV_esb200.setText("Esbeltez < 200: OK!");
            TV_esb200.setTextSize(tam_pequeno);
            TV_esb200.setPadding(50,15,0,50);
            TV_esb200.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_esb200);
        }
        else
        {
            TextView TV_esb200 = new TextView(OutputVCompressaoActivity.this);
            TV_esb200.setText("Esbeltez > 200: NÃO OK!");
            TV_esb200.setTextSize(tam_pequeno);
            TV_esb200.setPadding(50,15,0,50);
            TV_esb200.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_esb200);
        }


    }
    private void Show_Results_SoldadoCustom(DatabaseCustom db, String perfil, double fy, double ncsd, double kx, double ky, double kz, double lx, double ly, double lz,
                                            double qa, double qs, double q, double esbx, double esby, double esb, double nex, double ney, double nez, double ne,
                                            double esbzero, double X, double ncrd, double coef  )
    {
        //getting linear layout scroll view
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idcomp);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));

        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputVCompressaoActivity.this);
        TV_perfil.setText("PERFIL " + perfil);
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        Show_Dimensoes_Database_Perfil(db,scroll_results,OutputVCompressaoActivity.this);

        //2 - Parametros
        TextView TV_parametros = new TextView(OutputVCompressaoActivity.this);
        TV_parametros.setText("PARÂMETROS DO MATERIAL");
        TV_parametros.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_parametros.setTextSize(tam_grande);
        scroll_results.addView(TV_parametros);
        TV_parametros.setGravity(Gravity.CENTER);
        TV_parametros.setTextColor(Color.WHITE);
        TV_parametros.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_parametros.setPadding(50,20,50,20);

        TextView TV_elasticidade = new TextView(OutputVCompressaoActivity.this);
        TV_elasticidade.setText(Html.fromHtml("E<small><sub>aco</sub></small> = 200 GPa"));
        TV_elasticidade.setTextSize(tam_pequeno);
        TV_elasticidade.setPadding(50,15,0,15);
        scroll_results.addView(TV_elasticidade);
        TV_elasticidade.setTextColor(Color.BLACK);

        TextView TV_fy = new TextView(OutputVCompressaoActivity.this);
        TV_fy.setText(Html.fromHtml("f<small><sub>y</sub></small> = " + casasDecimais(fy,2) + " MPa"));
        TV_fy.setTextSize(tam_pequeno);
        TV_fy.setPadding(50,15,0,50);
        scroll_results.addView(TV_fy);
        TV_fy.setTextColor(Color.BLACK);

        //3 - Solicitacoes e contorno
        TextView TV_solic = new TextView(OutputVCompressaoActivity.this);
        TV_solic.setText("SOLICITAÇÕES E CONDIÇÕES DE CONTORNO");
        TV_solic.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_solic.setTextSize(tam_grande);
        scroll_results.addView(TV_solic);
        TV_solic.setGravity(Gravity.CENTER);
        TV_solic.setTextColor(Color.WHITE);
        TV_solic.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_solic.setPadding(50,20,50,20);

        LinearLayout contorno = new LinearLayout(OutputVCompressaoActivity.this);
        contorno.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(contorno);

        LinearLayout contorno_1 = new LinearLayout(OutputVCompressaoActivity.this);
        contorno_1.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_1);

        LinearLayout contorno_2 = new LinearLayout(OutputVCompressaoActivity.this);
        contorno_2.setOrientation(LinearLayout.VERTICAL);
        contorno.addView(contorno_2);

        TextView TV_ncsd = new TextView(OutputVCompressaoActivity.this);
        TV_ncsd.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> = " + casasDecimais(ncsd,2) + " kN"));
        TV_ncsd.setTextSize(tam_pequeno);
        TV_ncsd.setPadding(50,15,0,15);
        contorno_1.addView(TV_ncsd);
        TV_ncsd.setTextColor(Color.BLACK);

        TextView TV_kx = new TextView(OutputVCompressaoActivity.this);
        TV_kx.setText(Html.fromHtml("k<small><sub>x</sub></small> = " + casasDecimais(kx,2)));
        TV_kx.setTextSize(tam_pequeno);
        TV_kx.setPadding(50,15,0,15);
        contorno_1.addView(TV_kx);
        TV_kx.setTextColor(Color.BLACK);

        TextView TV_ky = new TextView(OutputVCompressaoActivity.this);
        TV_ky.setText(Html.fromHtml("k<small><sub>y</sub></small> = " + casasDecimais(ky,2) ));
        TV_ky.setTextSize(tam_pequeno);
        TV_ky.setPadding(50,15,0,15);
        contorno_1.addView(TV_ky);
        TV_ky.setTextColor(Color.BLACK);

        TextView TV_kz = new TextView(OutputVCompressaoActivity.this);
        TV_kz.setText(Html.fromHtml("k<small><sub>z</sub></small> = " + casasDecimais(kz,2) ));
        TV_kz.setTextSize(tam_pequeno);
        TV_kz.setPadding(50,15,0,50);
        contorno_1.addView(TV_kz);
        TV_kz.setTextColor(Color.BLACK);

        TextView TV_lx = new TextView(OutputVCompressaoActivity.this);
        TV_lx.setText(Html.fromHtml("L<small><sub>x</sub></small> = " + casasDecimais(lx,2) + " cm"));
        TV_lx.setTextSize(tam_pequeno);
        TV_lx.setPadding(50,15,0,15);
        contorno_2.addView(TV_lx);
        TV_lx.setTextColor(Color.BLACK);

        TextView TV_ly = new TextView(OutputVCompressaoActivity.this);
        TV_ly.setText(Html.fromHtml("L<small><sub>y</sub></small> = " + casasDecimais(ly,2) + " cm"));
        TV_ly.setTextSize(tam_pequeno);
        TV_ly.setPadding(50,15,0,15);
        contorno_2.addView(TV_ly);
        TV_ly.setTextColor(Color.BLACK);

        TextView TV_lz = new TextView(OutputVCompressaoActivity.this);
        TV_lz.setText(Html.fromHtml("L<small><sub>z</sub></small> = " + casasDecimais(lz,2) + " cm"));
        TV_lz.setTextSize(tam_pequeno);
        TV_lz.setPadding(50,15,0,15);
        contorno_2.addView(TV_lz);
        TV_lz.setTextColor(Color.BLACK);

        TextView TV_flamblocal = new TextView(OutputVCompressaoActivity.this);
        TV_flamblocal.setText("FLAMBAGEM LOCAL");
        TV_flamblocal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flamblocal.setTextSize(tam_grande);
        scroll_results.addView(TV_flamblocal);
        TV_flamblocal.setGravity(Gravity.CENTER);
        TV_flamblocal.setTextColor(Color.WHITE);
        TV_flamblocal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flamblocal.setPadding(50,20,50,20);

        TextView TV_Qa = new TextView(OutputVCompressaoActivity.this);
        TV_Qa.setText(Html.fromHtml("Q<small><sub>a</sub></small> = " + casasDecimais(qa,3)));
        TV_Qa.setTextSize(tam_pequeno);
        TV_Qa.setPadding(50,15,0,15);
        scroll_results.addView(TV_Qa);
        TV_Qa.setTextColor(Color.BLACK);

        TextView TV_Qs = new TextView(OutputVCompressaoActivity.this);
        TV_Qs.setText(Html.fromHtml("Q<small><sub>s</sub></small> = " + casasDecimais(qs,3)));
        TV_Qs.setTextSize(tam_pequeno);
        TV_Qs.setPadding(50,15,0,15);
        scroll_results.addView(TV_Qs);
        TV_Qs.setTextColor(Color.BLACK);

        TextView TV_Q = new TextView(OutputVCompressaoActivity.this);
        TV_Q.setText(Html.fromHtml("Q = " + casasDecimais(q,3)));
        TV_Q.setTextSize(tam_pequeno);
        TV_Q.setPadding(50,15,0,50);
        scroll_results.addView(TV_Q);
        TV_Q.setTextColor(Color.BLACK);

        TextView TV_flambglobal = new TextView(OutputVCompressaoActivity.this);
        TV_flambglobal.setText("FLAMBAGEM GLOBAL");
        TV_flambglobal.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_flambglobal.setTextSize(tam_grande);
        scroll_results.addView(TV_flambglobal);
        TV_flambglobal.setGravity(Gravity.CENTER);
        TV_flambglobal.setTextColor(Color.WHITE);
        TV_flambglobal.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_flambglobal.setPadding(50,20,50,20);

        LinearLayout glob = new LinearLayout(OutputVCompressaoActivity.this);
        glob.setOrientation(LinearLayout.HORIZONTAL);
        scroll_results.addView(glob);

        LinearLayout globesq = new LinearLayout(OutputVCompressaoActivity.this);
        globesq.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globesq);

        LinearLayout globdir = new LinearLayout(OutputVCompressaoActivity.this);
        globdir.setOrientation(LinearLayout.VERTICAL);
        glob.addView(globdir);


        TextView TV_nex = new TextView(OutputVCompressaoActivity.this);
        TV_nex.setText(Html.fromHtml("N<small><sub>ex</sub></small> = " + casasDecimais(nex,2) + " kN"));
        TV_nex.setTextSize(tam_pequeno);
        TV_nex.setPadding(50,15,0,15);
        globesq.addView(TV_nex);
        TV_nex.setTextColor(Color.BLACK);

        TextView TV_ney = new TextView(OutputVCompressaoActivity.this);
        TV_ney.setText(Html.fromHtml("N<small><sub>ey</sub></small> = " + casasDecimais(ney,2) + " kN"));
        TV_ney.setTextSize(tam_pequeno);
        TV_ney.setPadding(50,15,0,15);
        globesq.addView(TV_ney);
        TV_ney.setTextColor(Color.BLACK);

        TextView TV_nez = new TextView(OutputVCompressaoActivity.this);
        TV_nez.setText(Html.fromHtml("N<small><sub>ez</sub></small> = " + casasDecimais(nez,2) + " kN"));
        TV_nez.setTextSize(tam_pequeno);
        TV_nez.setPadding(50,15,0,50);
        globesq.addView(TV_nez);
        TV_nez.setTextColor(Color.BLACK);

        TextView TV_ne = new TextView(OutputVCompressaoActivity.this);
        TV_ne.setText(Html.fromHtml("N<small><sub>e</sub></small> = " + casasDecimais(ne,2) + " kN"));
        TV_ne.setTextSize(tam_pequeno);
        TV_ne.setPadding(50,15,0,15);
        globdir.addView(TV_ne);
        TV_ne.setTextColor(Color.BLACK);

        TextView TV_esbzero = new TextView(OutputVCompressaoActivity.this);
        TV_esbzero.setText(Html.fromHtml("λ<small><sub>0</sub></small> = " + casasDecimais(esbzero,3) ));
        TV_esbzero.setTextSize(tam_pequeno);
        TV_esbzero.setPadding(50,15,0,15);
        globdir.addView(TV_esbzero);
        TV_esbzero.setTextColor(Color.BLACK);

        TextView TV_flamb = new TextView(OutputVCompressaoActivity.this);
        TV_flamb.setText(Html.fromHtml("χ = " + casasDecimais(X,3) ));
        TV_flamb.setTextSize(tam_pequeno);
        TV_flamb.setPadding(50,15,0,50);
        globdir.addView(TV_flamb);
        TV_flamb.setTextColor(Color.BLACK);

        //2 - ESBELTEZ
        TextView TV_esbeltezglob = new TextView(OutputVCompressaoActivity.this);
        TV_esbeltezglob.setText("ESBELTEZ GLOBAL");
        TV_esbeltezglob.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_esbeltezglob.setTextSize(tam_grande);
        scroll_results.addView(TV_esbeltezglob);
        TV_esbeltezglob.setGravity(Gravity.CENTER);
        TV_esbeltezglob.setTextColor(Color.WHITE);
        TV_esbeltezglob.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_esbeltezglob.setPadding(50,20,50,20);

        TextView TV_esbx = new TextView(OutputVCompressaoActivity.this);
        TV_esbx.setText(Html.fromHtml("λ<small><sub>x</sub></small> = " + casasDecimais(esbx,2)));
        TV_esbx.setTextSize(tam_pequeno);
        TV_esbx.setPadding(50,15,0,15);
        scroll_results.addView(TV_esbx);
        TV_esbx.setTextColor(Color.BLACK);

        TextView TV_esby = new TextView(OutputVCompressaoActivity.this);
        TV_esby.setText(Html.fromHtml("λ<small><sub>y</sub></small> = " + casasDecimais(esby,2)));
        TV_esby.setTextSize(tam_pequeno);
        TV_esby.setPadding(50,15,0,15);
        scroll_results.addView(TV_esby);
        TV_esby.setTextColor(Color.BLACK);

        TextView TV_esb = new TextView(OutputVCompressaoActivity.this);
        TV_esb.setText(Html.fromHtml("λ = " + casasDecimais(esb,2)));
        TV_esb.setTextSize(tam_pequeno);
        TV_esb.setPadding(50,15,0,50);
        scroll_results.addView(TV_esb);
        TV_esb.setTextColor(Color.BLACK);

        TextView TV_compressao = new TextView(OutputVCompressaoActivity.this);
        TV_compressao.setText("COMPRESSÃO RESISTENTE DE CÁLCULO");
        TV_compressao.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_compressao.setTextSize(tam_grande);
        scroll_results.addView(TV_compressao);
        TV_compressao.setGravity(Gravity.CENTER);
        TV_compressao.setTextColor(Color.WHITE);
        TV_compressao.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_compressao.setPadding(50,20,50,20);

        TextView TV_compNCRD = new TextView(OutputVCompressaoActivity.this);
        TV_compNCRD.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> = " + casasDecimais(ncrd,2) + " kN"));
        TV_compNCRD.setTextSize(tam_pequeno);
        TV_compNCRD.setPadding(50,15,0,50);
        scroll_results.addView(TV_compNCRD);
        TV_compNCRD.setTextColor(Color.BLACK);

        TextView TV_coef = new TextView(OutputVCompressaoActivity.this);
        TV_coef.setText("COEFICIENTE DE UTILIZAÇÃO");
        TV_coef.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_coef.setTextSize(tam_grande);
        scroll_results.addView(TV_coef);
        TV_coef.setGravity(Gravity.CENTER);
        TV_coef.setTextColor(Color.WHITE);
        TV_coef.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_coef.setPadding(50,20,50,20);

        TextView TV_coefvalor = new TextView(OutputVCompressaoActivity.this);
        TV_coefvalor.setText(Html.fromHtml("N<small><sub>c,Sd</sub></small> / N<small><sub>c,Rd</sub></small> = " + casasDecimais(coef,3) + " kN"));
        TV_coefvalor.setTextSize(tam_pequeno);
        TV_coefvalor.setPadding(50,15,0,50);
        scroll_results.addView(TV_coefvalor);
        TV_coefvalor.setTextColor(Color.BLACK);

        TextView TV_verif = new TextView(OutputVCompressaoActivity.this);
        TV_verif.setText("VERIFICAÇÕES");
        TV_verif.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_verif.setTextSize(tam_grande);
        scroll_results.addView(TV_verif);
        TV_verif.setGravity(Gravity.CENTER);
        TV_verif.setTextColor(Color.WHITE);
        TV_verif.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_verif.setPadding(50,20,50,20);

        if(NCRD_MaiorIgual_NCSD(ncrd,ncsd))
        {
            TextView TV_nc = new TextView(OutputVCompressaoActivity.this);
            TV_nc.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> > N<small><sub>c,Sd</sub></small>: OK! "));
            TV_nc.setTextSize(tam_pequeno);
            TV_nc.setPadding(50,15,0,15);
            TV_nc.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_nc);
        }
        else
        {
            TextView TV_nc = new TextView(OutputVCompressaoActivity.this);
            TV_nc.setText(Html.fromHtml("N<small><sub>c,Rd</sub></small> < N<small><sub>c,Sd</sub></small>: NÃO OK! "));
            TV_nc.setTextSize(tam_pequeno);
            TV_nc.setPadding(50,15,0,15);
            TV_nc.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_nc);
        }

        if(ESBELTEZ_MenorIgual_200(esb))
        {
            TextView TV_esb200 = new TextView(OutputVCompressaoActivity.this);
            TV_esb200.setText("Esbeltez < 200: OK!");
            TV_esb200.setTextSize(tam_pequeno);
            TV_esb200.setPadding(50,15,0,50);
            TV_esb200.setTextColor(getResources().getColor(R.color.color_ok));
            scroll_results.addView(TV_esb200);
        }
        else
        {
            TextView TV_esb200 = new TextView(OutputVCompressaoActivity.this);
            TV_esb200.setText("Esbeltez > 200: NÃO OK!");
            TV_esb200.setTextSize(tam_pequeno);
            TV_esb200.setPadding(50,15,0,50);
            TV_esb200.setTextColor(getResources().getColor(R.color.color_Nok));
            scroll_results.addView(TV_esb200);
        }

    }
    private void Show_Kc_erro(double kc, double h, double tw)
    {
        scroll_results = (LinearLayout) findViewById(R.id.scroll_results_idcomp);
        scroll_results.setBackgroundColor(getResources().getColor(R.color.output_infoback));
        //1 - PERFIL
        TextView TV_perfil = new TextView(OutputVCompressaoActivity.this);
        TV_perfil.setText("PERFIL CUSTOMIZADO");
        TV_perfil.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil);
        TV_perfil.setGravity(Gravity.CENTER);
        TV_perfil.setTextColor(Color.WHITE);
        TV_perfil.setBackgroundColor(getResources().getColor(R.color.output_blue));
        TV_perfil.setPadding(50,20,50,20);

        TextView TV_perfil_dimen = new TextView(OutputVCompressaoActivity.this);
        TV_perfil_dimen.setText("ERRO");
        TV_perfil_dimen.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
        TV_perfil_dimen.setTextSize(tam_grande);
        scroll_results.addView(TV_perfil_dimen);
        TV_perfil_dimen.setGravity(Gravity.CENTER);
        TV_perfil_dimen.setTextColor(Color.WHITE);
        TV_perfil_dimen.setBackgroundColor(getResources().getColor(R.color.color_Nok));
        TV_perfil_dimen.setPadding(50,20,50,20);

        TextView TV_kc = new TextView(OutputVCompressaoActivity.this);
        TV_kc.setGravity(Gravity.CENTER);
        TV_kc.setText("\n\nKc = 4/( √(h/tw) ) \n= 4/( √(" + casasDecimais(h,2) + "/" + casasDecimais(tw,2) + ") ) \n= " + casasDecimais(kc,2));
        TV_kc.setTextSize(tam_pequeno);
        scroll_results.addView(TV_kc);
        TV_kc.setPadding(50,15,50,50);
        TV_kc.setTextColor(Color.BLACK);

        TextView TV_restricao = new TextView(OutputVCompressaoActivity.this);
        TV_restricao.setGravity(Gravity.CENTER);
        TV_restricao.setText("\nE Kc precisa atender a restrição :\n 0.35 ≤ Kc ≤ 0.76");
        TV_restricao.setTextSize(tam_pequeno);
        scroll_results.addView(TV_restricao);
        TV_restricao.setPadding(50,15,50,50);
        TV_restricao.setTextColor(Color.BLACK);


    }



}
