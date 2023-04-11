package org.chennaimetrorail.appv1;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;

import static com.norbsoft.typefacehelper.TypefaceHelper.typeface;

public class FontStyle {

    public Typeface helveticaCE, helveticabold_CE;

    public void Changeview(Context context, View view) {
        TypefaceCollection typeface = new TypefaceCollection.Builder().set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaCE-Cond.ttf")).create();
        TypefaceHelper.init(typeface);
        typeface(view);
        helveticaCE = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaCE-CondBold.ttf");
        helveticabold_CE = Typeface.create(helveticaCE, Typeface.BOLD);


    }
    public void Changeview(Context context) {
        TypefaceCollection typeface = new TypefaceCollection.Builder().set(Typeface.NORMAL, Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaCE-Cond.ttf")).create();
        TypefaceHelper.init(typeface);
        typeface((Activity) context);
        helveticaCE = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaCE-CondBold.ttf");
        helveticabold_CE = Typeface.create(helveticaCE, Typeface.BOLD);


    }

}
