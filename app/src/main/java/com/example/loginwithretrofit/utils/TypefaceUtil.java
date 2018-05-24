package com.example.loginwithretrofit.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

public class TypefaceUtil {

	
	 public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
	        try {
	            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

	            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
	            defaultFontTypefaceField.setAccessible(true);
	            defaultFontTypefaceField.set(null, customFontTypeface);
	            
	        } catch (Exception e) {
	            Log.e("Text Log","Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
	        }
	    }
	 
}
