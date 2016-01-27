/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/* Custom imageview for the gridview to have square images. Used for the imageviews in the
   gridview in Results-Activity.
   All credits to a.bertucci (see resource below)
   Resource: http://stackoverflow.com/questions/16506275 */

package nl.mprog.mume.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {


    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
