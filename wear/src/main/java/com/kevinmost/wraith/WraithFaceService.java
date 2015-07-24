package com.kevinmost.wraith;

import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import com.kevinmost.wraith.hand.WraithHand;
import com.kevinmost.wraith.event.Event;

import java.util.Calendar;
import java.util.List;

public class WraithFaceService extends BaseFaceService {

  @Override
  public Engine onCreateEngine() {
    return new Engine();
  }

  class Engine extends BaseFaceService.Engine {

    @Override
    public void onDraw(Canvas canvas, Rect bounds) {
      super.onDraw(canvas, bounds);

      // Erase the canvas' last state
      canvas.drawColor(0, PorterDuff.Mode.CLEAR);

      calendar.setTimeInMillis(System.currentTimeMillis());

      final float centerX = bounds.width() / 2F;
      final float centerY = bounds.height() / 2F;

      final float seconds =
          calendar.get(Calendar.SECOND) + calendar.get(Calendar.MILLISECOND) / 1000F;
      final float minutes = calendar.get(Calendar.MINUTE) + seconds / 60F;
      final float hours = calendar.get(Calendar.HOUR) + minutes / 60F;

      WraithHand.HOUR.drawToCanvas(canvas, centerX, centerY, hours);
      WraithHand.MINUTE.drawToCanvas(canvas, centerX, centerY, minutes);

      // The circle that is sandwiched between the min/hour hands and second hand on a real watch.
      canvas.drawCircle(centerX, centerY, 7, WraithHand.HOUR.getPaint());
      canvas.drawCircle(centerX, centerY, 3, WraithHand.SECOND.getPaint());

      if (!isInAmbientMode()) {
        // We don't want seconds to display in-between integer values. It looks weird.
        WraithHand.SECOND.drawToCanvas(canvas, centerX, centerY, (int) seconds);
      }
    }

    public void onCalendarEventsLoaded(List<Event> events) {
      if (events != null) {
        // TODO: Do the things with the events
      }
      if (isVisible()) {
        updateHandler.sendEmptyMessageDelayed(MSG_LOAD_CALENDAR_EVENTS, 1000 * 60);
      }
    }
  }
}
