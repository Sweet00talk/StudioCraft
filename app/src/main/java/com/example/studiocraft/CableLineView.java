package com.example.studiocraft;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CableLineView extends View {

    private final Paint outerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint corePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint headPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final List<CableConnection> correctCables = new ArrayList<>();

    private PointF tempStart;
    private PointF tempEnd;

    private PointF wrongStart;
    private PointF wrongEnd;

    public CableLineView(Context context) {
        super(context);
        init();
    }

    public CableLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CableLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        outerPaint.setStyle(Paint.Style.STROKE);
        outerPaint.setStrokeCap(Paint.Cap.ROUND);
        outerPaint.setStrokeWidth(dp(8));
        outerPaint.setColor(Color.parseColor("#111111"));

        glowPaint.setStyle(Paint.Style.STROKE);
        glowPaint.setStrokeCap(Paint.Cap.ROUND);
        glowPaint.setStrokeWidth(dp(7));

        corePaint.setStyle(Paint.Style.STROKE);
        corePaint.setStrokeCap(Paint.Cap.ROUND);
        corePaint.setStrokeWidth(dp(3));

        headPaint.setStyle(Paint.Style.FILL);
    }

    public void addCorrectCable(PointF start, PointF end) {
        correctCables.add(new CableConnection(new PointF(start.x, start.y), new PointF(end.x, end.y)));
        invalidate();
    }

    public void startTemporaryCable(PointF start, PointF end) {
        tempStart = new PointF(start.x, start.y);
        tempEnd = new PointF(end.x, end.y);
        invalidate();
    }

    public void updateTemporaryCable(PointF end) {
        if (tempEnd != null) {
            tempEnd.set(end.x, end.y);
            invalidate();
        }
    }

    public void clearTemporaryCable() {
        tempStart = null;
        tempEnd = null;
        invalidate();
    }

    public void showWrongCable(PointF start, PointF end) {
        wrongStart = new PointF(start.x, start.y);
        wrongEnd = new PointF(end.x, end.y);
        invalidate();
    }

    public void clearWrongCable() {
        wrongStart = null;
        wrongEnd = null;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (CableConnection cable : correctCables) {
            drawCable(canvas, cable.start, cable.end, Color.parseColor("#39FF14"), true);
        }

        if (wrongStart != null && wrongEnd != null) {
            drawCable(canvas, wrongStart, wrongEnd, Color.parseColor("#FF304F"), true);
        }

        if (tempStart != null && tempEnd != null) {
            drawCable(canvas, tempStart, tempEnd, Color.parseColor("#00E8FF"), false);
        }
    }

    private void drawCable(Canvas canvas, PointF start, PointF end, int color, boolean glow) {
        canvas.drawLine(start.x, start.y, end.x, end.y, outerPaint);

        glowPaint.setColor(color);
        if (glow) {
            glowPaint.setShadowLayer(dp(14), 0, 0, color);
        } else {
            glowPaint.clearShadowLayer();
        }
        canvas.drawLine(start.x, start.y, end.x, end.y, glowPaint);

        corePaint.setColor(color);
        canvas.drawLine(start.x, start.y, end.x, end.y, corePaint);

        drawConnectorHead(canvas, start, end, color);
    }

    private void drawConnectorHead(Canvas canvas, PointF start, PointF end, int color) {
        float dx = end.x - start.x;
        float dy = end.y - start.y;
        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));

        canvas.save();
        canvas.translate(end.x, end.y);
        canvas.rotate(angle);

        headPaint.setColor(Color.parseColor("#111111"));
        RectF outerHead = new RectF(-dp(18), -dp(7), dp(2), dp(7));
        canvas.drawRoundRect(outerHead, dp(3), dp(3), headPaint);

        headPaint.setColor(color);
        RectF innerHead = new RectF(-dp(14), -dp(4), -dp(2), dp(4));
        canvas.drawRoundRect(innerHead, dp(2), dp(2), headPaint);

        canvas.restore();
    }

    private float dp(float value) {
        return value * getResources().getDisplayMetrics().density;
    }

    private static class CableConnection {
        PointF start;
        PointF end;

        CableConnection(PointF start, PointF end) {
            this.start = start;
            this.end = end;
        }
    }
}