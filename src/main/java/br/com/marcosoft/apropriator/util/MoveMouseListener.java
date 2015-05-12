package br.com.marcosoft.apropriator.util;

import java.awt.Container;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class MoveMouseListener implements MouseListener, MouseMotionListener {
    private final JComponent target;

    private Point start_drag;

    private Point start_loc;

    public MoveMouseListener(JComponent target) {
        this.target = target;
    }

    public static Window getWindow(Container target) {
        if (target instanceof Window) {
            return (Window) target;
        }
        return getWindow(target.getParent());
    }

    Point getScreenLocation(MouseEvent e) {
        final Point cursor = e.getPoint();
        final Point target_location = this.target.getLocationOnScreen();
        return new Point((int) (target_location.getX() + cursor.getX()),
            (int) (target_location.getY() + cursor.getY()));
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        this.start_drag = this.getScreenLocation(e);
        this.start_loc = getWindow(this.target).getLocation();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        final Point current = this.getScreenLocation(e);
        final Point offset = new Point((int) current.getX() - (int) start_drag.getX(),
            (int) current.getY() - (int) start_drag.getY());
        final Window frame = getWindow(target);
        final Point new_location = new Point(
            (int) (this.start_loc.getX() + offset.getX()),
            (int) (this.start_loc.getY() + offset.getY()));
        frame.setLocation(new_location);
    }

    public void mouseMoved(MouseEvent e) {
    }
}