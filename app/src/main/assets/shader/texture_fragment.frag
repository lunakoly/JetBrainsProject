precision mediump float;
uniform sampler2D uTextureSampler;

varying vec2 vTextureCoord;
varying vec4 vPosition;


void main() {
    gl_FragColor = texture2D(uTextureSampler, vTextureCoord);

    if (gl_FragColor.a == 0.0) {
        gl_FragColor.a = 1.0;
        gl_FragColor.r = 1.0;
    }
}