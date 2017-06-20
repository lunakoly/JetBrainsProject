precision mediump float;
uniform sampler2D uTextureSampler;
uniform vec2 uDimensions;
uniform vec2 uScreen;

varying vec2 vTextureCoord;
varying vec4 vPosition;

float scale(float f) {
    return f / 2.0 + 0.5;
}

void main() {
    vec2 tc = vTextureCoord;
    tc.x = tc.x * uDimensions.y / uDimensions.x * uScreen.x / uScreen.y;

    tc.x = scale(tc.x);
    tc.y = 1.0 - scale(tc.y);

    gl_FragColor = texture2D(uTextureSampler, tc);
}