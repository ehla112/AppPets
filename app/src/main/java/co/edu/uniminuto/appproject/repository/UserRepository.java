package co.edu.uniminuto.appproject.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;



import com.google.android.material.snackbar.Snackbar;

import co.edu.uniminuto.appproject.dataaccess.ManagerDataBase;
import co.edu.uniminuto.appproject.entities.User;

public class UserRepository {
    private ManagerDataBase dataBase;
    private View view;
    private Context context;
    private User user;

    public UserRepository(View view, Context context) {
        this.view = view;
        this.context = context;
        this.dataBase = new ManagerDataBase(context);
    }
        public User login(String user, String password){
                SQLiteDatabase sqLiteDatabase = dataBase.getReadableDatabase();
                String sql = "SELECT * FROM users WHERE use_user = ? AND use_password = ?";
                Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{user, password});
                if(cursor.moveToFirst()){
                    User us = new User();
                    us.setId(cursor.getInt(0));
                    us.setUser(cursor.getString(1));
                    us.setName(cursor.getString(2));
                    us.setPassword(cursor.getString(3));
                    us.setEmail(cursor.getString(4));
                    us.setPhone(cursor.getString(5));
                    us.setAddress(cursor.getString(6));
                    us.setStatus(cursor.getInt(7) == 1);
                    cursor.close();
                    return us;
                }else{
                    return null;
                }




        }
    public long insertUser(User user){
        long newID=-1;
        try {
        SQLiteDatabase sqLiteDatabase = dataBase.getWritableDatabase();
        if(sqLiteDatabase != null){
            ContentValues values = new ContentValues();
            values.put("use_user", user.getUser());
            values.put("use_name", user.getName());
            values.put("use_password", user.getPassword());
            values.put("use_email", user.getEmail());
            values.put("use_phone", user.getPhone());
            values.put("use_address", user.getAddress());
            values.put("use_status", "1");
            newID=sqLiteDatabase.insert("users", null, values);
            sqLiteDatabase.close();

        }
    }catch (Exception e){
            Log.i("Error en bases de datos", "insertUser: " +e.getMessage());
            throw new RuntimeException(e);
        }return newID;

        }

    public User getUserById(int id){
        SQLiteDatabase sqLiteDatabase = dataBase.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM users WHERE use_id = ? AND use_status = 1",
                new String[]{String.valueOf(id)}
        );
        if(cursor.moveToFirst()){
            User us = new User();
            us.setId(cursor.getInt(0));
            us.setUser(cursor.getString(1));
            us.setName(cursor.getString(2));
            us.setPassword(cursor.getString(3));
            us.setEmail(cursor.getString(4));
            us.setPhone(cursor.getString(5));
            us.setAddress(cursor.getString(6));
            cursor.close();
            return us;
        }
        cursor.close();
        return null;
    }
    public boolean updateUser(int id, String usuario, String nombre, String contrasena, String correo, String telefono, String direccion) {
        SQLiteDatabase sqLiteDatabase = dataBase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("use_user", usuario);
        values.put("use_name", nombre);
        values.put("use_password", contrasena);
        values.put("use_email", correo);
        values.put("use_phone", telefono);
        values.put("use_address", direccion);

        int rows = sqLiteDatabase.update("users", values, "use_id = ?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        return rows > 0;
    }
    public boolean disableUser(int id){
        SQLiteDatabase sqLiteDatabase = dataBase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("use_status", 0);

        int rows = sqLiteDatabase.update("users", values, "use_id = ?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
        return rows > 0;

    }

}
