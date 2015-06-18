package grohden.recentsmod;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Gabriel on 29/03/2015.
 * Used to animate width and height of a view
 */
public class ViewAnimator {

    private View view;
    private Interpolator interpolator = new AccelerateDecelerateInterpolator();

    public ViewAnimator(View v) {
        view=v;
    }

    public void setInterpolator(Interpolator newInterpolator){
        interpolator=newInterpolator;
    }

    public void animateWidth(int intialPos, int finalPos){
        ValueAnimator widthAnimator = ValueAnimator.ofInt(intialPos, finalPos);

        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                view.getLayoutParams().width = value;
                view.requestLayout();
            }
        });
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.play(widthAnimator);
        animatorSet.setInterpolator(interpolator);
        animatorSet.start();
    }

    public void animateHeight(int intialPos, int finalPos){
        ValueAnimator heightAnimator = ValueAnimator.ofInt(intialPos, finalPos);

        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                view.getLayoutParams().height = value;
                view.requestLayout();
            }
        });
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.play(heightAnimator);
        animatorSet.setInterpolator(interpolator);
        animatorSet.start();
    }



}
