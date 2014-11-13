package com.abc.weathernow;

import java.util.Date;

import org.json.JSONException;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends Activity {

	private TextView weatherIcon;
	private TextView temp;
	Typeface weatherFont;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String city = "Delhi,IN";
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weather.ttf");
        
        weatherIcon = (TextView) findViewById(R.id.weather_icon);
        temp = (TextView) findViewById(R.id.info_text);
        
        weatherIcon.setTypeface(weatherFont);
        
        JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[]{city});
    }
	
	private void setWeatherIcon(int actualId, long sunrise, long sunset){
	    int id = actualId / 100;
	    String icon = "";
	    if(actualId == 800){
	        long currentTime = new Date().getTime();
	        if(currentTime>=sunrise && currentTime<sunset) {
	            icon = getResources().getString(R.string.weather_sunny);
	        } else {
	            icon = getResources().getString(R.string.weather_clear_night);
	        }
	    } else {
	        switch(id) {
	        case 2 : icon = getResources().getString(R.string.weather_thunder);
	                 break;         
	        case 3 : icon = getResources().getString(R.string.weather_drizzle);
	                 break;     
	        case 7 : icon = getResources().getString(R.string.weather_foggy);
	                 break;
	        case 8 : icon = getResources().getString(R.string.weather_cloudy);
	                 break;
	        case 6 : icon = getResources().getString(R.string.weather_snowy);
	                 break;
	        case 5 : icon = getResources().getString(R.string.weather_rainy);
	                 break;
	        }
	    }
	    weatherIcon.setText(icon);
	}
    
private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
		
		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();
			String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

			try {
				weather = JSONWeatherParser.getWeather(data);
				
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			return weather;
		
	}
		
	@Override
		protected void onPostExecute(Weather weather) {			
			super.onPostExecute(weather);
			temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "\u00b0 C");
			setWeatherIcon(weather.currentCondition.getWeatherId(),weather.location.getSunrise(),weather.location.getSunset());
		}
	
  }
}
