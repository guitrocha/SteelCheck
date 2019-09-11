package steelcheck.net.steelcheck;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    //private contructor so that object creation is avoided from outside the class
    private DatabaseAccess(Context context)
    {
        this.openHelper = new DatabaseOpenHelper(context);
    }
    //to return the single instance of database
    public static DatabaseAccess getInstance(Context context)
    {
        if(instance == null) instance = new DatabaseAccess(context);
        return instance;
    }
    //to open the database
    public void open()
    {
        this.db = openHelper.getWritableDatabase();
    }
    //to close the database connection
    public void close()
    {
        if(db != null) this.db.close();
    }

    //INSERT METHODS HERE
    public void resetCursor()
    {
        c.moveToFirst();
    }
    public int index_init(int position, String name)
    {
        if(c==null)
            c = db.rawQuery("SELECT * FROM perfis",null);
        c.moveToPosition(position-1);
        return c.getColumnIndex(name);
    }
    public String get_perfil(int position)
    {   int index = index_init(position,"perfil");
        return c.getString(index);
    }
    public int get_id(int position)
    {   int index = index_init(position,"id");
        return c.getInt(index);
    }
    public double get_massa(int position)
    {   int index = index_init(position,"massa");
        return c.getDouble(index);
    }
    public double get_d(int position)
    {   int index = index_init(position,"d");
        return c.getDouble(index);
    }
    public double get_bf(int position)
    {   int index = index_init(position,"bf");
        return c.getDouble(index);
    }
    public double get_tw(int position)
    {   int index = index_init(position,"tw");
        return c.getDouble(index);
    }
    public double get_tf(int position)
    {   int index = index_init(position,"tf");
        return c.getDouble(index);
    }
    public double get_h(int position)
    {   int index = index_init(position,"h");
        return c.getDouble(index);
    }
    public double get_dlinha(int position)
    {   int index = index_init(position,"dlinha");
        return c.getDouble(index);
    }
    public double get_area(int position)
    {   int index = index_init(position,"area");
        return c.getDouble(index);
    }
    public double get_ix(int position)
    {   int index = index_init(position,"ix");
        return c.getDouble(index);
    }
    public double get_wx(int position)
    {   int index = index_init(position,"wx");
        return c.getDouble(index);
    }
    public double get_rx(int position)
    {   int index = index_init(position,"rx");
        return c.getDouble(index);
    }
    public double get_zx(int position)
    {   int index = index_init(position,"zx");
        return c.getDouble(index);
    }
    public double get_iy(int position)
    {   int index = index_init(position,"iy");
        return c.getDouble(index);
    }
    public double get_wy(int position)
    {   int index = index_init(position,"wy");
        return c.getDouble(index);
    }
    public double get_ry(int position)
    {   int index = index_init(position,"ry");
        return c.getDouble(index);
    }
    public double get_zy(int position)
    {   int index = index_init(position,"zy");
        return c.getDouble(index);
    }
    public double get_rt(int position)
    {   int index = index_init(position,"rt");
        return c.getDouble(index);
    }
    public double get_j(int position)
    {   int index = index_init(position,"j");
        return c.getDouble(index);
    }
    public double get_aba(int position)
    {   int index = index_init(position,"aba");
        return c.getDouble(index);
    }
    public double get_mesa(int position)
    {   int index = index_init(position,"mesa");
        return c.getDouble(index);
    }
    public double get_cw(int position)
    {   int index = index_init(position,"cw");
        return c.getDouble(index);
    }
    public double get_u(int position)
    {   int index = index_init(position,"u");
        return c.getDouble(index);
    }
}