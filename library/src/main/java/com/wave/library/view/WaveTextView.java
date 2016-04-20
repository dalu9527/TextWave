package com.wave.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.wave.library.R;
import com.wave.library.util.ScreenUtil;
import com.wave.library.util.StringUtil;

import java.lang.ref.WeakReference;

/**
 * use the formula: y = A * sin(wx) to draw the wave shape
 * as we all know, w is the cycle, A is the amplitudes
 * Created by cherish on 2016/4/18.
 */
public class WaveTextView extends View {
    private final static float AMPLITUDES = 12;
    protected boolean mStarted = false;
    private Context mContext;
    private Paint mTextPaint;
    private float mWaveAmplitudes = 12;
    private float mWaveSpeed = 0.5f;
    private float mTextSize = 36;

    private int mStrLength;

    private String[] mStr;
    private String mString;
    private int mPaddingLeft;
    private int mViewWidth;
    private int mViewHeight;

    private float[] mYPos;
    private float[] mResetYPos;
    private int mXOffset;
    private MyHandler myHandler;
    private Path mPath;

    public WaveTextView(Context context) {
        super(context);
        mContext = context;
        int textColor = Color.RED;
        init(textColor, mTextSize, mWaveSpeed);
    }

    public WaveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        TypedArray typedarray = context.obtainStyledAttributes(attrs,
                R.styleable.WaveTextView);

        mWaveAmplitudes = typedarray.getInt(R.styleable.WaveTextView_waveAmplitudes,
                0);
        mWaveSpeed = typedarray.getFloat(R.styleable.WaveTextView_waveSpeed, 0.5f);
        int textColor = typedarray.getColor(R.styleable.WaveTextView_textColor,
                0xFFFF0000);
        mTextSize = typedarray.getDimension(R.styleable.WaveTextView_textSize, 36);
        typedarray.recycle();
        init(textColor, mTextSize, mWaveSpeed);
    }

    public WaveTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray typedarray = context.obtainStyledAttributes(attrs,
                R.styleable.WaveTextView);

        mWaveAmplitudes = typedarray.getInt(R.styleable.WaveTextView_waveAmplitudes,
                0);
        mWaveSpeed = typedarray.getFloat(R.styleable.WaveTextView_waveSpeed, 0.5f);
        int textColor = typedarray.getColor(R.styleable.WaveTextView_textColor,
                0xFFFF0000);
        mTextSize = typedarray.getDimension(R.styleable.WaveTextView_textSize, 36);
        typedarray.recycle();
        init(textColor, mTextSize, mWaveSpeed);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        // deal with the padding
        mPaddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        mViewWidth = getWidth() - mPaddingLeft - paddingRight;
        mViewHeight = getHeight() - paddingTop - paddingBottom;
        // save the y position
        mYPos = new float[mStrLength];
        // save the rest y position
        mResetYPos = new float[mStrLength];
        // set the cycle
        float cycle = (float) (2 * Math.PI / mStrLength);

        if(mWaveAmplitudes == 0){
            mWaveAmplitudes = AMPLITUDES;
        }
        if (mWaveAmplitudes <= mViewHeight) {
            for (int i = 0; i < mStrLength; i++) {
                mYPos[i] = (float) (mWaveAmplitudes * Math.sin(cycle * i));
            }
        }else {
            throw new IllegalStateException("not set amplitudes higher than the TextView's height");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        runWaveTextView();
        int x;
        int y;
        mPath.reset();
        if(mStrLength != 0){
            float textWidth = mTextPaint.measureText(mString);
            if(textWidth < mViewWidth){ // one column
                /**the first method to solve the problem by use drawText*/
                int step = mViewWidth / mStrLength;
                int j = 0;
                for (int i = 0; i < mViewWidth; i++) {
                    if(i == (j * step) && j < mStrLength){
                        canvas.drawText(mStr[j], i + mPaddingLeft, mViewHeight / 2 - mResetYPos[j], mTextPaint);
                        j ++;
                    }
                }
                /**the second method to solve the problem by use drawTextOnPath*/
//                for (int i = 0; i < mResetYPos.length; i++) {
//                    x = i * (int)mTextSize;
//                    y = (int)mResetYPos[i];
//                    if (i == 0) {
//                        mPath.moveTo(x, y);
//                    }
//                    mPath.quadTo(x, y, x + 1, y);
//                }
//                canvas.drawTextOnPath(mString, mPath,  mPaddingLeft, mViewHeight / 2, mTextPaint);
            }else{// more than one column
                int n = 0;
                int column = (mStrLength * mTextSize) % mViewWidth == 0 ? (int)((mStrLength * mTextSize) / mViewWidth) : (int)((mStrLength * mTextSize) / mViewWidth) + 1;
                for(int i = 0; i < column; i ++){
                    int m = 0;
                    for (int j = 0; j < mViewWidth; j++) {
                        if (j == (m * mTextSize) && n < mStrLength && (m * mTextSize) < (mViewWidth - mTextSize)) {
                            canvas.drawText(mStr[n], j + mPaddingLeft, mViewHeight * (i + 1) / (column + 1) - mResetYPos[n], mTextPaint);
                            m ++;
                            n ++;
                        }
                    }
                }
            }
        }

        // change the offset
        mXOffset += mWaveSpeed;
        // reset the offset
        if (mXOffset >= mStrLength) {
            mXOffset = 0;
        }
    }

    /**
     * start wave
     */
    public void startWave() {
        if (!mStarted) {
            mStarted = true;
            myHandler.sendEmptyMessage(0);
        }
    }

    /**
     * stop wave
     */
    public void stopWave() {
        if (mStarted) {
            mStarted = false;
            myHandler.removeMessages(0);
        }
    }

    public String getString() {
        return mString;
    }

    public void setString(String string) {
        mString = string;
        mStr = StringUtil.toStringArray(mString);
        mStrLength = mStr.length;
    }

    /**
     * init relative parameters
     */
    private void init(int paintColor, float paintSize, float waveSpeed) {
        mStr = StringUtil.toStringArray(getString());
        mStrLength = mStr.length;
        mTextPaint = new Paint();
        mTextPaint.setColor(paintColor);
        mTextPaint.setTextSize(paintSize);
        mWaveSpeed = ScreenUtil.dip2px(mContext, waveSpeed);
        myHandler = new MyHandler(WaveTextView.this);
        mPath = new Path();
    }

    /**
     * change the array to let the wave look like start
     */
    private void runWaveTextView() {
        int length = mYPos.length - mXOffset;
        System.arraycopy(mYPos, mXOffset, mResetYPos, 0, length);
        System.arraycopy(mYPos, 0, mResetYPos, length, mXOffset);
    }

    private static class MyHandler extends Handler {
        WeakReference<WaveTextView> mTextViewReference;

        MyHandler(WaveTextView waveTextView) {
            mTextViewReference= new WeakReference<WaveTextView>(waveTextView);
        }

        @Override
        public void handleMessage(Message msg) {
            final WaveTextView waveTextView = mTextViewReference.get();
            if (waveTextView != null) {
                if (msg.what == 0) {
                    waveTextView.invalidate();
                    if (waveTextView.mStarted) {
                        sendEmptyMessageDelayed(0, 60L);
                    }
                }
            }
        }
    }
}
