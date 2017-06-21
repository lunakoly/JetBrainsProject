package ru.luna_koly.jetbrainsproject.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import ru.luna_koly.jetbrainsproject.GameRegistry;
import ru.luna_koly.jetbrainsproject.GameRenderer;
import ru.luna_koly.jetbrainsproject.basic_shapes.SceneObject;
import ru.luna_koly.jetbrainsproject.basic_shapes.entity.Human;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene;

/**
 * Created with love by luna_koly on 19.06.17.
 */

public class FileLoader {
    private static final String TAG = "file_loader";
    private static final String TEXTURES_PATH = "textures/";
    private static final String SHADER_PATH = "shader/";
    private static final String SCENES_PATH = "scenes/";

    private static String readStream(InputStream is) {
        Scanner scn = new Scanner(is);
        String out = "";

        while (scn.hasNextLine()) {
            out += scn.nextLine();
        }

        return out;
    }

    private static InputStream streamFile(Context context, String path) {
        try {
            return context.getAssets().open(path);
        } catch (IOException e) {
            Log.d(TAG, "Failed loading file at \'" + path + "\'");
            e.printStackTrace();
        }

        return null;
    }

    private static String readFile(Context context, String path) {
        return readStream(streamFile(context, path));
    }

    public static String readShaderSource(Context context, String path) {
        return readFile(context, SHADER_PATH + path);
    }

    public static Bitmap readBitmap(Context context, String path) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(TEXTURES_PATH + path));
        } catch (IOException e) {
            Log.e(TAG, "Unable to load file " + path);
            return null;
        }
    }

    public static int bitmapToTexture(Bitmap bitmap) {
        int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            GLES20.glEnable(GLES20.GL_BLEND);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        } else {
            Log.e(TAG, "Error loading texture.");
        }

        return textureHandle[0];
    }

    public static int loadShader(int type, String source) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);

        int[] status = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);

        if (status[0] == 0) {
            Log.e(TAG, "Couldn't load " + type);
            Log.e(TAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0)
            Log.e(TAG, "Loading shader trouble : " + type);

        return shader;
    }

    public static int loadProgram(int vertexShader, int fragmentShader) {
        int program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glLinkProgram(program);

        int[] status = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, status, 0);

        if (status[0] == 0) {
            Log.e(TAG, "Couldn't load shader program");
            Log.e(TAG, GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            program = 0;
        }

        if (program == 0)
            Log.e(TAG, "Loading program trouble");

        return program;
    }

    public static Scene loadScene(Context context, String path) {
        try {
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setInput(streamFile(context, SCENES_PATH + path + ".xml"), null);
            return readScene(context, xpp, path);

        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Error loading scene " + path);
            e.printStackTrace();

        }

        return new Scene(0, 0, 0);
    }

    private static Scene readScene(Context context, XmlPullParser xpp, String id) throws IOException, XmlPullParserException {
        ArrayList<SceneObject> objects = new ArrayList<>();
        ArrayList<Human>        humans = new ArrayList<>();
        Scene scene = null;

        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (xpp.getEventType()) {
                case XmlPullParser.START_DOCUMENT: break;
                case XmlPullParser.START_TAG:

                    switch (xpp.getName()) {
                        case "scene":
                            scene = decodeSceneTag(context, xpp);
                            break;

                        case "obj":
                            objects.add(decodeObjectTag(context, xpp));
                            break;

                        case "human":
                            Human h = decodeHumanTag(context, xpp);
                            if (h != null)
                                humans.add(h);
                            break;
                    }

                    break;
            }

            xpp.next();
        }

        if (scene == null)
            scene = new Scene(0, 0, 0);

        GameRegistry.addScene(scene, id);

        for (SceneObject o : objects)
            scene.add(o);

        for (Human h : humans)
            scene.add(h);

        return scene;
    }

    private static SceneObject decodeObjectTag(Context context, XmlPullParser xpp) {
        String texturePath = "";
        String shaderId = "";
        SceneObject object;

        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            switch (xpp.getAttributeName(i)) {
                case "tex":
                    texturePath = xpp.getAttributeValue(i) + ".png";
                    break;
                case "shader":
                    shaderId = xpp.getAttributeValue(i);
            }
        }

        if (texturePath.length() > 0 &&
                shaderId.length() > 0) {
            object = new SceneObject(context, GameRenderer.getShaderProgram(shaderId), texturePath);
        } else {
            object = new SceneObject(context, 2, 2, GameRenderer.getDefaultShaderProgram());
        }

        return object;
    }

    private static Scene decodeSceneTag(Context context, XmlPullParser xpp) {
        Scene scene = new Scene(0, 0, 0);
        SceneObject object = decodeObjectTag(context, xpp);
        scene.add(object);
        scene.cropToObject(object);
        return scene;
    }

    private static Human decodeHumanTag(Context context, XmlPullParser xpp) {
        String texturePath = "";
        String shaderId = "";
        String name = "";
        float dx = 0;
        float dy = 0;
        Human human;

        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            switch (xpp.getAttributeName(i)) {
                case "tex":
                    texturePath = "char/" + xpp.getAttributeValue(i) + "_standing.png";
                    break;
                case "who":
                    name = xpp.getAttributeValue(i);
                    break;
                case "pos":
                    String[] str = xpp.getAttributeValue(i).split(" ");
                    dx = Float.parseFloat(str[0]);
                    dy = Float.parseFloat(str[1]);
                    break;
                case "shader":
                    shaderId = xpp.getAttributeValue(i);
            }
        }

        if (texturePath.length() > 0 &&
                name.length() > 0) {
            human = new Human(context, name, texturePath);
            if (dx != 0) human.moveX(dx);
            if (dy != 0) human.moveY(dy);
            if (shaderId.length() > 0) human.setShaderProgram(GameRenderer.getShaderProgram(shaderId));

        } else {
            human = null;
        }

        return human;
    }

}