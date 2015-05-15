/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.marcosoft.apropriator.util;

import java.awt.GraphicsConfiguration;
import java.awt.Shape;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anthony Petrov
 */
public class AWTUtilitiesWrapper {

    private static Class<?> awtUtilitiesClass;
    private static Class<?> translucencyClass;
    private static Method mIsTranslucencySupported,  mIsTranslucencyCapable,  mSetWindowShape,  mSetWindowOpacity,  mSetWindowOpaque;
    public static Object PERPIXEL_TRANSPARENT,  TRANSLUCENT,  PERPIXEL_TRANSLUCENT;

    static void init() {
        try {
            awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
            translucencyClass = Class.forName("com.sun.awt.AWTUtilities$Translucency");
            if (translucencyClass.isEnum()) {
                final Object[] kinds = translucencyClass.getEnumConstants();
                if (kinds != null) {
                    PERPIXEL_TRANSPARENT = kinds[0];
                    TRANSLUCENT = kinds[1];
                    PERPIXEL_TRANSLUCENT = kinds[2];
                }
            }
            mIsTranslucencySupported = awtUtilitiesClass.getMethod("isTranslucencySupported", translucencyClass);
            mIsTranslucencyCapable = awtUtilitiesClass.getMethod("isTranslucencyCapable", GraphicsConfiguration.class);
            mSetWindowShape = awtUtilitiesClass.getMethod("setWindowShape", Window.class, Shape.class);
            mSetWindowOpacity = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class, float.class);
            mSetWindowOpaque = awtUtilitiesClass.getMethod("setWindowOpaque", Window.class, boolean.class);
        } catch (final NoSuchMethodException ex) {
            Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final SecurityException ex) {
            Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final ClassNotFoundException ex) {
            Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static {
        init();
    }

    public static void setOpacity(Window window, float opacity) {
    	final boolean isOpacityControlSupported = AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.TRANSLUCENT);
    	if (isOpacityControlSupported) {
    		AWTUtilitiesWrapper.setWindowOpacity(window, opacity);
    	}
//        boolean isShapingSupported = AWTUtilitiesWrapper.isTranslucencySupported(AWTUtilitiesWrapper.PERPIXEL_TRANSPARENT);
//        if (isShapingSupported) {
//            Shape shape = new RoundRectangle2D.Float(0, 0, this.getWidth(), this.getHeight(), 30, 30);
//            AWTUtilitiesWrapper.setWindowShape(this, shape);
//        }
    	
    }
    public static void setOpacity(Window window) {
    	setOpacity(window, 0.85f);
    }


    private static boolean isSupported(Method method, Object kind) {
        if (awtUtilitiesClass == null ||
                method == null)
        {
            return false;
        }
        try {
            final Object ret = method.invoke(null, kind);
            if (ret instanceof Boolean) {
                return ((Boolean)ret).booleanValue();
            }
        } catch (final IllegalAccessException ex) {
            Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final IllegalArgumentException ex) {
            Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final InvocationTargetException ex) {
            Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean isTranslucencySupported(Object kind) {
        if (translucencyClass == null) {
            return false;
        }
        return isSupported(mIsTranslucencySupported, kind);
    }

    public static boolean isTranslucencyCapable(GraphicsConfiguration gc) {
        return isSupported(mIsTranslucencyCapable, gc);
    }

    private static void set(Method method, Window window, Object value) {
        if (awtUtilitiesClass == null ||
                method == null)
        {
            return;
        }
        try {
            method.invoke(null, window, value);
        } catch (final IllegalAccessException ex) {
            Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final IllegalArgumentException ex) {
            Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (final InvocationTargetException ex) {
            Logger.getLogger(AWTUtilitiesWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setWindowShape(Window window, Shape shape) {
        set(mSetWindowShape, window, shape);
    }

    public static void setWindowOpacity(Window window, float opacity) {
        set(mSetWindowOpacity, window, Float.valueOf(opacity));
    }

    public static void setWindowOpaque(Window window, boolean opaque) {
        set(mSetWindowOpaque, window, Boolean.valueOf(opaque));
    }
}