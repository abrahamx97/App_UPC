package mx.edu.updc.app_upc;

import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

/**
 * Created by root on 2/12/16.
 */

public class UPClient {

    UPClient(){

    }


    private AsyncHttpClient client = new AsyncHttpClient();
    private final int activo=1;
    private final int baja=0;
    private boolean logeado;

    public boolean JSON_get(String URL, RequestParams params ){
        client.post(URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int encontrado=1;
                    if (response.getInt("estado")==encontrado){
                        if (response.getInt("activo")==activo) {
                            int id_profesor = response.getInt("id_profesor");
                            //CARGAR TODO
                            logeado = true;
                        }else if (response.getInt("activo")==baja){
                            //SI EL USUARIO ESTA DADO DE BAJA
                            logeado = false;
                        }
                    }else{
                        //NO SE ENCONTRO USUARIO
                        logeado = false;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                //NO SE RETORNO UN JSON VALIDO
                logeado = false;
            }
        }) ;

        return logeado;
    }

    public void JSON_post(String URL, RequestParams params){

    }
}

class Carreras{
    JSONObject response ;
    Carreras(JSONObject response){
        this.response=response;
    }

    public void getInfo(){

    }


}
