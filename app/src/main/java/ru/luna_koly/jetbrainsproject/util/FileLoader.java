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
import ru.luna_koly.jetbrainsproject.basic_shapes.entity.Player;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Scene;
import ru.luna_koly.jetbrainsproject.basic_shapes.util.Texture;
import ru.luna_koly.jetbrainsproject.dialogs.Dialog;
import ru.luna_koly.jetbrainsproject.dialogs.Replica;
import ru.luna_koly.jetbrainsproject.graph.Graph;
import ru.luna_koly.jetbrainsproject.graph.GraphVertex;
import ru.luna_koly.jetbrainsproject.util.containers.Vector2D;
import ru.luna_koly.jetbrainsproject.util.containers.vec2;
import ru.luna_koly.jetbrainsproject.util.containers.vec3;

/**
 * Created with love by luna_koly on 19.06.17.
 */

public class FileLoader {
    private static final String TAG = "file_loader";
    private static final String TEXTURES_PATH = "textures/";
    private static final String SHADER_PATH = "shader/";
    private static final String SCENES_PATH = "scenes/";
    private static final String DIALOGS_PATH = "dialogs/";
    private static final String GRAPHS_PATH = "graphs/";

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

    static Scene loadScene(Context context, String path) {
        try {
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setInput(streamFile(context, SCENES_PATH + path + ".xml"), null);
            return readScene(context, xpp, path);

        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Error loading scene " + path);
            e.printStackTrace();

        }

        return new Scene(context, 0, 0, 0);
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
            scene = new Scene(context, 0, 0, 0);

        GameRegistry.addScene(scene, id);
        scene.setGraph(FileLoader.loadGraph(context, id));

        if (scene.getGraph().getMode().equals("debug"))
            scene.lightGraph();

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
            object = new SceneObject(context, GameRenderer.getShaderProgram(shaderId), new Texture(context, texturePath, 1, 1000));
        } else {
            object = new SceneObject(context, 2, 2, GameRenderer.getDefaultShaderProgram());
        }

        return object;
    }

    private static Scene decodeSceneTag(Context context, XmlPullParser xpp) {
        Scene scene = new Scene(context, 0, 0, 0);
        SceneObject object = decodeObjectTag(context, xpp);
        scene.add(object);
        scene.cropToObject(object);
        return scene;
    }

    private static Human decodeHumanTag(Context context, XmlPullParser xpp) {
        String texturePath = "";
        String shaderId = "";
        String dialogPath = "";
        String name = "";
        float dx = 0;
        float dy = 0;
        Human human;

        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            switch (xpp.getAttributeName(i)) {
                case "tex":
                    dialogPath = xpp.getAttributeValue(i);
                    texturePath = "char/" + dialogPath + "_standing.png";
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

        Dialog d = loadDialog(context, dialogPath);

        if (texturePath.length() > 0 &&
                name.length() > 0) {
            human = new Human(context, name, new Texture(context, texturePath, 1, 1000));
            if (dx != 0) human.moveX(dx);
            if (dy != 0) human.moveY(dy);
            if (shaderId.length() > 0) human.setShaderProgram(GameRenderer.getShaderProgram(shaderId));
            if (!d.isEmpty()) human.addDialog(d);

        } else {
            human = null;
        }

        return human;
    }

    public static Dialog loadDialog(Context context, String path) {
        try {
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setInput(streamFile(context, DIALOGS_PATH + path + ".xml"), null);
            return readDialog(context, xpp, path);

        } catch (XmlPullParserException | IOException | IllegalArgumentException e) {
            Log.e(TAG, "Error loading dialog " + path);
            e.printStackTrace();
        }

        return new Dialog();
    }

    private static Dialog readDialog(Context context, XmlPullParser xpp, String id) throws XmlPullParserException, IOException {
        Dialog dialog = new Dialog();

        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (xpp.getEventType()) {
                case XmlPullParser.START_DOCUMENT: break;
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("rep")) {
                        dialog = addReplica(context, xpp, dialog);
                    }
            }

            xpp.next();
        }

        GameRegistry.addDialog(dialog, id);
        return dialog;
    }

    private static Dialog addReplica(Context context, XmlPullParser xpp, Dialog dialog) {
        String id = "";
        Replica r = new Replica();

        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            switch (xpp.getAttributeName(i)) {
                case "who":
                    r.person = xpp.getAttributeValue(i);
                    break;

                case "id":
                    id = xpp.getAttributeValue(i);
                    break;

                case "next":
                    String[] str = xpp.getAttributeValue(i).split(" ");
                    r.nextReplicas = new String[str.length];
                    for (int j = 0; j < str.length; j++)
                        r.nextReplicas[j] = str[j];
                    break;

                case "text":
                    r.text = xpp.getAttributeValue(i);
            }
        }

        if (r.person.equals("nobody")) r.person = "Charly";

        if (id.length() == 0)
            return dialog;

        dialog.put(id, r);
        return dialog;
    }

    public static Graph loadGraph(Context context, String path) {
        try {
            XmlPullParser xpp = Xml.newPullParser();
            xpp.setInput(streamFile(context, GRAPHS_PATH + path + ".xml"), null);
            return readGraph(context, xpp, path);

        } catch (XmlPullParserException | IOException | IllegalArgumentException e) {
            Log.e(TAG, "Error loading graph " + path);
            e.printStackTrace();
        }

        return new Graph();
    }

    private static Graph readGraph(Context context, XmlPullParser xpp, String path) throws XmlPullParserException, IOException {
        Graph graph = new Graph();

        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (xpp.getEventType()) {
                case XmlPullParser.START_DOCUMENT: break;
                case XmlPullParser.START_TAG:

                    switch (xpp.getName()) {
                        case "graph":
                            graph.setMode(addMode(context, xpp));
                            break;
                        case "vert":
                            graph = addVertex(context, xpp, graph, path);
                    }
            }

            xpp.next();
        }

        return graph;
    }

    private static String addMode(Context context, XmlPullParser xpp) {
        for (int i = 0; i < xpp.getAttributeCount(); i++)
            if (xpp.getAttributeName(i).equals("mode"))
                return xpp.getAttributeValue(i);

        return "";
    }

    private static Graph addVertex(Context context, XmlPullParser xpp, Graph graph, String id) {
        String[] str;
        final GraphVertex r = new GraphVertex();

        for (int i = 0; i < xpp.getAttributeCount(); i++) {
            switch (xpp.getAttributeName(i)) {
                case "pos":
                    str = xpp.getAttributeValue(i).split(" ");
                    r.position = new vec2(
                            Float.parseFloat(str[0]),
                            Float.parseFloat(str[1]));
                    break;

                case "id":
                    r.id = xpp.getAttributeValue(i);
                    break;

                case "next":
                    str = xpp.getAttributeValue(i).split(" ");
                    r.nextVertices = new String[str.length];
                    for (int j = 0; j < str.length; j++)
                        r.nextVertices[j] = str[j];
                    break;

                case "to":
                    str = xpp.getAttributeValue(i).split(" ");
                    final String[] finalStr = str;
                    final Scene current = GameRegistry.getScene(id);

                    current.addMovementDependency(new Runnable() {
                        @Override
                        public void run() {
                            Player p = current.getPlayer();
                            vec2 pos = new vec2(-p.getPosition().x, p.getPosition().y);

                            if (Vector2D.getDistanceOf2Points(pos, r.position) < 0.1) {
                                Scene scene = GameRegistry.getScene(finalStr[0]);
                                GameRegistry.runScene(scene);
                                scene.putPlayer(scene.getGraph().get(finalStr[1]).position);
                            }
                        }
                    });
            }
        }

        if (r.id.length() == 0)
            return graph;

        graph.put(r.id, r);
        return graph;
    }
}
