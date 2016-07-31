package com.akproject.easybuy.ods;

import android.os.AsyncTask;

import com.akproject.easybuy.utility.LogManager;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Allan on 5/6/2016.
 */
public class WebServiceCall extends AsyncTask<String, Void, Void>  {

    @Override
    protected Void doInBackground(String... params) {
        //Invoke webservice
        testWS();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    public void testWS() {
        //String URL = "http://www.w3schools.com/xml/tempconvert.asmx?WSDL";
        //String NAMESPACE = "http://www.w3schools.com/xml/";
        //String METHOD_NAME = "CelsiusToFahrenheit";
        String URL = "http://10.0.2.2:8080/EasyBuyWS/services/HelloWorld?WSDL";
        String NAMESPACE = "http://service.web.easybuy.akproject.com/";
        String METHOD_NAME = "testtest";
        String SOAP_ACTION =  NAMESPACE + METHOD_NAME;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        //Float a = new Float(40.1);
        //request.addProperty("value", a);
        request.addProperty("a", "sadfsdg");

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL, 60 * 1000);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();
            LogManager.logSystem(resultsRequestSOAP.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
