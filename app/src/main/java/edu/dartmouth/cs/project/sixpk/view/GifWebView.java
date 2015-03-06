package edu.dartmouth.cs.project.sixpk.view;

import android.content.Context;
import android.webkit.WebView;

/**
 * Created by koliver on 3/6/15.
 *
 * A class used to display a gi
 */
public class GifWebView extends WebView {

    public GifWebView(Context context, String path) {
        super(context);
        loadUrl(path);
    }
}